package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Lightning;
import android.content.Context;
import android.graphics.Canvas;


public class WoodLevel4 extends WoodLevel3 {

	Lightning bigLi;
	
	public WoodLevel4(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		bigLi = new Lightning(li, context, "big");
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		bigLi.update4(canvas);
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (bigLi.collidePaddle(p)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
}