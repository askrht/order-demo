package com.conferencecentral.api.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.conferencecentral.api.service.OfyService.ofy;
import com.conferencecentral.api.LoadGroups.Everything;
import com.conferencecentral.api.LoadGroups.Stopper;
import com.conferencecentral.api.form.OrderForm;
import com.conferencecentral.api.form.OrderQty;
import com.google.common.collect.ImmutableMap;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Cache
@Entity
public class Order {
    @Id private Long id;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    @Parent @Load public Ref<Profile> profile;
//    @Parent @Load(value={Everything.class}, unless=Stopper.class) Ref<Profile> profile;
    @Index private String title;
    @Load @Index List<Ref<Product>> products = new ArrayList<Ref<Product>>(10);
    Map<String, OrderedProduct> orderedProducts = new HashMap<String, OrderedProduct>(10);
    // @Ignore Profile parent;

	@SuppressWarnings("unused")
	private Order() {}
	
    public Order(final Profile profile,
    			final OrderForm orderForm
    		) {
    		 this.profile = Ref.create(Key.create(Profile.class, profile.getUserId()));
    		// this.parent = profile;
    		final List<OrderQty> oql = orderForm.getOrderQty();
    		// pre-fetch ordered products
        final List<Key<Product>> fetchList = new ArrayList<>(oql.size());
        for (OrderQty oq : oql) {
        		fetchList.add(Key.create(Product.class, oq.productId));
        }
        final Map<Key<Product>, Product> fetched = ofy().load().keys(fetchList);
        final List<Ref<Product>> prods = new ArrayList<Ref<Product>>(fetched.size());
        final Map<String, OrderedProduct> opl = new HashMap<String, OrderedProduct>(fetched.size());
        for (OrderQty oq : oql) {
        		final OrderedProduct op = new OrderedProduct(oq.qty);
        		final Key<Product> key = Key.create(Product.class, oq.productId);
        		final Product p = fetched.get(key);
        		final Ref<Product> pr = Ref.create(p);
    			prods.add(pr);
    			opl.put(p.getId(), op);
        }
        this.products = ImmutableList.copyOf(prods);
        this.orderedProducts = ImmutableMap.copyOf(opl);
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Ref<Profile> getProfile() {
		return this.profile;
	}
    
    // public Profile getParent() {
    // 		return this.parent;
    // }

    // public void setParent(Profile parent) {
		// 	this.parent = parent;
		// }

	public Long getId() {
		return this.id;
	}

	public Map<String, OrderedProduct> getOrderedProducts() {
		return ImmutableMap.copyOf(this.orderedProducts);
	}

	public List<Product> getProducts() {
				final List<Product> pl = new ArrayList<Product>(this.products.size());
				for (Ref<Product> rp : this.products) {
						Product p = rp.get();
						pl.add(p);
				}
				return pl;
	}

}
