package pe.qhawpay.android;

import java.text.Collator;
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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.androidquery.AQuery;

public class FragmentStoreCategory extends SherlockFragmentActivity {

	protected static final String TAG = FragmentStoreCategory.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();
		
		String category_slug = getIntent().getStringExtra("pe.qhawpay.android.FragmentStoreCategory.slug");
		String name = getIntent().getStringExtra("pe.qhawpay.android.FragmentStoreCategory.name");
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    
	    
	    getSupportActionBar().setSubtitle("Lugares donde hay "+name);
		
	    getSupportActionBar().setTitle("Establecimientos");
		
		
		
		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			StoreCategoryListFragment list = new StoreCategoryListFragment();
			Bundle bundle = new Bundle();
			bundle.putString("filter", category_slug);
			list.setArguments(bundle);
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}		
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
	
	/**
	 * A custom Loader that loads all of the installed applications.
	 */
	public static class StoreCategoryListLoader extends AsyncTaskLoader<List<Store>> {

		List<Store> mservices;
		private String filter;

		public StoreCategoryListLoader(Context context, String textFilter) {
			super(context);
			filter = textFilter;

		}

		/**
		 * This is where the bulk of our work is done. This function is called
		 * in a background thread and should generate a new set of data to be
		 * published by the loader.
		 */
		@Override
		public List<Store> loadInBackground() {
			List<Store> stores = null;
			try {
				// The URL for making the GET request
				String url;
				
				filter = (filter == null)? "0" : filter;

				url = getContext().getString(R.string.base_uri)+ "/store/category/category_slug/"+filter+"/store_id/a/5/1.json";
				
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

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Store> stores) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (stores != null) {
					onReleaseResources(stores);
				}
			}
			List<Store> oldservices = stores;
			mservices = stores;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(stores);
			}

			// At this point we can release the resources associated with
			// 'oldApps' if needed; now that the new result is delivered we
			// know that it is no longer in use.
			if (oldservices != null) {
				onReleaseResources(oldservices);
			}
		}

		/**
		 * Handles a request to start the Loader.
		 */
		@Override
		protected void onStartLoading() {
			if (mservices != null) {
				// If we currently have a result available, deliver it
				// immediately.
				deliverResult(mservices);
			}

			
			if (takeContentChanged() || mservices == null ) {
				// If the data has changed since the last time it was loaded
				// or is not currently available, start a load.
				forceLoad();
			}
		}

		/**
		 * Handles a request to stop the Loader.
		 */
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		/**
		 * Handles a request to cancel a load.
		 */
		@Override
		public void onCanceled(List<Store> stores) {
			super.onCanceled(stores);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(stores);
		}

		/**
		 * Handles a request to completely reset the Loader.
		 */
		@Override
		protected void onReset() {
			super.onReset();

			// Ensure the loader is stopped
			onStopLoading();

			// At this point we can release the resources associated with 'apps'
			// if needed.
			if (mservices != null) {
				onReleaseResources(mservices);
				mservices = null;
			}

		}

		/**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(List<Store> services) {
			// For a simple List<> there is nothing to do. For something
			// like a Cursor, we would close it here.
		}
	}

	public static class StoreCategoryListAdapter extends ArrayAdapter<Store> {
		private final LayoutInflater mInflater;
		
		public StoreCategoryListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<Store> datos) {
			this.clear();
			if (datos != null) {
				for (Store store : datos) {
					add(store);
				}
			}
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

	public static class StoreCategoryListFragment extends SherlockListFragment
			implements LoaderManager.LoaderCallbacks<List<Store>> {

		// This is the Adapter being used to display the list's data.
		StoreCategoryListAdapter mAdapter;

		// If non-null, this is the current filter the user has provided.
		String mCurFilter;

		public StoreCategoryListFragment() {
		}



		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			// Give some text to display if there is no data. In a real
			// application this would come from a resource.
			setEmptyText("Sin Establecimientos");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new StoreCategoryListAdapter(getActivity());
			setListAdapter(mAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
			
		 
		}



		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Insert desired behavior here.
			Log.i(TAG, "Item clicked: " + id);
		}

		@Override
		public Loader<List<Store>> onCreateLoader(int id, Bundle args) {
			// This is called when a new Loader needs to be created. This
			// sample only has one Loader with no arguments, so it is simple.
			
			Bundle bundle = this.getArguments();
			mCurFilter = bundle.getString("filter");
			
			Log.i(TAG, "on create loader: " + mCurFilter);			
			return new StoreCategoryListLoader(getActivity(), mCurFilter);
		}

		@Override
		public void onLoadFinished(Loader<List<Store>> loader, List<Store> data) {
			
			// Set the new data in the adapter.
			mAdapter.setData(data);

			// The list should now be shown.
			if (this.isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Store>> loader) {
			// Clear the data in the adapter.
			mAdapter.setData(null);
		}
	}
}
