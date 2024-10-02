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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.enums.ImageType;
import com.regula.facesdk.enums.LivenessStatus;
import com.regula.facesdk.model.MatchFacesImage;
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit;
import com.regula.facesdk.request.MatchFacesRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class MatchFacesActivity extends Activity {

    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;

    ImageView imageView1;
    ImageView imageView2;

    RadioGroup group0, group1;

    Button buttonMatch;
    Button buttonLiveness;
    Button buttonClear;

    TextView textViewSimilarity;
    TextView textViewLiveness;

    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_faces);

        imageView1 = findViewById(R.id.imageView1);
        imageView1.getLayoutParams().height = 400;

        imageView2 = findViewById(R.id.imageView2);
        imageView2.getLayoutParams().height = 400;

        group0 = findViewById(R.id.rbGroup0);
        group1 = findViewById(R.id.rbGroup1);

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
                Toast.makeText(MatchFacesActivity.this, "Having both images are compulsory", Toast.LENGTH_SHORT).show();
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


    @SuppressLint("NonConstantResourceId")
    private void showMenu(ImageView imageView, int i) {
        PopupMenu popupMenu = new PopupMenu(MatchFacesActivity.this, imageView);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.gallery:
                    openGallery(i);
                    return true;
                case R.id.camera:
                    RadioGroup radioGroup;
                    if(i == PICK_IMAGE_1)
                        radioGroup = group0;
                    else //if PICK_IMAGE_2
                        radioGroup = group1;
                    startFaceCaptureActivity(imageView, radioGroup);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.show();
    }

    private Bitmap getImageBitmap(ImageView imageView) {
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    private void openGallery(int id) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, id);
    }

    private void startFaceCaptureActivity(ImageView imageView, RadioGroup group) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build();

        FaceSDK.Instance().presentFaceCaptureActivity(MatchFacesActivity.this, configuration, faceCaptureResponse -> {
            if (faceCaptureResponse.getImage() == null)
                return;

            imageView.setImageBitmap(faceCaptureResponse.getImage().getBitmap());

            setGroupSelection(group, ImageType.LIVE);
        });
    }

    private void setGroupSelection(RadioGroup group, ImageType type) {
        switch (type){
            case LIVE:
                group.check(group.getChildAt(2).getId());
                break;
            case RFID:
                group.check(group.getChildAt(1).getId());
                break;
            case PRINTED:
                group.check(group.getChildAt(0).getId());
                break;
            case EXTERNAL:
                group.check(group.getChildAt(4).getId());
                break;
            case DOCUMENT_WITH_LIVE:
                group.check(group.getChildAt(3).getId());
                break;
            case GHOST_PORTRAIT:
                group.check(group.getChildAt(5).getId());
                break;
        }
    }

    private ImageType getGroupSelection(RadioGroup group){
        RadioButton button = group.findViewById(group.getCheckedRadioButtonId());
        int index = group.indexOfChild(button);
        switch (index){
            case 2:
                return ImageType.LIVE;
            case 1:
                return ImageType.RFID;
            case 0:
                return ImageType.PRINTED;
            case 4:
                return ImageType.EXTERNAL;
            case 3:
                return ImageType.DOCUMENT_WITH_LIVE;
            case 5:
                return ImageType.GHOST_PORTRAIT;
        }
        return ImageType.PRINTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        imageUri = data.getData();
        textViewSimilarity.setText("Similarity: null");

        ImageView imageView = null;
        RadioGroup group = null;

        if (requestCode == PICK_IMAGE_1) {
            imageView = imageView1;
            group = group0;
        }
        else if (requestCode == PICK_IMAGE_2) {
            imageView = imageView2;
            group = group1;
        }

        if (imageView == null)
            return;

        imageView.setImageURI(imageUri);

        setGroupSelection(group, ImageType.PRINTED);
    }

    private void matchFaces(Bitmap first, Bitmap second) {
        List<MatchFacesImage> imageList = new ArrayList<>();
        imageList.add(new MatchFacesImage(first, getGroupSelection(group0), true));
        imageList.add(new MatchFacesImage(second, getGroupSelection(group1), true));
        MatchFacesRequest matchRequest = new MatchFacesRequest(imageList);

        FaceSDK.Instance().matchFaces(matchRequest, matchFacesResponse -> {
            MatchFacesSimilarityThresholdSplit  split =
                    new MatchFacesSimilarityThresholdSplit(matchFacesResponse.getResults(), 0.75d);
            Double similarity = null;
            if (split.getMatchedFaces().size() > 0) {
                similarity = split.getMatchedFaces().get(0).getSimilarity();
            } else if (split.getUnmatchedFaces().size() > 0) {
                similarity = split.getUnmatchedFaces().get(0).getSimilarity();
            }

            if (similarity != null)
                textViewSimilarity.setText("Similarity: " + String.format("%.2f", similarity * 100) + "%");
            else
                textViewSimilarity.setText("Similarity: null");

            buttonMatch.setEnabled(true);
            buttonLiveness.setEnabled(true);
            buttonClear.setEnabled(true);
        });
    }

    private void startLiveness() {

        FaceSDK.Instance().startLiveness(MatchFacesActivity.this, livenessResponse -> {
            if (livenessResponse.getBitmap() != null) {
                imageView1.setImageBitmap(livenessResponse.getBitmap());
                imageView1.setTag(ImageType.LIVE);

                if (livenessResponse.getLiveness() == LivenessStatus.PASSED) {
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