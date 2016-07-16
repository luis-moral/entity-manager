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
package es.molabs.ecs.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import es.molabs.ecs.Component;

public class ComponentMap 
{
	private Map<Integer, Component> mapById = null;
	private Map<Integer, Map<Class<?>, Component>> mapByEntityId = null;
	
	public ComponentMap()
	{
		mapById = new HashMap<Integer, Component>();
		mapByEntityId = new HashMap<Integer, Map<Class<?>, Component>>();
	}
	
	public Component getById(Integer id)
	{
		return mapById.get(id);
	}
	
	public Collection<Component> getByEntityId(Integer entityId)
	{		
		Map<Class<?>, Component> map = mapByEntityId.get(entityId);
		
		return (map != null ? map.values() : null);
	}
	
	@SuppressWarnings("unchecked")
	public<C extends Component> C getByClass(Integer entityId, Class<C> clazz)
	{
		C component = null;
		
		// Gets the map for this entity
		Map<Class<?>, Component> map = mapByEntityId.get(entityId);
		
		if (map != null)
		{
			component = (C) map.get(clazz);
		}
		
		return component;
	}
	
	@SuppressWarnings("unchecked")
	public<C extends Component> Collection<C> getCollectionByClass(Class<C> clazz)
	{		
		Collection<C> collection = new LinkedList<C>();
		Collection<Component> allComponents = mapById.values();
		
		Iterator<Component> iterator = allComponents.iterator();
		while (iterator.hasNext())
		{
			Component component = iterator.next();
			
			if (component.getClass().equals(clazz))
			{
				collection.add((C) component);
			}
		}
		
		return collection;
	}
	
	public<C extends Component> boolean hasComponent(Integer entityId, Class<C> clazz)
	{
		Component component = null;
		
		// Gets the map for this entity
		Map<Class<?>, Component> map = mapByEntityId.get(entityId);
		
		if (map != null)
		{
			component = map.get(clazz);
		}
		
		return component != null;
	}
	
	/**
	 * Adds a component to the map.
	 * 
	 * @param component to be added to the map.
	 * 
	 * @return the previous component of the same class for the same entityId if already exists.
	 */
	public Component put(Component component)
	{
		// Removes any component that could exists with the same class
		Component removedComponent = removeByClass(component.getEntityId(), component.getClass());
		
		// If there was another component with the same class
		if (removedComponent != null)
		{
			// Deletes it from the map
			mapById.remove(removedComponent.getId());
		}
		
		mapById.put(component.getId(), component);		
		
		// Gets the map for the entity of the component
		Map<Class<?>, Component> map = mapByEntityId.get(component.getEntityId());		
		// If it does not exist
		if (map == null)
		{
			map = new HashMap<Class<?>, Component>();
			
			mapByEntityId.put(component.getEntityId(), map);
		}
		
		// Adds the new component to the map
		map.put(component.getClass(), component);
		
		return removedComponent;
	}
	
	public Component removeById(Integer id)
	{
		// Remove the component from the map
		Component component = mapById.remove(id);
		
		// If exists
		if (component != null)
		{
			// Removes from the map of components by entity
			mapByEntityId.get(component.getEntityId()).remove(component.getClass());
		}
		
		return component;
	}
	
	public Collection<Component> removeByEntityId(Integer entityId)
	{
		Collection<Component> removedComponents = new HashSet<Component>();
		
		// Removes the map for this entity id
		Map<Class<?>, Component> map = mapByEntityId.remove(entityId);
		
		// If exists
		if (map != null)
		{
			// For each component of this entity
			Iterator<Component> iterator = map.values().iterator();
			while (iterator.hasNext())
			{
				removedComponents.add(mapById.remove(iterator.next().getId()));
			}
		}
		
		return removedComponents;
	}
	
	@SuppressWarnings("unchecked")
	public<C extends Component> C removeByClass(Integer entityId, Class<C> clazz)
	{
		C component = null;
		
		// Gets the map for this entity
		Map<Class<?>, Component> map = mapByEntityId.get(entityId);
		
		if (map != null)
		{
			component = (C) map.remove(clazz);
		}
		
		return component;
	}
	
	public int size()
	{
		return mapById.keySet().size();
	}
	
	public Collection<Component> values()
	{
		return mapById.values();
	}
	
	public void clear()
	{
		mapById.clear();
		mapByEntityId.clear();
	}
}
