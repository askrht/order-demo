package com.conferencecentral.api.domain;

import static com.conferencecentral.api.service.OfyService.ofy;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.conferencecentral.api.form.OrderForm;
import com.conferencecentral.api.form.OrderQty;
import com.conferencecentral.api.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class OrderTest {
    private static final String EMAIL = "example@gmail.com";
    private static final String USER_ID = "123456789";
    private static final TeeShirtSize TEE_SHIRT_SIZE = TeeShirtSize.M;
    private static final String DISPLAY_NAME = "Your Name Here";
    private Profile profile;
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
    Closeable session;

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        session = ObjectifyService.begin();
        profile = new Profile(USER_ID, DISPLAY_NAME, EMAIL, TEE_SHIRT_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        session.close();
    }

    @Test
    public void testOrderCru() throws Exception {
        final Key<Profile> profileKey = ofy().save().entity(profile).now();
        final Product product = new Product("a1", "a title");
        final Key<Product> productKey = ofy().save().entity(product).now();
        final OrderQty oq = new OrderQty();
        oq.productId = "a1";
        oq.qty = 12;
        final List<OrderQty> oql = new ArrayList<OrderQty>(1);
        oql.add(oq);
        final OrderForm of = new OrderForm(oql);
        final Order order = new Order(profile, of);
        final Key<Order> orderKey = ofy().save().entity(order).now();
        assertNotNull("Profile Key is null", profileKey);
        assertNotNull("Order Key is null", orderKey);
        Order fetchedOrder = ofy().load().key(orderKey).now();
        assertNotNull("Cannot fetch order.", fetchedOrder);
        assertNotNull("Profile missing in fetched order.", fetchedOrder.getProfile().isLoaded());
//        assertEquals("Profile name in the order is not correct", profile.getDisplayName(), fetchedOrder.getProfile().getDisplayName());
    }

}
