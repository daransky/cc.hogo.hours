package cc.hogo.hours.wizard.imp;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import cc.hogo.hours.views.log.LogEntry;
import cc.hogo.hours.views.log.Logger;

public class ImportWizard extends Wizard {

	public static final String ID = "cc.hogo.hours.wizard.ImportWizard";

	private ImportContext context = new ImportContext();
	private IWizardPage last;
	private IWizardPage next = null;

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page.canFlipToNextPage()) {
			next = super.getNextPage(page);
			((IImportPage) next).reload();
		}
		return next;
	}

	@Override
	public boolean canFinish() {
		return next == last;
	}


	@Override
	public boolean performFinish() {
		LogEntry entry = Logger.instance().getImportEntry(context.year, context.month);
		if (entry != null && !MessageDialog.openConfirm(getShell(), "Import vorhanden",
					String.format("Import mit aktuellen Datum (%d/%d) bereits vorhanden. Möchten Sie vortfahren?",
							context.month, context.year))) {
				return false;
			}

		try {
			context.importRecords();
			MessageDialog.openConfirm(getShell(), "Import ist erfolgreich", "Es wurden "+context.getHours().size()+" sätze importiert." );
		} catch (Exception e) {
			DialogPage page = (DialogPage) getContainer().getCurrentPage();
			page.setErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void addPages() {
		last = new ImportPage2(context);
		addPage(new ImportPage(context));
		addPage(last);
	}

}
