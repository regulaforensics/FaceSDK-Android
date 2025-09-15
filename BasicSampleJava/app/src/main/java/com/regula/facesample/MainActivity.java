package com.regula.facesample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        applyEdgeToEdgeInsets();
    }

    private void applyEdgeToEdgeInsets() {
        View rootView = getWindow().getDecorView().findViewWithTag("content");
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
                @NonNull
                @Override
                public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat insets) {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
            });
        }
    }
}
