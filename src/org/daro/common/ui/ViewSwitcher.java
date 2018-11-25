package org.daro.common.ui;

import java.util.Hashtable;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public final class ViewSwitcher {
	
	private static Hashtable<String,ViewSwitcher> table = new Hashtable<String, ViewSwitcher>(5);
	
	private AbstractView selected=null;
	private String perspectiveId;
	
	ViewSwitcher(String id) { perspectiveId = id; }
	
	public AbstractView switchView(String viewID) throws PartInitException {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		
		if(this.selected != null)
			if(viewID.equals(selected.getViewID())) {
				return selected;
			}
		
		AbstractView temp = selected;
		this.selected = (AbstractView)page.showView(viewID);
		
		if(temp!= null) {
          	page.hideView(temp);
           	temp.dispose();
        }
		return this.selected;
	}
	
	public void setView(AbstractView view) { 
		selected = view;
	}
	
	public String getPerspectiveId() { return perspectiveId; }
	
	public static ViewSwitcher getDefault(String perspectiveId) {
		ViewSwitcher switcher = table.get(perspectiveId);
		if(switcher == null) {
			switcher = new ViewSwitcher(perspectiveId);
			table.put(perspectiveId, switcher);
		}
		return switcher;
	}
}
