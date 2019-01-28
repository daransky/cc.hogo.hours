package cc.hogo.hours.views;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.daro.common.ui.AbstractView;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.CustomAction;

public abstract class HoursAbstractView extends AbstractView {

	protected TabFolder folder;
	private boolean expanded;
	@Override
	public void createPartControl(Composite parent) {
		folder = new TabFolder(parent, SWT.BORDER);

		TabItem item = newTab("Tabelle");
		item.setControl(createTable(folder));

		item = newTab("Übersicht");
		item.setControl(createChart(folder));

	}

	@Override
	public void setFocus() {
	}

	protected abstract Composite createTable(Composite parent);

	protected abstract Composite createChart(Composite parent);

	protected TabItem newTab(String title) {
		TabItem item = new TabItem(folder, SWT.BORDER);
		item.setText(title);
		return item;
	}

	protected void addCollapseExpandMenu(TreeViewer viewer) {
		CustomAction export = new CustomAction("Copy to clipboard", Activator.getImageDescriptor("icons/export.gif"),
				() -> { 
					if( expanded )
						viewer.collapseAll();
					else
						viewer.expandAll();
					expanded = !expanded;
				});
		IToolBarManager toolBar = getViewSite().getActionBars().getToolBarManager();
		toolBar.add(export);
	}

	protected void addExportToFileMenu(Consumer<Path> toFile) {
		CustomAction export = new CustomAction("Export to file ...", Activator.getImageDescriptor("icons/export.gif"),
				() -> { 
					 FileDialog fd = new FileDialog(getSite().getShell(), SWT.SAVE);
				        fd.setText("Export to file ...");
				        String[] filterExt = { "*.csv", "*.*" };
				        fd.setFilterExtensions(filterExt);
				        
				        String selected = fd.open();
				        if( selected != null ) {
				        	Path path = Paths.get(selected);
				        	toFile.accept(path);
				        }
						
				});

		IToolBarManager toolBar = getViewSite().getActionBars().getToolBarManager();
		toolBar.add(export);
	}
	
	protected void addExportToClipboardMenu(Supplier<String> toClipboard) {
		CustomAction export = new CustomAction("Copy to clipboard", Activator.getImageDescriptor("icons/copy.gif"), () -> {
			Clipboard clipboard = new Clipboard(Display.getCurrent());
			HTMLTransfer htmlTransfer = HTMLTransfer.getInstance();
			Transfer[] transfers = new Transfer[] { htmlTransfer };
			clipboard.setContents(new Object[] { toClipboard.get() }, transfers);
			clipboard.dispose();
		});
		
		IToolBarManager toolBar = getViewSite().getActionBars().getToolBarManager();
		toolBar.add(export);
	}
}
