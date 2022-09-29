package com.arindo.ketagiahn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arindo.ketagiahn.constanta.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResetActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private int Value = 0;
    Button resetsemua, resetbykoked, btnhapus;
    Spinner spinerkoked;
    DBHelper SQLite = new DBHelper(this);
    List<ItemSQL> itemList = new ArrayList<ItemSQL>();
    TextView persentase;
    LinearLayout linerkoked;
    int indexmenu;
    String getSpiner, Item, menu;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        indexmenu = 0;
        progressBar = findViewById(R.id.progress);
        persentase = findViewById(R.id.persentase);
        progressBar.setVisibility(View.GONE);
        persentase.setVisibility(View.GONE);
        resetsemua = findViewById(R.id.resetsemua);
        resetbykoked = findViewById(R.id.resetbykoked);
        btnhapus = findViewById(R.id.btnhapus);
        spinerkoked = findViewById(R.id.spinerkoked);
        linerkoked = findViewById(R.id.linerkoked);
        btnhapus.setVisibility(View.GONE);
        linerkoked.setVisibility(View.GONE);

        Item = preferences.getString("kokedpref", null);

        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetActivity.this.finish();
            }
        });

        getSupportActionBar().setTitle("Reset Data");

        resetsemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SQLite.ceksemuadata() > 0) {
                    pertanyaan();
                } else {
                    tidakadadata2();
                }
            }
        });

        resetbykoked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linerkoked.setVisibility(View.VISIBLE);
            }
        });

        spinerkoked.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexmenu = position;
                getSpiner = spinerkoked.getSelectedItem().toString().trim();
                btnhapus.setVisibility(View.VISIBLE);
                itemList.clear();
                Log.e("getSpiner", getSpiner);
                //getAllData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SQLite.cekkoked(getSpiner) > 0) {
                    pertanyaanbykoked();
                } else {
                    tidakadadata();
                }
            }
        });
        getresponse();
    }

    private void deletberhasil() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("Seluruhh data berhasil di reset")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLite.deleteall();
                        itemList.clear();
                        ResetActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deletbykoked() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("Data berdasarkan koked " + getSpiner +  " berhasil di reset")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLite.deletebykoked(getSpiner);
                        itemList.clear();
                        ResetActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void pertanyaandua() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("Data berdasarkan koked " + getSpiner +  " berhasil di reset")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLite.deletebykoked(getSpiner);
                        itemList.clear();
                        ResetActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void pertanyaan() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Hapus Data ");
        alertDialogBuilder
                .setMessage("Reset data akan menghapus seluruh data di aplikasi! lanjutkan. . . ")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loading();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void pertanyaanbykoked() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Hapus Data ");
        alertDialogBuilder
                .setMessage("Reset data akan menghapus seluruh data berdasarkan kokod " + getSpiner + "lanjutkan...")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        loadingbykoked();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void tidakadadata() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("Data bersadarkan koked " + getSpiner + " tidak ada" )
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void tidakadadata2() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("Data tidak ada" )
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        persentase.setVisibility(View.VISIBLE);

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
                    Toast.makeText(getApplicationContext(), "Reset Completed", Toast.LENGTH_SHORT).show();
                    try {
                        deletberhasil();
                    } catch (Exception e){
                        Log.e("Submit", e.toString()); }
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

    private void loadingbykoked() {
        progressBar.setVisibility(View.VISIBLE);
        persentase.setVisibility(View.VISIBLE);

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
                    Toast.makeText(getApplicationContext(), "Reset Completed", Toast.LENGTH_SHORT).show();
                    try {
                        deletbykoked();
                    } catch (Exception e){
                        Log.e("Submit", e.toString()); }
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
            spinerkoked.setAdapter(adapter);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

}