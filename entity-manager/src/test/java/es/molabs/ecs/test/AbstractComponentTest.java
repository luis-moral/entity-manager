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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import es.molabs.ecs.EntityManager;
import es.molabs.ecs.test.entity.TestComponent;
import es.molabs.ecs.test.entity.TestEntity;

@RunWith(MockitoJUnitRunner.class)
public class AbstractComponentTest 
{	
	@Test
	public void testAbstractComponent() throws Throwable
	{		
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an entity
		TestEntity entity = new TestEntity();
		
		// Registers the entity
		entityManager.registerEntity(entity);
		
		// Creates the component
		TestAbstractComponent component = Mockito.spy(new TestAbstractComponent());		
		
		// Registers the component for the entity
		entityManager.registerComponent(entity.getId(), component);
		
		// Checks that it has an id
		Assert.assertEquals(Integer.valueOf(1), component.getId());
		
		// Checks that its id is the same that the entity id
		Assert.assertEquals(entity.getId(), component.getEntityId());
		
		// Checks that onCreate was called
		Mockito.verify(component, Mockito.times(1)).onCreate();
		
		// Unregisters the component
		entityManager.unregisterComponent(component.getId());
		
		// Checks that onDispose was called
		Mockito.verify(component, Mockito.times(1)).onDispose();
		
		// Checks that it has no id
		Assert.assertEquals(null, component.getId());
				
		// Checks that the entity has no id
		Assert.assertEquals(null, component.getEntityId());
		
		// Destroys the manager
		entityManager.destroy();		
	}
	
	private class TestAbstractComponent extends TestComponent
	{
		public TestAbstractComponent() 
		{
			super(1, 5);
		}
		
		protected void onCreate()
		{		
		}
		
		protected void onDispose()
		{		
		}
	}
}
