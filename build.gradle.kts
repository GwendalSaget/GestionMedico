// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    dependencies {
        // Ajoutez cette ligne pour inclure la bibliothèque Google Services
        classpath("com.google.gms:google-services:4.4.2")
        // Ajoutez le SDK Firebase BOM pour gérer les versions des dépendances Firebase
        classpath("com.google.firebase:firebase-bom:31.0.2")
    }
}