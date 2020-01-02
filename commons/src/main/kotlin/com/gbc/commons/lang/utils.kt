package com.gbc.commons.lang

import java.util.regex.Pattern
import java.io.PrintWriter
import java.io.StringWriter
import java.text.Normalizer


private val MARKS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")


fun pack(str: String?): String? {
    if (str == null) {
        return null
    }

    val s = str.trim()

    if (s.isEmpty() || s.isBlank()) {
        return null;
    }
    return s
}

fun steam(str: String?): String? {
    if (str == null) {
        return null
    }

    var str = str.trim()

    str = str.replace("\r\n", "").replace('\n', '\u0000').trim()
    if (str.isEmpty()) {
        return null
    }

    str = Normalizer.normalize(str, Normalizer.Form.NFD)
    str = MARKS_PATTERN.matcher(str).replaceAll("")
    str = str.toLowerCase()
    return str
}



fun getStackTrace(t: Throwable?): String {
    if (t == null) {
        return "null"
    }

    // exception stack trace is mostly large.
    val sw = StringWriter(1024)
    val pw = PrintWriter(sw)
    t.printStackTrace(pw)

    return sw.toString()
}