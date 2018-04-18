package xyz.akbertgao.mockkankocontext

import android.os.Handler
import io.mockk.*
import kotlinx.coroutines.experimental.android.HandlerContext
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

import java.util.concurrent.ForkJoinPool

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val mockWebService: FakeWebService = mockk(relaxed = true)
    private val fakeViewModel: FakeViewModel = spyk(FakeViewModel(mockWebService), recordPrivateCalls = true)

    private val handlerCtxKt = staticMockk("kotlinx.coroutines.experimental.android.HandlerContextKt")

    @Test
    fun methodCall_should_happen_in_order() {
        handlerCtxKt.use {
            every {
                mockWebService.getResult()
            } returns "albert"

            val handler = mockk<Handler>()
            every { handler.post(any()) } answers {
                ForkJoinPool.commonPool().submit(firstArg<Runnable>())
                true
            }
            every { UI } returns HandlerContext(handler)

            runBlocking {
                fakeViewModel.tryAsync().join()
            }

            verifySequence {
                fakeViewModel.tryAsync()
                mockWebService.getResult()
                fakeViewModel invoke "checkResult" withArguments listOf("albert")
            }
        }
    }
}

