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

rootProject.name = "LensVault"
include(":app")

// Core Modules
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:filesystem")
include(":core:database")
include(":core:datastore")
include(":core:ui")
include(":core:ai-engine")
include(":core:ai-index")
include(":core:domain")

// Feature Modules
include(":feature:camera")
include(":feature:gallery")
include(":feature:editor")
include(":feature:ai")
include(":feature:security")
include(":feature:backup")
include(":feature:settings")
