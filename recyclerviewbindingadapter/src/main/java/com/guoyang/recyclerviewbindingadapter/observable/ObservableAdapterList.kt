package com.guoyang.recyclerviewbindingadapter.observable

import android.databinding.ObservableArrayList

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */
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
 * Created by guoyang on 2018/10/31.
 * github https://github.com/GuoYangGit
 * QQ:352391291
 */
class ObservableAdapterList<T> : ObservableArrayList<T>() {
    private var onAdapterChangedCallback: OnAdapterChangedCallback? = null

    fun addAdapterChangedCallback(onAdapterChangedCallback: OnAdapterChangedCallback) {
        this.onAdapterChangedCallback = onAdapterChangedCallback
    }

    fun setNewData(collection: Collection<T>) {
        this.clear(false)
        this.addAll(collection)
    }

    override fun add(element: T): Boolean {
        val success = super.add(element)
        if (success) onAdapterChangedCallback?.notifyAdapter()
        return success
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        onAdapterChangedCallback?.notifyAdapter()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val success = super.addAll(elements)
        if (success) onAdapterChangedCallback?.notifyAdapter()
        return success
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val success = super.addAll(index, elements)
        if (success) onAdapterChangedCallback?.notifyAdapter()
        return success
    }

    fun clear(notify: Boolean = true) {
        clear()
        if (notify) onAdapterChangedCallback?.notifyAdapter()
    }

    override fun remove(element: T): Boolean {
        val success = super.remove(element)
        if (success) onAdapterChangedCallback?.notifyAdapter()
        return success
    }

    override fun removeAt(index: Int): T {
        onAdapterChangedCallback?.notifyAdapter()
        return super.removeAt(index)
    }

    override fun set(index: Int, element: T): T {
        onAdapterChangedCallback?.notifyAdapter()
        return super.set(index, element)
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        onAdapterChangedCallback?.notifyAdapter()
        super.removeRange(fromIndex, toIndex)
    }


    interface OnAdapterChangedCallback {
        fun notifyAdapter()
    }
}