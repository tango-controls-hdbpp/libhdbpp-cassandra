// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/OpenedConfComboBox.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenedConfComboBox.
// (Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenedConfComboBox.java,v $
// Revision 1.2 2006/04/05 13:42:53 ounsy
// function centralization
//
// Revision 1.1 2005/11/29 18:27:24 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class OpenedConfComboBox<H> extends JComboBox {

    private static final long serialVersionUID = -4856256936806633924L;
    // MULTI-CONF
    protected LimitedStack<H> stack;

    /**
     * 
     */
    protected OpenedConfComboBox(LimitedStack<H> stack) {
        super();
        setElements(stack);
        Dimension maxDimension = new Dimension(300, 25);
        setMaximumSize(maxDimension);
        setMinimumSize(maxDimension);
        setPreferredSize(maxDimension);
        setSize(maxDimension);
    }

    public LimitedStack<H> getElements() {
        return stack;
    }

    protected void setElements(LimitedStack<H> stack) {
        this.stack = stack;
        setModel(new DefaultComboBoxModel(stack));
    }

    public void setMaxSize(int _size) {
        stack.setMaxSize(_size);
    }

    public int getMaxSize() {
        return stack.getMaxSize();
    }

    public int getStackSize() {
        return stack.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object anObject) {
        stack.selectElement((H) anObject);
        if ((getSelectedItem() == null)
                || (!getSelectedItem().equals(anObject))) {
            super.setSelectedItem(anObject);
        }
    }

    public void push(H anObject) {
        stack.push(anObject);
    }

}
