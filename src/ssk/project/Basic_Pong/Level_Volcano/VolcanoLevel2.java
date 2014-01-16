package ssk.project.Basic_Pong.Level_Volcano;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Ball;
import android.content.Context;
import android.graphics.Canvas;


public class VolcanoLevel2 extends VolcanoLevel1 {

	Ball b3;
	boolean ball3Start = false;
	
	public VolcanoLevel2(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setVolcanoLevel(70);
	}
	
	@Override
	public void levelEvent() {
		super.levelEvent();
		if (ballHits == 8) {
			b3 = new Ball(context, b, playSound);
			ball3Start = true;
		}
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 70) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ball3Start) {
			if (b3.y > (b3.screenH - b3.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	@Override
	public void updateBall(Canvas canvas) {
		super.updateBall(canvas);
		if (ball3Start) {
			b3.update2(canvas, b);
			if (b3.bouncePaddle2(p, b)) {
				ballHits++;
				if (playSound) {
					soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
				}
			}
			if (b3.bounceWall() && playSound) {
				soundPool.play(wallSfx, 1, 1, 1, 0, 1);
			}
		}
	}
	
}