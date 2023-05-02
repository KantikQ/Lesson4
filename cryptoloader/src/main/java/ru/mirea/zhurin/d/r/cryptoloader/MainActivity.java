package ru.mirea.zhurin.d.r.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rmz.cryptoutil.CryptoUtil;

import java.security.InvalidParameterException;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {
    public final String TAG = this.getClass().getSimpleName();

    private EditText editText;
    private Button button;
    private final int LoaderID = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
    }
    public void onClickButton(View view) {
        String input = editText.getText().toString();
        if (!input.isEmpty()) {
            try {
                String encrypted = CryptoUtil.encrypt(input);
                Bundle bundle = new Bundle();
                bundle.putString(MyLoader.ARG_WORD, encrypted);
                LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Введите фразу", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == LoaderID) {
            Toast.makeText(this, "onCreateLoader:" + i, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        throw new InvalidParameterException("Invalid loader id");
    }
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "onLoadFinished: " + s);
            try {
                String encryptedString = AESCrypt.encrypt("password", s);
                Log.d(TAG, "Encrypted string: " + encryptedString);

                Bundle resultBundle = new Bundle();
                resultBundle.putString("result", encryptedString);

                LoaderManager.getInstance(this).initLoader(LoaderID+1, resultBundle, this);

            } catch (Exception e) {
                Log.e(TAG, "Encryption failed", e);
            }
        } else if (loader.getId() == LoaderID+1) {
            String encryptedString = s;
            try {
                String decryptedString = AESCrypt.decrypt("password", encryptedString);
                Log.d(TAG, "Decrypted string: " + decryptedString);

                Toast.makeText(this, "Decrypted string: " + decryptedString, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e(TAG, "Decryption failed", e);
            }
        }
    }
}
