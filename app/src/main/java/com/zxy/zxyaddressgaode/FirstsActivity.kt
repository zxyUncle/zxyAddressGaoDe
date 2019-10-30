package com.zxy.zxyaddressgaode

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.zxy.zxygaodemap.utils.ARouterUtils

/**
 * Created by zxy on 2019/10/10-11:41
 * Class functions
 * ******************************************
 * *
 * ******************************************
 */
class FirstsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firsts)
    }

    fun onMyIntent(view: View) {
        ARouter.getInstance().build(ARouterUtils.GAODE_MAIN).navigation()
        runOnUiThread { Log.e("123","123") }
    }


}