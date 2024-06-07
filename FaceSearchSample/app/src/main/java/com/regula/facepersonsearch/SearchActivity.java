package com.regula.facepersonsearch;

import static com.regula.facepersonsearch.Common.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.regula.facepersonsearch.Common.pickImage;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.model.results.person.PageableItemList;
import com.regula.facesdk.model.results.person.Person;
import com.regula.facesdk.model.results.person.PersonImage;
import com.regula.facesdk.model.results.person.SearchPerson;
import com.regula.facesdk.request.person.ImageUpload;
import com.regula.facesdk.request.person.SearchPersonRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    Button browseBtn, searchBtn, searchByUlrBtn;
    ImageView searchImg;
    Common.ImageUploadWithThumbnail image;
    RecyclerView personsRv;
    List<Person>persons = new ArrayList<>();

    static FragmentManager manager;

    private String[] groupIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        browseBtn = findViewById(R.id.browseBtn);
        searchBtn = findViewById(R.id.searchBtn);
        searchByUlrBtn = findViewById(R.id.searchByUlrBtn);
        searchImg = findViewById(R.id.searchIV);
        personsRv = findViewById(R.id.personRV);

        manager = getSupportFragmentManager();

        groupIds = getIntent().getStringArrayExtra(GroupsActivity.GROUP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        browseBtn.setOnClickListener(view -> {
            Common.pickImage(SearchActivity.this);
        });

        searchBtn.setOnClickListener(view -> {

            SearchPersonRequest searchPersonRequest = new SearchPersonRequest();
            if(groupIds != null)
                searchPersonRequest.setGroupIdsForSearch(groupIds);
            searchPersonRequest.setImageUpload(image);
            searchPersonRequest.setDetectAll(true);
            searchPerson(searchPersonRequest);
        });

        searchByUlrBtn.setOnClickListener(v -> {
            SearchPersonRequest searchPersonRequest = new SearchPersonRequest();
            if(groupIds != null)
                searchPersonRequest.setGroupIdsForSearch(groupIds);
            searchPersonRequest.setImageUpload(image);

            ImageUpload imageUpload = new ImageUpload();
            imageUpload.setImageUrl("https://faceapi.regulaforensics.com/face-demo/detection/04.jpg");
            searchPersonRequest.setImageUpload(imageUpload);

            searchPerson(searchPersonRequest);
        });

        personsRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        personsRv.addItemDecoration(new DividerItemDecoration(SearchActivity.this,
                DividerItemDecoration.HORIZONTAL));
        personsRv.setAdapter(new PersonsAdapter(persons));
    }

    private void searchPerson(SearchPersonRequest searchPersonRequest) {
        FaceSDK.Instance().personDatabase().searchPerson(searchPersonRequest,
                new PersonDBCallback<List<SearchPerson>>() {
                    @Override
                    public void onSuccess(List<SearchPerson> response) {
                        persons.clear();
                        persons.addAll(response);
                        personsRv.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(SearchActivity.this, "ERROR: " + message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick image after request permission success
                pickImage(SearchActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Common.PICK_IMAGE) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                image = new Common.ImageUploadWithThumbnail();
                image.setImageData(bytes);

                searchImg.setImageBitmap(image.getThumbnail());

                browseBtn.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class PersonsAdapter extends RecyclerView.Adapter<PersonHolder>{

        List<Person> persons;

        PersonsAdapter(List<Person> persons){
            this.persons = persons;
        }

        @NonNull
        @Override
        public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
            holder.textView.setText(persons.get(position).getName());

            Person person = persons.get(position);
            holder.itemView.setOnClickListener(view -> {
                FaceSDK.Instance().personDatabase().getPersonImages(person.getId(),
                        new PersonDBCallback<PageableItemList<List<PersonImage>, PersonImage>>() {
                            @Override
                            public void onSuccess(PageableItemList<List<PersonImage>, PersonImage> response) {
                                new PersonImageFragment(response.getItemsList()).show(manager, "TAG");
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(holder.itemView.getContext(), "ERROR: " + message, Toast.LENGTH_LONG).show();
                            }
                        });
            });
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }
    }

    static class PersonHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public PersonHolder(View view) {
            super(view);

            textView = view.findViewById(android.R.id.text1);
        }
    }
}