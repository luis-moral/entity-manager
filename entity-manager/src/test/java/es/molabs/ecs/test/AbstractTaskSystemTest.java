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
package es.molabs.ecs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import es.molabs.ecs.Component;
import es.molabs.ecs.EntityManager;
import es.molabs.ecs.test.entity.TestComponent;
import es.molabs.ecs.test.entity.TestEntity;
import es.molabs.ecs.test.entity.TestTaskSystem;
import es.molabs.task.Task;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTaskSystemTest 
{	
	@Test
	public void testAbstractSystem() throws Throwable
	{
		float DELTA = 0.16f;
		
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates a task for the system
		TestTask task = Mockito.spy(new TestTask());
		
		// Creates the system
		TestAbstractSystem system = Mockito.spy(new TestAbstractSystem(false, task));		
		
		// Registers the system
		entityManager.registerSystem(system);
		
		// Checks that onCreate was called
		Mockito.verify(system, Mockito.times(1)).onCreate();
		
		// Creates an entity
		TestEntity entity = new TestEntity();
		
		// Registers the entity
		entityManager.registerEntity(entity);		
		
		// Creates a component
		TestComponent component = new TestComponent(1, 5);
		
		// Registers the component for the entity
		entityManager.registerComponent(entity.getId(), component);		
		
		// Calls update one time
		entityManager.update(DELTA);
		
		// Checks that execute was called one time in the task
		Mockito.verify(task, Mockito.times(1)).execute(Mockito.anyFloat());
		
		// Destroys the manager
		entityManager.destroy();
		
		// Checks that onDispose was called
		Mockito.verify(system, Mockito.times(1)).onDispose();
	}
	
	private class TestAbstractSystem extends TestTaskSystem
	{
		private Task task = null;
		
		public TestAbstractSystem(boolean concurrent, Task task) 
		{
			super(concurrent);
			
			this.task = task;
		}
		
		protected void onCreate()
		{		
		}
		
		protected void onDispose()
		{		
		}
		
		protected void onComponentRemoved(Component component)
		{		
		}
		
		protected Task onComponentAdded(Component component)
		{
			return task;
		}
	}
	
	private class TestTask implements Task
	{
		public void execute(float delta) 
		{		
		}	

		public void added() 
		{
		}

		public void removed() 
		{
		}

		public boolean isFinished() 
		{
			return false;
		}
		
		public void reset() 
		{			
		}

		public Task getNext() 
		{
			return null;
		}		
	}
}
