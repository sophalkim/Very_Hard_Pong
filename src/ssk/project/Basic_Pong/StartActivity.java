package ssk.project.Basic_Pong;

import ssk.project.Basic_Pong.DialogScreens.AudioPreferences;
import ssk.project.Basic_Pong.Level_Volcano.VolcanoGameActivity;
import ssk.project.Basic_Pong.Level_Volcano_2.VolcanoGameActivity2;
import ssk.project.Basic_Pong.Level_Volcano_3.VolcanoGameActivity3;
import ssk.project.Basic_Pong.Level_Volcano_4.VolcanoGameActivity4;
import ssk.project.Basic_Pong.Level_Wood.WoodGameActivity;
import ssk.project.Basic_Pong.Level_Wood_2.WoodGameActivity2;
import ssk.project.Basic_Pong.Level_Wood_3.WoodGameActivity3;
import ssk.project.Basic_Pong.Level_Wood_4.WoodGameActivity4;
import ssk.project.Basic_Pong.Modular.BaseActivity;
import ssk.project.Pong_Basic.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

public class StartActivity extends FragmentActivity implements AudioPreferences.Listener {

	StartView gameView;
	static Typeface tf1;
	static Typeface tf2;
	Intent i;
	MediaPlayer mp;
	boolean playSound = true;
	boolean playMusic = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(gameView = new StartView(this));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        loadPreferences();
        tf1 = Typeface.createFromAsset(getAssets(), "fonts/pirataone.ttf");
        tf2 = Typeface.createFromAsset(getAssets(), "fonts/iceland.ttf");
        mp = MediaPlayer.create(this, R.raw.starting_jingle);
		mp.setLooping(true);
        if (playMusic) {
	        mp.start();
        }
    }
    
    @Override
	public void onPause() {
		super.onPause();
		gameView.thread.setRunning(false);
		if (playMusic) {
			mp.stop();
		}
	}
    
    @Override
	public void onResume() {
		super.onResume();
		if (playMusic) {
	        mp.start();
        }
	}
    
    @Override
	public void onDestroy(){
		super.onDestroy();
		if (playMusic) {
			mp.release();
		}
	}
    
    public void startWood() {
    	i = new Intent(StartActivity.this, WoodGameActivity.class);
    	startActivity(i);
    }
    
    public void startWood2() {
    	i = new Intent(StartActivity.this, WoodGameActivity2.class);
    	startActivity(i);
    }
    
    public void startWood3() {
    	i = new Intent(StartActivity.this, WoodGameActivity3.class);
    	startActivity(i);
    }
    
    public void startWood4() {
    	i = new Intent(StartActivity.this, WoodGameActivity4.class);
    	startActivity(i);
    }
    
    public void startVolcano() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity.class);
    	startActivity(i);
    }
    
    public void startVolcano2() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity2.class);
    	startActivity(i);
    }
    
    public void startVolcano3() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity3.class);
    	startActivity(i);
    }
    
    public void startVolcano4() {
    	i = new Intent(StartActivity.this, VolcanoGameActivity4.class);
    	startActivity(i);
    }
    
    public void startIce() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 5);
    	i.putExtra("Music", 5);
    	startActivity(i);
    }
    
    public void startIce2() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 6);
    	i.putExtra("Music", 5);
    	startActivity(i);
    }
    
    public void startIce3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 7);
    	i.putExtra("Music", 5);
    	startActivity(i);
    }
    
    public void startIce4() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 8);
    	i.putExtra("Music", 5);
    	startActivity(i);
    }
    
    public void startBeach1() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 1);
    	i.putExtra("Music", 1);
    	startActivity(i);
    }
    
    public void startBeach2() {
    	AudioPreferences audioDialog = new AudioPreferences();
		audioDialog.show(getSupportFragmentManager(), "missisles");
    }
    
    public void startBeach3() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 2);
    	i.putExtra("Music", 2);
    	startActivity(i);
    }
    
    public void startBeach4() {
    	i = new Intent(StartActivity.this, BaseActivity.class);
    	i.putExtra("Level", 3);
    	i.putExtra("Music", 3);
    	startActivity(i);
    }

	@Override
	public void onClickMusic(boolean music) {
		playMusic = music;
		if (music) {
			mp.start();
		} else {
			mp.pause();
		}
		savePreferences("MUSIC", music);
	}
	
	@Override
	public void onClickSound(boolean sound) {
		gameView.playSound = sound;
	}
	
	public void loadPreferences() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		playSound = sp.getBoolean("SOUND", true);
		playMusic = sp.getBoolean("MUSIC", true);
	}
	
	public void savePreferences(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
}