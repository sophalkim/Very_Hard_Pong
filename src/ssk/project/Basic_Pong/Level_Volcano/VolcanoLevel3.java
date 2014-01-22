package ssk.project.Basic_Pong.Level_Volcano;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Ball;
import android.content.Context;
import android.graphics.Canvas;


public class VolcanoLevel3 extends VolcanoLevel2 {

	Ball b4;
	boolean ball4Start = false;
	
	public VolcanoLevel3(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setVolcanoLevel(150);
	}
	
	@Override
	public void levelEvent() {
		super.levelEvent();
		if (ballHits == 14) {
			b4 = new Ball(context, b, playSound);
			ball4Start = true;
		}
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 150) {
			thread.setRunning(false);
			pause = true;
			savePreferences("volcanoLock4", false);
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ball4Start) {
			if (b4.y > (b4.screenH - b4.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	@Override
	public void updateBall(Canvas canvas) {
		super.updateBall(canvas);
		if (ball4Start) {
			b4.update2(canvas, b);
			if (b4.bouncePaddle2(p, b)) {
				ballHits++;
				if (playSound && soundPool != null) {
					soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
				}
			}
			if (b4.bounceWall() && playSound && soundPool != null) {
				soundPool.play(wallSfx, 1, 1, 1, 0, 1);
			}
		}
	}
	
}