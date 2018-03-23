package com.conferencecentral.api.spi;

import static com.conferencecentral.api.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.conferencecentral.api.LoadGroups.Everything;
import com.conferencecentral.api.LoadGroups.Stopper;
import com.conferencecentral.api.domain.Order;
import com.conferencecentral.api.domain.Product;
import com.conferencecentral.api.domain.Profile;
import com.conferencecentral.api.form.OrderForm;
import com.conferencecentral.api.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.conferencecentral.api.Constants;

@Api(name = "order", version = "v1",
        scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.WEB_CLIENT_ID,
        Constants.API_EXPLORER_CLIENT_ID },
        description = "API for the conferencecentral Backend application.")
public class OrderApi {

    private static Profile getProfileFromUser(User user) {
        Profile profile = ofy().load().key(
                Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            String email = user.getEmail();
            profile = new Profile(user.getUserId(),
                    extractDefaultDisplayNameFromEmail(email), email, TeeShirtSize.NOT_SPECIFIED);
        }
        return profile;
    }
    
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }
    
    @ApiMethod(name = "createOrder", path = "order", httpMethod = HttpMethod.POST)
    public Order createOrder(final User user,
    		final OrderForm orderForm)
        throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Profile profile = getProfileFromUser(user);
        Order order = new Order(profile, orderForm);
        Key<Order> orderKey = ofy().save().entity(order).now();
        Order fetchedOrder = ofy().load().key(orderKey).now();
        return fetchedOrder;
    }

    @ApiMethod(name = "createProduct", path = "product", httpMethod = HttpMethod.POST)
    public Product createProduct(@Named("id") final String id,
    			@Named("title") final String title) {
    		ofy().load().type(Product.class).limit(1).keys(); // TODO Work around Product.class not registered error
        Product product = new Product(id, title);
        Key<Product> productKey = ofy().save().entity(product).now();
        Product fetchedProduct = ofy().load().key(productKey).now();
        return fetchedProduct;
    }

    @ApiMethod(
	    name = "queryOrdersNofilter",
	    path = "queryOrdersNofilter",
	    httpMethod = HttpMethod.POST
    )
    public List<Order> queryOrdersNofilter() {
        Query<Order> query = ofy().load().group(Everything.class).type(Order.class);
        return query.list();
    }

    @ApiMethod(
	    name = "queryOrdersByProductAdmin",
	    path = "queryOrdersByProductAdmin",
	    httpMethod = HttpMethod.POST
    )
    public List<Order> queryOrdersByProductAdmin(@Named("id") String id) {
			Key<Product> pk = Key.create(Product.class, id);
        Query<Order> query = ofy().load().group(Everything.class).type(Order.class).filter("products", pk);
        return query.list();
    }

    @ApiMethod(
	    name = "queryOrdersByProductsAdmin",
	    path = "queryOrdersByProductsAdmin",
	    httpMethod = HttpMethod.POST
    )
    public List<Order> queryOrdersByProductsAdmin(@Named("ids") List<String> ids) {
			Object[] pks = ids.stream().map(x -> Key.create(Product.class, x)).toArray();
        Query<Order> oq = ofy().load().group(Everything.class).type(Order.class).filter("products IN", pks);
        List<Order> orders = oq.list();
//        List<OrderProfile> opl = new ArrayList<OrderProfile>(orders.size());
//        for(Order o: orders) {
////        		Key<Profile> k = o.getProfile().getKey();
////        		Profile p = o.getProfile().getValue();
//        		OrderProfile op = new OrderProfile();
//        		op.order = o;
//        		op.profile = o.getProfile();
//        		opl.add(op);
//        }
        return orders;
    }
    
    class OrderProfile {
    		public Order order;
    		public Ref<Profile> profile;
    }

    @ApiMethod(
    	    name = "queryOrdersByProducts",
    	    path = "queryOrdersByProducts",
    	    httpMethod = HttpMethod.POST
    )
    public List<Order> queryOrdersByProducts(
    			final User user,
    			@Named("ids") final List<String> ids) throws UnauthorizedException {
        if (null == user) {
            throw new UnauthorizedException("Authorization required");
        }
        final Profile profile = getProfileFromUser(user);
		final Object[] pks = ids.stream().map(x -> Key.create(Product.class, x)).toArray();
        final Query<Order> query = ofy().load().type(Order.class).
        		ancestor(profile).filter("products IN", pks);
        return query.list();
    }    
    
    
}
