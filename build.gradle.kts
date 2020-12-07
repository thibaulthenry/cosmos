plugins {
    java
}

version = "2.0.0-8.0.0"

repositories {
    mavenCentral()
    maven("https://repo-new.spongepowered.org/repository/maven-public/")
}

dependencies {
    //implementation("org.spongepowered:spongeapi:8.0.0-SNAPSHOT")
    implementation("org.spongepowered:spongeapi:8.0.0-20201203.141757-258")
}
