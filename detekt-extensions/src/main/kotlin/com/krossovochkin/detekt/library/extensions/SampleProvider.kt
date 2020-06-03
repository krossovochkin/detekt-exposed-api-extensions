package com.krossovochkin.detekt.library.extensions

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import com.krossovochkin.detekt.library.extensions.rules.ExposedApi

const val RULE_SET_ID_LIBRARY = "library"

class SampleProvider : RuleSetProvider {

    override val ruleSetId: String =
        RULE_SET_ID_LIBRARY

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            ExposedApi(
                config
            )
        )
    )
}
