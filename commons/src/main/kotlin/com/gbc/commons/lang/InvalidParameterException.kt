package com.gbc.commons.lang

class InvalidParameterException:GbcException {

    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}