package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Lightning;
import ssk.project.GameUnits.PowerUp;
import ssk.project.GameUnits.ScrollingBackground;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;


public class WoodLevel3 extends WoodLevel2 {

	ScrollingBackground sbg;
	Lightning li3;
	PowerUp pu;
	
	public WoodLevel3(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		sbg = new ScrollingBackground(context, bgBitmap, screenW, screenH);
		li3 = new Lightning(li, context, 3, playSound);
		pu = new PowerUp(this, context, screenW, screenH, playSound);
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
	}
	
	@Override
	public void draw(Canvas canvas) {
		sbg.update(canvas);
		gt.updateText(canvas, screenW, screenH, ballHits);
		p.update(canvas);
		updateBall(canvas);
		winCondition();
		loseCondition();
		levelEvent();
		li2.update3(canvas, p, li);
		pu.updateShrink(canvas, p);
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
}