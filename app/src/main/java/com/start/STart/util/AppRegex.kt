package com.start.STart.util

object AppRegex {
    const val PASSWORD_VALIDATE = """^(?=.*[A-Za-z])(?=.*\d)(?=.*[${'$'}@${'$'}!%*#?~^<>,.&+=])[A-Za-z\d${'$'}@${'$'}!%*#?~^<>,.&+=]{8,16}${'$'}"""
    const val PHONE_VALIDATE = """^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})"""
}