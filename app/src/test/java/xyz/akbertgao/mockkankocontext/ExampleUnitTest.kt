package xyz.akbertgao.mockkankocontext

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Test
import kotlin.test.*

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val mockWebService:FakeWebService = mockk(relaxed = true)
    private val fakeViewModel:FakeViewModel = spyk(FakeViewModel(mockWebService))

    @Test
    fun methodCall_should_happen_in_order() {
        every {
            mockWebService.getResult()
        } returns "albert"

        fakeViewModel.tryAsync()

        verifySequence {
            mockWebService.getResult()
            fakeViewModel invoke "checkResult" withArguments listOf("albert")
        }
    }
}
