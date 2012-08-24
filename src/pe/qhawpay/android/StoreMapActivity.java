package pe.qhawpay.android;

import java.util.ArrayList;

import pe.qhawpay.android.application.QhawpayApplication;
import pe.qhawpay.android.domain.Store;
import pe.qhawpay.android.util.QhawpayMapUtil;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class StoreMapActivity extends SherlockMapActivity {

	private Store currentStore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.store_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);

		currentStore = ((QhawpayApplication) getApplicationContext())
				.getStore();

		setTitle(currentStore.getName());

		getSupportActionBar().setSubtitle("Mapa");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		QhawpayMapUtil mapUtil = new QhawpayMapUtil(
				currentStore.getAddresses(), currentStore.getName());

		BasicItemizedOverlay itemizedOverlay = new BasicItemizedOverlay(
				getResources().getDrawable(R.drawable.ic_location_place), this);

		for (OverlayItem overlayItem : mapUtil.getMapAddresses()) {
			itemizedOverlay.addOverlay(overlayItem);
		}

		mapView.getOverlays().add(itemizedOverlay);

		mapView.getController().setZoom(12);

		mapView.setBuiltInZoomControls(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class BasicItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		Context mContext;

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public BasicItemizedOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}
	}
}