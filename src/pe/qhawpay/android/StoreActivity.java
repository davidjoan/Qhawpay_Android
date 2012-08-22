package pe.qhawpay.android;

import java.util.List;

import pe.qhawpay.android.application.QhawpayApplication;
import pe.qhawpay.android.domain.Store;
import pe.qhawpay.android.domain.Photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class StoreActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
	
	protected static final String TAG = StoreActivity.class.getSimpleName();
	
    private TextView mSelected;
    private TextView creation;
    
    
    PhotoListFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    Store currentStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.store);
        
        mSelected = (TextView)findViewById(R.id.text);
        
        creation = (TextView)findViewById(R.id.creation);
        
		currentStore = ((QhawpayApplication) getApplicationContext()).getStore();
		
		creation.setText("Registrado el "+currentStore.getDatetime()+"por "+currentStore.getCustomer().getRealname());
		
		Log.i(TAG, currentStore.getInfo());
		Log.i(TAG, currentStore.getUrl());
		Log.i(TAG, currentStore.getCustomer().getEmail());

        setTitle(currentStore.getName());
        
        getSupportActionBar().setSubtitle("Establecimiento");
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab tabInfo = getSupportActionBar().newTab();
        tabInfo.setText("Información");
        tabInfo.setTabListener(this);
        getSupportActionBar().addTab(tabInfo);
        
        ActionBar.Tab tabAddress = getSupportActionBar().newTab();
        tabAddress.setText("Locales");
        tabAddress.setTabListener(this);
        getSupportActionBar().addTab(tabAddress);
        
        ActionBar.Tab tabServices = getSupportActionBar().newTab();
        tabServices.setText("Servicios");
        tabServices.setTabListener(this);
        getSupportActionBar().addTab(tabServices);
        
           
        mAdapter = new PhotoListFragmentAdapter(getSupportFragmentManager(), currentStore.getPhotos());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
        mSelected.setText(currentStore.getInfo());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }
    
    
    
    class PhotoListFragmentAdapter extends FragmentPagerAdapter {

        private int mCount = 0;
        
        private List<Photo> photos;

        public PhotoListFragmentAdapter(FragmentManager fm, List<Photo> inputPhotos) {
            super(fm);
            photos = inputPhotos;
            mCount = photos.size();
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentPhoto.newInstance(photos.get(position).getPath());
        }

        @Override
        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
            }
        }
    }
    
    
    
   
}
