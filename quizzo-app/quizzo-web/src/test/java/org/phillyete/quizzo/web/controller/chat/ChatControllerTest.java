/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.phillyete.quizzo.web.controller.chat;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

public class ChatControllerTest {

	private MockMvc mockMvc;

	private ChatRepository chatRepository;

	@Before
	public void setup() {
		this.chatRepository = EasyMock.createMock(ChatRepository.class);
		this.mockMvc = standaloneSetup(new ChatController(this.chatRepository)).build();
	}

	@Test
	public void getMessages() throws Exception {
        List<String> dummyMessages = new ArrayList<String>();
        for(int i = 0; i < 9; i++) {
            dummyMessages.add("value " + i);
        }
        ChatMessages expected = new ChatMessages(9, dummyMessages);
        expect(this.chatRepository.getMessages(9)).andReturn(expected);
		replay(this.chatRepository);

		this.mockMvc.perform(get("/mvc/chat").param("messageIndex", "9"))
				.andExpect(status().isOk())
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult(expected));

		verify(this.chatRepository);
	}

	@Test
	public void getMessagesStartAsync() throws Exception {
        List<String> dummyMessages = new ArrayList<String>();
        for(int i = 0; i < 9; i++) {
            dummyMessages.add("value " + i);
        }
		expect(this.chatRepository.getMessages(9))
                .andReturn(
                        new ChatMessages(9, dummyMessages));

		replay(this.chatRepository);

		this.mockMvc.perform(get("/mvc/chat").param("messageIndex", "9"))
				.andExpect(request().asyncStarted());

		verify(this.chatRepository);
	}

}
