package com.regula.facepersonsearch;

import static com.regula.facepersonsearch.Common.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.regula.facepersonsearch.Common.pickImage;
import static com.regula.facepersonsearch.FaceSearchApp.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facepersonsearch.databinding.ActivityCreateBinding;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.callback.PersonDBCallback;
import com.regula.facesdk.model.results.person.PageableItemList;
import com.regula.facesdk.model.results.person.Person;
import com.regula.facesdk.model.results.person.PersonImage;
import com.regula.facesdk.request.person.ImageUpload;

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
            binding.createBtn.setText("Update");
            FaceSDK.Instance().personDatabase().getPerson(personId, new PersonDBCallback<Person>() {
                @Override
                public void onSuccess(@Nullable Person response) {
                    if(response == null)
                        return;
                    person = response;
                    String[] names = response.getName().split(" ");
                    binding.nameEt.setText(names[0]);

                    if(names.length > 1){
                        String surname = "";
                        for(int i=1; i<names.length; i++){
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
                    if(response == null || response.getItemsList() == null)
                        return;
                    existingImages.clear();
                    existingImages.addAll(response.getItemsList());
                    handler.post( () -> {
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
        } else {
            binding.createBtn.setOnClickListener(view -> createPerson());
            binding.existingImagesLayout.setVisibility(View.GONE);
        }

        binding.addImageBtn.setOnClickListener(view -> {
            pickImage(CreateActivity.this);
        });
    }

    private void createPerson() {
        String name = binding.nameEt.getText() + " " + binding.surnameEt.getText();
        FaceSDK.Instance().personDatabase().createPerson(name, new String[]{groupId}, new PersonDBCallback<Person>() {
            @Override
            public void onSuccess(Person response) {
                for (ImageUpload image : images) {
                    FaceSDK.Instance().personDatabase().addPersonImage(response.getId(), image, imagePersonDBCallback);
                }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Common.PICK_IMAGE) {

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                byte[] bytes = new byte[inputStream.available()];
                        inputStream.read(bytes);
                Common.ImageUploadWithThumbnail image = new Common.ImageUploadWithThumbnail();
                image.setImageData(bytes);
                images.add(image);
                adapter.notifyItemInserted(images.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick image after request permission success
                pickImage(CreateActivity.this);
            }
        }
    }

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