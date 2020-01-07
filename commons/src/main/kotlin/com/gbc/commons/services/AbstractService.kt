package com.gbc.commons.services

import com.gbc.commons.context.Locator
import com.gbc.commons.entities.IEntity
import com.gbc.commons.lang.checkParamIsPositive
import com.gbc.commons.lang.checkParamNotEmpty
import com.gbc.commons.lang.checkParamNotNull
import com.gbc.commons.lang.pack
import com.gbc.commons.services.exceptions.NotFoundException
import com.gbc.commons.structs.Sort
import com.gbc.commons.structs.Sort.ASC
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.*
import javax.persistence.EntityManager
import kotlin.collections.LinkedHashMap

abstract class AbstractService<E:IEntity>  {

    protected val DEFAULT_LIST_LIMIT = 50
    protected val DEFAULT_SORT:Map<String, Sort> = mapOf("id" to ASC)

    protected val em: EntityManager by lazy {
        Locator.get(EntityManager::class.java)
    }

    private val entityName: String
    private val entityClass: Class<E>

    constructor(entityClass: Class<E>){
        this.entityClass = entityClass
        entityName = entityClass.simpleName
    }


    open fun count():Long{
        return countBy(fields = emptyMap())
    }

    protected fun existBy(fields: Map<String, Any?>):Boolean{
        return countBy(fields) > 0
    }

    protected fun countBy(fields: Map<String, Any?> = emptyMap()):Long{
        val jpql = if(fields.isEmpty()) {
            """
                select count(e)
                from $entityName e
            """
        } else {
            val and = _and(fields)
            """
                select count(e)
                from $entityName e
                where $and
            """
        }

        val q = em.createQuery(jpql)
        q.maxResults = 1
        return q.singleResult as Long
    }

    open fun find(id: Long):E?{
        checkParamIsPositive("id", id)
        return em.find(entityClass, id)
    }

    open fun get(id: Long):E{
        checkParamIsPositive("id", id)

        val ans = em.find(entityClass, id)
        if(ans == null){
            throw NotFoundException("Could not get entity $entityName with id $id.")
        }

        return ans
    }

    open fun list():List<E>{
        return listBy()
    }

    protected fun reference(id: Long):E{
        return em.getReference(entityClass, id)
    }

    protected fun insert(entity: E){
        em.persist(entity)
        em.flush()
    }

    protected fun insert(entities: List<E>){
        for(e in entities){
            em.persist(e)
        }
        em.flush()
    }

    protected fun update(entity: E){
        em.merge(entity)
        em.flush()
    }

    protected fun update(entities: List<E>){
        for(e in entities){
            em.merge(e)
        }
        em.flush()
    }

    protected fun getBy(fields: Map<String, Any?>):E{
        val list = listBy(fields = fields, limit = 1)
        if(list.isEmpty()){
            throw NotFoundException("Could not get entity $entityName with the given criteria.")
        }
        return list.first()
    }

    protected fun listBy(
        fields: Map<String, Any?> = emptyMap(),
        limit: Int = DEFAULT_LIST_LIMIT,
        sorts: Map<String, Sort> = DEFAULT_SORT
    ):List<E>{



        var jpql: String =
            """
                select e
                from $entityName e
            """

        if(fields.isNotEmpty()){
            val and = _and(fields)
            jpql += " where $and"
        }

        if(sorts.isNotEmpty()){
            val sort = _sort(sorts)
            jpql += " sort by $sort"
        }

        val q = em.createQuery(jpql)
        q.maxResults = DEFAULT_LIST_LIMIT
        for((k, v) in fields){
            if(v == null){
                continue
            }

            q.setParameter(k, v)
        }
        q.maxResults = limit
        return q.resultList as List<E>
    }


    protected fun checkField(value: Any?){
        checkParamNotNull("value", value)
        val casted = value.toString()

        var packed = pack(casted)
        if(packed == null){
            throw IllegalArgumentException("Invalid field '$value'")
        }

        for(c in packed){
            if(!allowedFieldChar(c)){
                throw IllegalArgumentException("Invalid field '$value'")
            }
        }
    }

    private fun allowedFieldChar(c:Char):Boolean{
        if(     ('a' >= c && c <= 'z') ||
                ('A' >= c && c <= 'Z') ||
                ('0' >= c && c <= '9') ||
                c == '.'){
            return true
        }

        return false
    }

    protected fun _and(fields: Map<String, Any?>):String{

        val jpql = StringBuilder()
        for((k, v) in fields){

            checkField(k) //important: to prevent any other statement injection.

            if(jpql.isNotEmpty()){
                jpql.append(", ")
            }
            jpql.append(k)
            if(v == null){
                jpql.append(" is null")
            }else{
                jpql.append(" = :$k")
            }
        }

        return jpql.toString()
    }

    protected fun _sort(sorts: Map<String, Sort>):String{

        val jpql = StringBuilder()
        for((k, v) in sorts){

            checkField(k) //important: to prevent any other statement injection.
            checkField(v) //important: to prevent any other statement injection.

            if(jpql.isNotEmpty()){
                jpql.append(", ")
            }
            jpql.append("$k $v")
        }

        return jpql.toString()
    }

    protected fun detach(entity: Any?){
        if(entity == null){
            return
        }

        if(entity is List<*>){
            detach(entity as List<*>)
        }else{
            em.detach(entity)
        }
    }

    protected fun detach(list: List<*>){
        if(list == null || list.isEmpty()){
            return
        }

        for(e in list){
            if(e == null){
                continue
            }
            em.detach(e)
        }
    }
}