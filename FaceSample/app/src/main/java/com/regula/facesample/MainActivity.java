package com.regula.facesample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.regula.facesdk.Face;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.configuration.LivenessConfiguration;
import com.regula.facesdk.enums.eInputFaceType;
import com.regula.facesdk.structs.Image;
import com.regula.facesdk.structs.MatchFacesRequest;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;

    ImageView imageView1;
    ImageView imageView2;

    Button buttonMatch;
    Button buttonLiveness;
    Button buttonClear;

    TextView textViewSimilarity;
    TextView textViewLiveness;

    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = findViewById(R.id.imageView1);
        imageView1.getLayoutParams().height = 400;

        imageView2 = findViewById(R.id.imageView2);
        imageView2.getLayoutParams().height = 400;

        buttonMatch = findViewById(R.id.buttonMatch);
        buttonLiveness = findViewById(R.id.buttonLiveness);
        buttonClear = findViewById(R.id.buttonClear);

        textViewSimilarity = findViewById(R.id.textViewSimilarity);
        textViewLiveness = findViewById(R.id.textViewLiveness);

        imageView1.setOnClickListener(v -> {
            showMenu(imageView1, PICK_IMAGE_1);
        });

        imageView2.setOnClickListener(v -> {
            showMenu(imageView2, PICK_IMAGE_2);
        });

        buttonMatch.setOnClickListener(v -> {
            if (imageView1.getDrawable() != null && imageView2.getDrawable() != null) {
                textViewSimilarity.setText("Processing...");

                matchFaces(getImageBitmap(imageView1), getImageBitmap(imageView2));
                buttonMatch.setEnabled(false);
                buttonLiveness.setEnabled(false);
                buttonClear.setEnabled(false);
            } else {
                Toast.makeText(MainActivity.this, "Having both images are compulsory", Toast.LENGTH_SHORT).show();
            }
        });

        buttonLiveness.setOnClickListener(v -> startLiveness());

        buttonClear.setOnClickListener(v -> {
            imageView1.setImageDrawable(null);
            imageView2.setImageDrawable(null);
            textViewSimilarity.setText("Similarity: null");
            textViewLiveness.setText("Liveness: null");
        });
    }

    @SuppressLint("ResourceType")
    private void showMenu(ImageView imageView, int i) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, imageView);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.gallery:
                    openGallery(i);
                    return true;
                case R.id.camera:
                    startFaceCaptureActivity(imageView);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.getMenuInflater().inflate(R.layout.menu, popupMenu.getMenu());

        popupMenu.show();
    }

    private Bitmap getImageBitmap(ImageView imageView) {
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        return bitmap;
    }

    private void openGallery(int id) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, id);
    }

    private void startFaceCaptureActivity(ImageView imageView) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build();

        Face.Instance().presentFaceCaptureActivity(MainActivity.this, configuration, faceCaptureResponse -> {
            if (faceCaptureResponse != null && faceCaptureResponse.image != null) {
                imageView.setImageBitmap(faceCaptureResponse.image.getBitmap());
                imageView.setTag(eInputFaceType.ift_Live);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_1){
            imageUri = data.getData();
            imageView1.setImageURI(imageUri);
            imageView1.setTag(eInputFaceType.ift_DocumentPrinted);
            textViewSimilarity.setText("Similarity: null");
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_2){
            imageUri = data.getData();
            imageView2.setImageURI(imageUri);
            imageView2.setTag(eInputFaceType.ift_DocumentPrinted);
            textViewSimilarity.setText("Similarity: null");
        }
    }

    private void matchFaces(Bitmap first, Bitmap second) {
        MatchFacesRequest matchRequest = new MatchFacesRequest();

        Image firstImage = new Image();
        firstImage.setImage(first);
        firstImage.imageType = (Integer) imageView1.getTag();
        matchRequest.images.add(firstImage);

        Image secondImage = new Image();
        secondImage.setImage(second);
        secondImage.imageType = (Integer) imageView2.getTag();
        matchRequest.images.add(secondImage);

        Face.Instance().matchFaces(matchRequest, matchFacesResponse -> {
            if (matchFacesResponse != null && matchFacesResponse.matchedFaces != null && matchFacesResponse.matchedFaces.size() > 0) {
                double similarity = matchFacesResponse.matchedFaces.get(0).similarity;
                textViewSimilarity.setText("Similarity: " + String.format("%.2f", similarity * 100) + "%");
            } else {
                textViewSimilarity.setText("Similarity: null");
            }

            buttonMatch.setEnabled(true);
            buttonLiveness.setEnabled(true);
            buttonClear.setEnabled(true);
        });
    }

    private void startLiveness() {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder().setCameraSwitchEnabled(true).build();

        Face.Instance().startLiveness(MainActivity.this, configuration, livenessResponse -> {
            if (livenessResponse != null && livenessResponse.getBitmap() != null) {
                imageView1.setImageBitmap(livenessResponse.getBitmap());
                imageView1.setTag(eInputFaceType.ift_Live);

                if (livenessResponse.liveness == 0) {
                    textViewLiveness.setText("Liveness: passed");
                } else {
                    textViewLiveness.setText("Liveness: unknown");
                }
            } else {
                textViewLiveness.setText("Liveness: null");
            }

            textViewSimilarity.setText("Similarity: null");
        });
    }
}
