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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class InMemoryChatRepository implements ChatRepository {

	private final List<String> messages = new CopyOnWriteArrayList<String>();

	public ChatMessages getMessages(Integer index) {
        if (index == null) {
            // get the whole shebang - good at the beginning to sync up
            return new ChatMessages(this.messages.size(), this.messages);
        }
		if (this.messages.isEmpty()) {
			return new ChatMessages(0, Collections.<String> emptyList());
		}
		Assert.isTrue(index <= this.messages.size(), "Invalid message index:" + index);
        int size = this.messages.size();
		return new ChatMessages(size, this.messages.subList(index, size));
	}

	public void addMessage(String message) {
		this.messages.add(message);
	}
}
