package org.daro.common.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class UIError {
	
	private static MultiStatus toStatus(Throwable e)
	{
		MultiStatus status = new MultiStatus("ID",IStatus.ERROR,e.getLocalizedMessage(), null);
		status.add(new Status(IStatus.INFO,"ID",1,"Exception "+e.getClass().toString(), null));
		StringBuffer buffer = new StringBuffer(128);
		StackTraceElement trace[] = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			buffer.delete(0, buffer.length());
			buffer.append("          ").append(trace[i].getClassName()).append('.').append(trace[i].getMethodName()).append('(').append(trace[i].getFileName()).append(':').append(trace[i].getLineNumber()).append(')');
			status.add(new Status(IStatus.ERROR,"ID",1,buffer.toString(), null));
		}
		return status;
	}
	
	public static void  showError(Shell parent, String title, String message, Throwable e) {
		ErrorDialog.openError(parent, title, message, toStatus(e));
	}
	
	public static void  showError(String title, Throwable e) {
		ErrorDialog.openError(Display.getCurrent().getActiveShell(), title, e.getMessage(), toStatus(e));
	}

	public static void  showError(String title, String message) {
		MessageDialog.openError(Display.getCurrent().getActiveShell(), title, message);
	}

	public static void  showInfo(Shell parent, String title, Exception e) {
		MessageDialog.openInformation(parent, title, e.getMessage());
	}
	
	public static void  showInfo(String title, Exception e) {
		MessageDialog.openInformation(new Shell(Display.getCurrent()), title, e.getMessage());
	}

	public static void  showInfo(String title, String message) {
		MessageDialog.openInformation(new Shell(Display.getCurrent()), title, message);
	}

	
	public static void  showWarning(Shell parent, String title, Exception e) {
		MessageDialog.openWarning(parent, title, e.getMessage());
	}
	
	public static void  showWarning(String title, Throwable e) {
		MessageDialog.openWarning(new Shell(Display.getCurrent()), title, e.getMessage());
	}

	public static void  showWarning(String title, String message) {
		MessageDialog.openWarning(new Shell(Display.getCurrent()), title, message);
	}	
}
