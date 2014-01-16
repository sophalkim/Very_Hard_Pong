package ssk.project.Basic_Pong.Level_Volcano;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.Paddle;
import android.content.Context;
import android.graphics.Canvas;


public class VolcanoLevel1 extends BaseLevel {

	Ball b2;
	boolean ball2Start = false;
	
	public VolcanoLevel1(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setVolcanoLevel(30);
		p.setPaddleBitmap(this, Paddle.VOLCANO);
	}
	
	public void levelEvent() {
		if (ballHits == 4) {
			b2 = new Ball(context, b, playSound);
			ball2Start = true;
		}
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 30) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ball2Start) {
			if (b2.y > (b2.screenH - b2.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	@Override
	public void updateBall(Canvas canvas) {
		super.updateBall(canvas);
		if (ball2Start) {
			b2.update2(canvas, b);
			if (b2.bouncePaddle2(p, b)) {
				ballHits++;
				if (playSound) {
					soundPool.play(paddleSfx, 1, 1, 1, 0, 1);
				}
			}
			if (b2.bounceWall() && playSound) {
				soundPool.play(wallSfx, 1, 1, 1, 0, 1);
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		levelEvent();
	}
	
}