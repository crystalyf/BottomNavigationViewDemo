package com.change.towerfarm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.change.towerfarm.repository.IApiRepository
import com.change.towerfarm.usecase.MainUseCase
import com.change.towerfarm.utils.SharedPrefsManager

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val sharePref: SharedPrefsManager,
    private val apiRepository: IApiRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(sharePref, MainUseCase(apiRepository))
                isAssignableFrom(StartViewModel::class.java) ->
                    StartViewModel(sharePref)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {
        @Volatile
        var INSTANCE: ViewModelFactory? = null

        fun getInstance(
            sharePref: SharedPrefsManager,
            apiRepository: IApiRepository
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            ViewModelFactory(
                                sharePref,
                                apiRepository
                            )
                    }
                }
            }
            return INSTANCE!!
        }
    }

}