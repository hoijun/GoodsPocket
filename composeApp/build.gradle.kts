import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktlint)
}

ktlint {
    filter {
        exclude { it.file.path.contains("/build/") }
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

sqldelight {
    databases {
        create("GoodsPocketDatabase") {
            packageName.set("goods.pocket.app.db")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "goods.pocket.app.composeapp")
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            api(libs.koin.annotations)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidUnitTest.dependencies {
            implementation(libs.sqldelight.sqlite.driver)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.kotlin.testJunit)
            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.test.runner)
            implementation(libs.androidx.compose.ui.test.junit4)
        }
        val iosArm64Main by getting {
            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }
        val iosSimulatorArm64Main by getting {
            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }
    }

    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

android {
    namespace = "goods.pocket.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "goods.pocket.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
    debugImplementation(libs.compose.uiTooling)
}

tasks.matching {
    it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata"
}.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

abstract class CheckArchitecture : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: DirectoryProperty

    @TaskAction
    fun checkSources() {
        val root = sources.get().asFile
        val violations = mutableListOf<String>()
        val importPattern = Regex("^import\\s+([\\w.*]+)")
        root.walkTopDown().filter { it.extension == "kt" }.forEach { file ->
            val path = file.relativeTo(root).invariantSeparatorsPath
            val lines = file.readLines()
            if (lines.size > 600) violations += "$path exceeds 600 lines (${lines.size})"
            lines.forEachIndexed { index, line ->
                val dependency =
                    importPattern.find(line)?.groupValues?.get(1) ?: return@forEachIndexed
                val forbidden = when {
                    path.startsWith("domain/") -> listOf(
                        "goods.pocket.app.data.",
                        "goods.pocket.app.presentation.",
                        "goods.pocket.app.di.",
                        "goods.pocket.app.db.",
                        "goods.pocket.app.i18n.",
                        "android.",
                        "androidx.",
                        "platform.",
                        "app.cash.sqldelight.",
                        "org.koin.",
                        "com.google.firebase.",
                        "io.github.jan.supabase.",
                    )
                    path.startsWith("presentation/") -> listOf(
                        "goods.pocket.app.data.",
                        "goods.pocket.app.db.",
                        "app.cash.sqldelight.",
                    )
                    path.startsWith("data/") -> listOf("goods.pocket.app.presentation.")
                    else -> emptyList()
                }
                if (forbidden.any(dependency::startsWith)) {
                    violations += "$path:${index + 1}: forbidden dependency $dependency"
                }
            }
        }
        check(violations.isEmpty()) { violations.joinToString("\n") }
    }
}

val checkArchitecture by tasks.registering(CheckArchitecture::class) {
    group = "verification"
    description = "Checks shared Kotlin layer imports and production file sizes."
    sources.set(layout.projectDirectory.dir("src/commonMain/kotlin/goods/pocket/app"))
}

tasks.named("check") {
    dependsOn(checkArchitecture)
}

// KSP's manually registered source root still participates in Gradle input validation.
tasks.matching { it.name.startsWith("runKtlint") && it.name.endsWith("CommonMainSourceSet") }
    .configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }
