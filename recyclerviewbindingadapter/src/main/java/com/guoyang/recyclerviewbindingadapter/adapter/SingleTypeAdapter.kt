package com.guoyang.recyclerviewbindingadapter.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
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

open class SingleTypeAdapter<T>(context: Context, private val layoutRes: Int, list: ObservableAdapterList<T>) : BindingViewAdapter<T>(context, list) {

    init {
        initSingleList()
    }

    private fun initSingleList() {
        list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(toPosition, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                if (sender?.isNotEmpty() == true) {
                    notifyItemRangeChanged(positionStart, itemCount)
                } else {
                    mLastPosition = -1
                    notifyDataSetChanged()
                }
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> = BindingViewHolder(DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutRes, parent, false))
}