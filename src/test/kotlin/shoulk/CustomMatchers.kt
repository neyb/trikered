package shoulk

import io.github.neyb.shoulk.matcher.MatchResult
import io.github.neyb.shoulk.matcher.Matcher

fun <T, V> have(description: String, expected: V, extractor: (T) -> V): Matcher<T> {
    return object : Matcher<T> {
        override val description = "has $description equals to $expected"

        override fun match(actual: T) = extractor(actual).let {
            if (it == expected) MatchResult.ok
            else MatchResult.Fail("$actual $description should be \"$expected\" but was \"$it\"")
        }
    }
}
