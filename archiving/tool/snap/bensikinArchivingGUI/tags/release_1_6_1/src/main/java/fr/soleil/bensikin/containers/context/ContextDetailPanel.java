// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextDetailPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextDetailPanel.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: ContextDetailPanel.java,v $
// Revision 1.7 2006/01/12 10:27:48 ounsy
// minor changes
//
// Revision 1.6 2005/12/14 16:21:39 ounsy
// added alternate selection mode
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.context.ValidateAlternateSelectionAction;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.tango.util.entity.bean.AttributeTableSelectionBean;
import fr.soleil.tango.util.entity.datasource.ITangoEntitiesSelectionManager;

/**
 * Contains all informations about the current context, via inclusion of
 * ContextDataPanel and ContextAttributesPanel.
 * 
 * @author CLAISSE
 */
public class ContextDetailPanel extends JPanel {

	private static final long serialVersionUID = 2234119108150225491L;
	private static ContextDetailPanel contextDetailPanelInstance = null;
	private boolean isAlternateSelectionMode;
	private JPanel contextAttributesPanel = null;
	private AttributeTableSelectionBean attributeTableSelectionBean;
	private JButton selectValidateButton;

	private final static ITangoEntitiesSelectionManager BUFFERED_TANGO_SELECTION_MANAGER = AttributeTableSelectionBean
			.generateTangoManager(AttributeTableSelectionBean.BUFFERED_AND_SORTED);

	// This way, we are sure domains are loaded only once
	static {
		AttributeTableSelectionBean tempBean = new AttributeTableSelectionBean();
		tempBean.setTangoManager(BUFFERED_TANGO_SELECTION_MANAGER);
		tempBean.start();
		tempBean.setTangoManager(AttributeTableSelectionBean
				.generateTangoManager(AttributeTableSelectionBean.DUMMY));
		tempBean = null;
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static ContextDetailPanel getInstance() {
		if (contextDetailPanelInstance == null) {
			contextDetailPanelInstance = new ContextDetailPanel();
		}

		return contextDetailPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private ContextDetailPanel() {
		GUIUtilities.setObjectBackground(this, GUIUtilities.CONTEXT_COLOR);

		this.initComponents();

		boolean _hires = Bensikin.isHires();
		if (!_hires) {
			this.setMaximumSize(new Dimension(350, 100));
		}
		this.setPreferredSize(new Dimension(350, 100));
	}

	/**
     *
     */
	private void initComponents() {
		String msg = Messages.getMessage("CONTEXT_DETAIL_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP, GUIUtilities
						.getTitleFont());
		CompoundBorder cb = BorderFactory.createCompoundBorder(tb,
				BorderFactory.createEmptyBorder(2, 4, 4, 4));
		this.setBorder(cb);
		this.setLayout(new BorderLayout(5, 5));

		attributeTableSelectionBean = new AttributeTableSelectionBean();
		attributeTableSelectionBean
				.setTangoManager(BUFFERED_TANGO_SELECTION_MANAGER);
		attributeTableSelectionBean.getSelectionPanel().setGlobalBackground(
				GUIUtilities.getContextColor());
		// Add validate button;
		msg = Messages
				.getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_VALIDATE_BUTTON");
		selectValidateButton = new JButton(
				new ValidateAlternateSelectionAction(msg));
		GUIUtilities.setObjectBackground(selectValidateButton,
				GUIUtilities.CONTEXT_COLOR);
		GridBagConstraints selectValidateButtonConstraints = new GridBagConstraints();
		selectValidateButtonConstraints.fill = GridBagConstraints.VERTICAL;
		selectValidateButtonConstraints.gridx = 2;
		selectValidateButtonConstraints.gridy = 6;
		selectValidateButtonConstraints.weightx = 0;
		selectValidateButtonConstraints.weighty = 0;
		selectValidateButtonConstraints.insets = new Insets(6, 6, 0, 6);
		selectValidateButtonConstraints.anchor = GridBagConstraints.EAST;
		attributeTableSelectionBean.getSelectionPanel().getTopHalf().add(
				selectValidateButton, selectValidateButtonConstraints);

		this.add(ContextDataPanel.getInstance(), BorderLayout.NORTH);
		this.add(ContextActionPanel.getInstance(), BorderLayout.SOUTH);

		if (isAlternateSelectionMode) {
			contextAttributesPanel = attributeTableSelectionBean
					.getSelectionPanel();
		} else {
			contextAttributesPanel = ContextAttributesPanel.getInstance();
		}
		this.add(contextAttributesPanel, BorderLayout.CENTER);
	}

	/**
	 * @param isAlternateSelectionMode
	 *            The isAlternateSelectionMode to set.
	 */
	public void setAlternateSelectionMode(boolean _isAlternateSelectionMode) {
		if (isAlternateSelectionMode != _isAlternateSelectionMode) {
			this.remove(contextAttributesPanel);

			isAlternateSelectionMode = _isAlternateSelectionMode;

			if (isAlternateSelectionMode) {
				contextAttributesPanel = attributeTableSelectionBean
						.getSelectionPanel();
			} else {
				contextAttributesPanel = ContextAttributesPanel.getInstance();
			}
			this.add(contextAttributesPanel, BorderLayout.CENTER);

			this.validate();
			this.repaint();
		}
	}

	/**
	 * @return Returns the isAlternateSelectionMode.
	 */
	public boolean isAlternateSelectionMode() {
		return isAlternateSelectionMode;
	}

	public AttributeTableSelectionBean getAttributeTableSelectionBean() {
		return attributeTableSelectionBean;
	}

}
