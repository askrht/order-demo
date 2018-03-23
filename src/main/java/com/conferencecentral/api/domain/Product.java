package com.conferencecentral.api.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Cache
@Entity
public class Product {

	@Id private String id;
    @Index private String title;
    @Ignore private String websafeId;
    
    @SuppressWarnings("unused")
	private Product() {}
    
    public Product (String id, String title) {
    		this.id = id;
    		this.title = title;
    		this.websafeId = Key.create(this).toWebSafeString();
    }
    
    public String getWebsafeId() {
    		return websafeId;
    }

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}
 
}

