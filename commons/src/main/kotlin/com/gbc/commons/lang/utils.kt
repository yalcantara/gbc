package com.gbc.commons.lang

import java.util.regex.Pattern
import java.io.PrintWriter
import java.io.StringWriter
import java.text.Normalizer
import java.util.ArrayList


private val MARKS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")


fun pack(str: String?): String? {
    if (str == null) {
        return null
    }

    val s = str.trim()

    if (s.isEmpty() || s.isBlank()) {
        return null
    }
    return s
}

fun steam(str: String?): String? {
    if (str == null) {
        return null
    }

    var ans = str.trim()

    ans = ans.replace("\r\n", "").replace('\n', '\u0000').trim()
    if (ans.isEmpty()) {
        return null
    }

    ans = Normalizer.normalize(ans, Normalizer.Form.NFD)
    ans = MARKS_PATTERN.matcher(ans).replaceAll("")
    ans = ans.toLowerCase()
    return ans
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




fun toList(iter:Iterable<String>):ArrayList<String> {
    val list = ArrayList<String>()

    for (str in iter) {
        list.add(str)
    }
    return list
}