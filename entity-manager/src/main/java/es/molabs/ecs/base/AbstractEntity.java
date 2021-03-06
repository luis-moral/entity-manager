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
package es.molabs.ecs.base;

import es.molabs.ecs.Entity;
import es.molabs.ecs.EntityManager;

public abstract class AbstractEntity implements Entity 
{
	private Integer id = null;
	private EntityManager entityManager = null;
	
	protected AbstractEntity()
	{		
	}
	
	public Integer getId()
	{
		return id;
	}
	
	protected EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	public void create(int id, EntityManager entityManager)
	{
		this.id = Integer.valueOf(id);
		this.entityManager = entityManager;
		
		onCreate();
	}
	
	public void dispose()
	{
		onDispose();
		
		entityManager = null;
		id = null;
	}
	
	/**
	 * Reserves and initializes any resource needed by this entity. 
	 */
	protected void onCreate()
	{		
	}
	
	/**
	 * Releases all resources reserved by this entity.
	 */
	protected void onDispose()
	{		
	}
}
