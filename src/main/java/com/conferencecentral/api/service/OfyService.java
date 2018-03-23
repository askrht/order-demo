package com.conferencecentral.api.service;

import com.conferencecentral.api.domain.Order;
import com.conferencecentral.api.domain.Product;
import com.conferencecentral.api.domain.Profile;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Custom Objectify Service that this application should use.
 */
public class OfyService {

	/**
     * This static block ensure the entity registration.
     */
    static {
		ObjectifyService.register(Product.class);
		ObjectifyService.register(Order.class);
    	ObjectifyService.register(Profile.class);
    }
    /**
     * Use this static method for getting the Objectify service object in order to make sure the
     * above static block is executed before using Objectify.
     * @return Objectify service object.
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
    /**
     * Use this static method for getting the Objectify service factory.
     * @return ObjectifyFactory.
     */
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
