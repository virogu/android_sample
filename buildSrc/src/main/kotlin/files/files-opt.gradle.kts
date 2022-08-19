package files

task("clearAllBuildFiles") {
    val fileDir = "D:\\F\\WorkSpace\\"
    group = "files operate"
    doFirst {
        val path = File(fileDir)
        if (path.exists() && path.isDirectory) {
            path.walk().filter {
                it.isDirectory && it.name == "build"
            }.filter {
                (it.parentFile.name == "app" || it.parentFile.listFiles()
                    .any { it.isDirectory && it.name == "app" })
            }.also {
                println("find ${it.count()} directory need to delete")
            }.forEach {
                println("clear build dir [${it.absolutePath}]")
                it.deleteRecursively()
            }
        } else {
            println("path[$path] not exist, do nothing")
        }
    }
}