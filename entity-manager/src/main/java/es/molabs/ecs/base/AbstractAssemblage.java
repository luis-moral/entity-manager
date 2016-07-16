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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import es.molabs.ecs.Assemblage;
import es.molabs.ecs.Component;
import es.molabs.ecs.Entity;
import es.molabs.ecs.EntityManager;

public class AbstractAssemblage implements Assemblage 
{
	private Set<Component> componentSet = null;
	
	protected AbstractAssemblage()
	{
		componentSet = new LinkedHashSet<Component>();
	}
	
	public void addComponent(Component component)
	{
		componentSet.add(component);
	}
	
	public void removeComponent(Component component)
	{
		componentSet.remove(component);
	}
	
	@SuppressWarnings("unchecked")
	public<C extends Component> C getComponent(Class<C> clazz)
	{
		C component = null;
		
		// For each component
		Iterator<Component> iterator = componentSet.iterator();
		while (iterator.hasNext())
		{
			Component item = iterator.next();
			
			if (item.getClass() == clazz)
			{
				component = (C) item;
			}
		}
		
		return component;
	}
	
	public Entity createEntity(EntityManager entityManager) 
	{
		// Creates the entity
		Entity entity = new BaseEntity();		
		entityManager.registerEntity(entity);
		
		// Register its components
		Iterator<Component> iterator = componentSet.iterator();
		while (iterator.hasNext())
		{
			entityManager.registerComponent(entity.getId(), iterator.next());
		}
		
		return entity;
	}
	
	private class BaseEntity extends AbstractEntity
	{
		private BaseEntity()
		{			
		}
	}
}
