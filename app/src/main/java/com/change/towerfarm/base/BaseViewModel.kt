package com.change.towerfarm.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.change.towerfarm.api.exceptions.ApiError
import com.change.towerfarm.api.model.Result
import com.change.towerfarm.utils.Constants
import com.change.towerfarm.utils.Event
import com.change.towerfarm.utils.ExceptionEvent
import com.change.towerfarm.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.UnknownHostException


open class BaseViewModel : ViewModel() {

    private val _authErrorEvent = MutableLiveData<ExceptionEvent<ApiError>>()
    val authErrorEvent: LiveData<ExceptionEvent<ApiError>> = _authErrorEvent

    private val _serverDataEvent = MutableLiveData<ExceptionEvent<ApiError>>()
    val serverDataEvent: LiveData<ExceptionEvent<ApiError>> = _serverDataEvent

    private val _serverErrorEvent = MutableLiveData<ExceptionEvent<ApiError>>()
    val serverErrorEvent: LiveData<ExceptionEvent<ApiError>> = _serverErrorEvent

    private val _otherErrorEvent = MutableLiveData<ExceptionEvent<ApiError>>()
    val otherErrorEvent: LiveData<ExceptionEvent<ApiError>> = _otherErrorEvent

    private val _networkErrorEvent = MutableLiveData<ExceptionEvent<Exception>>()
    val networkErrorEvent: LiveData<ExceptionEvent<Exception>> = _networkErrorEvent

    private val _networkNotConnectErrorEvent = MutableLiveData<ExceptionEvent<Exception>>()
    val networkNotConnectErrorEvent: LiveData<ExceptionEvent<Exception>> =
        _networkNotConnectErrorEvent

    private val _jsonErrorEvent = MutableLiveData<ExceptionEvent<Exception>>()
    val jsonErrorEvent: LiveData<ExceptionEvent<Exception>> = _jsonErrorEvent

    // API通信：通信中のインジケーター
    private val _loadingEvent = MutableLiveData<Event<Boolean>>()
    val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

    fun <T : BaseResultModel> runPipeline(
        showProgress: Boolean = true,
        pipelineBlock: suspend CoroutineScope.() -> Result<T>,
        errorAction: ((Constants.ApiErrorType) -> Unit)? = null,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            if (showProgress) {
                startLoading()
            }
            // ネットワーク設定無効判断
            GatewayApplication.instance?.let {
                if (!Utils.isNetworkAvailable(it)) {
                    if (showProgress) {
                        stopLoading()
                    }
                    _networkNotConnectErrorEvent.postValue(
                        ExceptionEvent(
                            UnknownHostException(),
                            errorAction
                        )
                    )
                    return@launch
                }
            }
            val result = handleApiResponse(pipelineBlock(), errorAction)
            if (result != null) {
                onSuccess.invoke(result.data)
            }
            if (showProgress) {
                stopLoading()
            }
        }
    }

    /**
     * API通信エラーの共通処理
     *
     * @param T エラーなしの場合、データのタイプ
     * @param result 処理するAPIの結果
     * @return エラーなしの場合はSuccessでデータを返す。エラーありの場合はメソッドの中に処理してnullを返す。
     */
    private fun <T> handleApiResponse(
        result: Result<T>,
        errorAction: ((Constants.ApiErrorType) -> Unit)? = null
    ): Result.Success<T>? {
        // エラーなしの場合
        if (result is Result.Success) {
            return result
        }
        val errorResult = result as Result.Error
        when (errorResult.type) {
            Constants.ApiErrorType.AuthError -> _authErrorEvent.postValue(
                ExceptionEvent(
                    errorResult.error!!,
                    errorAction
                )
            )
            Constants.ApiErrorType.ServerDataError -> _serverDataEvent.postValue(
                ExceptionEvent(
                    errorResult.error!!,
                    errorAction
                )
            )
            Constants.ApiErrorType.ServerError -> _serverErrorEvent.postValue(
                ExceptionEvent(
                    errorResult.error!!,
                    errorAction
                )
            )
            Constants.ApiErrorType.OtherError -> _otherErrorEvent.postValue(
                ExceptionEvent(
                    errorResult.error!!,
                    errorAction
                )
            )
            Constants.ApiErrorType.NetworkError -> _networkErrorEvent.postValue(
                ExceptionEvent(
                    errorResult.exception!!,
                    errorAction
                )
            )
            Constants.ApiErrorType.JsonError -> _jsonErrorEvent.postValue(
                ExceptionEvent(
                    errorResult.exception!!,
                    errorAction
                )
            )
        }

        return null
    }

    /**
     * 通信中のインジケーターを表示のイベントを送信
     *
     */
    fun startLoading() {
        _loadingEvent.postValue(Event(true))
    }

    /**
     * 通信中のインジケーターを消すのイベントを送信
     *
     */
    fun stopLoading() {
        _loadingEvent.postValue(Event(false))
    }

}
