package ssk.project.Basic_Pong.Level_Beach;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.Paddle;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;


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
		if (ballHits == 1000) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
}