package ssk.project.Basic_Pong.Level_Ice.Objects;

import ssk.project.GameUnits.Ball;
import ssk.project.GameUnits.GameUnit;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class IceBlock extends GameUnit {
	
	public int state = 2;
	public int iceSfx;
	Bitmap iceCrack;
	
	public int iceBlockWidth;
	public int iceBlockHeight;
	public int iceBlockX;
	public int iceBlockY;
	public int iceBlockState;
	
	public IceBlock() {};
	
	public IceBlock(View v, Context context, int screenW, int screenH) {
		this.screenW = screenW;
		this.screenH = screenH;
		iceSfx = soundPool.load(context, R.raw.ice_cracking_sound_effect, 1);
		bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.iceblock);
		bitmap = Bitmap.createScaledBitmap(bitmap, screenW / 10, screenW / 10, false);
		iceCrack = BitmapFactory.decodeResource(v.getResources(), R.drawable.iceblock_cracked);
		iceCrack = Bitmap.createScaledBitmap(iceCrack, screenW / 10, screenW / 10, false);
		w = bitmap.getWidth();
		h = bitmap.getHeight();
		x = (int) (screenW / 2) - (w / 2);
		y = 0;
	}
	
	public IceBlock(IceBlock ib, int x, int y) {
		screenW = ib.screenW;
		screenH = ib.screenH;
		bitmap = ib.bitmap;
		iceCrack = ib.iceCrack;
		w = ib.w;
		h = ib.h;
		this.x = x;
		this.y = y;
	}
	
	public void playIceSfx() {
		soundPool.play(iceSfx, 1, 1, 1, 0, 1);
	}
	
	public void update(Canvas canvas, Ball b) {
		if (state == 2) {
			render(canvas);
		}
		if (state == 1) {
			canvas.drawBitmap(iceCrack, x, y, null);
		}
		bounceBall(b);
	}
	
	public void update2(Canvas canvas, Ball b, IceBlock ib) {
		if (state == 2) {
			render(canvas);
		}
		if (state == 1) {
			canvas.drawBitmap(iceCrack, x, y, null);
		}
		bounceBall2(b, ib);
	}
	
	public boolean bounceBall(Ball b) {
		// Bounce off IceBlock Top
		if (state > 0 && b.vY > 0 && Math.abs(b.y + b.h - y) <= 10 && b.x + b.w > x && b.x < x + w) {
			b.vY = (-1) * b.vY;
			state--;
			playIceSfx();
		}		
		// Bounce off IceBlock Bottom
		if (state > 0 && b.vY < 0 && b.x + b.w >= x && b.x <= x + w && Math.abs(b.y - (y + h)) <= 10) {
			b.vY = (-1) * b.vY;
			state--;
			playIceSfx();
		}	
		// Bounce off IceBlock Left
		if (state > 0 && b.vX > 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x + b.w - x) <= 10) {
			b.vX = (-1) * b.vX;
			state--;
			playIceSfx();
		}		
		// Bounce off IceBlock Right
		if (state > 0 && b.vX < 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x - (x + w)) <= 10) {
			b.vX = (-1) * b.vX;
			state--;
			playIceSfx();
		}
		return true;
	}
	
	public boolean bounceBall2(Ball b, IceBlock ib) {
		// Bounce off IceBlock Top
		if (state > 0 && b.vY > 0 && Math.abs(b.y + b.h - y) <= 10 && b.x + b.w > x && b.x < x + w) {
			b.vY = (-1) * b.vY;
			state--;
			ib.playIceSfx();
		}		
		// Bounce off IceBlock Bottom
		if (state > 0 && b.vY < 0 && b.x + b.w >= x && b.x <= x + w && Math.abs(b.y - (y + h)) <= 10) {
			b.vY = (-1) * b.vY;
			state--;
			ib.playIceSfx();
		}	
		// Bounce off IceBlock Left
		if (state > 0 && b.vX > 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x + b.w - x) <= 10) {
			b.vX = (-1) * b.vX;
			state--;
			ib.playIceSfx();
		}		
		// Bounce off IceBlock Right
		if (state > 0 && b.vX < 0 && b.y + b.h >= y && b.y <= y + h && Math.abs(b.x - (x + w)) <= 10) {
			b.vX = (-1) * b.vX;
			state--;
			ib.playIceSfx();
		}
		return true;
	}
}
