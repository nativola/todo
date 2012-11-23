package com.web.todo;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskAccess extends DataAccess {

	public TaskAccess() {
		super();
	}
	/*
	 * create a new task
	 */
	public void createNewTask(String task, String user) {
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
	 * update an user task
	 */
	public void updateExistingTask(String task, String taskId) {
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
	public void deleteExistingTask(String task) {
		String params = "tasks/" + task + ".json";
		deleteService(params);
	}
}
