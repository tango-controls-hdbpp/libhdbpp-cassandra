package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import fr.soleil.comete.widget.util.filechooser.MultiExtFileFilter;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.view.AbstractViewSpectrumPanel;
import fr.soleil.mambo.tools.Messages;

public class SaveSpectrumAction extends AbstractAction {

    private static final long serialVersionUID = -4686904303766697490L;
    private WeakReference<AbstractViewSpectrumPanel> panelReference;

    public SaveSpectrumAction(AbstractViewSpectrumPanel panel) {
        super();
        String name = "";
        if (panel == null) {
            panelReference = null;
        }
        else {
            name = panel.getName();
            panelReference = new WeakReference<AbstractViewSpectrumPanel>(panel);
        }
        putValue(NAME, String.format(Messages.getMessage("VIEW_SPECTRUM_TO_FILE"), name));
        putValue(SMALL_ICON, new ImageIcon(Mambo.class
                .getResource("/com/famfamfam/silk/table_save.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (panelReference != null) {
            AbstractViewSpectrumPanel panel = panelReference.get();
            if (panel != null) {
                String directory = panel.getFileDirectory();
                if (directory != null) {
                    JFileChooser fileChooser = new JFileChooser(directory);
                    fileChooser.setFileFilter(new MultiExtFileFilter(Messages
                            .getMessage("DIALOGS_FILE_CHOOSER_TEXT_FILES"), "txt", (String) null));
                    int choice = fileChooser.showSaveDialog(panel);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        String path = fileToSave.getAbsolutePath();
                        if (!"txt".equalsIgnoreCase(MultiExtFileFilter.getExtension(fileToSave))) {
                            path = path + ".txt";
                        }
                        panel.saveDataToFile(path);
                        fileToSave = null;
                    }
                    fileChooser = null;
                }
                directory = null;
            }
            panel = null;
        }
    }

}
