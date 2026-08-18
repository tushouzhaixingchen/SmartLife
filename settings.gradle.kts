pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SmartLife"
include(":app")
include(":library_common")
include(":library_network")
include(":feature_user")
include(":feature_notes")
include(":feature_weather")
include(":feature_charts")
include(":feature_music")
include(":feature_ipc")
include(":feature_background")
include(":feature_responsive")
