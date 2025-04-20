package com.example.witsparking;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard2 extends AppCompatActivity {

    Dashboard2 b = Dashboard2.this;
    parkingSpot2 spot = new parkingSpot2();
    TextView book;
    String year;
    int counter=0;
    ImageView logout;



    String coordinatesPrev;
    int counterPrev;
    String spotPrev;
    SharedPreferences data;

    public static final String SHARED_PREFS="Sharedpref";

    Context context;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard2);
        book=findViewById(R.id.booking);
         year= getIntent().getStringExtra("YearOfStudy");
         context = this;
        logout = findViewById(R.id.logout);  // Initialize logout here
        data= getSharedPreferences("Data", Context.MODE_PRIVATE);


        coordinatesPrev=data.getString("Coordinates",null);
        counter=data.getInt("counter",0);
        spotPrev=data.getString("spotnumber",null);

        if(spotPrev==null){
            book.setText("you have no bookings");
        }
        else{
            book.setText("You have been allocated parking spot :"+spotPrev);
        }

        // Check if year is passed correctly from the Intent
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter !=0){
                    Toast.makeText(Dashboard2.this,"please free your current booking before logging out",Toast.LENGTH_SHORT).show();

                }
                else {
                    SharedPreferences.Editor editor1= data.edit();
                    editor1.putString("Coordinates",null);
                    editor1.putString("spotnumber",null);
                    editor1.putInt("counter",0);
                    editor1.apply();

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name", "false");
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity1.class);// where to go loginpage
                    startActivity(intent);
                    finish();
                }





            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }






    private void GoToSpot(String coordinates){

        if(coordinates==null){
            Toast.makeText(Dashboard2.this, "could not find spot,please try finding a spot again..", Toast.LENGTH_SHORT).show();
        }

        else {
            String[] split = coordinates.split(",");
            String latitude = split[0];
            String longitude = split[1];

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&avoid=tf");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

    }

    public void setspot(){

        if(spotPrev==null) {
            book.setText("You have been allocated parking spot :" + spot.getspotnumber());
            SharedPreferences.Editor editor= data.edit();
            editor.putString("Coordinates",spot.getcoordinates());
            editor.putString("spotnumber",spot.getspotnumber());
            editor.putInt("counter",counter);
            editor.apply();

        }

    }

    public synchronized   void test2(View v){
        if (counter==0) {
            reserve(year + "Parking");
            book.setText("You have been allocated parking spot :" + spot.getspotnumber());

        }
        else{
            Toast.makeText(Dashboard2.this, "you already have a spot", Toast.LENGTH_SHORT).show();
        }
    }


    public  void Gotospot(View v){
        System.out.println(coordinatesPrev);
        System.out.println(spot.getcoordinates());

        if(coordinatesPrev==null){
            GoToSpot(spot.getcoordinates());
        }
        else{

            GoToSpot(coordinatesPrev);

        }
    }

    private void book(String parkinglot){
        spot.findspot(parkinglot,"bookspot",b);

    }

    private void reserve(String parkinglot){
        spot.findspot(parkinglot,"reservespot",b);
        counter=1;
    }

    private void free(String parkinglot){
        System.out.println(parkinglot);
        spot.freespot(parkinglot,spot.getspotnumber(),b);


    }
//"https://lamp.ms.wits.ac.za/home/s2683067/freespot.php?table=" + year + "Parking" + "&parkingspot=" + spot.getspotnumber(),
    public void cancel(View v){

        fetch data1= new fetch();

        System.out.println(data.getString("spotnumber",null));
        System.out.println(spot.getspotnumber());



        data1.getdata(b, "https://lamp.ms.wits.ac.za/home/s2683067/freespot.php?table=" +year+ "Parking" + "&parkingspot=" +data.getString("spotnumber",null), new requesthandler() {
            @Override
            public void processresponse(String responce) {
                book.setText("you have no bookings");

            }
        });


        counter=0;
        spot.setcoordinates(null);

        //book.setText("you have no bookings");

        SharedPreferences.Editor editor= data.edit();
        editor.putString("Coordinates",null);
        editor.putString("spotnumber",null);
        editor.putInt("counter",0);
        editor.apply();

    }




}