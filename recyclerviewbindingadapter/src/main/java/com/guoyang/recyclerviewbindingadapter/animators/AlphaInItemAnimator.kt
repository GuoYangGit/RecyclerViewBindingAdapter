package com.guoyang.recyclerviewbindingadapter.animators

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import android.view.animation.LinearInterpolator
import com.guoyang.recyclerviewbindingadapter.ItemAnimator

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
 * Created by guoyang on 2018/8/30.
 * github https://github.com/GuoYangGit
 * QQ:352391291
 */
class AlphaInItemAnimator(private val from :Float=0f,private val duration: Long = 500L, private val interpolator: TimeInterpolator = LinearInterpolator()) : ItemAnimator {
    override fun scrollUpAnim(v: View) {
        ObjectAnimator.ofFloat(v, "alpha", from, 1f)
                .setDuration(duration).apply {
                    interpolator = this@AlphaInItemAnimator.interpolator
                }
                .start()
    }

    override fun scrollDownAnim(v: View) {
        ObjectAnimator.ofFloat(v, "alpha", from, 1f)
                .setDuration(duration).apply {
                    interpolator = this@AlphaInItemAnimator.interpolator
                }
                .start()
    }

}