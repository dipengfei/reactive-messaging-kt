plugins {
	kotlin("jvm")
}

version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.11.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.2")
}

