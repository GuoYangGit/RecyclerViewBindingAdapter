package com.guoyang.recyclerviewbindingadapter.divider.suspension

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View

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
 * Created by guoyang on 2018/10/30.
 * github https://github.com/GuoYangGit
 * QQ:352391291
 */
class SuspensionDecoration(context: Context, private var datas: List<ISuspensionInterface>) : RecyclerView.ItemDecoration() {
    private val mPaint: Paint = Paint()
    private val mBounds: Rect = Rect()//用于存放测量文字Rect

    private val mInflater: LayoutInflater

    private var mTitleHeight: Int = 0//title的高

    private var mHeaderViewCount = 0

    private var mPaddingLeft = 0


    init {
        mTitleHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, context.resources.displayMetrics).toInt()
        mPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, context.resources.displayMetrics).toInt()
        mTitleFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, context.resources.displayMetrics).toInt()
        mPaint.textSize = mTitleFontSize.toFloat()
        mPaint.isAntiAlias = true
        mInflater = LayoutInflater.from(context)
    }


    fun setTitleHeight(titleHeight: Int): SuspensionDecoration {
        this.mTitleHeight = titleHeight
        return this
    }


    fun setColorTitleBg(colorTitleBg: Int): SuspensionDecoration {
        COLOR_TITLE_BG = colorTitleBg
        return this
    }

    fun setColorTitleFont(colorTitleFont: Int): SuspensionDecoration {
        COLOR_TITLE_FONT = colorTitleFont
        return this
    }

    fun setTitleFontSize(titleFontSize: Int): SuspensionDecoration {
        mPaint.textSize = titleFontSize.toFloat()
        return this
    }

    fun setPaddingLeft(paddingLeft: Int): SuspensionDecoration {
        mPaddingLeft = paddingLeft
        return this
    }

    fun setData(data: List<ISuspensionInterface>): SuspensionDecoration {
        this.datas = data
        return this
    }

    private fun getHeaderViewCount(): Int {
        return mHeaderViewCount
    }

    fun setHeaderViewCount(headerViewCount: Int): SuspensionDecoration {
        mHeaderViewCount = headerViewCount
        return this
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            var position = params.viewLayoutPosition
            position -= getHeaderViewCount()
            //pos为1，size为1，1>0? true
            if (datas.isEmpty() || position > datas.size - 1 || position < 0 || !datas[position].isShowSuspension()) {
                continue//越界
            }
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {//等于0肯定要有title的
                    drawTitleArea(c, left, right, child, params, position)

                } else {//其他的通过判断
                    if (datas[position].getSuspensionTag() != datas[position - 1].getSuspensionTag()) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position)
                    } else {
                        //none
                    }
                }
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private fun drawTitleArea(c: Canvas, left: Int, right: Int, child: View, params: RecyclerView.LayoutParams, position: Int) {//最先调用，绘制在最下层
        mPaint.color = COLOR_TITLE_BG
        c.drawRect(left.toFloat(), (child.top - params.topMargin - mTitleHeight).toFloat(), right.toFloat(), (child.top - params.topMargin).toFloat(), mPaint)
        mPaint.color = COLOR_TITLE_FONT
        /*
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;*/

        mPaint.getTextBounds(datas[position].getSuspensionTag(), 0, datas[position].getSuspensionTag().length, mBounds)
        c.drawText(datas[position].getSuspensionTag(), child.paddingLeft.toFloat() + mPaddingLeft, (child.top - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2)).toFloat(), mPaint)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {//最后调用 绘制在最上层
        var pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        pos -= getHeaderViewCount()
        //pos为1，size为1，1>0? true
        if (datas.isEmpty() || pos > datas.size - 1 || pos < 0 || !datas[pos].isShowSuspension()) {
            return //越界
        }

        val tag = datas[pos].getSuspensionTag()
        //View child = parent.getChildAt(pos);
        val child = parent.findViewHolderForLayoutPosition(pos + getHeaderViewCount())!!.itemView//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView

        var flag = false//定义一个flag，Canvas是否位移过的标志
        if (pos + 1 < datas.size) {//防止数组越界（一般情况不会出现）
            if (tag != datas[pos + 1].getSuspensionTag()) {//当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了
                if (child.height + child.top < mTitleHeight) {//当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，我们也该开始做悬浮Title的“交换动画”
                    c.save()//每次绘制前 保存当前Canvas状态，
                    flag = true

                    //一种头部折叠起来的视效，个人觉得也还不错~
                    //可与123行 c.drawRect 比较，只有bottom参数不一样，由于 child.getHeight() + child.getTop() < mTitleHeight，所以绘制区域是在不断的减小，有种折叠起来的感觉
                    //c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());

                    //类似饿了么点餐时,商品列表的悬停头部切换“动画效果”
                    //上滑时，将canvas上移 （y为负数） ,所以后面canvas 画出来的Rect和Text都上移了，有种切换的“动画”感觉
                    c.translate(0f, (child.height + child.top - mTitleHeight).toFloat())
                }
            }
        }
        mPaint.color = COLOR_TITLE_BG
        c.drawRect(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat(), (parent.right - parent.paddingRight).toFloat(), (parent.paddingTop + mTitleHeight).toFloat(), mPaint)
        mPaint.color = COLOR_TITLE_FONT
        mPaint.getTextBounds(tag, 0, tag.length, mBounds)
        c.drawText(tag, child.paddingLeft.toFloat() + mPaddingLeft,
                (parent.paddingTop + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2)).toFloat(),
                mPaint)
        if (flag)
            c.restore()//恢复画布到之前保存的状态
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //super里会先设置0 0 0 0
        super.getItemOffsets(outRect, view, parent, state)
        var position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        position -= getHeaderViewCount()
        if (datas.isEmpty() || position > datas.size - 1) {//pos为1，size为1，1>0? true
            return //越界
        }
        //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
        if (position > -1) {
            val titleCategoryInterface = datas[position]
            //等于0肯定要有title的,
            // 2016 11 07 add 考虑到headerView 等于0 也不应该有title
            // 2016 11 10 add 通过接口里的isShowSuspension() 方法，先过滤掉不想显示悬停的item
            if (titleCategoryInterface.isShowSuspension()) {
                if (position == 0) {
                    outRect.set(0, mTitleHeight, 0, 0)
                } else {//其他的通过判断
                    if (titleCategoryInterface.getSuspensionTag() != datas[position - 1].getSuspensionTag()) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        outRect.set(0, mTitleHeight, 0, 0)
                    }
                }
            }
        }
    }

    companion object {
        private var COLOR_TITLE_BG = Color.parseColor("#FFF8F8F8")
        private var COLOR_TITLE_FONT = Color.parseColor("#FF999999")
        private var mTitleFontSize: Int = 0//title字体大小
    }

}
