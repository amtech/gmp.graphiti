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
 *    Benjamin Schmeling - mwenz - Bug 367483 - Support composite connections
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.testtool.sketch.features.create;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.ICreateService;
import org.eclipse.graphiti.testtool.sketch.SketchUtil;
import org.eclipse.graphiti.util.IColorConstant;

/**
 * The Class AbstractSketchCreateSimpleConnectionFeature.
 */
abstract class AbstractSketchCreateSimpleConnectionFeature extends AbstractSketchCreateConnectionFeature {

	/**
	 * Instantiates a new abstract sketch create simple connection feature.
	 * 
	 * @param fp
	 *            the fp
	 * @param name
	 *            the name
	 * @param description
	 *            the description
	 */
	public AbstractSketchCreateSimpleConnectionFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@SuppressWarnings("unused")
	public Connection create(ICreateConnectionContext context) {

		ICreateService cs = Graphiti.getCreateService();

		Anchor startAnchor = context.getSourceAnchor();
		Anchor endAnchor = context.getTargetAnchor();

		if (startAnchor == null && (context.getSourcePictogramElement() instanceof Connection)) {

			Shape s = SketchUtil.createConnectionPoint(context.getSourceLocation(), getDiagram());
			startAnchor = cs.createChopboxAnchor(s);

			Connection splitConnection = (Connection) context.getSourcePictogramElement();
			createConnection(splitConnection.getStart(), startAnchor);
			createConnection(startAnchor, splitConnection.getEnd());

			EcoreUtil.delete(splitConnection);
		}

		if (endAnchor == null && (context.getTargetPictogramElement() instanceof Connection)) {
			Shape s = SketchUtil.createConnectionPoint(context.getTargetLocation(), getDiagram());
			endAnchor = cs.createChopboxAnchor(s);

			Connection splitConnection = (Connection) context.getTargetPictogramElement();
			createConnection(splitConnection.getStart(), endAnchor);
			createConnection(endAnchor, splitConnection.getEnd());

			EcoreUtil.delete(splitConnection);
		}

		if (startAnchor == null || endAnchor == null) {
			return null;
		}

		Connection connection = createConnection(startAnchor, endAnchor);

		/* add bend point midway */
		if (false && connection instanceof FreeFormConnection) {
			GraphicsAlgorithm startGa = startAnchor.getParent().getGraphicsAlgorithm();
			GraphicsAlgorithm endGa = endAnchor.getParent().getGraphicsAlgorithm();
			int startAnchorX = startGa.getX() + startGa.getWidth() / 2;
			int startAnchorY = startGa.getY() + startGa.getHeight() / 2;
			int endAnchorX = endGa.getX() + endGa.getWidth() / 2;
			int endAnchorY = endGa.getY() + endGa.getHeight() / 2;
			int bendpointX = (startAnchorX + endAnchorX) / 2;
			int bendpointY = (startAnchorY + endAnchorY) / 2;
			Point bendpoint = Graphiti.getGaCreateService().createPoint(bendpointX, bendpointY);
			((FreeFormConnection) connection).getBendpoints().add(bendpoint);
		}
		return connection;
	}

	/**
	 * Creates the connection.
	 * 
	 * @return the connection
	 */
	protected abstract Connection createConnection();

	protected Connection createConnection(Anchor startAnchor, Anchor endAnchor) {
		Connection connection = createConnection();

		createVisualization(connection);
		setAnchors(startAnchor, endAnchor, connection);

		return connection;
	}

	protected void setAnchors(Anchor startAnchor, Anchor endAnchor, Connection connection) {
		connection.setStart(startAnchor);
		connection.setEnd(endAnchor);
	}

	protected void createVisualization(Connection connection) {
		Polyline p = Graphiti.getCreateService().createPolyline(connection);
		p.setLineWidth(3);
		p.setForeground(manageColor(IColorConstant.LIGHT_BLUE));
		p.setLineStyle(LineStyle.DASHDOT);
	}
}
