package ru.mirea.zhurin.d.r.cryptoloader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import com.rmz.cryptoutil.CryptoUtil;

public class MyLoader extends AsyncTaskLoader<String> {
    private String firstName;
    public static final String ARG_WORD = "word";

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null)
            firstName = args.getString(ARG_WORD);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            String decrypted = CryptoUtil.decrypt(firstName);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
