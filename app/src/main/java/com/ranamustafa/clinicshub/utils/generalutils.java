package com.ranamustafa.clinicshub.utils;

import android.content.Context;
import android.content.Intent;

import com.ranamustafa.clinicshub.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.ranamustafa.clinicshub.ui.main.MainActivity.returnhome;

public class generalutils {
    public static String getcurrentTimeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        String current = sdf.format(new Date());
    return  current  ;
    }

    public static void logout(Context context) {
        new SessionManager(context).clearSession();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
