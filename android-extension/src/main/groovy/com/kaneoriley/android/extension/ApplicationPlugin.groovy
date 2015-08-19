package com.kaneoriley.android.extension

import com.android.build.gradle.api.ApkVariant
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.Project

import java.text.SimpleDateFormat

@SuppressWarnings("GroovyUnusedDeclaration")
class ApplicationPlugin extends ExtensionPlugin {

    @Override
    void apply(Project project) {
        project.apply plugin: 'android-sdk-manager'
        project.apply plugin: 'com.android.application'
        super.apply(project)

        project.afterEvaluate {
            project.android.applicationVariants.each { ApkVariant variant ->
                variant.outputs.each { BaseVariantOutput vo ->
                    if (!vo.outputFile.name.endsWith('.apk')) {
                        return;
                    }

                    String appName = project.name
                    Integer versionCode = variant.mergedFlavor.versionCode
                    String versionName = variant.mergedFlavor.versionName
                    String buildType = variant.buildType.name

                    String filename = "${appName}-${versionName}-${versionCode}-${buildType}.apk".toLowerCase()
                    //noinspection GroovyAssignabilityCheck
                    vo.outputFile = new File(vo.outputFile.parentFile, filename)
                }
            }
        }

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

    static def getDate() {
        def df = new SimpleDateFormat("yyMMddHH")
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        df.setTimeZone(tz)
        return df.format(new Date())
    }

    static def getShortDate() {
        def df = new SimpleDateFormat("yyMMdd")
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        df.setTimeZone(tz)
        return df.format(new Date())
    }

}
