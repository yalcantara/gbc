package com.gbc.config.ws.services

import com.gbc.commons.services.AbstractService
import com.gbc.config.ws.entities.Property
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Lazy
@Service
class PropertyService:AbstractService<Property> {

    constructor():super(Property::class.java){

    }

    fun list(app: String):List<Property>{
        return listBy(fields = mapOf("appId.name" to app))
    }
}