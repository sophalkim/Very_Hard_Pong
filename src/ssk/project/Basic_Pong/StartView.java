package ssk.project.Basic_Pong;

import ssk.project.BaseClasses.BaseThread;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class StartView extends SurfaceView implements SurfaceHolder.Callback {
	
	int screenWidth;
	int screenHeight;

	BaseThread thread;
	SoundPool soundPool;
	private int onClickSound;
	
	Bitmap background;
	int backgroundWidth;
	int backgroundHeight;
	
	Bitmap iceCave;
	int iceCaveX;
	int iceCaveY;
	int iceCaveWidth;
	int iceCaveHeight;
	boolean iceCaveSelected = false;
	boolean iceCave2Selected = false;
	boolean iceCave3Selected = false;
	boolean iceCave4Selected = false;
	
	Bitmap volcano;
	int volcanoX;
	int volcanoY;
	int volcanoWidth;
	int volcanoHeight;
	boolean volcanoSelected = false;
	boolean volcano2Selected = false;
	boolean volcano3Selected = false;
	boolean volcano4Selected = false;
	
	Bitmap wood;
	int woodX;
	int woodY;
	int woodWidth;
	int woodHeight;
	boolean woodSelected = false;
	boolean wood2Selected = false;
	boolean wood3Selected = false;
	boolean wood4Selected = false;
	
	Bitmap beach;
	int beachX;
	int beachY;
	int beachWidth;
	int beachHeight;
	boolean beachSelected = false;
	boolean beach2Selected = false;
	boolean beach3Selected = false;
	boolean beach4Selected = false;
	
	int velocityX;
	int velocityY;
	
	int icex;
	int volcanox;
	int woody;
	int beachy;
	int scaleX;
	int scaleY;
	Paint paint = new Paint();
	Paint levelPaint = new Paint();
	
	Bitmap lockBitmap;
	Bitmap beachballBitmap;
	float beachballscale = 1;
	boolean beachballscaling = false;
	Bitmap musicnoteBitmap;
	int musicnoteangle = 0;
	boolean musicnotereverse = false;
	
	boolean levelSelected = false;
	boolean animationCompleted = false;
	
	SharedPreferences sp;
	boolean playSound = true;
	
	public StartView(Context context) {
		super(context);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);	
		onClickSound = soundPool.load(context, R.raw.synth_organ, 1);
		getHolder().addCallback(this);
		setFocusable(true);
		velocityX = 10;
		velocityY = 10;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
		playSound = sp.getBoolean("SOUND", true);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		screenWidth = w;
		screenHeight = h;
		
		velocityX = screenWidth / 100;
		velocityY = screenHeight / 100;
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.start_background_image);
		background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, false);
		
		iceCave = BitmapFactory.decodeResource(getResources(), R.drawable.ice_cave);
		iceCave = Bitmap.createScaledBitmap(iceCave, screenWidth / 3 + screenWidth / 12, screenHeight / 3 + screenHeight / 12, false);
		iceCaveWidth = iceCave.getWidth();
		iceCaveHeight = iceCave.getHeight();
		iceCaveX = (screenWidth / 24);
		iceCaveY = (screenHeight / 24);
		
		volcano = BitmapFactory.decodeResource(getResources(), R.drawable.rolling_hills);
		volcano = Bitmap.createScaledBitmap(volcano, screenWidth / 3 + screenWidth / 12, screenHeight / 3 + screenHeight / 12, false);
		volcanoWidth = volcano.getWidth();
		volcanoHeight = volcano.getHeight();
		volcanoX = (screenWidth / 24) * 3 + iceCave.getWidth();
		volcanoY = (screenHeight / 24);
		
		wood = BitmapFactory.decodeResource(getResources(), R.drawable.wood_flooring_background);
		wood = Bitmap.createScaledBitmap(wood, screenWidth / 3 + screenWidth / 12, screenHeight / 3 + screenHeight / 12, false);
		woodWidth = wood.getWidth();
		woodHeight = wood.getHeight();
		woodX = (screenWidth / 24);
		woodY = (screenHeight / 24) + (screenHeight / 2);
		
		beach = BitmapFactory.decodeResource(getResources(), R.drawable.beach2);
		beach = Bitmap.createScaledBitmap(beach, screenWidth / 3 + screenWidth / 12, screenHeight / 3 + screenHeight / 12, false);
		beachWidth = beach.getWidth();
		beachHeight = beach.getHeight();
		beachX = (screenWidth / 24) * 3 + iceCave.getWidth();
		beachY = (screenHeight / 24) + (screenHeight / 2);
		
		icex = 0 - (iceCaveWidth * 2);
		volcanox = screenWidth + volcanoWidth;
		woody = screenHeight + beachHeight;
		beachy = screenHeight + beachHeight;
		
		scaleX = 0;
		scaleY = 0;
		// Stage Select Text Paint
		paint.setTextSize(screenWidth / 12);
		paint.setTypeface(StartActivity.tf1);
		paint.setColor(Color.GREEN);
		paint.setShadowLayer(5, 0, 0, Color.MAGENTA);
		// Levels Number Paint
		levelPaint.setTextSize(screenWidth / 6);
		levelPaint.setColor(Color.RED);
		levelPaint.setTextAlign(Align.CENTER);
		levelPaint.setTypeface(StartActivity.tf2);
		levelPaint.setShadowLayer(10, 0, 0, Color.GREEN);
		
		lockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lock);
		lockBitmap = Bitmap.createScaledBitmap(lockBitmap, iceCaveWidth / 4, iceCaveHeight / 4, false);
		
		beachballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beachball);
		beachballBitmap = Bitmap.createScaledBitmap(beachballBitmap, screenWidth / 7, screenHeight / 11, false);
		musicnoteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.musicnote);
		musicnoteBitmap = Bitmap.createScaledBitmap(musicnoteBitmap, iceCaveWidth / 4, iceCaveHeight / 4, false);
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN: {
				if (!levelSelected) {
					// Ice Level Selected
					if (x >= iceCaveX && x <= iceCaveX + iceCaveWidth / 2 && y >= iceCaveY && y <= iceCaveY + iceCaveHeight / 2) {
	 					iceCaveSelected = true;
	 					levelSelected = true;
	 				}
					if (x >= iceCaveX + iceCaveWidth / 2 && x <= iceCaveX + iceCaveWidth && y >= iceCaveY && y <= iceCaveY + iceCaveHeight / 2) {
						iceCave2Selected = true;
	 					levelSelected = true;
					}
					if (x >= iceCaveX && x <= iceCaveX + iceCaveWidth / 2 && y >= iceCaveY + iceCaveHeight / 2 && y <= iceCaveY + iceCaveHeight) {
	 					iceCave3Selected = true;
	 					levelSelected = true;
	 				}
					if (x >= iceCaveX + iceCaveWidth / 2 && x <= iceCaveX + iceCaveWidth && y >= iceCaveY + iceCaveHeight / 2 && y <= iceCaveY + iceCaveHeight) {
	 					iceCave4Selected = true;
	 					levelSelected = true;
	 				}
					// Volcano Level Selected
					if (x >= volcanoX && x <= volcanoX + volcanoWidth / 2 && y >= volcanoY && y <= volcanoY + volcanoHeight / 2) {
	 					volcanoSelected = true;
	 					levelSelected = true;
	 				}
					if (x >= volcanoX + volcanoWidth / 2 && x <= volcanoX + volcanoWidth && y >= volcanoY && y <= volcanoY + volcanoHeight / 2) {
						volcano2Selected = true;
	 					levelSelected = true;
					}
					if (x >= volcanoX && x <= volcanoX + volcanoWidth / 2 && y >= volcanoY + volcanoHeight / 2 && y <= volcanoY + volcanoHeight) {
	 					volcano3Selected = true;
	 					levelSelected = true;
	 				}
					if (x >= volcanoX + volcanoWidth / 2 && x <= volcanoX + volcanoWidth && y >= volcanoY + volcanoHeight / 2 && y <= volcanoY + volcanoHeight) {
	 					volcano4Selected = true;
	 					levelSelected = true;
	 				}
					// Wood Level Selected
					if (x >= woodX && x <= woodX + woodWidth / 2 && y >= woodY && y <= woodY + woodHeight / 2) {
	 					woodSelected = true;
	 					levelSelected = true;
	 				}
					if (x >= woodX + woodWidth / 2 && x <= woodX + woodWidth && y >= woodY && y <= woodY + woodHeight / 2) {
						wood2Selected = true;
	 					levelSelected = true;
					}
					if (x >= woodX && x <= woodX + woodWidth / 2 && y >= woodY + woodHeight / 2 && y <= woodY + woodHeight) {
	 					wood3Selected = true;
	 					levelSelected = true;
	 				}
					if (x >= woodX + woodWidth / 2 && x <= woodX + woodWidth && y >= woodY + woodHeight / 2 && y <= woodY + woodHeight) {
	 					wood4Selected = true;
	 					levelSelected = true;
	 				}
					// Beach Level Selected
					if (x >= beachX && x <= beachX + beachWidth && y >= beachY && y <= beachY + beachHeight / 2) {
	 					beachSelected = true;
	 					levelSelected = true;
	 				}
					if (x >= beachX && x <= beachX + beachWidth && y >= beachY + beachHeight / 2 && y <= beachY + beachHeight) {
	 					beach4Selected = true;
	 					levelSelected = true;
	 				}
				}
			}
		}
		return true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// Transition Animation
		if (!animationCompleted) {
			canvas.drawBitmap(iceCave, icex, iceCaveY, null);
			canvas.drawBitmap(volcano, volcanox, volcanoY, null);
			canvas.drawBitmap(wood, woodX, woody, null);
			canvas.drawBitmap(beach, beachX, beachy, null);
			icex += velocityX;
			volcanox -= velocityX;
			woody -= velocityY;
			beachy -= velocityY;
			if (woody <= woodY) {
				animationCompleted = true;
			}
		}
		// Animation is completed
		if (animationCompleted) {
			canvas.drawBitmap(background, 0, 0, null);
			// Drawing IceCave Level, Number and locks
			canvas.drawBitmap(iceCave, iceCaveX, iceCaveY, null);
			canvas.drawText("1", iceCaveX + iceCaveWidth / 4, iceCaveY + iceCaveHeight / 3, levelPaint);
			canvas.drawText("2", iceCaveX + iceCaveWidth - iceCaveWidth / 4, iceCaveY + iceCaveHeight / 3, levelPaint);
			canvas.drawText("3", iceCaveX + iceCaveWidth / 4, iceCaveY + iceCaveHeight - iceCaveHeight / 6, levelPaint);
			canvas.drawText("4", iceCaveX + iceCaveWidth - iceCaveWidth / 4, iceCaveY + iceCaveHeight - iceCaveHeight / 6, levelPaint);
			canvas.drawBitmap(lockBitmap, iceCaveX + iceCaveWidth - iceCaveWidth / 3 - iceCaveWidth / 24, iceCaveY + iceCaveHeight / 8, null);
			canvas.drawBitmap(lockBitmap, iceCaveX + iceCaveWidth / 6 - iceCaveWidth / 24, iceCaveY + iceCaveHeight - iceCaveHeight / 3 - iceCaveHeight / 16, null);
			canvas.drawBitmap(lockBitmap, iceCaveX + iceCaveWidth - iceCaveWidth / 3 - iceCaveWidth / 24, iceCaveY + iceCaveHeight - iceCaveHeight / 3 - iceCaveHeight / 16, null);
			// Drawing Volcano Level, number, and locks
			canvas.drawBitmap(volcano, volcanoX, volcanoY, null);
			canvas.drawText("1", volcanoX + volcanoWidth / 4, volcanoY + volcanoHeight / 3, levelPaint);
			canvas.drawText("2", volcanoX + volcanoWidth - volcanoWidth / 4, volcanoY + volcanoHeight / 3, levelPaint);
			canvas.drawText("3", volcanoX + volcanoWidth / 4, volcanoY + volcanoHeight - volcanoHeight / 6, levelPaint);
			canvas.drawText("4", volcanoX + volcanoWidth - volcanoWidth / 4, volcanoY + volcanoHeight - volcanoHeight / 6, levelPaint);
			canvas.drawBitmap(lockBitmap, volcanoX + volcanoWidth - volcanoWidth / 3 - volcanoWidth / 24, volcanoY + volcanoHeight / 8, null);
			canvas.drawBitmap(lockBitmap, volcanoX + volcanoWidth / 6 - volcanoWidth / 24, volcanoY + volcanoHeight - volcanoHeight / 3 - volcanoHeight / 16, null);
			canvas.drawBitmap(lockBitmap, volcanoX + volcanoWidth - volcanoWidth / 3 - volcanoWidth / 24, volcanoY + volcanoHeight - volcanoHeight / 3 - volcanoHeight / 16, null);	
			// Drawing Wood Level, number, and locks
			canvas.drawBitmap(wood, woodX, woodY, null);
			canvas.drawText("1", woodX + woodWidth / 4, woodY + woodHeight / 3, levelPaint);
			canvas.drawText("2", woodX + woodWidth - woodWidth / 4, woodY + woodHeight / 3, levelPaint);
			canvas.drawText("3", woodX + woodWidth / 4, woodY + woodHeight - woodHeight / 6, levelPaint);
			canvas.drawText("4", woodX + woodWidth - woodWidth / 4, woodY + woodHeight - woodHeight / 6, levelPaint);
			canvas.drawBitmap(lockBitmap, woodX + woodWidth - woodWidth / 3 - woodWidth / 24, woodY + woodHeight / 8, null);
			canvas.drawBitmap(lockBitmap, woodX + woodWidth / 6 - woodWidth / 24, woodY + woodHeight - woodHeight / 3 - woodHeight / 16, null);
			canvas.drawBitmap(lockBitmap, woodX + woodWidth - woodWidth / 3 - woodWidth / 24, woodY + woodHeight - woodHeight / 3 - woodHeight / 16, null);
			// Drawing Beach Level, number, and locks
			canvas.drawBitmap(beach, beachX, beachY, null);
			// BeachBall
			canvas.drawBitmap(beachballBitmap, (beachX + (beachWidth / 2)  - (beachballBitmap.getWidth() / 2)), (beachY + (beachHeight / 4) - beachballBitmap.getHeight() / 2), null);
			// Music Note
			if (!musicnotereverse) {
				musicnoteangle++;
			}
			if (musicnotereverse) {
				musicnoteangle--;
			}
			if (Math.abs(musicnoteangle) == 10) {
				musicnotereverse = !musicnotereverse;
			}
			canvas.save();
			canvas.rotate(musicnoteangle, (beachX + (beachWidth / 2)  - (musicnoteBitmap.getWidth() / 2)) + (musicnoteBitmap.getWidth() / 2), (beachY + (beachHeight / 2)) + musicnoteBitmap.getHeight() / 2);
			canvas.drawBitmap(musicnoteBitmap,(beachX + (beachWidth / 2)  - (musicnoteBitmap.getWidth() / 2)), (beachY + (beachHeight / 2)) + musicnoteBitmap.getHeight() / 2, null);
			canvas.restore();
			// Drawing Select Stage Text
			canvas.drawText("Select Stage", screenWidth / 2 - (paint.measureText("Select Stage") / 2), screenHeight / 2 + screenHeight / 64, paint);
		}
		// Animation for IceCave
		if (iceCaveSelected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, 0, 0 + scaleX, 0 + scaleY);
			canvas.drawBitmap(iceCave, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				iceCaveSelected = false;
				((StartActivity)getContext()).startIce();
				levelSelected = false;
			}
		}
		// Animation for IceCave 2
		if (iceCave2Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, 0, 0 + scaleX, 0 + scaleY);
			canvas.drawBitmap(iceCave, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				iceCave2Selected = false;
				((StartActivity)getContext()).startIce2();
				levelSelected = false;
			}
		}
		// Animation for IceCave 3
		if (iceCave3Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, 0, 0 + scaleX, 0 + scaleY);
			canvas.drawBitmap(iceCave, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				iceCave3Selected = false;
				((StartActivity)getContext()).startIce3();
				levelSelected = false;
			}
		}
		// Animation for IceCave 4
		if (iceCave4Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, 0, 0 + scaleX, 0 + scaleY);
			canvas.drawBitmap(iceCave, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				iceCave4Selected = false;
				((StartActivity)getContext()).startIce4();
				levelSelected = false;
			}
		}
		// Animation for Volcano
		if (volcanoSelected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, 0, screenWidth, 0 + scaleY);
			canvas.drawBitmap(volcano, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				volcanoSelected = false;
				((StartActivity)getContext()).startVolcano();
				levelSelected = false;
			} 
		}
		// Animation for Volcano 2
		if (volcano2Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, 0, screenWidth, 0 + scaleY);
			canvas.drawBitmap(volcano, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				volcano2Selected = false;
				((StartActivity)getContext()).startVolcano2();
				levelSelected = false;
			} 
		}
		// Animation for Volcano 3
		if (volcano3Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, 0, screenWidth, 0 + scaleY);
			canvas.drawBitmap(volcano, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				volcano3Selected = false;
				((StartActivity)getContext()).startVolcano3();
				levelSelected = false;
			} 
		}
		// Animation for Volcano 4
		if (volcano4Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, 0, screenWidth, 0 + scaleY);
			canvas.drawBitmap(volcano, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				volcano4Selected = false;
				((StartActivity)getContext()).startVolcano4();
				levelSelected = false;
			} 
		}
		// Animation for Wood 
		if (woodSelected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, screenHeight - scaleY, 0 + scaleX, screenHeight);
			canvas.drawBitmap(wood, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				woodSelected = false;
				((StartActivity)getContext()).startWood();
				levelSelected = false;
			}
		}
		// Animation for Wood 2
		if (wood2Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, screenHeight - scaleY, 0 + scaleX, screenHeight);
			canvas.drawBitmap(wood, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				wood2Selected = false;
				((StartActivity)getContext()).startWood2();
				levelSelected = false;
			}
		}
		// Animation for Wood 3
		if (wood3Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, screenHeight - scaleY, 0 + scaleX, screenHeight);
			canvas.drawBitmap(wood, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				wood3Selected = false;
				((StartActivity)getContext()).startWood3();
				levelSelected = false;
			}
		}
		// Animation for Wood 4
		if (wood4Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(0, screenHeight - scaleY, 0 + scaleX, screenHeight);
			canvas.drawBitmap(wood, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth && scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				wood4Selected = false;
				((StartActivity)getContext()).startWood4();
				levelSelected = false;
			}
		}
		// Animation for Beach
		if (beachSelected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, screenHeight - scaleY, screenWidth + scaleX, screenHeight + scaleY);
			canvas.drawBitmap(beach, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth&& scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				beachSelected = false;
				((StartActivity)getContext()).startBeach1();
				levelSelected = false;
			}
		}
		// Animation for Beach 2
		if (beach2Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, screenHeight - scaleY, screenWidth + scaleX, screenHeight + scaleY);
			canvas.drawBitmap(beach, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth&& scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				beach2Selected = false;
				((StartActivity)getContext()).startBeach2();
				levelSelected = false;
			}
		}
		// Animation for Beach 3
		if (beach3Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, screenHeight - scaleY, screenWidth + scaleX, screenHeight + scaleY);
			canvas.drawBitmap(beach, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth&& scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				beach3Selected = false;
				((StartActivity)getContext()).startBeach3();
				levelSelected = false;
			}
		}
		// Animation for Beach 3
		if (beach4Selected) {
			if (scaleX == 0) {
				if (playSound) {
					soundPool.play(onClickSound, 1, 1, 1, 0, 1);
				}
			}
			Rect startingRect = new Rect(screenWidth - scaleX, screenHeight - scaleY, screenWidth + scaleX, screenHeight + scaleY);
			canvas.drawBitmap(beach, null, startingRect, null);
			scaleX = scaleX + (screenWidth / 24);
			scaleY = scaleY + (screenHeight / 24);
			if (scaleX >= screenWidth&& scaleY >= screenHeight) {
				scaleX = 0;
				scaleY = 0;
				beach4Selected = false;
				((StartActivity)getContext()).startBeach4();
				levelSelected = false;
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new BaseThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pauseSound() {
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}
	}
	
	public void resumeSound() {
		if (soundPool == null) {
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);	
			onClickSound = soundPool.load(getContext(), R.raw.synth_organ, 1);
		}
	}
}