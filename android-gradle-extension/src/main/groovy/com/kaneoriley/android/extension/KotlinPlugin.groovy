package com.kaneoriley.android.extension

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.apply plugin: 'kotlin-android'

        project.android {
            sourceSets {
                main.java.srcDirs += 'src/main/kotlin'
            }
        }

        project.dependencies {
            compile 'org.jetbrains.kotlin:kotlin-stdlib:0.12.1230'
        }
    }
}
