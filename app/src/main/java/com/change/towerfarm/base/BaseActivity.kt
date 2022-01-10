package com.change.towerfarm.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.change.towerfarm.utils.Utils
import com.change.towerfarm.R

abstract class BaseActivity : AppCompatActivity() {

    abstract var layout: Int?

    private var mProgressDialog: Dialog? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Utils.timberTrace(this::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.timberTrace(this::class.java)
        layout?.let {
            setContentView(it)
        }
    }

    override fun onStart() {
        super.onStart()
        Utils.timberTrace(this::class.java)
    }

    override fun onResume() {
        super.onResume()
        Utils.timberTrace(this::class.java)
    }

    override fun onStop() {
        super.onStop()
        Utils.timberTrace(this::class.java)
    }

    override fun onPause() {
        super.onPause()
        Utils.timberTrace(this::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.timberTrace(this::class.java)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        Utils.timberTrace(this::class.java)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Utils.timberTrace(this::class.java)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Utils.timberTrace(this::class.java)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            Utils.hideSoftKeyboard(this)
        }
        return super.onTouchEvent(event)
    }

    fun showBlockingProgress() {
        if (this.isFinishing) {
            return
        }
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(this, R.style.commonContentTransparent)
            mProgressDialog!!.setContentView(R.layout.view_common_dialog_progress)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }
    }

    fun hideBlockingProgress() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

}