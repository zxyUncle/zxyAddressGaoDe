package com.zxy.zxygaodemap.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zxy.zxygaodemap.R
import com.zxy.zxygaodemap.utils.ARouterUtils
import com.zxy.zxytools.logger.LoggerUtils

@Route(path = ARouterUtils.GAODE_MAIN)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoggerUtils.init("msg") //msg是默认的日志过滤TAG
    }

    /**
     * 定位
     * @param view View
     */
    fun onLocation(view: View) {
        ARouter.getInstance().build(ARouterUtils.GAODE_LOCATION)
            .withOptionsCompat(ARouterUtils.getAnimationLeftToRight(this))
            .navigation()
    }

    /**
     * 地图
     * @param view View
     */
    fun onMap(view: View) {
        ARouter.getInstance().build(ARouterUtils.GAODE_MAP)
            .withOptionsCompat(ARouterUtils.getAnimationLeftToRight(this))
            .navigation()
    }

    /**
     *地图POI跟搜索
     * @param view View
     */
    fun onMapPOI(view: View) {
        ARouter.getInstance().build(ARouterUtils.GAODE_MAPPOI)
            .withOptionsCompat(ARouterUtils.getAnimationLeftToRight(this))
            .navigation()
    }

    /**
     * 路线规划
     * @param view View
     */
    fun onRoutePlanning(view: View) {
        ARouter.getInstance().build(ARouterUtils.GAODE_ROUTEPLANNING)
            .withOptionsCompat(ARouterUtils.getAnimationLeftToRight(this))
            .navigation()
    }
}
