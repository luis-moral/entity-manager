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

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import es.molabs.ecs.test.entity.ChildTestComponent;
import es.molabs.ecs.test.entity.OtherTestComponent;
import es.molabs.ecs.test.entity.TestComponent;
import es.molabs.ecs.util.ComponentMap;

@RunWith(MockitoJUnitRunner.class)
public class ComponentMapTest 
{	
	@Test
	public void testGetById() throws Throwable
	{		
		// Creates a ComponentMap
		ComponentMap componentMap = new ComponentMap();
		
		// Creates a component
		TestComponent componentA = new TestComponent(1, 5);
		// Initializes it
		componentA.create(1, 1, null);
		
		// Creates another component
		OtherTestComponent componentB = new OtherTestComponent(5);
		// Initializes it
		componentB.create(2, 1, null);
		
		// Adds the component to the ComponentMap
		componentMap.put(componentA);
		componentMap.put(componentB);
		
		// Checks that it has 2 components
		Assert.assertEquals(2, componentMap.size());

		// Checks getting a component by id
		Assert.assertEquals(componentB, componentMap.getById(componentB.getId()));
		
		// Removes a component by id
		componentMap.removeById(componentB.getId());
		
		// Check that the removed component is no longer in ComponentMap
		Assert.assertEquals(null, componentMap.getById(componentB.getId()));
		
		// Clears the ComponentMap
		componentMap.clear();
		
		// Checks that its empty
		Assert.assertEquals(0, componentMap.size());
	}
	
	@Test
	public void testGetByEntityId() throws Throwable
	{		
		// Creates the ComponentMap
		ComponentMap componentMap = new ComponentMap();
		
		// Creates an initializes some components
		TestComponent componentA = new TestComponent(1, 5);		
		TestComponent componentB = new TestComponent(3, 4);
		OtherTestComponent componentC = new OtherTestComponent(5);
		TestComponent componentD = new TestComponent(2, 3);
		componentA.create(1, 1, null);
		componentB.create(2, 1, null);		
		componentC.create(3, 1, null);
		componentD.create(4, 2, null);
				
		// Adds the components
		componentMap.put(componentA);
		componentMap.put(componentB);
		componentMap.put(componentC);
		componentMap.put(componentD);
		
		// Checks that it has 3 components
		Assert.assertEquals(3, componentMap.size());

		// Checks that it has 2 components for the entity 1
		Assert.assertEquals(2, componentMap.getByEntityId(1).size());
		
		// Checks that it has 1 components for the entity 2		
		Assert.assertEquals(1, componentMap.getByEntityId(2).size());
		
		// Removes the components for the entity 1
		componentMap.removeByEntityId(1);
		
		// Checks that it has no components for the entity 1
		Assert.assertEquals(null, componentMap.getByEntityId(1));
		
		// Checks that it has 1 components for the entity 2		
		Assert.assertEquals(1, componentMap.getByEntityId(2).size());
		
		// Clears the ComponentMap
		componentMap.clear();
		
		// Checks that its empty
		Assert.assertEquals(0, componentMap.size());
	}
	
