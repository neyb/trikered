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
        engine.listen<MyEvent> { called = true }

        val changelog = engine.createChangelog {
            on_trigger bind "default"
        }

        changelog.add(MyEvent())
        called shouldEqual false

        changelog.trigger()
        called shouldEqual true
    }

    @Test fun `calling twice trigger should be ignored`() {
        var called = 0
        engine.listen<MyEvent> { called += 1 }

        val changelog = engine.createChangelog {
            on_trigger bind "default"
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
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }

        var hotCalled = 0
        engine.listen<MyEvent>("hot register") { hotCalled += 1 }
        engine.listen<MyEvent>("hot register") { hotCalled += 1 }

        val changelog = engine.createChangelog {
            on_trigger bind "cold register"
            on_event bind "hot register"
        }

        changelog.add(MyEvent())
        coldCalled shouldEqual 0
        hotCalled shouldEqual 2

        changelog.trigger()
        coldCalled shouldEqual 3
        hotCalled shouldEqual 2
    }

    @Test fun `a default configuration can be set`() {
        var coldCalled = 0
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }
        engine.listen<MyEvent>("cold register") { coldCalled += 1 }

        var hotCalled = 0
        engine.listen<MyEvent>("hot register") { hotCalled += 1 }
        engine.listen<MyEvent>("hot register") { hotCalled += 1 }

        engine.bindByDefault("cold register", on_trigger)
        engine.bindByDefault("hot register", on_event)

        val changelog = engine.createChangelog()

        changelog.add(MyEvent())
        coldCalled shouldEqual 0
        hotCalled shouldEqual 2

        changelog.trigger()
        coldCalled shouldEqual 3
        hotCalled shouldEqual 2
    }

    @Test fun `can specify register when triggering`() {
        val calls = mutableListOf<String>()

        engine.listen<MyEvent>("register1") { calls += "listener1" }
        engine.listen<MyEvent>("register2") { calls += "listener2" }
        val changelog = engine.createChangelog {}
        changelog.add(MyEvent())

        changelog.trigger("register1")

        calls shouldEqual listOf("listener1")
    }

    @Test fun `can use default without a specific register`() {
        val calls = mutableListOf<String>()

        engine.listen<MyEvent> { calls += "listener1" }
        engine.listen<MyEvent>("other register") { calls += "other" }
        engine.bindByDefault("other register", on_event)

        val changelog = engine.createChangelog {
            bindDefault()
            unbind("default")
        }

        changelog.add(MyEvent())

        calls shouldEqual listOf("other")
    }

    @Test fun `adding twice the same default binding should register it only once`() {
        val calls = mutableListOf<String>()

        engine.listen<MyEvent>("r") { calls += "tick" }
        engine.bindByDefault("r", on_event)
        engine.bindByDefault("r", on_event)
        engine.bindByDefault("r", on_trigger)
        engine.bindByDefault("r", on_trigger)

        val changelog = engine.createChangelog()

        changelog.add(MyEvent())

        calls shouldEqual listOf("tick")

        calls.clear()

        changelog.trigger()

        calls shouldEqual listOf("tick")
    }
}