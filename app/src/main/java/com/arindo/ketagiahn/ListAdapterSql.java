package com.arindo.ketagiahn;

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

public class ListAdapterSql extends ArrayAdapter<ItemSQL> implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ItemSQL> items;
    private List<ItemSQL> itemsModelListFiltered;
    Locale localeID;
    NumberFormat formatRupiah;
    private String TAG = MainActivity.class.getSimpleName();

    public ListAdapterSql(Activity activity, List<ItemSQL> items) {
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
            convertView = inflater.inflate(R.layout.list_row, null);
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
                Intent intent = new Intent(getContext(), TagihanBulananActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ItemSQL itemSQL = itemsModelListFiltered.get(position);
                intent.putExtra("id", itemSQL.getId());
                intent.putExtra("bulan", itemSQL.getBulan());
                intent.putExtra("unitup", itemSQL.getUnitup());
                intent.putExtra("idpel", itemSQL.getIdpel());
                intent.putExtra("nama", itemSQL.getNama());
                intent.putExtra("alamat", itemSQL.getAlamat());
                intent.putExtra("tarif", itemSQL.getTarif());
                intent.putExtra("daya", itemSQL.getDaya());
                intent.putExtra("koked", itemSQL.getKoked());
                intent.putExtra("langkah", itemSQL.getLangkah());
                intent.putExtra("lembar", itemSQL.getLembar());
                intent.putExtra("tagihan", itemSQL.getTagihan());
                intent.putExtra("spinerket", itemSQL.getSpinerket());
                intent.putExtra("latitud", itemSQL.getLatitud());
                intent.putExtra("langitud", itemSQL.getLangitud());
                intent.putExtra("keterangansatu", itemSQL.getKeterangan1());
                intent.putExtra("keterangandua", itemSQL.getKeterangan2());
                intent.putExtra("keterangantiga", itemSQL.getKeterangan3());
                intent.putExtra("rencanabayar", itemSQL.getRencanabayar());
                intent.putExtra("notelepon", itemSQL.getNotelepon());
                intent.putExtra("status", itemSQL.getStatus());
                intent.putExtra("tanggalsurvey", itemSQL.getTanggalsurvey());
                getContext().startActivity(intent);
                //((Activity)getContext()).finish();
                itemsModelListFiltered.clear();
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
