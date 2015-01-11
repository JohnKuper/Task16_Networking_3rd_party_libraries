package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Dmitriy Korobeynikov on 11.01.2015.
 * Simple class for preventing accumulation of toasts.
 */
public class SingleToast {

    private static Toast mToast;

    public static void show(Context context, String text, int duration) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }
}
