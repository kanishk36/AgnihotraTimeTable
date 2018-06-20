package com.madhavashram.agnihotratimetable.managers;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class NetworkManager {
	
	private static final String baseUrl = "https://maps.googleapis.com/maps/api/";
	private static final String placeAPI = "place/autocomplete/json?input=%s&sensor=false&key=%s&components=country:IN";
	private static final String geoAPI = "geocode/json?key=%s&";
	private static final String latLongAPI = "address=%s&sensor=true";
	private static final String addressAPI = "latlng=%s&sensor=true";
	private static final String App_Key = "AIzaSyDxeJAb0l57nBHUP67mDoTu6avbkqRS2cw";

	private static final int NETWORK_TIMEOUT = 1000 * 60 * 2;
	
	
	public ArrayList<String> getAllPlaces(String searchText) throws Exception
	{
		String response = "";
		String url = baseUrl.concat(placeAPI);
		searchText = Uri.encode(searchText);
		url = String.format(url, searchText,App_Key);

		HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(NETWORK_TIMEOUT);
		conn.connect();
		
		switch (conn.getResponseCode()) 
		{
		
		case HttpsURLConnection.HTTP_OK:
			response = convertStreamToString((InputStream) conn.getContent());
			break;

		default:
			break;
		}
		
		conn.disconnect();
		return parsePlaceJSON(response);
	}
	
	public ArrayList<Double> getLocationOfCity(String locationName) throws Exception
	{
		String url = baseUrl.concat(geoAPI).concat(latLongAPI);
		locationName = Uri.encode(locationName);
		url = String.format(url, App_Key, locationName);
		String response = "";
		
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(NETWORK_TIMEOUT);
		conn.connect();
		
		switch (conn.getResponseCode()) 
		{
		
		case HttpsURLConnection.HTTP_OK:
			response = convertStreamToString((InputStream) conn.getContent());
			break;

		default:
			break;
		}
		
		conn.disconnect();
		return parseLocationJSON(response);
	}
	
	public String getCityFromLatLong(String latLong)  throws Exception {
		String url = baseUrl.concat(geoAPI).concat(addressAPI);
		url = String.format(url, App_Key, latLong);
		String response = "";
		
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(NETWORK_TIMEOUT);
		conn.connect();
		
		switch (conn.getResponseCode()) 
		{
		
		case HttpsURLConnection.HTTP_OK:
			response = convertStreamToString((InputStream) conn.getContent());
			break;

		default:
			break;
		}
		
		conn.disconnect();
		return parseCityLocationJSON(response);
	}

	private String convertStreamToString(InputStream is) throws Exception
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line.concat("\n"));
	    }
	    reader.close();
	    return sb.toString();
	}
	
	private ArrayList<String> parsePlaceJSON(String jsonString) throws JSONException
	{
		ArrayList<String> places = new ArrayList<>();
		JSONObject jsObject = new JSONObject(jsonString);
		JSONArray jsArray = (JSONArray) jsObject.get("predictions");
		for(int i=0; i<jsArray.length(); i++)
		{
			JSONObject obj = jsArray.getJSONObject(i);
			if(!obj.isNull("description")) {
				if(obj.getString("description").contains("India")) {
					String description = obj.getString("description");
					String[] stringArray = description.split(",");
					description = "";
					for(int j=0; j<stringArray.length; j++)
					{
						description = description.concat(stringArray[j].trim());
						if(j != (stringArray.length-1)) {
							description += ",";
						}
					}
					description = description.replace(",India", "");
					places.add(description);
				}
			}
		}

		return places;
	}
	
	private ArrayList<Double> parseLocationJSON(String jsonString) throws JSONException
	{
		ArrayList<Double> locationsList = new ArrayList<>();
		JSONObject jsObject = new JSONObject(jsonString);
		JSONArray jsArray = (JSONArray) jsObject.get("results");
		JSONObject location = jsArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

		locationsList.add(location.getDouble("lat"));
		locationsList.add(location.getDouble("lng"));
		
		return locationsList;
	}
	
	private String parseCityLocationJSON(String jsonString) throws JSONException
	{
		JSONObject jsObject = new JSONObject(jsonString);
		JSONArray jsArray = (JSONArray) jsObject.get("results");
		String city = "";
		boolean isCity = false;
		if(jsArray.length() > 0) {
			JSONArray addrComponent = jsArray.getJSONObject(0).getJSONArray("address_components");
			 for(int i=0; i<addrComponent.length(); i++) {
				 JSONArray types = addrComponent.getJSONObject(i).getJSONArray("types");
				 for(int j=0; j<types.length(); j++) {
					 if(types.getString(j).equals("locality")) {
						 city = addrComponent.getJSONObject(i).getString("long_name");
						 isCity = true;
						 break;
					 }
				 }

				 if(isCity) {
					 break;
				 }
			 }
		}

		return city;
	}
}
