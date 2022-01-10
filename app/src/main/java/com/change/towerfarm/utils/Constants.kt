package com.change.towerfarm.utils

object Constants {

    const val UNKNOWN_ERROR = "E_unknown"
    const val MAINTENANCE_ERROR = "E_Maintenance"
    const val UNAUTHORIZED_ERROR = "E400-1"
    const val SERVER_DB_ERROR = "E404-1"
    const val INTERNAL_SERVER_ERROR = "E500-1"

    enum class ApiErrorType {

        /** 認証情報が間違っている場合に返却するエラー */
        AuthError,

        /** DB に存在しない情報を参照した場合。 */
        ServerDataError,

        /** 想定していないエラーが発生した場合 */
        ServerError,

        /** Json エラー　*/
        JsonError,

        /** ネットワーク異常 */
        NetworkError,

        /** ネットワーク設定無効 */
        NetworkNotConnectError,

        /** 上記以外 */
        OtherError
    }

    const val PLAY_CONTROL_VIEW_DISMISS_TIME = 2000L

}