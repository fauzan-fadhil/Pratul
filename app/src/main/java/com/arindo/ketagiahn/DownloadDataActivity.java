package com.arindo.ketagiahn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arindo.ketagiahn.constanta.DBHelper;
import com.arindo.ketagiahn.constanta.GPSTracker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DownloadDataActivity extends AppCompatActivity {

    private int Value = 0;
    public List<ItemKoked> itemKokeds;
    private ListViewAdapter adapter;
    JSONObject playerObject;
    ItemKoked itemKoked;
    JSONArray playerArray;
    ListView listVie;
    JSONObject obj;
    private TextView txtkoked, persentase, txtunitup;
    private Button btnlihatdata, btndownload;
    DBHelper SQLite = new DBHelper(this);
    private String bulan, unitup, idpel, nama, alamat, tarif, daya, koked, langkah, lembar, tagihan, spinerket, latitud, langitud;
    private String keterangan1, keterangan2, keterangan3, rencanabayar, notelepon, status, id, tanggal_survey, pbm, latitudstr, langitudstr;
    private SharedPreferences preferences;
    private Spinner spinner;
    private String Item;
    private GPSTracker gpsTracker;
    double latitude, longitude;
    int indexmenu;
    private ProgressBar loadingbar, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        //getLocation();
        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        preferences.getString("unitpref", null);
        //loadingline
        progressBar = findViewById(R.id.progress);
        //loadingpie
        loadingbar = findViewById(R.id.progressBar);
        persentase = findViewById(R.id.persentase);
        progressBar.setVisibility(View.GONE);
        persentase.setVisibility(View.GONE);
        loadingbar.setVisibility(View.GONE);
        listVie = findViewById(R.id.listview);
        //txtkoked = findViewById(R.id.txtkoked);
        txtunitup = findViewById(R.id.unitupedt);
        btnlihatdata = findViewById(R.id.btnlihatdata);
        btndownload = findViewById(R.id.btndownload);
        //txtkoked.setText(preferences.getString("kokedpref", null));
        txtunitup.setText( preferences.getString("unitpref", null));
        spinner = findViewById(R.id.spinerkoked);
        Item = preferences.getString("kokedpref", null);

        id = "";
        spinerket = "";
        latitud = "";
        langitud = "";
        keterangan1 = "";
        keterangan2 = "";
        keterangan3 = "";
        rencanabayar = "";
        notelepon = "";
        status = "00";
        tanggal_survey = "";
        btndownload.setEnabled(false);
        itemKokeds = new ArrayList<>();
        indexmenu = 0;

        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadDataActivity.this.finish();
            }
        });

        getSupportActionBar().setTitle("Download Tagihan");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexmenu = position;
               // getSpiner = spinner.getSelectedItem().toString().trim();
                itemKokeds.clear();
              //  Log.e("getSpiner", getSpiner);
                //getAllData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnlihatdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem().toString().equals("")|| txtunitup.getText().toString().equals("")){
                Toast.makeText(DownloadDataActivity.this, "isi koked dan unitup untuk melihat data", Toast.LENGTH_LONG).show();
                } else {
                    loadingbar.setVisibility(View.VISIBLE);
                    getdata();
                }
            }
        });

        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SQLite.cekidpel(idpel) > 0) {
                    showdialog();
                } else {
                    loading();
                }
            }
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
      getresponse();
    }

    private void getresponse() {
        try {
           JSONObject objspiner = new JSONObject(Item);
           JSONArray arrayspiner = objspiner.getJSONArray("data");
            String[] list  = new String[arrayspiner.length()];
            for (int i = 0; i < arrayspiner.length(); i++) {
               JSONObject ObjectSpiner = arrayspiner.getJSONObject(i);
                list[i]=ObjectSpiner.getString("koked");
                Log.e("response", String.valueOf(Item));
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,list);
            spinner.setAdapter(adapter);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void showdialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Download Tagihan ");
        alertDialogBuilder
                .setMessage("Data tagihan sudah pernah di download\nreset untuk download ulang")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getdata(){
        String url_json = "https://bilman.arindo.net/index.php?r=bilman/allbykoked";
        final RequestQueue requestQueue = Volley.newRequestQueue(DownloadDataActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_json,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    obj = new JSONObject(response);
                    playerArray = obj.getJSONArray("data");
                    for (int i = 0; i < playerArray.length(); i++) {
                        playerObject = playerArray.getJSONObject(i);
                        bulan = playerObject.getString("bulan");
                        unitup = playerObject.getString("unitup");
                        idpel = playerObject.getString("idpel");
                        nama = playerObject.getString("nama");
                        alamat = playerObject.getString("alamat");
                        tarif = playerObject.getString("tarif");
                        daya = playerObject.getString("daya");
                        koked = playerObject.getString("koked");
                        langkah = playerObject.getString("langkah");
                        lembar = playerObject.getString("lembar");
                        tagihan = playerObject.getString("tagihan");
                        pbm = playerObject.getString("pbm");
                        itemKoked = new ItemKoked(
                                playerObject.getString("bulan"),
                                playerObject.getString("unitup"),
                                playerObject.getString("idpel"),
                                playerObject.getString("nama"),
                                playerObject.getString("alamat"),
                                playerObject.getString("tarif"),
                                playerObject.getString("daya"),
                                playerObject.getString("koked"),
                                playerObject.getString("langkah"),
                                playerObject.getString("lembar"),
                                playerObject.getString("tagihan"),
                                playerObject.getString("biller"),
                                playerObject.getString("pbm"));
                        itemKokeds.add(itemKoked);
                    }
                    adapter = new ListViewAdapter(itemKokeds, getApplicationContext());
                    listVie.setAdapter(adapter);
                    btndownload.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Json", String.valueOf(playerArray));
                loadingbar.setVisibility(View.GONE);
                requestQueue.stop();
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(DownloadDataActivity.this,
                            "Oops. request ke server gagal coba lagi",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    lostkoneksi();
                }
                Toast.makeText(DownloadDataActivity.this, error.toString(), Toast.LENGTH_LONG);
                Log.e("pos trx Error", error.toString());
                progressBar.setVisibility(View.GONE);
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("koked", spinner.getSelectedItem().toString().trim());
                params.put("unitup", txtunitup.getText().toString());
                // Log.e("Json", params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers =new HashMap<String, String>();
                headers.put("Authorization", "Basic YnVkaTpsaW1h");
                String credentials = "webapi"+":"+"Bi1lM3N@rind0";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void lostkoneksi() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Coba lagi");
        alertDialogBuilder
                .setMessage("Periksan internet anda dan coba lagi")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void save() {
        try {
            for (int i = 0; i < playerArray.length(); i++) {
                playerArray = obj.getJSONArray("data");
                playerObject = playerArray.getJSONObject(i);
               String langkahh = playerObject.getString("langkah");
                SQLite.insert( bulan = playerObject.getString("bulan"),
                unitup = playerObject.getString("unitup"),
                idpel = playerObject.getString("idpel"),
                nama = playerObject.getString("nama"),
                alamat = playerObject.getString("alamat"),
                tarif = playerObject.getString("tarif"),
                daya = playerObject.getString("daya"),
                koked = playerObject.getString("koked"),
                Integer.parseInt(langkahh),
                lembar = playerObject.getString("lembar"),
                tagihan = playerObject.getString("tagihan"),
                        spinerket, latitud, langitud, keterangan1,
                        keterangan2, keterangan3, rencanabayar, notelepon, status, tanggal_survey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        persentase.setVisibility(View.VISIBLE);
        listVie.setVisibility(View.GONE);
        btndownload.setEnabled(false);
        progressBar.setProgress(0); //Set Progress Dimulai Dari O
        // Handler untuk Updating data pada latar belakang
        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Menampung semua data yang ingin diproses oleh thread
                persentase.setText(Value +"%");
                if(Value == progressBar.getMax()){
                    Toast.makeText(getApplicationContext(), "Progress Completed", Toast.LENGTH_SHORT).show();
                    if (id.equals("")) {
                        try {
                            save();
                            berhasil();
                        } catch (Exception e){
                            Log.e("Submit", e.toString());
                        }
                    }
                    listVie.setVisibility(View.VISIBLE);
                    btndownload.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    persentase.setVisibility(View.GONE);
                    //startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    //finish();
                }
                Value++;
            }
        };

        // Thread untuk updating progress pada ProgressBar di Latar Belakang
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int w=0; w<=progressBar.getMax(); w++){
                        progressBar.setProgress(w); // Memasukan Value pada ProgressBar
                        // Mengirim pesan dari handler, untuk diproses didalam thread
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(100); // Waktu Pending 100ms/0.1 detik
                    }
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void berhasil() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Download Data ");
        alertDialogBuilder
                .setMessage("Data berhasil di download")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DownloadDataActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void getLocation(){
        gpsTracker = new GPSTracker(DownloadDataActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("lat", String.valueOf(latitude));
            Log.e("lang", String.valueOf(longitude));
            latitudstr = String.valueOf(latitude).toString();
            langitudstr = String.valueOf(longitude).toString();
            if (latitudstr.length() == 2 && langitudstr.length() == 2) {
                Toast.makeText(DownloadDataActivity.this, "Terjadi kesalahan dalam memasukan lokasi anda. Coba lagi!", Toast.LENGTH_LONG).show();
            } else {
                Geocoder geocoder = new Geocoder(DownloadDataActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 0);
//                    Address obj = (Address) addresses.get(0);
                    //String currentCity= obj.getSubAdminArea();
                    // String currentState= obj.getAdminArea();
                   // titikkordinattxt.setText(latitude + "" + latitude); /*"Desa "+obj.getSubLocality() +" "+obj.getLocality()+" " +currentCity+" Provinsi "+ currentState + " "+ obj.getPostalCode() + " " +obj.getCountryName());*/
                    //txttitikkordinat.setText(" "+ obj.getAddressLine(0));/*"Desa "+obj.getSubLocality() +" "+obj.getLocality()+" " +currentCity+" Provinsi "+ currentState + " "+ obj.getPostalCode() + " " +obj.getCountryName());*/


                    //etKot.setText(" "+currentCity + " "+currentState);
                    // etAlmt.setText(" Desa " +obj.getSubLocality()+" Kecamatan "+obj.getLocality());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            // etlokasi.setText(latitude + " " + longitude);
            // tvLatit.setText(String.valueOf(latitude));
            // tvLangit.setText(String.valueOf(longitude));
            // etKot.setText(String.formatted_adres);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

}