package com.kaneoriley.android.extension

import org.gradle.api.Project

class ApplicationPlugin extends ExtensionPlugin {

    @Override
    void apply(Project project) {
        project.apply plugin: 'android-sdk-manager'
        project.apply plugin: 'com.android.application'
        super.apply(project)

        project.apt {
            arguments {
                resourcePackageName project.android.defaultConfig.applicationId
            }
        }
    }
}
