package fr.droidfactory.cosmo.sdk.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

class TestDispatcher : CoroutineDispatcher(){
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()
    }
}