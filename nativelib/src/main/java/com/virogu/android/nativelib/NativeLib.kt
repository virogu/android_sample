package com.virogu.android.nativelib

class Data(
    var j_int: Int,
    val j_long: Long,
    var j_string: String,
    var j_float: Float,
    var j_short: Short,
    var j_double: Double,
    var j_boolean: Boolean,
    var j_char: Char,
    val j_byteArray: ByteArray
) {
    override fun toString(): String {
        return "${j_int}, ${j_long}, ${j_string}, ${j_float}, ${j_short}, ${j_double}, ${j_boolean}, ${j_char}, ${j_byteArray}."
    }
}

class NativeLib {

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }
    }

    external fun testObject(arg: Data): Data


}