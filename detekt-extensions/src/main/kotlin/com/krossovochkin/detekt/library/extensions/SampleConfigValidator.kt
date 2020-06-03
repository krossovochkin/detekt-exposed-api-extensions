package com.krossovochkin.detekt.library.extensions

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.ConfigValidator
import io.gitlab.arturbosch.detekt.api.Notification

class SampleConfigValidator : ConfigValidator {

    override fun validate(config: Config): Collection<Notification> {
        val result = mutableListOf<Notification>()
        runCatching {
            config.subConfig("library")
                .subConfig("ExposedApi")
                .run {
                    valueOrNull<Boolean>("active")
                }
        }.onFailure {
            result.add(
                SampleMessage(
                    "'active' property must be of type boolean."
                )
            )
        }
        runCatching {
            config.subConfig("library")
                .subConfig("ExposedApi")
                .run {
                    valueOrNull<List<String>>("ignorePathRegex")
                        ?.map { it.toRegex() }
                }
        }.onFailure {
            result.add(
                SampleMessage(
                    "'ignorePathRegex' property must be of type list of regex strings."
                )
            )
        }
        runCatching {
            config.subConfig("library")
                .subConfig("ExposedApi")
                .run {
                    valueOrNull<List<String>>("ignoreAnnotated")
                }
        }.onFailure {
            result.add(
                SampleMessage(
                    "'ignoreAnnotated' property must be of type list of strings."
                )
            )
        }
        return result
    }
}

class SampleMessage(
    override val message: String,
    override val level: Notification.Level = Notification.Level.Error
) : Notification
