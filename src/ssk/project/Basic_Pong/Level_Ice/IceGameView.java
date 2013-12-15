package ssk.project.Basic_Pong.Level_Ice;
import java.util.ArrayList;
import java.util.List;

import ssk.project.Basic_Pong.Level_Ice.Objects.IceBlock;
import ssk.project.Basic_Pong.Level_Ice.Objects.SolidBlock;
import ssk.project.Basic_Pong.Modular.BaseLevel;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class IceGameView extends BaseLevel {
	
	int iceHit;
	List<IceBlock> iceBlocks = new ArrayList<IceBlock>();
	// Level Variables
	int ballHits = 0;
	int iceblockQuantity = 16;
	IceBlock ib;
	SolidBlock sb;
		
	public IceGameView(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ice_cave);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		ib = new IceBlock(this, context, screenW, screenH);
		for (int i = 0; i < (iceblockQuantity / 2); i++) {
			IceBlock ice = new IceBlock(ib, i * ib.w + 10, screenH / 2);
			iceBlocks.add(ice);
		}
		sb = new SolidBlock(this, context, screenW, screenH);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		ib.update(canvas, b);
		// Drawing IceBlocks
		for (int i = 0; i < iceBlocks.size(); i++) {
			iceBlocks.get(i).update2(canvas, b, ib);
		}		
		// Drawing SolidBlock
		sb.update(canvas, b);
	}
}