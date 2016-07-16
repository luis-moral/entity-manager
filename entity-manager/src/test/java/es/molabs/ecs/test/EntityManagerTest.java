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

import es.molabs.ecs.Component;
import es.molabs.ecs.Entity;
import es.molabs.ecs.EntityManager;
import es.molabs.ecs.test.entity.ChildTestComponent;
import es.molabs.ecs.test.entity.OtherTestComponent;
import es.molabs.ecs.test.entity.TestAssemblage;
import es.molabs.ecs.test.entity.TestComponent;
import es.molabs.ecs.test.entity.TestDestroySystem;
import es.molabs.ecs.test.entity.TestEntity;
import es.molabs.ecs.test.entity.TestSystem;
import es.molabs.ecs.test.entity.TestTaskSystem;

@RunWith(MockitoJUnitRunner.class)
public class EntityManagerTest 
{
	@Test
	public void testInitialization() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();		
				
		// Checks that it is initialized
		boolean expectedValue = true;
		boolean value = entityManager.isInitialized();
		Assert.assertEquals("Value must be [" + expectedValue + "].", expectedValue, value);
				
		// Destroys the manager
		entityManager.destroy();
		
		// Checks that it is destroyed
		expectedValue = false;
		value = entityManager.isInitialized();
		Assert.assertEquals("Value must be [" + expectedValue + "].", expectedValue, value);
		
		// Initializes the manager again
		entityManager.init();
		
		// Checks that it is initialized
		expectedValue = true;
		value = entityManager.isInitialized();
		Assert.assertEquals("Value must be [" + expectedValue + "].", expectedValue, value);
		
