package com.thanh_nguyen.image_compressor.constraint

import java.io.File

 interface Constraint {
    fun isSatisfied(imageFile: File): Boolean

    fun satisfy(imageFile: File): File
}