plugins {

    kotlin("jvm") version "1.3.72" apply false
    kotlin("plugin.spring") version "1.3.72" apply false
}

allprojects {

    group = "me.danielpf"

    repositories {
        mavenCentral()
    }

}

subprojects {

    apply {
        plugin("kotlin")
    }

}







