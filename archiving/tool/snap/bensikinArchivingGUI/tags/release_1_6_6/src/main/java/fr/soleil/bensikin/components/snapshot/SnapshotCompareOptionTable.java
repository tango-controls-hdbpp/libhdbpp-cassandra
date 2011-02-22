package fr.soleil.bensikin.components.snapshot;

import java.util.ArrayList;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

import fr.soleil.bensikin.components.renderers.SnapshotCompareOptionTableRenderer;
import fr.soleil.bensikin.models.StringArrayTableModel;

public class SnapshotCompareOptionTable extends JXTable {

	private static final long serialVersionUID = -6846250878274697189L;
	private final static SnapshotCompareOptionTableRenderer renderer = new SnapshotCompareOptionTableRenderer();

	public SnapshotCompareOptionTable() {
		super();
		init();
	}

	public SnapshotCompareOptionTable(StringArrayTableModel dm) {
		super();
		init();
		setModel(dm);
	}

	private void init() {
		setDefaultRenderer(Object.class, renderer);
		getTableHeader().setDefaultRenderer(renderer);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setOpaque(false);
	}

	@Override
	public void setModel(TableModel dataModel) {
		if (dataModel == null || dataModel instanceof StringArrayTableModel) {
			super.setModel(dataModel);
			packAll();
			revalidate();
		}
	}

	public String[] getNameModelAsArray() {
		String[] model = new String[getColumnCount()];
		for (int i = 0; i < model.length; i++) {
			model[i] = getColumnName(i);
		}
		return model;
	}

	public ArrayList<String> getNameModelAsList() {
		ArrayList<String> model = new ArrayList<String>();
		for (int i = 0; i < getColumnCount(); i++) {
			model.add(getColumnName(i));
		}
		return model;
	}

	public void addName(String name) {
		ArrayList<String> model = getNameModelAsList();
		if (name != null && !model.contains(name)) {
			model.add(name);
		}
		String[] result = model.toArray(new String[model.size()]);
		setModel(new StringArrayTableModel(result));
		result = null;
		revalidate();
	}

	public void removeName(String name) {
		ArrayList<String> model = getNameModelAsList();
		if (name != null && model.contains(name)) {
			model.remove(name);
		}
		String[] result = model.toArray(new String[model.size()]);
		setModel(new StringArrayTableModel(result));
		result = null;
		revalidate();
	}

	public int indexOf(String value) {
		if (value != null) {
			String[] values = getNameModelAsArray();
			for (int i = 0; i < values.length; i++) {
				if (value.equals(values[i])) {
					values = null;
					return i;
				}
			}
			values = null;
		}
		return -1;
	}

	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		return renderer;
	}

}
