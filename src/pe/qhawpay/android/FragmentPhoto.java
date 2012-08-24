package pe.qhawpay.android;

import com.androidquery.AQuery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class FragmentPhoto extends Fragment {
    

    public static FragmentPhoto newInstance(String content) {
        FragmentPhoto fragment = new FragmentPhoto();
        fragment.mContent = content;
        return fragment;
    }

    private String mContent = "empty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ImageView text = new ImageView(getActivity());
    	
    	AQuery aq = new AQuery(text);
		aq.image(getString(R.string.photo_base_uri)+ mContent, true, true, 0,0, null, AQuery.FADE_IN_NETWORK);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}
