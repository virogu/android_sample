package configs

import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

val gitCommitCount: Int
    get() = with(ByteArrayOutputStream()) {
        use { os ->
            exec {
                executable = "git"
                args = listOf("rev-list", "--all", "--count")
                standardOutput = os
            }
            val revision = os.toString().trim()
            return@with revision.toInt()
        }
    }

val buildFormatDate: String
    get() = with(SimpleDateFormat("yyMMdd")) {
        format(Date())
    }

val gitCommitShortId: String
    get() = with(ByteArrayOutputStream()) {
        use { os ->
            exec {
                executable = "git"
                args = listOf("rev-parse", "--short", "HEAD")
                standardOutput = os
            }
            return@with os.toString().trim()
        }
    }

val buildVersionCode: Int = buildFormatDate.toInt()

val buildVersionName: String = "5.0.0"