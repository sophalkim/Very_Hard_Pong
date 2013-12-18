package ssk.project.Basic_Pong.Level_Ice;
import java.util.ArrayList;
import java.util.List;

import ssk.project.BaseClasses.BaseActivity;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.GameUnits.IceBlock;
import ssk.project.GameUnits.SolidBlock;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class IceLevel1 extends BaseLevel {
	
	public int iceHit = 0;
	public int iceblockQuantity = 8;
	public IceBlock ib;
	public SolidBlock sb;
	public List<IceBlock> iceBlocks = new ArrayList<IceBlock>();
		
	public IceLevel1(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ice_cave);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		gt.setIceLevel();
		ib = new IceBlock(this, context, screenW, screenH);
		for (int i = 0; i < (iceblockQuantity); i++) {
			IceBlock ice = new IceBlock(context, ib, i * ib.w + 10, screenH / 2);
			iceBlocks.add(ice);
		}
		sb = new SolidBlock(this, context, screenW, screenH);
	}
	
	public void updateIceBlocks(Canvas canvas) {
		for (int i = 0; i < iceBlocks.size(); i++) {
			iceBlocks.get(i).update2(canvas, ib);
		}
		for (int i = 0; i < iceBlocks.size(); i++) {
			if (iceBlocks.get(i).bounceBall2(b, ib)) {
				iceHit++;
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		updateIceBlocks(canvas);
		sb.update(canvas, b);
	}
	
	@Override
	public void winCondition() {
		if (iceHit == iceblockQuantity * 2) {
			thread.setRunning(false);
			pause = true;
			((BaseActivity)getContext()).winScreen();
		}
	}
}