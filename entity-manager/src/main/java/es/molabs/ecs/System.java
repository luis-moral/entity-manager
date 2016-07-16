/**
 * Copyright (C) 2016 Luis Moral Guerrero <luis.moral@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.molabs.ecs;

public interface System
{	
	/**
	 * Returns if this system would run in its own thread or not.
	 * 
	 * @return if this system would run in its own thread or not.
	 */
	public boolean isConcurrent();
	
	public Integer getId();
	
	/**
	 * Reserves and initializes any resource needed by this system.
	 * 
	 * @param id of this system.
	 * @param entityManager that manages this system.
	 */
	public void create(int id, EntityManager entityManager);
	
	/**
	 * Releases all resources reserved by this system.
	 */
	public void dispose();
		
	public void componentAdded(Component component);
	
	public void componentRemoved(Component component);
	
	public void update(float delta);
}
