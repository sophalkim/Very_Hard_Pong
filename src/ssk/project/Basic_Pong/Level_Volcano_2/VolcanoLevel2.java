package ssk.project.Basic_Pong.Level_Volcano_2;
import ssk.project.BaseClasses.BaseActivity;
import ssk.project.Basic_Pong.Level_Volcano.VolcanoLevel1;
import ssk.project.GameUnits.Ball;
import android.content.Context;
import android.graphics.Canvas;


public class VolcanoLevel2 extends VolcanoLevel1 {

	Ball b3;
	boolean ball3Start = false;
	
	public VolcanoLevel2(Context context) {
		super(context);
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setVolcanoLevel(70);
	}
	
	@Override
	public void levelEvent() {
		super.levelEvent();
		if (ballHits == 8) {
			b3 = new Ball(b);
			ball3Start = true;
		}
	}
	
	@Override
	public void winCondition() {
		if (ballHits == 70) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
	@Override
	public void loseCondition() {
		super.loseCondition();
		if (ball3Start) {
			if (b3.y > (b3.screenH - b3.h)) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).loseScreen();
			}
		}
	}
	
	@Override
	public void updateBall(Canvas canvas) {
		super.updateBall(canvas);
		if (ball3Start) {
			b3.update2(canvas, b);
			if (b3.bouncePaddle2(p, b)) {
				ballHits++;
			}
		}
	}
	
}