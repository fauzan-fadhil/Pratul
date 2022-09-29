package com.arindo.ketagiahn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends ArrayAdapter<ItemKoked> implements Filterable  {

    private List<ItemKoked> itemKokeds;
    private List<ItemKoked> itemsModelListFiltered;
    private Context context;
    Locale localeID;
    NumberFormat formatRupiah;
    private String TAG = MainActivity.class.getSimpleName();

    public ListViewAdapter(List<ItemKoked> itemKokeds, Context context) {
        super(context, R.layout.activity_download_data, itemKokeds);
        this.itemKokeds = itemKokeds;
        this.context = context;
        this.itemsModelListFiltered = itemKokeds;
        localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();
    }

    @Override
    public ItemKoked getItem(int position) {
        return itemsModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View listViewItem = inflater.inflate(R.layout.activity_download_data, null, true);
        final TextView txtidpel = listViewItem.findViewById(R.id.idpel);
        final TextView textnama = listViewItem.findViewById(R.id.nama);
        final TextView textalamat = listViewItem.findViewById(R.id.alamat);
        final TextView textkoked = listViewItem.findViewById(R.id.koked);
        final TextView txtlangkah = listViewItem.findViewById(R.id.langkah);
        final TextView textdaya = listViewItem.findViewById(R.id.daya);
        final TextView texttarif = listViewItem.findViewById(R.id.tarif);
        final TextView texttagihan = listViewItem.findViewById(R.id.tagihan);

        ItemKoked itemKoked = itemsModelListFiltered.get(position);
        txtidpel.setText(itemKoked.getIdpel());
        textnama.setText(itemKoked.getNama());
        textalamat.setText(itemKoked.getAlamat());
        textkoked.setText(itemKoked.getKoked());
        txtlangkah.setText(itemKoked.getLangkah());
        textdaya.setText(itemKoked.getDaya());
        texttarif.setText(itemKoked.getTarif());
        texttagihan.setText(formatRupiah.format(Integer.parseInt(itemKoked.getTagihan())));
        return listViewItem;
    }
}


