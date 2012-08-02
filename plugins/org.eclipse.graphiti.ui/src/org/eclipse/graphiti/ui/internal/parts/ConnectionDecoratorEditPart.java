/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2012 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.ui.internal.parts;

import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.internal.config.IConfigurationProviderInternal;

/**
 * The Class ConnectionDecoratorEditPart.
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ConnectionDecoratorEditPart extends ShapeEditPart implements IConnectionDecoratorEditPart {

	/**
	 * The Constructor.
	 * 
	 * @param configurationProvider
	 *            the configuration provider
	 * @param shape
	 *            the shape
	 */
	public ConnectionDecoratorEditPart(IConfigurationProviderInternal configurationProvider, Shape shape) {
		super(configurationProvider, shape);
	}

}