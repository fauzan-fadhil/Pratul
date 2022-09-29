package com.arindo.ketagiahn;

import static com.arindo.ketagiahn.KunjunganActivity.TAG_ALAMAT;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_BULAN;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_DAYA;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_ID;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_IDPEL;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN1;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN2;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN3;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KOKED;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LANGITUD;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LANGKAH;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LATITUD;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LEMBAR;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_NAMA;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_NOTELEPON;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_RENCANABAYAR;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_SPINERKET;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_STATUS;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TAGIHAN;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TANGGALSURVEY;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TARIF;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_UNITUP;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.arindo.ketagiahn.constanta.DBHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private ImageView Btn_Download, Btn_kunjungan, btn_reset, btn_pemutusan, btn_menulain, btn_repayment;
    private BottomSheetBehavior sheetBehavior;
    private BottomSheetDialog sheetDialog;
    private View bottom_sheet;
    DBHelper SQLite = new DBHelper(this);
    private String idsqlite, unitupsqlite, bulansqlite, idpelsqlit, namasqlite, alamatsqlite;
    private String tarifsqlite, dayasqlite, tagihansqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tolbarrr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pratul Bansel v2.10.0");
        SQLite = new DBHelper(getApplicationContext());
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        preferences.getLong("waktu", 0);
        long sekarang = System.currentTimeMillis();
        long time = sekarang - preferences.getLong("waktu", 0);
        Log.e("waktu1", String.valueOf(time));

        //43200000 = 12 jam
        if (time <= 43200000) {
        } else {
            preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.clear();
            editor.apply();
            ShowDialog();
            return;
        }

        //datagrafik();
        btn_menulain = findViewById(R.id.btn_menulain);
        btn_menulain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        btn_pemutusan = findViewById(R.id.btn_pemutusan);
        btn_pemutusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PemutusanActivity.class));
            }
        });

        Btn_Download = findViewById(R.id.Btn_Download);
        Btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DownloadDataActivity.class));
            }
        });

        Btn_kunjungan = findViewById(R.id.Btn_kunjungan);
        Btn_kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KunjunganActivity.class));
            }
        });

        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finish();
                    }
                }).create().show();
    }

    private void ShowDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Session Sudah Berakhir");
        alertDialogBuilder
                .setMessage("Login kembali untuk melanjutkan")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.sheet, null);

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        (view.findViewById(R.id.btn_internal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SimpanOfflineActivity.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.btn_storage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SimpanOnlineActivity.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.Btn_Download)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DownloadDataActivity.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.btn_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetActivity.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.Btn_kunjungan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KunjunganActivity.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.btn_laporan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityLaporan.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.btn_repayment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityRepayment.class));
                sheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
                sheetDialog.dismiss();
            }
        });

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.view_custom_dialog_dua);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnlogout = (Button) dialog.findViewById(R.id.btnlogout);
        Button gantipassword = (Button) dialog.findViewById(R.id.btngantipass);
        Button batalbtn = (Button) dialog.findViewById(R.id.batalbtn);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Logout");
                    builder1.setMessage("Anda Yakin akan keluar aplikasi?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Ya",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                    builder1.setNegativeButton(
                            "Batal",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        gantipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(MainActivity.this, GantiPasswordActivity.class));
                    dialog.dismiss();
                } catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        batalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void datagrafik() {
        ArrayList<HashMap<String, String>> row = SQLite.tampilsemua();
        for (int i = 0; i < row.size(); i++) {
            idsqlite = row.get(i).get(TAG_ID);
            unitupsqlite = row.get(i).get(TAG_UNITUP);
            bulansqlite = row.get(i).get(TAG_BULAN);
            idpelsqlit = row.get(i).get(TAG_IDPEL);
            namasqlite = row.get(i).get(TAG_NAMA);
            alamatsqlite = row.get(i).get(TAG_ALAMAT);
            tarifsqlite = row.get(i).get(TAG_TARIF);
            dayasqlite = row.get(i).get(TAG_DAYA);
            tagihansqlite = row.get(i).get(TAG_TAGIHAN);
            String koked = row.get(i).get(TAG_KOKED);
            String langkah = row.get(i).get(TAG_LANGKAH);
            String lembar = row.get(i).get(TAG_LEMBAR);
            String spinerket = row.get(i).get(TAG_SPINERKET);
            String langitud = row.get(i).get(TAG_LANGITUD);
            String latitud = row.get(i).get(TAG_LATITUD);
            String keterangan1 = row.get(i).get(TAG_KETERANGAN1);
            String keterangan2 = row.get(i).get(TAG_KETERANGAN2);
            String keterangan3 = row.get(i).get(TAG_KETERANGAN3);
            String rencanabayar = row.get(i).get(TAG_RENCANABAYAR);
            String notelepon = row.get(i).get(TAG_NOTELEPON);
            String status = row.get(i).get(TAG_STATUS);
            String tanggalsurvey = row.get(i).get(TAG_TANGGALSURVEY);
        }
    }
}


