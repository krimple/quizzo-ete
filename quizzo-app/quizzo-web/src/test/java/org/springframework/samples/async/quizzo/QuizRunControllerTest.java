package org.springframework.samples.async.quizzo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.async.config.AppConfig;
import org.springframework.samples.async.config.WebMvcConfig;
import org.springframework.samples.async.quizzo.engine.QuizRunEngine;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

//for the MVC test DSL

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class })
public class QuizRunControllerTest {


    @Autowired
    WebApplicationContext context;

    @Autowired
    QuizRunEngine quizRunEngine;

    MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(this.context).build();
    }

    @Test
    public void testContext() {
        Assert.assertNotNull(context);
    }

    @Test
    @DirtiesContext
    public void testStartQuiz() throws Exception {
        MvcResult result = startGoodQuiz();
        Assert.assertTrue(result.getResponse().getContentAsString().contains("QuizStarted"));
    }

    private MvcResult startGoodQuiz() throws Exception {
        return this.mvc.perform(post("/quizRun/startQuiz/Javascript Quiz")).
                andReturn();
    }

    @Test(expected = NestedServletException.class)
    @DirtiesContext
    public void testNonExistantQuiz() throws Exception {
        MvcResult result = this.mvc.perform(post("/quizRun/startQuiz/JavascrBQuiz")).andReturn();
    }

    @Test
    @DirtiesContext
    public void testStartQuizAndAskForQuestion() throws Exception {
        startGoodQuiz();

        MvcResult result = this.mvc.perform(get("/quizRun/Javascript Quiz/currentQuestion"))
                .andReturn();

        Assert.assertTrue(result.getResponse().getContentAsString().contains("QuestionPending"));
    }

    @Test
    @DirtiesContext
    public void testStartQuizAskForQuestionAndAnswer() throws Exception {

        startGoodQuiz();
        MvcResult result = this.mvc.perform(get("/quizRun/Javascript Quiz/currentQuestion"))
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains("QuestionPending"));
        //MvcResult result = this.mvc.perform(



    }
}
