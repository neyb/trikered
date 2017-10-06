package trikered

import io.github.neyb.shoulk.shouldEqual
import org.junit.jupiter.api.Test
import trikered.BindingType.on_event
import trikered.BindingType.on_trigger

class BindingTypeTest {
    val engine = Engine()

    class MyEvent : Event

    @Test fun `a cold binding should be called only on trigger`() {
        var called = false
        engine.listen(MyEvent::class.java) { called = true }

        val changelog = engine.createChangelog {
            "default" bindedAs on_trigger
        }

        changelog.add(MyEvent())
        called shouldEqual false

        changelog.trigger()
        called shouldEqual true
    }

    @Test fun `calling twice trigger should be ignored`() {
        var called = 0
        engine.listen(MyEvent::class.java) { called += 1 }

        val changelog = engine.createChangelog {
            "default" bindedAs on_trigger
        }

        changelog.add(MyEvent())
        called shouldEqual 0

        changelog.trigger()
        called shouldEqual 1

        changelog.trigger()
        called shouldEqual 1
    }

    @Test fun `mixing on_trigger and on_event should work`() {
        var coldCalled = 0
        engine.listen(MyEvent::class.java, "cold register") { coldCalled += 1 }
        engine.listen(MyEvent::class.java, "cold register") { coldCalled += 1 }
        engine.listen(MyEvent::class.java, "cold register") { coldCalled += 1 }

        var hotCalled = 0
        engine.listen(MyEvent::class.java, "hot register") { hotCalled += 1 }
        engine.listen(MyEvent::class.java, "hot register") { hotCalled += 1 }

        val changelog = engine.createChangelog {
            "cold register" bindedAs on_trigger
            "hot register" bindedAs on_event
        }

        changelog.add(MyEvent())
        coldCalled shouldEqual 0
        hotCalled shouldEqual 2

        changelog.trigger()
        coldCalled shouldEqual 3
        hotCalled shouldEqual 2
    }

    @Test fun `a default configuration can be set when adding listener`() {
        var coldCalled = 0
        engine.listen(MyEvent::class.java, "cold register", on_trigger) { coldCalled += 1 }
        engine.listen(MyEvent::class.java, "cold register", on_trigger) { coldCalled += 1 }
        engine.listen(MyEvent::class.java, "cold register", on_trigger) { coldCalled += 1 }

        var hotCalled = 0
        engine.listen(MyEvent::class.java, "hot register", on_event) { hotCalled += 1 }
        engine.listen(MyEvent::class.java, "hot register", on_event) { hotCalled += 1 }

        val changelog = engine.createChangelog()

        changelog.add(MyEvent())
        coldCalled shouldEqual 0
        hotCalled shouldEqual 2

        changelog.trigger()
        coldCalled shouldEqual 3
        hotCalled shouldEqual 2
    }
}