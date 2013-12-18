package ssk.project.Basic_Pong.Level_Wood;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.Lightning;
import ssk.project.GameUnits.WarningText;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class WoodLevel1 extends BaseLevel {

	Lightning li;
	WarningText wt;
	
	public WoodLevel1(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood_flooring_background);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		wt = new WarningText(screenW, screenH);
		li = new Lightning(this, context, screenW, screenH, playSound);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		wt.updateText(canvas);
		li.update(canvas, p);
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