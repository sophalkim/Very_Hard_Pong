package ssk.project.Basic_Pong.Level_Ice.Objects;

import java.util.Random;

import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.GameUnit;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class SolidBlock extends GameUnit {
	
	public int solidBlockState = 1;
	public int solidBlockX;
	public int solidBlockY;
	public int solidBlockWidth;
	public int solidBlockHeight;
	
	public int solidSfx;
	
	long pTime = 0;
	long cTime;
	long dTime;
	
	Random r = new Random();
	
	public SolidBlock() {}
	
	public SolidBlock(View v, Context context, int screenW, int screenH) {
		this.screenW = screenW;
		this.screenH = screenH;
		solidSfx = soundPool.load(context, R.raw.solid_block_sound_effect, 1);
		bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.solidblock);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenW / 10, screenW / 10, false);
		w = bitmap.getWidth();
		h = bitmap.getHeight();
		x = (int) (screenW / 2) - (w / 2);
		y = screenH / 2 + screenH / 4;
	}
	
	public SolidBlock(SolidBlock sb, int x, int y) {
		screenW = sb.screenW;
		screenH = sb.screenH;
		bitmap = sb.bitmap;
		w = sb.w;
		h = sb.h;
		this.x = x;
		this.y = y;
	}
	
	public void playSolidSfx() {
		soundPool.play(solidSfx, 1, 1, 1, 0, 1);
	}
	
	public void update(Canvas canvas, Ball b) {
		cTime = System.currentTimeMillis();
		dTime = cTime - pTime;
		if (dTime >= 5000) {
			render(canvas);
			if (dTime >= 9000) {
				pTime = cTime;
				x = r.nextInt(screenW - w);
			}
		}
		bounceBall(b);
	}
	
	public boolean bounceBall(Ball b) {
		// Bounce off IceBlock Top
		if (b.vY > 0 && Math.abs(b.y + b.h - y) <= 10 && b.x + b.w > x && b.x < x + w) {
			b.vY = (-1) * b.vY;
			b.vY = -1 * b.vY;
			playSolidSfx();
		}		
		// Bounce off IceBlock Bottom
		if (b.vY < 0 && b.x + b.w >= x && b.x <= x + w && Math.abs(b.y - (y + h)) <= 10) {
			b.vY = (-1) * b.vY;
			b.vY = -1 * b.vY;
			playSolidSfx();
		}	
		// Bounce off IceBlock Left
		if (b.vX > 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x + b.w - x) <= 10) {
			b.vX = (-1) * b.vX;
			b.vX = -1 * b.vX;
			playSolidSfx();
		}		
		// Bounce off IceBlock Right
		if (b.vX < 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x - (x + w)) <= 10) {
			b.vX = (-1) * b.vX;
			b.vX = -1 * b.vX;
			playSolidSfx();
		}
		return true;
	}
}
