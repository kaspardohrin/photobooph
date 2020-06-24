package com.example.photobooph;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final static String LOG_TAG = "Homescreen: ";
    public final static String IMAGE_URL = "https://picsum.photos/id/";


    // Array that saves images that are saved by the user
    ArrayList<String> linksArray;

    // Variable randomNumber
    private int randomNumber;

    // Getter for variable randomNumber
    public int getRandomNumber() {
        return randomNumber;
    }

    // Setter for variable randomNumber
    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // New array to put saved images in
        linksArray = new ArrayList<>();

        setContentView(R.layout.activity_main);

        // Assinging the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Function that handles clicks for the save button
    public void onClickSave(View v) {
        // Adding the link of the image to the array with the ID of the image(in this case equal to randomNumber)
        linksArray.add("https://picsum.photos/id/" + getRandomNumber());
        Log.d(LOG_TAG, "Image with ID was saved: " + linksArray);

        Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
    }

    // Function that handles clicks for the next button
    public void onClickNext(View v) {
        RequestQueue requestQueue;

        TextView tvTextTitle = findViewById(R.id.appDescriptionTitle);
        TextView tvText = findViewById(R.id.appDescription);
        TextView buttonText = findViewById(R.id.button_start);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        // Creating a randomNumber which acts as an image ID
        int i = (int) (Math.random() * 1084);

        // Setting the ID to the global variable randomNumber
        setRandomNumber(i);

        Log.d(LOG_TAG, "Loading picture with ID: " + i);

        ImageRequest imageRequest = createRequest(i);

        requestQueue.add(imageRequest);

        // Clearing the text displayed on the home screen so it doesn't overlap with the image being displayed
        tvText.setText("");
        tvTextTitle.setText("");
        buttonText.setText("►");
    }


    private ImageRequest createRequest(final int id) {

        // ImageRequest(String url, Response.Listener<Bitmap> listener,
        // int maxWidth, int maxHeight, Bitmap.Config decodeConfig,
        // Response.ErrorListener errorListener)

        final ImageRequest imageRequest = new ImageRequest
                (IMAGE_URL + id + "/750/1060", new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap response) {
                        ImageView imageView = findViewById(R.id.imageView_image);
                        imageView.setImageBitmap(response);

                    }
                }, 0, 0, Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(LOG_TAG, "error executing request");
                            }
                        });
        return imageRequest;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent i;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_album) {
            i = new Intent(this, AlbumActivity.class);
            i.putStringArrayListExtra("links", linksArray);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {

        // On resume checks if nightmode is enabled or disabled and acts accordingly
        // I know this is a bit sloppy, I tried multiple things to clean this up
        // but none were succesfull.. I'm sorry :/
        if (SettingsActivity.nightMode(this)) {
            Log.d(LOG_TAG, "nightmode enabled");

            setTheme(R.style.DarkTheme);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        super.onResume();

        if (!SettingsActivity.nightMode(this)) {
            Log.d(LOG_TAG, "daymode enabled");

            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        super.onResume();

        // On resume checks if dutchmode is enabled or disabled and acts accordingly
        if (SettingsActivity.language(this)) {
            Log.d(LOG_TAG, "dutchmode enabled");

            Locale locale = new Locale("nl");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        super.onResume();

        if (!SettingsActivity.language(this)) {
            Log.d(LOG_TAG, "englishmode enabled");

            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        super.onResume();
    }
}

