package org.daro.common.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.part.ViewPart;

import cc.hogo.hours.Activator;

public abstract class AbstractView extends ViewPart implements IRefreshableView {
	private   IStatusLineManager statusLine;
	protected IMenuManager 		menuManager;
	protected IToolBarManager 	toolBarManager;
	protected boolean 			isDirty;
	protected Object 			input;

	private KeyListener refreshOnKey = new KeyListener() {
		@Override
		public void keyReleased(KeyEvent e) {
			//
		}

		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.F5) {
				refresh();
			}
		}
	};

	public KeyListener getRefreshOnKeyAction() {
		return refreshOnKey;
	}

	protected Action refresh = new Action("", Activator.getImageDescriptor("icons/clcl16/refresh_nav.gif")) {
		@Override
		public void run() {
			refresh();
		}

		@Override
		public String getToolTipText() {
			return "Refresh View";
		}

		@Override
		public ImageDescriptor getDisabledImageDescriptor() {
			return Activator.getImageDescriptor("icons/dlcl16/refresh_nav.gif");
		}
	};

	public void addToolBarAction(IAction action) {
		if (toolBarManager == null)
			toolBarManager = getViewSite().getActionBars().getToolBarManager();

		toolBarManager.add(action);
	}

	public void addToolBarAction(IContributionItem item) {
		if (toolBarManager == null)
			toolBarManager = getViewSite().getActionBars().getToolBarManager();

		toolBarManager.add(item);
	}

	public void addMenuManagerAction(IAction action) {
		if (menuManager == null)
			menuManager = getViewSite().getActionBars().getMenuManager();

		menuManager.add(action);
	}

	public void addMenuManagerAction(IContributionItem action) {
		if (menuManager == null)
			menuManager = getViewSite().getActionBars().getMenuManager();

		menuManager.add(action);
	}

	public IToolBarManager getToolBarManager() {
		if (toolBarManager == null)
			toolBarManager = getViewSite().getActionBars().getToolBarManager();

		return toolBarManager;
	}

	public IMenuManager getMenuManager() {
		if (menuManager == null)
			menuManager = getViewSite().getActionBars().getMenuManager();
		return menuManager;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public abstract String getViewID();

	public IStatusLineManager getStatusLine() {
		if (statusLine != null)
			statusLine = getViewSite().getActionBars().getStatusLineManager();

		return statusLine;
	}

	public void writeMessage(Image image, String message) {
		if (statusLine != null)
			statusLine.setMessage(image, message);
	}

	public void writeErrorMessage(Image image, String message) {
		if (statusLine != null)
			statusLine.setErrorMessage(image, message);
	}

	public void setInput(Object arg) {
		input = arg;
	}
}
