package com.gbc.commons.context

import org.springframework.beans.BeansException

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component


@Component
class ContextHolder {

    companion object{
        @Volatile
        private var __ctx: ApplicationContext? = null

        fun isInitilized(): Boolean {
            return __ctx != null
        }

        fun get(): ApplicationContext {
            val ctx: ApplicationContext? = __ctx
            if (ctx == null) {
                throw RuntimeException(
                    "The application context is not set. " +
                            "Maybe this method was called way " +
                            "before the Spring Context had been initialized."
                )
            }
            return ctx
        }

        @Throws(BeansException::class)
        fun setApplicationContext(applicationContext: ApplicationContext?) {
            __ctx = applicationContext
        }
    }

}