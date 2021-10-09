package com.regula.facesample.items.basic;

import android.content.Context;
import android.content.Intent;

import com.regula.facesample.MatchFacesActivity;
import com.regula.facesample.items.CategoryItem;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class MatchFacesRequestItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        Intent intent = new Intent(context, MatchFacesActivity.class);
        context.startActivity(intent);
    }

    @Override
    public String getTitle() {
        return "MatchFacesRequest";
    }

    @Override
    public String getDescription() {
        return "Checks the likelihood that two faces belong to the same person.";
    }
}
