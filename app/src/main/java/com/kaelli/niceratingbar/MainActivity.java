package com.kaelli.niceratingbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by KaelLi on 2019/11/19.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NiceRatingBar mNiceRatingBar = findViewById(R.id.niceRatingBar);

        mNiceRatingBar.setOnRatingChangedListener(new OnRatingChangedListener() {
                    @Override
                    public void onRatingChanged(float rating) {
                        // 在这里可以将用户的评分上传给服务端
                    }
                });
    }
}
