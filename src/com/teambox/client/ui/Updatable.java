/*
 * Copyright 2013 Joan Fuentes
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

package com.teambox.client.ui;

/**
 * This interface should be implemented in classes with sensitive displayed data
 * of being updated with a network upgrade.
 * 
 * @author Joan Fuentes
 * 
 */
public interface Updatable {

	/**
	 * Method used to refresh sensitive displayed data of being updated with a
	 * network upgrade
	 * 
	 */
	public void refreshDataInViews();
}
