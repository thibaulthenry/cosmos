plugins {
    java
}

group = "cosmos"
version = "1.12-7.3.0-beta"
description = project.description

repositories {
    mavenCentral()
    maven("https://repo-new.spongepowered.org/repository/maven-public/")
}

dependencies {
    implementation("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")
}
