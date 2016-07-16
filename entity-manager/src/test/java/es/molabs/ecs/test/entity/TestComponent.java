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

import es.molabs.ecs.base.AbstractComponent;

public class TestComponent extends AbstractComponent
{
	private int currentValue;
	private int maxValue;
	
	public TestComponent(int currentValue, int maxValue)
	{
		this.currentValue = currentValue;
		this.maxValue = maxValue;
	}

	public int getCurrentValue() 
	{
		return currentValue;
	}

	public void setCurrentValue(int currentValue) 
	{
		this.currentValue = currentValue;
	}

	public int getMaxValue() 
	{
		return maxValue;
	}

	public void setMaxValue(int maxValue) 
	{
		this.maxValue = maxValue;
	}
}
