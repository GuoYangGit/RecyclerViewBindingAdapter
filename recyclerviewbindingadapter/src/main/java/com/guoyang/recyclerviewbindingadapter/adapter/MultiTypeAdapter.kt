package com.guoyang.recyclerviewbindingadapter.adapter

import android.content.Context
import android.content.res.Resources
import android.databinding.*
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.ViewGroup
import com.guoyang.recyclerviewbindingadapter.observable.ObservableAdapterList

/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by guoyang on 2018/8/20.
 * github https://github.com/GuoYangGit
 * QQ:352391291
 */

class MultiTypeAdapter(context: Context, list: ObservableAdapterList<Any>, val multiTye: MultiViewType) : BindingViewAdapter<Any>(context, list) {
    private var mCollectionViewType: MutableList<Int> = mutableListOf()

    private val mItemTypeToLayoutMap = ArrayMap<Int, Int>()

    init {
        initMultiTypeList()
    }

    private fun initMultiTypeList() {
        list.addAdapterChangedCallback(object :ObservableAdapterList.OnAdapterChangedCallback{
            override fun notifyAdapter() {
                notifyDataSetChanged()
            }

        })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> =
            BindingViewHolder(
                    DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, getLayoutRes(viewType), parent, false)
            )

    fun addViewTypeToLayoutMap(viewType: Int?, layoutRes: Int?) {
        mItemTypeToLayoutMap.put(viewType, layoutRes)
    }

    override fun getItemViewType(position: Int): Int = mCollectionViewType[position]

    @LayoutRes
    private fun getLayoutRes(viewType: Int): Int =
            mItemTypeToLayoutMap[viewType]
                    ?: throw Resources.NotFoundException("$viewType has not registered")


    interface MultiViewType {
        fun getViewType(item: Any): Int
    }
}