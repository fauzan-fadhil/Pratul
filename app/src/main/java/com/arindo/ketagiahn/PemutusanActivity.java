package com.arindo.ketagiahn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arindo.ketagiahn.adapter.ListViewAdapterPemutusan;
import com.arindo.ketagiahn.constanta.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PemutusanActivity extends AppCompatActivity {

    private ListView listView;
    List<ItemSQL> itemList = new ArrayList<ItemSQL>();
    ListViewAdapterPemutusan adapter;
    DBHelper SQLite = new DBHelper(this);
    private ImageView noNotesView;
    private TextView tidakadadata;
    String Item, getSpiner;
    int indexmenu;

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

    ArrayList<HashMap<String, String>> row;
    private SharedPreferences preferences;
    Spinner spinner;
    public static MenuItem searchIem;
    public static SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);

        preferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        spinner = findViewById(R.id.spinerkoked);
        indexmenu = 0;
        Toolbar toolbar = findViewById(R.id.tolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PemutusanActivity.this.finish();
            }
        });

        getSupportActionBar().setTitle("Galangan");
        SQLite = new DBHelper(getApplicationContext());

        listView = (ListView) findViewById(R.id.listview);
        noNotesView = findViewById(R.id.empty_notes_view);
        tidakadadata = findViewById(R.id.tidakadadata);

        Item = preferences.getString("kokedpref", null);

        adapter = new ListViewAdapterPemutusan(PemutusanActivity.this, itemList);
        listView.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexmenu = position;
                getSpiner = spinner.getSelectedItem().toString().trim();
                itemList.clear();
                Log.e("getSpiner", getSpiner);
                getdatatanggal21();
                cekkoked();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getresponse();
        getdatatanggal21();
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
        if (SQLite.cekkoked(getSpiner) > 0) {
            noNotesView.setVisibility(View.GONE);
            tidakadadata.setVisibility(View.GONE);
            searchIem.setEnabled(true);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            tidakadadata.setVisibility(View.VISIBLE);
            searchIem.setEnabled(false);
        }
    }

    private void getdatatanggal21() {
        ArrayList<HashMap<String, String>> row = SQLite.getdatatanggal21(getSpiner);
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
        //cekkoked();
        toggleEmptyNotes();
        adapter.notifyDataSetChanged();
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size()>0
        if (SQLite.getNotesCount01(getSpiner) > 0) {
            noNotesView.setVisibility(View.GONE);
            tidakadadata.setVisibility(View.GONE);
            //btnsimpanserver.setVisibility(View.VISIBLE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            tidakadadata.setVisibility(View.VISIBLE);
            //btnsimpanserver.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getdatatanggal21();
    }

    private void getresponse() {
        try {
            JSONObject objspiner = new JSONObject(Item);
            JSONArray arrayspiner = objspiner.getJSONArray("data");
            String[] list = new String[arrayspiner.length()];
            for (int i = 0; i < arrayspiner.length(); i++) {
                JSONObject ObjectSpiner = arrayspiner.getJSONObject(i);
                list[i] = ObjectSpiner.getString("koked");
                Log.e("response", String.valueOf(Item));
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, list);
            spinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ShowDialog() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                this
        );
        alertDialogBuilder.setTitle("Simpan data");
        alertDialogBuilder
                .setMessage("Simpan data ke server")
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //updatedata();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void editonline() {
        row = SQLite.getStatus01(getSpiner);
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
            //02 sudah dikunjungi sudah dikirim
            String statusstr = "02";
            SQLite.update(Integer.parseInt(id),
                    bulan,
                    unitup, idpel,
                    nama, alamat, tarif, daya,
                    koked, Integer.parseInt(langkah), lembar, tagihan, spinerket, langitud, latitud,
                    keterangan1, keterangan2, keterangan3, rencanabayar,
                    notelepon, statusstr, tanggalsurvey
            );
            finish();
        }
    }

    private void updatedata() {
        String url_json = "http://bilman.arindo.net/index.php?r=bilman/updatetgk";
        ArrayList<HashMap<String, String>> row = SQLite.getStatus01(getSpiner);
        for (int J = 0; J < row.size(); J++) {
            final int finalJ = J;
            final RequestQueue requestQueue = Volley.newRequestQueue(PemutusanActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url_json,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            proses();
                            // Toast.makeText(TagihanBulananActivity.this, response, Toast.LENGTH_LONG).show();
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

                final String unitup = itemList.get(finalJ).getUnitup();
                final String bulan = itemList.get(finalJ).getBulan();
                final String idpelanggan = itemList.get(finalJ).getIdpel();
                final String tgl_bayar = itemList.get(finalJ).getRencanabayar();
                final String tgl_survey = itemList.get(finalJ).getTanggalsurvey();
                final String latitude = itemList.get(finalJ).getLatitud();
                final String longitude = itemList.get(finalJ).getLangitud();
                final String ket_bayar = itemList.get(finalJ).getSpinerket();
                final String keterangan1str = itemList.get(finalJ).getKeterangan1();
                final String keterangan2str = itemList.get(finalJ).getKeterangan2();
                final String keterangan3str = itemList.get(finalJ).getKeterangan3();
                final String no_telp = itemList.get(finalJ).getNotelepon();

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("unitup", unitup);
                    params.put("bulan", bulan);
                    params.put("idpelanggan", idpelanggan);
                    params.put("tgl_bayar", tgl_bayar);
                    params.put("tgl_survey", tgl_survey);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("ket_bayar", ket_bayar);
                    params.put("sta_infotag", keterangan1str);
                    params.put("sta_infotempo", keterangan2str);
                    params.put("sta_infodenda", keterangan3str);
                    params.put("no_telp", no_telp);
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
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
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
                        PemutusanActivity.this.finish();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}


