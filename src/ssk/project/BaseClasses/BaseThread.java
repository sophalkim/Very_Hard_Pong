package ssk.project.BaseClasses;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BaseThread extends Thread {
	
	private SurfaceHolder holder;
	private SurfaceView gameView;
	private boolean running = false;
	
	public BaseThread(SurfaceHolder holder, SurfaceView gameView) {
		this.holder = holder;
		this.gameView = gameView;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public SurfaceHolder getSurfaceHolder() {
		return holder;
	}

	@Override
	public void run() {
		Canvas canvas;
		while (running) {
			canvas = null;
			try {
				canvas = holder.lockCanvas();
				synchronized (holder) {
					gameView.draw(canvas);
				}		
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
