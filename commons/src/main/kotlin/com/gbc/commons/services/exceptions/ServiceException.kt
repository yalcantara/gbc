package com.gbc.commons.services.exceptions

import com.gbc.commons.lang.GbcException

open class ServiceException:GbcException {

    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}