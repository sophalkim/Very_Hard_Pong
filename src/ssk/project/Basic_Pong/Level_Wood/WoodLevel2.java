package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.Lightning2;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Canvas;


public class WoodLevel2 extends WoodLevel1 {

	Lightning2 li2;
	int lightningSfx2;
	
	public WoodLevel2(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		li2 = new Lightning2(this, context, screenW, screenH, playSound);
		lightningSfx2 = soundPool.load(context, R.raw.lightning_sound_effect2, 1);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		li2.update(canvas, p, soundPool, lightningSfx2);
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