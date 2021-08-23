package com.regula.facesample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.regula.facesample.adapter.CategoryAdapter;
import com.regula.facesample.data.CategoryDataProvider;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CategoryDataProvider dataProvider = new CategoryDataProvider();

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new CategoryAdapter(this, dataProvider));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataProvider.getCategoryItems().get(position).onItemSelected(MainActivity.this);
            }
        });
    }
}
