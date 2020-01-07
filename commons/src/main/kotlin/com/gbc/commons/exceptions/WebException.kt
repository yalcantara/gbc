package com.gbc.commons.exceptions

import com.gbc.commons.lang.GbcException

open class WebException:GbcException {
    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}