package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.containers.sub.dialogs.ViewImageDialog;
import fr.soleil.bensikin.data.attributes.FakeNumberImage;
import fr.soleil.bensikin.tools.Messages;

public class ViewImageAction extends AbstractAction {
	private FakeNumberImage image;
	private ViewImageDialog dialog;

	public ViewImageAction(String name, Object value, int data_type,
			String displayFormat) {
		super();
		this.putValue(Action.NAME, Messages
				.getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW_IMAGE"));
		image = null;
		image = new FakeNumberImage();
		image.setName(name);
		dialog = new ViewImageDialog(image);

		double[][] val = new double[0][0];
		switch (data_type) {
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			Byte[][] bval = (Byte[][]) value;
			if (bval != null && bval.length > 0) {
				val = new double[bval.length][bval[0].length];
				for (int i = 0; i < bval.length; i++) {
					for (int j = 0; j < bval[i].length; j++) {
						val[i][j] = bval[i][j].doubleValue();
					}
				}
			}
			break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			Short[][] sval = (Short[][]) value;
			if (sval != null && sval.length > 0) {
				val = new double[sval.length][sval[0].length];
				for (int i = 0; i < sval.length; i++) {
					for (int j = 0; j < sval[i].length; j++) {
						val[i][j] = sval[i][j].doubleValue();
					}
				}
			}
			break;
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			Integer[][] lval = (Integer[][]) value;
			if (lval != null && lval.length > 0) {
				val = new double[lval.length][lval[0].length];
				for (int i = 0; i < lval.length; i++) {
					for (int j = 0; j < lval[i].length; j++) {
						val[i][j] = lval[i][j].doubleValue();
					}
				}
			}
			break;
		case TangoConst.Tango_DEV_FLOAT:
			Float[][] fval = (Float[][]) value;
			if (fval != null && fval.length > 0) {
				val = new double[fval.length][fval[0].length];
				for (int i = 0; i < fval.length; i++) {
					for (int j = 0; j < fval[i].length; j++) {
						val[i][j] = fval[i][j].doubleValue();
					}
				}
			}
			break;
		case TangoConst.Tango_DEV_DOUBLE:
			Double[][] dval = (Double[][]) value;
			if (dval != null && dval.length > 0) {
				val = new double[dval.length][dval[0].length];
				for (int i = 0; i < dval.length; i++) {
					for (int j = 0; j < dval[i].length; j++) {
						val[i][j] = dval[i][j].doubleValue();
					}
				}
			}
			break;
		default: // nothing to do
		}
		image.setValue(val);
		image.setFormat(displayFormat);
	}

	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}
}
