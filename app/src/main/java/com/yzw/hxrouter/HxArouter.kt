package com.yzw.hxrouter

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.lang.ref.WeakReference

/**
 * Create by yinzhengwei on 2018/12/20
 * @Function activity跳转路由
 */
object HxRouter {

    private val tag = this.javaClass.name
    private var isdebug = true

    //当前发起跳转的上下文
    private var mContext = WeakReference<Context>(null)
    //目标界面的路径
    private var mClassPath = ""

    //跳转界面需要传递的参数
    private var mParams = HashMap<String, Any?>()

    //intent需要设置的模式
    private var mCategory = ""
    private var mFlag = arrayListOf<Int>()

    //开启转场动画时需要的上下文
    private var currentActivity = WeakReference<Activity>(null)
    //转场动画的id
    private var mEnterAnimID = 0
    private var mExitAnimID = 0

    private var mAction = Intent.ACTION_MAIN
    private var mData: Uri? = null
    private var mType = ""

    /**
     * 获取上下文环境对象
     * @Params context
     * @Details 此方法只需要在Application的oncreate里初始化一次即可
     */
    fun init(context: Context?) {
        if (context == null) {
            if (isdebug) {
                Log.d(tag, "this context is null")
            }
            return
        }
        mContext = WeakReference(context)
    }

    /**
     * 获取目标界面的路径
     * @Params classPath
     * @TODO 类名的检测
     */
    fun build(classPath: String?): HxRouter {
        if (classPath.isNullOrEmpty()) {
            if (isdebug) {
                Log.d(tag, "this classPath is null")
            }
        } else
            mClassPath = classPath ?: ""
        return this
    }

    /**
     * 获取传递的参数
     * @Params  key/value
     */
    fun put(key: String, value: Any?): HxRouter {
        mParams[key] = value
        return this
    }

    /**
     * 获取传递的参数
     * @Params  k/v = map
     */
    fun put(paramsMap: HashMap<String, Any?>): HxRouter {
        mParams = paramsMap
        return this
    }

    /**
     * 获取设置intent的参数
     * @Params category
     */
    fun addCategory(category: String): HxRouter {
        mCategory = category
        return this
    }

    /**
     * 获取设置intent的参数
     * @Params flags = array
     */
    fun addFlags(vararg flags: Int): HxRouter {
        flags.forEach {
            mFlag.add(it)
        }
        return this
    }

    /**
     * 添加Intent的action
     * @Params action = String
     */
    fun addAction(action: String): HxRouter {
        mAction = action
        return this
    }

    /**
     * 添加data
     * @Params data = Uri
     */
    fun addData(uri: Uri): HxRouter {
        mData = uri
        return this
    }

    /**
     * 添加type
     * @Params type = String
     */
    fun addType(type: String): HxRouter {
        mType = type
        return this
    }

    /**
     * 获取转场动画的参数
     * @Params fromContext 当前界面的上下文
     * @Params enterAnim 旧界面退出时的动画
     * @Params exitAnim 新界面时的动画
     */
    fun transitionAnim(fromContext: Activity?, enterAnimID: Int, exitAnimID: Int): HxRouter {
        mEnterAnimID = enterAnimID
        mExitAnimID = exitAnimID

        currentActivity = WeakReference(fromContext!!)

        return this
    }

    /**
     * 启动跳转
     */
    fun start() {
        launchUI(null, null)
    }

    /**
     * 启动有回调的跳转
     */
    fun start(fromContext: Activity?, requestCode: Int?) {
        if (fromContext == null || fromContext.isFinishing) {
            if (isdebug) {
                Log.d(tag, "this context is null or isFinishing")
            }
            return
        }
        launchUI(fromContext, requestCode)
    }

    private fun launchUI(fromContext: Activity?, requestCode: Int?) {
        if (mContext.get() == null) {
            if (isdebug) {
                Log.d(tag, "this context is null")
            }
            return
        }

        if (mAction == Intent.ACTION_MAIN) {

        }

        Intent(mAction).run {
            //判断是否有需要传递的参数
            if (mParams.isNotEmpty()) {
                mParams.forEach {
                    putExtra(it.key, if (it.value != null) it.value.toString() else "")
                }
                //用完后及时清除，防止被下次的复用
                mParams = HashMap()
            }

            setDataAndType(mData, mType)

            //判断是否需要设置intent的category
            if (mCategory.isNotEmpty()) {
                addCategory(mCategory)
                mCategory = ""
            }
            //判断是否需要设置intent的flags
            if (mFlag.isNotEmpty()) {
                mFlag.forEach {
                    addFlags(it)
                }
                mFlag = arrayListOf()
            }
            //绑定要跳转的目标界面的路径
            if (mClassPath.isNotEmpty())
                component = ComponentName(mContext.get()?.packageName!!, mClassPath)
            mClassPath = ""

            try {
                //启动跳转
                if (requestCode == null) {
                    mContext.get()?.startActivity(this)
                } else {
                    fromContext?.startActivityForResult(this, requestCode)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //判断是否需要开启转场动画
            if (currentActivity.get() != null)
                anim()

            mAction = Intent.ACTION_MAIN
            mData = null
            mType = ""
        }
    }

    private fun anim() {
        currentActivity.get()?.overridePendingTransition(mEnterAnimID, mExitAnimID)

        //完成动画后将设置的id和上下文对象清空
        mEnterAnimID = 0
        mExitAnimID = 0
        currentActivity = WeakReference<Activity>(null)
    }

}