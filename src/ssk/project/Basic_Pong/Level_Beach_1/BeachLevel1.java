package ssk.project.Basic_Pong.Level_Beach_1;
import ssk.project.BaseClasses.BaseLevel;
import ssk.project.Pong_Basic.R;
import android.content.Context;
import android.graphics.BitmapFactory;


public class BeachLevel1 extends BaseLevel {

	public BeachLevel1(Context context) {
		super(context);
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beach_background);
	}
	
}