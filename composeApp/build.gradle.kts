import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)

    kotlin("plugin.serialization") version "1.8.0" // Asegúrate de agregar el plugin de serialización

}

kotlin {
    tasks.create("testClasses")
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }


    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"  // o la versión que estés usando
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            implementation(libs.settings)
            implementation("com.russhwolf:multiplatform-settings-serialization:1.2.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqliteBundled)
            //implementation("network.chaintech:kmp-date-time-picker:1.0.5")



        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)


        }
    }

    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }
}

android {
    namespace = "com.johan.inventario"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.johan.inventario"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.text.desktop)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.johan.inventario.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe,TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Inventario"
            packageVersion = "1.0.0"
            windows{
                packageVersion = "1.0.0"
                msiPackageVersion = "1.0.0"
                exePackageVersion = "1.0.0"
                shortcut = true  // Creación de acceso directo en el menú de inicio
                iconFile.set(project.file("src/commonMain/composeResources/drawable/Logo.ico"))  // Ruta al icono .ico


            }
        }
    }
}


dependencies{
    add("kspCommonMainMetadata", libs.androidx.room.compiler)

}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach{
     if(name != "kspCommonMainKotlinMetadata"){
         dependsOn("kspCommonMainKotlinMetadata")
     }
}

room {
    schemaDirectory("$projectDir/schemas")
}