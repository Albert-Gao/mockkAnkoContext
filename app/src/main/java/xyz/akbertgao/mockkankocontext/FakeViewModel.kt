package xyz.akbertgao.mockkankocontext

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class FakeViewModel(
    private val fakeWebService: FakeWebService
):ViewModel() {
    fun tryAsync() {
        val self = this
        async(UI) {
            val result = bg { fakeWebService.getResult() }.await()
            self.checkResult(result)
        }

    }

    private fun checkResult(result:String) {
        println(result == "abc")
    }
}