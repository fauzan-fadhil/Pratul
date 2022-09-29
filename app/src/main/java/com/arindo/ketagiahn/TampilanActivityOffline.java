package com.arindo.ketagiahn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.arindo.ketagiahn.KunjunganActivity.TAG_ALAMAT;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_BULAN;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_DAYA;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_IDPEL;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN1;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN2;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KETERANGAN3;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_KOKED;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LANGITUD;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LANGKAH;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_LATITUD;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_NAMA;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_NOTELEPON;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_RENCANABAYAR;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_SPINERKET;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TAGIHAN;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TANGGALSURVEY;
import static com.arindo.ketagiahn.KunjunganActivity.TAG_TARIF;

import com.arindo.ketagiahn.constanta.DBHelper;

public class TampilanActivityOffline extends AppCompatActivity {

    Locale localeID;
    NumberFormat formatRupiah;
    String spinerketstr, keterangan1str, keterangan2str, keterangan3str, namastr, alamatstr;
    String rencanabayatstr, edtNoTelefonstr, latitud, langitud, tagihanstr;
    String bulanstr, idpelstr, tarifstr, dayastr, kokedstr, langkahstr, tglsurveystr;
    Spinner spinerketspr;
    CheckedTextView checkketsatu, checkketdua, checkkettiga;
    TextView rencanabayar, txttitikkordinat, txttagihan, txtbulan, txtidpel;
    TextView txttarif, txtdaya, txtkoked, txtlangkah, tanggalsurvey, txtnama, txtalamat;
    EditText edtNoTelefon;
    ImageView imgcopy, getgambar;
    DBHelper SQLite = new DBHelper(this);
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_offline);
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        SQLite = new DBHelper(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TampilanActivityOffline.this, SimpanOfflineActivity.class));
                TampilanActivityOffline.this.finish();
            }
        });

        getSupportActionBar().setTitle("Informasi Tagihan internal");

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
        checkketsatu = (CheckedTextView) findViewById(R.id.checkketsatu);
        checkketdua = (CheckedTextView) findViewById(R.id.checkketdua);
        checkkettiga = (CheckedTextView) findViewById(R.id.checkkettiga);
        txtnama = findViewById(R.id.namatxt);
        txtalamat = findViewById(R.id.alamattxt);
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
        tglsurveystr = getIntent().getStringExtra(TAG_TANGGALSURVEY);
        namastr = getIntent().getStringExtra(TAG_NAMA);
        alamatstr = getIntent().getStringExtra(TAG_ALAMAT);
        Log.e("tanggal survey offline", tglsurveystr);
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
        }

        final List<String> plantsList = new ArrayList<>(Arrays.asList(spinerketstr));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        spinerketspr.setAdapter(spinnerArrayAdapter);
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

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TampilanActivityOffline.this, SimpanOfflineActivity.class));
        TampilanActivityOffline.this.finish();
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