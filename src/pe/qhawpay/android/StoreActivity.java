package pe.qhawpay.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.qhawpay.android.application.QhawpayApplication;
import pe.qhawpay.android.domain.Address;
import pe.qhawpay.android.domain.Service;
import pe.qhawpay.android.domain.Store;
import pe.qhawpay.android.domain.Photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class StoreActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
	
	protected static final String TAG = StoreActivity.class.getSimpleName();
	
    private TextView mSelected;
    private TextView creation;
    private ListView mList;
    private TextView mTitleTab;
    
    PhotoListFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    Store currentStore;
    
    private ArrayAdapter<String> listAdapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.store);
        
        mSelected = (TextView)findViewById(R.id.text);
        
        creation = (TextView)findViewById(R.id.creation);
        
        mList = (ListView) findViewById(R.id.store_services);
        
        mTitleTab = (TextView)findViewById(R.id.title_tab);
        
		currentStore = ((QhawpayApplication) getApplicationContext()).getStore();
		
		creation.setText("Registrado el "+currentStore.getDatetime()+" por "+currentStore.getCustomer().getRealname());
		

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
        
        
		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			mAdapter = new PhotoListFragmentAdapter(fm, currentStore.getPhotos());
		}
                
        mSelected.setText(currentStore.getInfo());
        mTitleTab.setText("Información");
        
       
        ImageButton mainButton = (ImageButton) findViewById(R.id.map_store);
        mainButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setClass(getApplicationContext(), StoreMapActivity.class);
		        startActivity(intent);
				
			}
		});
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        
        if(currentStore.getPhotos() == null)
        {
        	mPager.setVisibility(View.GONE);
        }
        else
        {
            mPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mPager);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
        case android.R.id.home:
          finish();break;          
      }   
      return true;
    }
    
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
    	
    	//Toast.makeText(this, "Tab: "+tab.getText(), Toast.LENGTH_SHORT).show();
    	
    	if(tab.getText() == "Información")
    	{
    	  mList.setVisibility(View.GONE);
    	  mSelected.setVisibility(View.VISIBLE);
    	  mTitleTab.setText("Información");
    	}
    	else if(tab.getText() == "Locales")
    	{
    		
    		mTitleTab.setText("Locales");
    		
    		List<Address> addresses = currentStore.getAddresses();
    		String[] addressesArray = new String[addresses.size()];
    		
    		int i= 0;
			for (Address address : addresses) {
				addressesArray[i] = address.getAddress();
				i++;
			}

            ArrayList<String> addressList = new ArrayList<String>();  
            addressList.addAll( Arrays.asList(addressesArray) );  

            listAdapter = new ArrayAdapter<String>(this, R.layout.list_item_text, addressList);  

            mList.setAdapter(listAdapter);

            
    	  mList.setVisibility(View.VISIBLE);
      	  mSelected.setVisibility(View.GONE);
      	  
    	} else //Servicios
    	{
    		mTitleTab.setText("Servicios");
    		List<Service> services = currentStore.getServices();
    		String[] servicesArray = new String[services.size()];
    		
    		int i= 0;
			for (Service service : services) {
				servicesArray[i] = service.getName();
				i++;
			}

            ArrayList<String> serviceList = new ArrayList<String>();  
            serviceList.addAll( Arrays.asList(servicesArray) );  

            listAdapter = new ArrayAdapter<String>(this, R.layout.list_item_text, serviceList);  

            mList.setAdapter(listAdapter);
    		
    	  mList.setVisibility(View.VISIBLE);
      	  mSelected.setVisibility(View.GONE);
    	}
    	
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
