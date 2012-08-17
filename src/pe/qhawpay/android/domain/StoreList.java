package pe.qhawpay.android.domain;

import java.util.List;


/**
 * @author dtataje
 *
 *API REST for list stores by service:
 *
 *http://backend.qhawpay.pe/api.php/store/service/service_id/0/store_id/a/100/1.json
 *
 *http://backend.qhawpay.pe/api.php/store/category/category_id/0/store_id/a/100/1.json
 */
public class StoreList {

	private List<Store> stores;

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
}
