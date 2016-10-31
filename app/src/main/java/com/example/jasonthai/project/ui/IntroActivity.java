package com.example.jasonthai.project.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.example.jasonthai.project.R;

public class IntroActivity extends AppCompatActivity {

    final Animation in = new AlphaAnimation(0.0f, 1.0f);
    final Animation out = new AlphaAnimation(1.0f, 0.0f);

    AnimationSet as = new AnimationSet(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final TextView mSwitcher = (TextView) findViewById(R.id.intro);
        mSwitcher.setText(getResources().getString(R.string.intro));

        in.setDuration(3000);
        out.setDuration(3000);

        as.addAnimation(in);
        in.setStartOffset(3000);
        mSwitcher.startAnimation(as);

        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                as.addAnimation(out);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mSwitcher.setText("");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSwitcher.startAnimation(as);



    }
}
