/*
 * Copyright 2002-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.phillyete.quizzo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

/**
 * @author David Turanski
 *
 */
public class Player implements Comparable<Player> {
	@Id
	private String name;

	public Player(String name) {
		Assert.hasText(name,"name cannot be null or blank");
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(Player p) {
        return name.compareTo(p.getName());
    }
}
