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
include(":libraries:library_common")
include(":libraries:library_network")
include(":features:feature_user")
include(":features:feature_notes")
include(":features:feature_weather")
include(":features:feature_charts")
include(":features:feature_music")
include(":features:feature_ipc")
include(":features:feature_background")
include(":features:feature_responsive")
include(":features:shell_bottom")
include(":shell:shell_launcher")
include(":shell:shell_bottom")
include(":features:feature_home")
include(":features:feature_ai")
