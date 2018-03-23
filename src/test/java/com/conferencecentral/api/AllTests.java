package com.conferencecentral.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.conferencecentral.api.domain.OrderTest;
import com.conferencecentral.api.domain.ProfileTest;


@RunWith(Suite.class)
@SuiteClasses({
    ProfileTest.class,
    OrderTest.class
})
public class AllTests {}
