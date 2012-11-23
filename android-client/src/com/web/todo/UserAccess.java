package com.web.todo;

import org.json.JSONArray;

public class UserAccess extends DataAccess{

	public UserAccess() {
		super();
	}
	/*
	 * validate user exist
	 */
	public JSONArray validateUser(String user) {
		String params = "users/get_user/" + user + ".json";
		return super.getService(params);
	}
	/*
	 * read all user tasks
	 */
	public JSONArray readUserTasks(String user) {
		String params = "users/" + user + ".json";
		return getService(params);
	}

}
