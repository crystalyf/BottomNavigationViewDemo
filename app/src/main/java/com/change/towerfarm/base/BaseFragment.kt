package com.change.towerfarm.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.change.towerfarm.api.exceptions.ApiError
import com.change.towerfarm.R
import com.change.towerfarm.utils.*

open class BaseFragment : Fragment() {

    /**
     * データ保存操作の対象
     */
    val preferences by lazy { SharedPrefsManager(requireContext()) }

    var isDialogShow = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Utils.timberTrace(this::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.timberTrace(this::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils.timberTrace(this::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Utils.timberTrace(this::class.java)
        addObservers()
    }

    override fun onResume() {
        super.onResume()
        Utils.timberTrace(this::class.java)
    }

    override fun onPause() {
        super.onPause()
        Utils.timberTrace(this::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils.timberTrace(this::class.java)

        parentFragmentManager.also {
            it.fragments.forEach { fragment ->
                if (fragment is CommonDialog<*>) {
                    fragment.clearListeners()
                    if (activity?.isFinishing == false) {
                        fragment.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.timberTrace(this::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        Utils.timberTrace(this::class.java)
    }

    open fun addObservers() {

    }

    /**
     * 通信中の状態イベントをObserve
     *
     * @param viewModel FragmentにBindingのViewModel
     */
    fun observeApiLoadingEvent(viewModel: BaseViewModel) {
        viewModel.loadingEvent.observe(this, EventObserver {
            if (it) {
                (activity as? BaseActivity)?.showBlockingProgress()
            } else {
                (activity as? BaseActivity)?.hideBlockingProgress()
            }
        })
    }

    /**
     * ViewModel中のAPIエラーイベントをObserve
     *
     * @param viewModel FragmentにBindingのViewModel
     */
    fun observeApiErrorEvent(viewModel: BaseViewModel) {
        viewModel.authErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showAuthError(data, action)
        })
        viewModel.serverDataEvent.observe(this, ExceptionEventObserver { data, action ->
            showServerDataError(data, action)
        })
        viewModel.serverErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showServerError(data, action)
        })
        viewModel.otherErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showOtherError(data, action)
        })
        viewModel.networkErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showNetworkError(data, action)
        })
        viewModel.networkNotConnectErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showNetworkNotConnectError(data, action)
        })
        viewModel.jsonErrorEvent.observe(this, ExceptionEventObserver { data, action ->
            showJsonError(data, action)
        })
    }

    /**
     * 認証エラーのメッセージを画面に表示
     */
    open fun showAuthError(error: ApiError, action: ((Constants.ApiErrorType) -> Unit)? = null) {
//        var errorMsg = getString(R.string.dialog_error_common_message)
//        if (!TextUtils.isEmpty(error.)) {
//            errorMsg = error.message
//        }
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            errorMsg
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterAuthError(action)
//        }
    }

    /**
     * 認証エラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterAuthError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.AuthError)
        actionAfterAuthError()
    }

    /**
     * 認証エラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterAuthError() {
        // custom error action
    }

    /**
     * リソース参照エラーのメッセージを画面に表示
     */
    open fun showServerDataError(
        error: ApiError,
        action: ((Constants.ApiErrorType) -> Unit)? = null
    ) {
//        var errorMsg = getString(R.string.dialog_error_common_message)
//        if (!TextUtils.isEmpty(error.message)) {
//            errorMsg = error.message
//        }
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            errorMsg
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterServerDataError(action)
//        }
    }

    /**
     * リソース参照エラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterServerDataError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.ServerDataError)
        actionAfterServerDataError()
    }

    /**
     * リソース参照エラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterServerDataError() {
        // custom error action
    }

    /**
     * システムエラーのメッセージを画面に表示
     */
    open fun showServerError(error: ApiError, action: ((Constants.ApiErrorType) -> Unit)? = null) {
//        var errorMsg = getString(R.string.dialog_error_common_message)
//        if (!TextUtils.isEmpty(error.message)) {
//            errorMsg = error.message
//        }
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            errorMsg
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterServerError(action)
//        }
    }

    /**
     * システムエラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterServerError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.ServerError)
        actionAfterServerError()
    }

    /**
     * システムエラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterServerError() {
        // custom error action
    }

    /**
     * ネットワークエラーのメッセージを画面に表示
     */
    open fun showNetworkError(
        e: Exception?,
        action: ((Constants.ApiErrorType) -> Unit)? = null
    ) {
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            getString(R.string.dialog_network_error_message)
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterNetworkError(action)
//        }
    }

    /**
     * ネットワークエラーダイアログの「OK」ボタンがタップした後の処理
     *
     * @param action viewModelに指定した処理
     */
    private fun actionAfterNetworkError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.NetworkError)
        actionAfterNetworkError()
    }

    /**
     * ネットワークエラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterNetworkError() {
        // custom error action
    }

    /**
     * ネットワーク設定無効エラーのメッセージを画面に表示
     */
    open fun showNetworkNotConnectError(
        e: Exception?,
        action: ((Constants.ApiErrorType) -> Unit)? = null
    ) {
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            getString(R.string.dialog_network_not_connect_error_message)
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterNetworkNotConnectError(action)
//        }
    }

    /**
     * ネットワーク設定無効エラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterNetworkNotConnectError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.NetworkNotConnectError)
        actionAfterNetworkNotConnectError()
    }

    /**
     * ネットワーク設定無効エラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterNetworkNotConnectError() {
        // custom error action
    }

    /**
     * JSONエラーのメッセージを画面に表示
     */
    open fun showJsonError(e: Exception?, action: ((Constants.ApiErrorType) -> Unit)? = null) {
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            getString(R.string.dialog_json_error_message)
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterJsonError(action)
//        }
    }

    /**
     * JSONエラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterJsonError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.JsonError)
        actionAfterJsonError()
    }

    /**
     * JSONエラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterJsonError() {
        // custom error action
    }

    /**
     * 想定しないエラーのメッセージを画面に表示
     */
    open fun showOtherError(error: ApiError, action: ((Constants.ApiErrorType) -> Unit)? = null) {
//        showErrorMessage(
//            getString(R.string.dialog_error_common_title),
//            getString(R.string.dialog_error_common_message)
//        ) { _, _ ->
//            isDialogShow = false
//            actionAfterOtherError(action)
//        }
    }

    /**
     * 想定しないエラーダイアログの「OK」ボタンがタップした後の処理
     */
    private fun actionAfterOtherError(action: ((Constants.ApiErrorType) -> Unit)? = null) {
        action?.invoke(Constants.ApiErrorType.OtherError)
        actionAfterOtherError()
    }

    /**
     * 想定しないエラーダイアログの「OK」ボタンがタップした後の処理
     *
     */
    open fun actionAfterOtherError() {
        // custom error action
    }

    /**
     * 共通のエラーメッセージを表示する
     *
     * @param message エラーメッセージ
     * @param positiveListener 「OK」ボタンの動作、デフォルトはダイアログを消す
     */
    private fun showErrorMessage(
        title: String,
        message: String,
        positiveListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> }
    ) {
        if (isDialogShow) {
            return
        }
        isDialogShow = true
        parentFragmentManager.also {
            CommonDialog<CommonDialog<*>>()
                .title(title)
                .message(message)
                .positiveTitle(getString(R.string.dialog_ok_button))
                .onPositiveListener(positiveListener)
                .isCancelable(false)
                .isCancelOutside(false)
                .show(it)
        }
    }

    /**
     *  共通のDialog
     */
    fun showCommonDialog(
        title: String,
        message: String? = null,
        positiveButtonText: String = getString(R.string.dialog_ok_button),
        positiveListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ -> }
    ) {
        if (isDialogShow) {
            return
        }
        isDialogShow = true
        parentFragmentManager.also {
            CommonDialog<CommonDialog<*>>()
                .title(title)
                .message(message ?: "")
                .positiveTitle(positiveButtonText)
                .onPositiveListener(positiveListener)
                .isCancelable(false)
                .isCancelOutside(false)
                .show(it)
        }
    }
}