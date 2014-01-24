package ssk.project.Basic_Pong.Level_Volcano;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.PowerUp;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;


public class VolcanoLevel4 extends VolcanoLevel3 {

	Ball b5;
	PowerUp pu;
	boolean ball5Start = false;
	
	public VolcanoLevel4(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setGoal(250);
		pu = new PowerUp(this, context, screenW, screenH, playSound);
	}
	
	public synchronized boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		pu.onClick(event);
		return true;
	}
	
	@Override
	public void levelEvent() {
		super.levelEvent();
		if (ballHits == 3 && pu.isAvailable()) {
			pu.show();			
		}
		if (ballHits == 20) {
			b5 = new Ball(context, b, playSound);
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
				if (playSound && soundPool != null) {
					soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
				}
			}
			if (b5.bounceWall() && playSound && soundPool != null) {
				soundPool.play(wallSfx, 1, 1, 1, 0, 1);
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		pu.update(canvas, p);
	}
}