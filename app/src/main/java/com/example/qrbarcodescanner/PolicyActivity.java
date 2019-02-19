package com.example.qrbarcodescanner;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class PolicyActivity extends AppCompatActivity {

    private WebView policy;
    /*private AdView mAdView;
    private AdRequest adBRequest;*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        //set title of toolbar
        getSupportActionBar().setTitle("Privacy Policy");

        //HomeAsUpEnabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //casting views
        policy = (WebView) findViewById(R.id.about_app);

        //code for ads
       /* mAdView = (AdView) findViewById(R.id.adView);
        adBRequest = new AdRequest.Builder()
                //.addTestDevice("495C9C6B1071EA763E181E31139637AB")
                .build();
        mAdView.loadAd(adBRequest);
*/
        //load url
        policy.getSettings().setJavaScriptEnabled(true);
        policy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        policy.setLongClickable(false);
        policy.setHapticFeedbackEnabled(false);
        policy.loadUrl("file:///android_asset/policy.html");
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}