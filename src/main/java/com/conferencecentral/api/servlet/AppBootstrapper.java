package com.conferencecentral.api.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.conferencecentral.api.domain.Order;
import com.conferencecentral.api.domain.Product;
import com.conferencecentral.api.domain.Profile;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class AppBootstrapper implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
//		ObjectifyService.factory().init();
		factory().register(Profile.class);
		factory().register(Order.class);
		factory().register(Product.class);	
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
