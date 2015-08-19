[![Release](https://img.shields.io/github/release/lennykano/android-gradle-extension.svg?label=jitpack)](https://jitpack.io/#com.kaneoriley/android-gradle-extension) [![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/lennykano/android-gradle-extension.svg?branch=master)](https://travis-ci.org/lennykano/android-gradle-extension) [![Dependency Status](https://www.versioneye.com/user/projects/55d44b37265ff6001c0011a2/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d44b37265ff6001c0011a2)

# android-gradle-extension

TODO: Actual README

# Gradle Dependency

Firstly, you need to add JitPack.io to your repositories list in the root projects build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

Then, add android-gradle-extension to your buildscript dependencies:

```gradle
buildscript {
    dependencies {
        classpath 'com.kaneoriley:android-gradle-extension:0.1.1'
    }
}
```

The last step is to apply the plugin to your application or library project, for example:

```gradle
apply plugin: 'com.android.application' || apply plugin: 'com.android.library'
apply plugin: 'com.kaneoriley.android.extension'
```
