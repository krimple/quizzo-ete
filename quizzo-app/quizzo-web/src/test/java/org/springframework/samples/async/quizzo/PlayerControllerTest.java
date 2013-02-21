package org.springframework.samples.async.quizzo;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.async.config.AppConfig;
import org.springframework.samples.async.config.WebMvcConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

//for the MVC test DSL

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class })

public class PlayerControllerTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(this.context).build();
    }

    @Test
    public void testContext() {
        Assert.assertNotNull(context);
    }
}
