package com.yifeng.logistics.activity.map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.yifeng.logistics.Base.BaseActivity;
import com.yifeng.logistics.R;
import com.yifeng.logistics.activity.GetCommunication;
import com.yifeng.logistics.util.WeiboDialogUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */
public class ShowMap extends BaseActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Button btn_test;
    private double lon = 0.0, lat = 0.0;
    private Marker marker;
    private Dialog mWeiboDialog;
    LinearLayout rl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.layout_baidu_showmap);
        setTranslucentStatus(R.color.blue);
        mMapView = (MapView) findViewById(R.id.bd_map);
        rl_back = (LinearLayout) findViewById(R.id.rl_back);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ShowMap.this, "加载中...");
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        setMap();
    }

    private void setMarker() {
        View markView = View.inflate(getApplicationContext(), R.layout.layout_item_maker, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(markView);
        LatLng point = new LatLng(Double.parseDouble(getIntent().getStringExtra("Latitude")), Double.parseDouble(getIntent().getStringExtra("Longitude")));
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmapDescriptor)
                .zIndex(9);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, Float.parseFloat("14"));
        mBaiduMap.animateMapStatus(u);
        View markInfoView = View.inflate(getApplicationContext(), R.layout.layout_windowinfo, null);
        TextView vID = (TextView) markInfoView.findViewById(R.id.txt_vID);
        TextView vName = (TextView) markInfoView.findViewById(R.id.txt_vName);
        vID.setText(getIntent().getStringExtra("vID") + "");
        vName.setText(getIntent().getStringExtra("time") + "");
        LatLng ll = marker.getPosition();
        InfoWindow.OnInfoWindowClickListener listener = null;
        listener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ShowMap.this, GetCommunication.class);
                intent.putExtra("vID",getIntent().getStringExtra("vID"));
                intent.putExtra("ID",getIntent().getIntExtra("ID",0));
                startActivity(intent);
            }
        };
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(markInfoView), ll, -110, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            lat = location.getLatitude();
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            lon = location.getLongitude();
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            setMarker();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    private void setMap() {
        // 地图初始化
        mBaiduMap = mMapView.getMap();

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(option);
        //开始定位
        mLocationClient.start();
    }


    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

