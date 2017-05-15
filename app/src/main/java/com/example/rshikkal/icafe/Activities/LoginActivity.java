package com.example.rshikkal.icafe.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rshikkal.icafe.Models.User;
import com.example.rshikkal.icafe.Parsers.UserParser;
import com.example.rshikkal.icafe.Preferences.LoginPreferences;
import com.example.rshikkal.icafe.R;
import com.example.rshikkal.icafe.ServerUtils.ServerRequests;
import com.example.rshikkal.icafe.Utils.DialogUtils;
import com.example.rshikkal.icafe.Utils.DisplayToast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    Button loginBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        emailET = (EditText)findViewById(R.id.email);
        passwordET = (EditText)findViewById(R.id.password);
        loginBT = (Button)findViewById(R.id.login_button);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = emailET.getText().toString();
                String userpassword = passwordET.getText().toString();
                if (useremail.equals("") || userpassword.equals("")){
                   Toast.makeText(LoginActivity.this,"enter email and password", Toast.LENGTH_LONG).show();
                }
                else{
                    loginUser(useremail, userpassword);
                }
            }
        });

    }

    private void loginUser(final String useremail, final String userpassword) {
        new AsyncTask<Object, Object, JSONObject>(){

            DialogUtils progressDialog = new DialogUtils(LoginActivity.this,DialogUtils.Type.PROGRESS_DIALOG);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.showProgressDialog("Login user", "please wait...",false);
            }

            @Override
            protected JSONObject doInBackground(Object... params) {
                return new ServerRequests().loginUser(useremail,userpassword);
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                super.onPostExecute(response);
                progressDialog.dismissProgressDialog();
                LoginPreferences loginPreferences = new LoginPreferences(LoginActivity.this);
                try {
                    if (response != null) {
                        if (response.getString("status").equals("success")) {
                            User user = new UserParser().parse(response.getJSONObject("user"));
                            loginPreferences.loginUser(user);
                            openMainActivity();
                        } else if (response.getString("status").equals("failed")) {
                            Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
