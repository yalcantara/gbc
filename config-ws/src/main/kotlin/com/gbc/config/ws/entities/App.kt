package com.gbc.config.ws.entities

import com.gbc.commons.entities.IAudit
import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class App:IAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long?              = null
    var name:String?                    = null
    var label: String?                  = null
    override var dateCreated: Date?     = null
    override var createdBy: String?     = null
    override var dateUpdated: Date?     = null
    override var updatedBy: String?     = null

    override fun toString(): String {
        return "App(id=$id, " +
                "name='$name', " +
                "label='$label', " +
                "dateCreated=$dateCreated, " +
                "createdBy='$createdBy', " +
                "dateUpdated=$dateUpdated, " +
                "updatedBy='$updatedBy')"
    }


}