/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
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
package org.eclipse.graphiti.features;

import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * The Interface IMappingProvider.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IMappingProvider {

	/**
	 * Returns all business objects which are linked to the given pictogram
	 * element.
	 * 
	 * @param pictogramElement
	 *            The pictogram element for which to return the business
	 *            objects.
	 * @return The business objects which are linked to the given pictogram
	 *         element. Can be empty but not null.
	 * @see #link(PictogramElement, Object[])
	 */
	Object[] getAllBusinessObjectsForPictogramElement(PictogramElement pictogramElement);

	/**
	 * Returns the first of possibly several business objects which are linked
	 * to the given pictogram element. This is a convenience method for
	 * {@link #getAllBusinessObjectsForPictogramElement(PictogramElement)},
	 * because in many usecases only a single business object is linked.
	 * 
	 * @param pictogramElement
	 *            The pictogram element for which to return the business object.
	 * @return The first of possibly several business objects which are linked
	 *         to the given pictogram element. Can be null.
	 * @see #link(PictogramElement, Object)
	 */
	Object getBusinessObjectForPictogramElement(PictogramElement pictogramElement);

	/**
	 * Provides the pictogram elements which represents the given business
	 * object.
	 * 
	 * @param businessObject
	 *            the given business object
	 * @return the pictogram elements
	 */
	PictogramElement[] getAllPictogramElementsForBusinessObject(Object businessObject);

	/**
	 * Links the pictogram element with a business object. This deletes all
	 * previous links of this pictogram element.
	 * 
	 * @param pictogramElement
	 *            The pictogram element which to link to a new business object.
	 * @param businessObject
	 *            The business object to link. Can be null to just delete
	 *            previous links.
	 */
	void link(PictogramElement pictogramElement, Object businessObject);

	/**
	 * Links the pictogram element with several business objects. This deletes
	 * all previous links of this pictogram element.
	 * 
	 * @param pictogramElement
	 *            The pictogram element which to link to new business objects.
	 * @param businessObjects
	 *            The business objects to link. Can be null or empty to just
	 *            delete previous links.
	 */
	void link(PictogramElement pictogramElement, Object[] businessObjects);

	/**
	 * Check does there have pictogram element linked to this business object.
	 * 
	 * @param businessObject
	 *            the business object
	 * @return true when at least one pictogram element is linked, otherwise
	 *         return false.
	 */
	boolean hasPictogramElementForBusinessObject(Object businessObject);

	/**
	 * This method is similar to the method
	 * getAllPictogramElementsForBusinessObject, but only return the first
	 * PictogramElement.
	 * 
	 * @param businessObject
	 *            the business object
	 * @return linked pictogram element
	 * @see #getAllPictogramElementsForBusinessObject(Object)
	 */
	PictogramElement getPictogramElementForBusinessObject(Object businessObject);

}