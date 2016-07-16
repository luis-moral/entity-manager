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
package es.molabs.ecs.test.entity;

import es.molabs.ecs.Component;
import es.molabs.ecs.base.AbstractTaskSystem;
import es.molabs.task.Task;
import es.molabs.task.time.simple.SimpleRepeatTimeTask;

public class TestTaskSystem extends AbstractTaskSystem 
{	
	public TestTaskSystem(boolean concurrent)
	{
		super(concurrent);
	}
	
	protected Task onComponentAdded(Component component) 
	{
		Task task = null;
		
		if (component instanceof TestComponent)
		{
			task = new UpdateValueTask((TestComponent) component);
		}
		
		return task;
	}
	
	private class UpdateValueTask extends SimpleRepeatTimeTask
	{
		private TestComponent component = null;
		
		protected UpdateValueTask(TestComponent component) 
		{
			super(0.1f);
			
			this.component = component;
		}

		protected void doProcess(float delta) 
		{
			// If the current value is lesser that the maximum
			if (component.getCurrentValue() < component.getMaxValue())
			{
				// Adds one to the current value
				component.setCurrentValue(component.getCurrentValue() + 1);
			}
		}		
	}	
}
