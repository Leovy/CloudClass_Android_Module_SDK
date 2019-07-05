package com.example.xianxueliang.uicomponent

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.allin.basicres.utils.ViewUtils
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * SupportSlidingTabLayout Plugin: 用于下载模块
 */
class DownloadTabPlugin private constructor(private val tabLayout: SupportSlidingTabLayout) : DownloadTabPluginApi {

    companion object {

        @JvmStatic
        fun apply(tabLayout: SupportSlidingTabLayout): DownloadTabPluginApi {
            return DownloadTabPlugin(tabLayout)
        }
    }

    private val context: Context by lazy { tabLayout.context }

    init {

        tabLayout.eachTabView { index, tabView ->
            tabView.setPadding(AutoSizeUtils.dp2px(context, 15f), 0, AutoSizeUtils.dp2px(context, 15f), 0)
            with(tabView as RelativeLayout) {
                val pluginView = LayoutInflater.from(context).inflate(R.layout.layout_download_tab_plugin, null)
                addView(pluginView, run {
                    val params = RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.marginStart = (tabView.getChildAt(0) as TextView).measuredWidth().toInt()
                    params.marginStart += AutoSizeUtils.dp2px(context, 6f)
                    params.topMargin = if (index == tabLayout.currentTab) {
                        AutoSizeUtils.dp2px(context, 10f)
                    } else {
                        0
                    }
                    params
                })
                ViewUtils.setVisibility(pluginView, View.GONE)
            }
        }

        tabLayout.addOnTabAttributeUpdateListener { tabTextView, curIndex, tabIndex ->

            val layoutParams = tabTextView.layoutParams as RelativeLayout.LayoutParams
            layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT)
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            tabTextView.layoutParams = layoutParams


            val subscriptView = (tabTextView.parent as ViewGroup).findSubscriptView()
            subscriptView.setTextColor(if (curIndex == tabIndex) Color.parseColor("#222222") else Color.parseColor("#777777"))

            val pluginView = (tabTextView.parent as RelativeLayout).getChildAt(2)
            val params = pluginView.layoutParams as RelativeLayout.LayoutParams
            params.marginStart = tabTextView.measuredWidth().toInt()
            params.marginStart += AutoSizeUtils.dp2px(context, 6f)
            params.topMargin = if (curIndex == tabIndex) {
                AutoSizeUtils.dp2px(context, 10f)
            } else {
                0
            }
            pluginView.layoutParams = params
        }
    }

    private fun TextView.measuredWidth(): Float = paint.measureText(text.toString())

    private fun View.findDotView(): View = findViewById<View>(R.id.dotView)

    private fun View.findSubscriptView(): TextView {
        val subscriptView = findViewById<View>(R.id.subscriptView)
        return if (subscriptView is TextView) {
            subscriptView
        } else {
            throw IllegalStateException("$subscriptView is not instanceOf TextView")
        }
    }

    private fun redDotDrawable(context: Context): Drawable {
        return GradientDrawable().apply {
            setSize(AutoSizeUtils.dp2px(context, 10f), AutoSizeUtils.dp2px(context, 10f))
            setColor(Color.parseColor("#FF4F4F"))
            shape = GradientDrawable.OVAL
        }
    }

    private fun checkTabIndex(tabIndex: Int) {
        if (tabIndex !in 0 until tabLayout.tabCount) {
            throw IllegalArgumentException("tabIndex is $tabIndex, required in range from 0 to ${tabLayout.tabCount - 1}")
        }
    }

    /**
     * 根据[dotView]和[subscriptView]的状态决定PluginView的可见性
     */
    private fun visibilityOfPluginView(dotView: View, subscriptView: TextView): Int {
        return if (dotView.visibility == View.VISIBLE || subscriptView.visibility == View.VISIBLE){
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 在[tabIndex]的位置上显示红点
     */
    override fun showDotAt(tabIndex: Int) {
        checkTabIndex(tabIndex)
        val pluginView = (tabLayout.getTabTextViewAt(tabIndex)!!.parent as ViewGroup).getChildAt(2)
        val dotView = pluginView.findDotView()
        val subscriptView = pluginView.findSubscriptView()
        dotView.background = redDotDrawable(context)
        ViewUtils.setVisibility(dotView, View.VISIBLE)

        ViewUtils.setVisibility(pluginView, visibilityOfPluginView(dotView, subscriptView))
    }

    /**
     * 在[tabIndex]的位置上隐藏红点
     */
    override fun hideDotAt(tabIndex: Int) {
        checkTabIndex(tabIndex)
        val pluginView = (tabLayout.getTabTextViewAt(tabIndex)!!.parent as ViewGroup).getChildAt(2)
        val dotView = pluginView.findDotView()
        val subscriptView = pluginView.findSubscriptView()
        ViewUtils.setVisibility(dotView, View.GONE)

        ViewUtils.setVisibility(pluginView, visibilityOfPluginView(dotView, subscriptView))
    }

    /**
     * 在[tabIndex]的位置上显示角标数字[num],如果[num] is null,则不显示
     */
    override fun showSubscriptNumberAt(tabIndex: Int, num: String?) {
        checkTabIndex(tabIndex)
        val pluginView = (tabLayout.getTabTextViewAt(tabIndex)!!.parent as ViewGroup).getChildAt(2)
        val dotView = pluginView.findDotView()
        val subscriptView = pluginView.findSubscriptView()
        ViewUtils.setVisibility(subscriptView, View.VISIBLE)
        subscriptView.text = num

        ViewUtils.setVisibility(pluginView, visibilityOfPluginView(dotView, subscriptView))
    }
}

interface DownloadTabPluginApi {
    /**
     * 在[tabIndex]的位置上显示红点
     */
    fun showDotAt(tabIndex: Int)
    /**
     * 在[tabIndex]的位置上隐藏红点
     */
    fun hideDotAt(tabIndex: Int)
    /**
     * 在[tabIndex]的位置上显示角标数字[num],如果[num] is null,则不显示
     */
    fun showSubscriptNumberAt(tabIndex: Int, num: String?)
}