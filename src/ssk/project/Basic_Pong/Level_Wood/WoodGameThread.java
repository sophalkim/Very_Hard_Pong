package ssk.project.Basic_Pong.Level_Wood;
import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class WoodGameThread extends Thread {
	
	private SurfaceHolder holder;
	private WoodGameView gameView;
	private boolean running = false;
	
	public WoodGameThread(SurfaceHolder holder, WoodGameView gameView) {
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
