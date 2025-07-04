import net.ltgt.gradle.errorprone.errorprone
import org.gradle.kotlin.dsl.assign

plugins {
    `java-library`
    alias(libs.plugins.errorprone)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jspecify)
    errorprone(libs.nullaway)
    errorprone(libs.errorprone.core)

    testImplementation(libs.archunit)

    testImplementation(platform(libs.assertj.bom))
    testImplementation(libs.assertj.core)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        disableAllChecks = true // Other errorprone checks are disabled
        option("NullAway:OnlyNullMarked", "true") // Enable nullness checks only in null-marked code
        error("NullAway") // bump checks from warnings (default) to errors
    }
    if (name.lowercase().contains("test")) {
        options.errorprone {
            disable("NullAway")
        }
    }
}
