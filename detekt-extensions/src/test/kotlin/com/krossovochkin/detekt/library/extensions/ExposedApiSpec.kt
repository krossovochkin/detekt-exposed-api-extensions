package com.krossovochkin.detekt.library.extensions

import com.krossovochkin.detekt.library.extensions.rules.ExposedApi
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Test

class ExposedApiSpec {

    @Test
    fun `if private then ok`() {
        val code = """
            private class A {
            }
            
            private object B {
            }
            
            private interface C {
            }
            
            private fun d() {
            }
            
            private val e: Boolean = true
            
            private enum class F {
            }
            
            private annotation class G {
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(0)
    }

    @Test
    fun `if internal then ok`() {
        val code = """
            internal class A {
            }
            
            internal object B {
            }
            
            internal interface C {
            }
            
            internal fun d() {
            }
            
            internal val e: Boolean = true
            
            internal enum class F {
            }
            
            internal annotation class G {
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(0)
    }

    @Test
    fun `if explicit public and not annotated then fail`() {
        val code = """
            public class A {
            }
            
            public object B {
            }
            
            public interface C {
            }
            
            public fun d() {
            }
            
            public val e: Boolean = true
            
            public enum class F {
            }
            
            public annotation class G {
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(7)
    }

    @Test
    fun `if implicit public and not annotated then fail`() {
        val code = """
            class A {
            }
            
            object B {
            }
            
            interface C {
            }
            
            fun d() {
            }
            
            val e: Boolean = true
            
            enum class F {
            }
            
            annotation class G {
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(7)
    }

    @Test
    fun `if public and annotated with wrong annotation then fail`() {
        val code = """
            @Wrong
            class A {
            }
            
            @Wrong
            object B {
            }
            
            @Wrong
            interface C {
            }
            
            @Wrong
            fun d() {
            }
            
            @Wrong
            val e: Boolean = true
            
            @Wrong
            enum class F {
            }
            
            @Wrong
            annotation class G {
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(7)
    }

    @Test
    fun `if public and annotated with correct annotation then ok`() {
        val code = """
            @PublicApi
            class A {
            }
            
            @PublicApi
            object B {
            }
            
            @PublicApi
            interface C {
            }
            
            @PublicApi
            fun d() {
            }
            
            @PublicApi
            val e: Boolean = true
            
            @PublicApi
            enum class F {
            }
            
            @PublicApi
            annotation class G {
            }
        """.trimIndent()

        val config = TestConfig(
            values = mapOf(
                "ignoreAnnotated" to listOf("PublicApi")
            )
        )

        assertThat(
            ExposedApi(
                config
            ).lint(code))
            .hasSize(0)
    }

    @Test
    fun `if public and annotated with correct annotation from multiple allowed then ok`() {
        val code = """
            @PublicApi
            class A {
            }
            
            @PublicApi
            object B {
            }
            
            @PublicApi
            interface C {
            }
            
            @PublicApi
            fun d() {
            }
            
            @PublicApi
            val e: Boolean = true
            
            @PublicApi
            enum class F {
            }
            
            @PublicApi
            annotation class G {
            }
        """.trimIndent()

        val config = TestConfig(
            values = mapOf(
                "ignoreAnnotated" to listOf("PublishedApi", "Public", "PublicApi")
            )
        )

        assertThat(
            ExposedApi(
                config
            ).lint(code))
            .hasSize(0)
    }

    @Test
    fun `if matches exclude then skipped`() {
        val code = """
            package com.something.very.long

            class A {
            
            }
        """.trimIndent()

        val config = TestConfig(
            values = mapOf(
                "ignorePathRegex" to listOf(".*very.*")
            )
        )

        assertThat(
            ExposedApi(
                config
            ).lint(code))
            .hasSize(0)
    }

    @Test
    fun `if default inside internal class then ok`() {
        val code = """
            internal class Parent {
            
                class A {
                }
                
                object B {
                }
                
                interface C {
                }
                
                fun d() {
                }
                
                val e: Boolean = true
                
                enum class F {
                }
                
                annotation class G {
                }
            
            }
        """.trimIndent()

        assertThat(
            ExposedApi(
                TestConfig()
            ).lint(code))
            .hasSize(0)
    }
}