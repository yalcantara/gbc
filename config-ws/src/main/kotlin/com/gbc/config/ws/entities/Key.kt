package com.gbc.config.ws.entities

import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Key:Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?           = null
    var name:String?        = null
    var label: String?      = null
    var value: String?      = null
    var dateCreated: Date?  = null
    var dateUpdated: Date?  = null

    override fun toString(): String {
        return "Key(id=$id, " +
                "name='$name', " +
                "label='$label', " +
                "value='$value', " +
                "dateCreated=$dateCreated, " +
                "dateUpdated=$dateUpdated)"
    }
}