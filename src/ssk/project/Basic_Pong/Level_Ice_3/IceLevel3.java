package ssk.project.Basic_Pong.Level_Ice_3;
import ssk.project.Basic_Pong.Level_Ice_2.IceLevel2;
import ssk.project.Basic_Pong.Modular.BaseActivity;
import ssk.project.GameUnits.GreenBar;
import ssk.project.GameUnits.IceBlock;
import android.content.Context;
import android.graphics.Canvas;


public class IceLevel3 extends IceLevel2 {

	public GreenBar gb;
	int amount = 24;
	
	public IceLevel3(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		for (int i = 0; i < 8; i++) {
			IceBlock ice = new IceBlock(ib, i * ib.w + 10, screenH / 2 - (ib.h * 2));
			iceBlocks.add(ice);
		}
		gb = new GreenBar(this, context, screenW, screenH);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		gb.update(canvas, b);
	}
	
	@Override
	public void winCondition() {
		if (iceHit == amount * 2) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
}