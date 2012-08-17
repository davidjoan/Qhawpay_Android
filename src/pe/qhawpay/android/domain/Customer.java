package pe.qhawpay.android.domain;


/**
 * @author dtataje
 *
 * Example of the REST API for Customer
 *    "customer" : 
    {
    "id" : 2, 
    "realname" : "David Tataje",
    "username" : "dtataje",
    "email" : "dtataje@qhawpay.pe",
    "picture" : "",
    "slug" : "dtataje",
    },   
 */
public class Customer {
	
	private Long id;
	
	private String realname;
	
	private String username;
	
	private String email;
	
	private String picture;
	
	private String slug;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
