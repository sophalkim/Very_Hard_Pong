package ssk.project.Basic_Pong.Modular;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameUnit {

	int x;
	int y;
	int w;
	int h;
	boolean isVisible = false;
	boolean isMoving = false;
	Rect rect;
	Bitmap bitmap;
	SoundPool soundPool;
	
	public GameUnit() {
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
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
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean collide(Rect r) {
		return r.intersect(rect);
	}
	
	public void render(Canvas canvas) {
		canvas.drawBitmap(bitmap, x,  y, null);
	}
}