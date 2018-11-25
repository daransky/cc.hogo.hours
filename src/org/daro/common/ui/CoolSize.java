package org.daro.common.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;

public class CoolSize {

	
	public static CoolItem getNew(CoolBar coolBar, ToolBar toolBar)
	{
		CoolItem item = new CoolItem(coolBar, SWT.NULL);
		item.setControl(toolBar);
		Point size = toolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT);
		Point coolSize = item.computeSize (size.x, size.y);
		item.setSize(coolSize);
		
		return item;
	}
	
	
}
