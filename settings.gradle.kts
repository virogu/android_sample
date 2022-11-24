dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/repository/google")
    }
}

rootProject.name = "MySamples"

//libs module
include(":library")
include(":xlog")

//app module
//include(":AIDLTest")
//include(":CardViewPager")
//include(":MotionDemo-master")
//include(":MotionLayout")
//include(":MySerialPort")
//include(":OverlayRecyView")
//include(":Paging3")
//include(":RecyclerSelection")
//include(":SerialPortDemo")
include(":TestApp")
//include(":materialdemo")
//include(":nativelib")
//include(":common")
include(":emptyapp")
//include(":rvpayload")
include(":systemAlert")
include(":base")
