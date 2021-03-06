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
package org.eclipse.graphiti.util;

/**
 * The Class ColorConstant.
 */
public class ColorConstant implements IColorConstant {

	private int red;

	private int green;

	private int blue;

	/**
	 * Creates a new {@link ColorConstant} given the desired red, green and blue
	 * values expressed as ints in the range 0 to 255 (where 0 is black and 255
	 * is full brightness).
	 * 
	 * @param red
	 *            the amount of red in the color
	 * @param green
	 *            the amount of green in the color
	 * @param blue
	 *            the amount of blue in the color
	 */
	public ColorConstant(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * Creates a new {@link ColorConstant} for for a given String, which defines
	 * the RGB values in hexadecimal format. This means, that the String must
	 * have a length of 6 characters. Example: <code>getColor("FF0000")</code>
	 * returns a red color.
	 * 
	 * @param hexRGBString
	 *            The RGB values in hexadecimal format.
	 * @since 0.8
	 */
	public ColorConstant(String hexRGBString) {
		this(ColorUtil.getRedFromHex(hexRGBString), ColorUtil.getGreenFromHex(hexRGBString), ColorUtil.getBlueFromHex(hexRGBString));
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

}
