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

import java.util.HashMap;
import java.util.Map;

import es.molabs.ecs.Component;
import es.molabs.ecs.EntityManager;
import es.molabs.ecs.System;
import es.molabs.task.Task;
import es.molabs.task.TaskExecutor;
import es.molabs.task.base.SingleThreadTaskExecutor;

public abstract class AbstractTaskSystem implements System
{
	private boolean concurrent;
	
	private Integer id = null;
	private EntityManager entityManager = null;
	
	private Map<Component, Task> componentMap = null;
	private TaskExecutor taskExecutor = null;
		
	protected AbstractTaskSystem(boolean concurrent)
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
	
	protected TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}
	
	public void create(int id, EntityManager entityManager)
	{
		this.id = Integer.valueOf(id);
		this.entityManager = entityManager;
		
		componentMap = new HashMap<Component, Task>();
		
		taskExecutor = new SingleThreadTaskExecutor();		
		
		onCreate();
	}
	
	public void dispose()
	{
		onDispose();			
		
		taskExecutor.clear();
		taskExecutor = null;
		
		componentMap.clear();
		componentMap = null;
		
		entityManager = null;
		id = null;		
	}
	
	public void componentAdded(Component component)
	{
		Task task = onComponentAdded(component);
				
		if (task != null)
		{
			componentMap.put(component, task);
			taskExecutor.add(task);
		}
	}
	
	public void componentRemoved(Component component)
	{
		Task task = componentMap.remove(component);
		
		if (task != null)
		{		
			taskExecutor.remove(task);
		}
		
		onComponentRemoved(component);
	}
	
	public void update(float delta)
	{		
		taskExecutor.execute(delta);
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
	
	protected void onComponentRemoved(Component component)
	{		
	}
	
	protected abstract Task onComponentAdded(Component component);
}
