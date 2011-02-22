// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttribute.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingConfigurationAttribute.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.8 $
//
// $Log: ArchivingConfigurationAttribute.java,v $
// Revision 1.8 2007/02/01 14:16:29 pierrejoseph
// XmlHelper reorg
//
// Revision 1.7 2006/12/07 16:45:39 ounsy
// removed keeping period
//
// Revision 1.6 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.5 2006/10/19 12:39:58 ounsy
// added adeviceClass attribute
//
// Revision 1.4 2006/06/15 15:39:11 ounsy
// added support for dedicate archivers definition
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:56 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.data.archiving;

import javax.swing.tree.TreePath;

import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Attributes;

public class ArchivingConfigurationAttribute extends Attribute {

    private static final long                         serialVersionUID               = -7480602305338222563L;
    private ArchivingConfigurationAttributes          group;
    private ArchivingConfigurationAttributeProperties properties;

    public static final String                        XML_TAG                        = "attribute";
    public static final String                        COMPLETE_NAME_PROPERTY_XML_TAG = "completeName";

    private boolean                                   isSelected                     = true;
    private boolean                                   isNew                          = false;

    private String                                    deviceClass;

    public ArchivingConfigurationAttribute(
            ArchivingConfigurationAttributeProperties _properties,
            Attributes _contextAttributes) {
        super(_contextAttributes);

        this.properties = _properties;
        this.properties.setAttribute(this);
    }

    /**
     * @param _contextAttributes
     */
    public ArchivingConfigurationAttribute(Attributes _contextAttributes) {
        super(_contextAttributes);
        this.properties = new ArchivingConfigurationAttributeProperties();
        this.properties.setAttribute(this);
    }

    public ArchivingConfigurationAttribute() {
        super();
        this.properties = new ArchivingConfigurationAttributeProperties();
        this.properties.setAttribute(this);
    }

    /**
     * @param _attribute
     */
    public ArchivingConfigurationAttribute(Attribute _attribute) {
        super();
        this.properties = new ArchivingConfigurationAttributeProperties();
        this.setCompleteName(_attribute.getCompleteName());
        this.setDevice(_attribute.getDeviceName());
        this.setDeviceClass(_attribute.getDeviceClass());
        this.setDomain(_attribute.getDomainName());
        this.setFamily(_attribute.getFamilyName());
        this.setMember(_attribute.getMemberName());
        this.setName(_attribute.getName());
    }

    public String toString() {
        this.setNew(false);

        String ret = "";

        XMLLine openingLine = new XMLLine(
                ArchivingConfigurationAttribute.XML_TAG,
                XMLLine.OPENING_TAG_CATEGORY);
        openingLine.setAttribute(
                ArchivingConfigurationAttribute.COMPLETE_NAME_PROPERTY_XML_TAG,
                super.getCompleteName());
        XMLLine closingLine = new XMLLine(
                ArchivingConfigurationAttribute.XML_TAG,
                XMLLine.CLOSING_TAG_CATEGORY);

        ret += openingLine;
        ret += GUIUtilities.CRLF;

        if (properties != null) {
            ArchivingConfigurationAttributeHDBProperties HDBProperties = properties
                    .getHDBProperties();
            if (!HDBProperties.isEmpty()) {
                ret += HDBProperties.toString();
                ret += GUIUtilities.CRLF;
            }

            ArchivingConfigurationAttributeTDBProperties TDBProperties = properties
                    .getTDBProperties();
            if (!TDBProperties.isEmpty()) {
                ret += TDBProperties.toString();
                ret += GUIUtilities.CRLF;
            }
        }

        ret += closingLine;

        return ret;
    }

    public void loadFromCurrentArchivingState() throws Exception {
        boolean iah, iat;
        try {
            iah = ArchivingManagerFactory.getCurrentImpl().isArchived(
                    getCompleteName(), true);
        }
        catch (Exception e) {
            iah = false;
            e.printStackTrace();
        }
        try {
            iat = ArchivingManagerFactory.getCurrentImpl().isArchived(
                    getCompleteName(), false);
        }
        catch (Exception e) {
            iat = false;
            e.printStackTrace();
        }
        if (iah) {
            ArchivingConfigurationAttributeHDBProperties HDBProperties = ArchivingConfigurationAttributeHDBProperties
                    .loadHDBProperties(this.getCompleteName());
            if (HDBProperties != null) {
                this.properties.setHDBProperties(HDBProperties);
            }
        }
        if (iat) {
            ArchivingConfigurationAttributeTDBProperties TDBProperties = ArchivingConfigurationAttributeTDBProperties
                    .loadTDBProperties(this.getCompleteName());
            if (TDBProperties != null) {
                this.properties.setTDBProperties(TDBProperties);
            }
        }
    }

    /**
     * @return Returns the group.
     */
    public ArchivingConfigurationAttributes getGroup() {
        return group;
    }

    /**
     * @param group
     *            The group to set.
     */
    public void setGroup(ArchivingConfigurationAttributes group) {
        this.group = group;
    }

    /**
     * @return Returns the properties.
     */
    public ArchivingConfigurationAttributeProperties getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            The properties to set.
     */
    public void setProperties(
            ArchivingConfigurationAttributeProperties properties) {
        this.properties = properties;
    }

