package com.jamie1192.haveibeenpwned.utils

import android.graphics.Color

/**
 * Created by jamie1192 on 5/1/19.
 */
class ColorUtil {

    companion object {
        @JvmStatic
        fun blendColors(from : Int, to : Int, ratio : Float) : Int {
            val inverseRatio = 1f - ratio

            val r = Color.red(to) * ratio + Color.red(from) * inverseRatio
            val g = Color.green(to) * ratio + Color.green(from) * inverseRatio
            val b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio

            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
    }
}