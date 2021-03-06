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
 *    mwenz - Bug 340443 - Fixed AbstractTracer must not implement ILog warning
 *    mwenz - Bug 340627 - Features should be able to indicate cancellation
 *    mwenz - Bug 376008 - Iterating through navigation history causes exceptions
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.internal.util;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.graphiti.internal.pref.GFPreferences;
import org.osgi.framework.Bundle;

/**
 * Adds <code>entering</code> to allow trace entries with any kind of parameters
 * when a specific method is entered and <code>exiting</code> to allow trace
 * entries with any kind of parameters when a method is exited. To create an
 * instance, use the static method <code>T.racer()</code> from the concrete sub
 * classes <code>org.eclipse.graphiti.util.T</code> or
 * <code>org.eclipse.graphiti.ui.T</code> depending on the package you want to
 * trace.
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class AbstractTracer {

	private static boolean sIsInfoLogging = false;
	private static boolean sIsDebugLogging = false;

	static {
		if (Platform.getBundle("org.eclipse.graphiti.examples.common") != null) { //$NON-NLS-1$
			// Graphiti preference page (from examples plug-in) is usable by user and preferred
			GFPreferences prefs = GFPreferences.getInstance();
			sIsInfoLogging = prefs.isInfoLevelTracingActive();
			sIsDebugLogging = prefs.isDebugLevelTracingActive();
		} else {
			String logInfoProperty = System.getProperty("org.eclipse.graphiti.logging.info", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			if ("true".equals(logInfoProperty)) { //$NON-NLS-1$
				sIsInfoLogging = true;
			}

			String logDebugProperty = System.getProperty("org.eclipse.graphiti.logging.debug", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			if ("true".equals(logDebugProperty)) { //$NON-NLS-1$
				sIsDebugLogging = true;
			}
		}
	}

	private static final String EOL = System.getProperty("line.separator"); //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String ENTERING_MSG = "Entering method "; //$NON-NLS-1$

	private static final String SIGNATURE_PATTERN = "$signature$"; //$NON-NLS-1$

	//	private static final String FULL_ENTERING_MSG = "#" + ENTERING_MSG + SIGNATURE_PATTERN + "#" + EOL; //$NON-NLS-1$ //$NON-NLS-2$

	private static final String EXITING_MSG = "Exiting method "; //$NON-NLS-1$

	private static final String FULL_EXITING_MSG = "#" + EXITING_MSG + SIGNATURE_PATTERN + "#" + EOL; //$NON-NLS-1$ //$NON-NLS-2$

	//	private static final String THROWING_MSG = "Throwing"; //$NON-NLS-1$

	//	private static final String CATCHING_MSG = "Caught"; //$NON-NLS-1$

	private ILog log = null;
	private boolean infoAlwaysTrue = false;

	public AbstractTracer(String location) {
		if (Platform.isRunning()) {
			// The Eclipse platform is running, this should be the standard case for Graphiti being executed inside Eclipse
			Bundle bundle = Platform.getBundle(location);
			log = Platform.getLog(bundle);
		} else {
			// Graphiti is not run within the Eclipse platform. This happens e.g. while running Unit tests

			// Warn user (in case this happens in other cases than Unit testing)
			System.err.println("================================================================================================"); //$NON-NLS-1$
			System.err.println("Logging is disabled because the platform is not running! (This message is OK for Unit test runs)"); //$NON-NLS-1$
			System.err.println("================================================================================================"); //$NON-NLS-1$

			log = null;
		}

	}

	//	/**
	//	 * Writes a trace entry that the specified method was entered.
	//	 * 
	//	 * @param clazz
	//	 *            Class of the traced method
	//	 * @param signature
	//	 *            signature of the traced method
	//	 */
	//	public void entering(Class clazz, String signature) {
	//		t.info(clazz, signature, FULL_ENTERING_MSG.replaceFirst(SIGNATURE_PATTERN, signature));
	//	}

	/**
	 * @param isInfoLogging the sIsInfoLogging to set
	 */
	public static void setInfoLogging(boolean isInfoLogging) {
		AbstractTracer.sIsInfoLogging = isInfoLogging;
	}

	/**
	 * @param isDebugLogging the sIsDebugLogging to set
	 */
	public static void setDebugLogging(boolean isDebugLogging) {
		AbstractTracer.sIsDebugLogging = isDebugLogging;
	}

	/**
	 * Writes a trace entry that the specified method was entered.
	 * 
	 * @param clazz
	 *            Class of the traced method
	 * @param signature
	 *            signature of the traced method
	 * @param args
	 *            Arguments as object references
	 */
	public void entering(Class<?> clazz, String signature, Object... args) {
		if (log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), "Class '" + clazz.getName() + "': " //$NON-NLS-1$ //$NON-NLS-2$
					+ createTraceMsg(ENTERING_MSG + signature, args)));
		}
	}

	/**
	 * Writes a trace entry that the specified method is about to be exited.
	 * 
	 * @param clazz
	 *            Class of the traced method
	 * @param signature
	 *            signature of the traced method
	 */
	public void exiting(Class<?> clazz, String signature) {
		if (log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), "Class '" + clazz.getName() + "':'" //$NON-NLS-1$ //$NON-NLS-2$
					+ FULL_EXITING_MSG.replaceFirst(SIGNATURE_PATTERN, signature)));
		}
	}

	/**
	 * Writes a trace entry that the specified method is about to be exited.
	 * 
	 * @param clazz
	 *            Class of the traced method
	 * @param signature
	 *            signature of the traced method
	 * @param res
	 *            Result as object reference
	 */
	public void exiting(Class<?> clazz, String signature, Object res) {
		Object result = res;
		if (result == null) {
			result = new String("<null>"); //$NON-NLS-1$
		}
		if (log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), "Class '" + clazz.getName() + "':'" //$NON-NLS-1$ //$NON-NLS-2$
					+ createTraceMsg(EXITING_MSG + signature, new Object[] { result })));
		}
	}

	//	/**
	//	 * Writes a trace entry that the specified throwable is about to be thrown.
	//	 * 
	//	 * @param clazz
	//	 *            Class of the traced method
	//	 * @param signature
	//	 *            signature of the traced method
	//	 * @param throwable
	//	 *            Throwable
	//	 */
	//	public void throwing(Class clazz, String signature, Throwable throwable) {
	//		t.warning(clazz, signature, THROWING_MSG, throwable);
	//	}
	//
	//	/**
	//	 * Writes a trace entry that the specified throwable was caught.
	//	 * 
	//	 * @param clazz
	//	 *            Class of the traced method
	//	 * @param signature
	//	 *            signature of the traced method
	//	 * @param throwable
	//	 *            Throwable
	//	 */
	//	public void catching(Class clazz, String signature, Throwable throwable) {
	//		t.warning(clazz, signature, CATCHING_MSG, throwable);
	//	}

	public boolean debug() {
		return sIsDebugLogging;
	}

	//	public void debug(Class clazz, String methodName, String msg) {
	//		t.debug(clazz, methodName, msg);
	//	}
	//
	//	public void debug(String className, String methodName, String msg) {
	//		t.debug(className, methodName, msg);
	//	}
	//
	//	public void debug(String methodName, String msg) {
	//		t.debug(methodName, msg);
	//	}

	public void debug(String msg) {
		if (debug() && log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), "DEBUG: " + msg)); //$NON-NLS-1$
		}
	}

	public void debug(String msg, Throwable throwable) {
		if (debug() && log != null) {
			log.log(new Status(IStatus.ERROR, log.getBundle().getSymbolicName(), msg, throwable));
		}
	}

	//	public boolean error() {
	//		return t.error();
	//	}
	//
	//	public void error(Class clazz, String methodName, String msg, Throwable throwable) {
	//		t.error(clazz, methodName, msg, throwable);
	//	}
	//
	//	public void error(Class clazz, String methodName, String msg) {
	//		t.error(clazz, methodName, msg);
	//	}
	//
	//	public void error(String className, String methodName, String msg, Throwable throwable) {
	//		t.error(className, methodName, msg, throwable);
	//	}
	//
	//	public void error(String className, String methodName, String msg) {
	//		t.error(className, methodName, msg);
	//	}
	//
	//	public void error(String methodName, String msg, Throwable throwable) {
	//		t.error(methodName, msg, throwable);
	//	}

	public void error(String methodName, String msg) {
		if (log != null) {
			log.log(new Status(IStatus.ERROR, log.getBundle().getSymbolicName(), "Method '" + methodName + "': " + msg)); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void error(String msg, Throwable throwable) {
		if (log != null) {
			log.log(new Status(IStatus.ERROR, log.getBundle().getSymbolicName(), msg, throwable));
		}
	}

	public void error(String msg) {
		if (log != null) {
			log.log(new Status(IStatus.ERROR, log.getBundle().getSymbolicName(), msg));
		}
	}

	//	public boolean fatal() {
	//		return t.fatal();
	//	}
	//
	//	public void fatal(Class clazz, String methodName, String msg, Throwable throwable) {
	//		t.fatal(clazz, methodName, msg, throwable);
	//	}
	//
	//	public void fatal(Class clazz, String methodName, String msg) {
	//		t.fatal(clazz, methodName, msg);
	//	}
	//
	//	public void fatal(String className, String methodName, String msg, Throwable throwable) {
	//		t.fatal(className, methodName, msg, throwable);
	//	}
	//
	//	public void fatal(String className, String methodName, String msg) {
	//		t.error(className, methodName, msg);
	//	}
	//
	//	public void fatal(String methodName, String msg, Throwable throwable) {
	//		t.fatal(methodName, msg, throwable);
	//	}
	//
	//	public void fatal(String methodName, String msg) {
	//		t.fatal(methodName, msg);
	//	}
	//
	//	public void fatal(String msg, Throwable throwable) {
	//		t.fatal(msg, throwable);
	//	}
	//
	//	public void fatal(String msg) {
	//		t.fatal(msg);
	//	}
	//
	//	public int getTraceLevel() {
	//		return t.getTraceLevel();
	//	}

	public boolean info() {
		// useful for testing / code-coverage
		if (getInfoAlwaysTrue()) {
			return true;
		}
		return sIsInfoLogging;
	}

	//	public void info(Class clazz, String methodName, String msg) {
	//		t.info(clazz, methodName, msg);
	//	}

	public void info(String className, String methodName, String msg) {
		if (info() && log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), "Class '" + className + "' method '" + methodName + "': " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ msg));
		}
	}

	//	public void info(String methodName, String msg) {
	//		t.info(methodName, msg);
	//	}

	public void info(String msg) {
		if (info() && log != null) {
			log.log(new Status(IStatus.INFO, log.getBundle().getSymbolicName(), msg));
		}
	}

	//	public void log(int level, Class clazz, String methodName, String msg, Throwable throwable) {
	//		t.log(level, clazz, methodName, msg, throwable);
	//	}
	//
	//	public void log(int level, Class clazz, String methodName, String msg) {
	//		t.log(level, clazz, methodName, msg);
	//	}
	//
	//	public void log(int level, String className, String methodName, String msg, Throwable throwable) {
	//		t.log(level, className, methodName, msg, throwable);
	//	}
	//
	//	public void log(int level, String className, String methodName, String msg) {
	//		t.log(level, className, methodName, msg);
	//	}
	//
	//	public void log(int level, String methodName, String msg) {
	//		t.log(level, methodName, msg);
	//	}

	public void log(int level, String msg, Throwable throwable) {
		log.log(new Status(level, log.getBundle().getSymbolicName(), msg, throwable));
	}

	public void log(int level, String msg) {
		if (((level == IStatus.INFO) && info()) || (level == IStatus.WARNING) || (level == IStatus.ERROR)) {
			if (log != null) {
				log.log(new Status(level, log.getBundle().getSymbolicName(), msg));
			}
		}
	}

	//	public boolean log(int level) {
	//		return t.log(level);
	//	}
	//
	//	public boolean path() {
	//		return t.path();
	//	}
	//
	//	public void path(Class clazz, String methodName, String msg) {
	//		t.path(clazz, methodName, msg);
	//	}
	//
	//	public void path(String className, String methodName, String msg) {
	//		t.path(className, methodName, msg);
	//	}
	//
	//	public void path(String methodName, String msg) {
	//		t.path(methodName, msg);
	//	}
	//
	//	public void path(String msg) {
	//		t.path(msg);
	//	}
	//
	//	public void userOut(int level, String msg) {
	//		t.userOut(level, msg);
	//	}
	//
	//	public void userOut(String subCategory, int level, String msg) {
	//		t.userOut(subCategory, level, msg);
	//	}
	//
	//	public void userOut(String subCategory, String msg) {
	//		t.userOut(subCategory, msg);
	//	}
	//
	//	public void userOut(String msg) {
	//		t.userOut(msg);
	//	}
	//
	//	public boolean warning() {
	//		return t.warning();
	//	}
	//
	//	public void warning(Class clazz, String methodName, String msg, Throwable throwable) {
	//		t.warning(clazz, methodName, msg, throwable);
	//	}
	//
	//	public void warning(Class clazz, String methodName, String msg) {
	//		t.debug(clazz, methodName, msg);
	//	}
	//
	//	public void warning(String className, String methodName, String msg, Throwable throwable) {
	//		t.warning(className, methodName, msg, throwable);
	//	}
	//
	//	public void warning(String className, String methodName, String msg) {
	//		t.warning(className, methodName, msg);
	//	}
	//
	//	public void warning(String methodName, String msg, Throwable throwable) {
	//		t.warning(methodName, msg, throwable);
	//	}

	public void warning(String methodName, String msg) {
		if (info() || debug()) {
			if (log != null) {
				log.log(new Status(IStatus.WARNING, log.getBundle().getSymbolicName(), "Method '" + methodName + "': " + msg)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	//	public void warning(String msg, Throwable throwable) {
	//		t.warning(msg, throwable);
	//	}

	public void warning(String msg) {
		if (info() || debug()) {
			if (log != null) {
				log.log(new Status(IStatus.WARNING, log.getBundle().getSymbolicName(), msg));
			}
		}
	}

	public void warning(String msg, Throwable throwable) {
		if (log != null) {
			log.log(new Status(IStatus.WARNING, log.getBundle().getSymbolicName(), msg, throwable));
		}
	}

	//	@SuppressWarnings("deprecation")
	//	public boolean fine() {
	//		return false;
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void fine(Class clazz, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void fine(String className, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void fine(String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void fine(String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public boolean finer() {
	//		return false;
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finer(Class clazz, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finer(String className, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finer(String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finer(String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public boolean finest() {
	//		return false;
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finest(Class clazz, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finest(String className, String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finest(String methodName, String msg) {
	//	}
	//
	//	@SuppressWarnings("deprecation")
	//	public void finest(String msg) {
	//	}

	private String createTraceMsg(String msg, Object... args) {
		StringBuffer sb = new StringBuffer(512);
		StringBuffer esc = new StringBuffer();

		sb.append('#');
		sb.append(msg);

		if (args != null) {
			sb.append('#');
			sb.append(args.length);
			for (int i = 0; i < args.length; i++) {
				sb.append('#');
				if (args[i] != null) {
					sb.append(escape(args[i].toString(), esc));
				} else {
					sb.append(EMPTY_STRING);
				}
			}
		}

		sb.append('#');
		sb.append(EOL);
		return sb.toString();
	}

	private StringBuffer escape(String s, StringBuffer buf) {
		int len = 0;

		if (s == null) {
			s = EMPTY_STRING;
		}

		len = s.length();

		buf.setLength(0);
		buf.append(s);
		for (int i = 0; i < len; ++i) {
			switch (buf.charAt(i)) {
			case '#': {
				buf.replace(i, i + 1, "\\#"); //$NON-NLS-1$
				++len;
				++i;
				break;
			}
			case '\\': {
				buf.replace(i, i + 1, "\\\\"); //$NON-NLS-1$
				++len;
				++i;
				break;
			}
			}
		}
		return buf;
	}

	public final boolean getInfoAlwaysTrue() {
		return infoAlwaysTrue;
	}

	public final void setInfoAlwaysTrue(boolean b) {
		infoAlwaysTrue = b;
	}
}