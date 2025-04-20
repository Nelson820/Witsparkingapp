package com.example.witsparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StudentRegister extends AppCompatActivity {

    TextView register;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_register);

        EditText firstname = findViewById(R.id.fname);
        EditText lastname = findViewById(R.id.lname);
        EditText email = findViewById(R.id.email);
        EditText studentnum = findViewById(R.id.Snumber);
        EditText password = findViewById(R.id.password);
        EditText contact = findViewById(R.id.contacts);
        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                       @Override
                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                                           String item  = parent.getItemAtPosition(position).toString();
                                           Toast.makeText(StudentRegister.this,"Selected Item" , Toast.LENGTH_SHORT).show();
                                       }

                                       @Override
                                       public void onNothingSelected(AdapterView<?> parent) {

                                       }
                                   });




        ArrayList<String> arraylist = new ArrayList<>();
        arraylist.add("FirstYear");
        arraylist.add("SecondYear");
        arraylist.add("ThirdYear");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arraylist);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);



        register = findViewById(R.id.Register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://lamp.ms.wits.ac.za/home/s2654606/students.php";

                //Retrieve data from EditText
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String Email = email.getText().toString();
                String student = studentnum.getText().toString();
                String pwd = password.getText().toString();
                String Contact = contact.getText().toString();
                String year =spinner.getSelectedItem().toString();

                // Check if any field is empty
                if (fname.isEmpty() || lname.isEmpty() || Email.isEmpty() || student.isEmpty() || pwd.isEmpty() || Contact.isEmpty() || year.isEmpty()) {
                    Toast.makeText(StudentRegister.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Create OkHttpClient to make requests on the server
                OkHttpClient client = new OkHttpClient();

                //Create the form body of the request
                //Data I want to send to the sever
                RequestBody Formbody = new FormBody.Builder()
                        .add("Firstname", fname)
                        .add("Lastname", lname)
                        .add("Email", Email)
                        .add("StudentNumber", student)
                        .add("Password", pwd)
                        .add("Contact", Contact)
                        .add("Year", year)

                        .build();

                //Create the Post Request
                Request request = new Request.Builder()
                        .url(url)
                        .post(Formbody)
                        .build();


                //Execute the request asynchonously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(StudentRegister.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        runOnUiThread(() -> {
                            if (response.isSuccessful()) {
                                Toast.makeText(StudentRegister.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StudentRegister.this, MainActivity1.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(StudentRegister.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }
}