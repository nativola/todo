package com.main.todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.web.todo.DataAccess;

public class AccessActivity extends Activity {

	Button aa_bn;
	
	private boolean access = false;
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
		private ProgressDialog Dialog = new ProgressDialog(AccessActivity.this);
		@Override
		protected void onPreExecute() {
            Dialog.setMessage("Por favor espere");
            Dialog.show();
        }
		@Override
		protected Void doInBackground(Void... params) {
			EditText aa_et = (EditText)findViewById(R.id.aa_et);
			DataAccess oDA = new DataAccess();
			JSONArray jsonArray = oDA.validateUser(aa_et.getText().toString());
			JSONObject jsonObjet;
			for(int i = jsonArray.length()-1; i >= 0; i--) {
				try {
					jsonObjet = jsonArray.getJSONObject(i);
					user = jsonObjet.getString("id");
				}
				catch(JSONException e) {}
				access = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(access) {
				Intent main = new Intent(AccessActivity.this, MainActivity.class);
				main.putExtra("user", user);
				startActivity(main);
				finish();
			}
			else {
				TextView aa_tv = (TextView)findViewById(R.id.aa_tv);
				aa_tv.setText(R.string.invalid_access);
			}
			Dialog.dismiss();
		}
	}
}
