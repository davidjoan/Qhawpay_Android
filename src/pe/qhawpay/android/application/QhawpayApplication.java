package pe.qhawpay.android.application;

import pe.qhawpay.android.domain.Store;
import android.app.Application;

public class QhawpayApplication extends Application {
	
	private Store store;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
}
