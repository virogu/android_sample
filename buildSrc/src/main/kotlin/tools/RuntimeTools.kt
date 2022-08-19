package tools

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

object RuntimeTools {

    private val runtime = Runtime.getRuntime()

    fun exec(
        command: String,
        vararg envp: String
    ): String {
        try {
            val process = runtime.exec(command, envp)
            val result = inputStream2String(process.inputStream).trim()
            val error = inputStream2String(process.errorStream).trim()
            process.waitFor()
            if (error.isNotEmpty()) {
                return error
            }
            return result
        } catch (e: Throwable) {
            throw e
        }
    }

    private fun inputStream2String(inputStream: InputStream?): String {
        if (inputStream == null) return ""
        return InputStreamReader(inputStream, "GBK").use { inputReader ->
            BufferedReader(inputReader).use {
                it.lines().parallel().collect(Collectors.joining(System.lineSeparator()))
            }
        }
    }

}