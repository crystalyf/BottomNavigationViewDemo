package com.change.towerfarm.views

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.change.towerfarm.adapter.AsideSensorListAdapter
import com.change.towerfarm.R
import com.change.towerfarm.viewmodels.MainViewModel

/**
 * Sensor information listサイドバーを表示
 */
class AsidePopupWindow(
    private val context: Context,
    activity: FragmentActivity,
    viewModel: MainViewModel?
) : PopupWindow(), View.OnClickListener {
    private val contentViews: View
    private val activity: FragmentActivity = activity
    private val faceWarnMultiTypeAdapter: AsideSensorListAdapter
    private val popupWindow: PopupWindow
    private val mRecyclerView: RecyclerView
    private val img_close: ImageView
    private val rel_close: RelativeLayout
    fun showWindow(view: View) {
        //画面Width
        val wm1: WindowManager = activity.windowManager
        val screenWidth: Int = wm1.defaultDisplay.width
        if (!this.isShowing) {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            this.showAtLocation(view, Gravity.NO_GRAVITY, screenWidth,  0)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rel_close -> {
                popupWindow.dismiss()
            }
        }
    }

    init {
        popupWindow = this
        contentViews = LayoutInflater.from(context).inflate(R.layout.aside_popwindow, null)
        this.contentView = contentViews
        //popWindow height
        this.height = context.resources.getDimensionPixelSize(R.dimen.aside_popwindow_height)
        //popWindow width
        this.width = 450
        this.setBackgroundDrawable(BitmapDrawable())
        //焦点を合わせる
        this.isFocusable = false
        //外をクリックすると消えます
        this.isOutsideTouchable = true
        this.isTouchable = true
        //アニメーション効果
        this.animationStyle = R.style.AnimationRightFade
        img_close = contentView.findViewById(R.id.img_close)
        mRecyclerView = contentView.findViewById(R.id.rcv_warning)
        rel_close = contentView.findViewById(R.id.rel_close)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        faceWarnMultiTypeAdapter =
            AsideSensorListAdapter(viewModel, viewModel?.sensorInformationList?.value)
        mRecyclerView.adapter = faceWarnMultiTypeAdapter
        rel_close.setOnClickListener(this)
    }
}