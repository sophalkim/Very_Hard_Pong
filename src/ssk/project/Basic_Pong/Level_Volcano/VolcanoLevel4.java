package ssk.project.Basic_Pong.Level_Volcano;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Ball;
import android.content.Context;
import android.graphics.Canvas;


public class VolcanoLevel4 extends VolcanoLevel3 {

	Ball b5;
	boolean ball5Start = false;
	
	public VolcanoLevel4(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setVolcanoLevel(250);
	}
	
	@Override
	public void levelEvent() {
		super.levelEvent();
		if (ballHits == 20) {
			b5 = new Ball(b);
			ball5Start = true;
		}
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 250) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ball5Start) {
			if (b5.y > (b5.screenH - b5.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	@Override
	public void updateBall(Canvas canvas) {
		super.updateBall(canvas);
		if (ball5Start) {
			b5.update2(canvas, b);
			if (b5.bouncePaddle2(p, b)) {
				ballHits++;
			}
		}
	}
	
}