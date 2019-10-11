package com.zxy.zxygaodemap.utils

import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.PolylineOptions

/**
 * Created by zxy on 2019/10/11-14:39
 * Class functions
 * ******************************************
 * * 地图工具类
 * ******************************************
 */
class AMapUtils {
    companion object {
        /**
         * Created by adminZPH on 2017/4/14.
         * 设置线条中的纹理的方法
         * @return PolylineOptions
         * */
        fun GetPolylineOptions(): PolylineOptions {
            //添加纹理图片
            var textureList = ArrayList<BitmapDescriptor>()
            var mRedTexture = BitmapDescriptorFactory
                .fromAsset("wenli.png")
            var mBlueTexture = BitmapDescriptorFactory
                .fromAsset("wenli.png")
            var mGreenTexture = BitmapDescriptorFactory
                .fromAsset("wenli.png")
            textureList.add(mRedTexture)
            textureList.add(mBlueTexture)
            textureList.add(mGreenTexture)
            // 添加纹理图片对应的顺序
            var textureIndexs = ArrayList<Int>()
            textureIndexs.add(0)
            textureIndexs.add(1)
            textureIndexs.add(2)
            var polylienOptions = PolylineOptions()
            polylienOptions.customTextureList = textureList
            polylienOptions.customTextureIndex = textureIndexs
            polylienOptions.isUseTexture = true
            polylienOptions.width(7.0f)
            return polylienOptions
        }
    }
}