package ssk.project.Basic_Pong.Level_Ice_2;
import android.content.Context;
import android.graphics.Canvas;
import ssk.project.Basic_Pong.Level_Ice.IceLevel1;
import ssk.project.Basic_Pong.Modular.BaseActivity;
import ssk.project.GameUnits.IceBlock;
import ssk.project.GameUnits.SolidBlock;


public class IceLevel2 extends IceLevel1 {
	
	SolidBlock sb2;
	int quantity = 16;
	
	public IceLevel2(Context context) {
		super(context);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		for (int i = 0; i < quantity / 2; i++) {
			IceBlock ice = new IceBlock(ib, i * ib.w + 10, screenH / 2 - ib.h);
			iceBlocks.add(ice);
		}
		sb2 = new SolidBlock(sb);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		sb2.update2(canvas, b, sb);
	}
	
	@Override
	public void winCondition() {
		if (iceHit == quantity * 2) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
	
}