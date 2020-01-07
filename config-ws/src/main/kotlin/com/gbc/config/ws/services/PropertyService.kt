package com.gbc.config.ws.services

import com.gbc.commons.services.AbstractService
import com.gbc.config.ws.entities.Property

class PropertyService:AbstractService<Property> {

    constructor():super(Property::class.java){

    }
}