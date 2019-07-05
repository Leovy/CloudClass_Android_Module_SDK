package com.example.xianxueliang.uicomponent

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.view.ViewPager
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * SupportSlidingTabLayout Plugins: Tab Title 底部对齐插件
 */
class AlignBottomPlugin private constructor(tabLayout: SupportSlidingTabLayout) {

    @Suppress("unused")
    private val context: Context by lazy { tabLayout.context }

    init {
        tabLayout.addOnTabAttributeUpdateListener { _, _, _ ->
            val parent = tabLayout.getChildAt(0) as LinearLayout
            for (index in 0 until parent.childCount) {
                val tabContainer: RelativeLayout = parent.getChildAt(index) as RelativeLayout

                //For Test
                runBlockForTest {

                    tabContainer.setBackgroundColor(Color.argb(55, 255, 0, 0))

                    (0 until tabContainer.childCount).map { tabContainer.getChildAt(it) }
                            .filterIsInstance(TextView::class.java)
                            .first()
                            .let {
                                it.setBackgroundColor(Color.argb(55, 0, 255, 0))
                            }

//                    tabContainer.addView(
//                            View(context).applyTo { setBackgroundColor(Color.argb(55, 0, 0, 255)) },
//                            RelativeLayout.LayoutParams(500, 500).applyTo {
//                                //                                addRule(RelativeLayout.END_OF, SupportSlidingTabLayout.ID_RES_TAB_TEXT_VIEW)
//                                marginStart = 500
//                            }
//                    )
                }
                val params = tabContainer.layoutParams as LinearLayout.LayoutParams
                params.gravity = Gravity.BOTTOM
                if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    tabContainer.layoutParams = params
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun applyTo(tabLayout: SupportSlidingTabLayout) {
            AlignBottomPlugin(tabLayout)
        }

        private fun runBlockForTest(@Suppress("UNUSED_PARAMETER") block: () -> Unit) {
//            block()
        }
    }
}

/**
 * SupportSlidingTabLayout Plugins: 平滑的缩放Tab Title插件
 * Note:只有[SupportSlidingTabLayout.mSelectedTextSize] > [SupportSlidingTabLayout.mUnselectedTextSize]
 * 的时候生效
 */
class SmoothScaleTitleSizePlugin private constructor(tabLayout: SupportSlidingTabLayout) {

    init {
        val maxTitleSize: Float = tabLayout.selectedTextSize
        val minTitleSize: Float = tabLayout.unselectedTextSize

        if (maxTitleSize > minTitleSize) {
            tabLayout.viewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    if (position + 1 < tabLayout.tabCount) {

                        val currentSize = (minTitleSize - maxTitleSize) * positionOffset + maxTitleSize
                        tabLayout.getTabTextViewAt(position)!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentSize)

                        val nextSize = (maxTitleSize - minTitleSize) * positionOffset + minTitleSize
                        tabLayout.getTabTextViewAt(position + 1)!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, nextSize)
                    }
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun applyTo(tabLayout: SupportSlidingTabLayout) {
            SmoothScaleTitleSizePlugin(tabLayout)
        }
    }
}