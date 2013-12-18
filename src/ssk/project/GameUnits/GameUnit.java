package ssk.project.GameUnits;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class GameUnit {

	public int x;
	public int y;
	public int w;
	public int h;
	public int screenW;
	public int screenH;
	public boolean isVisible = false;
	public boolean isMoving = false;
	public Rect rect;
	public Bitmap bitmap;
	public SoundPool soundPool;
	boolean soundLoaded = false;
	
	boolean playSound = true;
	
	public GameUnit() {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
		    public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
		       soundLoaded = true;
		    }
		});
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean isMoving() {
		return isMoving;
	}
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Rect getRect() {
		return rect;
	}
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	
	public boolean collide(Rect r) {
		return r.intersect(rect);
	}
	
	public void render(Canvas canvas) {
		canvas.drawBitmap(bitmap, x,  y, null);
	}
}