

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    /*Dokka not working*/
    id("org.jetbrains.dokka") version "1.9.10"
}

/*Dokka not working*/
tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

    dependencies {

        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.10")
    }
}