package com.arindo.ketagiahn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arindo.ketagiahn.constanta.DBHelper;

public class TampilanActivityPelunasan extends AppCompatActivity {


    Locale localeID;
    NumberFormat formatRupiah;
    String spinerketstr, keterangan1str, keterangan2str, keterangan3str, namastr, alamatstr, idstr;
    String rencanabayatstr, edtNoTelefonstr, latitud, langitud, tagihanstr, statusstr, lembarstr;
    String bulanstr, idpelstr, tarifstr, dayastr, kokedstr, langkahstr, tglsurveystr, unitupstr;
    Spinner spinerketspr;
    CheckedTextView checkketsatu, checkketdua, checkkettiga;
    TextView rencanabayar, txttitikkordinat, txttagihan, txtbulan, txtidpel;
    TextView txttarif, txtdaya, txtkoked, txtlangkah, tanggalsurvey, txtnama, txtalamat;
    EditText edtNoTelefon;
    ImageView imgcopy, getgambar;
    Button btnsimpan;
    DBHelper SQLite = new DBHelper(this);
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_pelunasan);
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        SQLite = new DBHelper(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TampilanActivityPelunasan.this, ActivityRepayment.class));
                TampilanActivityPelunasan.this.finish();
            }
        });

        getSupportActionBar().setTitle("Tagihan belum lunas");

        txttarif = findViewById(R.id.txttarif);
        txtdaya = findViewById(R.id.txtdaya);
        txtkoked = findViewById(R.id.txtkoked);
        txtidpel = findViewById(R.id.txtidpel);
        txtbulan = findViewById(R.id.txtbulan);
        rencanabayar = findViewById(R.id.rencanabayar);
        edtNoTelefon = findViewById(R.id.edtNoTelefon);
        txttitikkordinat = findViewById(R.id.txttitikkordinat);
        txttagihan = findViewById(R.id.txtnominal);
        txtlangkah = findViewById(R.id.txtlangkah);
        tanggalsurvey = findViewById(R.id.tanggalsurvey);
        getgambar = findViewById(R.id.getgambar);

        spinerketspr = findViewById(R.id.spiner);
        btnsimpan = findViewById(R.id.btnsimpan);

        checkketsatu = (CheckedTextView) findViewById(R.id.checkketsatu);
        checkketdua = (CheckedTextView) findViewById(R.id.checkketdua);
        checkkettiga = (CheckedTextView) findViewById(R.id.checkkettiga);
        txtnama = findViewById(R.id.namatxt);
        txtalamat = findViewById(R.id.alamattxt);
        idstr = getIntent().getStringExtra(TAG_ID);
        spinerketstr = getIntent().getStringExtra(TAG_SPINERKET);
        keterangan1str = getIntent().getStringExtra(TAG_KETERANGAN1);
        keterangan2str = getIntent().getStringExtra(TAG_KETERANGAN2);
        keterangan3str = getIntent().getStringExtra(TAG_KETERANGAN3);
        rencanabayatstr = getIntent().getStringExtra(TAG_RENCANABAYAR);
        edtNoTelefonstr = getIntent().getStringExtra(TAG_NOTELEPON);
        latitud = getIntent().getStringExtra(TAG_LATITUD);
        langitud = getIntent().getStringExtra(TAG_LANGITUD);
        tagihanstr = getIntent().getStringExtra(TAG_TAGIHAN);
        bulanstr = getIntent().getStringExtra(TAG_BULAN);
        idpelstr = getIntent().getStringExtra(TAG_IDPEL);
        tarifstr = getIntent().getStringExtra(TAG_TARIF);
        dayastr = getIntent().getStringExtra(TAG_DAYA);
        kokedstr = getIntent().getStringExtra(TAG_KOKED);
        langkahstr = getIntent().getStringExtra(TAG_LANGKAH);
        lembarstr = getIntent().getStringExtra(TAG_LEMBAR);
        tglsurveystr = getIntent().getStringExtra(TAG_TANGGALSURVEY);
        namastr = getIntent().getStringExtra(TAG_NAMA);
        alamatstr = getIntent().getStringExtra(TAG_ALAMAT);
        unitupstr = getIntent().getStringExtra(TAG_UNITUP);
        statusstr = getIntent().getStringExtra(TAG_STATUS);
        Log.e("status", statusstr);

        switch (spinerketstr) {
            case "L":
                spinerketstr = "LUNAS DI TEMPAT";
                break;
            case "J":
                spinerketstr = "JANJI BAYAR";
                break;
            case "K":
                spinerketstr = "KOLEKTIF";
                break;
            case "R":
                spinerketstr = "RUMAH KOSONG";
                break;
        }
        final List<String> plantsList = new ArrayList<>(Arrays.asList(spinerketstr, "LUNAS DI TEMPAT", "KOLEKTIF" ));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        spinerketspr.setAdapter(spinnerArrayAdapter);
        spinerketspr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                int j = 0;
                if (spinerketspr.getSelectedItem().toString().equals("KOLEKTIF")) {
                    spinerketstr = "K";
                    //  Toast.makeText(getApplicationContext(), spinerketstr, Toast.LENGTH_SHORT).show();
                } else if (spinerketspr.getSelectedItem().toString().equals("JANJI BAYAR")){
                    spinerketstr = "J";
                    // Toast.makeText(getApplicationContext(), spinerketstr, Toast.LENGTH_SHORT).show();
                } else if (spinerketspr.getSelectedItem().toString().equals("LUNAS DI TEMPAT")) {
                    spinerketstr = "L";
                    // Toast.makeText(getApplicationContext(), spinerketstr, Toast.LENGTH_SHORT).show();
                } else if (spinerketspr.getSelectedItem().toString().equals("RUMAH KOSONG")) {
                    spinerketstr = "R";
                }
            }
            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });
        checkketsatu.setText("Informasi tagihan Sebesar " + formatRupiah.format(Integer.parseInt(tagihanstr)) + " Sudah Disampaikan");
        try {
            if (keterangan1str.equals("Y")) {
                checkketsatu.isChecked();
                checkketsatu.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                checkketsatu.setChecked(true);
            } else if (keterangan1str.equals("T")) {
                checkketsatu.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                checkketsatu.setChecked(false);
            }

            if (keterangan2str.equals("Y")) {
                checkketdua.isChecked();
                checkketdua.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                checkketdua.setChecked(true);
            } else if (keterangan2str.equals("T")) {
                checkketdua.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                checkketdua.setChecked(false);
            }

            if (keterangan3str.equals("Y")) {
                checkkettiga.isChecked();
                checkkettiga.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                checkkettiga.setChecked(true);
            } else if (keterangan3str.equals("T")) {
                checkkettiga.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                checkkettiga.setChecked(false);
            }
        } catch (Exception e) {
            Log.e("error", String.valueOf(e));
        }

        formatbulan();
        txtidpel.setText(idpelstr);
        txtnama.setText(namastr);
        txtalamat.setText(alamatstr);
        txttarif.setText(tarifstr);
        txtdaya.setText(dayastr);
        txtkoked.setText(kokedstr);
        txtlangkah.setText(langkahstr);
        tanggalsurvey.setText(tglsurveystr);
        txttagihan.setText(formatRupiah.format(Integer.parseInt(tagihanstr)));
        txttitikkordinat.setText(latitud + langitud);
        rencanabayar.setText(rencanabayatstr);
        edtNoTelefon.setText(edtNoTelefonstr);
        edtNoTelefon.setFocusable(false);

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinerketspr.getSelectedItem().equals("-")){
                    Toast.makeText(TampilanActivityPelunasan.this, "isi keterangan terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (!checkketsatu.isChecked()) {
                    Toast.makeText(TampilanActivityPelunasan.this, "informasi tagihan belum di ceklis", Toast.LENGTH_LONG).show();
                } else if (!checkketdua.isChecked()) {
                    Toast.makeText(TampilanActivityPelunasan.this, "informasi pembayaran belum di ceklis", Toast.LENGTH_LONG).show();
                } else if (!checkkettiga.isChecked()){
                    Toast.makeText(TampilanActivityPelunasan.this, "informasi konsekuensi belum di ceklis", Toast.LENGTH_LONG).show();
                }  else {
                    //updatedata();
                    proses();
                    //saveToGallery();

                }
            }
        });
    }

    private void updatedata() {
        String url_json = "http://bilman.arindo.net/index.php?r=bilman/updatetgk";
        final RequestQueue requestQueue = Volley.newRequestQueue(TampilanActivityPelunasan.this);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url_json,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        proses();
                        Log.e("response", response);
                        requestQueue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lostkoneksi();
                error.printStackTrace();
                Log.e("pos trx Error", error.toString());
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("unitup", unitupstr);
                params.put("bulan", bulanstr);
                params.put("idpelanggan", txtidpel.getText().toString());
                params.put("tgl_bayar", rencanabayatstr);
                params.put("tgl_survey", tanggalsurvey.getText().toString());
                params.put("latitude", String.valueOf(latitud));
                params.put("longitude", String.valueOf(langitud));
                params.put("ket_bayar", spinerketstr);
                params.put("sta_infotag", keterangan1str);
                params.put("sta_infotempo", keterangan2str);
                params.put("sta_infodenda", keterangan3str);
                params.put("no_telp", edtNoTelefon.getText().toString());
                Log.e("jsonparam", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-HTTP-Method-Override", "PATCH");
                headers.put("Authorization", "Basic YnVkaTpsaW1h");
                String credentials = "webapi" + ":" + "Bi1lM3N@rind0";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
        // requestQueue.getInstance().getRequestQueue().add(stringRequest);
    }

    private void proses() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Pemberitahuan");
        alertDialogBuilder
                .setMessage(" Penyimpanan ke server berhasil")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editonline();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void lostkoneksi() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Internet Terputus");
        alertDialogBuilder
                .setMessage("Sambungkan ke internet untuk simpan keserver")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void editonline() {
        //02 sudah dikunjungi sudah dikirim
        statusstr = "02";
        SQLite.update(Integer.parseInt(idstr),
                bulanstr,
                unitupstr, txtidpel.getText().toString().trim(),
                namastr, alamatstr,
                txttarif.getText().toString().trim(),
                txtdaya.getText().toString().trim(),
                txtkoked.getText().toString().trim(),
                Integer.parseInt(langkahstr),
                lembarstr, tagihanstr, spinerketstr, String.valueOf(langitud), String.valueOf(langitud), keterangan1str,
                keterangan2str, keterangan3str, rencanabayatstr,
                edtNoTelefon.getText().toString().trim(), statusstr, tanggalsurvey.getText().toString().trim()
        );
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TampilanActivityPelunasan.this, ActivityRepayment.class));
        TampilanActivityPelunasan.this.finish();
    }

    public void formatbulan() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM" + " " + "yyyy");
            DateFormat df = new SimpleDateFormat("yyyyMM");
            String tglBaru = dateFormat.format(df.parse(bulanstr));
            txtbulan.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}