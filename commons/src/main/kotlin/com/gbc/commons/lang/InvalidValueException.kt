package com.gbc.commons.lang

class InvalidValueException:GbcException {

    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}