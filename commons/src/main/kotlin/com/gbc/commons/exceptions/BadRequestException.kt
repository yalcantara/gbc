package com.gbc.commons.exceptions

open class BadRequestException:WebException {
    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}