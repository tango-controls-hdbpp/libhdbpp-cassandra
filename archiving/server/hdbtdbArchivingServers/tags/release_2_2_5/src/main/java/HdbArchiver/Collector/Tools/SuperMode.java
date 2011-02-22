//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/Tools/SuperMode.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SuperMode.
//						(Chinkumo Jean) - Apr 27, 2004
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SuperMode.java,v $
// Revision 1.4  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.3.16.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3.16.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.3.16.1  2005/09/26 08:01:20  chinkumo
// Minor changes !
//
// Revision 1.3  2005/02/04 17:10:12  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:15  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:25  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector.Tools;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;

public class SuperMode {
	private int format;
	private int type;
	private int writable;
	private Mode mode = null;

	public SuperMode(int format, int type, int writable, Mode mode) {
		this.format = format;
		this.type = type;
		this.writable = writable;
		this.mode = mode;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SuperMode))
			return false;

		final SuperMode superMode = (SuperMode) o;

		if (format != superMode.format)
			return false;
		if (type != superMode.type)
			return false;
		if (writable != superMode.writable)
			return false;
		if (!mode.equals(superMode.mode))
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = format;
		result = 29 * result + type;
		result = 29 * result + writable;
		result = 29 * result + mode.hashCode();
		return result;
	}
}
