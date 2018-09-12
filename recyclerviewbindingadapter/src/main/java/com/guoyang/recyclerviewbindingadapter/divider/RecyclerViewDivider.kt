package com.guoyang.recyclerviewbindingadapter.divider

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.LinearLayout

/***
 *
 * █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 *
 * Created by guoyang on 2018/8/30.
 * github https://github.com/GuoYangGit
 * QQ:352391291
 * RecyclerView通用分割线
 * 用法:Divider.builder()
 *            .color(Color.BLUE)// 设颜色
 *            .width(10)// 设线宽px，用于画垂直线
 *            .height(20)// 设线高px，用于画水平线
 *            .headerCount(1)// 设头的数量
 *            .footerCount(1)// 设尾的数量
 *            .build()
 * 注意事项：
 * 如果增删不是调用的 adapter.notifyDataSetChanged() 则需要调用下面方法重新绘制线
 * recyclerView.invalidateItemDecorations()
 */
class RecyclerViewDivider internal constructor() : RecyclerView.ItemDecoration() {
    /**
     * 设置线Drawable，和setLineColor()二选一
     */
    var dividerDrawable: Drawable? = null
    private val DEFAULT_LINE_WIDTH = 1
    private val DEFAULT_LINE_HEIGHT = 1
    /**
     * 设置线宽
     */
    var lineWidth = DEFAULT_LINE_WIDTH
    /**
     * 设置线高
     */
    var lineHeight = DEFAULT_LINE_HEIGHT
    /**
     * 设置头数量，即头部跳过绘制
     */
    private var headerCount = 0
    /**
     * 设置尾数量，即尾部跳过绘制
     */
    private var footerCount = 0

