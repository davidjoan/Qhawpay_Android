package pe.qhawpay.android.domain;

public class Service {
	
	private Long  id;
	
	private String name;
	
	private String description;
	
	private String image;
	
	private String slug;
	
	private String active;

	public Long  getId() {
		return id;
	}

	public void setId(Long  id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
