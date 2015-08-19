package com.kaneoriley.android.extension

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class ExtensionPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        final def variants
        final def plugin

        try {
            if (project.plugins.hasPlugin(AppPlugin)) {
                variants = project.android.applicationVariants
                plugin = project.plugins.getPlugin(AppPlugin)
            } else if (project.plugins.hasPlugin(LibraryPlugin)) {
                variants = project.android.libraryVariants
                plugin = project.plugins.getPlugin(LibraryPlugin)
            } else {
                throw new GradleException("The 'com.android.application' or 'com.android.library' plugin is required.")
            }
        } catch (Exception e) {
            throw new GradleException(e.getMessage());
        }

        project.repositories {
            jcenter()
            mavenCentral()
            mavenLocal()
        }

        project.apply plugin: 'kotlin-android'
        project.apply plugin: 'me.tatarka.retrolambda'
        project.apply plugin: 'com.neenbedankt.android-apt'

        project.android {
            compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }

            sourceSets {
                main.java.srcDirs += 'src/main/kotlin'
            }
        }

        project.dependencies {
            compile project.fileTree(dir: 'libs', include: ['*.jar'])
            compile 'org.aspectj:aspectjrt:1.8.6'
            compile 'com.android.support:support-annotations:23.0.0'
            provided 'org.projectlombok:lombok:1.16.6'
            compile 'org.slf4j:slf4j-api:1.7.12'
            compile('com.github.tony19:logback-android-classic:1.1.1-4') {
                exclude group: 'com.google.android', module: 'android'
                exclude module: 'apktool-lib'
            }
            compile 'org.jetbrains.kotlin:kotlin-stdlib:0.12.1230'
        }

        project.afterEvaluate {
            variants.all { variant ->
                JavaCompile javaCompile = variant.javaCompile
                javaCompile.doLast {

                    def bootClasspath
                    //noinspection GroovyAssignabilityCheck
                    if (plugin.properties['runtimeJarList']) {
                        bootClasspath = plugin.runtimeJarList
                    } else {
                        bootClasspath = project.android.bootClasspath
                    }

                    def String[] args = [
                            "-Xlint:cantFindTypeAffectingJPMatch=warning",
                            "-Xlint:cantFindType=warning",
                            "-showWeaveInfo",
                            "-preserveAllLocals",
                            "-encoding", "UTF-8",
                            "-${project.android.compileOptions.sourceCompatibility}",
                            "-inpath", javaCompile.destinationDir.absolutePath,
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", javaCompile.destinationDir.absolutePath,
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", bootClasspath.join(File.pathSeparator),
                    ]

                    def log = project.logger
                    MessageHandler handler = new MessageHandler(true);
                    new Main().run(args, handler);

                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                throw new GradleException(message.message, message.thrown)
                            case IMessage.WARNING:
                                log.warn message.message, message.thrown
                                break;
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break;
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break;
                        }
                    }
                }
            }
        }
    }
}
