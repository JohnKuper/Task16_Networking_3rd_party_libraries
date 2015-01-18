package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Dmitriy Korobeynikov on 18.01.2015.
 * Provides auxiliary operations with views.
 */
public class ViewsUtils {

    private static Toast mToast;

    public static boolean isEditTextEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    public static void showToast(Context context, String text, int duration) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }
}
