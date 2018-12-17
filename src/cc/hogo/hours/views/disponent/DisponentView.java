package cc.hogo.hours.views.disponent;

import java.time.Year;
import java.util.Collection;
import java.util.Iterator;

import org.daro.common.ui.AbstractView;
import org.daro.common.ui.TreeContentProvider;
import org.daro.common.ui.TreeNodeData;
import org.daro.common.ui.UIError;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import cc.hogo.hours.Activator;
import cc.hogo.hours.core.CustomAction;
import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.views.hours.HoursViewAll;
import cc.hogo.hours.views.hours.disponent.HoursViewDisponent;
import cc.hogo.hours.views.hours.year.HoursViewYear;

public class DisponentView extends AbstractView {
	public static final String ID = "cc.hogo.hours.DisponentView"; //$NON-NLS-1$
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private TreeNodeData root = new TreeNodeData("", TreeNodeData.NULL_NODE);
	private TreeNodeData home = new TreeNodeData("Hogo", new TreeNodeData("Hogo", Disponent.All), Disponent.All);
	private TreeViewer viewer;
	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.FILL);
		container.setLayout(new FillLayout());
		toolkit.paintBordersFor(container);

		CustomAction refresh = new CustomAction("Laden", Activator.getImageDescriptor("icons/refresh.gif"), 
				() -> refresh());

		IToolBarManager toolBar = getViewSite().getActionBars().getToolBarManager(); 
		toolBar.add(refresh);
		
		initializeTree(container);
	}

	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	private void initializeTree(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(new ILabelProvider() {
			final Image imgDisponent = Activator.getImageDescriptor("icons/person.gif").createImage(),
					imgYear = Activator.getImageDescriptor("icons/report_obj.gif").createImage(),
					imgHome = Activator.getImageDescriptor("icons/home.png").createImage();

			public void removeListener(ILabelProviderListener listener) {//
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
				imgDisponent.dispose();
				imgYear.dispose();
			}

			public void addListener(ILabelProviderListener listener) {//
			}

			@Override
			public String getText(Object element) {
				return element.toString();
			}

			@Override
			public Image getImage(Object element) {
				final TreeNodeData data = (TreeNodeData) element;
				switch (data.getType()) {
				case Disponent.Year:
					return imgYear;
				case Disponent.Disponent:
					return imgDisponent;
				case Disponent.All:
					return imgHome;
				default: 
					return null;
				}
			}
		});
		viewer.setContentProvider(new TreeContentProvider());

		refresh();
		
		root.addChild(home);
		viewer.setInput(root);
		
		viewer.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Tree tree = (Tree) e.getSource();
				TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {

					final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					final TreeNodeData node = (TreeNodeData) selection[0].getData();
					try {
						if (node.getType() == Disponent.All) {
							HoursViewAll view = (HoursViewAll) page.showView(HoursViewAll.ID);
							view.setInput(selection[0].getData());
						} else if (node.getType() == Disponent.Year) {
							HoursViewYear view = (HoursViewYear) page.showView(HoursViewYear.ID);
							view.setInput(selection[0].getData());
						} else if (node.getType() == Disponent.Disponent) {
							HoursViewDisponent view = (HoursViewDisponent) page.showView(HoursViewDisponent.ID);
							view.setInput(selection[0].getData());
						}
					} catch (PartInitException e1) {
						UIError.showError("Internal Error", e1);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) { //
			}
		});
	}

	@Override
	public void setFocus() { //
	}

	@Override
	public void refresh() {
		TreeNodeData current = null;
		
		home.removeChilds();
		
		try(DisponentModel db = new DisponentModel()) {
			final int now = Year.now().getValue();
			
			Collection<Integer> years = db.getYears();
			for (Integer year : years) {
				TreeNodeData item = new TreeNodeData(year.toString(), Disponent.Year);
				Iterator<Disponent> it = db.select(year);
				while (it.hasNext()) {
					Disponent disponent = it.next();
					TreeNodeData node = new TreeNodeData(disponent.toString(), Disponent.Disponent);
					node.setData(disponent);
					item.addChild(node);
				}
				home.addChild(item);

				if( year == now) {
					current = item;
				}
				
			}
		} catch (Exception e) {
			UIError.showError("DB Fehler", e);
		}

		viewer.expandToLevel(current!=null ? current : home, AbstractTreeViewer.ALL_LEVELS);
			
//		viewer.refresh();
	}

	@Override
	public String getViewID() {
		return ID;
	}


}