	@Test
	public void testGetByClass() throws Throwable
	{		
		// Creates the ComponentMap
		ComponentMap componentMap = new ComponentMap();
		
		// Creates an initializes some components
		TestComponent componentA = new TestComponent(1, 5);		
		TestComponent componentB = new TestComponent(3, 4);
		OtherTestComponent componentC = new OtherTestComponent(5);
		TestComponent componentD = new TestComponent(2, 3);
		ChildTestComponent componentE = new ChildTestComponent(1, 2, 5);
		componentA.create(1, 1, null);
		componentB.create(2, 1, null);		
		componentC.create(3, 1, null);
		componentD.create(4, 2, null);
		componentE.create(5, 2, null);
				
		// Adds the components
		componentMap.put(componentA);
		componentMap.put(componentB);
		componentMap.put(componentC);
		componentMap.put(componentD);
		componentMap.put(componentE);
				
		// Checks that it has componentB as TestComponent for the entity 1
		Assert.assertEquals(componentB, componentMap.getByClass(1, TestComponent.class));
		
		// Checks that the entity 1 has 1 component of class TestComponent
		Assert.assertEquals(true, componentMap.hasComponent(1, TestComponent.class));
		
		// Removes TestComponent for the entity 1
		componentMap.removeByClass(1, TestComponent.class);
		
		// Checks that the entity 1 has no component of class TestComponent
		Assert.assertEquals(false, componentMap.hasComponent(1, TestComponent.class));
		
		// Checks that the entity 1 has a component of class OtherTestComponent
		Assert.assertEquals(true, componentMap.hasComponent(1, OtherTestComponent.class));
		
		// Checks that the entity 2 has 2 components
		Assert.assertEquals(2, componentMap.getByEntityId(2).size());
		
		// Checks that the entity 2 has 1 component of class TestComponent
		Assert.assertEquals(true, componentMap.hasComponent(2, TestComponent.class));
		
		// Checks that the entity 2 has 1 component of class ChildTestComponent 
		Assert.assertEquals(true, componentMap.hasComponent(2, ChildTestComponent.class));
		
		// Checks that the components TestComponent and ChildTestComponent are not equal
		Assert.assertEquals(true, componentMap.getByClass(2, TestComponent.class) != componentMap.getByClass(2, ChildTestComponent.class));
		
		// Clears the ComponentMap
		componentMap.clear();
		
		// Checks that the entity 1 has no component of class TestComponent
		Assert.assertEquals(false, componentMap.hasComponent(1, TestComponent.class));
		
		// Checks that the entity 1 has no component of class OtherTestComponent
		Assert.assertEquals(false, componentMap.hasComponent(1, OtherTestComponent.class));
		
		// Checks that the entity 2 has no component of class TestComponent
		Assert.assertEquals(false, componentMap.hasComponent(2, TestComponent.class));
		
		// Checks that its empty
		Assert.assertEquals(0, componentMap.size());
	}
	
	@Test
	public void testGetCollectionByClass() throws Throwable
	{
		// Creates the ComponentMap
		ComponentMap componentMap = new ComponentMap();
		
		// Creates an initializes some components
		TestComponent componentA = new TestComponent(1, 5);		
		TestComponent componentB = new TestComponent(3, 4);
		TestComponent componentC = new TestComponent(2, 3);
		OtherTestComponent componentD = new OtherTestComponent(5);
		componentA.create(1, 1, null);
		componentB.create(2, 2, null);		
		componentC.create(3, 3, null);
		componentD.create(4, 4, null);
				
		// Adds the components
		componentMap.put(componentA);
		componentMap.put(componentB);
		componentMap.put(componentC);
		componentMap.put(componentD);
		
		// Checks that returns the components of class TestComponent
		Collection<TestComponent> testComponentCollection = componentMap.getCollectionByClass(TestComponent.class);
		Assert.assertEquals(3, testComponentCollection.size());
		Assert.assertEquals(true, testComponentCollection.contains(componentA));
		Assert.assertEquals(true, testComponentCollection.contains(componentB));
		Assert.assertEquals(true, testComponentCollection.contains(componentC));
		
		// Checks that returns the components of class OtherTestComponent
		Collection<OtherTestComponent> otherTestComponentCollection = componentMap.getCollectionByClass(OtherTestComponent.class);
		Assert.assertEquals(1, otherTestComponentCollection.size());
		Assert.assertEquals(true, otherTestComponentCollection.contains(componentD));
		
		// Clears the ComponentMap
		componentMap.clear();
		
		// Checks that its empty
		Assert.assertEquals(0, componentMap.size());
	}
	
	@Test
	public void testNull() throws Throwable
	{
		// Creates the ComponentMap
		ComponentMap componentMap = new ComponentMap();
		
		// Gets by an entity that does not exists
		Assert.assertEquals(null, componentMap.getByEntityId(1));		
		
		// Clears the ComponentMap
		componentMap.clear();
	}
}
