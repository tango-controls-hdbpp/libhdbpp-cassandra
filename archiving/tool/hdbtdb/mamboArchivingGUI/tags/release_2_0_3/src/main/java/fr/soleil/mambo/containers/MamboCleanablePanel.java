package fr.soleil.mambo.containers;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;


public abstract class MamboCleanablePanel extends JPanel
{
    public abstract void clean();
    public abstract void loadPanel();
    public abstract String getName();
    public abstract String getFullName();

    public void outOfMemoryErrorManagement()
    {
    	String msg = getName() + " Panel. " + Messages.getLogMessage( "LOAD_VIEW_MEMORY_ERROR" );
    	
    	JOptionPane.showMessageDialog(MamboFrame.getInstance(), msg,
    			Messages.getMessage( "DIALOG_VIEW_MEMORY_ERROR_TITLE"), 
    			JOptionPane.ERROR_MESSAGE);
    	
      	LoggerFactory.getCurrentImpl().trace( ILogger.LEVEL_ERROR , msg );
    }
}