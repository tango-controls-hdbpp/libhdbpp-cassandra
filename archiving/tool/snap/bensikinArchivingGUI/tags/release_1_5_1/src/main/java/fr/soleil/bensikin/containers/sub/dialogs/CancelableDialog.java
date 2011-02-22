package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;

public abstract class CancelableDialog extends JDialog {

    private static final long serialVersionUID = -5891743257770246069L;

    public CancelableDialog() {
        super();
    }

    public CancelableDialog(Frame owner) {
        super(owner);
    }

    public CancelableDialog(Dialog owner) {
        super(owner);
    }

    public CancelableDialog(Window owner) {
        super(owner);
    }

    public CancelableDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    public CancelableDialog(Frame owner, String title) {
        super(owner, title);
    }

    public CancelableDialog(Dialog owner, boolean modal) {
        super(owner, modal);
    }

    public CancelableDialog(Dialog owner, String title) {
        super(owner, title);
    }

    public CancelableDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    public CancelableDialog(Window owner, String title) {
        super(owner, title);
    }

    public CancelableDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public CancelableDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public CancelableDialog(Window owner, String title,
            ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    public CancelableDialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public CancelableDialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public CancelableDialog(Window owner, String title,
            ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    public abstract void cancel();

}
