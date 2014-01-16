package ssk.project.Basic_Pong.DialogScreens;

import ssk.project.Pong_Basic.R;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WinDialogFragment extends DialogFragment {
	
	Button button;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.winlayout, container);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.winlayoutid);
        AnimatorSet rightIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.shiftright);
		rightIn.setTarget(ll);
		rightIn.start();
        TextView tv = (TextView) view.findViewById(R.id.wintext);
        ImageView heart = (ImageView) view.findViewById(R.id.smiley);
        button = (Button) view.findViewById(R.id.win_exit_button);
        AnimatorSet scaleXY2 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.slidebackandforth);
		scaleXY2.setTarget(heart);
		scaleXY2.start();
		AnimatorSet scaleXY3 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.scalexy);
		scaleXY3.setTarget(tv);
		scaleXY3.start();
//        AnimatorSet scaleXY = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.scalexy);
//		scaleXY.setTarget(button);
//		scaleXY.start();
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	getActivity().finish();
            }
        });
        return view;
    }
}
