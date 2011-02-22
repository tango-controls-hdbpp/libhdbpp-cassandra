//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/standard/ITangoManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ITangoManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ITangoManager.java,v $
// Revision 1.1  2006/09/20 12:47:45  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.5  2006/09/14 11:29:29  ounsy
// complete buffering refactoring
//
// Revision 1.4  2006/07/18 10:29:25  ounsy
// possibility to reload tango attributes
//
// Revision 1.3  2006/06/15 15:41:49  ounsy
// added a findArchivers method
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.datasources.tango.standard;

import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.tango.util.entity.data.Domain;

public interface ITangoManager
{
    // Operators.
    public static final String OP_EQUALS = "=";
    public static final String OP_GREATER_THAN = ">=";
    public static final String OP_LOWER_THAN = "<=";
    public static final String OP_GREATER_THAN_STRICT = ">";
    public static final String OP_LOWER_THAN_STRICT = "<";
    public static final String OP_CONTAINS = "Contains";
    public static final String OP_STARTS_WITH = "Starts with";
    public static final String OP_ENDS_WITH = "Ends with";

    public static final String FIELD_ATTRIBUTE_DOMAIN = "domain";
    public static final String FIELD_ATTRIBUTE_FAMILY = "family";
    public static final String FIELD_ATTRIBUTE_MEMBER = "member";
    public static final String FIELD_ATTRIBUTE_NAME = "att_name";

    /**
     * @param searchCriterions
     * @return 8 juil. 2005
     */
    public Vector<Domain> loadDomains ( Criterions searchCriterions, boolean forceReload );
    public Archiver [] findArchivers ( boolean historic ) throws ArchivingException;
}
