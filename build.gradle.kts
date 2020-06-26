import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val kotlinVersion = "1.3.72"
val coroutinesVersion = "1.3.7"

val tornadoFxVersion = "1.7.20"
val micronautVersion = "2.0.0.RC2"
val konfVersion = "0.22.1"
val kotlinLoggingVersion = "1.7.9"
val slf4jVersion = "1.7.30"
val logbackVersion = "1.2.3"
val kotestVersion = "4.0.6"
val micronautKotestVersion = "1.2.0"
val mockkVersion = "1.10.0"
val testFxVersion = "4.0.16-alpha"
val picocliCodeGenVersion = "4.2.0"

plugins {
    java
    application
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "dev.talosdx"
version = "0.0.1"

kapt { arguments { arg("micronaut.processing.incremental", true) } }
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
    implementation(kotlin("reflect"))
    //https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    //second dependencies
    implementation("no.tornado:tornadofx:$tornadoFxVersion")

    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kapt("info.picocli:picocli-codegen:$picocliCodeGenVersion")
    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
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
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    testImplementation("io.micronaut.test:micronaut-test-kotest:$micronautKotestVersion")

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
        jvmArgs("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        if (gradle.startParameter.isContinuous) {
            systemProperties(
                mapOf(
                    "micronaut.io.watch.restart" to "true",
                    "micronaut.io.watch.enabled" to "true",
                    "micronaut.io.watch.paths" to "src/main"
                )
            )
        }
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
        mergeServiceFiles()
        archiveVersion.set("${project.version}")
        exclude("icon.png", "license.header")
    }
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
        arg("micronaut.processing.annotations", "dev.talosdx.microlauncherfx.*")
        arg("micronaut.processing.group", "dev.talosdx.microlauncherfx")
        arg("micronaut.processing.module", "MicroLauncherFx")
    }
}

