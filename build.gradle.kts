import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    `maven-publish`
}

var skriptMinestomVersion = "1.0.0-alpha.41"
description = "Combat for Minestom"
group = "rocks.minestom"
version = "1.0.0"

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://maven.hapily.me/snapshots")
}

dependencies {
    compileOnly("com.github.hapily04:skript-minestom:$skriptMinestomVersion")
    compileOnly("it.unimi.dsi:fastutil:8.5.12")
    testImplementation("com.github.hapily04:skript-minestom:$skriptMinestomVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("minestom.inside-test", "true")
    failOnNoDiscoveredTests = false
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to project.version.toString())
    inputs.properties(props) // Ensures cache invalidates correctly if version changes
    expand(props)
}