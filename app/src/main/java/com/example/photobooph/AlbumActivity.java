package com.example.photobooph;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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


public class AlbumActivity extends AppCompatActivity {

    // Array that saves images that are saved by the user
    ArrayList<String> linksArray;

    ListView listview;

    // When set to 0, clicking the first image always results in the first image of the list view to
    // be active which makes the image not show when clicked on create. :/
    int index = 1087;

    private static final String TAG = "Album: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        // Get the intent from mainActivity, put array data into list items, setting on click listeners
        // for each list item
        Intent intent = getIntent();
        linksArray = intent.getStringArrayListExtra("links");
        listview = findViewById(R.id.listView_Album);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, linksArray);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == index) {
                    Log.d(TAG, "onItemClick: deze wordt al uitgevoerd");
                    ImageView imageView = findViewById(R.id.imageView_detail);
                    // If list item with active ID is clicked it clears the image view and returns..
                    imageView.setImageBitmap(null);
                    index = 1087;
                    return;
                }
                index = i;

                // ..else the image is fetched from the internet
                RequestQueue requestQueue;

                // Instantiate the cache
                Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

                // Set up the network to use HttpURLConnection as the HTTP client.
                Network network = new BasicNetwork(new HurlStack());

                // Instantiate the RequestQueue with the cache and network.
                requestQueue = new RequestQueue(cache, network);

                // Start the queue
                requestQueue.start();

                ImageRequest imageRequest = createRequest(i);

                requestQueue.add(imageRequest);
            }
        });
    }

    // Standard generated code by Android Studio
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Standard generated code by Android Studio
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
        if (id == R.id.action_home) {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        if (SettingsActivity.nightMode(this)) {
            Log.d(TAG, "nightmode enabled");

            setTheme(R.style.DarkTheme);
        }
        super.onResume();

        if (!SettingsActivity.nightMode(this)) {
            Log.d(TAG, "daymode enabled");

            setTheme(R.style.AppTheme);
        }
        super.onResume();
    }

    private ImageRequest createRequest(int i) {

        // ImageRequest(String url, Response.Listener<Bitmap> listener,
        // int maxWidth, int maxHeight, Bitmap.Config decodeConfig,
        // Response.ErrorListener errorListener)

        final ImageRequest imageRequest = new ImageRequest
                ( linksArray.get(i) + "/300/300", new Response.Listener<Bitmap>() {


                    @Override
                    public void onResponse(Bitmap response) {
                        ImageView imageView = findViewById(R.id.imageView_detail);
                        imageView.setImageBitmap(response);

                    }
                }, 0, 0, Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "error executing request");
                            }
                        });
        return imageRequest;
    }
}