		// Destroys the manager
		entityManager.destroy();
	}
	
	@Test
	public void testEntity() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an entity
		TestEntity entity = Mockito.spy(new TestEntity());
		
		// Registers the entity
		entityManager.registerEntity(entity);
		
		// Saves its id
		int id = entity.getId();
		
		// Checks that create has been called
		Mockito.verify(entity, Mockito.times(1)).create(Mockito.anyInt(), Mockito.any(entityManager.getClass()));
		
		// DesRegisters the entity
		entityManager.unregisterEntity(entity.getId());
				
		// Checks that dispose have been called
		Mockito.verify(entity, Mockito.times(1)).dispose();
		
		// Checks that the manager does not longer exists
		Assert.assertEquals(null, entityManager.getEntity(id));				
		
		// Checks thats there is no entities left in the manager
		Assert.assertEquals(0, entityManager.getEntityCount());
		
		// Registers the entity again
		entityManager.registerEntity(entity);
		
		// Destroys the manager
		entityManager.destroy();
		
		// Checks that dispose have been called
		Mockito.verify(entity, Mockito.times(2)).dispose();
	}
	
	@Test
	public void testComponent() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an entity
		TestEntity entity = new TestEntity();
		
		// Registers the entity
		entityManager.registerEntity(entity);
		
		// Creates a component
		TestComponent component = Mockito.spy(new TestComponent(1, 5));
		
		// Registers the component
		entityManager.registerComponent(entity.getId(), component);
		
		// Saves its id
		int id = component.getId();
		
		// Checks that create has been called
		Mockito.verify(component, Mockito.times(1)).create(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(entityManager.getClass()));
		
		// Unregisters the component
		entityManager.unregisterComponent(component.getId());
				
		// Checks that dispose has been called
		Mockito.verify(component, Mockito.times(1)).dispose();
		
		// Checks that the manager does not longer exists
		Assert.assertEquals(null, entityManager.getComponent(id));
		
		// Checks that there is no components in the entity manager
		Assert.assertEquals(0, entityManager.getComponentCount());
		
		// Registers the component again
		entityManager.registerComponent(entity.getId(), component);
		
		// Unregisters the entity
		entityManager.unregisterEntity(entity.getId());
		
		// Checks that dispose has been called
		Mockito.verify(component, Mockito.times(2)).dispose();
		
		// Checks that there is no components in the entity manager
		Assert.assertEquals(0, entityManager.getComponentCount());
		
		// Registers the entity again
		entityManager.registerEntity(entity);
		
		// Registers the component again
		entityManager.registerComponent(entity.getId(), component);
		
		// Destroys the manager
		entityManager.destroy();
		
		// Checks that dispose has been called
		Mockito.verify(component, Mockito.times(3)).dispose();
	}
	
	@Test
	public void testSystem() throws Throwable
	{
		float DELTA = 0.16f;
		
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates a system
		TestSystem system = Mockito.spy(new TestSystem(false));		
		
		// Registers the system
		entityManager.registerSystem(system);
		
		// Saves its id
		int id = system.getId();
		
		// Checks that create has been called
		Mockito.verify(system, Mockito.times(1)).create(Mockito.anyInt(), Mockito.any(entityManager.getClass()));
		
		// Calls update once
		entityManager.update(DELTA);
		
		// Checks that update has been called
		Mockito.verify(system, Mockito.times(1)).update(DELTA);
		
		// Unregisters the system
		entityManager.unregisterSystem(system.getId());
		
		// Checks that System.dispose() has been called
		Mockito.verify(system, Mockito.times(1)).dispose();
		
		// Checks that the manager does not longer exists
		Assert.assertEquals(null, entityManager.getSystem(id));
		
		// Checks that there is no systems in the entity manager
		Assert.assertEquals(0, entityManager.getSystemCount());
		
		// Registers the system again
		entityManager.registerSystem(system);
		
		// Destroys the manager
		entityManager.destroy();		
		
		// Checks that System.dispose() has been called
		Mockito.verify(system, Mockito.times(2)).dispose();
	}
	
	/**
	 * This will test that System.update() is not called if a system has destroyed the entity manager on his update call.
	 * 
	 * @throws Throwable if an error occurred.
	 */
	@Test
	public void testUpdateAfterDestroy() throws Throwable
	{
		float DELTA = 0.16f;
		
		// Creates the entity manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates a system that will destroy the entityManager on his update call
		TestDestroySystem destroySystem = Mockito.spy(new TestDestroySystem(false));
		
		// Creates a system
		TestSystem system = Mockito.spy(new TestSystem(false));				
				
		// Registers the systems
		entityManager.registerSystem(destroySystem);
		entityManager.registerSystem(system);		
				
		// Calls update
		entityManager.update(DELTA);		
		
		// Checks that update has not been called after the entity manager was destroyed		
		Mockito.verify(destroySystem, Mockito.times(1)).update(Mockito.anyFloat());
		Mockito.verify(system, Mockito.times(0)).update(Mockito.anyFloat());
		
		// Destroys the entityManager
		entityManager.destroy();
	}
	
	@Test
	public void testGetSystem() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
				
		// Gets a system of the same class and checks that does not exists in the entity manager
		Assert.assertEquals(null, entityManager.getSystemByClass(TestTaskSystem.class));
		
		// Creates a system
		TestSystem system = new TestSystem(false);		
		
		// Registers the system
		entityManager.registerSystem(system);
		
		// Gets a system of the same class and checks that exists in the entity manager
		Assert.assertEquals(system, entityManager.getSystemByClass(TestSystem.class));
		
		// Unregisters the system
		entityManager.unregisterSystem(system.getId());
		
		// Gets a system of the same class and checks that does not exists in the entity manager
		Assert.assertEquals(null, entityManager.getSystemByClass(TestTaskSystem.class));
		
		// Destroys the manager
		entityManager.destroy();
	}
	
	@Test
	public void testComponentSystem() throws Throwable
	{		
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates a system
		TestSystem system = Mockito.spy(new TestSystem(false));		
		
		// Registers the system
		entityManager.registerSystem(system);
		
		// Creates an entity
		TestEntity entity = new TestEntity();
				
		// Registers the entity
		entityManager.registerEntity(entity);
		
		// Creates a component
		TestComponent component = new TestComponent(1, 5);
				
		// Registers the component
		entityManager.registerComponent(entity.getId(), component);
		
		// Checks that addComponent has been called
		Mockito.verify(system, Mockito.times(1)).componentAdded(Mockito.any());
		
		// Registers a component of another class
		entityManager.registerComponent(entity.getId(), new OtherTestComponent(17));
		
		// Checks that componentAdded has been called once per component added
		Mockito.verify(system, Mockito.times(2)).componentAdded(Mockito.any());
		
		// Registers a component of a child class
		entityManager.registerComponent(entity.getId(), new ChildTestComponent(1, 5, 7));
		
		// Checks that componentAdded has been called once per component added
		Mockito.verify(system, Mockito.times(3)).componentAdded(Mockito.any());
		
		// Unregisters the component
		entityManager.unregisterComponent(component.getId());
		
		// Checks that componentRemoved has been called
		Mockito.verify(system, Mockito.times(1)).componentRemoved(Mockito.any());
		
		// Destroys the manager
		entityManager.destroy();		
		
		// Checks that dispose have been called
		Mockito.verify(system, Mockito.times(1)).dispose();
	}
	
	/**
	 * This will test that System.componentAdded() will be called if there was components before registering the system.
	 * 
	 * @throws Throwable if an error occurred.
	 */
	@Test
	public void testComponentSystemAfter() throws Throwable
	{		
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an entity
		Integer entityId = entityManager.registerEntity(new TestEntity()).getId();
		
		// Creates a component
		Component component = Mockito.mock(Component.class);
		
		// Registers the component for that entity
		entityManager.registerComponent(entityId, component);
		
		// Creates a system
		TestSystem system = Mockito.spy(new TestSystem(false));
		
		// Registers the system
		entityManager.registerSystem(system);
		
		// Tests that component added was called
		Mockito.verify(system, Mockito.times(1)).componentAdded(component);
		
		// Destroys the manager
		entityManager.destroy();
	}
	
	@Test
	public void testAssemblage() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an assemblage
		TestAssemblage assemblage = Mockito.spy(new TestAssemblage(1, 5));
		
		// Registers the entity with the assemblage
		Entity entity = entityManager.registerEntity(assemblage);
		
		// Checks that createEntity has been called
		Mockito.verify(assemblage, Mockito.times(1)).createEntity(Mockito.any(entityManager.getClass()));
		
		// Unregisters the entity
		entityManager.unregisterEntity(entity.getId());
		
		// Destroys the manager
		entityManager.destroy();
	}
}
