/*
 * Synchrotron Soleil File : ViewImageTableDialog.java Project : mambo_IMAGES
 * Description : Author : CLAISSE Original : 25 avr. 2006 Revision: Author:
 * Date: State: Log: ImageViewDialog.java,v
 */
/*
 * Created on 25 avr. 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.ViewImageAction;
import fr.soleil.mambo.components.view.images.ImageViewTable;
import fr.soleil.mambo.components.view.images.ImageViewTableModel;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author CLAISSE
 */
public class ViewImageTableDialog extends JDialog {

	private static final long serialVersionUID = 3924599588317242967L;
	private ImageData imageData;
	private JPanel myPanel;
	private JScrollPane scrollPane;
	private JButton viewButton;
	private ImageIcon viewIcon = new ImageIcon(Mambo.class
			.getResource("icons/viewImage.gif"));
	private String displayFormat;

	/**
     * 
     */
	public ViewImageTableDialog(ImageData _imageData, String _displayFormat) {
		super(MamboFrame.getInstance(), "", true);

		this.imageData = _imageData;
		this.loadImage();
		this.displayFormat = _displayFormat;

		this.initComponents();
		this.addComponents();
		this.setLayout();
		this.setSizeAndLocation();
	}

	private void setSizeAndLocation() {
		String title = this.imageData.getName() + " ("
				+ this.imageData.getDate() + ")";
		this.setTitle(title);
		int x = MamboFrame.getInstance().getX()
				+ MamboFrame.getInstance().getWidth();
		x /= 2;
		this.setLocation(x, MamboFrame.getInstance().getY() + 30);
	}

	private void setLayout() {
		myPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(),
				1, 0, 10, 0, 10, true);
	}

	private void addComponents() {
		myPanel.add(this.scrollPane);
		myPanel.add(this.viewButton);
		this.setContentPane(myPanel);
	}

	private void initComponents() {
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
		myPanel = new JPanel();
		GUIUtilities.setObjectBackground(myPanel, GUIUtilities.VIEW_COLOR);

		ImageViewTableModel model = new ImageViewTableModel(this.imageData
				.getValue());
		ImageViewTable table = new ImageViewTable(model);

		this.scrollPane = new JScrollPane(table);
		GUIUtilities.setObjectBackground(scrollPane, GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(scrollPane.getViewport(),
				GUIUtilities.VIEW_COLOR);
		viewButton = new JButton(new ViewImageAction(imageData.getName(),
				imageData.getValue(), displayFormat));
		viewButton.setMargin(new Insets(0, 0, 0, 0));
		viewButton.setIcon(viewIcon);
	}

	private void loadImage() {
		try {
			this.imageData.load();
		} catch (ArchivingException e) {
			e.printStackTrace();
		}
	}

}
