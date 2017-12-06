package com.qinqi.debugtoolbox;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.qinqi.debugtoolbox.log.Logger;
import com.qinqi.debugtoolbox.manager.DebugToolBoxManager;

import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    //    private static final String URL = "http://xtaikan.m.jinerkan.com/v3/api/media/list?_d=1&page=1&count=20&sort=random";
    private static final String URL = "http://gank.io/api/data/Android/10/1";
    private static final String URL_POST = "https://mapi.biqu.panatrip.cn/1.1.4/biqu/advancedsearch2";

    private static final String URL_POST_2 = "https://mapi.biqu.panatrip.cn/1.1.3/bi/ordersd";

    private static final String LOG_TEST = "{\"code\":\"200\",\"desc\":\"\",\"list\":[{\"arriveAir port\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"CA3202\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T08:50:00\",\"depDateTime\":\"2016-11-25T06:35:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"MU3924\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T08:50:00\",\"depDateTime\":\"2016-11-25T06:35:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"HO1252\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T08:50:00\",\"depDateTime\":\"2016-11-25T06:35:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MF4061\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:00:00\",\"depDateTime\":\"2016-11-25T06:40:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MU3662\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:00:00\",\"depDateTime\":\"2016-11-25T06:40:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"CZ6412\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:00:00\",\"depDateTime\":\"2016-11-25T06:40:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MU5138\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:15:00\",\"depDateTime\":\"2016-11-25T07:00:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"CZ9271\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:15:00\",\"depDateTime\":\"2016-11-25T07:00:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T1\",\"flightNo\":\"HU7607\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:35:00\",\"depDateTime\":\"2016-11-25T07:25:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"ZH1831\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:40:00\",\"depDateTime\":\"2016-11-25T07:30:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"CA1831\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T09:40:00\",\"depDateTime\":\"2016-11-25T07:30:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MU5102\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:15:00\",\"depDateTime\":\"2016-11-25T08:00:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"CZ9235\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:15:00\",\"depDateTime\":\"2016-11-25T08:00:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"FM9108\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:50:00\",\"depDateTime\":\"2016-11-25T08:25:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MU9108\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:50:00\",\"depDateTime\":\"2016-11-25T08:25:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"HA3822\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:40:00\",\"depDateTime\":\"2016-11-25T08:30:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"CA1501\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:40:00\",\"depDateTime\":\"2016-11-25T08:30:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T3\",\"flightNo\":\"ZH1501\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:40:00\",\"depDateTime\":\"2016-11-25T08:30:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"NS8178\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:45:00\",\"depDateTime\":\"2016-11-25T08:35:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"MU8571\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:45:00\",\"depDateTime\":\"2016-11-25T08:35:00\"},{\"arriveAirport\":\"SHA\",\"departureTerminal\":\"T2\",\"flightNo\":\"CZ5116\",\"departureAirport\":\"PEK\",\"arriveTerminal\":\"T2\",\"arrDateTime\":\"2016-11-25T10:45:00\",\"depDateTime\":\"2016-11-25T08:35:00\"}]}";

    private Button mOpenBtn;
    private Button mLeakBtn;
    private Button mThreadWaitBtn;
    private Button mIoBlockBtn;
    private Button mComputationBtn;
    private Button mSendRequestBtn;
    private Button mSendPostrequestBtn;
    private Button mCrashBtn;
    private Button mSaveDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }

    private void initView() {
        mOpenBtn = (Button) findViewById(R.id.btn_open);
        mLeakBtn = (Button) findViewById(R.id.btn_leak);
        mThreadWaitBtn = (Button) findViewById(R.id.btn_thread_wait);
        mIoBlockBtn = (Button) findViewById(R.id.btn_io_block);
        mComputationBtn = (Button) findViewById(R.id.btn_computation);
        mSendRequestBtn = (Button) findViewById(R.id.btn_send_request);
        mSendPostrequestBtn = (Button) findViewById(R.id.btn_send_post);
        mCrashBtn = (Button) findViewById(R.id.btn_crash);
        mSaveDataBtn = (Button) findViewById(R.id.btn_save_data);
    }

    private void setListener() {
        mOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d("MainActivity", "{\"alias\":\"462B262EB8E1868F7627D9A8C7BF2BB749EF4211\",\"t\":\"f3c601d7\",\"tm\":\"1494408284097\",\"v\":\"107cf7ad4a1c3d46371aa436ad26678c\"}', state=3}");
                Logger.e("MainActivity", "myLog E");
                Logger.i("MainActivity", "myLog I");
                Logger.v("MainActivity", "myLog V");
                Logger.w("MainActivity", "myLog W");
//
                Logger.d("MainActivity", LOG_TEST);
                DebugToolBoxManager.openDebugToolBox(MainActivity.this);
                finish();
            }
        });

        mLeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeakClick();
            }
        });

        mThreadWaitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onThreadWaitClick();
            }
        });

        mIoBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 100; ++i) {
                    onIoBlockClick();
                }
            }
        });

        mComputationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double result = onComputationClick();
                System.out.println(result);
            }
        });

        mSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendRequestClick();
            }
        });

        mSendPostrequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendPostRequestClick();
            }
        });

        mCrashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCrashClick();
            }
        });

        mSaveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveDataClick();
            }
        });
    }

    private void onLeakClick() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(20000);
                return null;
            }
        }.execute();
    }

    private void onThreadWaitClick() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void onIoBlockClick() {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream("/proc/stat");
            while (reader.read() != -1) ;
        } catch (IOException e) {
            //Logger.e(TAG, "readFile: /proc/stat", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //Logger.e(TAG, " on close reader ", e);
                }
            }
        }
    }

    private double onComputationClick() {
        double result = 0;
        for (int i = 0; i < 1000000; ++i) {
            result += Math.acos(Math.cos(i));
            result -= Math.asin(Math.sin(i));
        }
        return result;
    }

    private void onSendRequestClick() {
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return NetHelper.getData(URL);
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                Logger.i(TAG, "onSendRequestClick:result:" + s);
                return Observable.just(s);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.i(TAG, "onSendRequestClick:ok:" + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(TAG, "onSendRequestClick:error:" + throwable.toString());
                    }
                });
    }

    private void onSendPostRequestClick() {
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
//                String url = "https://dl.reg.163.com/l";
//                String data = "{\"un\":\"xxx@163.com\",\"pw\":\"jLg5uZJBTn70Q9kF9gehA6eDeORO6hrrZovUnoie0nEZEumiBQnzlPsyHAwNqLhJB+e0z8eL7r9m/MMAIP2dw3R91dBr8HdCex0GCszEgCenWHa3kro1ljA7EwN6u4XX0WtFcENaKVQaZun9kzoDiEYjXDx7n3SCAfatzToxijo=\",\"pd\":\"163\",\"l\":0,\"d\":10,\"t\":1479802023426,\"pkid\":\"MODXOXd\",\"domains\":\"163.com\",\"tk\":\"b236ddd17aa24fc7e578e66c1af80826\",\"pwdKeyUp\":1}";
                RequestBody body = new FormBody.Builder()
                        .add("hour", "true")
                        .add("today", "")
                        .add("start", "2016-12-10 11:11:00")
                        .add("end", "2016-12-12 11:11:00")
                        .build();

                return NetHelper.postDataByRequestBody(URL_POST_2, body);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.i(TAG, "onSendRequestClick:ok:" + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.e(TAG, "onSendRequestClick:error:" + throwable.toString());
                    }
                });
    }

    private void onCrashClick() {
        throw new NullPointerException("crash");
    }

    private void onSaveDataClick() {
        LocalDataStorage storage = new LocalDataStorage("test", this);
        storage.setIntProperty("one", 1);
        storage.setStringProperty("two", "2");
    }
}