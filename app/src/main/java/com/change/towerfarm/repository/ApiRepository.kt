package com.change.towerfarm.repository

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ApiRepository(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IApiRepository {

    companion object {
        private var instance: IApiRepository? = null
        fun instance(
            context: Context
        ): IApiRepository {
            if (instance == null) {
                instance =
                    ApiRepository(
                        context
                    )
            }
            return instance!!
        }
    }


}