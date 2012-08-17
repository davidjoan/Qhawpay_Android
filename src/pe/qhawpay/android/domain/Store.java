package pe.qhawpay.android.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 * @author dtataje
 * 
 *example of API REST GET 
 *
 *http://backend.qhawpay.pe/api.php/store/{slug of the store}.json
 *
 *{ 
    "id" : "1",
    "customer" : 
    {
    "id" : 2, 
    "realname" : "David Tataje",
    "username" : "dtataje",
    "email" : "dtataje@qhawpay.pe",
    "picture" : "",
    "slug" : "dtataje",
    },   
    "categories" : [{"id" : "1","name" : "Tienda","slug" : "tienda"}   ],     
    "services" : [{"id" : "4","name" : "Teléfono","slug" : "telefono"}  ],    
    "addresses" : [{ "id" : "1", "city" : "Lima", "address" : "Ampliacion Oeste Mz P2 Lte 15, Lima 036, PerÃº", "zip-code" : "lima 36", "phone" : "392-6855",
        "fax" : "392-6855", "mobile" : "997280378", "slug" : "array" }  ],    
    "photos" : [ {"id" : "2","name" : "logo","content" : "","path" : "98323a0e4fc5111190d65b46a7609b2896b9df03.png","slug" : "logo"}   ],
    "ruc" : "",
    "name" : "Bodega la vecina",
    "logo" : "",
    "url" : "",
    "phrase" : "Precios mas bajos siempre",
    "info" : "creada por la seÃ±ara tal el dia 1999",
    "description" : "bodega tradicional",
    "datetime" : "1921-11-30 00:00:00",
    "qty-votes" : "5",
    "status" : "Activado",
    "slug" : "bodega_la_vecina"
    }
 */
public class Store {
	
	private Long  id;
	
	private Customer customer;
	
	private List<Category> categories;
	
	private List<Service> services;
	
	private List<Address> addresses;
	
	private List<Photo> photos;
		
    private String ruc;
    
    private String name;
    
    private String logo;
    
    private String url;
    
    private String phrase;
    
    private String info;
    
    private String description;
    
    private String datetime;
    
    @SerializedName(value="qty-votes")
    private String qtyVotes;
    
    private String status;
    
    private String slug;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getQtyVotes() {
		return qtyVotes;
	}

	public void setQtyVotes(String qtyVotes) {
		this.qtyVotes = qtyVotes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
