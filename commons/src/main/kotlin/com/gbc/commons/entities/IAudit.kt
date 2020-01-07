package com.gbc.commons.entities

import java.io.Serializable
import java.util.*

interface IAudit: IEntity, Serializable {

    var dateCreated: Date?
    var dateUpdated: Date?
    var createdBy: String?
    var updatedBy: String?
}