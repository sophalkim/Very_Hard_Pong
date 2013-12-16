package ssk.project.Basic_Pong.Level_Ice_4;
import ssk.project.Basic_Pong.Level_Ice_3.IceLevel3;
import ssk.project.Basic_Pong.Modular.BaseActivity;
import ssk.project.GameUnits.GreenBar;
import ssk.project.GameUnits.IceBlock;
import android.content.Context;
import android.graphics.Canvas;


public class IceLevel4 extends IceLevel3 {

	GreenBar gb2;
	int iceBlockAmount = 32;
	
	public IceLevel4(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		for (int i = 0; i < 8; i++) {
			IceBlock ice = new IceBlock(ib, i * ib.w + 10, screenH / 2 - (ib.h * 3));
			iceBlocks.add(ice);
		}
		gb2 = new GreenBar(gb);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		gb2.update2(canvas, b, gb);
	}
	
	@Override
	public void winCondition() {
		if (iceHit == iceBlockAmount * 2) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
}