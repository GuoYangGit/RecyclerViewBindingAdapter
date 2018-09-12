package com.guoyang.recyclerviewbindingadapter.adapter

import android.content.Context
import android.content.res.Resources
import android.databinding.*
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.ViewGroup

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

class MultiTypeAdapter(context: Context, list: ObservableArrayList<Any>, val multiTyper: MultiViewTyper) : BindingViewAdapter<Any>(context, list) {
    private var mCollectionViewType: MutableList<Int> = mutableListOf()

    private val mItemTypeToLayoutMap = ArrayMap<Int, Int>()

    init {
        initMultiTypeList()
    }

    private fun initMultiTypeList() {
        list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Any>>() {
            override fun onChanged(sender: ObservableList<Any>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                for (i in positionStart + itemCount - 1 downTo positionStart) mCollectionViewType.removeAt(i)
                if (sender?.isNotEmpty() == true) {
                    notifyItemRangeRemoved(positionStart, itemCount)
                } else {
                    mLastPosition = -1
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeMoved(sender: ObservableList<Any>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                sender?.run {
                    (positionStart until positionStart + itemCount).forEach {
                        mCollectionViewType.add(it, multiTyper.getViewType(this[it]))
                    }
                    notifyItemRangeInserted(positionStart, itemCount)
                }
            }

            override fun onItemRangeChanged(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
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


    interface MultiViewTyper {
        fun getViewType(item: Any): Int
    }
}