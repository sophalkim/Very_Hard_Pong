package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Lightning;
import android.content.Context;
import android.graphics.Canvas;


public class WoodLevel3 extends WoodLevel2 {

	Lightning li3;
	
	public WoodLevel3(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		li3 = new Lightning(li, context, 3);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		li2.update3(canvas, p, li);
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