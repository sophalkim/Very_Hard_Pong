package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.GameUnits.LightningBig;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Canvas;


public class WoodLevel4 extends WoodLevel3 {

	LightningBig liBig;
	public int lightningBigSfx;
	
	public WoodLevel4(Context context) {
		super(context);
		lightningBigSfx = soundPool.load(context, R.raw.big_lightning_sound_effect, 1);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		liBig = new LightningBig(this, context, screenW, screenH, playSound);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		liBig.update(canvas, p, soundPool, lightningBigSfx);
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (liBig.collidePaddle(p)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
}