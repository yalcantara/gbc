package com.gbc.commons.lang

import java.lang.IllegalArgumentException

fun checkParamNotNull(name: String, value: Any?){
    if(value == null){
        throw IllegalArgumentException("The parameter $name can not be null.")
    }
}

fun checkParamNotEmpty(name: String, value: String?){
    checkParamNotNull(name, value)
    val packed = pack(value)
    if(packed == null){
        throw IllegalArgumentException("The parameter $name can not be empty.")
    }
}

fun checkParamNotEmpty(name: String, value: Map<*, *>){
    if(value.isEmpty()){
        throw IllegalArgumentException("The parameter $name can not be empty.")
    }
}

fun checkParamIsPositive(name: String, value: Long){
    if(value <= 0){
        throw IllegalArgumentException("The parameter $name must be positive. Got: $value.")
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