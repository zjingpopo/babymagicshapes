package com.hijames.babymagicshapes;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.content.Context;

public class AdsActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        if (wifiAvailable()) {
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        } else {
            startMainActivity();
        }
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                startMainActivity();
            }

            @Override
            public void onAdClosed() {
                //Start Main Activity.
                startMainActivity();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise switch to MainActivity.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startMainActivity();
        }
    }

    private void loadInterstitial() {
        // Load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void startMainActivity() {
        Intent MainIntent = new Intent(this, MainActivity.class);
        this.startActivity(MainIntent);
        this.finish();
    }

    private boolean wifiAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
