import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val kotlinVersion = "1.3.72"
val coroutinesVersion = "1.3.7"
val koinVersion = "2.1.6"
val ktorClientVersion = "1.3.1"

val tornadoFxVersion = "1.7.21-SNAPSHOT"

val fontAwesomeVersion = "8.9"
val fontAwesomeVersionCommons = "8.15"

val konfVersion = "0.22.1"
val kotlinLoggingVersion = "1.7.9"
val slf4jVersion = "1.7.30"
val logbackVersion = "1.2.3"

val kotestVersion = "4.0.6"
val mockkVersion = "1.10.0"
val testFxVersion = "4.0.16-alpha"

plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "dev.talosdx"
version = "0.0.1"

application {
    mainClassName = "dev.talosdx.pentaquark.App"
}


repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    //always check dependencies Ctrl + Alt + Shift + U

    //main dependencies
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    //https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")
    implementation ("io.ktor:ktor-client-core:$ktorClientVersion")

    //fix minimize app by click on task bar see FxFxUtils.kt
    implementation("net.java.dev.jna:jna:4.2.1")
    implementation("net.java.dev.jna:jna-platform:4.2.1")

    //second dependencies
    implementation("no.tornado:tornadofx:$tornadoFxVersion")
    implementation("de.jensd:fontawesomefx:$fontAwesomeVersion")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

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
    testImplementation("org.koin:koin-test:$koinVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")

    testImplementation("org.testfx:testfx-core:$testFxVersion")
    testImplementation("org.testfx:testfx-junit5:$testFxVersion")

    //tests mock
    //https://mockk.io/
    testImplementation("io.mockk:mockk:$mockkVersion")

    testImplementation("com.github.tomakehurst:wiremock-jre8:2.26.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    val developmentOnly = configurations.create("developmentOnly")
    configurations.runtimeClasspath.get().extendsFrom(developmentOnly)

    compileKotlin {
        kotlinOptions.jvmTarget = sourceCompatibility
        kotlinOptions.javaParameters = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = targetCompatibility
        kotlinOptions.javaParameters = true
    }

    withType<Test> {
        classpath += developmentOnly
        useJUnitPlatform()
    }

    withType<JavaExec> {
        classpath += developmentOnly
    }

    withType<ShadowJar> {
        archiveBaseName.set(project.name)
        archiveClassifier.set("shadow") // fat, shadow
        manifest.attributes.apply {
            put("Application-Name", project.name)
            put("Build-Date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME))
            put("Created-By", System.getProperty("user.name"))
            put("Gradle-Version", gradle.gradleVersion)
            put("Implementation-Version", "${project.version}")
            put("JDK-Version", System.getProperty("java.version"))
        }
        archiveVersion.set("${project.version}")
        exclude("icon.png", "license.header")
    }
}

