package org.springframework.samples.async.quizzo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.examples.quizzo.domain.Choice;
import org.springframework.data.examples.quizzo.domain.MultipleChoiceQuestion;
import org.springframework.samples.async.config.AppConfig;
import org.springframework.samples.async.config.WebMvcConfig;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

//for the MVC test DSL

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebMvcConfig.class })
public class QuizControllerTest {

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

	@Test
	@DirtiesContext
	// todo - mock backend to use fake JavascriptQuiz - using real one for
	// now...
	public void testStartQuizOk() throws Exception {
		this.mvc.perform(post("/quiz/start/JavascriptQuiz")).andExpect(
				status().isOk());
	}

	@Test
	@DirtiesContext
	public void testStartQuizAskQuestionAndGetAnswer() throws Exception {
		this.mvc.perform(post("/quiz/start/JavascriptQuiz")).andExpect(
				status().isOk());
		
		MvcResult result = this.mvc.perform(get("/quiz/nextq"))
				.andExpect(status().isOk()).andReturn();
		
		Object async = result.getAsyncResult(15000L); 
		Assert.assertTrue(async.getClass().isAssignableFrom(MultipleChoiceQuestion.class));

        // assume that there is an answer 'a'
        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) async;
        Choice choice = mcq.getChoices().get('a');

        //this.mvc.perform(post("/quiz/answer").param("choice", choice);
	}

    @Test
    @DirtiesContext
    public void testStartQuizTwiceShouldFail() throws Exception {
        this.mvc.perform(post("/quiz/start/JavascriptQuiz")).andExpect(
                status().isOk());

        this.mvc.perform(post("/quiz/start/FooBarBaz")).andExpect(
                status().isBadRequest());

    }

}
