/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.ApiUtil;
import fr.esrf.TangoApi.AttributeInfo;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagerFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingMessConfig;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeHeavy;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeSupport;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributesArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.FaultingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.LoadBalancedList;

/**
 * @author AYADI
 */
public abstract class ArchivingManagerApiRef implements IArchivingManagerApiRef {

    // ============================================================
    // Members
    protected int               type;
    // Command name
    private static final String m_ARCHIVINGSTART      = "TriggerArchiveConf";
    private static final String m_ARCHIVINGSTOP       = "StopArchiveAtt";
    private static final String m_ARCHIVINGSTOPGROUP  = "StopArchiveConf";
    // Attribute Name
    private static final String m_SCALARCHARGE        = "scalar_charge";
    private static final String m_SPECTRUMCHARGE      = "spectrum_charge";
    private static final String m_IMAGECHARGE         = "image_charge";

    protected DataBaseManager   m_DataBase            = null;

    private boolean             is_connected          = false;

    private static Map          attributeToDeviceInfo = new Hashtable();

    /**
     * @return the facility property for that type of archiving.
     */
    protected abstract void setArchivingException() throws ArchivingException;

    protected abstract String getHost();

    protected abstract String getUser();

    protected abstract String getPassword();

    protected abstract String getSchema();

    protected abstract String geDevicePattern();

    public abstract String getClassDevice();

    protected abstract String getDB();

    abstract public boolean isM_Facility();

    abstract public void setM_Facility(boolean facility);

    abstract public String[] getM_ExportedArchiverList();

    abstract public void setM_ExportedArchiverList(String[] exportedArchiverList);

    abstract public String[] getM_NotExportedArchiverList();

    abstract public void setM_NotExportedArchiverList(
            String[] notExportedArchiverList);

    /*
	 *
	 */
    public ArchivingManagerApiRef() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef
     * #getDbHost()
     */
    public String getDbHost() throws ArchivingException {
        if (m_DataBase != null)
            return m_DataBase.getDbConn().getHost();
        return "";
    }

    /**
     * @return the name of the database
     * @throws ArchivingException
     */
    public String getDbName() throws ArchivingException {
        if (m_DataBase != null)
            return m_DataBase.getDbConn().getName();
        return "";
    }

    /**
     * @return the user of the database
     * @throws ArchivingException
     * @throws ArchivingException
     */
    public String getDbUser() throws ArchivingException {
        if (m_DataBase != null)
            return m_DataBase.getDbConn().getUser();
        return "";
    }

    /**
     * @return the password of the database
     * @throws ArchivingException
     */
    public String getDbPassword() throws ArchivingException {
        if (m_DataBase != null)
            return m_DataBase.getDbConn().getPasswd();
        return "";
    }

    /**
     * @return the database type SQL, ORACLE...
     * @throws ArchivingException
     */
    public int getDbType() throws ArchivingException {
        if (m_DataBase != null)
            return m_DataBase.getDbConn().getDbType();
        return -1;
    }

    public boolean getFacility() {
        // TODO Auto-generated method stub
        boolean facility = false;
        try {
            facility = GetConf.getFacility(getClassDevice());
        } catch (ArchivingException e) {
            System.err.println(e.toString());
        }
        return facility;

    }

    // ============================================================
    // Methodes

    /**
     * initialize hdbManagerApi
     */
    public void Init() {
        closeDatabase();
    }

