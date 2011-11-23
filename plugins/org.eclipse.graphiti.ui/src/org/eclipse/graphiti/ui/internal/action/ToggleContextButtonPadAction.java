/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2011, 2011 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Felix Velasco - Bug 323351 Action creation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.ui.internal.action;

import org.eclipse.graphiti.ui.internal.Messages;
import org.eclipse.graphiti.ui.internal.editor.DiagramEditorInternal;
import org.eclipse.jface.action.Action;

/**
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToggleContextButtonPadAction extends Action {

	private DiagramEditorInternal graphicsEditor;
	
	public static final String TOOL_TIP = Messages.ToggleContextButtonPadAction_0_xmsg;
	
	public static final String TEXT = Messages.ToggleContextButtonPadAction_1_xfld;
	
	public static final String ACTION_ID = "toggle_context_button_pad"; //$NON-NLS-1$
	
	public ToggleContextButtonPadAction(DiagramEditorInternal graphicsEditor) {
		super();
		this.graphicsEditor = graphicsEditor;
		setText(TEXT);
		setToolTipText(TOOL_TIP);
		setId(ACTION_ID);
	}

	@Override
	public void run() {
		graphicsEditor.getContextButtonManager().setContextButtonShowing(!isChecked());
	}
}