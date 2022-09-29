package com.arindo.ketagiahn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arindo.ketagiahn.async.AsyncBluetoothEscPosPrint;
import com.arindo.ketagiahn.async.AsyncEscPosPrint;
import com.arindo.ketagiahn.async.AsyncEscPosPrinter;
import com.arindo.ketagiahn.constanta.DBHelper;
import com.arindo.ketagiahn.constanta.GPSTracker;
import com.arindo.modulprint.connection.DeviceConnection;
import com.arindo.modulprint.connection.bluetooth.BluetoothConnection;
import com.arindo.modulprint.connection.bluetooth.BluetoothPrintersConnections;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TagihanBulananActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String[] Item = {" ","LUNAS DI TEMPAT","JANJI BAYAR","KOLEKTIF","RUMAH KOSONG"};
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_CAMERA_KWH = 002;
    private GPSTracker gpsTracker;
    private Calendar myCalendar;
    private Spinner spinerketspr;
    double latitude, longitude;
    private int mYear, mMonth, mDay, rupiah;
    private String keterangan1str, keterangan2str, keterangan3str, spinerketstr;
    private String idstr, idpelstr, tarifstr, dayastr, kokedstr, langkahstr, bulanstr, rencanabayarstr, namastr;
    private String tagihanstr, latitudstr, langitudstr, unitupstr, lembarstr, statusstr, noteleponstr, alamatstr, tanggalsurveystr;
    private ImageView imgcopy, OpenImage, setImage, setImagekwh;
    private TextView txtidpel, titikkordinattxt, rencanabayartxt, txtbulan, txttarif, txtdaya;
    private TextView txtkoked, txtlangkah, txttagihan, idtxt, tanggalsurvey, namatxt, alamattxt;
    private EditText edtNoTelefon;
    private Locale localeID;
    private NumberFormat formatRupiah;
    private CheckedTextView checkketsatu, checkketdua, checkkettiga;
    private Button btnsimpan, defaultprint;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final int RC_BLUETOOTH = 0;
    DBHelper SQLite = new DBHelper(this);
    Bitmap photo;
    int max_resolution_image = 2500;
    String[] PERMISSIONS;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 112;
    private Activity contect;
    BluetoothAdapter mBluetoothAdapter;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan_bulanan);
        PERMISSIONS = new String[]{
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,

        };
        contect = this;
        checkPermission();

        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(TagihanBulananActivity.this, KunjunganActivity.class));
                TagihanBulananActivity.this.finish();
            }
        });

        getSupportActionBar().setTitle("Informasi Tagihan");

        OpenImage = findViewById(R.id.btngambar);
       // imgcopy = findViewById(R.id.imgcopy);
        txtidpel = findViewById(R.id.txtidpel);
        titikkordinattxt = (TextView) findViewById(R.id.txttitikkordinat);
        txtbulan = (TextView) findViewById(R.id.txtbulan);
        rencanabayartxt = (TextView) findViewById(R.id.rencanabayar);
        txttarif = (TextView) findViewById(R.id.txttarif);
        txtdaya = (TextView) findViewById(R.id.txtdaya);
        txtkoked = (TextView) findViewById(R.id.txtkoked);
        txtlangkah = (TextView) findViewById(R.id.txtlangkah);
        txttagihan = (TextView) findViewById(R.id.txtnominal);
        idtxt = (TextView) findViewById(R.id.id);
        edtNoTelefon = (EditText) findViewById(R.id.edtNoTelefon);
        setImage = (ImageView) findViewById(R.id.getgambar);
        setImagekwh = (ImageView) findViewById(R.id.getgambarkwh);
        btnsimpan = (Button) findViewById(R.id.btnsimpan);
        defaultprint = (Button) findViewById(R.id.defaultprint);
        tanggalsurvey = findViewById(R.id.tanggalsurvey);
        namatxt = findViewById(R.id.namatxt);
        alamattxt = findViewById(R.id.alamattxt);

        checkketsatu = (CheckedTextView) findViewById(R.id.checkketsatu);
        checkketdua = (CheckedTextView) findViewById(R.id.checkketdua);
        checkkettiga = (CheckedTextView) findViewById(R.id.checkkettiga);

        spinerketspr = findViewById(R.id.spiner);

        myCalendar = Calendar.getInstance();

        idstr = getIntent().getStringExtra("id");
        bulanstr = getIntent().getStringExtra("bulan");
        unitupstr = getIntent().getStringExtra("unitup");
        idpelstr = getIntent().getStringExtra("idpel");
        namastr = getIntent().getStringExtra("nama");
        alamatstr = getIntent().getStringExtra("alamat");
        tarifstr = getIntent().getStringExtra("tarif");
        dayastr = getIntent().getStringExtra("daya");
        kokedstr = getIntent().getStringExtra("koked");
        langkahstr = getIntent().getStringExtra("langkah");
        lembarstr = getIntent().getStringExtra("lembar");
        tagihanstr = getIntent().getStringExtra("tagihan");
        spinerketstr = getIntent().getStringExtra("spinerket");
        latitudstr = getIntent().getStringExtra("latitud");
        langitudstr = getIntent().getStringExtra("langitud");
        keterangan1str = getIntent().getStringExtra("keterangansatu");
        keterangan2str = getIntent().getStringExtra("keterangandua");
        keterangan3str = getIntent().getStringExtra("keterangantiga");
        rencanabayarstr = getIntent().getStringExtra("rencanabayar");
        noteleponstr = getIntent().getStringExtra("notelepon");
        statusstr = getIntent().getStringExtra("status");
        tanggalsurveystr = getIntent().getStringExtra("tanggalsurvey");

        rupiah = Integer.parseInt(tagihanstr);

        setTitle("Edit Data");
        idtxt.setText(idstr);
        txtidpel .setText(idpelstr);
        namatxt.setText(namastr);
        alamattxt.setText(alamatstr);
        txttarif.setText(tarifstr);
        txtdaya .setText(dayastr);
        txtkoked.setText(kokedstr);
        txtlangkah .setText(langkahstr);
        formatbulan();
        tanggalsurveystr = getTanggal();
        tanggalsurvey.setText(tanggalsurveystr);
        Log.e("tanggal survey tagihan", tanggalsurveystr);
        //txtbulan.setText(bulanstr);
        txttagihan.setText(formatRupiah.format(Integer.parseInt(tagihanstr)));
        //titikkordinattxt.setText(latitudstr + " " + langitudstr);
        rencanabayartxt.setText(rencanabayarstr);
        edtNoTelefon.setText(noteleponstr);

        Calendar c = Calendar.getInstance();
        String str = c.getTime().toString();
        Log.i("Current time", str);

        checkketsatu.setText("Informasi tagihan Sebesar " + formatRupiah.format(rupiah) + " Sudah Disampaikan");

        checkketsatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkketsatu.isChecked()) {
                    checkketsatu.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                    checkketsatu.setChecked(false);
                    keterangan1str = "T";
                } else {
                    checkketsatu.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                    checkketsatu.setChecked(true);
                    keterangan1str = "Y";
                    getLocation();
                }
               // Toast.makeText(TagihanBulananActivity.this, keterangan1str, Toast.LENGTH_SHORT).show();
            }
        });

        checkketdua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkketdua.isChecked()) {
                    checkketdua.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                    checkketdua.setChecked(false);
                    keterangan2str = "T";
                } else {
                    checkketdua.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                    checkketdua.setChecked(true);
                    keterangan2str = "Y";
                }
               //Toast.makeText(TagihanBulananActivity.this, keterangan2str, Toast.LENGTH_SHORT).show();
            }
        });

        checkkettiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkkettiga.isChecked()) {
                    checkkettiga.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
                    checkkettiga.setChecked(false);
                    keterangan3str = "T";
                } else {
                    checkkettiga.setCheckMarkDrawable(R.drawable.ic_baseline_check_box_24);
                    checkkettiga.setChecked(true);
                    keterangan3str = "Y";
                }
               //Toast.makeText(TagihanBulananActivity.this, keterangan3str, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            if (keterangan1str.equals("Y")){
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

            if(keterangan3str.equals("Y")) {
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

        OpenImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                setRequestImage();
            }
        });
