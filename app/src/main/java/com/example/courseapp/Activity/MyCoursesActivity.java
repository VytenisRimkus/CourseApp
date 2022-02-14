package com.example.courseapp.Activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.courseapp.R;
import com.example.courseapp.RESTController;
import com.example.courseapp.ds.Course;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.courseapp.Constants.CRS_LIST;

public class MyCoursesActivity extends AppCompatActivity {

    int userID;
    ListView courseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        setCurrentUser();
        setCourses();
    }

    private void setCourses() {
        Courses courses = new Courses();
        courses.execute();
    }


    private final class Courses extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {

            try {
                return RESTController.sendGet(CRS_LIST);
            } catch (IOException e) {
                e.printStackTrace();
                return "ERROR";
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                if (!response.equals("") && !response.equals("ERROR")){
                    Type courseListType = new TypeToken<List<Course>>(){
                    }.getType();

                    courseListView = findViewById(R.id.courseListView);
                    JSONArray json = new JSONArray(response);

                    ArrayList<String> appendJson = new ArrayList<>();

                    for (int i=0; i<json.length(); i++){
                        JSONArray responsibleUserJSON = json.getJSONObject(i).getJSONArray("responsibleUsers");
                        JSONObject courseJson = json.getJSONObject(i);
                        for (int j=0; j<responsibleUserJSON.length(); j++){
                             String userIdInString = responsibleUserJSON.getJSONObject(j).getString("ID");
                             if (userID == Integer.parseInt(userIdInString)){
                                 courseJson.remove("responsibleUsers");
                                 courseJson.remove("ID");
                                 String formattedString = courseJson.toString();
                                 appendJson.add(formattedString);
                             }
                        }
                    }
                    courseListView = findViewById(R.id.courseListView);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MyCoursesActivity.this, android.R.layout.simple_list_item_1, appendJson);
                    courseListView.setAdapter(arrayAdapter);


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    private void setCurrentUser() {
        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
    }
}