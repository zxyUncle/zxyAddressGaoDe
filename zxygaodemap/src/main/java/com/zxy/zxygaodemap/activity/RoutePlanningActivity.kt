package com.zxy.zxygaodemap.activity

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.orhanobut.logger.Logger
import com.zxy.zxygaodemap.R
import com.zxy.zxygaodemap.utils.ARouterUtils
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_map_poi.mapView
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.route.*
import com.zxy.zxygaodemap.utils.AMapUtils
import kotlinx.android.synthetic.main.activity_map_poi.root
import kotlinx.android.synthetic.main.activity_route_planning.*

/**
 * Created by zxy on 2019/10/11-9:58
 * Class functions
 * ******************************************
 * * 路线规划
 * ******************************************
 */
@Route(path = ARouterUtils.GAODE_ROUTEPLANNING)
class RoutePlanningActivity : AppCompatActivity() {
    @Autowired
    @JvmField
    var poiItem: PoiItem? = null  //传递的目的地经纬度

    lateinit var aMap: AMap
    var isMoveMarker: Boolean = true //只移动一次marker点
    var latLng: LatLng? = null
    //声明AMapLocationClient类对象
    lateinit var mLocationClient: AMapLocationClient
    lateinit var mAMapLocation: AMapLocation
    lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_planning)
        ARouter.getInstance().inject(this)
        //图片渗入状态栏
        StatusBarUtil.setTranslucentForImageView(this, root, R.id.root)
        //状态栏图片变白，
        StatusBarUtil.setStatusIconCollor(this, true)
        mapView.onCreate(savedInstanceState)    //创建地图

        initMap()        //初始化地图
        initlocationResult() //定位结果
