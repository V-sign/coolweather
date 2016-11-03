package com.blacksun.coolweather.model;

import static android.R.attr.id;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public class WeatherInfo {

    private String cityName;
    private String weatherCode;
    private String tempLow;
    private String tempHigh;
    private String weaterDesp;
    private String publishTime;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getWeaterDesp() {
        return weaterDesp;
    }

    public void setWeaterDesp(String weaterDesp) {
        this.weaterDesp = weaterDesp;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
