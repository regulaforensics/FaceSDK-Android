package com.regula.facesample.items;

import android.content.Context;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public abstract class CategoryItem {
    public abstract void onItemSelected(Context context);
    public abstract String getTitle();
    public abstract String getDescription();
}
