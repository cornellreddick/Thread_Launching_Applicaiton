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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ProgressDialog progressDialog;
    TextView textView;
    ListView listView;
    SeekBar seekBar;
    Button button;
    ExecutorService executorService;
    ArrayAdapter arrayAdapter;
    HeavyWork heavyWork = new HeavyWork();

    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executorService = Executors.newFixedThreadPool(10);

        textView = findViewById(R.id.numTimesTextView);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int number, boolean b) {
                textView.setText(number +" " + "Times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayList list = new ArrayList();
        findViewById(R.id.generateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executorService.execute(new DoHeavyWork());
            }
        });



//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Update Progress");
//        progressDialog.setMax(100);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setCancelable(false);


    handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case DoHeavyWork.STATUS_START:
//                    progressDialog.setProgress(0);
//                    progressDialog.show();


                    Log.d("Demo", "Starting ....."+ message.getData().getDouble(DoHeavyWork.PROGRESS_KEY));
                    break;
                case DoHeavyWork.STATUS_STOP:
                   // progressDialog.dismiss();
                    Log.d("Demo", "Stopping ....."+ message.getData().getDouble(DoHeavyWork.PROGRESS_KEY));
                    break;
                case DoHeavyWork.STATUS_PROGRESS:
                   // progressDialog.setProgress(message.getData().getInt(DoHeavyWork.PROGRESS_KEY));
                    arrayList.add(String.valueOf(message.getData().getDouble(DoHeavyWork.PROGRESS_KEY)));
                    int result = arrayList.size();
                    Log.d("Size", "handleMessage:" + result);
                    Log.d("Demo", "Progress ....." + message.getData().getDouble(DoHeavyWork.PROGRESS_KEY));
                    break;
            }

            listView = findViewById(R.id.listiView);
            arrayAdapter = new ArrayAdapter<>(MainActivity.this , android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);

            return false;
        }
    });

    new Thread(new DoHeavyWork()).start();

    }

    class DoHeavyWork implements Runnable{
        static final int STATUS_START = 0x00;
        static final int STATUS_PROGRESS = 0x01;
        static final int STATUS_STOP = 0x02;
        static final String PROGRESS_KEY = "PROGRESS";

        @Override
        public void run(){
//            Message startMsg = new Message();
//            startMsg.what = STATUS_START;
//            Bundle start = new Bundle();
//            start.putDouble(PROGRESS_KEY, HeavyWork.getNumber());
//            startMsg.setData(start);
//            handler.sendMessage(startMsg);
//

            Message message = new Message();
            message.what = STATUS_PROGRESS;
            Bundle progress = new Bundle();
            progress.putDouble(PROGRESS_KEY, HeavyWork.getNumber());
            message.setData(progress);
            handler.sendMessage(message);



//            Message stopMsg = new Message();
//            message.what = STATUS_STOP;
//            Bundle stop = new Bundle();
//            stop.putDouble(PROGRESS_KEY, HeavyWork.getNumber());
//            stopMsg.setData(stop);
//            handler.sendMessage(stopMsg);

//
//           // Log.d("Demo", "Started Work");
//            for (int i = 0; i < 100; i++) {
//                for (int j = 0; j < 100000000; j++) {
//                }
//                Message message = new Message();
//                message.what = i;
//                message.obj = (Integer)i;
//                handler.sendMessage(message);
//
//                Message message = new Message();
//                message.what = STATUS_PROGRESS;
//               // message.obj = (Integer)i;
//                Bundle bundle = new Bundle();
//                bundle.putInt(PROGRESS_KEY,(Integer)i );
//                message.setData(bundle);
//                handler.sendMessage(message);
//            }
//
//            Message stopMessage = new Message();
//            stopMessage.what = STATUS_STOP;
//            handler.sendMessage(stopMessage);
        }
    }

}