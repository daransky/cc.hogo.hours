package cc.hogo.hours.wizard.imp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class ImportAction extends Action {
	
	public static final String ID = "cc.hogo.hours.wizard.ImportAction";
	private final Shell shell;
	
	public ImportAction(final IWorkbenchWindow window) {
		super("Import  ... ");
		this.shell = window.getShell();
		setId(ID);
	}
	@Override
	public void run() {
		WizardDialog dialog = new WizardDialog(shell, new ImportWizard());
		dialog.open();
	}
}
