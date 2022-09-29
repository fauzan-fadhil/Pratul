package com.arindo.ketagiahn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_ALAMAT;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_BULAN;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_DAYA;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_ID;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_IDPEL;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_KETERANGAN1;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_KETERANGAN2;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_KETERANGAN3;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_KOKED;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_LANGITUD;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_LANGKAH;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_LATITUD;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_LEMBAR;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_NAMA;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_NOTELEPON;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_RENCANABAYAR;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_SPINERKET;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_STATUS;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_TAGIHAN;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_TANGGALSURVEY;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_TARIF;
import static com.arindo.ketagiahn.SimpanOfflineActivity.TAG_UNITUP;

import com.arindo.ketagiahn.ItemSQL;
import com.arindo.ketagiahn.MainActivity;
import com.arindo.ketagiahn.R;
import com.arindo.ketagiahn.TampilActivityOnline;

public class ListViewAdapterOnline extends ArrayAdapter<ItemSQL> implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ItemSQL> items;
    private List<ItemSQL> itemsModelListFiltered;
    Locale localeID;
    NumberFormat formatRupiah;
    private String TAG = MainActivity.class.getSimpleName();

    public ListViewAdapterOnline(Activity activity, List<ItemSQL> items) {
        super(activity, R.layout.list_row, items);
        this.activity = activity;
        this.items = items;
        this.itemsModelListFiltered = items;
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();
    }

    @Override
    public ItemSQL getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row2, null);
        ItemSQL data = itemsModelListFiltered.get(position);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView alamat = (TextView) convertView.findViewById(R.id.alamat);
        TextView idpel = (TextView) convertView.findViewById(R.id.idpel);
        TextView tarif = (TextView) convertView.findViewById(R.id.tarif);
        TextView daya = (TextView) convertView.findViewById(R.id.daya);
        TextView koked = (TextView) convertView.findViewById(R.id.koked);
        TextView tagihan = (TextView) convertView.findViewById(R.id.tagihan);
        TextView langkah = (TextView) convertView.findViewById(R.id.langkah);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearshowdata);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idx = itemsModelListFiltered.get(position).getId();
                final String bulan = itemsModelListFiltered.get(position).getBulan();
                final String unitup = itemsModelListFiltered.get(position).getUnitup();
                final String nama = itemsModelListFiltered.get(position).getNama();
                final String alamat = itemsModelListFiltered.get(position).getAlamat();
                final String idpel = itemsModelListFiltered.get(position).getIdpel();
                final String tarif = itemsModelListFiltered.get(position).getTarif();
                final String daya = itemsModelListFiltered.get(position).getDaya();
                final String koked = itemsModelListFiltered.get(position).getKoked();
                final String langkah = itemsModelListFiltered.get(position).getLangkah();
                final String lembar = itemsModelListFiltered.get(position).getLembar();
                final String tagihan = itemsModelListFiltered.get(position).getTagihan();
                final String spinerket = itemsModelListFiltered.get(position).getSpinerket();
                final String latitud = itemsModelListFiltered.get(position).getLatitud();
                final String langitud = itemsModelListFiltered.get(position).getLangitud();
                final String keterangan1 = itemsModelListFiltered.get(position).getKeterangan1();
                final String keterangan2 = itemsModelListFiltered.get(position).getKeterangan2();
                final String keterangan3 = itemsModelListFiltered.get(position).getKeterangan3();
                final String rencanabayar = itemsModelListFiltered.get(position).getRencanabayar();
                final String notelepon = itemsModelListFiltered.get(position).getNotelepon();
                final String status = itemsModelListFiltered.get(position).getStatus();
                final String tanggalsurvey = itemsModelListFiltered.get(position).getTanggalsurvey();
                Intent intent = new Intent(getContext(), TampilActivityOnline.class);
                intent.putExtra(TAG_ID, idx);
                intent.putExtra(TAG_BULAN, bulan);
                intent.putExtra(TAG_UNITUP, unitup);
                intent.putExtra(TAG_NAMA, nama);
                intent.putExtra(TAG_ALAMAT, alamat);
                intent.putExtra(TAG_IDPEL, idpel);
                intent.putExtra(TAG_TARIF, tarif);
                intent.putExtra(TAG_DAYA, daya);
                intent.putExtra(TAG_KOKED, koked);
                intent.putExtra(TAG_LANGKAH, langkah);
                intent.putExtra(TAG_LEMBAR, lembar);
                intent.putExtra(TAG_TAGIHAN, tagihan);
                intent.putExtra(TAG_SPINERKET, spinerket);
                intent.putExtra(TAG_LATITUD, latitud);
                intent.putExtra(TAG_LANGITUD, langitud);
                intent.putExtra(TAG_KETERANGAN1, keterangan1);
                intent.putExtra(TAG_KETERANGAN2, keterangan2);
                intent.putExtra(TAG_KETERANGAN3, keterangan3);
                intent.putExtra(TAG_RENCANABAYAR, rencanabayar);
                intent.putExtra(TAG_NOTELEPON, notelepon);
                intent.putExtra(TAG_STATUS, status);
                intent.putExtra(TAG_TANGGALSURVEY, tanggalsurvey);
                Log.e("tgl svr adptr", tanggalsurvey);
                getContext().startActivity(intent);
                ((Activity)getContext()).finish();
            }
        });
        nama.setText(data.getNama());
        alamat.setText(data.getAlamat());
        idpel.setText(data.getIdpel());
        tarif.setText(data.getTarif());
        daya.setText(data.getDaya());
        koked.setText(data.getKoked());
        tagihan.setText(formatRupiah.format(Integer.parseInt(data.getTagihan())));
        langkah.setText(data.getLangkah());
        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.e(TAG, "Response from url: " + constraint);
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    //   Log.e(TAG, "6 " +  filterResults);
                    filterResults.count = items.size();
                    filterResults.values = items;
                }else{
                    List<ItemSQL> resultsModel = new ArrayList<>();
                    //  Log.e(TAG, "7 " + resultsModel );
                    String searchStr = constraint.toString().toUpperCase();
                    // Log.e(TAG, "3 " + searchStr);
                    for(ItemSQL itemSQL:items){
                        //  Log.e(TAG, "4 " + itemBarang.getMenu());
                        if(itemSQL.getNama().contains(searchStr) || itemSQL.getIdpel().contains(searchStr)){
                            Log.e(TAG, "8 " + searchStr);
                            resultsModel.add(itemSQL);
                        }
                        filterResults.count = resultsModel.size();
                        //  Log.e(TAG, "1 " +  filterResults.count);
                        filterResults.values = resultsModel;
                        Log.e(TAG, "2 " +  filterResults.values);
                    }
                }
                //  Log.e(TAG, "5 " + filterResults.count);
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                itemsModelListFiltered = (List<ItemSQL>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
