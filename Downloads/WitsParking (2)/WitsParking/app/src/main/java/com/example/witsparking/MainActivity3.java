package com.example.witsparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity3 extends AppCompatActivity {

    TextView register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        EditText firstname = findViewById(R.id.fname);
        EditText lastname = findViewById(R.id.lname);
        EditText email = findViewById(R.id.email);
        EditText staffnum = findViewById(R.id.Snumber);
        EditText password = findViewById(R.id.password);
        EditText contact = findViewById(R.id.contacts);

        register = findViewById(R.id.Register2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText and trim
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String Email = email.getText().toString();
                String staff = staffnum.getText().toString();
                String pwd = password.getText().toString();
                String Contact = contact.getText().toString();

                // Check if any field is empty
                if (fname.isEmpty() || lname.isEmpty() || Email.isEmpty() || staff.isEmpty() || pwd.isEmpty() || Contact.isEmpty()) {
                    Toast.makeText(MainActivity3.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://lamp.ms.wits.ac.za/home/s2654606/staff.php";

                // Create OkHttpClient to make requests on the server
                OkHttpClient client = new OkHttpClient();

                // Create the form body of the request
                RequestBody formbody = new FormBody.Builder()
                        .add("Firstname", fname)
                        .add("Lastname", lname)
                        .add("Email", Email)
                        .add("StaffNumber", staff)
                        .add("Password", pwd)
                        .add("Contact", Contact)
                        .build();

                // Create the Post Request
                Request request = new Request.Builder()
                        .url(url)
                        .post(formbody)
                        .build();

                // Execute the request asynchronously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity3.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(() -> {
                            if (response.isSuccessful()) {
                                Toast.makeText(MainActivity3.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity3.this, MainActivity1.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity3.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}
