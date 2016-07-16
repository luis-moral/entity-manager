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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.molabs.ecs.Component;
import es.molabs.ecs.EntityManager;
import es.molabs.ecs.System;

public abstract class AbstractSystem implements System
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean concurrent;
	
	private Integer id = null;
	private EntityManager entityManager = null;
		
	protected AbstractSystem(boolean concurrent)
	{
		this.concurrent = concurrent;		
	}

	public boolean isConcurrent()
	{
		return concurrent;
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
		
		logger.debug("Created.");
	}
	
	public void dispose()
	{
		onDispose();
		
		entityManager = null;
		id = null;
		
		logger.debug("Disposed.");
	}
	
	public void componentAdded(Component component)
	{
		onComponentAdded(component);
	}
	
	public void componentRemoved(Component component)
	{		
		onComponentRemoved(component);
	}
	
	public void update(float delta)
	{		
		onUpdate(delta);
	}
	
	/**
	 * Reserves and initializes any resource needed by this system. 
	 */
	protected void onCreate()
	{		
	}
	
	/**
	 * Releases all resources reserved by this system.
	 */
	protected void onDispose()
	{		
	}
	
	protected void onUpdate(float delta)
	{		
	}
	
	protected void onComponentAdded(Component component)
	{		
	}	
	
	protected void onComponentRemoved(Component component)
	{		
	}
}
