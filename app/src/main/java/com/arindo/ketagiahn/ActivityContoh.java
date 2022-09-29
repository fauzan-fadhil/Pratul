package com.arindo.ketagiahn;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ActivityContoh extends AppCompatActivity {

    private BarChart mBarChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contoh);

        mBarChart = findViewById(R.id.chart);

        float groupSpace = 0.08f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;
        float tahunAwal = 2016f;

        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> dataPemasukan = new ArrayList<BarEntry>();
        dataPemasukan.add(new BarEntry(2016, 15));
        dataPemasukan.add(new BarEntry(2017, 14));
        dataPemasukan.add(new BarEntry(2018, 17));
        dataPemasukan.add(new BarEntry(2019, 13));

        List<BarEntry> dataPengeluaran = new ArrayList<BarEntry>();
        dataPengeluaran.add(new BarEntry(2016, 5));
        dataPengeluaran.add(new BarEntry(2017, 4));
        dataPengeluaran.add(new BarEntry(2018, 7));
        dataPengeluaran.add(new BarEntry(2019, 3));

        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(dataPemasukan, "Pemasukan");
        dataSet1.setColor(ColorTemplate.JOYFUL_COLORS[0]);

        BarDataSet dataSet2 = new BarDataSet(dataPengeluaran, "Pengeluaran");
        dataSet2.setColor(ColorTemplate.JOYFUL_COLORS[1]);

        // Membuat Bar data yang akan di set ke Chart
        BarData barData = new BarData(dataSet1, dataSet2);

        // Pengaturan sumbu X
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);
        xAxis.setCenterAxisLabels(true);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        mBarChart.getAxisRight().setEnabled(false);

        // Menghilankan deskripsi pada Chart
        mBarChart.getDescription().setEnabled(false);

        // Set data ke Chart
        // Tambahkan invalidate setiap kali mengubah data chart
        mBarChart.setData(barData);
        mBarChart.getBarData().setBarWidth(barWidth);
        mBarChart.getXAxis().setAxisMinimum(tahunAwal);
        mBarChart.getXAxis().setAxisMaximum(tahunAwal + mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * 4);
        mBarChart.groupBars(tahunAwal, groupSpace, barSpace);
        mBarChart.setDragEnabled(true);
        mBarChart.invalidate();
    }
}

