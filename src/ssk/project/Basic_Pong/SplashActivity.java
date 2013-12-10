package ssk.project.Basic_Pong;

import ssk.project.Pong_Basic.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this, StartActivity.class);
				startActivity(i);
				finish();
			}
		}, 250);
	}
}
