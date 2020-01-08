package com.gbc.config.ws.entities

import com.gbc.commons.entities.IAudit
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
class Property: IAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long?              = null
    @JoinColumn
    @ManyToOne
    var appId: App?                     = null
    var name:String?                    = null
    var label: String?                  = null
    override var dateCreated: Date?     = null
    override var createdBy: String?     = null
    override var dateUpdated: Date?     = null
    override var updatedBy: String?     = null



    override fun toString(): String {
        return "Property(id=$id, " +
                "name=$name, " +
                "label=$label, " +
                "dateCreated=$dateCreated, " +
                "createdBy=$createdBy, " +
                "dateUpdated=$dateUpdated, " +
                "updatedBy=$updatedBy)"
    }


}