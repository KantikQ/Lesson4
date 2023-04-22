package ru.mirea.zhurin.d.r.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int MSG_CALCULATE = 1;
    private EditText editTextAge, editTextJob;
    private Handler mMainThreadHandler;
    private CalculationHandler mCalculationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainThreadHandler = new Handler(Looper.getMainLooper());

        editTextAge = findViewById(R.id.editTextAge);
        editTextJob = findViewById(R.id.editTextJob);

        Button calculateButton = findViewById(R.id.buttonMirea);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = Integer.parseInt(editTextAge.getText().toString());
                String profession = editTextJob.getText().toString();
                Message calculationMessage = Message.obtain();
                calculationMessage.what = MSG_CALCULATE;
                Bundle calculationData = new Bundle();
                calculationData.putInt("age", age);
                calculationData.putString("profession", profession);
                calculationMessage.setData(calculationData);
                mCalculationHandler.sendMessage(calculationMessage);
            }
        });

        HandlerThread calculationThread = new HandlerThread("CalculationThread");
        calculationThread.start();
        mCalculationHandler = new CalculationHandler(calculationThread.getLooper());
    }

    private class CalculationHandler extends Handler {
        public CalculationHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CALCULATE) {
                Bundle calculationData = msg.getData();
                int age = calculationData.getInt("age");
                String profession = calculationData.getString("profession");
                try {
                    Thread.sleep(age * 1000);
                    String result = "Вы работаете " + profession + " уже " + age + " лет!";
                    Message resultMessage = Message.obtain();
                    Bundle resultData = new Bundle();
                    resultData.putString("result", result);
                    resultMessage.setData(resultData);
                    mMainThreadHandler.sendMessage(resultMessage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
