//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotAttributeWriteValue.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotAttributeWriteValue.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: SnapshotAttributeWriteValue.java,v $
// Revision 1.9  2007/08/24 14:22:34  ounsy
// "Can Set" not selected by default (Mantis bug 4826)
//
// Revision 1.8  2007/08/23 12:59:23  ounsy
// toUserFriendlyString() available in SnapshotAttributeValue
//
// Revision 1.7  2007/02/28 11:18:18  ounsy
// better snapshot file management
//
// Revision 1.6  2005/12/14 16:37:22  ounsy
// added methods necessary for direct clipboard edition
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * The write value of a snapshot attribute.
 *
 * @author CLAISSE
 */
public class SnapshotAttributeWriteValue extends SnapshotAttributeValue
{
    /**
     * The XML tag name used in saving/loading
     */
    public static final String XML_TAG = "WriteValue";

    private boolean settable;

    /**
     * Calls its mother's constructor.
     *
     * @param _dataFormat The data format (scalar/spectrum/image)
     * @param _dataType   The data type (double/string/...)
     * @param _value      The write value, not cast yet
     */
    public SnapshotAttributeWriteValue ( int _dataFormat , int _dataType , Object _value )
    {
        super( _dataFormat , _dataType , _value );
        this.settable = false;
    }

    /**
     * Returns a XML representation of the write value.
     *
     * @return a XML representation of the write value
     */
    public String toXMLString ()
    {
        String ret = "";

        XMLLine openingLine = new XMLLine( SnapshotAttributeWriteValue.XML_TAG , XMLLine.OPENING_TAG_CATEGORY );
        XMLLine closingLine = new XMLLine( SnapshotAttributeWriteValue.XML_TAG , XMLLine.CLOSING_TAG_CATEGORY );

        openingLine.setAttribute( SnapshotAttributeValue.NOT_APPLICABLE_XML_TAG, String.valueOf(this.isNotApplicable) );

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        ret += super.toString();
        ret += GUIUtilities.CRLF;

        ret += closingLine.toString();

        return ret;
    }

    /**
     * Sets the boolean value to know wheather this WriteValue
     * can be used to set equipment or not
     *
     * @param set The boolean value
     */
    public void setSettable ( boolean set )
    {
        this.settable = set;
    }

    /**
     * @return a boolean value that tells wheather this WriteValue
     *         can be used to set equipment or not
     */
    public boolean isSettable ()
    {
        return this.settable;
    }

}
