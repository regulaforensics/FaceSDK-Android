package com.regula.facepersonsearch;

import static com.regula.facepersonsearch.FaceSearchApp.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facepersonsearch.databinding.ActivityCreateBinding;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.FaceCaptureCallback;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.model.results.FaceCaptureResponse;
import com.regula.facesdk.model.results.person.PageableItemList;
import com.regula.facesdk.model.results.person.Person;
import com.regula.facesdk.model.results.person.PersonImage;
import com.regula.facesdk.request.person.ImageUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    public static final String PERSON_ID = "person_id";
    ActivityCreateBinding binding;
    private static List<Common.ImageUploadWithThumbnail> images;
    private static List<PersonImage> existingImages;
    PersonImageAdapter adapter;
    PersonImageRecyclerViewAdapter addedImagesAdapter;
    Handler handler = new Handler(Looper.getMainLooper());
    List<String> itemsToRemove = new ArrayList<>();
    String groupId, personId;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        images = new ArrayList<>();
        existingImages = new ArrayList<>();
        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        groupId = getIntent().getStringExtra(GroupsActivity.GROUP_ID);
        personId = getIntent().getStringExtra(PERSON_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(CreateActivity.this, LinearLayoutManager.HORIZONTAL, false);

        adapter = new PersonImageAdapter();
        binding.imagesRV.setLayoutManager(layoutManager);
        binding.imagesRV.setAdapter(adapter);

        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(CreateActivity.this, LinearLayoutManager.HORIZONTAL, false);

        addedImagesAdapter = new PersonImageRecyclerViewAdapter(existingImages, true);
        binding.imagesAddedRv.setLayoutManager(layoutManager2);
        binding.imagesAddedRv.setAdapter(addedImagesAdapter);

        if(personId != null) {
            binding.createBtn.setText("Update Person");
            getPerson();
        } else {
            binding.createBtn.setOnClickListener(view -> createPerson());
            binding.existingImagesLayout.setVisibility(View.GONE);
        }

        binding.addImageBtn.setOnClickListener(view -> {
            showMenu(binding.addImageBtn);
        });
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
                    addImage(byteImage);
                }
            }
        });
    }

    private void getPerson() {
        FaceSDK.Instance().personDatabase().getPerson(personId, new PersonDBCallback<Person>() {
            @Override
            public void onSuccess(@Nullable Person response) {
                if (response == null)
                    return;
                person = response;
                String[] names = response.getName().split(" ");
                binding.nameEt.setText(names[0]);

                if (names.length > 1) {
                    String surname = "";
                    for (int i = 1; i < names.length; i++) {
                        surname += names[i] + " ";
                    }
                    binding.surnameEt.setText(surname);
                }

                binding.createBtn.setOnClickListener(view -> updatePerson());
            }

            @Override
            public void onFailure(@NonNull String message) {
                Toast.makeText(CreateActivity.this,
                        message,
                        Toast.LENGTH_LONG).show();
            }
        });

        FaceSDK.Instance().personDatabase().getPersonImages(personId,
                new PersonDBCallback<PageableItemList<List<PersonImage>, PersonImage>>() {
                    @Override
                    public void onSuccess(@Nullable PageableItemList<List<PersonImage>, PersonImage> response) {
                        if (response == null || response.getItemsList() == null)
                            return;
                        existingImages.clear();
                        existingImages.addAll(response.getItemsList());
                        handler.post(() -> {
                            addedImagesAdapter.notifyDataSetChanged();
                            binding.existingImagesCountTv.setText(String.valueOf(existingImages.size()));
                        });
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        Toast.makeText(CreateActivity.this,
                                message,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGallery() {
        startForResult.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getData() != null && result.getData().getData() != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(result.getData().getData());
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    inputStream.close();
                    addImage(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(CreateActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void openDefaultCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startCameraForResult.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> startCameraForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null && result.getData() != null && result.getData().getExtras() != null) {
                    Object photo = result.getData().getExtras().get("data");

                    if(photo instanceof Bitmap) {
                        byte[] byteImage = Common.transformBitmapToByte((Bitmap) photo);
                        addImage(byteImage);
                    }
                }
            });

    private void addImage(byte[] byteImage) {
        Common.ImageUploadWithThumbnail image = new Common.ImageUploadWithThumbnail();
        image.setImageData(byteImage);
        images.add(0, image);
        adapter.notifyItemInserted(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            binding.nameEt.setText("");
            binding.surnameEt.setText("");
            images.clear();
            adapter.notifyDataSetChanged();
            if(personId != null) {
                getPerson();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reset, menu);
        return true;
    }

    private void createPerson() {
        String name = binding.nameEt.getText() + " " + binding.surnameEt.getText();
        FaceSDK.Instance().personDatabase().createPerson(name, new String[]{groupId}, new PersonDBCallback<Person>() {
            @Override
            public void onSuccess(Person response) {
                for (ImageUpload image : images) {
                    FaceSDK.Instance().personDatabase().addPersonImage(response.getId(), image, imagePersonDBCallback);
                }
                if (response != null) {
                    Toast.makeText(CreateActivity.this, "Person created", Toast.LENGTH_LONG).show();
                }
                CreateActivity.this.finish();
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, message);
            }
        });
    }

    private void updatePerson() {
        String name = binding.nameEt.getText() + " " + binding.surnameEt.getText();
        person.setName(name);

        for (ImageUpload image : images) {
            FaceSDK.Instance().personDatabase().addPersonImage(person.getId(), image, imagePersonDBCallback);
        }

        for(String toRemove : itemsToRemove){
            FaceSDK.Instance().personDatabase().deletePersonImage(person.getId(), toRemove, new PersonDBCallback<Void>() {
                @Override
                public void onSuccess(@Nullable Void response) {
                    Log.d(TAG,  "Image removed: " + toRemove);
                }

                @Override
                public void onFailure(@NonNull String message) {
                    Log.d(TAG,  message);
                }
            });
        }

        FaceSDK.Instance().personDatabase().updatePerson(person, new PersonDBCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void response) {
                Toast.makeText(CreateActivity.this,
                        "Person updated",
                        Toast.LENGTH_LONG).show();

                CreateActivity.this.finish();
            }

            @Override
            public void onFailure(@NonNull String message) {
                Toast.makeText(CreateActivity.this,
                        message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    PersonDBCallback<PersonImage> imagePersonDBCallback = new PersonDBCallback<PersonImage>() {
        @Override
        public void onSuccess(PersonImage response) {
            Log.d(TAG,  "Image added: " + response.getPath());
        }

        @Override
        public void onFailure(String message) {
            Log.d(TAG,  message);
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemPosition = -1;
        try {
            itemPosition = ((PersonImageRecyclerViewAdapter) binding.imagesAddedRv.getAdapter()).getPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }

        itemsToRemove.add(existingImages.get(itemPosition).getId());

        existingImages.remove(itemPosition);
        addedImagesAdapter.notifyItemRemoved(itemPosition);

        String size = existingImages == null ? "0" : String.valueOf(existingImages.size());
        binding.existingImagesCountTv.setText(size);

        return super.onContextItemSelected(item);
    }

    static class PersonImageAdapter extends RecyclerView.Adapter<PersonImageAdapter.PersonImageVH>{

        @NonNull
        @Override
        public PersonImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new PersonImageVH(layoutInflater.inflate(R.layout.image_vh, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PersonImageVH holder, int position) {
            holder.imgView.setImageBitmap(images.get(position).getThumbnail());
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        static class PersonImageVH extends RecyclerView.ViewHolder{
            ImageView imgView;

            public PersonImageVH(@NonNull View itemView) {
                super(itemView);

                imgView = itemView.findViewById(R.id.personIV);
            }
        }
    }
}