package com.example.launchingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ProgressDialog progressDialog;
    TextView textView, avgTextView, pBar;
    ListView listView;
    SeekBar seekBar;
    ProgressBar progressBar;
    int r;
    Button button;
    ExecutorService executorService;
    ArrayAdapter arrayAdapter;
    HeavyWork heavyWork = new HeavyWork();
    private  int proStatBar = 1;
    ArrayList<String> arrayList = new ArrayList<>();
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView = findViewById(R.id.numTimesTextView);
        pBar = findViewById(R.id.textViewMax);
        seekBar = findViewById(R.id.seekBar);
        progressBar = findViewById(R.id.progressBarMax);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int n = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int number, boolean b) {
               textView.setText(number + " Times");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
        }
        });
        button = (Button) findViewById(R.id.generateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = seekBar.getProgress();
                progressBar.setProgress(r);

                if (r !=0){
                    executorService = Executors.newFixedThreadPool(r);
                    executorService.execute(new DoHeavyWork());
                }else{
                    Toast.makeText(getApplicationContext(), "Move the seek bar!!", Toast.LENGTH_LONG).show();
                }

              progressBar.setMax(r);

                int count = r;
              if (proStatBar <= count ) {
                  pBar.setText(proStatBar +"/"+ r);
                  progressBar.setProgress(proStatBar);
                  proStatBar += 1;
                  count++;
              }else{
                  Toast.makeText(getApplicationContext(), "Completed!!", Toast.LENGTH_LONG).show();
                  button.setEnabled(false);
                  seekBar.setEnabled(false);
              }

            }
        });



    handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case DoHeavyWork.STATUS_START:
                    Log.d("Demo", "Starting .....");
                    break;
                case DoHeavyWork.STATUS_STOP:
                    Log.d("Demo", "Stopping .....");
                    break;
                case DoHeavyWork.STATUS_PROGRESS:

                    arrayList.add(String.valueOf(message.getData().getDouble(DoHeavyWork.PROGRESS_KEY)));

                 double t = (double) message.getData().getDouble(DoHeavyWork.PROGRESS_KEY);
                    ArrayList<Double> list = new ArrayList<>();
                    list.add(t);
                   double r = calculateAverage(list);

                    TextView tv = findViewById(R.id.textViewAvg);
                    tv.setText(String.valueOf(r));

                    Log.d("Demo", "Progress ....." + message.getData().getDouble(DoHeavyWork.PROGRESS_KEY));
                    break;
            }


            listView = findViewById(R.id.listiView);
            arrayAdapter = new ArrayAdapter<>(MainActivity.this , android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);

            return false;
        }

    });

    }

    class DoHeavyWork implements Runnable{
        static final int STATUS_START = 0x00;
        static final int STATUS_PROGRESS = 0x01;
        static final int STATUS_STOP = 0x02;
        static final String PROGRESS_KEY = "PROGRESS";

        @Override
        public void run() {

            Message message = new Message();
            message.what = STATUS_PROGRESS;
            Bundle progress = new Bundle();
            progress.putDouble(PROGRESS_KEY, HeavyWork.getNumber());
            message.setData(progress);
            handler.sendMessage(message);
        }
    }

    private double calculateAverage(ArrayList <Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}