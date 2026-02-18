pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RamadanCompanionAI"

include(":app")
include(":core:ui")
include(":core:designsystem")
include(":core:network")
include(":core:database")
include(":core:location")
include(":core:ai")
include(":feature:today")
include(":feature:companion")
include(":feature:quran")
include(":feature:reflection")
include(":domain")
