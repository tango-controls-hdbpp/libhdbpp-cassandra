// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/archiving/LimitedACStack.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class LimitedACStack.
// (Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: LimitedACStack.java,v $
// Revision 1.2 2006/05/16 09:36:11 ounsy
// minor changes
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
package fr.soleil.mambo.components.archiving;

import java.util.Enumeration;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.mambo.components.LimitedStack;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationData;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;

public class LimitedACStack extends LimitedStack<ArchivingConfiguration> {

    private static final long serialVersionUID = -1297587380856628547L;

    /**
     * @param _maxSize
     */
    public LimitedACStack(final int _maxSize) {
	super(_maxSize);
    }

    /**
     * 
     */
    public LimitedACStack() {
	super();
    }

    @Override
    public boolean removeElement(final Object item) {
	final boolean ret = super.removeElement(item);
	if (ret) {
	    return ret;
	}

	final ArchivingConfiguration vc = (ArchivingConfiguration) item;
	final Enumeration<ArchivingConfiguration> enumeration = elements();
	while (enumeration.hasMoreElements()) {
	    final ArchivingConfiguration next = enumeration.nextElement();
	    if (next.equals(vc)) {
		return super.removeElement(next);
	    }
	}
	return false;
    }

    @Override
    public String toString() {
	final Enumeration<ArchivingConfiguration> enumeration = super.elements();
	String ret = "";

	while (enumeration.hasMoreElements()) {
	    final ArchivingConfiguration vc = enumeration.nextElement();

	    ret += vc.toString();
	    ret += GUIUtilities.CRLF;
	}

	return ret;
    }

    /**
     * @return
     */
    public Object toString2() {
	final Enumeration<ArchivingConfiguration> enumeration = super.elements();
	String ret = "";

	while (enumeration.hasMoreElements()) {
	    final ArchivingConfiguration vc = enumeration.nextElement();
	    final ArchivingConfigurationData vcData = vc.getData();
	    ret += vcData.getName();
	    ret += "|";
	    ret += vcData.getPath();
	    ret += GUIUtilities.CRLF;
	}

	return ret;
    }

    /**
     * @param viewManager
     * @throws Exception
     */
    public boolean save(final IArchivingConfigurationManager viewManager, final ILogger logger) {
	final Enumeration<ArchivingConfiguration> enumeration = elements();
	boolean oneMissed = false;

	while (enumeration.hasMoreElements()) {
	    final ArchivingConfiguration openedArchivingConfiguration = enumeration.nextElement();
	    if (!openedArchivingConfiguration.isModified()) {
		continue;
	    }

	    try {
		openedArchivingConfiguration.save(viewManager, false);
	    } catch (final Exception e) {
		openedArchivingConfiguration.setModified(true);
		openedArchivingConfiguration.setPath(null);
		oneMissed = true;

		logger.trace(ILogger.LEVEL_WARNING, e);
	    }
	}

	return oneMissed;
    }

    public ArchivingConfiguration getSelectedAC() {
	if (super.size() == 0) {
	    return null;
	}
	return super.firstElement();
    }
}
