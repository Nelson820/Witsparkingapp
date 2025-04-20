package com.example.witsparking;
import android.app.Activity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public   class parkingSpot2 {


     private String spot;
    private String coordinates;
    private String state;





    private String urlprefix="https://lamp.ms.wits.ac.za/home/s2683067/";
    private String urlprefixgsp="https://lamp.ms.wits.ac.za/home/s2683067/getspot.php?table=";


    public  void getspot(String response) {

        try {
            JSONArray jsonArray = new JSONArray(response);
            System.out.println(response);

            //JSONArray jsonArray = jsonObject.getJSONArray("CARS");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2= jsonArray.getJSONObject(i);
                this.spot=jsonObject2.getString("parkingspot");
                this.coordinates =jsonObject2.getString("coordinates");



            }


        }

        catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

   // https://lamp.ms.wits.ac.za/home/s2683067/freespot.php?table=ThirdYearParking&parkingspot=1
    public void changestate( String parkinglot,String newState,String spotChanged,Activity a) {
        if(newState.equals("bookspot")){
            state="booked";

        }
        if(newState.equals("reservespot")){
            state="reseved";

        }
        else{
            state="empty";
        }


        fetch data= new fetch();
        data.getdata(a, urlprefix+newState+".php?table="+parkinglot+"&parkingspot="+spotChanged, new requesthandler() {
                    @Override
                    public void processresponse(String responce) {

                    }

                });

       /// state="booked";do this!!!!!!!!!!!!!
    }

   // https://lamp.ms.wits.ac.za/home/s2683067/getspot.php?table=ThirdYearParking
    public void findspot ( String parkinglot , String action,Dashboard2 a) {
        fetch data= new fetch();

        data.getdata(a, urlprefixgsp+parkinglot, new requesthandler() {
            @Override
            public void processresponse(String responce) {
                getspot(responce);
                a.setspot();


                if(action.equals("bookspot")){
                    Toast.makeText(a, "you have successfully booked a spot", Toast.LENGTH_SHORT).show();


                    parkingSpot2.this.changestate(parkinglot,"bookspot",spot,a);

                }
                else if(action.equals("reservespot")){
                    Toast.makeText(a, "spot successfully reserved press travel button when ready to go", Toast.LENGTH_LONG).show();

                   parkingSpot2.this.changestate(parkinglot,"reservespot",spot,a);

                }
                else{
                    coordinates=null;
                    spot=null;
                }

            }
        });


    }

    public void freespot(String parkinglot,String spot,Dashboard2 a){

        parkingSpot2.this.changestate(parkinglot,"empty",spot,a);

    }




    public String getspotnumber(){
        return spot;

    };

    public String getcoordinates(){
        return coordinates;

    };
    public String getstate(){
        return state;

    };
    public void setcoordinates(String c){

        this.coordinates=c;
    }

}




