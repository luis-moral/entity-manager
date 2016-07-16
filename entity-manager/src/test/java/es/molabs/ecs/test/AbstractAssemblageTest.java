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

import es.molabs.ecs.Entity;
import es.molabs.ecs.EntityManager;
import es.molabs.ecs.test.entity.OtherTestComponent;
import es.molabs.ecs.test.entity.TestAssemblage;
import es.molabs.ecs.test.entity.TestComponent;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAssemblageTest 
{	
	@Test
	public void testAbstractAssemblage() throws Throwable
	{
		// Creates the manager
		EntityManager entityManager = new EntityManager();
		entityManager.init();
		
		// Creates an assemblage
		TestAssemblage assemblage = Mockito.spy(new TestAssemblage(1, 5));
		
		// Checks that the assemblage has a TestComponent component
		Assert.assertEquals(true, assemblage.getComponent(TestComponent.class) != null);
		
		// Checks that the assemblage does not have an OtherTestComponent component
		Assert.assertEquals(true, assemblage.getComponent(OtherTestComponent.class) == null);
		
		// Register the assemblage as entity
		Entity entity = entityManager.registerEntity(assemblage);
		
		// Checks that the entity has been registered
		Assert.assertEquals(entity, entityManager.getEntity(entity.getId()));
		
		// Checks that the entity has a TestComponent component
		Assert.assertEquals(true, entityManager.hasComponent(entity.getId(), TestComponent.class));
		
		// Unregisters the entity
		entityManager.unregisterEntity(entity.getId());
		
		// Destroys the manager
		entityManager.destroy();
	}
}
