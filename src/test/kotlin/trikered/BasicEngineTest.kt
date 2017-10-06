package trikered

import io.github.neyb.shoulk.shouldEqual
import io.github.neyb.shoulk.shouldNotBe
import org.junit.jupiter.api.Test
import java.time.Instant

class BasicEngineTest {
    val engine = Engine()

    class MyEvent : Event

    @Test fun `create an engine`() {
    }

    @Test fun `can add listener on Event`() {
        engine.listen(MyEvent::class.java) { println("do stuff") }
    }

    @Test fun `engine can create a changelog`() {
        engine.createChangelog()
    }

    @Test fun `adding a listener and calling it through event`() {
        var called = false
        engine.listen(MyEvent::class.java) { called = true }

        engine.createChangelog().add(MyEvent())

        called shouldEqual true
    }

    @Test fun `an event should have a instant`() {
        var instant: Instant? = null
        engine.listen(MyEvent::class.java) { instant = it.instant }
        engine.createChangelog().add(MyEvent())

        instant shouldNotBe null
    }

    class MyTypedSubjectEvent(subject: String) : SubjectEvent<String>(subject)

    @Test fun `listener can access typed event`() {
        var called = false
        engine.listen(MyTypedSubjectEvent::class.java) {
            it.subject shouldEqual "message"
            called = true
        }

        engine.createChangelog().add(MyTypedSubjectEvent("message"))

        called shouldEqual true

    }

    @Test fun `several listeners are called in order`() {
        val calls = mutableListOf<String>()
        engine.listen(MyEvent::class.java) { calls += "event1" }
        engine.listen(MyEvent::class.java) { calls += "event2" }

        engine.createChangelog().add(MyEvent())

        calls shouldEqual listOf("event1", "event2")
    }
}