package pe.qhawpay.android;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pe.qhawpay.android.domain.Store;
import pe.qhawpay.android.domain.StoreList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.androidquery.AQuery;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class StoreCategoryActivity extends SherlockListActivity {
	
	protected static final String TAG = StoreCategoryActivity.class.getSimpleName();
	
    private List<Store> stores = new ArrayList<Store>();
    
    private String categorySlug;
    
    private Integer page = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        categorySlug = getIntent().getStringExtra("pe.qhawpay.android.StoreCategoryActivity.slug");
		String name = getIntent().getStringExtra("pe.qhawpay.android.StoreCategoryActivity.name");
		setContentView(R.layout.store_list);
        
        setTitle("Establecimientos");
        
        getSupportActionBar().setSubtitle("Categoria "+name);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    
        

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
            	page++;
            	new StoreSearchTask().execute(categorySlug, page.toString());
            }
        });

        new StoreSearchTask().execute(categorySlug, page.toString());
        
        
    }


    
    
	public static class StoreListAdapter extends ArrayAdapter<Store> {
		private final LayoutInflater mInflater;
		List<Store> stores;
		
		public StoreListAdapter(Context context, List<Store> newStores) {
			super(context, android.R.layout.simple_list_item_2);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			stores = newStores;
		}

		public void setData(List<Store> datos) {
			this.clear();
			if (datos != null) {
				for (Store store : datos) {
					add(store);
				}
			}
		}
		
		public int getCount() {
		    return stores.size();
		}
		
		public Store getItem(int position) {
		    return stores.get(position);
		}

		public long getItemId(int position) {
		    return position;
		}		

		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				view = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
			} else {
				view = convertView;
			}

			Store item = getItem(position);
			
			if(item.getLogo() == null || item.getLogo() == "")
			{
				((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_content_picture);	
			}
			else
			{
				AQuery aq = new AQuery(view);
				aq.id(R.id.icon).image(getContext().getString(R.string.store_base_uri)+ item.getLogo(), true, true, 0,0, null, AQuery.FADE_IN_NETWORK);
			}			
			

			((TextView) view.findViewById(R.id.text)).setText(item.getName());

			return view;
		}
	}
	
    private class StoreSearchTask extends AsyncTask<String, Void, List<Store>> {
    	
    String filter;
    String page;

	@Override
	protected List<Store> doInBackground(String... params) {
		
		filter = params[0];
		page = params[1];
		
		
		List<Store> stores = null;
		try {
			// The URL for making the GET request
			String url;
			
			filter = (filter == null || filter == "0" || filter == "")? "0" : filter;
			page = (page == null || page == "0" || page == "")? "1" : page;

			url = getString(R.string.base_uri)+ "/store/category/category_slug/"+filter+"/store_id/a/5/"+page+".json";
			
			Log.i(TAG, "API REST get called: " + url);
			
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			

			// Add the Gson message converter
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

			// Make the HTTP GET request, marshaling the response from JSON to an array of Events
			ResponseEntity<StoreList> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, StoreList.class);

			// convert the array to a list and return it
			stores = responseEntity.getBody().getStores();
			Collections.sort(stores, ALPHA_COMPARATOR);
			return stores;

		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return stores;		


	}

	@Override
	protected void onPostExecute(List<Store> response) {
	    // hide the progress indicator when the network request is complete
		((PullToRefreshListView) getListView()).onRefreshComplete();

	    // return the Rest results
	    refreshStoreResults(response);
	}

    }
    
    private void refreshStoreResults(List<Store> response) {
	if (response == null) {
		page--;
	    return;
	}
	
	this.stores.addAll(response);
	setListAdapter(new StoreListAdapter(this, this.stores));
	
    }
    
	/**
	 * Perform alphabetical comparison of application entry objects.
	 */
	public static final Comparator<Store> ALPHA_COMPARATOR = new Comparator<Store>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(Store object1, Store object2) {
			return sCollator.compare(object1.getName(), object2.getName());
		}
	};    
}
