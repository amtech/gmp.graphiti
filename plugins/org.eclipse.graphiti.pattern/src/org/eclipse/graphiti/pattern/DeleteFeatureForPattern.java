/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2011 Volker Wegert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Volker Wegert - initial API, implementation and documentation:
 *                    Bug 336828: patterns should support delete,
 *                    remove, direct editing and conditional palette
 *                    creation entry
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.pattern;

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

/**
 * This class is used by the {@link DefaultFeatureProviderWithPatterns} to wrap
 * the deletion behavior provided by an {@link IPattern} into an
 * {@link IDeleteFeature}.
 * 
 * @since 0.8.0
 */
public class DeleteFeatureForPattern extends DefaultDeleteFeature {

	private IFeatureForPattern delegate;

	/**
	 * Creates a new {@link DeleteFeatureForPattern}.
	 * 
	 * @param featureProvider
	 *            the feature provider
	 * @param pattern
	 *            the pattern
	 */
	public DeleteFeatureForPattern(IFeatureProvider featureProvider, IPattern pattern) {
		super(featureProvider);
		delegate = new FeatureForPatternDelegate(pattern);
	}

	@Override
	public boolean canDelete(IDeleteContext context) {
		return delegate.getPattern().canDelete(context);
	}

	@Override
	public void preDelete(IDeleteContext context) {
		delegate.getPattern().preDelete(context);
	}

	@Override
	public void delete(IDeleteContext context) {
		delegate.getPattern().delete(context);
	}

	@Override
	public void postDelete(IDeleteContext context) {
		delegate.getPattern().postDelete(context);
	}

}
