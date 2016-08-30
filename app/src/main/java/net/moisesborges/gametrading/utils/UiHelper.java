package net.moisesborges.gametrading.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import net.moisesborges.gametrading.R;

/**
 * Created by Moisés on 29/08/2016.
 */

public class UiHelper {

    public static ProgressDialog createProgressDialog(@NonNull Context context,
                                                      @StringRes int msgRes) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        String message = context.getResources().getString(msgRes);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        return progressDialog;
    }
}
