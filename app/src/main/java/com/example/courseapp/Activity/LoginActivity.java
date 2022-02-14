package com.example.courseapp.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseapp.R;
import com.example.courseapp.RESTController;

import java.io.IOException;
import java.util.Properties;

import static com.example.courseapp.Constants.LOGIN_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        EditText login = findViewById(R.id.loginField);
        EditText psw = findViewById(R.id.pswField);

        Properties properties = new Properties();
        properties.setProperty("login", login.getText().toString());
        properties.setProperty("psw", psw.getText().toString());

        String data = properties.toString();

        //GsonBuilder gsonBuilder = new GsonBuilder();


        //System.out.println(data);


        if (TextUtils.isEmpty(login.getText()) || TextUtils.isEmpty(psw.getText())) {
            Toast.makeText(getApplicationContext(), "Please fill in the fields", Toast.LENGTH_LONG).show();
        } else {
            UserValidate userValidate = new UserValidate();
            userValidate.execute(data);
        }

    }

    private final class UserValidate extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LoginActivity.this, "Sending data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return RESTController.sendPost(LOGIN_URL, strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null && !response.equals("Error") && !response.equals("Wrong credentials")) {
                int userID = Integer.parseInt(response);
                startActivity(new Intent(LoginActivity.this, MyCoursesActivity.class).putExtra("userID", userID));
            } else {
                Toast.makeText(LoginActivity.this, "No such user found", Toast.LENGTH_LONG).show();
            }

        }
    }
}

