package com.arindo.ketagiahn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.arindo.ketagiahn.adapter.ListViewAdapterOnline;
import com.arindo.ketagiahn.constanta.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpanOnlineActivity extends AppCompatActivity {

    ListView listView;
    List<ItemSQL> itemList = new ArrayList<ItemSQL>();
    ListViewAdapterOnline adapter;
    DBHelper SQLite = new DBHelper(this);
    private ImageView noNotesView;
    private TextView tidakadadata;
    Spinner spinner;
    String Item, getSpiner;
    private SharedPreferences preferences;
    int indexmenu;
    ImageView resetdata;

    public static final String TAG_ID = "id";
    public static final String TAG_BULAN = "bulan";
    public static final String TAG_UNITUP = "unitup";
    public static final String TAG_IDPEL = "idpel";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_TARIF = "tarif";
    public static final String TAG_DAYA = "daya";
    public static final String TAG_KOKED = "koked";
    public static final String TAG_LANGKAH = "langkah";
    public static final String TAG_LEMBAR = "lembar";
    public static final String TAG_TAGIHAN = "tagihan";
    public static final String TAG_SPINERKET = "spinerket";
    public static final String TAG_LATITUD = "latitud";
    public static final String TAG_LANGITUD = "langitud";
    public static final String TAG_KETERANGAN1 = "keterangan1";
    public static final String TAG_KETERANGAN2 = "keterangan2";
    public static final String TAG_KETERANGAN3 = "keterangan3";
    public static final String TAG_RENCANABAYAR = "rencanabayar";
    public static final String TAG_NOTELEPON = "notelepon";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TANGGALSURVEY = "tanggalsurvey";

    public static MenuItem searchIem;
    public static SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_online);
        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        spinner = findViewById(R.id.spinerkoked);
        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanOnlineActivity.this.finish();
            }
        });
        getSupportActionBar().setTitle("Data Server");
        getSupportActionBar().setSubtitle("data berhasil diupdate ke server");
        SQLite = new DBHelper(getApplicationContext());
        indexmenu = 0;
        listView = (ListView) findViewById(R.id.listview);
        noNotesView = findViewById(R.id.empty_notes_view);
        tidakadadata = findViewById(R.id.tidakadadata);
        resetdata = findViewById(R.id.resetdata);
        Item = preferences.getString("kokedpref", null);

        adapter = new ListViewAdapterOnline(SimpanOnlineActivity.this, itemList);
        listView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexmenu = position;
                getSpiner = spinner.getSelectedItem().toString().trim();
                itemList.clear();
                Log.e("getSpiner", getSpiner);
                getStatus02();
                cekkoked();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        resetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 pemberitahuan();
            }
        });

        getresponse();
        getStatus02();
    }

    private void pemberitahuan() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Pemberitahuan ");
        alertDialogBuilder
                .setMessage("Data yang akan anda hapus adalah data yang sudah dikunjungi dan sudah lunas! lanjutkan. . .")
                .setCancelable(false)
                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteperstatus();
                    }
                })
                .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteperstatus() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Reset Data ");
        alertDialogBuilder
                .setMessage("anda yakin akan menghapus data")
                .setCancelable(false)
                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLite.hapusperstatus(getSpiner);
                        itemList.clear();
                        SimpanOnlineActivity.this.finish();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        searchIem = menu.findItem(R.id.search);
        searchView = (SearchView) searchIem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Untuk memfilter data dari ArrayAdapter
                adapter.getFilter().filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                adapter.getFilter().filter(nextText);
                return false;
            }
        });
        return true;
    }

    private void cekkoked() {
        // you can check notesList.size()>0
        if (SQLite.getNotesCount02(getSpiner) > 0) {
            noNotesView.setVisibility(View.GONE);
            tidakadadata.setVisibility(View.GONE);
            searchIem.setEnabled(true);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            tidakadadata.setVisibility(View.VISIBLE);
            searchIem.setEnabled(false);
        }
    }

    private void getStatus02() {
        ArrayList<HashMap<String, String>> row = SQLite.getStatus02(getSpiner);
        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_ID);
            String unitup = row.get(i).get(TAG_UNITUP);
            String bulan = row.get(i).get(TAG_BULAN);
            String idpel = row.get(i).get(TAG_IDPEL);
            String nama = row.get(i).get(TAG_NAMA);
            String alamat = row.get(i).get(TAG_ALAMAT);
            String tarif = row.get(i).get(TAG_TARIF);
            String daya = row.get(i).get(TAG_DAYA);
            String koked = row.get(i).get(TAG_KOKED);
            String langkah = row.get(i).get(TAG_LANGKAH);
            String lembar = row.get(i).get(TAG_LEMBAR);
            String tagihan = row.get(i).get(TAG_TAGIHAN);
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
            ItemSQL data = new ItemSQL();
            data.setId(id);
            data.setUnitup(unitup);
            data.setBulan(bulan);
            data.setIdpel(idpel);
            data.setNama(nama);
            data.setAlamat(alamat);
            data.setTarif(tarif);
            data.setDaya(daya);
            data.setKoked(koked);
            data.setLangkah(langkah);
            data.setLembar(lembar);
            data.setTagihan(tagihan);
            data.setSpinerket(spinerket);
            data.setLatitud(latitud);
            data.setLangitud(langitud);
            data.setKeterangan1(keterangan1);
            data.setKeterangan2(keterangan2);
            data.setKeterangan3(keterangan3);
            data.setRencanabayar(rencanabayar);
            data.setNotelepon(notelepon);
            data.setStatus(status);
            data.setTanggalsurvey(tanggalsurvey);
            itemList.add(data);
        }
        adapter.notifyDataSetChanged();
        toggleEmptyNotes();
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size()>0
        if (SQLite.getNotesCount02(getSpiner) > 0) {
            noNotesView.setVisibility(View.GONE);
            tidakadadata.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            tidakadadata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getStatus02();
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
}