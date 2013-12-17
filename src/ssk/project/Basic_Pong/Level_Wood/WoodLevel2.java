package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Lightning;
import ssk.project.GameUnits.PowerUpSlow;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;


public class WoodLevel2 extends WoodLevel1 {

	Lightning li2;
	PowerUpSlow pus;
	
	public WoodLevel2(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		li2 = new Lightning(li);
		pus = new PowerUpSlow(this, context, screenW, screenH);
	}
	
	public synchronized boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		pus.onClick(event);
		return true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		li2.update2(canvas, p, li);
		levelEvent();
		pus.update(canvas, li2);
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (li2.collidePaddle(p)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
	public void levelEvent() {
		if (ballHits == 3 && pus.isAvailable()) {
			pus.show();			
		}
	}
}