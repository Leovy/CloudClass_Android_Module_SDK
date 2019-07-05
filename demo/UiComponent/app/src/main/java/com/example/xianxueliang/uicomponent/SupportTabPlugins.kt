package com.example.xianxueliang.uicomponent

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Tab Title 底部对齐插件
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
//                            View(context).apply { setBackgroundColor(Color.argb(55, 0, 0, 255)) },
//                            RelativeLayout.LayoutParams(500, 500).apply {
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
        fun apply(tabLayout: SupportSlidingTabLayout) {
            AlignBottomPlugin(tabLayout)
        }

        private fun runBlockForTest(@Suppress("UNUSED_PARAMETER") block: () -> Unit) {
//            block()
        }
    }
}