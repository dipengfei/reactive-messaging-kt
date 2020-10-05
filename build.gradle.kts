import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.4.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false

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

    if (name != "rmkt-core") {
        apply {
            plugin("org.springframework.boot")
            plugin("io.spring.dependency-management")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

}







