package com.regula.facepersonsearch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.FaceCaptureCallback;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.model.results.FaceCaptureResponse;
import com.regula.facesdk.model.results.person.PageableItemList;
import com.regula.facesdk.model.results.person.Person;
import com.regula.facesdk.model.results.person.PersonImage;
import com.regula.facesdk.model.results.person.SearchPerson;
import com.regula.facesdk.request.person.ImageUpload;
import com.regula.facesdk.request.person.SearchPersonRequest;

import java.io.IOException;
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
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search);

        browseBtn = findViewById(R.id.browseBtn);
        searchBtn = findViewById(R.id.searchBtn);
        searchByUlrBtn = findViewById(R.id.searchByUlrBtn);
        searchImg = findViewById(R.id.searchIV);
        personsRv = findViewById(R.id.personRV);

        manager = getSupportFragmentManager();

        groupIds = getIntent().getStringArrayExtra(GroupsActivity.GROUP_ID);
        name = getIntent().getStringExtra(GroupsActivity.GROUP_NAME);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search in " + (name != null ? name : "all groups"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        browseBtn.setOnClickListener(view -> {
            showMenu(browseBtn);
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
        personsRv.setAdapter(new PersonsAdapter(this, persons));
    }

    private void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.gallery:
                    openGallery();
                    return true;
                case R.id.camera:
                    startFaceCaptureActivity();
                    return true;
                case R.id.photo:
                    openDefaultCamera();
                    return true;
            }
            return false;
        });
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.show();
    }

    private void openGallery() {
        startForResult.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
    }

    private void startFaceCaptureActivity() {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .build();

        FaceSDK.Instance().presentFaceCaptureActivity(this, configuration, new FaceCaptureCallback() {
            @Override
            public void onFaceCaptured(@NonNull FaceCaptureResponse faceCaptureResponse) {
                if (faceCaptureResponse.getImage() != null && faceCaptureResponse.getImage().getBitmap() != null) {
                    Bitmap photo = faceCaptureResponse.getImage().getBitmap();
                    byte[] byteImage = Common.transformBitmapToByte(photo);
                    setImage(byteImage);
                }
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getData() != null && result.getData().getData() != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(result.getData().getData());
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();
                setImage(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    });

    private void setImage(byte[] bytes) {
        image = new Common.ImageUploadWithThumbnail();
        image.setImageData(bytes);
        searchImg.setImageBitmap(image.getThumbnail());
        browseBtn.setEnabled(true);
    }

    ActivityResultLauncher<Intent> startCameraForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null && result.getData() != null && result.getData().getExtras() != null) {
                    Object photo = result.getData().getExtras().get("data");
                    if(photo instanceof Bitmap) {
                        byte[] byteImage = Common.transformBitmapToByte((Bitmap) photo);
                        setImage(byteImage);
                    }
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(SearchActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void openDefaultCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startCameraForResult.launch(cameraIntent);
    }

    private void searchPerson(SearchPersonRequest searchPersonRequest) {
        FaceSDK.Instance().personDatabase(this).searchPerson(searchPersonRequest,
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

    static class PersonsAdapter extends RecyclerView.Adapter<PersonHolder>{

        List<Person> persons;
        Context context;

        PersonsAdapter(Context context, List<Person> persons){
            this.persons = persons;
            this.context = context;
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
                FaceSDK.Instance().personDatabase(context).getPersonImages(person.getId(),
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