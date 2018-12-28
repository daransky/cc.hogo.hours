package cc.hogo.hours.wizard.imp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class ImportAllAction extends Action {
	
	public static final String ID = "cc.hogo.hours.wizard.ImportAllAction";
	private final Shell shell;
	
	public ImportAllAction(final IWorkbenchWindow window) {
		super("Import Historie ... ");
		this.shell = window.getShell();
		setId(ID);
	}
	@Override
	public void run() {
		WizardDialog dialog = new WizardDialog(shell, new ImportAllWizard());
		dialog.open();
	}
}
