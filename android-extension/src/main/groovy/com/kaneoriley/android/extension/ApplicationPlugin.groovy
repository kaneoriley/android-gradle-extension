package com.kaneoriley.android.extension

import org.gradle.api.Project

class ApplicationPlugin extends ExtensionPlugin {

    @Override
    void apply(Project project) {
        project.apply plugin: 'android-sdk-manager'
        project.apply plugin: 'com.android.application'
        super.apply(project)

        project.ext.set(project.name, variant.mergedFlavor.versionName + "_" + variant.mergedFlavor.versionCode +
                "_" + variant.buildType.name);

        project.apt {
            arguments {
                resourcePackageName project.android.defaultConfig.applicationId
            }
        }

        project.android {
            packagingOptions {
                exclude 'META-INF/services/javax.annotation.processing.Processor'
                exclude 'META-INF/LICENSE.txt'
                exclude 'META-INF/LICENSE'
                exclude 'META-INF/NOTICE.txt'
                exclude 'META-INF/NOTICE'
                exclude 'LICENSE.txt'
            }

            buildTypes {
                release {
                    debuggable false
                    minifyEnabled false
                    shrinkResources false
                }
                debug {
                    debuggable true
                    versionNameSuffix ".${getShortDate()}-debug"
                    applicationIdSuffix '.debug'
                    minifyEnabled false
                    shrinkResources false
                }
                staging {
                    debuggable false
                    versionNameSuffix ".${getShortDate()}-staging"
                    applicationIdSuffix '.staging'
                    minifyEnabled false
                    shrinkResources false
                }
            }
        }
    }
}
