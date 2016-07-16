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
package es.molabs.ecs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.molabs.ecs.util.ComponentMap;
import es.molabs.eventbus.EventBus;

public class EntityManager 
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private AtomicInteger ENTITY_ID_GENERATOR = null;
	private AtomicInteger COMPONENT_ID_GENERATOR = null;
	private AtomicInteger SYSTEM_ID_GENERATOR = null;
	
	private Map<Integer, Entity> entityMap = null;
	private ComponentMap componentMap = null;
	private Map<Integer, System> systemMap = null;
	
	private EventBus eventBus = null;
	
	private boolean initialized;
	
	public EntityManager()
	{
	}
	
	public void init()
	{
		if (!initialized)
		{
			ENTITY_ID_GENERATOR = new AtomicInteger(1);
			COMPONENT_ID_GENERATOR = new AtomicInteger(1);
			SYSTEM_ID_GENERATOR = new AtomicInteger(1);
			
			entityMap = new HashMap<Integer, Entity>();
			componentMap = new ComponentMap();
			systemMap = new HashMap<Integer, System>();
			
			eventBus = new EventBus();
			
			// Sets the manager as initialized
			initialized = true;
			
			logger.info("Initialized.");
		}
		else
		{
			logger.warn("Already initialized.");
		}
	}
	
	public void destroy()
	{
		if (initialized)
		{
			// Sets the manager as not initialized
			initialized = false;
			
			destroyEntityMap();
			destroyComponentMap();
			destroySystemMap();			
			
			eventBus.clear();
			eventBus = null;
			
			ENTITY_ID_GENERATOR = null;
			COMPONENT_ID_GENERATOR = null;
			SYSTEM_ID_GENERATOR = null;
			
			logger.info("Destroyed.");
		}
		else
		{
			logger.warn("Already destroyed.");
		}
	}
	
	public EventBus getEventBus()
	{
		return eventBus;
	}
	
	public boolean isInitialized()
	{
		return initialized;
	}	
	
	public Entity getEntity(int id)
	{
		return entityMap.get(id);
	}
	
	public int getEntityCount()
	{
		return entityMap.keySet().size();
	}
	
	public Component getComponent(int id)
	{
		return componentMap.getById(id);
	}
	
	public Collection<Component> getComponentCollection(int entityId)
	{
		return componentMap.getByEntityId(entityId);
	}
	
	public<C extends Component> Collection<C> getComponentCollectionByClass(Class<C> clazz)
	{
		return componentMap.getCollectionByClass(clazz);
	}
	
	public<C extends Component> C getComponent(Integer entityId, Class<C> clazz)
	{
		return componentMap.getByClass(entityId, clazz);
	}
	
	public<C extends Component> boolean hasComponent(Integer entityId, Class<C> clazz)
	{
		return componentMap.hasComponent(entityId, clazz);
	}
	
	public int getComponentCount()
	{
		return componentMap.size();
	}
	
	public System getSystem(int id)
	{
		return systemMap.get(id);
	}
	
	public int getSystemCount()
	{
		return systemMap.keySet().size();
	}
	
	@SuppressWarnings({"unchecked"})
	public<S extends System> S getSystemByClass(Class<S> clazz)
	{
		S system = null;
		
		Iterator<System> iterator = systemMap.values().iterator();
		while (iterator.hasNext())
		{
			System item = iterator.next();
			
			if (item.getClass() == clazz)
			{
				system = (S) item;
				
				break;
			}
		}
		
		return system;
	}
	
	public Entity registerEntity(Assemblage assemblage)
	{
		return assemblage.createEntity(this);
	}
	
	public Entity registerEntity(Entity entity)
	{
		checkInitialized();
		
		int entityId = ENTITY_ID_GENERATOR.getAndIncrement();
		
		entityMap.put(entityId, entity);
		
		entity.create(entityId, this);
		
		return entity;
	}
	
	public void unregisterEntity(int entityId)
	{
		checkInitialized();
				
		// Removes the entity from the map
		Entity entity = entityMap.remove(entityId);
		
		// If exists
		if (entity != null)
		{
			unregisterEntity(entity);
		}
		else
		{
			logger.warn("Entity not found [id={}].", entityId);
		}
	}
	
	private void unregisterEntity(Entity entity)
	{
		// Removes the components associated to this entity
		Collection<Component> componentCollection = componentMap.removeByEntityId(entity.getId());
		
		// If there was any
		if (componentCollection != null && !componentCollection.isEmpty())
		{
			Iterator<Component> iterator = componentCollection.iterator();
			while (iterator.hasNext())
			{
				Component component = iterator.next();
				
				unregisterComponent(component);
			}
		}
		
		// Disposes the entity
		entity.dispose();		
	}

	public void registerComponent(int entityId, Component component)
	{
		checkInitialized();		
		
		int componentId = COMPONENT_ID_GENERATOR.getAndIncrement();
		
		component.create(componentId, entityId, this);
		
		componentMap.put(component);		
		
		// Inform the systems that a new component has been added
		Iterator<System> iterator = systemMap.values().iterator();
		while (iterator.hasNext())
		{
			iterator.next().componentAdded(component);
		}						
	}
	
	public void unregisterComponent(int componentId)
	{
		checkInitialized();		
		
		// Removes the component from the map
		Component component = componentMap.removeById(componentId);

		// If exists
		if (component != null)
		{
			unregisterComponent(component);
		}
		else
		{
			logger.warn("Component not found [id={}].", componentId);
		}	
	}
	
	private void unregisterComponent(Component component)
	{
		if (component != null)
		{
			// Inform the systems that a new component has been removed
			Iterator<System> iterator = systemMap.values().iterator();
			while (iterator.hasNext())
			{
				iterator.next().componentRemoved(component);
			}
			
			component.dispose();
		}
	}
	
	public void registerSystem(System system)
	{
		checkInitialized();
		
		system.create(SYSTEM_ID_GENERATOR.getAndIncrement(), this);
		
		systemMap.put(system.getId(), system);
		
		// For each existing component
		Iterator<Component> iterator = componentMap.values().iterator();
		while (iterator.hasNext())
		{
			system.componentAdded(iterator.next());
		}
	}
	
	public void unregisterSystem(int systemId)
	{
		checkInitialized();
		
		// Removes the system from the map
		System system = systemMap.remove(systemId);
		
		// If exists
		if (system != null)
		{
			unregisterSystem(system);
		}
		else
		{
			logger.warn("System not found [id={}].", systemId);
		}
	}
	
	private void unregisterSystem(System system)
	{
		system.dispose();
	}
	
	public void update(float delta)
	{
		checkInitialized();
		
		Iterator<System> iterator = systemMap.values().iterator();
		while (iterator.hasNext())
		{
			// Checks that is still initialized since an update from a system could have destroyed the entity manager)
			if (initialized)
			{
				iterator.next().update(delta);
			}
			// If it is not initialized
			else
			{
				break;
			}
		}
	}
	
	private void checkInitialized()
	{
		if (!initialized) throw new IllegalStateException("Not initialized.");
	}
	
	private void destroyEntityMap()
	{
		Iterator<Entity> iterator = entityMap.values().iterator();
		while (iterator.hasNext())
		{
			unregisterEntity(iterator.next());
		}
		
		entityMap.clear();
		entityMap = null;
	}
	
	private void destroyComponentMap()
	{
		Iterator<Component> iterator = componentMap.values().iterator();
		while (iterator.hasNext())
		{					
			unregisterComponent(iterator.next());
		}
		
		componentMap.clear();
		componentMap = null;
	}
	
	private void destroySystemMap()
	{
		Iterator<System> iterator = systemMap.values().iterator();
		while (iterator.hasNext())
		{
			unregisterSystem(iterator.next());
		}
		
		systemMap.clear();
		systemMap = null;
	}
}
