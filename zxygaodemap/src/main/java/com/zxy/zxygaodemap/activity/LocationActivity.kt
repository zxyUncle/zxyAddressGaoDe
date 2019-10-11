package com.zxy.zxygaodemap.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import permissions.dispatcher.RuntimePermissions
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.orhanobut.logger.Logger
import com.zxy.zxygaodemap.R
import com.zxy.zxygaodemap.utils.ARouterUtils
import kotlinx.android.synthetic.main.activity_location.*
import permissions.dispatcher.OnNeverAskAgain

/**
 * Created by zxy on 2019/10/9-16:00
 * Class functions
 * ******************************************
 * * 定位
 * ******************************************
 */
@RuntimePermissions
@Route(path = ARouterUtils.GAODE_LOCATION)
class LocationActivity : AppCompatActivity() {
    //声明AMapLocationClient类对象
    lateinit var mLocationClient: AMapLocationClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        initLocation()//初始化定位
        initlocationResult()//获取定位结果
        onPermissionsWithPermissionCheck()//动态权限申请

        var appInfo: ApplicationInfo? = null;
        try {
            appInfo = applicationContext.getPackageManager()
                .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
            val value = appInfo!!.metaData.getString("com.amap.api.v2.apikey")
            Logger.d("高德地图appKey设置成功:${value}")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 1、初始化定位
     */
    fun initLocation() {
        //zxy 高德地图
        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)
        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)

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
        mLocationOption.setOnceLocation(true)

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

    }

    /**
     * 2、获取定位结果
     */
    fun initlocationResult() {
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener {
            if (it != null) {
                if (it.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Logger.d(it.toString())
                    info_location.text = it.toString()
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Logger.d("ErrCode:${it.getErrorCode()}-ErrInfo:${it.getErrorInfo()}")
                }
            }
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

    }

    /**
     *  需要的权限
     */
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onPermissions() {
        //3、启动定位
        mLocationClient.startLocation()
    }

    /**
     *  拒绝的权限
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onNeverPermissions() {
    }

    /**
     * 权限回调
     * @param requestCode Int
     * @param permissions Array<out String>
     * @param grantResults IntArray
     */
    @SuppressLint("all")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}