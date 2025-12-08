plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.uopeople"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

javafx {
    version = "21"
    modules("javafx.controls", "javafx.graphics")
}

application {
    mainClass.set("org.uopeople.disc.InteractiveProductViewer")
}

// Configuração do JAR executável (fat JAR com todas as dependências)
tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.uopeople.disc.InteractiveProductViewer"
    }

    // Inclui todas as classes do projeto
    from(sourceSets.main.get().output)

    // Inclui todas as dependências no JAR (fat JAR)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().map { 
            if (it.isDirectory) it else zipTree(it) 
        }
    }) {
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
}

// Garante que o run funcione com JavaFX
tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "--module-path", configurations.runtimeClasspath.get().asPath,
        "--add-modules", "javafx.controls,javafx.graphics"
    )
}