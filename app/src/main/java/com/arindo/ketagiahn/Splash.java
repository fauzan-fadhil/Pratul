package com.arindo.ketagiahn;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity  {
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 112 ;//123;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static int SPLASH_TIME_OUT = 2000;
    String[] PERMISSIONS;
    Activity contect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //requestAppPermissions();
        contect = this;
        //   ShowActivity();
        PERMISSIONS = new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE
        };
        CekPermission();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void CekPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkPermission();
        } else {
            ShowActivity();
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) +
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.ACCESS_NETWORK_STATE)){


                SnackBarMsg("JSR Inspection require some permissions, please enabled.");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        }else{
            ShowActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean permission0 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean permission3 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean permission4 = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (permission0 && permission1 && permission2 && permission3 && permission4) {
                        ShowActivity();
                    } else {
                        ShowActivity();
                        //    SnackBarMsg("JSR Inspection require some permissions, please enabled.");
                    }
                } else {
                    SnackBarMsg("JSR Inspection require some permissions, please enabled.");
                }
                break;
        }
    }

    private void ShowActivity(){
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if(isNetworkAvailable()== true) {
                    //  CheckNewAppVersion cekapp = new CheckNewAppVersion();
                    // cekapp.CheckVersion(contect,0);
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(contect,"Please turn on your internet connection device !",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void SnackBarMsg(String msg){
        final Snackbar snackBar = Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackBar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackBar.setAction("Enable", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requestPermissions(PERMISSIONS, PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        });
        snackBar.show();
    }
}
