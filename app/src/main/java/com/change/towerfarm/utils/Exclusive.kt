package com.change.towerfarm.utils

import android.os.Handler
import android.os.Looper
import timber.log.Timber

/**
 * 連打を防止
 *
 */
sealed class Exclusive {

    private var mAbortRunnable: Runnable? = null

    private fun abort(runnable: Runnable?): Exclusive? {
        mAbortRunnable = runnable
        return this
    }

    private fun makeListener(runnable: Runnable?): ExclusiveListener<*> {
        return object : ExclusiveListener<Void?> {
            override fun onExclusive(): Void? {
                runnable?.run()
                return null
            }

            override fun onAbort() {
                if (mAbortRunnable != null) {
                    mAbortRunnable!!.run()
                }
            }
        }
    }

    private fun go(runnable: Runnable?) {
        switchTab(makeListener(runnable))
    }

    private fun start(runnable: Runnable?) {
        switchView(makeListener(runnable))
    }

    private fun tap(runnable: Runnable?) {
        normalAction(makeListener(runnable))
    }
    private fun shortTap(runnable: Runnable?) {
        shortAction(makeListener(runnable))
    }

    interface ExclusiveListener<T> {
        fun onExclusive(): T
        fun onAbort()
    }

    companion object {

        private val mExclusiveHandler = Handler(Looper.getMainLooper())
        private var isQuickPushpop = false
        private var isTabSwitch = false
        private var isQuickClick = false

        fun tab(): TabExclusive {
            return TabExclusive()
        }

        fun activity(): ActivityExclusive {
            return ActivityExclusive()
        }

        fun normal(): NormalExclusive {
            return NormalExclusive()
        }

        class ActivityExclusive : Exclusive() {
            fun abort(runnable: Runnable?): ActivityExclusive {
                super.abort(runnable)
                return this
            }

            fun start(runnable: Runnable?) {
                super.start(runnable)
            }
        }

        class TabExclusive : Exclusive() {
            fun abort(runnable: Runnable?): TabExclusive {
                super.abort(runnable)
                return this
            }

            fun go(runnable: Runnable?) {
                super.go(runnable)
            }
        }

        class NormalExclusive : Exclusive() {
            fun abort(runnable: Runnable?): NormalExclusive {
                super.abort(runnable)
                return this
            }

            fun tap(runnable: Runnable?) {
                super.tap(runnable)
            }
            fun shortTap(runnable: Runnable?) {
                super.shortTap(runnable)
            }
        }

        /**
         * Tab切替の場合にはexecuteを使って
         *
         */
        fun <T> switchTab(el: ExclusiveListener<T>): T? {
            if (isTabSwitch) {
                Timber.w("Exclusive Touch : Abort this Tab switch because the last not completed.")
                el.onAbort()
                return null
            }
            isTabSwitch = true
            val t = el.onExclusive()
            mExclusiveHandler.postDelayed({ isTabSwitch = false }, longTime.toLong())
            return t
        }

        /**
         * 画面Pop、Pushの場合にはexecuteを使って
         *
         */
        fun <T> switchView(el: ExclusiveListener<T>): T? {
            if (isQuickPushpop) {
                Timber.w("Exclusive Touch : Abort this operation because the last not completed.")
                el.onAbort()
                return null
            }
            isQuickPushpop = true
            val t = el.onExclusive()
            mExclusiveHandler.postDelayed({ isQuickPushpop = false }, longTime.toLong())
            return t
        }

        /**
         * 早速タップを防止する場合にはclickを使って
         * 画面遷移の途中にタップを許さないようにする
         */
        fun <T> normalAction(el: ExclusiveListener<T>): T? {
            if (isQuickClick || isQuickPushpop) {
                Timber.w("Exclusive Touch : Abort because clicking is fast.")
                return null
            }
            isQuickClick = true
            val t = el.onExclusive()
            mExclusiveHandler.postDelayed(recovery, longTime.toLong())
            return t
        }

        fun <T> shortAction(el: ExclusiveListener<T>): T? {
            if (isQuickClick || isQuickPushpop) {
                Timber.w("Exclusive Touch : Abort because clicking is fast.")
                return null
            }
            isQuickClick = true
            val t = el.onExclusive()
            mExclusiveHandler.postDelayed(recovery, shortTime.toLong())
            return t
        }

        private const val longTime = 500
        private const val shortTime = 100
        private val recovery = Runnable { isQuickClick = false }

        fun recoveryClick() {
            mExclusiveHandler.removeCallbacks(recovery)
            mExclusiveHandler.postDelayed(recovery, shortTime.toLong())
        }
    }
}