package com.muhammed.iqbal.telugu.geeks;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.muhammed.iqbal.telugu.R;
import com.muhammed.iqbal.telugu.geeks.db.VideoViewModel;
import com.muhammed.iqbal.telugu.geeks.model.PlaylistVideos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class YouTubeActivity extends AppCompatActivity {
    private static final String[] YOUTUBE_PLAYLISTS = {
            // vivek bindra one play  list
            "PL1b6gKtBSZLhfY7Xx2DLFKW5HPB0ctD7y", //biographies and history
            "PL1b6gKtBSZLgHGdtyLTLcRMqsTUDQCNPl", //business and investment
            "PL1b6gKtBSZLjqkkCRTjLjJemZb5cmU_6-", // marketing and sales
            "PL1b6gKtBSZLgxS9H8uOCsM9BsElCc2gn5", // personal development and self improvement
            "PL1b6gKtBSZLjMWqmDOPDZaTMo4RtRztju", // memory and study techniques in telugu
            "PL1b6gKtBSZLhDroynBjJL2vMg1zTJe_Xh", // productivity and time management
            "PL1b6gKtBSZLgWHi2-Xrfk_LWKbnEqE8P2", //habits for success
            "PL1b6gKtBSZLjjQgrf1m7GKc8QG7XPhGR8", // morning motivation
            "PL1b6gKtBSZLg-eZEr19OzTIm5PFvVJUjI", // TOP SELF HELP BOOKS EXPLAINED IN TELUGU
            "PL1b6gKtBSZLhejPzrg-LWCUHDAqmGeKKm", // success qualities of highly successful people
            "PL1b6gKtBSZLgeF1A1WytYTpCXfx3j81rQ", //  communication skills
            "PL1b6gKtBSZLg73UJIBcfcZm44MrPYey3i", // how to think series
            "PL1b6gKtBSZLjMcJQB0oPXPjokIm2r-_y-", // life hacks and tips
            "PL1b6gKtBSZLgx1vx_K4l7QGnlTodUWvOE", // law of attraction and subconscious mind videos in telugu
    };
    public static final int[] NAV_ITEM_IDS = {R.id.biographies_and_history, R.id.business_and_investment, R.id.marketing_and_sales,
            R.id.personal_development_and_self_improvement, R.id.memory_and_study_techniques_in_telugu, R.id.productivity_and_time_management,
            R.id.habits_for_success, R.id.morning_motivation, R.id.TOP_SELF_HELP_BOOKS_EXPLAINED_IN_TELUGU,
            R.id.success_qualities_of_highly_successful_people, R.id.communication_skills, R.id.how_to_think_series,
            R.id.life_hacks_and_tips, R.id.law_of_attraction_and_subconscious_mind_videos_in_telugu};

    Map<Integer,String> navItemToPlayListIdMap = new HashMap<Integer, String>();

    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();
    //private AdView mAdView;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private YouTubeRecyclerViewFragment youTubeRecyclerViewFragment;
    public static String FACEBOOK_URL = "https://www.facebook.com/telugugeeks";
    public static String FACEBOOK_PAGE_ID = "telugugeeks";

    private VideoViewModel mVideoViewModel;

    private AdView mAdView;

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // vivke bindra viedeos ca-app-pub-7893979407683638~7527829360
        MobileAds.initialize(this, AdmobKey.testAppId);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.i("device id=",deviceId);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        mVideoViewModel.getAllVideos().observe(this, new Observer<List<com.muhammed.iqbal.telugu.geeks.model.Video>>() {
            @Override
            public void onChanged(@Nullable final List<com.muhammed.iqbal.telugu.geeks.model.Video> videos) {
                // Update the cached copy of the words in the adapter.
                //adapter.setWords(words);
            }
        });

        // map navigation item to play list id
        for(int i =0; i < NAV_ITEM_IDS.length; i++) {
            navItemToPlayListIdMap.put(NAV_ITEM_IDS[i], YOUTUBE_PLAYLISTS[i]);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        dl = findViewById(R.id.youtube_activity);
        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch(item.getItemId()) {
                            case R.id.contact_us:
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                emailIntent.setData(Uri.parse("mailto:iqbalforall@gmail.com"));
                                YouTubeActivity.this.startActivity(emailIntent);
                                break;
                            case R.id.facebook:
                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                String facebookUrl = getFacebookPageURL(getApplicationContext());
                                facebookIntent.setData(Uri.parse(facebookUrl));
                                startActivity(facebookIntent);
                                break;
                            default:
                            // reload the UI with the playlist video list of the selected playlist
                            String playListId = navItemToPlayListIdMap.get(item.getItemId());
                            if (playListId != null) {
                                youTubeRecyclerViewFragment.mPlaylistVideos = new PlaylistVideos(playListId);
                                youTubeRecyclerViewFragment.reloadUi(youTubeRecyclerViewFragment.mPlaylistVideos, true);

                            } else {
                                Toast.makeText(YouTubeActivity.this, "Wrong navigation item clicked or need to implement yet", Toast.LENGTH_LONG).show();
                            }

                        }
                        dl.closeDrawers();
                        return true;
                    }
                }
        );
        if(!isConnected()){
            Toast.makeText(YouTubeActivity.this,"No Internet Connection Detected",Toast.LENGTH_LONG).show();
        }
        
        if (ApiKey.YOUTUBE_API_KEY.startsWith("YOUR_API_KEY")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                        .setTitle("Missing API Key")
                        .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (savedInstanceState == null) {
            mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
            youTubeRecyclerViewFragment = YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS, this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, youTubeRecyclerViewFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.muhammed.iqbal.telugu.R.menu.main_menu, menu);
        return true;
    }
    
    public boolean isConnected() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case  R.id.action_recyclerview :
                youTubeRecyclerViewFragment = YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS, this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, youTubeRecyclerViewFragment)
                        .commit();
                return true;
                //action_favorites
            case R.id.action_favorites:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
