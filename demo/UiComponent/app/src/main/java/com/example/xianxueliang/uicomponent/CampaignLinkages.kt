package com.example.xianxueliang.uicomponent

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import com.allin.basefeature.common.widget.BaseDialog
import com.allin.commlibrary.system.DeviceUtils
import kotlinx.android.synthetic.main.dialog_campaign_linkages.*
import kotlinx.android.synthetic.main.fragment_list_campaign_linkages.*

class LinkageDialog : BaseDialog() {
    companion object {

        @JvmStatic
        fun newInstance(): LinkageDialog {
            val dialog = LinkageDialog()
            return dialog
        }
    }

    override fun getDialogStyle(): Int = theme

    override fun getContentView(): Int = R.layout.dialog_campaign_linkages

    override fun getWindowAnimations(): Int = R.style.AnimBottom

    override fun onInitContentView(dialog: Dialog) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentView, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val campaignLinkagesTabLayout = view.findViewById<SupportSlidingTabLayout>(R.id.campaignLinkagesTabLayout)
        val campaignLinkagesSubmit = view.findViewById<TextView>(R.id.campaignLinkagesSubmit)
        val campaignLinkagesViewPager = view.findViewById<ViewPager>(R.id.campaignLinkagesViewPager)


        campaignLinkagesTabLayout.setViewPager(campaignLinkagesViewPager
                , arrayOf("AAAA", "BBBB")
                , childFragmentManager
                , listOf(LinkageListFragment(), LinkageListFragment()))

        campaignLinkagesTabLayout.currentTab = 0

    }

    override fun onResetWindowAttributes(window: Window) {
        super.onResetWindowAttributes(window)
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.getScreenHeight(mContext) / 2)
    }
}

class LinkageListFragment : Fragment() {
    private lateinit var mContext: Context
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_campaign_linkages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter<String>(mContext
                , R.layout.item_list_campaign_linkages
                , R.id.campaignLinkagesListItemText
                , arrayOf("放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的")
        )
        linkageListFragmentListView.adapter = adapter
    }
}