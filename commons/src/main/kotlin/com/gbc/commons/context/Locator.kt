package com.gbc.commons.context

class Locator {

    companion object{
        fun <T> get(clazz: Class<T>): T {
            return ContextHolder.get().getBean(clazz)
        }
    }

}