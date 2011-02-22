package fr.soleil.mambo.actions.view;

import java.awt.event.MouseEvent;

import net.infonode.gui.mouse.MouseButtonListener;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.util.docking.infonode.InfoNodeView;

public class VCTabMouseButtonListener implements MouseButtonListener {

    @Override
    public void mouseButtonEvent(MouseEvent event) {
        if ((event != null) && (event.getSource() instanceof InfoNodeView)) {
            InfoNodeView view = (InfoNodeView) event.getSource();
            if (view.getId() instanceof ViewConfiguration) {
                OpenedVCComboBox.getInstance().selectElement(
                        (ViewConfiguration) view.getId());
            }
            view = null;
        }
    }

}
