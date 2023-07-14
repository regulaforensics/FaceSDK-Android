package com.regula.facepersonsearch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.regula.facesdk.model.results.personDb.PersonImage;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class PersonImageFragment extends DialogFragment {

    List<PersonImage> images;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonImageFragment(List<PersonImage> images) {
        this.images = images;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater()
                .inflate(R.layout.fragment_item_list, null, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new PersonImageRecyclerViewAdapter(images));

        return new AlertDialog.Builder(requireContext())
                .setTitle("PERSON IMAGES: " + images.size())
                .setView(view)
                .setPositiveButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
    }
}