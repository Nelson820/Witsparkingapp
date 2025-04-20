package com.example.witsparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity1 extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    EditText usernameEditText;
    EditText passwordEditText;
    String year;
    public static final String SHARED_PREFS="Sharedpref";

    TextView v;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        check();


        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.Password);

        findViewById(R.id.button).setOnClickListener(view -> {
            // Retrieve username and password from EditText fields
            String StudentNumber = usernameEditText.getText().toString();
            String Password = passwordEditText.getText().toString();

            // Perform login with username and password
            performLogin(StudentNumber, Password);

        });
        v = findViewById(R.id.move);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void performLogin(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        // Construct the form body
        RequestBody formBody = new FormBody.Builder()
                .add("Email", username)
                .add("Password", password)
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2654606/login.php")
                .post(formBody)
                .build();

        // Make the network request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                // Handle failure appropriately
                runOnUiThread(() ->
                        Toast.makeText(MainActivity1.this, "Network request failed", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseStr = response.body().string();
                    runOnUiThread(() -> {
                        // Check if the response indicates successful login
                        if (responseStr.startsWith("found_student") ) {

                            SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("name","true");
                            editor.apply();
                            // Display "Login Successful" message
                            Toast.makeText(MainActivity1.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            ////year=...
                            String YearOfStudy =responseStr.replace("found_student","").trim();
                            Intent intent = new Intent(MainActivity1.this, Dashboard2.class);
                            intent.putExtra("YearOfStudy",YearOfStudy);
                            startActivity(intent);
                            finish();
                        }
                        else if (responseStr.startsWith("found_staff")){
                            SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("name","staff");
                            editor.apply();

                                Toast.makeText(MainActivity1.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                Intent intent1 = new Intent(MainActivity1.this, Dashboard.class);
                                startActivity(intent1);
                                finish();

                        }else {
                            // Display "Login Failed" message
                            Toast.makeText(MainActivity1.this, "Login Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle unsuccessful response
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity1.this, "Login Failed", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    public String get(){
        return this.year;

    }

    private void check(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check=sharedPreferences.getString("name","");
        if(check.equals("true")){
            Toast.makeText(MainActivity1.this,"successful",Toast.LENGTH_SHORT);
            Intent intent= new Intent(getApplicationContext(),Dashboard2.class);// where to go dashboard
            startActivity(intent);
            finish();
        }
        if(check.equals("staff")){
            Toast.makeText(MainActivity1.this,"successful",Toast.LENGTH_SHORT);
            Intent intent= new Intent(getApplicationContext(),Dashboard.class);// where to go dashboard
            startActivity(intent);
            finish();


        }


    }


    private void check2(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check=sharedPreferences.getString("name","");
        if(check.equals("true")){
            Toast.makeText(MainActivity1.this,"successful",Toast.LENGTH_SHORT);
            Intent intent= new Intent(getApplicationContext(),Dashboard2.class);// where to go dashboard
            startActivity(intent);
            finish();


        }


    }



}