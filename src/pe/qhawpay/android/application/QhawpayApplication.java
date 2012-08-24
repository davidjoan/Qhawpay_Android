package pe.qhawpay.android.application;

import pe.qhawpay.android.domain.Store;
import android.app.Application;

public class QhawpayApplication extends Application {
	
	private Store store;
	
	private String qtyRecords;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getQtyRecords() {
		return (qtyRecords == null) ? "10" : qtyRecords;
	}

	public void setQtyRecords(String qtyRecords) {
		this.qtyRecords = qtyRecords;
	}
	
	
}
