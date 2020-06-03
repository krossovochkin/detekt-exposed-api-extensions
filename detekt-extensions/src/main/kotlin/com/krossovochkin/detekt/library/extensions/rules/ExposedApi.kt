package com.krossovochkin.detekt.library.extensions.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPublic
import org.jetbrains.kotlin.util.isAnnotated

const val CONFIG_PARAM_IGNORE_PATH_REGEX = "ignorePathRegex"
const val CONFIG_PARAM_IGNORE_ANNOTATED = "ignoreAnnotated"

class ExposedApi(config: Config) : Rule(config) {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Security,
        "This rule reports an API (top-level class, function, property etc.) as exposed. " +
                "Use other non-public modifier (e.g. internal) or annotate to state that this API was exposed intentionally",
        Debt(mins = 1)
    )

    private val ignorePathRegex: List<Regex> = valueOrDefault(CONFIG_PARAM_IGNORE_PATH_REGEX, emptyList<String>())
        .map { it.toRegex() }
    private val ignoreAnnotated: List<String> = valueOrDefault(CONFIG_PARAM_IGNORE_ANNOTATED, emptyList())

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        if (!classOrObject.isTopLevel()) {
            return
        }

        if (!classOrObject.isPublic) {
            return
        }

        val isCorrectlyAnnotated = classOrObject.isAnnotated &&
                classOrObject.annotationEntries.any { it.shortName?.asString() in ignoreAnnotated }
        if (isCorrectlyAnnotated) {
            return
        }

        if (ignorePathRegex.any { it.matches(classOrObject.fqName?.asString().orEmpty()) }) {
            return
        }

        report(
            CodeSmell(
                issue,
                Entity.from(classOrObject),
                message = "The class or object ${classOrObject.name} is exposed public API"
            )
        )
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (!function.isTopLevel) {
            return
        }

        if (!function.isPublic) {
            return
        }

        if (function.isAnnotated && function.annotationEntries.any { it.shortName?.asString() in ignoreAnnotated }) {
            return
        }

        if (ignorePathRegex.any { it.matches(function.fqName?.asString().orEmpty()) }) {
            return
        }

        report(
            CodeSmell(
                issue,
                Entity.from(function),
                message = "The function ${function.name} is exposed public API"
            )
        )
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (!property.isTopLevel) {
            return
        }

        if (!property.isPublic) {
            return
        }

        if (property.isAnnotated && property.annotationEntries.any { it.shortName?.asString() in ignoreAnnotated }) {
            return
        }

        if (ignorePathRegex.any { it.matches(property.fqName?.asString().orEmpty()) }) {
            return
        }

        report(
            CodeSmell(
                issue,
                Entity.from(property),
                message = "The class ${property.name} is exposed public API"
            )
        )
    }
}