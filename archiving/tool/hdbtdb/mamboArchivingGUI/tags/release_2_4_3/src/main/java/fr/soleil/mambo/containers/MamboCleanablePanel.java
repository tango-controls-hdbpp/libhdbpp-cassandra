package fr.soleil.mambo.containers;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.tools.Messages;

public abstract class MamboCleanablePanel extends JPanel {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = -2545021518904028083L;

    public abstract void clean();

    public abstract void lightClean();

    public abstract void loadPanel();

    @Override
    public abstract String getName();

    public abstract String getFullName();

    public void outOfMemoryErrorManagement() {
	final String msg = String.format(Messages.getMessage("DIALOGS_VIEW_MEMORY_ERROR"),
		getName());
	JOptionPane.showMessageDialog(MamboFrame.getInstance(), msg, Messages
		.getMessage("DIALOGS_VIEW_MEMORY_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
	logger.trace(ILogger.LEVEL_ERROR, msg);
    }

}
