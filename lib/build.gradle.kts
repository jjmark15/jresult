import net.ltgt.gradle.errorprone.errorprone
import org.jreleaser.model.Active

plugins {
    `java-library`
    `maven-publish`
    signing
    id("org.jreleaser") version "1.19.0"
    alias(libs.plugins.errorprone)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jspecify)
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
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
    withJavadocJar()
    withSourcesJar()
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = "jresult"

            pom {
                name.set("JResult")
                description.set("A Result type")
                url.set("https://github.com/jjmark15/jresult")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        name.set("Josh Jones")
                        email.set("ohblonddev@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/jjmark15/jresult.git")
                    developerConnection.set("scm:git:ssh://github.com/jjmark15/jresult.git")
                    url.set("https://github.com/jjmark15/jresult")
                }
            }
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri("build/staging-deploy")
        }
    }
}

jreleaser {
    gitRootSearch = true

    announce {
        active = Active.NEVER
    }

    signing {
        active = Active.ALWAYS
        armored = true
        passphrase = System.getenv("JRELEASER_GPG_PASSPHRASE")
        publicKey = System.getenv("JRELEASER_GPG_PUBLIC_KEY")
        secretKey = System.getenv("JRELEASER_GPG_SECRET_KEY")
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepositories = listOf("build/staging-deploy")
                }
            }
        }
    }
}
