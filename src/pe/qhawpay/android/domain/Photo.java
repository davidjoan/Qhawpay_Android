package pe.qhawpay.android.domain;


/**
 * @author dtataje
 *
 *Example of the rest api for Photos
 *
 *"photos" : [ 
 *  {
 *  "id" : "2",
 *  "name" : "logo",
 *  "content" : "",
 *  "path" : "98323a0e4fc5111190d65b46a7609b2896b9df03.png",
 *  "slug" : "logo"
 *  }   
 *  ],
 */
public class Photo {
	
	private Long id;
	
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	private String content;
	
	private String path;
	
	private String slug;

	

}
