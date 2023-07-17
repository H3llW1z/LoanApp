package com.panassevich.panassevich.util.loans.ui

fun getDateOnly(dateAndTime: String): String =
    dateAndTime.substringBefore('T')
        .split('-')
        .reversed()
        .joinToString(".")