val coroutinesVersion = "1.3.7"

val konfVersion = "0.22.1"
val kotlinLoggingVersion = "1.7.9"
val slf4jVersion = "1.7.30"
val logbackVersion = "1.2.3"
val kotestVersion = "4.0.6"
val mockkVersion = "1.10.0"

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    java
    application
    kotlin("jvm") version "1.3.72"
}

group = "dev.talosdx"
version = "0.0.1"


application {
    mainClassName = "dev.talosdx.microlauncherfx.MicroLauncherFxAppKt"
}


repositories {
    mavenCentral()
}

dependencies {
    //always check dependencies Ctrl + Alt + Shift + U

    //main dependencies
    implementation(kotlin("stdlib-jdk8"))
    //https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    //second dependencies

    //configuration
    implementation("com.uchuhimo:konf-toml:$konfVersion")

    //logging
    //https://github.com/MicroUtils/kotlin-logging
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //tests
    //https://github.com/kotest/kotest
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")

    //tests mock
    //https://mockk.io/
    testImplementation("io.mockk:mockk:$mockkVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = sourceCompatibility
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = targetCompatibility
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

