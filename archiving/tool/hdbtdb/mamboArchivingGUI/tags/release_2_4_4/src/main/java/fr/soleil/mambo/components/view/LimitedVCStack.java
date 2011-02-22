// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/LimitedVCStack.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class LimitedVCStack.
// (Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: LimitedVCStack.java,v $
// Revision 1.2 2006/05/16 09:36:27 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:28:12 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.view;

import java.util.Enumeration;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.LimitedStack;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;

public class LimitedVCStack extends LimitedStack<ViewConfiguration> {

    private static final long serialVersionUID = -6772964460314853854L;

    /**
     * @param _maxSize
     */
    public LimitedVCStack(final int _maxSize) {
	super(_maxSize);
    }

    /**
     * 
     */
    public LimitedVCStack() {
	super();
    }

    @Override
    public boolean removeElement(final Object item) {
	final boolean ret = super.removeElement(item);
	if (ret) {
	    return ret;
	}

	final ViewConfiguration vc = (ViewConfiguration) item;
	final Enumeration<ViewConfiguration> enumeration = elements();
	while (enumeration.hasMoreElements()) {
	    final ViewConfiguration next = enumeration.nextElement();
	    if (next.equals(vc)) {
		return super.removeElement(next);
	    }
	}
	return false;
    }

    @Override
    public String toString() {
	final Enumeration<ViewConfiguration> enumeration = super.elements();
	String ret = "";
	while (enumeration.hasMoreElements()) {
	    final ViewConfiguration vc = enumeration.nextElement();
	    ret += vc.toString();
	    ret += GUIUtilities.CRLF;
	}
	return ret;
    }

    /**
     * @return
     */
    public Object toString2() {
	final Enumeration<ViewConfiguration> enumeration = super.elements();
	String ret = "";
	while (enumeration.hasMoreElements()) {
	    final ViewConfiguration vc = enumeration.nextElement();
	    final ViewConfigurationData vcData = vc.getData();
	    ret += vcData.getName();
	    ret += "|";
	    ret += vcData.getPath();
	    ret += GUIUtilities.CRLF;
	}

	return ret;
    }

    public boolean save(final ILogger logger) {
	final Enumeration<ViewConfiguration> enumeration = elements();
	boolean oneMissed = false;

	while (enumeration.hasMoreElements()) {
	    final ViewConfiguration openedViewConfiguration = enumeration.nextElement();
	    if (!openedViewConfiguration.isModified()) {
		continue;
	    }

	    try {
		ViewConfigurationBeanManager.getInstance().saveVC(openedViewConfiguration, false);
	    } catch (final Exception e) {
		openedViewConfiguration.setModified(true);
		openedViewConfiguration.setPath(null);
		oneMissed = true;

		logger.trace(ILogger.LEVEL_WARNING, e);
	    }
	}

	return oneMissed;
    }

    public ViewConfiguration getSelectedVC() {
	if (super.size() == 0) {
	    return null;
	}
	return firstElement();
    }
}
