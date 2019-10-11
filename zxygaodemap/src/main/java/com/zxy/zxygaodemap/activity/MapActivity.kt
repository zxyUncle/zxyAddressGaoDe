package com.zxy.zxygaodemap.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.orhanobut.logger.Logger
import com.zxy.zxygaodemap.R
import com.zxy.zxygaodemap.utils.ARouterUtils
import kotlinx.android.synthetic.main.activity_map.*

/**
 * Created by zxy on 2019/10/9-17:33
 * Class functions
 * ******************************************
 * * 显示地图
 * ******************************************
 */
@Route(path = ARouterUtils.GAODE_MAP)
class MapActivity : AppCompatActivity() {
    lateinit var aMap: AMap
    var isMoveMarker: Boolean = true //只移动一次marker点
    var latLng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView.onCreate(savedInstanceState)    //创建地图
        initMap()        //初始化地图
    }

    /**
     * 初始化地图-https://lbs.amap.com/api/android-sdk/guide/create-map/mylocation
     */
    fun initMap() {
        aMap = mapView.map      //得到地图实例类
        val myLocationStyle = MyLocationStyle()//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        val UiSettings = aMap.uiSettings
        UiSettings.setZoomControlsEnabled(false) //隐藏缩放按钮 -太难看

        aMap.setMyLocationStyle(myLocationStyle)//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true)// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位
        aMap.setOnMyLocationChangeListener {
            Logger.d(it.toString())
            mapView.visibility = View.VISIBLE
            latLng = LatLng(it.latitude, it.longitude)
            if (isMoveMarker) {
                //然后可以移动到定位点,使用animateCamera就有动画效果-设置缩放
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                isMoveMarker = false
            }
        }
    }

    /**
     * 自定义的当前位置按钮
     * @param view View
     */
    fun onLocationMap(view: View) {
        if (aMap != null && latLng != null) {//手动定位
            //然后可以移动到定位点,使用animateCamera就有动画效果-设置缩放
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        } else {//通过回调定位，有延迟
            isMoveMarker = true
        }
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}