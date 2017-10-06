package trikered

import java.time.Instant

interface Event {
    val instant: Instant get() = Instant.now()
}