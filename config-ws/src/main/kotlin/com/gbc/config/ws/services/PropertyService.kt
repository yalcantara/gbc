package com.gbc.config.ws.services

import com.gbc.commons.exceptions.BadRequestException
import com.gbc.commons.exceptions.ConflictException
import com.gbc.commons.lang.checkParamNotEmpty
import com.gbc.commons.lang.pack
import com.gbc.commons.services.AbstractService
import com.gbc.config.ws.entities.Property
import javassist.NotFoundException
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.inject.Inject


@Lazy
@Service
class PropertyService:AbstractService<Property> {

    @Inject
    private lateinit var appSrv: AppService

    constructor():super(Property::class.java){

    }

    fun list(app: String):List<Property>{
        return listBy(fields = mapOf("appId.name" to app))
    }

    fun get(app:String, name: String):Property{
        appSrv.checkExist(app)

        val list = listBy(fields = mapOf("appId.name" to app, "name" to name))
        if(list.isEmpty()){
            throw NotFoundException("The property '$name' does not exist.")
        }
        return list[0]
    }

    fun exist(app:String, name: String):Boolean{
        return existBy(fields = mapOf("appId.name" to app, "name" to name))
    }

    fun checkExist(app:String, name: String){
        if(exist(app, name) == false){
            throw BadRequestException("The property '$name' does not exist.")
        }
    }

    fun checkNameIsAvailable(app: String, name: String) {
        if (exist(app, name)) {
            throw ConflictException("The property '$name' already exists.")
        }
    }

    @Transactional
    fun create(app: String, prop: Property): Property {
        checkParamNotEmpty("app", app)
        checkParamNotEmpty("prop.name", prop.name)

        var name: String = Verifier.validateName(prop.name!!)

        appSrv.checkExist(app!!)

        checkNameIsAvailable(app, name)

        val ent = Property()
        ent.id = null
        ent.name = name
        ent.label = pack(prop.label)
        ent.createdBy = null
        ent.dateCreated = Date()
        ent.updatedBy = null
        ent.dateUpdated = null

        insert(ent)

        return ent
    }
}