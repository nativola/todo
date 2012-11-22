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
	 * validate user exist
	 */
	public JSONArray validateUser(String user) {
		String params = "users/get_user/" + user + ".json";
		return getService(params);
	}
	/*
	 * create a new task
	 */
	public void createTask(String task, String user) {
		JSONObject a = new JSONObject();
		JSONObject tasks = new JSONObject();
		try {
			a.put("name", task);
			a.put("user_id", user);
			tasks.put("task", a);
		}
		catch(JSONException e) {}
		String address = "tasks.json";
		postService(address, tasks);
	}
	/*
	 * read all user tasks
	 */
	public JSONArray readTasks(String user) {
		String params = "users/" + user + ".json";
		return getService(params);
	}
	/*
	 * update an user task
	 */
	public void updateTask(String task, String taskId) {
		JSONObject a = new JSONObject();
		JSONObject tasks = new JSONObject();
		try {
			a.put("name", task);
			tasks.put("task", a);
		}
		catch(JSONException e) {}
		String address = "tasks/" + taskId + ".json";
		putService(address, tasks);
	}
	/*
	 * delete an user task
	 */
	public void deleteTask(String task) {
		String params = "tasks/" + task + ".json";
		deleteService(params);
	}
	/*
	 *  access the web service and create new task
	 */
	public void postService(String address, JSONObject params) {
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
	private JSONArray getService(String params) {
		String result = "";
	    JSONArray array = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url + params);
		try {
	 		HttpResponse response = httpclient.execute(httpget);
	 		String s = "";
	 		String line = "";
		 	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	 		while ((line = rd.readLine()) != null) {
	 			s += line;
	 		}
	 		result = s;
	 	}
		catch(Exception e) {}
		try {
			array = new JSONArray(result);
		}
		catch(JSONException e) {}
		return array;
	}
	/*
	 *  access the web service and update a task
	 */
	public void putService(String address, JSONObject params) {
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
	private void deleteService(String params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(url + params);
		try {
	 		httpclient.execute(httpdelete);
	 	}
		catch(ClientProtocolException e) {}
		catch(Exception e) {}
	}
}
