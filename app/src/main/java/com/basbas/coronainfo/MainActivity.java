package com.basbas.coronainfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.basbas.coronainfo.model.ResponseData;
import com.basbas.coronainfo.network.ApiInterface;
import com.basbas.coronainfo.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvPositif, tvSembuh, tvMeninggal, tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniView();
        getData();
    }

    private void iniView() {

        tvDate = findViewById(R.id.tv_date);
        tvMeninggal = findViewById(R.id.meninggal);
        tvSembuh = findViewById(R.id.sembuh);
        tvPositif = findViewById(R.id.positif);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM  yyyy   ", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        tvDate.setText(currentDateandTime);

    }

    private void getData() {

        ApiInterface client = ApiService.createService(ApiInterface.class);
        // Fetch and print a list of the contributors to this library.
        Call<List<ResponseData>> call = client.getData();
        call.enqueue(new Callback<List<ResponseData>>() {
            @Override
            public void onResponse(Call<List<ResponseData>> call, Response<List<ResponseData>> response) {
                Log.e("TAG","hasil data"+response.body().get(0).getPositif());

                tvMeninggal.setText(response.body().get(0).getMeninggal());
                tvPositif.setText(response.body().get(0).getPositif());
                tvSembuh.setText(response.body().get(0).getSembuh());
            }

            @Override
            public void onFailure(Call<List<ResponseData>> call, Throwable t) {
                Log.e("TAG","gagal data"+t);

            }
        });


    }


}
