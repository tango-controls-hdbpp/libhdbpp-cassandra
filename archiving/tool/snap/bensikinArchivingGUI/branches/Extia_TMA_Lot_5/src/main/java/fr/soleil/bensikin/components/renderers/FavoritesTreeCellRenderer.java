package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.Bensikin;

public class FavoritesTreeCellRenderer extends DefaultTreeCellRenderer {
	private ImageIcon attributeIcon = new ImageIcon(Bensikin.class
			.getResource("icons/attribute.gif"));
	private ImageIcon folderIcon = new ImageIcon(Bensikin.class
			.getResource("icons/folder.gif"));
	private ImageIcon folderOpenedIcon = new ImageIcon(Bensikin.class
			.getResource("icons/folder_opened.gif"));

	public FavoritesTreeCellRenderer() {
		super();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		TreePath path = tree.getPathForRow(row);
		if (path != null) {
			boolean isContextNode = false;
			Object[] path_o = path.getPath();
			for (int i = 0; i < path_o.length - 1; i++) {
				DefaultMutableTreeNode currentPathElement = (DefaultMutableTreeNode) path_o[i + 1];
				if (currentPathElement.getUserObject().getClass().equals(
						DefaultMutableTreeNode.class)) {
					isContextNode = true;
				}
			}

			JLabel label = new JLabel(value.toString().trim());
			label.setOpaque(true);
			Font font = new Font("Arial", Font.PLAIN, 11);
			label.setFont(font);
			label.setBackground(Color.WHITE);
			label.setForeground(Color.BLACK);
			if (sel) {
				label.setBackground(new Color(0, 0, 120));
				label.setForeground(Color.WHITE);
			}
			label.setIgnoreRepaint(false);
			label.setIconTextGap(2);

			if (isContextNode) {
				label.setIcon(attributeIcon);
			} else {
				if (sel || expanded) {
					label.setIcon(folderOpenedIcon);
				} else {
					label.setIcon(folderIcon);
				}
			}

			label.repaint();
			return label;
		}

		return super.getTreeCellRendererComponent(tree, value, sel, expanded,
				leaf, row, hasFocus);
	}

}
