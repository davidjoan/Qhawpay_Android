package pe.qhawpay.android.domain;

import com.google.gson.annotations.SerializedName;


/**
 * @author dtataje
 *
 * Example of the REST API for address
 *    "addresses" : [
 *    { 
 *    "id" : "1",
 *    "city" : "Lima", 
 *    "address" : "Ampliacion Oeste Mz P2 Lte 15, Lima 036, PerÃº", 
 *    "zip-code" : "lima 36", 
 *    "phone" : "392-6855",
 *    "fax" : "392-6855", 
 *    "mobile" : "997280378", 
 *    "slug" : "array" }  
 *    ], 
 */
public class Address {
	
	private String id;
	
	private String city;
	
	private String address;
	
	@SerializedName(value="zip-code")
	private String zipCode;
	
	private String phone;
	
	private String fax;
	
	private String mobile;
	
	private String latitude;
	
	private String longitude;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
