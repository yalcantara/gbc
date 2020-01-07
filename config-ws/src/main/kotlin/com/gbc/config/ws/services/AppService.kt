package com.gbc.config.ws.services

import com.gbc.commons.exceptions.ConflictException
import com.gbc.commons.lang.checkParamIsPositive
import com.gbc.commons.lang.checkParamNotNull
import com.gbc.commons.lang.pack
import com.gbc.commons.services.AbstractService
import com.gbc.commons.structs.Sort.ASC
import com.gbc.config.ws.entities.App
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Lazy
@Service
class AppService:AbstractService<App> {

    constructor():super(App::class.java){

    }

    fun get(name: String):App{
        return getBy(fields = mapOf("name" to name))
    }

    fun exist(name: String):Boolean{
        return existBy(fields = mapOf("name" to name))
    }

    override fun list():List<App>{
        return listBy(sorts = mapOf("name" to ASC))
    }


    fun checkNameIsAvailable(name: String) {
        if (exist(name)) {
            throw ConflictException("The app '$name' already exists.")
        }
    }

    fun create(app: App): App {
        checkParamNotNull("app.name", app.name)
        var name = Verifier.validateName(app.name!!)

        checkNameIsAvailable(name)

        val ent = App()
        ent.id = null
        ent.name = name
        ent.label = pack(app.label)
        ent.dateCreated = Date()
        ent.createdBy = null
        ent.dateUpdated = null
        ent.updatedBy = null

        insert(ent)

        return ent
    }

    @Transactional
    fun patch(app: App):App{
        checkParamNotNull("app.id", app.id)
        checkParamIsPositive("app.id", app.id!!)
        checkParamNotNull("app.name", app.name)

        val id = app.id!!
        val name = Verifier.validateName(app.name!!)

        val ent = get(id)

        if(ent.name.equals(name) == false){
            // it's a rename, we have to check if the name is available
            checkNameIsAvailable(name);
        }

        ent.name = name
        ent.label = pack(app.label)
        ent.dateUpdated = Date()

        update(ent)
        return ent
    }


}