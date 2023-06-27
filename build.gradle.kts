plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
}

taboolib {
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-effect")
    install("module-ui")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.11-3"

    description {
        bukkitApi("1.19")

        contributors {
            name("xiaozhangup")
        }

        dependencies {
            name("Adyeshach")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("ink.ptms.adyeshach:all:2.0.0-snapshot-1")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}