    /**
     * Get the status of the archiver in charge of the given attribute
     * 
     * @param attributeName
     * @param historic
     *            true if historical archiving, false otherwise.
     * @return the status of the Archiver driver
     * @throws ArchivingException
     */
    public String getStatus(String attributeName, boolean historic)
            throws ArchivingException {

        try {
            String archiverName = "";
            if (m_DataBase != null) {
                archiverName = m_DataBase.getMode().getDeviceInCharge(
                        attributeName);
            }

            DeviceProxy archiverProxy = new DeviceProxy(archiverName);
            return archiverProxy.status();
        } catch (DevFailed devFailed) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                    + GlobalConst.ARC_UNREACH_EXCEPTION;
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing ArchivingManagerApi.getStatus() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN,
                    desc, "", devFailed);

        }
    }

    /*
	 *
	 */
    private void initDbArchiverList() throws ArchivingException {
        try {
            String m_dbDevicePattern = geDevicePattern();
            String m_dbClassDevice = getClassDevice();// isHistoric ?
            // m_hdbClassDevice :
            // m_tdbClassDevice;

            boolean facility = isM_Facility();
            String[] m_dbExportedArchiverList = getM_ExportedArchiverList();
            // String [] m_dbNotExportedArchiverList = isHistoric ?
            // m_hdbNotExportedArchiverList : m_tdbNotExportedArchiverList;
            String[] m_dbNotExportedArchiverList;
            String db = getDB();

            Vector<String> runningDevicesListVector = new Vector<String>();
            Vector<String> notRunningDevicesListVector = new Vector<String>();

            Database dbase = ApiUtil.get_db_obj();

            // String[] devicesList = dbase.getDevices ( m_dbDevice );
            String[] devicesList = dbase
                    .get_device_exported_for_class(m_dbClassDevice);

            for (int i = 0; i < devicesList.length; i++) {
                if (TangoAccess.deviceLivingTest(devicesList[i])) {
                    runningDevicesListVector.add(((facility) ? "//"
                            + dbase.get_tango_host() + "/" : "")
                            + devicesList[i]);
                } else {
                    notRunningDevicesListVector.add(((facility) ? "//"
                            + dbase.get_tango_host() + "/" : "")
                            + devicesList[i]);
                }
            }

            m_dbExportedArchiverList = new String[runningDevicesListVector
                    .size()];
            m_dbNotExportedArchiverList = new String[notRunningDevicesListVector
                    .size()];

            for (int i = 0; i < runningDevicesListVector.size(); i++) {
                m_dbExportedArchiverList[i] = (String) runningDevicesListVector
                        .elementAt(i);
                System.out.println(db + "Archiver (EXPORTED) "
                        + m_dbExportedArchiverList[i]);
            }
            for (int i = 0; i < notRunningDevicesListVector.size(); i++) {
                m_dbNotExportedArchiverList[i] = (String) notRunningDevicesListVector
                        .elementAt(i);
                System.out.println(db + "Archiver (NOT EXPORTED) "
                        + m_dbNotExportedArchiverList[i]);
            }
            setM_NotExportedArchiverList(m_dbNotExportedArchiverList);
            setM_ExportedArchiverList(m_dbExportedArchiverList);

        } catch (DevFailed devFailed) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                    + GlobalConst.DBT_UNREACH_EXCEPTION;
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing ArchivingManagerApi.initDbArchiverList method...";
            throw new ArchivingException(message, reason, ErrSeverity.PANIC,
                    desc, "", devFailed);
        }
    }

    /**
     * @return true if the connection to hdb exists
     */
    public boolean is_db_connected() {
        return is_connected;
    }

    /**
     * Start the archiving for the given attribute list and for the given
     * archiving type
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param archivingMessConfig
     *            An ArchivingMessConfig object (a list of AttributeLightMode
     *            object)
     * @throws ArchivingException
     */
    public void ArchivingStart(ArchivingMessConfig archivingMessConfig)
            throws ArchivingException {
        // long beforeInitArchiverList = System.currentTimeMillis ();
        AttributesArchivingException archivingStartException = new AttributesArchivingException();
        boolean needsThrow = false;

        initDbArchiverList();
        if (m_DataBase == null)
            setArchivingException();
        Vector goodAttributeList = new Vector();
        // The badAttributesList will be used to add a FaultingAttribute for
        // each attribute that causes an ArchivingException
        Vector badAttributeList = new Vector();
        // Check the support type, format and writable...
        Enumeration attributeNameList = archivingMessConfig
                .getAttributeListKeys();
        while (attributeNameList.hasMoreElements()) {
            String attribute = (String) attributeNameList.nextElement();
            try {
                if (checkAttributeSupport(attribute)) {
                    goodAttributeList.add(attribute);
                } else {
                    String message = GlobalConst.ARCHIVING_ERROR_PREFIX
                            + " : "
                            + "Attribute format, type or writable not supported !";
                    String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
                    String desc = "The archiving of the attribute named '"
                            + attribute + "'" + "was not launched ! : \r\n";
                    ArchivingException archivingException = new ArchivingException(
                            message, reason, ErrSeverity.WARN, desc, "");
                    archivingStartException.addStack(message, reason,
                            ErrSeverity.WARN, desc, "", archivingException);
                    badAttributeList.add(attribute);
                    // We add this attribute to the exception that is being
                    // forged
                    archivingStartException
                            .getFaultingAttributes()
                            .add(
                                    new FaultingAttribute(attribute,
                                            "Attribute format, type or writable not supported !"));
                }
            } catch (ArchivingException e) {
                e.printStackTrace();

                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
                String desc = "The archiving of the attribute named '"
                        + attribute + "'" + "was not launched ! : \r\n";
                archivingStartException.addStack(message, reason,
                        ErrSeverity.WARN, desc, "", e);
                badAttributeList.add(attribute);
                // We add this attribute to the exception that is being
                // forged
                archivingStartException
                        .getFaultingAttributes()
                        .add(
                                new FaultingAttribute(attribute,
                                        "Unreachable attribute. Check your connectivity"));

                needsThrow = true;
            } catch (Exception e) {
                e.printStackTrace();

                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
                String desc = "The archiving of the attribute named '"
                        + attribute + "'" + "was not launched ! : \r\n";

                ArchivingException archivingException = new ArchivingException(
                        message, reason, ErrSeverity.WARN, desc, "");
                archivingStartException.addStack(message, reason,
                        ErrSeverity.WARN, desc, "", archivingException);
                badAttributeList.add(attribute);
                // We add this attribute to the exception that is being
                // forged.
                archivingStartException.getFaultingAttributes().add(
                        new FaultingAttribute(attribute, "Unknown"));

                needsThrow = true;
            }
        }

        if (goodAttributeList.size() == 0) {
            String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method, no supported attributes";
            ArchivingException archivingException;
            // Whether there are bad attributes or not, we throw an
            // AttributeArchivingException, or a simple ArchivingException
            if (badAttributeList.size() > 0) {
                archivingException = new AttributesArchivingException(reason,
                        reason, ErrSeverity.ERR, reason, "ArchivingManagerApi");
                ((AttributesArchivingException) archivingException)
                        .getFaultingAttributes()
                        .addAll(archivingStartException.getFaultingAttributes());
            } else {
                archivingException = new ArchivingException(reason, reason,
                        ErrSeverity.ERR, reason, "ArchivingManagerApi");
            }
            throw archivingException;
        }

        // Build the new list in case of bad attribute(s)
        String[] finalAttributeList = new String[goodAttributeList.size()];
        for (int i = 0; i < goodAttributeList.size(); i++) {
            finalAttributeList[i] = (String) goodAttributeList.elementAt(i);
        }

        // if the creation table doest not work the archiving is not launched
        finalAttributeList = checkRegistration(finalAttributeList);
        LoadBalancedList loadBalancedList = selectArchiversTask(
                archivingMessConfig.isGrouped(), archivingMessConfig
                        .getAttributeToDedicatedArchiverHasthable(),
                finalAttributeList);
        // Sending order to archivers
        Enumeration my_archivers = loadBalancedList.getArchivers();
        boolean initCauseAlreadyDoneInException = false;

        while (my_archivers.hasMoreElements()) {
            String my_archiver = (String) my_archivers.nextElement();
            String[] assignedAttributes = loadBalancedList
                    .getArchiverAssignedAtt(my_archiver);

            if (assignedAttributes.length > 0) {
                ArchivingMessConfig archiver_archivingMessConfig = ArchivingMessConfig
                        .basicObjectCreation();

                for (int i = 0; i < assignedAttributes.length; i++) {
                    archiver_archivingMessConfig.add(archivingMessConfig
                            .getAttribute(assignedAttributes[i]));
                }

                DeviceProxy archiverProxy = null;
                try {
                    // Set the data_type, format and writable characteristics of
                    // each attributs
                    setAttributeInfoInArchivingMess(archiver_archivingMessConfig);
                    // String[] archivingMessStartArray =
                    // getArchivingMessStartArray(archiver_archivingMessConfig);
                    DeviceData device_data = new DeviceData();
                    String[] messConfigArray = archiver_archivingMessConfig
                            .toArray();
                    device_data.insert(messConfigArray);

                    // archiverProxy.set_timeout_millis(5000);
                    archiverProxy = new DeviceProxy(my_archiver);
                    archiverProxy.command_inout(m_ARCHIVINGSTART, device_data);
                } catch (DevFailed devFailed) {
                    devFailed.printStackTrace();

                    String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                    String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
                    String desc = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.ARC_UNREACH_EXCEPTION;
                    desc += "(Archiver: " + my_archiver + ")";
                    ArchivingException archivingException = new ArchivingException(
                            message, reason, ErrSeverity.PANIC, desc,
                            "ArchivingManagerApi");
                    if (!initCauseAlreadyDoneInException) {
                        try {
                            archivingStartException.initCause(devFailed);
                            initCauseAlreadyDoneInException = true;
                        } catch (IllegalStateException e) {
                            System.out.println("CLA/Failed during initCause!");
                            e.printStackTrace();
                            // do nothing
                        }
                    }
                    boolean computeIsDueToATimeOut = archivingStartException
                            .computeIsDueToATimeOut();
                    if (computeIsDueToATimeOut) {
                        desc += "\n--Time out";
                    }
                    archivingStartException.addStack(message, reason,
                            ErrSeverity.WARN, desc, "", archivingException);

                    needsThrow = true;
                } catch (Exception e) {
                    String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                    String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
                    String desc = GlobalConst.ARCHIVING_ERROR_PREFIX
                            + " : Unexpected exception";
                    desc += "(Archiver: " + my_archiver + ")";
                    ArchivingException archivingException = new ArchivingException(
                            message, reason, ErrSeverity.PANIC, desc,
                            "ArchivingManagerApi");
                    archivingStartException.addStack(message, reason,
                            ErrSeverity.WARN, desc, "", archivingException);
                    needsThrow = true;
                }
            }
        }
        throwArchivingStartException(archivingStartException, needsThrow,
                badAttributeList.size());
    }

    /**
     * This method checks the registration and create the table of the
     * attributes in the database
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param attributeNameList
     * @return true if the registration works
     */
    private String[] checkRegistration(String[] attributeNameList)
            throws ArchivingException {
        String tangoHost = TangoAccess.getTangoHost();
        Vector myNewListV = new Vector();
        for (int i = 0; i < attributeNameList.length; i++) {
            String attributeName = attributeNameList[i];
            try {
                AttributeInfo attributeInfo = getAttributeInfo(attributeName,
                        false);
                int index = attributeName.lastIndexOf("/");
                String deviceName = attributeName.substring(0, index);
                String[] attributeSplitName = split_att_name_3_fields(attributeName);
                java.sql.Timestamp time = new java.sql.Timestamp(
                        new java.util.Date().getTime());
                AttributeHeavy attributeHeavy = new AttributeHeavy(
                        attributeName);
                attributeHeavy.setAttribute_complete_name(attributeName); // ****************
                // The
                // whole
                // attribute
                // name
                // (device_name
                // +
                // attribute_name)
                attributeHeavy.setAttribute_device_name(deviceName);
                attributeHeavy.setRegistration_time(time); // ****************
                // Attribute
                // registration
                // timestamp
                attributeHeavy.setDomain(attributeSplitName[1]); // ****************
                // domain to
                // which the
                // attribute is
                // associated
                attributeHeavy.setFamily(attributeSplitName[2]); // ****************
                // family to
                // which the
                // attribute is
                // associated
                attributeHeavy.setMember(attributeSplitName[3]); // ****************
                // member to
                // which the
                // attribute is
                // associated
                attributeHeavy.setAttribute_name(attributeSplitName[4]); // ****************
                // attribute
                // name
                attributeHeavy.setData_type(attributeInfo.data_type); // ****************
                // Attribute
                // data
                // type
                attributeHeavy
                        .setData_format(attributeInfo.data_format.value()); // ****************
                // Attribute
                // data
                // format
                attributeHeavy.setWritable(attributeInfo.writable.value()); // ****************
                // Attribute
                // read/write
                // type
                attributeHeavy.setMax_dim_x(attributeInfo.max_dim_x); // ****************
                // Attribute
                // Maximum
                // X
                // dimension
                attributeHeavy.setMax_dim_y(attributeInfo.max_dim_y); // ****************
                // Attribute
                // Maximum
                // Y
                // dimension
                attributeHeavy.setLevel(attributeInfo.level.value()); // ****************
                // Attribute
                // display
                // level
                attributeHeavy.setCtrl_sys(tangoHost); // ****************
                // Control system to
                // which the attribute
                // belongs
                attributeHeavy.setArchivable(0); // **************** archivable
                // (Property that precises
                // whether the attribute is
                // "on-run-only" archivable, or
                // if it is "always" archivable
                attributeHeavy.setSubstitute(0); // **************** substitute
                attributeHeavy.setDescription(attributeInfo.description);
                attributeHeavy.setLabel(attributeInfo.label);
                attributeHeavy.setUnit(attributeInfo.unit);
                attributeHeavy.setStandard_unit(attributeInfo.standard_unit);
                attributeHeavy.setDisplay_unit(attributeInfo.display_unit);
                attributeHeavy.setFormat(attributeInfo.format);
                attributeHeavy.setMin_value(attributeInfo.min_value);
                attributeHeavy.setMax_value(attributeInfo.max_value);
                attributeHeavy.setMin_alarm(attributeInfo.min_alarm);
                attributeHeavy.setMax_alarm(attributeInfo.max_alarm);

                if (!isRegistred(attributeName)) {

                    m_DataBase.getAttribute().registerAttribute(attributeHeavy);
                }
                // m_DataBase.getAttribute().CreateAttributeTableIfNotExist(attributeName,
                // m_DataBase.getExtractor().getDataGetters(),
                // m_DataBase.getDbUtil());
                myNewListV.add(attributeName);
                m_DataBase.getAttribute().CreateAttributeTableIfNotExist(
                        attributeName, attributeHeavy);
            } catch (DevFailed devFailed) {
                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.DEV_UNREACH_EXCEPTION + " ("
                        + attributeName + ")";
                String reason = GlobalConst.TANGO_COMM_EXCEPTION;
                String desc = "Failed while executing ArchivingManagerApi.checkRegistration() method...";
                throw new ArchivingException(message, reason, ErrSeverity.WARN,
                        desc, "", devFailed);
            } catch (ArchivingException e) {
                throw e;
            }
        }

        return toStringArray(myNewListV);
    }

    /**
     * @param archivingStartException
     * @param needsThrow
     * @param badAttributeListSize
     * @throws ArchivingException
     */
    private static void throwArchivingStartException(
            ArchivingException archivingStartException, boolean needsThrow,
            int badAttributeListSize) throws ArchivingException {
        if (needsThrow) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
            String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";

            if (badAttributeListSize != -1) {
                String desc = "Impossible to launch the archiving of "
                        + badAttributeListSize + " attributes ! : \r\n";
                ArchivingException aef = new ArchivingException(message,
                        reason, ErrSeverity.PANIC, desc, "ArchivingManagerApi");
                archivingStartException.addStack(message, reason,
                        ErrSeverity.PANIC, desc, "ArchivingManagerApi", aef);
            }

            throw archivingStartException;
        }
    }

    private static void throwArchivingStopException(
            ArchivingException archivingStopException, boolean needsThrow,
            int badAttributeListSize) throws ArchivingException {
        if (needsThrow) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
            String reason = "Failed while executing ArchivingManagerApi.ArchivingStop() method...";

            if (badAttributeListSize != -1) {
                String desc = "Impossible to stop the archiving of "
                        + badAttributeListSize + " attributes ! : \r\n";
                ArchivingException aef = new ArchivingException(message,
                        reason, ErrSeverity.PANIC, desc, "ArchivingManagerApi");
                archivingStopException.addStack(message, reason,
                        ErrSeverity.PANIC, desc, "ArchivingManagerApi", aef);
            }

            throw archivingStopException;
        }
    }

    /**
     * Stop the archiving for the given attribute list and for the given
     * archiving type
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param attributeNameList
     *            the list of attributes (The archiving of each element of this
     *            list is about to be stopped)
     * @throws ArchivingException
     */
    public void ArchivingStop(String[] attributeNameList)
            throws ArchivingException {
        boolean needsThrow = false;
        int badAttributes = 0;

        if (!IsArchivedStop(attributeNameList))
            throw new ArchivingException(
                    GlobalConst.ALREADY_ARCHIVINGSTOP,
                    GlobalConst.ALREADY_ARCHIVINGSTOP,
                    ErrSeverity.WARN,
                    "Can't stopped archived because at least one attribute was not being archived",
                    this.getClass().getName());
        if (m_DataBase == null)
            throw new ArchivingException(
                    GlobalConst.UNCONNECTECTED_ADB,
                    GlobalConst.UNCONNECTECTED_ADB,
                    ErrSeverity.WARN,
                    "Can't stopped archiving for the given attribute list and for given archiving type because the archiving database is not connected",
                    this.getClass().getName());

        String attributeName = "";
        String archiverName = "";

        ArchivingException archivingStopException = new ArchivingException();
        boolean initCauseAlreadyDoneInException = false;

        for (int i = 0; i < attributeNameList.length; i++) {
            attributeName = attributeNameList[i];

            try {
                // get the Archiver that take in charge the archiving
                archiverName = m_DataBase.getMode().getDeviceInCharge(
                        attributeName);
                if (archiverName == ""
                        || !TangoAccess.deviceLivingTest(archiverName))
                    throw new ArchivingException(
                            GlobalConst.ARC_UNREACH_EXCEPTION,
                            GlobalConst.ARC_UNREACH_EXCEPTION,
                            ErrSeverity.WARN,
                            "Archiver name is empty or the given device is not alive",
                            this.getClass().getName());

                if (isArchived(attributeName)) {
                    Mode mode = GetArchivingMode(attributeName);
                    AttributeLightMode attributeLightMode = new AttributeLightMode(
                            attributeName);
                    attributeLightMode.setDevice_in_charge(archiverName);

                    /*
                     * AttributeInfo attributeInfo =
                     * getAttributeInfoFromDatabase(attributeName);
                     * attributeLightMode.setData_type(attributeInfo.data_type);
                     * attributeLightMode
                     * .setData_format(attributeInfo.data_format.value());
                     * attributeLightMode
                     * .setWritable(attributeInfo.writable.value());
                     */
                    int[] tfw = m_DataBase.getAttribute().getAtt_TFW_Data(
                            attributeName);
                    attributeLightMode.setData_type(tfw[0]);
                    attributeLightMode.setData_format(tfw[1]);
                    attributeLightMode.setWritable(tfw[2]);
                    attributeLightMode.setMode(mode);

                    DeviceProxy archiverProxy = new DeviceProxy(archiverName);
                    String[] attributeLightModeArray = attributeLightMode
                            .toArray();

                    DeviceData device_data = new DeviceData();
                    device_data.insert(attributeLightModeArray);
                    archiverProxy.command_inout(m_ARCHIVINGSTOP, device_data);
                }
            } catch (DevFailed devFailed) {
                devFailed.printStackTrace();

                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing ArchivingManagerApi.ArchivingStop() method...";
                String desc = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ARC_UNREACH_EXCEPTION;
                desc += "(Archiver: " + archiverName + ")";
                ArchivingException archivingException = new ArchivingException(
                        message, reason, ErrSeverity.PANIC, desc,
                        "ArchivingManagerApi");

                if (!initCauseAlreadyDoneInException) {
                    try {
                        archivingStopException.initCause(devFailed);
                        initCauseAlreadyDoneInException = true;
                    } catch (IllegalStateException e) {
                        System.out.println("CLA/Failed during initCause!");
                        e.printStackTrace();
                        // do nothing
                    }
                }

                if (archivingStopException.computeIsDueToATimeOut()) {
                    desc += "\n--Time out";
                }

                archivingStopException.addStack(message, reason,
                        ErrSeverity.WARN, desc, "", archivingException);
                needsThrow = true;
                badAttributes++;
            } catch (Exception e) {
                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing ArchivingManagerApi.ArchivingStop() method...";
                String desc = GlobalConst.ARCHIVING_ERROR_PREFIX
                        + " : Unexpected exception";
                desc += "(Archiver: " + archiverName + ")";
                ArchivingException archivingException = new ArchivingException(
                        message, reason, ErrSeverity.PANIC, desc,
                        "ArchivingManagerApi");
                archivingStopException.addStack(message, reason,
                        ErrSeverity.WARN, desc, "", archivingException);
                needsThrow = true;
                badAttributes++;
            }
        }

        throwArchivingStopException(archivingStopException, needsThrow,
                badAttributes);
    }

    // !!!!!!!!!!!!!! RG: Why are there 2 methods: ArchivingStop and
    // ArchivingStopConf ? It is very confusing !!!!!!
    /**
     * Sstop the archiving in the intermediate database
     * 
     * @param attributeNameList
     *            the list of attributes (The archiving of each element of this
     *            list is about to be stopped)
     * @param historic
     * @throws ArchivingException
     */
    public void ArchivingStopConf(String[] attributeNameList)
            throws ArchivingException {
        Hashtable myStoppingConf = new Hashtable();
        String attributeName = "";
        String archiverName = "";

        if (!IsArchivedStop(attributeNameList))
            throw new ArchivingException(
                    GlobalConst.ALREADY_ARCHIVINGSTOP,
                    GlobalConst.ALREADY_ARCHIVINGSTOP,
                    ErrSeverity.WARN,
                    "Can't stopped archived because at least attribute was not being archived",
                    this.getClass().getName());

        try {
            for (int i = 0; i < attributeNameList.length; i++) {
                attributeName = attributeNameList[i];
                // get the Archiver that take in charge the archiving
                archiverName = m_DataBase.getMode().getDeviceInCharge(
                        attributeName);

                if (!archiverName.equals("")) {
                    if (myStoppingConf.containsKey(archiverName)
                            || !TangoAccess.deviceLivingTest(archiverName)) {
                    }
                    // return ArchivingManagerResult.INEXISTANT_ARCHIVER;
                    if (isArchived(attributeName)) {
                        Mode mode = GetArchivingMode(attributeName);
                        AttributeLightMode attributeLightMode = new AttributeLightMode(
                                attributeName);
                        attributeLightMode.setDevice_in_charge(archiverName);

                        /*
                         * AttributeInfo attributeInfo =
                         * getAttributeInfoFromDatabase(attributeName);
                         * attributeLightMode
                         * .setData_type(attributeInfo.data_type);
                         * attributeLightMode
                         * .setData_format(attributeInfo.data_format.value());
                         * attributeLightMode
                         * .setWritable(attributeInfo.writable.value());
                         */
                        int[] tfw = m_DataBase.getAttribute().getAtt_TFW_Data(
                                attributeName);
                        attributeLightMode.setData_type(tfw[0]);
                        attributeLightMode.setData_format(tfw[1]);
                        attributeLightMode.setWritable(tfw[2]);

                        attributeLightMode.setMode(mode);
                        if (!myStoppingConf.containsKey(archiverName))
                            myStoppingConf.put(archiverName, new Vector());

                        ((Vector) myStoppingConf.get(archiverName))
                                .add(attributeLightMode);
                    }
                }
            }

            for (Enumeration e = myStoppingConf.keys(); e.hasMoreElements();) {
                archiverName = (String) e.nextElement();
                DeviceProxy archiverProxy = new DeviceProxy(archiverName);
                DeviceData device_data = new DeviceData();
                String[] archiverStoppingConf = toStoppingArrayAttLightMode((Vector) myStoppingConf
                        .get(archiverName));
                device_data.insert(archiverStoppingConf);
                archiverProxy.command_inout(m_ARCHIVINGSTOPGROUP, device_data);
            }

        } catch (DevFailed devFailed) {
            String message = GlobalConst.ERROR_ARCHIVINGSTOP + " : "
                    + "Problems talking to the archiver " + archiverName
                    + " for the attribute " + attributeName;
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing ArchivingManagerApi.ArchivingStopConf() method...";

            ArchivingException toBeThrown = new ArchivingException(message,
                    reason, ErrSeverity.WARN, desc, "", devFailed);
            toBeThrown.initCause(devFailed);
            // boolean computeIsDueToATimeOut =
            toBeThrown.computeIsDueToATimeOut();
            throw toBeThrown;
        }
    }

    /**
     * get the archiving mode of an attribute.
     * 
     * @param attributeName
     *            the complete name of the attribute
     * @param historic
     *            true if historical archiving, false otherwise.
     * @return the mode
     */
    public Mode GetArchivingMode(String attributeName)
            throws ArchivingException {
        if (isRegistred(attributeName)) {
            return m_DataBase.getMode().getCurrentArchivingMode(attributeName);
        }
        StringBuffer buffer = new StringBuffer("No achiving mode for '");
        buffer.append(attributeName).append("'");
        throw new ArchivingException(buffer.toString(), buffer.toString(),
                ErrSeverity.WARN,
                "Failed while executing ArchivingManagerApiRef.getArchivingMode(), the \""
                        + attributeName
                        + "\" attribute didn't have an archiving mode", this
                        .getClass().getName());
    }

    /**
     * return the attribute registred status
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param attributeName
     *            the complete name of the attribute
     * @return true if the attribute has been archived
     */
    protected boolean isRegistred(String attributeName)
            throws ArchivingException {
        if (m_DataBase != null) {
            return m_DataBase.getAttribute().isRegisteredADT(attributeName);
        }
        return false;
    }

    /**
     * return the attribute archiving status
     * 
     * @param attributeName
     *            the complete name of the attribute
     * @param historic
     *            true if historical archiving, false otherwise.
     * @return true if the attribute is being archived
     * @throws ArchivingException
     */
    public boolean isArchived(String attributeName) throws ArchivingException {
        if ((m_DataBase == null) || (m_DataBase.getMode() == null))
            return false;
        else
            return m_DataBase.getMode().isArchived(attributeName);
    }

    /**
     * Use for ArchivingStop method, to know if all the list is already in
     * archiving
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param attributeNameList
     * @return true if at least one attribute of the list is not in progress
     *         filing
     * @throws ArchivingException
     */
    private boolean IsArchivedStop(String[] attributeNameList)
            throws ArchivingException {
        for (int i = 0; i < attributeNameList.length; i++) {
            if (isArchived(attributeNameList[i]))
                return true;
        }
        return false;
    }

    /**
     * generate the ArchivingStart array command used in the Archiver driver
     * 
     * @param archivingMessConfig
     * @return the array that correspond to the archivingMessConfig object.
     */
    // private static String[] getArchivingMessStartArray(ArchivingMessConfig
    // archivingMessConfig) throws ArchivingException
    private static void setAttributeInfoInArchivingMess(
            ArchivingMessConfig archivingMessConfig) throws ArchivingException {
        Enumeration m_attributs = archivingMessConfig.getAttributeListKeys();

        while (m_attributs.hasMoreElements()) {
            String m_att = (String) m_attributs.nextElement();
            AttributeInfo attributeInfo = null;
            try {
                attributeInfo = getAttributeInfo(m_att, false);
                archivingMessConfig.getAttribute(m_att).setData_type(
                        attributeInfo.data_type);
                archivingMessConfig.getAttribute(m_att).setData_format(
                        attributeInfo.data_format.value());
                archivingMessConfig.getAttribute(m_att).setWritable(
                        attributeInfo.writable.value());

            } catch (DevFailed devFailed) {
                String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : ";
                String reason = "Failed while executing ArchivingManagerApi.getArchivingStartArray() method...";
                String desc = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ATT_UNREACH_EXCEPTION + " [" + m_att
                        + "]";
                throw new ArchivingException(message, reason,
                        ErrSeverity.PANIC, desc, "ArchivingManagerApi");
            }
        }
        // return archivingMessConfig.toArray();
    }

    /**
     * Distribute attributes on archivers the archiver that will take in charge
     * the archiving
     * 
     * @param grouped
     *            true if all attributes are associated to the same archiver
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param attributeNameList
     *            the attributes to distribute on archivers
     * @return An hashtable with archivers name as keys, and attributes list as
     *         values
     * @throws ArchivingException
     */
    private LoadBalancedList selectArchiversTask(boolean grouped,
            Hashtable attributeToChosenArchiverHasthable,
            String[] attributeNameList) throws ArchivingException {
        Hashtable attributeToDedicatedArchiver = getAttributesToDedicatedArchiver(false);// CLA
        // 10/11/06

        LoadBalancedList loadBalancedList = new LoadBalancedList(
                DbUtils.getHdbTdbType(m_DataBase.getDbConn().getDbType()) == ConfigConst.HDB);
        String[] exportedArchiversList = getM_ExportedArchiverList();
        initArchivers(loadBalancedList, grouped, exportedArchiversList);

        // Attribute distribution
        for (int i = 0; i < attributeNameList.length; i++) {
            String currentAttributeName = attributeNameList[i];
            String chosenArchiver = (String) attributeToChosenArchiverHasthable
                    .get(currentAttributeName);
            boolean theCurrentAttributeHasAChosenArchiver = chosenArchiver != null
                    && !chosenArchiver.equals("");
            String dedicatedArchiver = null;
            boolean theCurrentAttributeHasADedicatedArchiver = false;
            // System.out.println (
            // "ArchivingManagerApi/selectArchiversTask/currentAttributeName|"+currentAttributeName+"|chosenArchiver|"+chosenArchiver+"|"
            // );

            if (attributeToDedicatedArchiver != null) {
                if (attributeToDedicatedArchiver.size() > 0) {
                    Archiver archiver = (Archiver) attributeToDedicatedArchiver
                            .get(currentAttributeName);
                    dedicatedArchiver = archiver == null ? null : archiver
                            .getName();
                    // System.out.println (
                    // "ArchivingManagerApi/selectArchiversTask/dedicatedArchiver NOT NULL/dedicatedArchiver|"+dedicatedArchiver+"|"
                    // );
                    if (dedicatedArchiver != null
                            && !dedicatedArchiver.equals("")) {
                        theCurrentAttributeHasADedicatedArchiver = true;
                    }
                }
            }

            String archiver = "";

            // first let's find out whether the currentAttribute has already
            // been archived
            // if it has, we will use the same archiver
            archiver = m_DataBase.getMode().getArchiverForAttribute(
                    currentAttributeName);
            // System.out.println (
            // "ArchivingManagerApi/selectArchiversTask/m_hdbDataBase.getArchiverForAttribute/archiver|"+archiver+"|"
            // );
            // System.out.println (
            // "ArchivingManagerApi/selectArchiversTask/current archiver|"+archiver+"|"
            // );

            // if it hasn't, let's see if the currentAttribute has a
            // dedicatedArchiver
            if (archiver == null || "".equals(archiver)) {
                // si l'attribut n'est pas deja en cours d'archivage on regarde
                // s'il a un archiver reserve
                if (theCurrentAttributeHasADedicatedArchiver) {
                    archiver = dedicatedArchiver;
                    // System.out.println (
                    // "ArchivingManagerApi/selectArchiversTask/selected a NEW DEDICATED archiver"
                    // );
                }

                if (archiver == null || "".equals(archiver)) {
                    // si l'attribut n'a pas un archiver reserve , on regarde si
                    // l'utilisateur a specifie un choisi
                    if (theCurrentAttributeHasAChosenArchiver) {
                        archiver = chosenArchiver;
                    }
                    // sinon le load balancing se chargera de lui en trouver un.
                    else {
                        archiver = "";
                    }
                }
            }

            boolean canForce = false;
            if (!"".equals(archiver)) {
                // On a determine sur quel archiver l'attribut devait aller:
                // on est dans un cas ou l'attribut a un archiver soit parce
                // qu'il est deja archive, ou parce qu'il a un archiver choisi
                // ou dedie
                // mais il faut encore verifier qu'on ne met pas un attribut
                // non-reserve sur un archiver dedie.
                // Par exemple si l'attribut etait archive par un archiver
                // non-dedie, mais que cet archiver est maintenant dedie:
                // l'attribut n'a pas le droit de rester dessus
                // Ou autre exemple l'archiver choisi est un archiver dedie
                // (meme si Mambo ne le permet plus, l'API doit faire le
                // controle independamment de l'interface graphique)

                // On regarde si l'archiver sur lequel on veut mettre l'attribut
                // est dedie.
                boolean isArchiverDedicated = false;
                if (attributeToDedicatedArchiver != null) {
                    Iterator it = attributeToDedicatedArchiver.values()
                            .iterator();
                    // On parcourt la liste des archivers dedies, et pour chacun
                    // d'entre eux on regarde son nom:
                    // si ce nom = le nom recherche, alors l'archiver sur lequel
                    // on veut mettre l'attribut est un des archivers dedies.
                    while (it.hasNext()) {
                        Archiver next = (Archiver) it.next();
                        if (next.getName() != null
                                && next.getName().equals(archiver)) {
                            isArchiverDedicated = true;
                            break;
                        }
                    }
                }

                // Si oui (l'archiver sur lequel on veut mettre l'attribut est
                // dedie) il faut verifier que :
                // -l'attribut est reserve
                // -que son archiver dedie est l'archiver choisi
                if (isArchiverDedicated) {
                    // Pour ce faire, on regarde si l'attribut a un archiver
                    // dedie, et si oui si c'est l'archiver determine plus tot
                    boolean theCurrentAttributeIsRegisteredToThisDedicatedArchiver = dedicatedArchiver != null
                            && dedicatedArchiver.equals(archiver);
                    // Si oui, pas de probleme
                    if (theCurrentAttributeIsRegisteredToThisDedicatedArchiver) {
                        canForce = true;
                    }
                    // Sinon, on a indument tente d'affecter un attribut a un
                    // archiver dedie
                    // Il faut donc trouver un autre archiver a cet attribut
                    else {
                        canForce = false;
                    }
                } else {
                    canForce = true;
                }
            }

            if (canForce) {
                loadBalancedList.forceAddAtt2Archiver(currentAttributeName,
                        archiver, getAttributeDataFormat(attributeNameList[i]));
            } else {
                loadBalancedList.addAtt2LBL(currentAttributeName,
                        getAttributeDataFormat(attributeNameList[i]));
            }
        }
        return loadBalancedList;
    }

    /**
     * get the number of attribute taken in charge for the given Archiver device
     * 
     * @param archiverName
     * @return the number of attribute in charge
     * @throws ArchivingException
     */
    public static int get_charge(String archiverName) throws ArchivingException {
        try {
            int attribute_device_charge = 0;
            DeviceProxy archiverProxy = new DeviceProxy(archiverName);
            String[] attributeList = archiverProxy.get_attribute_list();
            for (int j = 0; j < attributeList.length; j++) {
                DeviceAttribute devattr = archiverProxy
                        .read_attribute(attributeList[j]);
                short[] shortarray = devattr.extractShortArray();
                short value = shortarray[0];
                // System.out.println(attributeList[ j ] + " = " + value);
                attribute_device_charge = attribute_device_charge + value;
            }
            return attribute_device_charge;
        } catch (DevFailed devFailed) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
            String reason = GlobalConst.TANGO_COMM_EXCEPTION;
            String desc = "Failed while executing ArchivingManagerApi.get_charge() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN,
                    desc, "", devFailed);
        }
    }

    /**
     * get the number of scalar taken in charge for the given Archiver device
     * 
     * @param archiverName
     * @return the number of scalar in charge
     * @throws ArchivingException
     */
    public static int get_scalar_charge(String archiverName)
            throws ArchivingException {
        try {
            DeviceProxy archiverProxy = new DeviceProxy(archiverName);
            DeviceAttribute devattr = archiverProxy
                    .read_attribute(m_SCALARCHARGE);
            short[] shortarray = devattr.extractShortArray();
            // System.out.println(m_SCALARCHARGE + " = " + shortarray[ 0 ]);
            return shortarray[0];
        } catch (DevFailed devFailed) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * get the number of spectrum taken in charge for the given Archiver device
     * 
     * @param archiverName
     * @return the number of spectrum in charge
     */
    public static int get_spectrum_charge(String archiverName) {
        try {
            DeviceProxy archiverProxy = new DeviceProxy(archiverName);
            DeviceAttribute devattr = archiverProxy
                    .read_attribute(m_SPECTRUMCHARGE);
            short[] shortarray = devattr.extractShortArray();
            // System.out.println(m_SPECTRUMCHARGE + " = " + shortarray[ 0 ]);
            return shortarray[0];
        } catch (DevFailed devFailed) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * get the number of image taken in charge for the given Archiver device
     * 
     * @param archiverName
     * @return the image of spectrum in charge
     */
    public static int get_image_charge(String archiverName) {
        try {
            DeviceProxy archiverProxy = new DeviceProxy(archiverName);
            DeviceAttribute devattr = archiverProxy
                    .read_attribute(m_IMAGECHARGE);
            short[] shortarray = devattr.extractShortArray();
            // System.out.println(m_IMAGECHARGE + " = " + shortarray[ 0 ]);
            return shortarray[0];
        } catch (DevFailed devFailed) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * this method return AttributeInfo tango class
     * 
     * @param attributeName
     * @return AttributeInfo
     */
    public static AttributeInfo getAttributeInfo(String attributeName,
            boolean doRefresh) throws DevFailed {
        AttributeInfo att_info = null;
        if (!doRefresh) {
            att_info = getBufferedAttributeInfo(attributeName);
        }

        if (att_info == null) {
            att_info = getAttributeInfoFromTheDevice(attributeName);
        }

        if (doRefresh && att_info != null) {
            bufferAttributeInfo(attributeName, att_info);
        }

        return att_info;
    }

    private static void bufferAttributeInfo(String attributeName,
            AttributeInfo att_info) {
        attributeToDeviceInfo.put(attributeName, att_info);
    }

    private static AttributeInfo getBufferedAttributeInfo(String attributeName) {
        return (AttributeInfo) attributeToDeviceInfo.get(attributeName);
    }

    private static AttributeInfo getAttributeInfoFromTheDevice(
            String attributeName) throws DevFailed {
        int index = attributeName.lastIndexOf("/");
        String device_name = attributeName.substring(0, index);
        DeviceProxy deviceProxy = new DeviceProxy(device_name);
        String att_name = attributeName.substring(index + 1);
        AttributeInfo att_info = deviceProxy.get_attribute_info(att_name);
        return att_info;
    }

    /**
     * this method return data_format value of the attribute
     * 
     * @param attributeName
     * @return AttrDataFormat
     */
    private static int getAttributeDataFormat(String attributeName) {
        AttributeInfo attribute = null;
        try {
            attribute = getAttributeInfo(attributeName, false);
            return attribute.data_format.value();
        } catch (DevFailed devFailed) {
            Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
                    + "ArchivingManagerApi.getAttributeDataFormat" + "\r\n"
                    + "\t Reason : \t " + "UNKNOW_ERROR" + "\r\n"
                    + "\t Description : \t " + devFailed.getMessage() + "\r\n"
                    + "\t Additional information : \t " + "Attribute named '"
                    + attributeName + "' not found !" + "\r\n");
        }
        return AttrDataFormat._SCALAR;
    }

    private static String[] split_att_name_3_fields(String att_name) {
        String host = "";
        String domain = "";
        String family = "";
        String member = "";
        String attribut = "";
        String[] argout = new String[5];// = {"HOST", "DOMAIN", "FAMILY",
        // "MEMBER", "ATTRIBUT"};
        String[] decoupe; // d�coupage en 5 partie : host, domain, family,
        // member, attribut

        // Host name management
        if (att_name.startsWith("//"))
            att_name = att_name.substring(2, att_name.length());
        else
            att_name = TangoAccess.getTangoHost() + "/" + att_name;

        // Spliting
        decoupe = att_name.split("/"); // spliting the name in 3 fields
        host = decoupe[0];
        domain = decoupe[1];
        family = decoupe[2];
        member = decoupe[3];
        attribut = decoupe[4];

        argout[0] = host;
        argout[1] = domain;
        argout[2] = family;
        argout[3] = member;
        argout[4] = attribut;
        return argout;
    }

    /**
     * This method allows to open the connection on either the historic or the
     * temporary database
     * 
     * @param historic
     *            true if historical archiving, false otherwise.
     * @param muser
     * @param mpassword
     * @throws ArchivingException
     */
    private void connectArchivingDatabase(String mdbhost, String mdbname,
            String muser, String mpassword, boolean rac)
            throws ArchivingException {
        if (m_DataBase == null) {
            // Host
            String host = mdbhost;
            try {
                if (host == null || host.equals("")) {
                    host = GetConf.getHost(getClassDevice());
                    if (host.equals("")) {
                        host = getHost();
                    }
                }
            } catch (ArchivingException e) {
                System.err.println(e.toString());
                host = getHost();
            }

            // Name
            String name = mdbname;
            try {
                if (name == null || name.equals("")) {
                    name = GetConf.getName(getClassDevice());
                    if (name.equals("")) {
                        name = getSchema();
                    }
                }

            } catch (ArchivingException e) {
                System.err.println(e.toString());
                name = getSchema();
            }

            // Schema
            String schema = null;
            try {
                schema = GetConf.getSchema(getClassDevice());
                if (schema.equals(""))
                    schema = getSchema();// ( historic ) ?
                // ConfigConst.HDB_SCHEMA_NAME
                // :
                // ConfigConst.TDB_SCHEMA_NAME;
            } catch (ArchivingException e) {
                System.err.println(e.toString());
                schema = getSchema();// ( historic ) ?
                // ConfigConst.HDB_SCHEMA_NAME :
                // ConfigConst.TDB_SCHEMA_NAME;
            }

            // User
            String user = muser;
            if (user.equals(""))
                user = getUser();// ( historic ) ?
            // ConfigConst.HDB_BROWSER_USER
            // :
            // ConfigConst.TDB_BROWSER_USER;

            String pass = mpassword;
            if (pass.equals(""))
                pass = getPassword();// ( historic ) ?
            // ConfigConst.HDB_BROWSER_PASSWORD
            // :
            // ConfigConst.TDB_BROWSER_PASSWORD;

            m_DataBase = DataBaseManagerFactory.getDataBaseManager(type);
            m_DataBase.setDbConn(ConnectionFactory.connect_auto(host, name,
                    schema, user, pass, type, rac));
            is_connected = true;
        }
    }

    /**
     * Close the connection of the given type archiving the database
     * 
     * @param historic
     */
    public void closeDatabase() {
        try {
            if (m_DataBase != null) {
                m_DataBase.getDbConn().close();
                m_DataBase = null;
                is_connected = false;
            }
        } catch (ArchivingException e) {
            m_DataBase = null;
        }
    }

    /**
     * Configure the Archiving
     * 
     * @param user
     * @param password
     * @param historic
     * @throws ArchivingException
     */
    public void ArchivingConfigure(String user, String password)
            throws ArchivingException {
        // System.out.println( "ArchivingConfigure/historic/"+historic);

        ArchivingConfigureWithoutArchiverListInit("", "", user, password, false);

        // The created lists are useful during the start archiving action
        initDbArchiverList();
    }

    /**
     * Configure the Archiving
     * 
     * @param user
     * @param password
     * @param historic
     * @throws ArchivingException
     */
    public void ArchivingConfigure(String host, String name, String user,
            String password, boolean rac) throws ArchivingException {
        // System.out.println( "ArchivingConfigure/historic/"+historic);

        ArchivingConfigureWithoutArchiverListInit(host, name, user, password,
                rac);

        // The created lists are useful during the start archiving action
        // //////////////// SPJZZZZZZZZZZZZ : A r�activer !!!!!!!!!!!!
        initDbArchiverList();
    }

    public void ArchivingConfigureWithoutArchiverListInit(String host,
            String name, String user, String password, boolean rac)
            throws ArchivingException {
        // System.out.println(
        // "ArchivingConfigureWithoutArchiverListInit/historic/"+historic+"/user/"+user);
        setM_Facility(getFacility());

        // Database connection
        connectArchivingDatabase(host, name, user, password, rac);
    }

    /**
     * @param historic
     */
    public DataBaseManager getDataBase() {
        return m_DataBase;
    }

    /**
     * @param my_vector
     */
    public static String[] toStringArray(Vector my_vector) {
        String[] my_array;
        my_array = new String[my_vector.size()];
        for (int i = 0; i < my_vector.size(); i++) {
            my_array[i] = (String) my_vector.elementAt(i);
        }
        return my_array;
    }

    /**
     * @param my_vector
     */
    public static String[] toStoppingArrayAttLightMode(Vector my_vector) {
        Vector myResultVect = new Vector();

        for (int i = 0; i < my_vector.size(); i++) {
            AttributeLightMode attributeLightMode = (AttributeLightMode) my_vector
                    .elementAt(i);
            String[] attributeLightMode_Arr = attributeLightMode.toArray();
            myResultVect.add(Integer.toString(attributeLightMode_Arr.length));
            for (int j = 0; j < attributeLightMode_Arr.length; j++) {
                myResultVect.add(attributeLightMode_Arr[j]);
            }
        }
        return toStringArray(myResultVect);
    }

    /**
     * @param my_light_attributs
     */
    public static Vector stoppingVector(String[] my_light_attributs) {
        Vector myResultVect = new Vector();
        int index = 0;
        while (index < my_light_attributs.length) {
            int myAttLenght = Integer.parseInt(my_light_attributs[index]);
            index++;
            String[] myLightAttribut = new String[myAttLenght];
            System.arraycopy(my_light_attributs, index, myLightAttribut, 0,
                    myAttLenght);
            index = index + myAttLenght;
            AttributeLightMode attributeLightMode = AttributeLightMode
                    .creationWithFullInformation(myLightAttribut);
            myResultVect.add(attributeLightMode);
        }
        return myResultVect;
    }

    public static boolean checkAttributeSupport(String attributeName)
            throws ArchivingException {
        try {
            AttributeInfo attributeInfo = getAttributeInfo(attributeName, true);
            int data_type = attributeInfo.data_type;
            int data_format = attributeInfo.data_format.value();
            int writable = attributeInfo.writable.value();
            return AttributeSupport.checkAttributeSupport(attributeName,
                    data_type, data_format, writable);
        } catch (DevFailed devFailed) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                    + GlobalConst.TANGO_COMM_EXCEPTION + " or "
                    + GlobalConst.ATT_UNREACH_EXCEPTION;
            String reason = "Failed while executing ArchivingManagerApi.checkAttributeSupport() method...";
            String desc = "Failed while retrieving informations for the attribute named '"
                    + attributeName + "'";
            throw new ArchivingException(message, reason, ErrSeverity.PANIC,
                    desc, DevFailed.class.getName(), devFailed);
        }
    }

    private static void traceXX(Hashtable deviceClassToDevices) {
        if (deviceClassToDevices == null) {
            System.out.println("traceXX/NULL!!");
        }
        Enumeration e = deviceClassToDevices.keys();
        System.out.println("Device classes/START");
        while (e.hasMoreElements()) {
            String nextKey = (String) e.nextElement();
            System.out.println("    Device class START|" + nextKey + "|");
            Vector nextVal = (Vector) deviceClassToDevices.get(nextKey);
            if (nextVal != null) {
                Enumeration e2 = nextVal.elements();
                while (e2.hasMoreElements()) {
                    String nextDev = (String) e2.nextElement();
                    System.out.println("        Device|" + nextDev + "|");
                }
            }
            System.out.println("    Device class END|" + nextKey + "|");

        }
        System.out.println("Device classes/END");
    }

    /*
	 *
	 */
    public Hashtable getAttributesToDedicatedArchiver(boolean refreshArchivers)
            throws ArchivingException {
        if (refreshArchivers) {
            initDbArchiverList();
        }

        String[] exportedArchiversNameList = getM_ExportedArchiverList();
        String[] notExportedArchiversNameList = getM_NotExportedArchiverList();
        if (exportedArchiversNameList.length == 0
                && notExportedArchiversNameList.length == 0) {
            return null;
        }

        Archiver[] achiversList = new Archiver[exportedArchiversNameList.length
                + notExportedArchiversNameList.length];
        for (int i = 0; i < exportedArchiversNameList.length; i++) {
            String currentExportedArchiverName = exportedArchiversNameList[i];
            Archiver currentExportedArchiver = Archiver
                    .findArchiver(currentExportedArchiverName,
                            DbUtils.getHdbTdbType(m_DataBase.getDbConn()
                                    .getDbType()) == ConfigConst.HDB);
            currentExportedArchiver.setExported(true);
            achiversList[i] = currentExportedArchiver;
        }
        for (int i = 0; i < notExportedArchiversNameList.length; i++) {
            String currentNotExportedArchiverName = notExportedArchiversNameList[i];
            Archiver currentNotExportedArchiver = Archiver
                    .findArchiver(currentNotExportedArchiverName,
                            DbUtils.getHdbTdbType(m_DataBase.getDbConn()
                                    .getDbType()) == ConfigConst.HDB);
            currentNotExportedArchiver.setExported(false);
            achiversList[i + exportedArchiversNameList.length] = currentNotExportedArchiver;
        }

        Hashtable ret = dispatchAttributeByArchiver(achiversList);
        Archiver
                .setAttributesToDedicatedArchiver(
                        DbUtils.getHdbTdbType(m_DataBase.getDbConn()
                                .getDbType()) == ConfigConst.HDB, ret);
        return ret;
    }

    /*
	 *
	 */
    private static Hashtable dispatchAttributeByArchiver(
            Archiver[] archiversList) {
        if (archiversList == null || archiversList.length == 0) {
            System.out
                    .println("ArchivingManagerApi/dispatchAttributeByArchiver/REPERE 1|");
            return null;
        }

        Hashtable ret = new Hashtable();
        for (int i = 0; i < archiversList.length; i++) {
            Archiver currentArchiver = archiversList[i];
            boolean isDedicated = currentArchiver.isDedicated();
            if (!isDedicated) {
                continue;
            }

            String[] reservedAttributes = currentArchiver
                    .getReservedAttributes();
            if (reservedAttributes == null || reservedAttributes.length == 0) {
                continue;
            }

            for (int j = 0; j < reservedAttributes.length; j++) {
                ret.put(reservedAttributes[j], currentArchiver);
            }
        }

        return ret;
    }

    /*
	 *
	 */
    private static void initArchivers(LoadBalancedList loadBalancedList,
            boolean grouped, String[] referenceArchiverList)
            throws ArchivingException {
        if (referenceArchiverList.length == 0) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
            String reason = GlobalConst.NO_ARC_EXCEPTION;
            String desc = "Failed while executing ArchivingManagerApi.selectArchiver() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN,
                    desc, "");
        }

        // LoadBalancedList archivers initializing
        if (grouped) {
            loadBalancedList.addArchiver(referenceArchiverList[0],
                    get_scalar_charge(referenceArchiverList[0]),
                    get_spectrum_charge(referenceArchiverList[0]),
                    get_image_charge(referenceArchiverList[0]));
        } else {
            for (int i = 0; i < referenceArchiverList.length; i++) {
                loadBalancedList.addArchiver(referenceArchiverList[i],
                        get_scalar_charge(referenceArchiverList[i]),
                        get_spectrum_charge(referenceArchiverList[i]),
                        get_image_charge(referenceArchiverList[i]));
            }
        }
    }

    /**
     * @return @return the number of archivers for the given archiving type
     */
    public int getArchiverListSize() {
        // System.out.println( "CLA/historic/"+historic);
        if (getM_ExportedArchiverList() == null) {
            System.out.println("CLA/ == null");
            return 0;
        }
        return getM_ExportedArchiverList().length;
    }

}
