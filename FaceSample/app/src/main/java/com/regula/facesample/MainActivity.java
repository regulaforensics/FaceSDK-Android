package com.regula.facesample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesample.adapter.CategoryAdapter;
import com.regula.facesample.data.CategoryDataProvider;
import com.regula.facesample.util.FileUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.InitConfig;

public class MainActivity extends Activity {

    private View progressLayout;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressLayout = findViewById(R.id.progressLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        byte[] license = FileUtil.getLicense(this);

        initFaceSdk(license);
    }

    private void initFaceSdk(byte[] license) {
        InitConfig initConfig = new InitConfig.Builder(license).setLicenseUpdate(true).build();
        FaceSDK.Instance().init(this, initConfig, (status, e) -> {
            if (!status) {
                Log.d("MainActivity", "FaceSDK error: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Init finished with error: " + (e != null ? e.getMessage() : ""), Toast.LENGTH_LONG).show();
                return;
            }

            Log.d("MainActivity", "FaceSDK init succeed ");

            CategoryDataProvider dataProvider = new CategoryDataProvider();

            progressLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new CategoryAdapter(MainActivity.this, dataProvider));

            Log.d("MainActivity", "FaceSDK init completed successfully");
        });
    }
}
