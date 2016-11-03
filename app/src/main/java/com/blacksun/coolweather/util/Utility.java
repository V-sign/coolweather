package com.blacksun.coolweather.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.blacksun.coolweather.activity.CoolWeatherApp;
import com.blacksun.coolweather.db.CoolWeatherDB;
import com.blacksun.coolweather.model.City;
import com.blacksun.coolweather.model.County;
import com.blacksun.coolweather.model.Province;
import com.blacksun.coolweather.model.WeatherInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param coolWeatherDB
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String c:allProvinces) {
                    String[] array = c.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    // 将解析出来的数据存储到Province表
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param coolWeatherDB
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c:allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    // 将解析出来的数据存储到City表
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param coolWeatherDB
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(allCounties[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    // 将解析出来的数据存储到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据,并将解析出的数据存储到本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context, String response) {

            WeatherInfo weatherInfo = CoolWeatherApp.sGson.fromJson(response, WeatherInfo.class);
            String cityName = weatherInfo.getCityName();
            String weatherCode = weatherInfo.getWeatherCode();
            String tempLow = weatherInfo.getTempLow();
            String tempHigh = weatherInfo.getTempHigh();
            String weaterDesp = weatherInfo.getWeaterDesp();
            String publishTime = weatherInfo.getPublishTime();
            saveWeatherInfo(context, cityName, weatherCode, tempLow, tempHigh, weaterDesp, publishTime);
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中
     * @param context
     * @param cityName
     * @param weatherCode
     * @param tempLow
     * @param tempHigh
     * @param weaterDesp
     * @param publishTime
     */
    @SuppressLint("CommitPrefEdits")
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String tempLow, String tempHigh, String weaterDesp, String publishTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年m月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp_low", tempLow);
        editor.putString("temp_high", tempHigh);
        editor.putString("weather_desp", weaterDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_time", sdf.format(new Date()));
        editor.commit();
    }

}
