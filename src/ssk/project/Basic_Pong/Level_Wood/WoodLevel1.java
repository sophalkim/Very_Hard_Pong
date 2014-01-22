package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.Lightning;
import ssk.project.GameUnits.Paddle;
import ssk.project.GameUnits.ScrollingBackground2;
import ssk.project.GameUnits.WarningText;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class WoodLevel1 extends BaseLevel {

	Lightning li;
	WarningText wt;
	ScrollingBackground2 scrollB;
	public int lightningSfx;
	
	public WoodLevel1(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood_background_long);
		lightningSfx = soundPool.load(context, R.raw.lightning_sound_effect, 1);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		p.setPaddleBitmap(this, Paddle.WOOD);
		wt = new WarningText(screenW, screenH);
		li = new Lightning(this, context, screenW, screenH, playSound);
		scrollB = new ScrollingBackground2(bgBitmap, w, h);
	}
	
	@Override
	public void draw(Canvas canvas) {
		scrollB.update(canvas);
		winCondition();
		loseCondition();
		gt.updateText(canvas, screenW, screenH, ballHits);
		p.update(canvas);
		updateBall(canvas);
		wt.updateText(canvas);
		li.update(canvas, p, soundPool, lightningSfx);
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (li.collidePaddle(p)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
		}
	}
	
}