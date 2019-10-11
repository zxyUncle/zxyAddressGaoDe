package com.zxy.zxyaddressgaode

import android.app.Application
import android.content.pm.ApplicationInfo
import com.alibaba.android.arouter.launcher.ARouter


/**
 * Created by zxy on 2019/10/9-15:39
 * Class functions
 * ******************************************
 * *
 * ******************************************
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }
    fun isDebug(): Boolean {
        return applicationContext.getApplicationInfo() != null && applicationContext.getApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE !== 0
    }
}