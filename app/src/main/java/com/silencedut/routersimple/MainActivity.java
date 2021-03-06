package com.silencedut.routersimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.silencedut.router.Annotation.RunThread;
import com.silencedut.router.Annotation.Subscribe;
import com.silencedut.router.Router;


public class MainActivity extends AppCompatActivity
        implements Event.MainView
        ,Event.TestMultiReceivers
        ,View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mMainTv;
    private TextView mPostingTv;
    private TextView mBackgroundTv;
    private TextView mAsyncTv;
    private TextView mTopFragmentTv;
    private TextView mBottomFragmentTv;
    private MainPresenter mMainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainTv = (TextView) findViewById(R.id.main_text);
        mPostingTv = (TextView) findViewById(R.id.posting_text);
        mBackgroundTv = (TextView) findViewById(R.id.background_text);
        mAsyncTv = (TextView) findViewById(R.id.async_text);

        mTopFragmentTv = (TextView)findViewById(R.id.top_tv);
        mBottomFragmentTv = (TextView)findViewById(R.id.bottom_tv);

        Router.getInstance().register(this);

        mMainPresenter = new MainPresenter();

        findViewById(R.id.left_bt).setOnClickListener(this);
        findViewById(R.id.right_bt).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Router.getInstance().unregister(this);
    }

    @Override
    public void textMainThread(String msg)  {
        mMainTv.setText(msg+",currentThread : "+Thread.currentThread().getName());
    }


    @Override
    @Subscribe(runThread = RunThread.POSTING)
    public void textPostThread(String msg) {
        final String testMsg = msg+",currentThread : "+Thread.currentThread().getName();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPostingTv.setText(testMsg);
            }
        });

    }


    @Override
    @Subscribe(runThread = RunThread.BACKGROUND)
    public void textBackgroundThread(String msg) {
        final String testMsg = msg+",currentThread : "+Thread.currentThread().getName();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBackgroundTv.setText(testMsg);
            }
        });
    }

    @Override
    @Subscribe(runThread = RunThread.ASYNC)
    public void textAsyncThread(String msg) {
        final String testMsg = msg+",currentThread : "+Thread.currentThread().getName();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAsyncTv.setText(testMsg);
            }
        });
    }

    @Override
    public void testMulti(String msg) {
        mMainTv.setText("Activity receive the msg : "+msg);
    }

    @Override
    public void onClick(View v) {
        clear();
        switch (v.getId()) {
            case R.id.left_bt:
                mMainPresenter.testRunThread();
                break;
            case R.id.right_bt:
                mMainPresenter.testMutiReceivers();
                break;
        }
    }

    private void clear() {
        mMainTv.setText("");
        mPostingTv.setText("");
        mBackgroundTv.setText("");
        mAsyncTv.setText("");
        mTopFragmentTv.setText("");
        mBottomFragmentTv.setText("");
    }
}
