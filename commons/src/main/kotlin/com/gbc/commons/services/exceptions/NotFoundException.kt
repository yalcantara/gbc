package com.gbc.commons.services.exceptions

import com.gbc.commons.lang.GbcException

class NotFoundException:ServiceException {
    constructor(msg: String):super(msg)  {
    }

    constructor(msg: String, cause:Throwable):super(msg, cause)  {
    }
}