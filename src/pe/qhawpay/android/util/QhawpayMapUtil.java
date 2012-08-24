package pe.qhawpay.android.util;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import pe.qhawpay.android.domain.Address;

public class QhawpayMapUtil {

	private List<Address> addresses;
	
	private String storeName;
	
	private List<OverlayItem> mapAddresses = new ArrayList<OverlayItem>();

	public QhawpayMapUtil(List<Address> inputAddresses, String inputStoreName) {
		addresses = inputAddresses;
		this.storeName = inputStoreName;
	}

	public List<OverlayItem> getMapAddresses() {
		
		int i = 0;

		for (Address address : addresses) {
			i++;
			double latitude = Double.parseDouble(address.getLatitude());
			double longitude = Double.parseDouble(address.getLongitude());

			GeoPoint geoPointTemp = new LatLonPoint(latitude, longitude);

			OverlayItem overlayTemp = new OverlayItem(geoPointTemp, storeName+" "+i, "Dirección: "+address.getAddress()+"\nTeléfono: "+address.getPhone());

			mapAddresses.add(overlayTemp);
		}

		return mapAddresses;
	}

	private static final class LatLonPoint extends GeoPoint {
		public LatLonPoint(double latitude, double longitude) {
			super((int) (latitude * 1E6), (int) (longitude * 1E6));
		}
	}

}
