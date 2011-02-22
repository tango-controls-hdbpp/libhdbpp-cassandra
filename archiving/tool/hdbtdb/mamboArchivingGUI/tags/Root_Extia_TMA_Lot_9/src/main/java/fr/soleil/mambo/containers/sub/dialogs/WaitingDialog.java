package fr.soleil.mambo.containers.sub.dialogs;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.Messages;

public class WaitingDialog extends JDialog {

    private static final long      serialVersionUID = -5480942031744819675L;
    protected static WaitingDialog instance;
    protected static String        firstPart        = Messages
                                                            .getMessage("DIALOGS_WAITING_LOADING_TITLE");
    protected static String        secondPart       = Messages
                                                            .getMessage("DIALOGS_WAITING_WAIT_TITLE");

    public static String getFirstMessage() {
        return firstPart;
    }

    public static String getSecondMessage() {
        return secondPart;
    }

    public static boolean isInstanceVisible() {
        if (instance == null) {
            return false;
        } else {
            return instance.isVisible();
        }
    }

    public static void openInstance() {
        if (instance == null) {
            instance = new WaitingDialog();
        }
        instance.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        instance.setTitle(firstPart + " " + secondPart);
        int myWidth = 500;
        int myHeight = 0;
        instance.setSize(myWidth, myHeight);
        instance.setLocationRelativeTo(MamboFrame.getInstance());
        instance.setVisible(true);
    }

    public static void closeInstance() {
        if (instance == null) {
            instance = new WaitingDialog();
        }
        firstPart = Messages.getMessage("DIALOGS_WAITING_LOADING_TITLE");
        secondPart = Messages.getMessage("DIALOGS_WAITING_WAIT_TITLE");
        instance.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        instance.setVisible(false);
    }

    protected WaitingDialog() {
        super(MamboFrame.getInstance(), firstPart + " " + secondPart);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        repaint();
    }

    /**
     * Changes the first part of the message (default : "loading...")
     * 
     * @param _firstPart
     *            The String that replaces the first part of the message
     */
    public static void changeFirstMessage(String _firstPart) {
        firstPart = _firstPart;
        if (isInstanceVisible()) {
            instance.setTitle(firstPart + " " + secondPart);
        }
    }

    /**
     * Changes the second part of the message (default :
     * "(Please Wait: It may take a while)")
     * 
     * @param _secondPart
     *            The String that replaces the second part of the message
     */
    public static void changeSecondMessage(String _secondPart) {
        secondPart = _secondPart;
        if (isInstanceVisible()) {
            instance.setTitle(firstPart + " " + secondPart);
        }
    }

}