    /**
     * @param properties
     *            The properties to set.
     */
    public void addProperties(
            ArchivingConfigurationAttributeProperties properties) {
        ArchivingConfigurationAttributeHDBProperties HDBPropertiesToAdd = properties
                .getHDBProperties();
        ArchivingConfigurationAttributeTDBProperties TDBPropertiesToAdd = properties
                .getTDBProperties();

        if (!HDBPropertiesToAdd.isEmpty()) {
            ArchivingConfigurationMode[] HDBModesToAdd = HDBPropertiesToAdd
                    .getModes();
            ArchivingConfigurationAttributeHDBProperties HDBProperties = this
                    .getProperties().getHDBProperties();

            for (int i = 0; i < HDBModesToAdd.length; i++) {
                HDBProperties.addMode(HDBModesToAdd[i]);
            }

            this.properties.setHDBProperties(HDBProperties);
        }

        if (!TDBPropertiesToAdd.isEmpty()) {
            ArchivingConfigurationMode[] TDBModesToAdd = TDBPropertiesToAdd
                    .getModes();
            ArchivingConfigurationAttributeTDBProperties TDBProperties = this
                    .getProperties().getTDBProperties();

            for (int i = 0; i < TDBModesToAdd.length; i++) {
                TDBProperties.addMode(TDBModesToAdd[i]);
            }

            this.properties.setTDBProperties(TDBProperties);
        }

    }

    public void removeProperty(boolean hasMode, int type, boolean historic) {
        if (!hasMode) {
            ArchivingConfigurationAttributeHDBProperties HDBProperties = this.properties
                    .getHDBProperties();
            ArchivingConfigurationAttributeTDBProperties TDBProperties = this.properties
                    .getTDBProperties();

            if (historic) {
                HDBProperties.removeMode(type);
            }
            else {
                TDBProperties.removeMode(type);
            }
        }
    }

    /**
     * @param period
     *            26 juil. 2005
     */
    public void setHDBPeriod(int period) {
        ArchivingConfigurationAttributeHDBProperties HDBProperties = this
                .getProperties().getHDBProperties();
        HDBProperties.setDefaultPeriod(period);
    }

    /**
     * @return 26 juil. 2005
     */
    public boolean isEmpty() {
        ArchivingConfigurationAttributeHDBProperties HDBProperties = properties
                .getHDBProperties();
        ArchivingConfigurationAttributeTDBProperties TDBProperties = properties
                .getTDBProperties();

        boolean ret = HDBProperties.isEmpty() && TDBProperties.isEmpty();
        return ret;
    }

    /**
     * @param period
     *            26 juil. 2005
     */
    public void setTDBPeriod(int period) {
        ArchivingConfigurationAttributeTDBProperties TDBProperties = this
                .getProperties().getTDBProperties();
        TDBProperties.setDefaultPeriod(period);

    }

    /**
     * @deprecated
     */
    public void setKeepingPeriod(long keepingPeriod) {
        /*
         * ArchivingConfigurationAttributeTDBProperties TDBProperties =
         * this.getProperties().getTDBProperties();
         * TDBProperties.setKeepingPeriod( keepingPeriod );
         */

    }

    /**
     * @param exportPeriod
     *            26 juil. 2005
     */
    public void setExportPeriod(long exportPeriod) {
        ArchivingConfigurationAttributeTDBProperties TDBProperties = this
                .getProperties().getTDBProperties();
        TDBProperties.setExportPeriod(exportPeriod);

    }

    /**
     * @param i
     * @return 9 sept. 2005
     */
    public void controlValues() throws ArchivingConfigurationException {
        if (this.properties == null) {
            return;
        }
        ArchivingConfigurationAttributeHDBProperties HDBProperties = this.properties
                .getHDBProperties();
        ArchivingConfigurationAttributeTDBProperties TDBProperties = this.properties
                .getTDBProperties();

        try {
            if (HDBProperties != null) {
                HDBProperties.controlValues();
            }
            if (TDBProperties != null) {
                TDBProperties.controlValues();
            }
        }
        catch (ArchivingConfigurationException ace) {
            ace.setAttributeName(this.getCompleteName());
            throw ace;
        }

    }

    /**
     * @return
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isSelected
     *            The isSelected to set.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return
     */
    public TreePath getTreePath() {

        String[] pathRef = new String[5];
        pathRef[0] = "";
        pathRef[1] = this.getDomainName();
        pathRef[2] = this.getFamilyName();
        pathRef[3] = this.getMemberName();
        pathRef[4] = this.getName();

        TreePath aPath = new TreePath(pathRef);
        return aPath;
    }

    /**
     * 
     */
    public void reverseSelection() {
        this.isSelected = !this.isSelected;
    }

    /**
     * @return Returns the isNew.
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * @param isNew
     *            The isNew to set.
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setDedicatedArchiver(boolean _historic,
            String _dedicatedArchiver) {
        ArchivingConfigurationAttributeDBProperties properties;
        if (_historic) {
            properties = this.getProperties().getHDBProperties();
        }
        else {
            properties = this.getProperties().getTDBProperties();
        }

        properties.setDedicatedArchiver(_dedicatedArchiver);
    }

    public String getDeviceClass() {
        return this.deviceClass;
    }

    /**
     * @param deviceClass
     *            The deviceClass to set.
     */
    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }
}
