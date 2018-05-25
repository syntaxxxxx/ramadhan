package com.idn.miniworkshopramadhan.response;

import com.google.gson.annotations.SerializedName;

public class TodayWeather{

	@SerializedName("temperature")
	private String temperature;

	@SerializedName("pressure")
	private int pressure;

	public String getTemperature(){
		return temperature;
	}

	public int getPressure(){
		return pressure;
	}
}