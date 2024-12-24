package com.github.yukkimoru.sdtm.utility

object NumConv {
    fun intToRoman(num: Int): String {
        val romanNumerals = listOf(
            1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
            100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
            10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
        )
        var number = num
        val result = StringBuilder()
        for ((value, symbol) in romanNumerals) {
            while (number >= value) {
                result.append(symbol)
                number -= value
            }
        }
        return result.toString()
    }

    fun tickToTime(tick: Int): String {
        val second = tick / 20
        val minute = second / 60
        val hour = minute / 60
        return if(hour > 0) "${hour % 24}:${minute % 60}:${second % 60}"
        else{
            "${minute % 60}:${second % 60}"
        }
    }
}