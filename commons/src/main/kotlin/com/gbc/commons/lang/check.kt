package com.gbc.commons.lang

fun checkParamNotNull(name: String, value: Any?){
    if(value == null){
        throw InvalidParameterException("The parameter '$name' can not be null.")
    }
}

fun checkRequired(name: String, value: String?):String{
    if(value == null){
        throw InvalidValueException("The value for $name can not be null.")
    }

    val ans = pack(value)
    if(ans == null){
        throw InvalidValueException("The value for $name can not be empty.")
    }

    return ans
}