    init {
        dividerDrawable = ColorDrawable(Color.GRAY)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (isSkipDraw(parent, view))
            return // 跳过，不绘制
        val currentPosition = parent.getChildAdapterPosition(view)
        val spanCount = getSpanCount(parent)// 水平个数，线性布局为-1
        val childCount = parent.adapter.itemCount// 总个数
        var right = lineWidth
        var bottom = lineHeight
        if (isNotDrawBottom(view, parent, currentPosition, spanCount, childCount))
        // 如果是最后一行，则不需要绘制底部
            bottom = 0
        if (isNotDrawRight(view, parent, currentPosition, spanCount, childCount))
        // 如果是最后一列，则不需要绘制右边
            right = 0
        outRect.set(0, 0, right, bottom)
    }


    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        drawHorizontal(canvas, parent, lineWidth, lineHeight)
        drawVertical(canvas, parent, lineWidth, lineHeight)
    }

    /**
     * 是否不绘制右部
     *
     * @param view            当前的view，StaggeredGridLayoutManager 用
     * @param parent          RecyclerView
     * @param position        当前的位置，GridLayoutManager、LinearLayoutManager用
     * @param spanCount       列数
     * @param count           adapter的总数
     * @return 返回true代表不绘制右部，返回false，代表绘制右部
     */
    private fun isNotDrawRight(view: View, parent: RecyclerView, position: Int, spanCount: Int, count: Int): Boolean {
        var currentPosition = position
        var adapterCount = count
        val layoutManager = parent.layoutManager
        when (layoutManager) {
            is GridLayoutManager -> {
                // GridLayoutManager
                currentPosition -= headerCount// 去掉头的数量
                adapterCount -= headerCount + footerCount// 去掉头、尾的数量
                // 判断最后一个是否绘制
                return if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                    // 垂直，判断是否是最后一列
                    (currentPosition + 1) % spanCount == 0
                } else {
                    // 水平，判断是不是最后的
                    if (adapterCount % spanCount == 0)
                        currentPosition >= adapterCount - spanCount
                    else
                        currentPosition >= adapterCount - adapterCount % spanCount
                }
            }
            is LinearLayoutManager -> // LinearLayoutManager
                // 判断最后一个是否绘制，垂直，不绘制右边，直接返回true,水平，判断，是否是最后一个
                return layoutManager.orientation == LinearLayout.VERTICAL || currentPosition == adapterCount - footerCount - 1
            is StaggeredGridLayoutManager -> {
                // 判断最后一个是否绘制，垂直，判断是否是最后一列，是返回true，水平，都显示，返回false
                val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                return layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL && (lp.spanIndex + 1) % spanCount == 0
            }
            else -> return false
        }
    }

    /**
     * 是否不绘制底部
     *
     * @param view            当前的view，StaggeredGridLayoutManager 用
     * @param parent          RecyclerView
     * @param position        当前的位置，GridLayoutManager、LinearLayoutManager用
     * @param spanCount       列数
     * @param count           adapter的总数
     * @return 返回true代表不绘制底部，返回false，代表绘制底部
     */
    private fun isNotDrawBottom(view: View, parent: RecyclerView, position: Int, spanCount: Int, count: Int): Boolean {
        var currentPosition = position
        var adapterCount = count
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // GridLayoutManager
            currentPosition -= headerCount// 去掉头的数量
            adapterCount -= headerCount + footerCount// 去掉头、尾的数量
            // 判断最后一个是否绘制
            return if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                // 垂直，判断是不是最后的
                if (adapterCount % spanCount == 0)
                    currentPosition >= adapterCount - spanCount
                else
                    currentPosition >= adapterCount - adapterCount % spanCount
            } else {
                // 水平，判断是不是最后一列
                (currentPosition + 1) % spanCount == 0
            }
        } else if (layoutManager is LinearLayoutManager) {
            // LinearLayoutManager
            // 判断最后一个是否绘制，垂直，判断是否是最后一行,水平，直接返回true，不绘制底部
            return layoutManager.orientation != LinearLayout.VERTICAL || currentPosition == adapterCount - footerCount - 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            // StaggeredGridLayoutManager
            // 判断最后一个是否绘制，垂直，都显示，返回false， 水平，判断是否是最后一列，是返回true
            val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            return layoutManager.orientation != StaggeredGridLayoutManager.VERTICAL && (lp.spanIndex + 1) % spanCount == 0
        }
        return false
    }

    /**
     * 绘制水平线
     *
     * @param canvas     画布
     * @param parent     RecyclerView
     * @param lineWidth  线宽
     * @param lineHeight 线高
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, lineWidth: Int, lineHeight: Int) {
        var isDrawDoubleLine = false
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager && layoutManager.orientation == StaggeredGridLayoutManager.HORIZONTAL)
        // 绘制双线
            isDrawDoubleLine = true
        canvas.save()
        val spanCount = getSpanCount(parent)// 水平个数，线性布局为-1
        val childCount = parent.childCount// 显示的个数
        val adapterCount = parent.adapter.itemCount// 总个数
        if (parent.clipToPadding) {
            canvas.clipRect(parent.paddingLeft, parent.paddingTop,
                    parent.width - parent.paddingRight,
                    parent.height - parent.paddingBottom)
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (isSkipDraw(parent, child))
            // 跳过，直接返回
                continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (!isNotDrawBottom(child, parent, currentPosition, spanCount, adapterCount)) {
                // 绘制底部
                val bottomLineWidth = if (isNotDrawRight(child, parent, currentPosition, spanCount, adapterCount)) 0 else lineWidth// 不绘制右部，公共区域不绘制
                // 绘制下线
                val downLeft = child.left - params.leftMargin
                val downTop = child.bottom + params.bottomMargin
                val downRight = child.right + params.rightMargin + bottomLineWidth// 公共区域绘制
                val downBottom = downTop + lineHeight
                dividerDrawable!!.setBounds(downLeft, downTop, downRight, downBottom)
                dividerDrawable!!.draw(canvas)
            }
            // 判断是否绘制双线
            if (isDrawDoubleLine && isStaggeredGridNotFirstView(child)) {
                // 绘制上线
                val upLeft = child.left - params.leftMargin
                val upTop = child.top + params.topMargin - lineHeight
                val upRight = child.right + params.rightMargin + lineWidth// 公共区域绘制
                val upBottom = upTop + lineHeight
                dividerDrawable!!.setBounds(upLeft, upTop, upRight, upBottom)
                dividerDrawable!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    /**
     * 绘制垂直线
     *
     * @param canvas     画布
     * @param parent     RecyclerView
     * @param lineWidth  线宽
     * @param lineHeight 线高
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView, lineWidth: Int, lineHeight: Int) {
        var height = lineHeight
        var isDrawDoubleLine = false
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager && layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL)
        // 绘制双线
            isDrawDoubleLine = true
        canvas.save()
        if (parent.clipToPadding) {
            canvas.clipRect(parent.paddingLeft, parent.paddingTop,
                    parent.width - parent.paddingRight,
                    parent.height - parent.paddingBottom)
        }
        val spanCount = getSpanCount(parent)// 水平个数，线性布局为-1
        val childCount = parent.childCount
        val adapterCount = parent.adapter.itemCount// 总个数
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (isSkipDraw(parent, child))
            // 跳过、不绘制右部，直接返回
                continue
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (!isNotDrawRight(child, parent, currentPosition, spanCount, adapterCount)) {
                // 不绘制右边
                if (isNotDrawBottom(child, parent, currentPosition, spanCount, adapterCount))
                // 不绘制底部，公共区域不绘制
                    height = 0
                val left = child.right + params.rightMargin
                val top = child.top - params.topMargin
                val right = left + lineWidth
                val bottom = child.bottom + params.bottomMargin + height// 公共区域水平绘制
                dividerDrawable!!.setBounds(left, top, right, bottom)
                dividerDrawable!!.draw(canvas)
            }
            // 判断是否绘制双线
            if (isDrawDoubleLine && isStaggeredGridNotFirstView(child)) {
                // 绘制左线
                val left = child.left + params.leftMargin - lineWidth
                val top = child.top - params.topMargin
                val right = left + lineWidth
                val bottom = child.bottom + params.bottomMargin + height// 公共区域水平绘制
                dividerDrawable!!.setBounds(left, top, right, bottom)
                dividerDrawable!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    /**
     * 是否是StaggeredGridLayoutManager的中间的view
     *
     * @param view      测定的view
     * @param spanCount 列数
     */
    private fun isStaggeredGridNotFirstView(view: View): Boolean {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        return lp.spanIndex != 0
    }

    /**
     * 是否跳过绘画
     *
     * @param parent RecyclerView
     * @param view   当前View
     */
    private fun isSkipDraw(parent: RecyclerView, view: View): Boolean {
        val currentPosition = parent.getChildAdapterPosition(view)// 当前item总位置
        val adapterCount = parent.adapter.itemCount
        return currentPosition < headerCount || currentPosition >= adapterCount - footerCount
    }

    /**
     * 获取列数
     *
     * @param parent RecyclerView
     * @return 列数
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is LinearLayoutManager) {
            spanCount = 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    /**
     * 设置线颜色，和setDividerDrawable()二选一
     */
    fun setLineColor(lineColor: Int) {
        this.dividerDrawable = ColorDrawable(lineColor)
    }

    class Builder internal constructor() {

        private val divider: RecyclerViewDivider = RecyclerViewDivider()

        /**
         * 设置线宽
         */
        fun width(lineWidth: Int): Builder {
            divider.lineWidth = lineWidth
            return this
        }

        /**
         * 设置线高
         */
        fun height(lineHeight: Int): Builder {
            divider.lineHeight = lineHeight
            return this
        }

        /**
         * 同时设置线宽、线高
         */
        fun widthAndHeight(lineSize: Int): Builder {
            divider.lineWidth = lineSize
            divider.lineHeight = lineSize
            return this
        }

        /**
         * 设置线颜色，和drawable二选一
         */
        fun color(lineColor: Int): Builder {
            divider.setLineColor(lineColor)
            return this
        }

        /**
         * 设置线背景，和color二选一
         */
        fun drawable(dividerDrawable: Drawable): Builder {
            divider.dividerDrawable = dividerDrawable
            return this
        }

        /**
         * 设置头的数量
         */
        fun headerCount(headerCount: Int): Builder {
            divider.headerCount = headerCount
            return this
        }

        /**
         * 设置尾的数量
         */
        fun footerCount(footerCount: Int): Builder {
            divider.footerCount = footerCount
            return this
        }

        /**
         * 返回Divider
         */
        fun build(): RecyclerViewDivider {
            return this.divider
        }

    }

    companion object {
        /**
         * Divider的构建者
         */
        fun builder(): Builder {
            return Builder()
        }
    }
}