/*
        imgcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = txtidpel.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    clipboardManager.setText(string);
                }
                Toast.makeText(getApplicationContext(), "Pesan Disalin", Toast.LENGTH_SHORT).show();
            }
        });
 */

        rencanabayartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TagihanBulananActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (year < mYear) {
                                    Toast.makeText(TagihanBulananActivity.this, "masukan tanggal dengan benar", Toast.LENGTH_LONG).show();
                                } else if ((monthOfYear < mMonth) && (year == mYear)) {
                                    Toast.makeText(TagihanBulananActivity.this, "masukan tanggal dengan benar", Toast.LENGTH_LONG).show();
                                } else if ((dayOfMonth < mDay) && (monthOfYear == mMonth) && (year == mYear)) {
                                    Toast.makeText(TagihanBulananActivity.this, "masukan tanggal dengan benar", Toast.LENGTH_LONG).show();
                                } else {
                                    int bulan = monthOfYear + 1;
                                    rencanabayartxt.setText(dayOfMonth + "-" + bulan + "-" + year);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        final List<String> plantsList = new ArrayList<>(Arrays.asList(spinerketstr));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        spinerketspr.setAdapter(spinnerArrayAdapter);

        //Inisialiasi Array Adapter dengan memasukkan String Array
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,Item);

        //Memasukan Adapter pada Spinner
        spinerketspr.setAdapter(adapter);

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

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinerketspr.getSelectedItem().equals("-")){
                    Toast.makeText(TagihanBulananActivity.this, "isi keterangan terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (!checkketsatu.isChecked()) {
                    Toast.makeText(TagihanBulananActivity.this, "informasi tagihan belum di ceklis", Toast.LENGTH_LONG).show();
                } else if (!checkketdua.isChecked()) {
                    Toast.makeText(TagihanBulananActivity.this, "informasi pembayaran belum di ceklis", Toast.LENGTH_LONG).show();
                } else if (!checkkettiga.isChecked()){
                    Toast.makeText(TagihanBulananActivity.this, "informasi konsekuensi belum di ceklis", Toast.LENGTH_LONG).show();
                } else if (rencanabayartxt.getText().toString().equals("")){
                    Toast.makeText(TagihanBulananActivity.this, "isi rencana bayar terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (edtNoTelefon.getText().toString().equals("")){
                    Toast.makeText(TagihanBulananActivity.this, "isi no telefon terlebih dahulu", Toast.LENGTH_LONG).show();
                }  else if (titikkordinattxt.getText().toString().equals("")) {
                    Toast.makeText(TagihanBulananActivity.this, "Periksa kembali titik kordinat", Toast.LENGTH_LONG).show();
                } else if (setImage.getDrawable() == null) {
                    Toast.makeText(TagihanBulananActivity.this, "Lampiran gambar terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (setImagekwh.getDrawable() == null) {
                    Toast.makeText(TagihanBulananActivity.this, "Lampiran gambar KWH terlebih dahulu", Toast.LENGTH_LONG).show();
                } else {
                    showCustomDialog();
                    saveToGallery();
                    saveToGallerykwh();
                }
            }
        });

        defaultprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                browseBluetoothDevice();
               // Toast.makeText(TagihanBulananActivity.this, "print struk?", Toast.LENGTH_LONG).show();
            }
        });
        getLocation();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(TagihanBulananActivity.this, "Gagal Terhubung Ke Bluethooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission (this, android.Manifest.permission.BLUETOOTH) +
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale (this, android.Manifest.permission.BLUETOOTH) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                SnackBarMsg("JSR Inspection require some permissions, please enabled.");
            } else {

                requestPermissions(PERMISSIONS, PERMISSIONS_MULTIPLE_REQUEST);

            }
        }else{
            ShowActivity();
        }
    }

    private void SnackBarMsg(String msg){
        final Snackbar snackBar = Snackbar.make(this.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackBar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackBar.setAction("Enable", new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
                requestPermissions(PERMISSIONS, PERMISSIONS_MULTIPLE_REQUEST);
            }
        });
        snackBar.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
                    Toast.makeText(contect,"permission Access \uD83D\uDE01",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(contect,"Please turn on your internet connection device !",Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
      //startActivity(new Intent(TagihanBulananActivity.this, KunjunganActivity.class));
      TagihanBulananActivity.this.finish();
    }

    private String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void getLocation(){
        gpsTracker = new GPSTracker(TagihanBulananActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("lat", String.valueOf(latitude));
            Log.e("lang", String.valueOf(longitude));
             latitudstr = String.valueOf(latitude).toString();
             langitudstr = String.valueOf(longitude).toString();
            if (latitudstr.length() == 2 && langitudstr.length() == 2) {
                Toast.makeText(TagihanBulananActivity.this, "Terjadi kesalahan dalam memasukan lokasi anda. Coba lagi!", Toast.LENGTH_LONG).show();
            } else {
                titikkordinattxt.setText(latitude + "" + latitude);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void formatbulan() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM" +" "+"yyyy");
            DateFormat df=new SimpleDateFormat("yyyyMM");
            String tglBaru = dateFormat.format(df.parse(bulanstr));
            txtbulan.setText(tglBaru);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void browseBluetoothDevice() {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default printer";
            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(TagihanBulananActivity.this);
            alertDialog.setTitle("Bluetooth printer selection");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int index = i - 1;
                    if (index == -1) {
                        selectedDevice = null;
                    } else {
                        selectedDevice = bluetoothDevicesList[index];
                    }
                    Button button = (Button) findViewById(R.id.defaultprint);
                    button.setText(items[i]);
                }
            });

            android.app.AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();

        }
    }
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.view_custom_dialog);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button offline = (Button) dialog.findViewById(R.id.btnoffline);
        Button online = (Button) dialog.findViewById(R.id.btnonline);
        Button printstruk = (Button) dialog.findViewById(R.id.btnprint);

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editoffline();
                    //saveToGallery();
                    //saveToGallerykwh();
                    //startActivity(new Intent(TagihanBulananActivity.this, KunjunganActivity.class));
                    TagihanBulananActivity.this.finish();
                    dialog.dismiss();
                } catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //updatedata();
                    editonline();
                    //saveToGallery();
                    //saveToGallerykwh();
                    dialog.dismiss();
                } catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        printstruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBluetooth();
            }
        });
        dialog.show();
    }

    private void setRequestImage(){
        CharSequence[] item = {"Ambil gambar orang", "Ambil gambar KWH"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Tambah gambar")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Membuka Kamera Untuk Mengambil Gambar
                                EasyImage.openCamera(TagihanBulananActivity.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openCamera(TagihanBulananActivity.this, REQUEST_CODE_CAMERA_KWH);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){

        } else {
            Toast.makeText(TagihanBulananActivity.this, "Tidak Terhubung Ke Bluethooth", Toast.LENGTH_SHORT).show();
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Error pada Image
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Image
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Glide.with(TagihanBulananActivity.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(setImage);
                        break;

                    case REQUEST_CODE_CAMERA_KWH:
                        Glide.with(TagihanBulananActivity.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(setImagekwh);
                        break;

                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Batalkan penanganan, Anda mungkin ingin menghapus foto yang diambil jika dibatalkan
            }
        });
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            setImage.setImageBitmap(photo);
        } else if (requestCode == CAMERA_REQUEST_KWH && resultCode == Activity.RESULT_OK) {
            photokwh = (Bitmap) data.getExtras().get("data");
            setImagekwh.setImageBitmap(photokwh);
        }
    }
 */
    private void editoffline() {
        //01 sudah dikunjungi belum dikirim
        statusstr = "01";
        SQLite.update(Integer.parseInt(idstr),
                bulanstr,
                unitupstr, txtidpel.getText().toString().trim(),
                namastr, alamatstr,
                txttarif.getText().toString().trim(),
                txtdaya.getText().toString().trim(),
                txtkoked.getText().toString().trim(),
                Integer.parseInt(langkahstr),
                lembarstr, tagihanstr, spinerketstr, String.valueOf(latitude), String.valueOf(longitude), keterangan1str,
                keterangan2str, keterangan3str, rencanabayartxt.getText().toString().trim(),
                edtNoTelefon.getText().toString().trim(), statusstr, tanggalsurvey.getText().toString().trim()
                );
       // showCustomDialog();
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
                lembarstr, tagihanstr, spinerketstr, String.valueOf(latitude), String.valueOf(longitude), keterangan1str,
                keterangan2str, keterangan3str, rencanabayartxt.getText().toString().trim(),
                edtNoTelefon.getText().toString().trim(), statusstr, tanggalsurvey.getText().toString().trim()
        );
        finish();
    }

    private void updatedata() {
        String url_json = "http://bilman.arindo.net/index.php?r=bilman/updatetgk";
            final RequestQueue requestQueue = Volley.newRequestQueue(TagihanBulananActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url_json,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           proses();
                            Log.e("response update data", response);
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
                    params.put("tgl_bayar", rencanabayartxt.getText().toString());
                    params.put("tgl_survey", tanggalsurvey.getText().toString());
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
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

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 2) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void saveToGallery(){
        BitmapDrawable draw = (BitmapDrawable) setImage.getDrawable();
        photo = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(sdCard.getAbsolutePath() + "/GambarPreventif");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap btm00 = getResizedBitmap(photo, max_resolution_image);
        btm00.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("tersimpan", String.valueOf(outFile));
    }

    private void saveToGallerykwh(){
        BitmapDrawable draw = (BitmapDrawable) setImagekwh.getDrawable();
        photo = draw.getBitmap();

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(sdCard.getAbsolutePath() + "/GambarKWH");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap btm00 = getResizedBitmap(photo, max_resolution_image);
        btm00.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("tersimpan", String.valueOf(outFile));
    }

    /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/
    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        //   mService = new BluetoothService(this, new BluetoothHandler(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case TagihanBulananActivity.PERMISSION_BLUETOOTH:
                case TagihanBulananActivity.PERMISSION_BLUETOOTH_ADMIN:
                case TagihanBulananActivity.PERMISSION_BLUETOOTH_CONNECT:
                case TagihanBulananActivity.PERMISSION_BLUETOOTH_SCAN:
                    this.printBluetooth();
                    break;
            }
        }
    }

    private BluetoothConnection selectedDevice;

    public void printBluetooth() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, TagihanBulananActivity.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, TagihanBulananActivity.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, TagihanBulananActivity.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, TagihanBulananActivity.PERMISSION_BLUETOOTH_SCAN);
        } else {
            new AsyncBluetoothEscPosPrint(
                    this,
                    new AsyncEscPosPrint.OnPrintFinished() {
                        @Override
                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                        }

                        @Override
                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                            TagihanBulananActivity.this.finish();
                        }
                    }
            ).execute(this.getAsyncEscPosPrinter(selectedDevice));
        }
    }

    /*==============================================================================================
    ===================================ESC/POS PRINTER PART=========================================
    ==============================================================================================*/

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd  HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                // "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.sulis, DisplayMetrics.DENSITY_400)) + "</img>\n" +
                "[L]\n" +
                        "[C]<u><font size='big'>INFORMASI</font></u>\n" +
                        "[C]<u><font size='big'>TAGIHAN LISTRIK</font></u>\n" +
                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        //"[C]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>ID pel       : </b>[R]562200126060\n" +
                        "[L]<b>Nama         : </b>[R]Sukarah\n" +
                        "[L]<b>Alamat       : </b>[R]KP Jaha Legon\n" +
                        "[L]<b>Tarif/Daya   : </b>[R]R1/450 VA\n" +
                        "[L]<b>Bulan-thn    : </b>[R]April-13\n" +
                        "[L]<b>Tagihan      : </b>[R]Rp.50.447\n" +
                        "[L]<b>Ket          : </b>[R]Belum Lunas\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[C]Untuk menghindari pemutusan\n" +
                        "[C]Segara lunasi tag. listrik ini\n" +
                        "[C]Terima Kasih\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[C]Rincian tagihan dapat diakses di\n" +
                        "[C]www.pln.co.id, Call Centre 123\n" +
                        "[C]Atau hubungi pln terdekat\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[C]INI BUKAN TANDA BUKTI PEMBAYARAN\n" +
                        //"[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        //"[L]Raymond DUPONT\n" +
                        //"[L]5 rue des girafes\n" +
                        //"[L]31547 PERPETES\n" +
                        //"[L]Tel : +33801201456\n" +
                        "\n"
                // "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                //"[L]\n" +
                //"[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
        );
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
        Log.e("Permissiomn", "berhasil");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
        Log.e("Permissiomn", "gagal");
    }
}