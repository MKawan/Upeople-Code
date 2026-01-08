plugins {
    application
    eclipse
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "21.0.5"
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.weatherapp.App")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
