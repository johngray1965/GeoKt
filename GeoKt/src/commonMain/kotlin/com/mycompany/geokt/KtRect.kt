package com.mycompany.geokt

class KtRect(var left: Int, var top: Int, var right: Int, var bottom: Int) {
    val width: Int
        get() = right - left
    val height: Int
        get() = bottom - top

    fun set(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }
}
