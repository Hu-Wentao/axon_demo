import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}
apply(plugin = "idea")



group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { // 华为云
        url = uri("https://repo.huaweicloud.com/repository/maven/")
        isAllowInsecureProtocol = true
    }
    mavenLocal()
    mavenCentral()
}

dependencies {
// https://mvnrepository.com/artifact/org.axonframework/axon
//    implementation("org.axonframework:axon:3.4.3")
// https://mvnrepository.com/artifact/org.axonframework/axon-spring-boot-autoconfigure
    implementation("org.axonframework:axon-spring-boot-autoconfigure:4.5.4")
    implementation("org.axonframework.extensions.kotlin:axon-kotlin-parent:0.1.0")
//    implementation("org.axonframework:axon-spring-boot-starter:4.5.4")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.1")

    implementation("org.springframework.boot:spring-boot-starter:2.6.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.1")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
