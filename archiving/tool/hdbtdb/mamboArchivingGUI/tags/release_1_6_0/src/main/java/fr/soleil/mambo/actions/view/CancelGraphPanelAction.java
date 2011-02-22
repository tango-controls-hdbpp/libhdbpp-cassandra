package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;

public class CancelGraphPanelAction extends AbstractAction {

    public CancelGraphPanelAction (String name) {
        this.putValue(Action.NAME , name);
    }

    public void actionPerformed (ActionEvent e) {
    	
        new Thread() {
            @Override
            public void run () {
            	
                if (ExtractingManagerFactory.getCurrentImpl() != null) {
                    try {
                        ExtractingManagerFactory.getCurrentImpl().cancel();
                        ExtractingManagerFactory.getCurrentImpl().allow();
                    }
                    catch (Exception ex) {
                        // nothing to do : ignore
                    	ex.printStackTrace();
                    }
                }
            }
        }.start();
        ViewAttributesGraphPanel.getInstance().clean();
        ViewAttributesGraphPanel.getInstance().revalidate();
        ViewAttributesGraphPanel.getInstance().repaint();
    }

}
