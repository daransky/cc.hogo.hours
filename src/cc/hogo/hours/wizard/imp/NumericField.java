package cc.hogo.hours.wizard.imp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NumericField {

	public static Text create(Composite parent, int style) {

		final Text text = new Text(parent, style);
		text.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				for (int i = 0, max = string.length(); i < max; i++) {
					char ch = string.charAt(i);
					if(ch < '0' || ch > '9') {
						e.doit = false;
						return;
					}
				}
			}
		});
		return text;
	}

}
