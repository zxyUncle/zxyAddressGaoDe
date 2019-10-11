package com.zxy.zxygaodemap.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.zxy.zxygaodemap.R

class ARouterUtils {
    companion object {
        const val AppGROUP = "/gaodeMap/"
        //main
        const val GAODE_MAIN = AppGROUP + "GAODE_MAIN"
        //定位
        const val GAODE_LOCATION = AppGROUP + "GAODE_LOCATION"
        //地图
        const val GAODE_MAP = AppGROUP + "GAODE_MAP"
        //地图POI
        const val GAODE_MAPPOI = AppGROUP + "GAODE_MAPPOI"
        //地图POI搜索
        const val GAODE_MAP_SEARCH = AppGROUP + "GAODE_MAP_SEARCH"
        //地图POI搜索
        const val GAODE_ROUTEPLANNING = AppGROUP + "GAODE_ROUTEPLANNING"

        //跳转动画，左右
        fun getAnimationLeftToRight(mContext: Context): ActivityOptionsCompat {
            return ActivityOptionsCompat
                .makeCustomAnimation(mContext, R.anim.translate_out, R.anim.translate_in)
        }

        //跳转动画，上下
        fun getAnimationBottonToTop(mContext: Context): ActivityOptionsCompat {
            return ActivityOptionsCompat
                .makeCustomAnimation(mContext, R.anim.translate_top, R.anim.translate_bottom)
        }

        //View的转场动画
        fun getAnimationView(mContext: Context, view: View, sharedElementName: String): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(mContext as Activity, view, sharedElementName)
        }

        //关闭动画
        fun finishLeft(activity: Activity) {
            activity.overridePendingTransition(R.anim.translate_out, R.anim.translate_in)
            activity.finishAfterTransition()
        }

        //关闭动画
        fun finishTop(activity: Activity) {
            activity.overridePendingTransition(R.anim.translate_top, R.anim.translate_bottom)
            activity.finishAfterTransition()
        }

    }
}