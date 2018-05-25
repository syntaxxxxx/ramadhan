package com.idn.miniworkshopramadhan.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseApi{

	@SerializedName("country")
	private String country;

	@SerializedName("status_description")
	private String statusDescription;

	@SerializedName("address")
	private String address;

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("method")
	private int method;

	@SerializedName("prayer_method_name")
	private String prayerMethodName;

	@SerializedName("city")
	private String city;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("query")
	private String query;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("for")
	private String jsonMemberFor;

	@SerializedName("link")
	private String link;

	@SerializedName("qibla_direction")
	private String qiblaDirection;

	@SerializedName("title")
	private String title;

	@SerializedName("status_valid")
	private int statusValid;

	@SerializedName("sealevel")
	private String sealevel;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("daylight")
	private String daylight;

	@SerializedName("today_weather")
	private TodayWeather todayWeather;

	@SerializedName("state")
	private String state;

	@SerializedName("postal_code")
	private String postalCode;

	@SerializedName("items")
	private List<ItemsItem> items;

	@SerializedName("map_image")
	private String mapImage;

	@SerializedName("longitude")
	private String longitude;

	public String getCountry(){
		return country;
	}

	public String getStatusDescription(){
		return statusDescription;
	}

	public String getAddress(){
		return address;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public int getMethod(){
		return method;
	}

	public String getPrayerMethodName(){
		return prayerMethodName;
	}

	public String getCity(){
		return city;
	}

	public String getTimezone(){
		return timezone;
	}

	public String getQuery(){
		return query;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getJsonMemberFor(){
		return jsonMemberFor;
	}

	public String getLink(){
		return link;
	}

	public String getQiblaDirection(){
		return qiblaDirection;
	}

	public String getTitle(){
		return title;
	}

	public int getStatusValid(){
		return statusValid;
	}

	public String getSealevel(){
		return sealevel;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public String getDaylight(){
		return daylight;
	}

	public TodayWeather getTodayWeather(){
		return todayWeather;
	}

	public String getState(){
		return state;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public String getMapImage(){
		return mapImage;
	}

	public String getLongitude(){
		return longitude;
	}
}