//        initView()      //初始化View
    }

    private fun initView() {
        /**
         * 地图点击事件- 生成marke点
         */
//        aMap.setOnMapClickListener {
//            val latLng = LatLng(it.latitude, it.longitude)
//            val marker = aMap.addMarker(MarkerOptions().position(latLng).title("标题").snippet("说明"))
//        }

    }

    /**
     * 出行路线规划-绘制
     */
    fun initRoutePlanning() {
        //zxy 第 1 步，初始化 RouteSearch 对象
        var routeSearch = RouteSearch(this)
        //zxy 第 2 步，设置数据回调监听器-设置路线回调
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            //驾车
            override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
                var latLngs = mutableListOf<LatLng>()
                Logger.e("${p0!!.paths.size}")
                for (index in 0 until p0!!.paths.size step 1) {
                    for (j in 0 until p0!!.paths[0].steps[index].polyline.size) {
                        Logger.e("${p0!!.paths[0].steps[index].polyline[j].latitude}-${p0!!.paths[0].steps[index].polyline[j].longitude}")
                        latLngs.add(
                            LatLng(
                                p0!!.paths[0].steps[index].polyline[j].latitude,
                                p0!!.paths[0].steps[index].polyline[j].longitude
                            )
                        )
                    }
                }
                //开始绘制路径
                var polyline = aMap.addPolyline(
                    AMapUtils.GetPolylineOptions().addAll(latLngs).width(30f).color(resources.getColor(R.color.c5C98FE))
                )
            }

            //打车
            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
                var latLngs = mutableListOf<LatLng>()
                Logger.e("${p0!!.paths[0].steps.size}")
                for (index in 0 until p0!!.paths[0].steps.size step 1) {
                    for (j in 0 until p0!!.paths[0].steps[index].busLines.size) {
                        Logger.e("${p0!!.paths[0].steps[index].busLines[j].polyline[0].latitude}-${p0!!.paths[0].steps[index].busLines[j].polyline[0].longitude}")
                        latLngs.add(
                            LatLng(
                                p0!!.paths[0].steps[index].busLines[j].polyline[0].latitude,
                                p0!!.paths[0].steps[index].busLines[j].polyline[0].longitude
                            )
                        )
                    }
                }
                //开始绘制路径
                var polyline = aMap.addPolyline(
                    AMapUtils.GetPolylineOptions().addAll(latLngs).width(30f).color(resources.getColor(R.color.c5C98FE))
                )

            }

            //公交/骑行
            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
                var latLngs = mutableListOf<LatLng>()
                Logger.e("${p0!!.paths[0].steps.size}")
                for (index in 0 until p0!!.paths[0].steps.size step 1) {
                    for (j in 0 until p0!!.paths[0].steps[index].polyline.size) {
                        Logger.e("${p0!!.paths[0].steps[index].polyline[j].latitude}-${p0!!.paths[0].steps[index].polyline[j].longitude}")
                        latLngs.add(
                            LatLng(
                                p0!!.paths[0].steps[index].polyline[j].latitude,
                                p0!!.paths[0].steps[index].polyline[j].longitude
                            )
                        )
                    }
                }
                //开始绘制路径
                var polyline = aMap.addPolyline(
                    AMapUtils.GetPolylineOptions().addAll(latLngs).width(30f).color(resources.getColor(R.color.c5C98FE))
                )
            }

            //步行
            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
                var latLngs = mutableListOf<LatLng>()
                Logger.e("${p0!!.paths[0].steps.size}")
                for (index in 0 until p0!!.paths[0].steps.size step 1) {
                    for (j in 0 until p0!!.paths[0].steps[index].polyline.size) {
                        Logger.e("${p0!!.paths[0].steps[index].polyline[j].latitude}-${p0!!.paths[0].steps[index].polyline[j].longitude}")
                        latLngs.add(
                            LatLng(
                                p0!!.paths[0].steps[index].polyline[j].latitude,
                                p0!!.paths[0].steps[index].polyline[j].longitude
                            )
                        )
                    }
                }
                //开始绘制路径
                var polyline = aMap.addPolyline(
                    AMapUtils.GetPolylineOptions().addAll(latLngs).width(30f).color(resources.getColor(R.color.c5C98FE))
                )
            }
        })
        //起始点，终点
        var fromAndTo =
            RouteSearch.FromAndTo(LatLonPoint(location.latitude, location.longitude), poiItem!!.latLonPoint)
        //zxy 第 3 步，设置搜索参数

        //zxy 第 4 步，发送请求
        rg_route_planning.setOnCheckedChangeListener { _, i ->
            aMap.clear()
            //初始化行程路径- 生成marke点
            var latLng = LatLng(poiItem!!.latLonPoint.latitude, poiItem!!.latLonPoint.longitude)
            var marker:Marker = aMap.addMarker(MarkerOptions().position(latLng).title(poiItem!!.title).snippet(poiItem!!.snippet))
            when (i) {
                R.id.rb_route_planning_Walk -> {
                    // 步行
                    var query = RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_MULTI_PATH)
                    routeSearch.calculateWalkRouteAsyn(query)
                }
                R.id.rb_route_planning_Ride -> {
                    //  公交
                    var query = RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, "021", 1)
                    query.cityd = poiItem!!.adCode//终点城市区号
                    routeSearch.calculateBusRouteAsyn(query)
                }
                R.id.rb_route_planning_Drive -> {
                    // 驾车
                    var query = RouteSearch.RideRouteQuery(fromAndTo, 0)
                    routeSearch.calculateRideRouteAsyn(query)
                }
                R.id.rb_route_planning_Bus -> {
                    // 打车
                    var query = RouteSearch.RideRouteQuery(fromAndTo, 0)
                    routeSearch.calculateRideRouteAsyn(query)
                }
            }
        }
        rg_route_planning.check(R.id.rb_route_planning_Walk)
    }


    /**
     * 定位结果
     */
    fun initlocationResult() {
        //zxy 高德地图
        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy

        val option = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation()
            mLocationClient.startLocation()
        }
        //获取一次定位结果：
        //该方法默认为false
        mLocationOption.isOnceLocation = true

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true;

        //声明定位回调监听器
        val mLocationListener = AMapLocationListener {
            mAMapLocation = it
            if (it != null) {
                if (it.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Logger.d(it.toString())
                    info_location.text = it.toString()
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Logger.d("ErrCode:${it.errorCode}-ErrInfo:${it.errorInfo}")
                }
            }
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

    }

    /**
     * 初始化地图-https://lbs.amap.com/api/android-sdk/guide/create-map/mylocation
     */
    private fun initMap() {
        aMap = mapView.map      //得到地图实例类
        val myLocationStyle = MyLocationStyle()//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.interval(1000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        val UiSettings = aMap.uiSettings
        UiSettings.isZoomControlsEnabled = false //隐藏缩放按钮 -太难看
        aMap.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        aMap.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位
        aMap.setOnMyLocationChangeListener {
            location = it
            mapView.visibility = View.VISIBLE
            latLng = LatLng(it.latitude, it.longitude)
            if (isMoveMarker) {
                Logger.d(it.toString())
                //然后可以移动到定位点,使用animateCamera就有动画效果-设置缩放
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                isMoveMarker = false
                initRoutePlanning()//驾车出行路线规划
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