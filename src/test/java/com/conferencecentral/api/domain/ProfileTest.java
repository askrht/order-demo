package com.conferencecentral.api.domain;

import static org.junit.Assert.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.conferencecentral.api.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for Profile POJO.
 */
public class ProfileTest {

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
    public void testGetters() throws Exception {
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
    }

    @Test
    public void testUpdate() throws Exception {
        String newDisplayName = "New Display Name";
        TeeShirtSize newTeeShirtSize = TeeShirtSize.M;
        profile.update(newDisplayName, newTeeShirtSize);
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(newDisplayName, profile.getDisplayName());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(newTeeShirtSize, profile.getTeeShirtSize());
    }

}
