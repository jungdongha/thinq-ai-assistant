import org.gradle.kotlin.dsl.implementation

plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
}
val springAiVersion by extra("2.0.0-M3")

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "demo1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }}

dependencies {
    //basic
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    //devTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    //h2
    runtimeOnly("com.h2database:h2")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    //gemini
    implementation("org.springframework.ai:spring-ai-starter-model-google-genai")

    //claude
    implementation("org.springframework.ai:spring-ai-starter-model-anthropic")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
