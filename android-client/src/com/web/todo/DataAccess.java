package com.web.todo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataAccess {
	
	private String url = "http://192.168.2.115:3000/";

	public DataAccess() {}
	/*
	 *  access the web service and create new task
	 */
	protected void postService(String address, JSONObject params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url + address);
		try {
			StringEntity se = new StringEntity(params.toString());
			httppost.setHeader("Content-Type","Application/json");
			httppost.setEntity(se);
			httpclient.execute(httppost);
		}
		catch(ClientProtocolException e) {}
		catch(UnsupportedEncodingException e) {}
		catch(Exception e) {}
	}
	/*
	 *  access the web service and read tasks
	 */
	protected JSONArray getService(String params) {
	    JSONArray array = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url + params);
		try {
	 		HttpResponse response = httpclient.execute(httpget);
	 		String result = "";
	 		String line = "";
		 	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	 		while ((line = rd.readLine()) != null) {
	 			result += line;
	 		}
	 		array = new JSONArray(result);
	 	}
		catch(ClientProtocolException e) {}
		catch(JSONException e) {}
		catch(Exception e) {}
		return array;
	}
	/*
	 *  access the web service and update a task
	 */
	protected void putService(String address, JSONObject params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url + address);
		try {
			StringEntity se = new StringEntity(params.toString());
			httpput.setHeader("Content-Type","Application/json");
			httpput.setEntity(se);
			httpclient.execute(httpput);
		}
		catch(ClientProtocolException e) {}
		catch(UnsupportedEncodingException e) {}
		catch(Exception e) {}
	}
	/*
	 *  access the web service and delete one task
	 */
	protected void deleteService(String params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(url + params);
		try {
	 		httpclient.execute(httpdelete);
	 	}
		catch(ClientProtocolException e) {}
		catch(Exception e) {}
	}
}
