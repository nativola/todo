package com.main.todo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.web.todo.TaskAccess;
import com.web.todo.UserAccess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	List<Tasks> taskModel = new ArrayList<Tasks>();
	List<Tasks> spprtModel = new ArrayList<Tasks>();
	taskAdapter taskAdapter = null;
	
	ListView ma_lv;
	
	private String user = "";
	private String task = "";
	private String sprt = "";
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * app start callback
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ma_lv = (ListView)findViewById(R.id.ma_lv);
        ma_lv.setOnItemClickListener(taskClick);
	    Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        new readUserTasks().execute();
    }
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     * app options menu creation callback
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }
    /*
     * options menu items event callback
     */
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equals("Nueva tarea")) {
			showDialog(3);
		}
		return true;
	}
	/*
	 * tasks list click event callback
	 */
	private AdapterView.OnItemClickListener taskClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Tasks tasks = taskModel.get(position);
			task =  tasks.getId();
			showDialog(0);
		}
	};
	/*
	 * dialog options create method
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog listOptions = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.custom_dialog, null);
		switch(id) {
		case 0:
			final CharSequence[] options = {this.getString(R.string.update),this.getString(R.string.delete)};
			builder.setTitle(R.string.task);
			builder.setItems(options, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if(item == 0) {
						showDialog(1);
					}
					else {
						showDialog(2);
					}
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
				}
			});
			listOptions = builder.create();
			break;
		case 1:
            builder.setTitle(R.string.title_update);
            builder.setView(textEntryView);
            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
            		EditText cd_et = (EditText)textEntryView.findViewById(R.id.cd_et);
            		sprt = cd_et.getText().toString();
            		cd_et.setText("");
            		if(sprt.isEmpty()) {
            			Toast.makeText(MainActivity.this, R.string.require_field, Toast.LENGTH_LONG).show();
            		}
            		else {
            			new updateExistingTask().execute();
            		}
            	}
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
            	}
            });      
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
				}
			});
            listOptions = builder.create();
			break;
		case 2:
			builder.setTitle(R.string.title_delete);
			builder.setMessage(R.string.message_delete);
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   new deleteExistingTask().execute();
	           }
		    });
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
				}
			});
			listOptions = builder.create();
			break;
		case 3:
			builder.setTitle(R.string.title_new);
            builder.setView(textEntryView);
            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
            		EditText cd_et = (EditText)textEntryView.findViewById(R.id.cd_et);
            		sprt = cd_et.getText().toString();
            		cd_et.setText("");
            		if(sprt.isEmpty()) {
            			Toast.makeText(MainActivity.this, R.string.require_field, Toast.LENGTH_LONG).show();
            		}
            		else {
            			new createNewTask().execute();
            		}
            	}
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int item) {
            	}
            });      
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
				}
			});
            listOptions = builder.create();
			break;
		}
		return listOptions;
	}
	/*
	 * star new async thread to create user task in server
	 */
	private class createNewTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		@Override
		protected void onPreExecute() {
            Dialog.setMessage("Por favor espere");
            Dialog.show();
            spprtModel.clear();
        }
		@Override
		protected Void doInBackground(Void... params) {
			TaskAccess oTA = new TaskAccess();
			oTA.createNewTask(sprt, user);
			UserAccess oUA = new UserAccess();
			JSONArray jsonArray = oUA.readUserTasks(user);
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					Tasks oTask = new Tasks();
					jsonObjet = jsonArray.getJSONObject(i);
					oTask.setId(jsonObjet.getString("id"));
					oTask.setUserId(jsonObjet.getString("user_id"));
					oTask.setTask(jsonObjet.getString("name"));
					spprtModel.add(oTask);
				}
				catch(JSONException e) {}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
			clearAdapter();
			taskAdapter = new taskAdapter();
			ma_lv.setAdapter(taskAdapter);
			while(i < spprtModel.size()) {
				Tasks oTask = spprtModel.get(i);
				taskAdapter.add(oTask);
				i++;			
			}
			Dialog.dismiss();
			Toast.makeText(MainActivity.this, R.string.create_success, Toast.LENGTH_LONG).show();
	    }
	}
    /*
	 * star new async thread to retrive the user task from server
	 */
	private class readUserTasks extends AsyncTask<Void, Void, Void> {
		ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		@Override
		protected void onPreExecute() {
			spprtModel.clear();
            Dialog.setMessage("Por favor espere");
            Dialog.show();
        }
		@Override
		protected Void doInBackground(Void... params) {
			UserAccess oUA = new UserAccess();
			JSONArray jsonArray = oUA.readUserTasks(user);
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					Tasks oTask = new Tasks();
					jsonObjet = jsonArray.getJSONObject(i);
					oTask.setId(jsonObjet.getString("id"));
					oTask.setUserId(jsonObjet.getString("user_id"));
					oTask.setTask(jsonObjet.getString("name"));
					spprtModel.add(oTask);
				}
				catch(JSONException e) {}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
			clearAdapter();
			taskAdapter = new taskAdapter();
			ma_lv.setAdapter(taskAdapter);
			while(i < spprtModel.size()) {
				Tasks oTask = spprtModel.get(i);
				taskAdapter.add(oTask);
				i++;			
			}
			Dialog.dismiss();
	    }
	}
	/*
	 * star new async thread to update a user task in server
	 */
	private class updateExistingTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		@Override
		protected void onPreExecute() {
			spprtModel.clear();
            Dialog.setMessage("Por favor espere");
            Dialog.show();
        }
		@Override
		protected Void doInBackground(Void... params) {
			TaskAccess oTA = new TaskAccess();
			UserAccess oUA = new UserAccess();
			oTA.updateExistingTask(sprt, task);
			JSONArray jsonArray = oUA.readUserTasks(user);
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					Tasks oTask = new Tasks();
					jsonObjet = jsonArray.getJSONObject(i);
					oTask.setId(jsonObjet.getString("id"));
					oTask.setUserId(jsonObjet.getString("user_id"));
					oTask.setTask(jsonObjet.getString("name"));
					spprtModel.add(oTask);
				}
				catch(JSONException e) {}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
			clearAdapter();
			taskAdapter = new taskAdapter();
			ma_lv.setAdapter(taskAdapter);
			while(i < spprtModel.size()) {
				Tasks oTask = spprtModel.get(i);
				taskAdapter.add(oTask);
				i++;			
			}
			Dialog.dismiss();
			Toast.makeText(MainActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
	    }
	}
	/*
	 * star new async thread to delete the user task from server
	 */
	private class deleteExistingTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		@Override
		protected void onPreExecute() {
			spprtModel.clear();
            Dialog.setMessage("Por favor espere");
            Dialog.show();
        }
		@Override
		protected Void doInBackground(Void... params) {
			TaskAccess oTA = new TaskAccess();
			UserAccess oUA = new UserAccess();
			oTA.deleteExistingTask(task);
			JSONArray jsonArray = oUA.readUserTasks(user);
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					Tasks oTask = new Tasks();
					jsonObjet = jsonArray.getJSONObject(i);
					oTask.setId(jsonObjet.getString("id"));
					oTask.setUserId(jsonObjet.getString("user_id"));
					oTask.setTask(jsonObjet.getString("name"));
					spprtModel.add(oTask);
				}
				catch(JSONException e) {}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			int i = 0;
			clearAdapter();
			taskAdapter = new taskAdapter();
			ma_lv.setAdapter(taskAdapter);
			while(i < spprtModel.size()) {
				Tasks oTask = spprtModel.get(i);
				taskAdapter.add(oTask);
				i++;			
			}
			Dialog.dismiss();
			Toast.makeText(MainActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
	    }
	}
	/*
	 * set a row to ListView
	 */
	class taskAdapter extends ArrayAdapter<Tasks> {
		public taskAdapter() {
			super(MainActivity.this, R.layout.row_tasks, taskModel);
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View row_task = convertView;
			taskHolder holder = null;
			if(row_task == null) {
				LayoutInflater inflater = getLayoutInflater();
				row_task = inflater.inflate(R.layout.row_tasks, parent, false);
				holder = new taskHolder(row_task);
				row_task.setTag(holder);
			}
			else {
				holder = (taskHolder)row_task.getTag();
			}
			holder.populateFrom(taskModel.get(position));
			return row_task;
		}
	}
	/*
	 * set information to the row
	 */
	static class taskHolder {
		private TextView rt_tv = null;
		public taskHolder(View row) {
			rt_tv = (TextView)row.findViewById(R.id.rt_tv);
		}
		public void populateFrom(Tasks r) {
			rt_tv.setText(r.getTask());
		}
	}
	/*
	 * clear all adapters
	 */
	private void clearAdapter() {
		if(taskAdapter != null) {
			taskAdapter.clear();
		}
	}
}
