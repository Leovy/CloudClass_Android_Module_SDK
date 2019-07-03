package com.example.xianxueliang.uicomponent

import android.app.Dialog
import android.arch.lifecycle.*
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.*
import com.allin.basefeature.common.widget.BaseDialog
import com.allin.commlibrary.system.DeviceUtils
import kotlinx.android.synthetic.main.fragment_list_campaign_linkages.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class LinkageDialog : BaseDialog() {
    private lateinit var campaignLinkagesTabLayout: SupportSlidingTabLayout
    private lateinit var tree: ArrayList<CampaignTree>

    companion object {
        private const val ARG_TREES = "ARG_TREES"
        @JvmStatic
        fun newInstance(tree: ArrayList<CampaignTree>): LinkageDialog {
            if (tree.isNullOrEmpty()) {
                throw IllegalArgumentException("tree must be not empty")
            }
            return LinkageDialog().apply {
                arguments = Bundle().apply { putSerializable(ARG_TREES, tree) }
            }
        }

        /**
         * 显示这个Dialog
         */
        @JvmStatic
        fun show(fragmentManager: FragmentManager, tree: ArrayList<CampaignTree>) {
            newInstance(tree).show(fragmentManager, UUID.randomUUID().toString())
        }
    }

    override fun getDialogStyle(): Int = theme

    override fun getContentView(): Int = R.layout.dialog_campaign_linkages

    override fun getWindowAnimations(): Int = R.style.AnimBottom

    override fun onInitContentView(dialog: Dialog) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            @Suppress("UNCHECKED_CAST")
            tree = getSerializable(ARG_TREES) as ArrayList<CampaignTree>
        }
        MySharedViewModel.instance(context as FragmentActivity)
                .hostPageLiveData
                .observe(this, object : LiveMessageObserver<HostPageData> {
                    override fun safeOnChanged(data: HostPageData) {
                        when (val msg = data.message) {
                            is ItemClickMessage -> {
                                val currentNode = msg.node
                                val currentTabIndex = msg.tabIndex
                                Toast.makeText(activity, "click $currentNode", Toast.LENGTH_SHORT).show()
                                /*
                                    1.更新tabTitle(如果点击了和选中不一样的元素)
                                    2.移除当前tabIndex后的所有tab
                                    3.如果有下一级节点的话,添加下一级节点并且设置首元素为tabTitle
                                 */
                                campaignLinkagesTabLayout.getTabTextViewAt(currentTabIndex)!!.text = currentNode.name

                                val startIndex = currentTabIndex + 1
                                val endIndex = campaignLinkagesTabLayout.tabCount - 1
                                if (startIndex <= endIndex) {
                                    repeat(campaignLinkagesTabLayout.tabCount - startIndex) {
                                        campaignLinkagesTabLayout.removeTabAt(startIndex)

                                    }
                                }

                                find(currentNode, tree) { owningList ->
                                    //清除当前节点以及以下所有的选中状态
                                    owningList.eachNode { it.isSelected = false }

                                    //调整选中的节点
                                    owningList.forEach {
                                        it.isSelected = it.id == currentNode.id
                                    }

                                    if (currentNode.hasChildren()) {
                                        val nodes = ArrayList<Pair<CampaignTree, ArrayList<CampaignTree>>>()
                                        filterSelectedNode(owningList){ node, nodeList ->
                                            if (node.id != currentNode.id) {
                                                nodes.add(node to nodeList)
                                            }
                                        }
                                        campaignLinkagesTabLayout.run {
                                            nodes.forEachIndexed { index, pair ->
                                                addTab(pair.first.name!!, LinkageListFragment.newInstance(pair.second, currentTabIndex + 1 + index))
                                            }
                                            setCurrentTab(currentTabIndex + 1, true)
                                        }
                                    }
                                }
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

        campaignLinkagesSubmit.setOnClickListener {
            val selectedNodes = ArrayList<CampaignTree>()
            tree.eachNode {
                if (it.isSelected) {
                    selectedNodes.add(it)
                }
            }

            selectedNodes.joinTo(StringBuilder(), separator = "/") { it.name!! }.toString().also { Log.i("CampaignLin", it) }
        }

        //Load Tree
        /*
            查询集合中所有isSelect的Tree
         */
        val nodes = ArrayList<Pair<CampaignTree, ArrayList<CampaignTree>>>()
        filterSelectedNode(tree) { node, nodeList -> nodes.add(node to nodeList) }

        campaignLinkagesViewPager.offscreenPageLimit = Int.MAX_VALUE
        campaignLinkagesTabLayout.setViewPager(campaignLinkagesViewPager
                , nodes.map { it.first.name }.toTypedArray()
                , childFragmentManager
                , nodes.mapIndexed { index, pair -> LinkageListFragment.newInstance(pair.second, index) }.toMutableList() as List<Fragment>)
        campaignLinkagesTabLayout.currentTab = 0
    }

    /**
     * 遍历指定tree下的所有node
     */
    private fun ArrayList<CampaignTree>.eachNode(consumer: (CampaignTree) -> Unit) {
        forEach {
            consumer(it)
            if (it.hasChildren()) {
                it.children!!.eachNode(consumer)
            }
        }
    }

    /**
     * 查询指定节点在那个节点树下
     */
    private fun find(node: CampaignTree, tree: ArrayList<CampaignTree>, consumer: (ArrayList<CampaignTree>) -> Unit){
        tree.forEach {
            if (it.id == node.id) {
                consumer(tree)
                return
            } else {
                if (it.hasChildren()) {
                    find(node, it.children!!, consumer)
                }
            }
        }
    }

    private fun filterSelectedNode(tree: ArrayList<CampaignTree>, block: (CampaignTree, ArrayList<CampaignTree>) -> Unit) {
        tree.firstOrNull { it.isSelected }.ifNull { tree.first().apply { isSelected = true } }.apply {
            block(this, tree)
            if (!children.isNullOrEmpty()) {
                filterSelectedNode(children!!, block)
            }
        }
    }

    /**
     * for chained call
     */
    private fun <T> T?.ifNull(defaultValue: () -> T): T {
        return this ?: defaultValue()
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
    private lateinit var nodes: ArrayList<CampaignTree>
    private var tabIndex: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            @Suppress("UNCHECKED_CAST")
            nodes = getSerializable(ARG_NODES) as ArrayList<CampaignTree>
            tabIndex = getInt(ARG_TAB_INDEX)
        }
        mSharedViewModel = MySharedViewModel.instance(context as FragmentActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_campaign_linkages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = object : ArrayAdapter<CampaignTree>(mContext
                , R.layout.item_list_campaign_linkages
                , R.id.campaignLinkagesListItemText
                , nodes
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val contentView = convertView
                        ?: LayoutInflater.from(context).inflate(R.layout.item_list_campaign_linkages, parent, false)
                val text: TextView = contentView.findViewById(R.id.campaignLinkagesListItemText)
                val item = getItem(position)
                item!!.apply {
                    text.text = name
                    text.setTextColor(getTextColor(item.isSelected))
                }
                return contentView
            }
        }
        linkageListFragmentListView.adapter = adapter

        linkageListFragmentListView.setOnItemClickListener { parent, _, position, _ ->
            //获取上次选中的item的索引
            val lastSelectedIndex = run loop@{
                parent.onEach { child, index ->
                    val text = child.findViewById<TextView>(R.id.campaignLinkagesListItemText)
                    if (text.currentTextColor == getTextColor(true)) {
                        return@loop index
                    }
                }
            }
            if (position != lastSelectedIndex) {
                //只有选中了和上次不一样的item才处理
                parent.onEach { child, index ->
                    child.findViewById<TextView>(R.id.campaignLinkagesListItemText)
                        .setTextColor(getTextColor(index == position)) }

                mSharedViewModel.sendToHost(HostPageData(ItemClickMessage(nodes[position], tabIndex)))
            }
        }
    }

    private inline fun AdapterView<*>.onEach(f: (View, Int) -> Unit) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            f(child, index)
        }
    }

    /**
     * 获取[isSelected]状态下文字的颜色
     */
    private fun getTextColor(isSelected: Boolean): Int {
        return if (isSelected) Color.parseColor("#FF6483E9") else Color.parseColor("#FF333333")
    }

    companion object {
        private const val ARG_NODES = "ARG_NODES"
        private const val ARG_TAB_INDEX = "ARG_TAB_INDEX"

        @JvmStatic
        fun newInstance(nodes: ArrayList<CampaignTree>, tabIndex: Int): LinkageListFragment {
            return LinkageListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_NODES, nodes)
                    putInt(ARG_TAB_INDEX, tabIndex)
                }
            }
        }
    }
}


