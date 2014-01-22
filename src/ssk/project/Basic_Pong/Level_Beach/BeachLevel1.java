package ssk.project.Basic_Pong.Level_Beach;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.Paddle;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;


public class BeachLevel1 extends BaseLevel {

	public BeachLevel1(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beach2);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		p.setPaddleBitmap(this, Paddle.BEACH);
		b.setBallBitmap(this, Ball.BEACH_BALL);
		gt.setBeachLevel();
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 2) {
			thread.setRunning(false);
			pause = true;
			savePreferences("iceLock2", false);
			savePreferences("iceLock3", false);
			savePreferences("iceLock4", false);
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	public void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
}