package com.change.towerfarm.api.model

import com.change.towerfarm.api.exceptions.ApiError
import com.change.towerfarm.utils.Constants
import com.google.gson.JsonSyntaxException

/**
 * APIのレスポンスを処理するために、API処理結果の容器
 *
 * @param R 異常なしの場合の返すデータタイプ
 */
sealed class Result<out R> {

    /**
     * 成功に処理した場合
     *
     * @param T データタイプ
     * @property data データ
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * エラーが発生した場合
     * exception、errorsのいずれかを渡すが必須
     *
     * @property exception exception発生した時その内容+
     *
     * @property error サーバーからエラー返した時その内容
     */
    data class Error(
        val exception: Exception? = null,
        val error: ApiError? = null
    ) : Result<Nothing>() {
        var type: Constants.ApiErrorType = Constants.ApiErrorType.OtherError

        init {
            // exception、errorsのいずれかを渡すが必須
            assert(exception != null || error != null)
            if (exception != null) {
                type = when (exception) {
                    is JsonSyntaxException -> Constants.ApiErrorType.JsonError // Json エラー
                    else -> Constants.ApiErrorType.NetworkError //　ネットワークエラー
                }
            }
            if (error != null) {
                //根据error code , 对error type进行赋值
                type = when (error.code) {
                    Constants.UNAUTHORIZED_ERROR -> Constants.ApiErrorType.AuthError
                    Constants.SERVER_DB_ERROR -> Constants.ApiErrorType.ServerDataError
                    Constants.INTERNAL_SERVER_ERROR -> Constants.ApiErrorType.ServerError
                    else -> Constants.ApiErrorType.OtherError //　その他エラー
                }
            }
        }
    }

    object Loading : Result<Nothing>()

}