package configs

import tools.RuntimeTools
import java.text.SimpleDateFormat
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
object BuildVersions {

    private val runTime = Runtime.getRuntime()

    @Suppress("ConstantLocale")
    private val shortDateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())

    val gitCommitCount: Int get() = RuntimeTools.exec("git rev-list --all --count").toInt()

    val gitCommitShortId: String get() = RuntimeTools.exec("git rev-parse --short HEAD")

    val buildShortFormatDate: String get() = shortDateFormat.format(Date())

    val buildVersionCode: Int get() = buildShortFormatDate.toInt()

    val buildVersionName: String get() = "5.0.0"

}