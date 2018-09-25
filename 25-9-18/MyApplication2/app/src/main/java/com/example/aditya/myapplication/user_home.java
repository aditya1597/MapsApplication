package com.example.aditya.myapplication;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibm.watson.developer_cloud.http.*;
import com.bvapp.arcmenulibrary.ArcMenu;
import com.example.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class user_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,LocationListener {
    private GoogleMap mMap;
    LocationManager locmgr;

    Location l;
    ArcMenu menu,sos_menu,feed_menu;
    Marker m;
    MarkerOptions mo;
    public String type="";
    GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
    String GOOGLE_API_KEY="AIzaSyDDOv1gkxueAZM24BtjonE6beLqsT3rQrg";
    private static final boolean PRINT_AS_STRING = false;
    String provider;
    double lat;double lng;
    private static final String[] sosmenu_options={"Call SOS services","Request Volunteers","Find Doctor"};
    private static final int[] sos_items={R.drawable.call_sos,R.drawable.request,R.drawable.medical_help};
    private static final String[] STR = {"People Finder","Chat","Safety Instructions"};
    private static final int[] ITEM_DRAWABLES = {R.drawable.people_finder,R.drawable.chat,R.drawable.safe_shield_protection};
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(user_home.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locmgr.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                        mMap.setMyLocationEnabled(true);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    public void blur_feed_info(String status)
    {
        if(status.equals("blur"))
        {
            ScrollView sv=findViewById(R.id.user_home_scrollview);
            sv.setAlpha(1);
            CoordinatorLayout cl=findViewById(R.id.layMain_feed);
            cl.setAlpha(1);
            CoordinatorLayout cl2=findViewById(R.id.layMain);
            cl2.setAlpha(1);
        }
        else
        {
            ScrollView sv=findViewById(R.id.user_home_scrollview);
            sv.setAlpha(0.75f);
            CoordinatorLayout cl=findViewById(R.id.layMain_feed);
            cl.setAlpha(0f);
            CoordinatorLayout cl2=findViewById(R.id.layMain);
            cl2.setAlpha(0f);
        }
    }
    public void blur_sos_feed(String status)
    {
        if(status.equals("blur"))
        {
            ScrollView sv=findViewById(R.id.user_home_scrollview);
           sv.setAlpha(1);
            CoordinatorLayout cl=findViewById(R.id.layMain_feed);
            cl.setAlpha(1);
            CoordinatorLayout cl2=findViewById(R.id.layMain_sos);
            cl2.setAlpha(1);
        }
        else
        {
            ScrollView sv=findViewById(R.id.user_home_scrollview);
            sv.setAlpha(0.75f);
            CoordinatorLayout cl=findViewById(R.id.layMain_feed);
            cl.setAlpha(0f);
            CoordinatorLayout cl2=findViewById(R.id.layMain_sos);
            cl2.setAlpha(0f);
        }
    }
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView status=findViewById(R.id.flood_status);
        setSupportActionBar(toolbar);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( this);
            FirebaseDatabase db=FirebaseDatabase.getInstance();
            DatabaseReference dbref=db.getReference();
            dbref.child("prediction").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String val=dataSnapshot.getValue(String.class);
                    switch (val)
                    {

                        case "Red":
                            status.setText("High chances of flood in your area.");
                            status.setBackgroundColor(Color.RED);
                            break;
                        case "Yellow":
                            status.setText("Be alert.");
                            status.setBackgroundColor(Color.YELLOW);
                            break;
                        case "Green":
                            status.setText("Very less chances of Flood.");
                            status.setBackgroundColor(Color.GREEN);
                            break;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            provider = locmgr.GPS_PROVIDER;
            if (checkLocationPermission()) {
                locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locmgr.requestLocationUpdates(provider, 20000, 50, (LocationListener) this);
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //FabButton menu options for sos
        RecyclerView recyclerView_sos = (RecyclerView) findViewById(R.id.recycler_view_sos);
        sos_menu=(ArcMenu)findViewById(R.id.sos_arcMenu);
        sos_menu.attachToRecyclerView(recyclerView_sos);
        sos_menu.showTooltip(true);
        sos_menu.setToolTipBackColor(Color.WHITE);
        sos_menu.setToolTipCorner(6f);
        sos_menu.setToolTipPadding(2f);
        sos_menu.setToolTipTextColor(Color.BLUE);

        sos_menu.setIcon(R.drawable.sos);
        sos_menu.setAnim(300,300,ArcMenu.ANIM_MIDDLE_TO_LEFT,ArcMenu.ANIM_MIDDLE_TO_LEFT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE,ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);
        sos_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sos_menu.isOpen())
                {
                    blur_feed_info("blur");
                }
                else
                {
                    blur_feed_info("abc");
                }
            }
        });
        for (int i = 0; i < 3; i++) {
            FloatingActionButton item = new FloatingActionButton(this);
            item.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
            item.setImageResource(sos_items[i]); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
            item.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
            // menu.setChildSize(item.getIntrinsicHeight()); // set absolout child size for menu

            final int position = i;
            sos_menu.addItem(item, sosmenu_options[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //You can access child click in here
                    switch(position)
                    {
                        case 0:
                            Intent i0=new Intent(user_home.this,call_sos_services.class);
                            startActivity(i0);
                            break;


                        case 1:
                            Intent i1=new Intent(user_home.this,request_help.class);
                            startActivity(i1);
                            break;

                        case 2:
                            Intent i2=new Intent(user_home.this,request_doctor.class);
                            startActivity(i2);
                            break;

                    }

                }
            });
        }

        //FabButton menu options for social feeds.
        RecyclerView recyclerView_feed = (RecyclerView) findViewById(R.id.recycler_view_feed);
        feed_menu = (ArcMenu) findViewById(R.id.feed_arcMenu);
        feed_menu.attachToRecyclerView(recyclerView_sos);
        feed_menu.showTooltip(true);
        feed_menu.setToolTipBackColor(Color.WHITE);
        feed_menu.setToolTipCorner(6f);
        feed_menu.setToolTipPadding(2f);
        feed_menu.setToolTipTextColor(Color.BLUE);
        feed_menu.setIcon(R.drawable.feed);
        feed_menu.setAnim(300,300,ArcMenu.ANIM_MIDDLE_TO_RIGHT,ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE,ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);
        feed_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feeds=new Intent(user_home.this,voip_call.class);
                startActivity(feeds);
            }
        });


        //FabButton menu options for info
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_info);
        menu = (ArcMenu) findViewById(R.id.arcMenu);
        menu.attachToRecyclerView(recyclerView);
        menu.showTooltip(true);
        menu.setToolTipBackColor(Color.WHITE);
        menu.setToolTipCorner(6f);
        menu.setToolTipPadding(2f);
        menu.setToolTipTextColor(Color.BLUE);
        menu.setIcon(R.drawable.tools);
        menu.setAnim(300,300,ArcMenu.ANIM_MIDDLE_TO_RIGHT,ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE,ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu.isOpen())
                {
                    blur_sos_feed("blur");
                }
                else
                {
                    blur_sos_feed("abc");
                }
            }
        });
        for (int i = 0; i < 3; i++) {

            FloatingActionButton item = new FloatingActionButton(this);
            item.setSize(FloatingActionButton.SIZE_MINI); // set initial size for child, it will create fab first
            item.setImageResource(ITEM_DRAWABLES[i]); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
            item.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // it will set fab child's color
            // menu.setChildSize(item.getIntrinsicHeight()); // set absolout child size for menu

            final int position = i;
            menu.addItem(item, STR[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //You can access child click in here
                    switch(position)
                    {
                        case 0:
                            Intent i0=new Intent(user_home.this,people_finder.class);
                            startActivity(i0);
                            break;


                        case 1:
                            Intent i1=new Intent(user_home.this,chat.class);
                            startActivity(i1);
                            break;

                        case 2:
                            Intent i2=new Intent(user_home.this,safety_instructions.class);
                            startActivity(i2);
                            break;

                    }

                }
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void addmenu(int i)
    {
        // Use internal FAB as child
        // ********* import com.bvapp.arcmenulibrary.widget.FloatingActionButton; *********


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    public void getnearbyplaces(String t)
    {
        type=t;
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + lat + "," + lng);
        googlePlacesUrl.append("&radius=" + 500);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }
    @Override
    public void onLocationChanged(Location location) {
        try {
            Toast.makeText(getApplicationContext(), location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG).show();
            double dist=distance(lat,lng,location.getLatitude(),location.getLongitude());
            if(dist>0.5) {
                lat=location.getLatitude();
                lng=location.getLongitude();

                global_data gd=new global_data();
                gd.places=1;
                getnearbyplaces("hospital");

                gd.places=2;
                getnearbyplaces("school");
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                global_data gd2=new global_data();

                DocumentReference dref=db.collection("user_location_data").document(gd.mobile_number.toString());
                Map<String,Object> update_data=new HashMap<>();
                update_data.put("user_mobile_number",gd2.mobile_number.toString());
                GeoPoint g= new GeoPoint(lat,lng);
                update_data.put("user_location",g);
                dref.update(update_data);
                if(global_data.doctor.equals("true"))
                {
                    DocumentReference dref_doc=db.collection("user_location_data").document(gd.mobile_number.toString());
                    Map<String,Object> update_data_doc=new HashMap<>();
                    update_data_doc.put("user_mobile_number",gd2.mobile_number.toString());
                    GeoPoint g_doc= new GeoPoint(lat,lng);
                    update_data_doc.put("user_location",g);
                    dref_doc.update(update_data_doc);
                }
                if(global_data.volunteer.equals("true"))
                {
                    DocumentReference dref_doc=db.collection("user_location_data").document(gd.mobile_number.toString());
                    Map<String,Object> update_data_doc=new HashMap<>();
                    update_data_doc.put("user_mobile_number",gd2.mobile_number.toString());
                    GeoPoint g_doc= new GeoPoint(lat,lng);
                    update_data_doc.put("user_location",g);
                    dref_doc.update(update_data_doc);
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(checkLocationPermission())
        {
            locmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locmgr.requestLocationUpdates(provider, 20000, 500, (LocationListener) this);
            mMap.setMyLocationEnabled(true);
            mMap.getMyLocation();
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user_location_data").document(global_data.mobile_number.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot pdata = task.getResult();
                        if (pdata.exists()) {
                            String data = pdata.getData().toString();
                            lat = Double.parseDouble(data.substring(data.indexOf("latitude=") + 9, data.indexOf(",", data.indexOf("latitude="))));
                            lng = Double.parseDouble(data.substring(data.indexOf("longitude=") + 10, data.indexOf("}", data.indexOf("longitude="))));
                            Log.d("Location",lat+","+lng);
                            global_data.places = 1;
                            getnearbyplaces("hospital");
                            global_data.places = 2;
                            getnearbyplaces("school");
                        }
                    }
                });
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            checkLocationPermission();
        }


    }
}
