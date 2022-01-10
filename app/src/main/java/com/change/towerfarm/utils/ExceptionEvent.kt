package com.change.towerfarm.utils

import androidx.lifecycle.Observer

open class ExceptionEvent<out T>(
    content: T,
    private val handler: ((Constants.ApiErrorType) -> Unit)? = null
) : Event<T>(content) {
    fun peekAction(): ((Constants.ApiErrorType) -> Unit)? {
        return handler
    }
}

class ExceptionEventObserver<T>(private val onEventUnhandledContent: (T, ((Constants.ApiErrorType) -> Unit)?) -> Unit) :
    Observer<ExceptionEvent<T>> {

    override fun onChanged(event: ExceptionEvent<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it, event.peekAction())
        }
    }
}