/**
 * 用于向Host发送信息的数据包
 */
private class HostPageData(var message: HostPageMessage) : LiveMessageObserver.TargetData(HOST_PAGE_NAME) {
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
private data class ItemClickMessage(val node: CampaignTree, val tabIndex: Int) : HostPageMessage

/***
 * 用于ListFragment和Host通信
 */
private class MySharedViewModel : ViewModel() {
    /**
     * receive message from host
     */
    val hostPageLiveData = object :MutableLiveData<HostPageData>(){

        override fun onInactive() {
            super.onInactive()
            if (value != null) {
                value = null
            }
        }
    }

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

data class CampaignTree(
        var propertyFullPath: String? = null,
        var propertyId: String? = null,
        var isSelected: Boolean = false
) : CLTree<CampaignTree>()

open class CLTree<T : CLTree<T>>(
        var id: String? = null,
        var parentId: String? = null,
        var name: String? = null,
        var children: ArrayList<T>? = null
) {
    fun hasChildren(): Boolean = !children.isNullOrEmpty()
}

fun <T : CLTree<T>> parseTree(treeArr: JSONArray?, transform: (JSONObject) -> T): ArrayList<T>? {
    if (treeArr != null && treeArr.length() > 0) {
        @Suppress("UNCHECKED_CAST")
        return CLTree<T>().apply {
            parseInternal(this as T, treeArr, 2, transform)
        }.children as ArrayList<T>
    }
    return null
}

private fun <T : CLTree<T>> parseInternal(
        currentNode: T,
        nextTreeArr: JSONArray?,
        nodeIndex: Int,
        transform: (JSONObject) -> T
) {
    if (nextTreeArr != null && nextTreeArr.length() > 0) {
        var list: ArrayList<T>? = null
        for (index in 0 until nextTreeArr.length()) {
            val jo = nextTreeArr[index] as? JSONObject
            jo?.apply {
                val nextNode = transform(jo).apply {
                    this.id = optString("id")
                    this.parentId = optString("parentId")
                    this.name = optString("propertyName")
                }
                parseInternal(
                        nextNode,
                        optJSONArray("property_$nodeIndex"),
                        nodeIndex + 1,
                        transform
                )
                if (list == null) {
                    list = ArrayList()
                }

                list!!.add(nextNode)
            }
        }
        currentNode.children = list
    }
}

fun ArrayList<CampaignTree>.updateSelectedStateBy(selectedNodes: LinkedList<CampaignTree>): ArrayList<CampaignTree> {
    if (!selectedNodes.isNullOrEmpty()) {
        f(this, 0, selectedNodes)
    }
    return this
}

private fun f(source: ArrayList<CampaignTree>, index: Int, selectedNodes: LinkedList<CampaignTree>) {
    val node = selectedNodes.getOrNull(index)
    source.forEach {
        if (node != null && node.id == it.id) {
            if (it.hasChildren()) {
                val children = it.children!!
                f(children, index + 1, selectedNodes)
            }
        }
    }
}
