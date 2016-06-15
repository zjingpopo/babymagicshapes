package com.hijames.babymagicshapes;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;


public class Eula {
    private AppCompatActivity mEntryActivty;

    public Eula(AppCompatActivity context) {
        mEntryActivty = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mEntryActivty.getPackageManager().getPackageInfo(mEntryActivty.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        String EULA_prefix = "eula_";
        PackageInfo versionInfo = getPackageInfo();
        final String eulaKey = EULA_prefix + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mEntryActivty);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);

        if (!hasBeenShown) {
            String title = mEntryActivty.getString(R.string.app_name) + " v" + versionInfo.versionName;
            String message = mEntryActivty.getString(R.string.updates) + mEntryActivty.getString(R.string.eula);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mEntryActivty)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mEntryActivty.finish();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.apply();
                            dialogInterface.dismiss();
                            startAdsActivity();
                        }
                    })
                    .setOnKeyListener(new OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialoginterface, int keyCode, KeyEvent event) {
                            return !(keyCode == KeyEvent.KEYCODE_HOME);
                        }
                    });
            mBuilder.create().show();
        } else {
            startAdsActivity();
        }
    }

    private void startAdsActivity() {
        Intent AdsIntent = new Intent(mEntryActivty, AdsActivity.class);
        mEntryActivty.startActivity(AdsIntent);
        mEntryActivty.finish();
    }
}
