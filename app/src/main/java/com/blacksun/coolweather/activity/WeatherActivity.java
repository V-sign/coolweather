package com.blacksun.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blacksun.coolweather.R;
import com.blacksun.coolweather.util.HttpUtil;
import com.blacksun.coolweather.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/3 0003.
 */

public class WeatherActivity extends AppCompatActivity {

    /**
     * 用于显示城市名
     */
    @BindView(R.id.city_name)
    TextView mCityName;
    /**
     * 用于显示发布时间
     */
    @BindView(R.id.publish_text)
    TextView mPublishText;
    /**
     * 用于显示当前日期
     */
    @BindView(R.id.current_date)
    TextView mCurrentDate;
    /**
     * 用于显示天气描述信息
     */
    @BindView(R.id.weather_desp)
    TextView mWeatherDesp;
    /**
     * 用于显示气温low
     */
    @BindView(R.id.temp_low)
    TextView mTempLow;
    /**
     * 用于显示气温high
     */
    @BindView(R.id.temp_high)
    TextView mTempHigh;
    @BindView(R.id.weather_info_layout)
    LinearLayout mWeatherInfoLayout;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.weather_layout);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        String countyCode = getIntent().getStringExtra("county_code");
        Toast.makeText(this,"county_code = " + countyCode, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(countyCode)) {
            // 有县级代号时就去查询天气
            mPublishText.setText("同步中...");
            mWeatherInfoLayout.setVisibility(View.INVISIBLE);
            mCityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            // 没有县级代号时就直接显示本地天气
            showWeather();
        }
    }

    /**
     * 查询县级代号所对应的天气代号
     * @param countyCode
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     */
    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onfinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    // 处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPublishText.setText("同步失败!");
                    }
                });
            }
        });
    }

    /**
     * 匆匆SharedPreferences文件中读取存储的天气信息,并显示到桌面上
     */
    private void showWeather() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mCityName.setText(prefs.getString("city_name", ""));
        mTempLow.setText(prefs.getString("temp_low", ""));
        mTempHigh.setText(prefs.getString("temp_high", ""));
        mWeatherDesp.setText(prefs.getString("weather_desp", ""));
        mPublishText.setText(prefs.getString("publish_time", ""));
        mCurrentDate.setText(prefs.getString("current_date", ""));
        mWeatherInfoLayout.setVisibility(View.VISIBLE);
        mCityName.setVisibility(View.VISIBLE);
    }
}
