package fr.akramlaurent.booklibrary

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Test

class ArchitectureUnitTest {

    private val rootPackage = "fr.akramlaurent.booklibrary"

    @Test
    fun `validate clean architecture rules`() {
        val classes = ClassFileImporter()
            .withImportOption(ImportOption.DoNotIncludeTests())
            .importPackages(rootPackage)

        val layeredArchitectureRule = Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer("Domain Layer").definedBy("$rootPackage.domain..")
            .layer("Driver Layer").definedBy("$rootPackage.infrastructure.driver..")
            .layer("Driven Layer").definedBy("$rootPackage.infrastructure.driven..")
            .layer("Application Layer").definedBy("$rootPackage.infrastructure.application..")
            .layer("Standard API").definedBy("kotlin..", "java..", "org.jetbrains..")
            .withOptionalLayers(true)
            .whereLayer("Domain Layer").mayOnlyBeAccessedByLayers("Standard API")
            .whereLayer("Driver Layer").mayNotBeAccessedByAnyLayer()

        layeredArchitectureRule.check(classes)
    }
}