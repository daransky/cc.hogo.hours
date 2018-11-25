package cc.hogo.hours;

import org.daro.common.ui.OpenViewAction;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import cc.hogo.hours.views.hours.disponent.HoursViewDisponent;
import cc.hogo.hours.views.hours.year.HoursViewYear;
import cc.hogo.hours.views.hoursimport.HoursImportView;
import cc.hogo.hours.views.log.LogView;
import cc.hogo.hours.wizard.imp.ImportAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction prefAction;
	private ImportAction importAction;

	private OpenViewAction hoursByYear, hoursByDisponent, importView, logView;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		prefAction = ActionFactory.PREFERENCES.create(window);
		register(prefAction);

		importAction = new ImportAction(window);
		importAction.setImageDescriptor(Activator.getImageDescriptor("icons/import_wiz.png"));
		register(importAction);
		
		hoursByYear = new OpenViewAction(window, "Jahresübersicht", HoursViewYear.ID);
		hoursByYear.setImageDescriptor(Activator.getImageDescriptor("icons/person.gif"));
		register(hoursByYear);

		hoursByDisponent = new OpenViewAction(window, "Übersicht pro person", HoursViewDisponent.ID);
		hoursByDisponent.setImageDescriptor(Activator.getImageDescriptor("icons/overview.gif"));
		register(hoursByDisponent);

		importView = new OpenViewAction(window, "Import Übersicht", HoursImportView.ID);
		importView.setImageDescriptor(Activator.getImageDescriptor("icons/import.gif"));
		register(importView);

		logView = new OpenViewAction(window, "Log", LogView.ID);
		logView.setImageDescriptor(Activator.getImageDescriptor("icons/log.gif"));
		register(logView);

	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("File", IWorkbenchActionConstants.M_FILE); //$NON-NLS-1$
		MenuManager actionsMenu = new MenuManager("Actions", IWorkbenchActionConstants.M_EDIT); //$NON-NLS-1$
		MenuManager helpMenu = new MenuManager("Help", IWorkbenchActionConstants.M_HELP); //$NON-NLS-1$

		menuBar.add(fileMenu);
		menuBar.add(actionsMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		menuBar.add(helpMenu);

		fileMenu.add(prefAction);
		fileMenu.add(new Separator());
		fileMenu.add(importAction);
		fileMenu.add(importView);
		fileMenu.add(logView);

		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		helpMenu.add(aboutAction);
	}

}
