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


import pe.qhawpay.android.domain.Service;
import pe.qhawpay.android.domain.ServiceList;
import android.content.Context;
import android.content.Intent;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;

public class FragmentService extends SherlockFragmentActivity {

	protected static final String TAG = FragmentService.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			ServiceListFragment list = new ServiceListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	/**
	 * Perform alphabetical comparison of application entry objects.
	 */
	public static final Comparator<Service> ALPHA_COMPARATOR = new Comparator<Service>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(Service object1, Service object2) {
			return sCollator.compare(object1.getName(), object2.getName());
		}
	};
	


	/**
	 * A custom Loader that loads all of the installed applications.
	 */
	public static class ServiceListLoader extends AsyncTaskLoader<List<Service>> {

		List<Service> mservices;
		private String filter;

		public ServiceListLoader(Context context, String textFilter) {
			super(context);
			filter = textFilter;

		}

		/**
		 * This is where the bulk of our work is done. This function is called
		 * in a background thread and should generate a new set of data to be
		 * published by the loader.
		 */
		@Override
		public List<Service> loadInBackground() {
			List<Service> services = null;
			try {
				// The URL for making the GET request
				String url;
				if(filter == null)
				{
					url = getContext().getString(R.string.base_uri)+ "/service/name/0/created_at/a/100/1.json";
				}
				else
				{
					url = getContext().getString(R.string.base_uri)+ "/service/name/"+filter+"/created_at/a/100/1.json";
				}
				
				Log.i(TAG, "API REST get called: " + url);
				
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
				HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

				// Create a new RestTemplate instance
				RestTemplate restTemplate = new RestTemplate();
				

				// Add the Gson message converter
				restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

				// Make the HTTP GET request, marshaling the response from JSON to an array of Events
				ResponseEntity<ServiceList> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ServiceList.class);

				// convert the array to a list and return it
				services = responseEntity.getBody().getServices();
				Collections.sort(services, ALPHA_COMPARATOR);
				return services;

			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}

			return services;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<Service> services) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (services != null) {
					onReleaseResources(services);
				}
			}
			List<Service> oldservices = services;
			mservices = services;

			if (isStarted()) {
				// If the Loader is currently started, we can immediately
				// deliver its results.
				super.deliverResult(services);
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
		public void onCanceled(List<Service> services) {
			super.onCanceled(services);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(services);
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
		protected void onReleaseResources(List<Service> services) {
			// For a simple List<> there is nothing to do. For something
			// like a Cursor, we would close it here.
		}
	}

	public static class ServiceListAdapter extends ArrayAdapter<Service> {
		private final LayoutInflater mInflater;

		public ServiceListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(List<Service> datos) {
			this.clear();
			if (datos != null) {
				for (Service Service : datos) {
					add(Service);
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
				view = mInflater.inflate(R.layout.list_item_icon_text, parent,
						false);
			} else {
				view = convertView;
			}

			Service item = getItem(position);

			
			
			
			if(item.getImage() == null || item.getImage() == "")
			{
				((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.icon);	
			}
			else
			{
				AQuery aq = new AQuery(view);
				aq.id(R.id.icon).image(getContext().getString(R.string.service_base_uri)+ item.getImage());
			}
			
			((TextView) view.findViewById(R.id.text)).setText(item.getName());

			return view;
		}
	}

	public static class ServiceListFragment extends SherlockListFragment
			implements LoaderManager.LoaderCallbacks<List<Service>> {

		// This is the Adapter being used to display the list's data.
		ServiceListAdapter mAdapter;

		// If non-null, this is the current filter the user has provided.
		String mCurFilter;

		OnQueryTextListenerCompat mOnQueryTextListenerCompat;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data. In a real
			// application this would come from a resource.
			setEmptyText("Sin Servicios");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new ServiceListAdapter(getActivity());
			setListAdapter(mAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.
			MenuItem item = menu.add("Buscar");
			item.setIcon(android.R.drawable.ic_menu_search);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			View searchView = SearchViewCompat.newSearchView(getActivity());
			if (searchView != null) {
				SearchViewCompat.setOnQueryTextListener(searchView,
						new OnQueryTextListenerCompat() {
							@Override
							public boolean onQueryTextChange(String newText) {
								// Called when the action bar search text has
								// changed. Since this
								// is a simple array adapter, we can just have
								// it do the filtering.
								mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
								Log.i("LoaderCustom", "Text Filter: " + mCurFilter);
								//mAdapter.getFilter().filter(mCurFilter);
								getLoaderManager().restartLoader(0, null, ServiceListFragment.this);
								
								return true;
							}
						});
				item.setActionView(searchView);
			}
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Insert desired behavior here.
			Log.i("LoaderCustom", "Item clicked: " + id);
			
			Intent intent = new Intent();
	        intent.setClass(getActivity(), PullToRefreshActivity.class);
	        startActivity(intent);
		}

		@Override
		public Loader<List<Service>> onCreateLoader(int id, Bundle args) {
			// This is called when a new Loader needs to be created. This
			// sample only has one Loader with no arguments, so it is simple.
			
			Log.i(TAG, "on create loader: " + mCurFilter);			
			return new ServiceListLoader(getActivity(), mCurFilter);
		}

		@Override
		public void onLoadFinished(Loader<List<Service>> loader, List<Service> data) {
			
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
		public void onLoaderReset(Loader<List<Service>> loader) {
			// Clear the data in the adapter.
			mAdapter.setData(null);
		}
	}

}
