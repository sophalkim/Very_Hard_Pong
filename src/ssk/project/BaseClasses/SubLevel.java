package ssk.project.BaseClasses;

import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.PowerUp;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class SubLevel extends BaseLevel {

	Ball b2;
	PowerUp pu;
	boolean ballStart = false;
	
	public SubLevel(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		pu = new PowerUp(this, context, screenW, screenH);
	}
	
	public synchronized boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		pu.onClick(event);
		return true;
	}
	
	public void levelEvent() {
		if (ballHits == 3 && pu.isAvailable()) {
			pu.show();			
		}
		if (ballHits == 4) {
			b2 = new Ball(context, b);
			ballStart = true;
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ballStart) {
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
		if (ballStart) {
			b2.update2(canvas, b);
			if (b2.bouncePaddle2(p, b)) {
				ballHits++;
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		levelEvent();
		pu.update(canvas, p);
	}
}