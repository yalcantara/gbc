package com.gbc.commons.lang

import java.lang.RuntimeException

open class GbcException: RuntimeException {


    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}