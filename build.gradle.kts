val kotlinx_io_version="0.1.9"
val serialization_version="0.11.0"
//val coroutines_version="1.2.1"
//val atomic_fu_version="0.13.2"

plugins {
    kotlin("multiplatform") version "1.3.50"
    id("maven-publish")

}

group = "io.pnyx"
version = "1.0-SNAPSHOT"

repositories {
    maven( url = "https://kotlin.bintray.com/kotlinx" )
    maven( url = "https://dl.bintray.com/kotlin/kotlin-eap" )
    maven( url = "https://dl.bintray.com/kotlin/kotlin-js-wrappers" )
    mavenCentral()
    jcenter()

}

kotlin {
    targets.all {
        compilations.all {
            kotlinOptions {
                //allWarningsAsErrors = true
            }
        }
    }
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the "main" compilation:
                jvmTarget = "1.8"
            }

            compileKotlinTask // get the Kotlin task "compileKotlinJvm"
            output // get the main compilation output
        }
        val test by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the "main" compilation:
                jvmTarget = "1.8"
            }

            compileKotlinTask // get the Kotlin task "compileKotlinJvm"
            output // get the main compilation output
        }

        compilations["test"].runtimeDependencyFiles // get the test runtime classpath
    }
    js {
      nodejs {

      }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-io:$kotlinx_io_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-io:$kotlinx_io_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:$kotlinx_io_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-io-jvm:$kotlinx_io_version")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-io-js:$kotlinx_io_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-io-js:$kotlinx_io_version")
//                implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.85-kotlin-1.3.50") {
//                    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-html-js")
//                }
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

    }
}
/*
compileKotlinJs.configure {
    kotlinOptions {
        metaInfo = true
        sourceMap = true
        moduleKind = "umd"
        main = "noCall"
        sourceMapEmbedSources = "always"
    }
}

compileTestKotlinJs.configure {
    kotlinOptions {
        metaInfo = true
        sourceMap = true
        moduleKind = "umd"
        main = "call"
        sourceMapEmbedSources = "always"
    }
}

task copyJsDependencies(type: Copy, dependsOn: compileTestKotlinJs) {
    from compileKotlinJs.destinationDir
    into "${buildDir}/node_modules"

    def configuration = configurations.jsTestRuntimeClasspath
    from(files {
        configuration.collect { File file ->
            file.name.endsWith(".jar")
                    ? zipTree(file.absolutePath).matching {
                include "*.js"
                include "*.js.map" }
                    : files()
        }
    }.builtBy(configuration))
}

//node {
//    version = "11.8.0"//nodeVersion
//    download = true
//}
//
//task installMocha(type: NpmTask) {
//    args = ["install", "mocha"]
//}
//
//task runMocha(type: NodeTask, dependsOn: [installMocha, compileTestKotlinJs, copyJsDependencies]) {
//    script = file("node_modules/mocha/bin/mocha")
//    args = [compileTestKotlinJs.outputFile]
//}
task runMocha(type: NodeJsTask, dependsOn: [compileTestKotlinJs, copyJsDependencies]) {//populateNodeModules, 
    require = ["mocha"]
    executable = "/home/zeno/sources/koan/node_modules/mocha/bin/mocha"//projectDir.toPath().relativize(file("node_modules/mocha/bin/mocha").toPath())
    args = [projectDir.toPath().relativize(file(compileTestKotlinJs.outputFile).toPath())]
}
jsTest.dependsOn runMocha
*/