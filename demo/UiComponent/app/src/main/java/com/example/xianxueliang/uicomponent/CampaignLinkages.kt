package com.example.xianxueliang.uicomponent

import android.app.Dialog
import android.arch.lifecycle.*
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.allin.basefeature.common.widget.BaseDialog
import com.allin.commlibrary.system.DeviceUtils
import kotlinx.android.synthetic.main.fragment_list_campaign_linkages.*

class LinkageDialog : BaseDialog() {
    private lateinit var campaignLinkagesTabLayout: SupportSlidingTabLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MySharedViewModel.instance(context as FragmentActivity)
                .hostPageLiveData
                .observe(this, object : LiveMessageObserver<HostPageData> {
                    override fun safeOnChanged(data: HostPageData) {
                        when (val msg = data.message) {
                            is ItemClickMessage -> {
                                Toast.makeText(activity, "click ${msg.name}", Toast.LENGTH_SHORT).show()
                                campaignLinkagesTabLayout.addTab(msg.name, LinkageListFragment())
                            }
                            else -> {
                            }
                        }
                    }

                    override fun getTarget(): String = HostPageData.HOST_PAGE_NAME
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentView, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campaignLinkagesTabLayout = view.findViewById(R.id.campaignLinkagesTabLayout)
        val campaignLinkagesSubmit = view.findViewById<TextView>(R.id.campaignLinkagesSubmit)
        val campaignLinkagesViewPager = view.findViewById<ViewPager>(R.id.campaignLinkagesViewPager)
        campaignLinkagesViewPager.offscreenPageLimit = Int.MAX_VALUE

        campaignLinkagesTabLayout.setViewPager(campaignLinkagesViewPager
                , arrayOf("AAAA", "BBBB")
                , childFragmentManager
                , mutableListOf(LinkageListFragment(), LinkageListFragment()) as List<Fragment>)

        campaignLinkagesTabLayout.currentTab = 0

        campaignLinkagesSubmit.setOnClickListener {
            campaignLinkagesTabLayout.removeTabAt(campaignLinkagesTabLayout.tabCount - 1)
        }
    }

    override fun onResetWindowAttributes(window: Window) {
        super.onResetWindowAttributes(window)
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.getScreenHeight(mContext) / 2)
    }
}

/**
 * 列表页面
 */
internal class LinkageListFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var mSharedViewModel: MySharedViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = MySharedViewModel.instance(context as FragmentActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_campaign_linkages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataArr = arrayOf("放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的"
                , "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的", "放进口袋罗斯福角色的")

        val adapter = ArrayAdapter<String>(mContext
                , R.layout.item_list_campaign_linkages
                , R.id.campaignLinkagesListItemText
                , dataArr
        )
        linkageListFragmentListView.adapter = adapter

        linkageListFragmentListView.setOnItemClickListener { _, _, position, _ ->
            linkageListFragmentListView.setSelection(position)
            mSharedViewModel.sendToHost(HostPageData(ItemClickMessage(dataArr[position])))
        }
    }
}

/**
 * 用于向Host发送信息的数据包
 */
private class HostPageData(val message: HostPageMessage) : LiveMessageObserver.TargetData(HOST_PAGE_NAME) {
    companion object {
        val HOST_PAGE_NAME = LinkageDialog::class.java.toString()
    }
}

/**
 * 用于标记信息类型
 */
private interface HostPageMessage

/**
 * 列表Item被点击的消息类型
 */
private data class ItemClickMessage(val name: String) : HostPageMessage

/***
 * 用于ListFragment和Host通信
 */
private class MySharedViewModel : ViewModel() {
    /**
     * receive message from host
     */
    val hostPageLiveData = MutableLiveData<HostPageData>()

    /**
     * send message from list
     */
    fun sendToHost(data: HostPageData) {
        LiveMessageObserver.LiveMessage.obtain(hostPageLiveData, data).sendToTarget()
    }

    object MyViewModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (MySharedViewModel::class.java.isAssignableFrom(modelClass)) {

                try {
                    return modelClass.newInstance()
                } catch (e: IllegalAccessException) {
                    throw RuntimeException("Cannot create an instance of $modelClass", e)
                } catch (e: InstantiationException) {
                    throw RuntimeException("Cannot create an instance of $modelClass", e)
                }

            }
            throw IllegalStateException("MySharedViewModel is not assignable from $modelClass")
        }
    }

    companion object {
        fun instance(activity: FragmentActivity): MySharedViewModel =
                ViewModelProviders.of(activity, MyViewModelFactory)[MySharedViewModel::class.java]
    }
}