package ssk.project.Basic_Pong;

import ssk.project.Pong_Basic.R;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
	
	TextView very, hard, pong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		very = (TextView) findViewById(R.id.tvVery);
		hard = (TextView) findViewById(R.id.tvHard);
		pong = (TextView) findViewById(R.id.tvPong);
		
		setAnimation();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, StartActivity.class);
				startActivity(i);
				finish();
			}
		}, 1500);
	}
	
	public void setAnimation() {
		final AnimatorSet veryAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in_fast);
		veryAnimation.setTarget(very);
		veryAnimation.start();
		
		final AnimatorSet hardAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in_medium);
		hardAnimation.setTarget(hard);
		hardAnimation.start();
		
		final AnimatorSet pongAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in_slow);
		pongAnimation.setTarget(pong);
		pongAnimation.start();
	}
}
