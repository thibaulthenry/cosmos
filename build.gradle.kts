plugins {
    java
}

version = "2.0.0-8.0.0"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
    maven("https://repo-new.spongepowered.org/repository/maven-public/")
}

dependencies {
    implementation("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")
}
