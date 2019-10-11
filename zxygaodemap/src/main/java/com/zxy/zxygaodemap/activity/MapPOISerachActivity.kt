package com.zxy.zxygaodemap.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.core.SuggestionCity
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zxy.zxygaodemap.R
import com.zxy.zxygaodemap.adapter.POISearchDataAdapter
import com.zxy.zxygaodemap.utils.ARouterUtils
import kotlinx.android.synthetic.main.activity_map_poi.*
import kotlinx.android.synthetic.main.activity_map_poi_search.*

/**
 * Created by zxy on 2019/10/10-15:50
 * Class functions
 * ******************************************
 * * 地图POi搜索
 * ******************************************
 */
@Route(path = ARouterUtils.GAODE_MAP_SEARCH)

class MapPOISerachActivity : AppCompatActivity(), OnRefreshListener, OnRefreshLoadMoreListener {
    @Autowired(name = "mAMapLocation")
    @JvmField
    var mAMapLocation: AMapLocation? = null

    var poisearchDataAdapter: POISearchDataAdapter? = null
    var page: Int = 0
    var pageSumData = 10 //每页数据
    var poiss: MutableList<PoiItem>? = null
    var searchData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_poi_search)
        ARouter.getInstance().inject(this)
        initSearchPOI() //关键字检索POI
        initDataAdapter()//初始化RecyclerView
        initSmartRefreshLayout()//刷新，加载的初始化-SmartRefreshLayout
        initView()
        //图片渗入状态栏
        StatusBarUtil.setColor(this, resources.getColor(R.color.ffffff), 0)
        //状态栏图片变白，
        StatusBarUtil.setStatusIconCollor(this, true)
    }

    fun initView() {
        iv_search_map_poi_delete.setOnClickListener {
            iv_search_map_poi_delete.visibility = View.GONE
            et_search_map_poi_search.setText("")
        }
        iv_search_map_poi_back.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * 刷新，加载的初始化-SmartRefreshLayout
     */
    fun initSmartRefreshLayout() {
        //添加刷新、加载的监听
        srl_data_search.setOnRefreshListener(this)
        srl_data_search.setOnRefreshLoadMoreListener(this)
        //设置滑动header跟footer的样式
        srl_data_search.setRefreshHeader(ClassicsHeader(this))
        srl_data_search.setRefreshFooter(ClassicsFooter(this).setArrowResource(R.mipmap.ic_launcher))

    }

    /**
     * 关键字检索POI
     */
    fun initSearchPOI() {
        et_search_map_poi_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length == 0) {
                    iv_search_map_poi_delete.visibility = View.GONE
                } else {
                    iv_search_map_poi_delete.visibility = View.VISIBLE
                }
                searchData = p0.toString()!! //搜索的内容

                //主动调用刷新
                srl_data_search.autoRefresh()
            }

        })
    }

    /**
     * 搜索结果
     */
    fun doSearchQueryResult() {
        page++
        //搜索条件添加
        var query = PoiSearch.Query(searchData.toString(), "", mAMapLocation!!.cityCode)
        query!!.pageSize = pageSumData// 设置每页最多返回多少条poiitem
        query!!.pageNum = page//设置查询页码
        var poiSearch = PoiSearch(this@MapPOISerachActivity, query) //查询
        poiSearch.bound = PoiSearch.SearchBound(
            LatLonPoint(
                mAMapLocation!!.latitude,
                mAMapLocation!!.longitude
            ), 5000
        )//设置周边搜索的中心点以及半径

        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

            override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                srl_data_search.finishLoadMore()
                srl_data_search.finishRefresh()

                var pois = p0!!.pois                        //得到查询列表
                var searchSuggestionCitys: MutableList<SuggestionCity>? = null  //建议的 列表
                var searchSuggestionKeywords: MutableList<String>? = null  //建议的关键字
                if (p1 == 1000) { // 查询成功
                    //得到查询列表
                    pois = p0!!.pois
                    if (pois.size == 0) {//没有查询到数据
                        // 如果当前城市没有指定关键字的poi，那么使用建议的 列表
                        searchSuggestionCitys = p0.searchSuggestionCitys
                        Logger.d("建议数据：$searchSuggestionCitys")
                    } else {     //查询到数据
                        Logger.d("正常数据：$pois")
                        setDataAdapter(pois)
                    }
                } else { //查询失败
                    //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
                    searchSuggestionKeywords = p0.searchSuggestionKeywords
                    Logger.d("建议关键字：$searchSuggestionKeywords")
                }
            }
        })
        poiSearch.searchPOIAsyn() //发送请求

    }

    /**
     * RecyclerView初始化
     */
    @SuppressLint("all")
    fun initDataAdapter() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        rv_data_search.layoutManager = layoutManager
    }

    /**
     * 设置正常搜索值
     */
    fun setDataAdapter(pois: MutableList<PoiItem>) {
        if (page == 1) {
            poiss = pois
            poisearchDataAdapter = POISearchDataAdapter(this, poiss!!)
            rv_data_search.adapter = poisearchDataAdapter
            poisearchDataAdapter!!.setOnItemClickListener(object : POISearchDataAdapter.OnItemClickListener {
                override fun onClick(view: View, position: Int, poiItem: PoiItem) {
                    ARouter.getInstance().build(ARouterUtils.GAODE_ROUTEPLANNING).withParcelable("poiItem", poiItem)
                        .navigation()
                }

            })
        } else {
            poiss!!.addAll(pois)
            poisearchDataAdapter!!.pois = poiss!!
            poisearchDataAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 0
        doSearchQueryResult() //搜索结果
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        doSearchQueryResult() //搜索结果
    }
}