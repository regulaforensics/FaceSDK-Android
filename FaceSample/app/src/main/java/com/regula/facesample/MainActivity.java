package com.regula.facesample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesample.adapter.CategoryAdapter;
import com.regula.facesample.data.CategoryDataProvider;
import com.regula.facesdk.FaceSDK;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CategoryDataProvider dataProvider = new CategoryDataProvider();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(new CategoryAdapter(this, dataProvider));

        FaceSDK.Instance().initialize(this, (status, e) -> {
            if (!status) {
                Toast.makeText(MainActivity.this, "Init finished with error: " + (e != null ? e.getMessage() : ""), Toast.LENGTH_LONG).show();
                return;
            }
            Log.d("MainActivity", "FaceSDK init completed successfully");
        });
    }
}
