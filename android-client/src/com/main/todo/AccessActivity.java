package com.main.todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.web.todo.UserAccess;

public class AccessActivity extends Activity {

	Button aa_bn;
	
	private String user = "";
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * app start callback
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_activity);
        aa_bn = (Button)findViewById(R.id.aa_bn);
        aa_bn.setOnClickListener(newTask);
    }
    /*
     * new task button event callback
     */
    private View.OnClickListener newTask = new View.OnClickListener() {
		public void onClick(View v) {
			new accessUser().execute();
		}
	};
    /*
	 * star new async thread to validate the user
	 */
	private class accessUser extends AsyncTask<Void, Void, Void> {
		ProgressDialog Dialog = new ProgressDialog(AccessActivity.this);
		EditText aa_et = (EditText)findViewById(R.id.aa_et);
		int access = 0;
		@Override
		protected void onPreExecute() {
            Dialog.setMessage("Por favor espere");
            Dialog.show();
			if(aa_et.getText().toString().isEmpty()) {
				access = 1;
			}
        }
		@Override
		protected Void doInBackground(Void... params) {
			if(access != 0) {
				return null;
			}
			UserAccess oUA = new UserAccess();
			JSONArray jsonArray = oUA.validateUser(aa_et.getText().toString());
			if(jsonArray.length() <= 0) {
				access = 2;
				return null;
			}
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					jsonObjet = jsonArray.getJSONObject(i);
					user = jsonObjet.getString("id");
				}
				catch(JSONException e) {}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(access == 0) {
				Intent main = new Intent(AccessActivity.this, MainActivity.class);
				main.putExtra("user", user);
				startActivity(main);
				finish();
			}
			else if(access == 1) {
				TextView aa_tv = (TextView)findViewById(R.id.aa_tv);
				aa_tv.setText(R.string.required_user);
			}
			else if(access == 2) {
				TextView aa_tv = (TextView)findViewById(R.id.aa_tv);
				aa_tv.setText(R.string.inexist_user);
			}
			Dialog.dismiss();
		}
	}
}
