package com.gbc.config.ws.services

import com.gbc.commons.exceptions.BadRequestException
import java.lang.IllegalArgumentException

class Verifier {


    companion object{

        private fun isAlphaNumeric(char: Char):Boolean{
            return char in 'a'..'z' || char in '0'..'9'
        }

        private fun isNotAlphaNumeric(char:Char):Boolean{
            return !isAlphaNumeric(char)
        }

        fun notAllowed(char: Char):Boolean{
            return !isAllowed(char)
        }

        fun isAllowed(char: Char):Boolean{
            if (    isAlphaNumeric(char) ||
                    char in '.'..'-') {
                return true
            }

            return false;
        }


        private fun checkChars(name: String, str: String) {
            for (c in str) {

                if (notAllowed(c)) {
                    throw BadRequestException(
                        "Invalid $name. Only letter, digit, dot(.) and dash(-) characters are allowed."
                    )
                }
            }
        }

        fun validateName(value: String):String{
            return validate("name", value)
        }

        // Check some basis:
        // 1 - Start with letter or digit.
        // 2 - Allows only: letters, digits, dots(.) and dashes(-).
        // 3 - The dot(.) and dash(-) characters can not be sequential.
        // 4 - It must end with a letter of digit.
        fun validate(name: String, field: String): String {

            var id = field.toLowerCase().trim()
            if(id.isEmpty()){
                throw BadRequestException("Ivalid $name. It can not be empty.")
            }

            // 1 - Start with [a-z] or [0-9]
            if (isNotAlphaNumeric(id.first())) {
                throw BadRequestException(
                    "Invalid $name. It must start with a letter or digit."
                )
            }
            // 2 - Allows only: letters, digits, dots(.) and dashes(-).
            checkChars("name", id)
            // 3 - The dot(.) and dash(-) characters can not be sequential.
            for (idx in 1 until id.length) {
                // start in the 1 index so que can analyze the
                // current and the previous char.
                val crt = id[idx]
                val prev = id[idx - 1]
                if (crt == '.' || crt == '-') {
                    if (prev == '.' || prev == '-') {
                        throw BadRequestException(
                            "Invalid $name. It can not have two consecutive special characters ('.' or '-')."
                        )
                    }
                }

            }
            // 4 - ending with [a-z] or [0-9]
            if (isNotAlphaNumeric(id.last())) {
                throw BadRequestException(
                    "Invalid $name. It must end with a letter or digit."
                )
            }
            return id
        }
    }
}