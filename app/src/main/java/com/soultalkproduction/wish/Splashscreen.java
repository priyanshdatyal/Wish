package com.soultalkproduction.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.soultalkproduction.wish.databinding.ActivitySplashscreenBinding;

public class Splashscreen extends AppCompatActivity {

    ActivitySplashscreenBinding binding;
    Animation ani_alp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ani_alp = AnimationUtils.loadAnimation(this,R.anim.alpha_amim);
        binding.log.setAnimation(ani_alp);
        binding.lis.setAnimation(ani_alp);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run(){
                Intent home = new Intent(Splashscreen.this, MainActivity.class);
                startActivity(home);
                finish();
            }
        },4500);
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(4000);
        v.startAnimation(anim);
    }

}