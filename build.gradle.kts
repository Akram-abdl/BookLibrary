import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	jacoco
	id("info.solidsoft.pitest") version "1.15.0"
}

group = "fr.akramlaurent"
version = "0.0.1-SNAPSHOT"

sourceSets {
	create("testIntegration") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}

	create("testComponent") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
	create("testArchitecture") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.testImplementation.get())
}

val testComponentImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.testImplementation.get())
}

val testArchitectureImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.testImplementation.get())
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.liquibase:liquibase-core:4.25.0")
	implementation("org.postgresql:postgresql:42.7.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testImplementation("net.jqwik:jqwik:1.8.2")
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("com.ninja-squad:springmockk:4.0.2")

	testImplementation("org.testcontainers:postgresql:1.19.3")
	testImplementation("org.testcontainers:junit-jupiter:1.19.3")
	testImplementation("org.testcontainers:testcontainers:1.19.3")

	testImplementation("io.cucumber:cucumber-java:7.14.1")
	testImplementation("io.cucumber:cucumber-spring:7.14.1")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.1")

	testImplementation("io.rest-assured:rest-assured:5.3.2")


	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-web")
	testIntegrationImplementation("org.jetbrains.kotlin:kotlin-reflect")

	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-jdbc")
	testIntegrationImplementation("org.liquibase:liquibase-core:4.25.0")
	testIntegrationImplementation("org.postgresql:postgresql:42.7.0")

	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test")
	testIntegrationImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testIntegrationImplementation("net.jqwik:jqwik:1.8.2")
	testIntegrationImplementation("io.mockk:mockk:1.13.8")
	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")

	testIntegrationImplementation("org.testcontainers:postgresql:1.19.3")
	testIntegrationImplementation("org.testcontainers:junit-jupiter:1.19.3")
	testIntegrationImplementation("org.testcontainers:testcontainers:1.19.3")

	testIntegrationImplementation("io.cucumber:cucumber-java:7.14.1")
	testIntegrationImplementation("io.cucumber:cucumber-spring:7.14.1")
	testIntegrationImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.1")

	testIntegrationImplementation("io.rest-assured:rest-assured:5.3.2")



	testComponentImplementation("org.springframework.boot:spring-boot-starter-web")
	testComponentImplementation("org.jetbrains.kotlin:kotlin-reflect")

	testComponentImplementation("org.springframework.boot:spring-boot-starter-jdbc")
	testComponentImplementation("org.liquibase:liquibase-core:4.25.0")
	testComponentImplementation("org.postgresql:postgresql:42.7.0")

	testComponentImplementation("org.springframework.boot:spring-boot-starter-test")
	testComponentImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testComponentImplementation("net.jqwik:jqwik:1.8.2")
	testComponentImplementation("io.mockk:mockk:1.13.8")
	testComponentImplementation("com.ninja-squad:springmockk:4.0.2")

	testComponentImplementation("org.testcontainers:postgresql:1.19.3")
	testComponentImplementation("org.testcontainers:junit-jupiter:1.19.3")
	testComponentImplementation("org.testcontainers:testcontainers:1.19.3")

	testComponentImplementation("io.cucumber:cucumber-java:7.14.1")
	testComponentImplementation("io.cucumber:cucumber-spring:7.14.1")
	testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.1")

	testComponentImplementation("io.rest-assured:rest-assured:5.3.2")



	testArchitectureImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")

}

task<Test>("testIntegration") {
	useJUnitPlatform()
	testClassesDirs = sourceSets["testIntegration"].output.classesDirs
	classpath = sourceSets["testIntegration"].runtimeClasspath
}

task<Test>("testComponent") {
	useJUnitPlatform()
	testClassesDirs = sourceSets["testComponent"].output.classesDirs
	classpath = sourceSets["testComponent"].runtimeClasspath
}

task<Test>("testArchitecture") {
	useJUnitPlatform()
	testClassesDirs = sourceSets["testComponent"].output.classesDirs
	classpath = sourceSets["testComponent"].runtimeClasspath
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
	finalizedBy(tasks.pitest)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}

jacoco {
	toolVersion = "0.8.11"
}

pitest {
	junit5PluginVersion = "1.2.1"
}
