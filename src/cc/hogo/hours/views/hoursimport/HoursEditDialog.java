package cc.hogo.hours.views.hoursimport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.db.HourEntry;

public class HoursEditDialog extends TitleAreaDialog {

	private Map<String, String> id2value = new HashMap<>();
	private Map<String, String> value2id = new HashMap<>();
	private final HourEntry entry;

	public HoursEditDialog(Shell parentShell, HourEntry entry) {
		super(parentShell);
		this.entry = entry;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Stundensatz");
//		setMessage("Hier können die Attributen von This is a TitleAreaDialog", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		Composite body = new Composite(area, SWT.NONE);
		body.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		body.setLayout(new GridLayout(4, false));

		Text text = addControlString(body, "Disponent", null, null);
		String[] proposals = id2value.values().toArray(new String[id2value.size()]);
		AutoCompleteField field = new AutoCompleteField(text, new TextContentAdapter(), proposals);
		field.setProposals(proposals);
		if (entry != null) {
			String value = id2value.get(entry.getDisponentId());
			String id = entry.getDisponentId();
			if (id == null)
				id = "";
			text.setText(value == null ? id : value);
		}
		text.addModifyListener(l -> {
			String t = text.getText();
			String id = value2id.get(t);
			entry.setDisponentId(id == null ? t : id);
		});
		addControlInt(body, "Kundennummer", HourEntry::getKundenNummer, HourEntry::setKundenNummer);
		addControlString(body, "Kundenname", HourEntry::getKundenName, HourEntry::setKundenName);
		addControlFloat(body, "Fakturstunden", HourEntry::getFakturStunden, HourEntry::setFakturStunden);
		addControlFloat(body, "Lohnstunden", HourEntry::getLohnStunden, HourEntry::setLohnStunden);
		Text t = addControlString(body, "Info", HourEntry::getInfo, HourEntry::setInfo);
		GridData id = GridDataFactory.copyData((GridData) t.getLayoutData());
		id.verticalSpan = 3;
		t.setLayoutData(id);
		return area;
	}

	Text addControl(Composite parent, String text, int swt) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalIndent = 5;

		GridData filler = new GridData(GridData.FILL_HORIZONTAL);
		filler.grabExcessHorizontalSpace = true;
		filler.horizontalIndent = 5;

		Label label = new Label(parent, SWT.NONE);
		GridData filler1 = new GridData();
		filler1.horizontalIndent = 15;
		label.setLayoutData(filler1);
		label.setText(text);

		Text c = new Text(parent, swt);
		c.setLayoutData(gd);

		new Label(parent, SWT.NONE).setLayoutData(filler);

		return c;
	}

	Listener numericValidator = (e) -> {
		String str = e.text;
		for (int i = 0, max = str.length(); i < max; i++) {
			if (str.charAt(i) != '.' && !Character.isDigit(str.charAt(i))) {
				e.doit = false;
				return;
			}
		}
	};

	void addControlInt(Composite parent, String text, Function<HourEntry, Integer> getter,
			BiConsumer<HourEntry, Integer> setter) {
		Text c = addControl(parent, text, SWT.RIGHT);
		if (getter != null)
			c.setText(Integer.toString(getter.apply(entry)));
		if (setter != null)
			c.addModifyListener(e -> setter.accept(entry, Integer.parseInt(c.getText())));
		c.addListener(SWT.Verify, numericValidator);
	}

	void addControlFloat(Composite parent, String text, Function<HourEntry, Float> getter,
			BiConsumer<HourEntry, Float> setter) {
		Text c = addControl(parent, text, SWT.RIGHT);
		if (getter != null)
			c.setText(Float.toString(getter.apply(entry)));
		if (setter != null)
			c.addModifyListener(e -> setter.accept(entry, Float.parseFloat(c.getText())));
		c.addListener(SWT.Verify, numericValidator);
	}

	Text addControlString(Composite parent, String text, Function<HourEntry, String> getter,
			BiConsumer<HourEntry, String> setter) {
		Text c = addControl(parent, text, SWT.NONE);
		if (getter != null) {
			String value = getter.apply(entry);
			c.setText(value != null ? value : "");
		}
		if (setter != null)
			c.addModifyListener(e -> setter.accept(entry, c.getText()));
		return c;
	}

	public HourEntry getEntry() {
		return entry;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public void setDisponents(Collection<Disponent> values) {
		if (values != null)
			values.forEach(e -> {
				value2id.put(e.getName() == null ? e.getSid() : e.getName(), e.getSid());
				id2value.put(e.getSid(), e.getName() == null ?  e.getSid() : e.getName() );
			});
	}

}
