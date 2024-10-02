package com.regula.facesamplekotlin.util

import java.util.Collections
import java.util.Stack

class RandomColors {
    private val recycle: Stack<Int> = Stack()
    private val colors: Stack<Int> = Stack()

    init {
        recycle.addAll(
            mutableListOf(
                -0xe91e63, -0x9c27b0, -0x673ab7, -0x340000,
                -0x3f51b5, -0x2196f3, -0x3a9f4, -0xbcd4,
                -0x9688, -0x4caf50, -0x8bc34a, -0xcddc39,
                -0x795548, -0xe3dce, -0x3dcd0f, -0x9dcd0f,
                -0x3e0ece, -0x1e52f1, -0xa9f890, -0x1ee9a6,
                -0xff33ee, -0xffcf34, -0x33ff4f, -0x374af6
            )
        )
    }

    val color: Int
        get() {
            if (colors.size == 0) {
                while (!recycle.isEmpty()) colors.push(recycle.pop())
                Collections.shuffle(colors)
            }
            val c = colors.pop()
            recycle.push(c)
            return c
        }
}
