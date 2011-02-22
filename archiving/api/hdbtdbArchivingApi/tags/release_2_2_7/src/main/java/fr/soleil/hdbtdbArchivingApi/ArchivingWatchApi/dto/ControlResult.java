/*	Synchrotron Soleil
 *
 *   File          :  ControlResult.java
 *
 *   Project       :  archiving_watcher
 *
 *   Description   :
 *
 *   Author        :  CLAISSE
 *
 *   Original      :  28 nov. 2005
 *
 *   Revision:  					Author:
 *   Date: 							State:
 *
 *   Log: ControlResult.java,v
 *
 */
/*
 * Created on 28 nov. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.DBReaderFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.IDBReader;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchiversComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchivingAttributeSubNamesComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ControlResultLineComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.DomainsComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.choosing.ChoosingStrategyFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.choosing.IChoosingStrategy;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes.IModeController;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * Models the result of a control step or cycle, and contains all the necessary
 * data to display a report and a status code.
 * 
 * @author CLAISSE
 */
public class ControlResult {
    private int code = ControlResult.NOT_READY;
    private Map<String, ControlResultLine> lines = new HashMap<String, ControlResultLine>();;
    private boolean isBuilt = false;

    private Map<String, Archiver> errorArchivers = new HashMap<String, Archiver>();
    private Map<String, Domain> errorDomains = new HashMap<String, Domain>();
    private Map<String, ArchivingAttribute> errorAttributes = new HashMap<String, ArchivingAttribute>();
    private Map<String, ArchivingAttributeSubName> errorSubAttributes = new HashMap<String, ArchivingAttributeSubName>();;

    private Map<String, ArchivingAttribute> okAttributes;

    private long totalTime;
    private long timeReadingArchivingAttributes;
    private long timeApplyingChoosingStrategy;
    private long timeControlling;

    private Timestamp startDate;
    private Timestamp endDate;

    private int numberOfArchivingAttributes;
    private int numberOfControlledAttributes;
    private int numberOfKoAttributes;
    private int numberOfUndeterminedAttributes;
    private int numberOfAttributesControlledWithSuccess;
    private int m_numberOfAttributesControlledWithSuccessWithNullValue;

    private boolean isFullCycle;
    private int cycles;
    private int steps;

    private final ControlResultLineComparator linesComparator;
    private final ArchiversComparator archiversComparator;
    private final DomainsComparator domainsComparator;
    private final ArchivingAttributeSubNamesComparator archivingAttributeSubNamesComparator;

    /**
     * Status code for when the ControlResult is empty or not computed yet.
     */
    public static final int NOT_READY = -1;

    /**
     * Status code for when all attributes are OK
     */
    public static final int ALL_OK = 10;
    /**
     * Status code for when there are no attributes to control
     */
    public static final int NO_ATTRIBUTES_TO_CONTROL = 11;

    /**
     * Status code for when all attributes are KO
     */
    public static final int ALL_KO = 20;
    /**
     * Status code for when some, but not all attributes are KO
     */
    public static final int PARTIAL_KO = 21;
    /**
     * Status code for when all attributes are Undetermined (eg. can happen if
     * the cycle or step failed to load the list of archiving attributes)
     */
    public static final int ALL_UNDETERMINED = 22;

    /**
     * The label for an empty report
     */
    public static final String EMPTY_REPORT = "NO REPORT AVAILABLE YET.";

    /**
     * Default constructor.
     */
    public ControlResult() {
	super();
	lines = new Hashtable<String, ControlResultLine>();

	linesComparator = new ControlResultLineComparator();
	archiversComparator = new ArchiversComparator();
	domainsComparator = new DomainsComparator();
	archivingAttributeSubNamesComparator = new ArchivingAttributeSubNamesComparator();

	isBuilt = false;

	final Timestamp _startDate = new Timestamp(System.currentTimeMillis());
	setStartDate(_startDate);
    }

    public void setAllArchivingAttributes(final Hashtable<String, ModeData> list) {
	if (list == null) {
	    return;
	}
	final Enumeration<String> en = list.keys();
	while (en.hasMoreElements()) {
	    final String next = en.nextElement();
	    lines.put(next, new ControlResultLine(next));
	}

    }

    /**
     * Sets the CR's OK and KO attributes
     * 
     * @param listOfControlledAttributes
     *            A Vector [] of size 2, which first element is the list of the
     *            KO attributes complete names, and which second element is the
     *            list of the OK attributes complete names.
     * @throws DevFailed
     */
    public void setLines(final Vector[] listOfControlledAttributes) throws DevFailed {
	final Vector koAttributes = listOfControlledAttributes[0];
	final Vector okAttributes = listOfControlledAttributes[1];

	addList(koAttributes, false);
	addList(okAttributes, true);

	setNumberOfArchivingAttributes(koAttributes.size() + okAttributes.size());

	build(false, false);
    }

    /**
     * @param controlledAttributes
     * @param b
     */
    private void addList(final Vector controlledAttributes, final boolean areOK) {
	// System.out.println ( "CLA/ControlResult/addList/areOK/"+areOK );

	if (controlledAttributes != null && controlledAttributes.size() != 0) {
	    final int numberOfKOAttributes = controlledAttributes.size();
	    for (int i = 0; i < numberOfKOAttributes; i++) {
		final String nextCompleteName = (String) controlledAttributes.elementAt(i);
		// System.out.println (
		// "CLA/ControlResult/addList/nextCompleteName|"+nextCompleteName
		// );

		// CLA 17/05/06--------
		// ModeData nextModeData = null;
		final ModeData nextModeData = new ModeData();
		nextModeData.setIncomplete(true);
		// --------

		final ArchivingAttribute attr = new ArchivingAttribute();
		attr.setCompleteName(nextCompleteName);
		final int[] result = getResult(areOK);

		final ControlResultLine line = new ControlResultLine(attr, nextModeData, result);
		addLine(line);
	    }
	}
    }

    /**
     * @return
     */
    private int[] getResult(final boolean isOK) {
	final int[] result = new int[IModeController.NUMBER_OF_MODES_TO_CONTROL];

	if (isOK) {
	    result[IModeController.MODE_P_POSITION] = IModeController.CONTROL_OK;
	} else {
	    result[IModeController.MODE_P_POSITION] = IModeController.CONTROL_KO;
	}
	result[IModeController.MODE_A_POSITION] = IModeController.CONTROL_NOT_DONE;
	result[IModeController.MODE_R_POSITION] = IModeController.CONTROL_NOT_DONE;
	result[IModeController.MODE_T_POSITION] = IModeController.CONTROL_NOT_DONE;
	result[IModeController.MODE_D_POSITION] = IModeController.CONTROL_NOT_DONE;
	result[IModeController.MODE_C_POSITION] = IModeController.CONTROL_NOT_DONE;
	result[IModeController.MODE_E_POSITION] = IModeController.CONTROL_NOT_DONE;

	return result;
    }

    /**
     * Returns true if and only if the CR is complete (no attribute missing)
     * 
     * @return True if and only if the CR is complete (no attribute missing)
     */
    public boolean isComplete() {
	return getCurrentSize() >= getFinalSize();
    }

    /**
     * Returns true if and only if the CR is empty (no attribute)
     * 
     * @return True if and only if the CR is empty (no attribute)
     */
    public boolean isEmpty() {
	return getCurrentSize() == 0;
    }

    /**
     * Returns the CR's current size (number of attributes)
     * 
     * @return
     */
    public int getCurrentSize() {
	final int size = lines == null ? 0 : lines.size();
	return size;
    }

    /**
     * Returns the CR's final size (all attributes accounted for)
     * 
     * @return
     */
    public int getFinalSize() {
	final IChoosingStrategy choosingStrategy = ChoosingStrategyFactory.getCurrentImpl();
	final int size = choosingStrategy.getNumberOfAttributesToControl();
	return size;
    }

    /**
     * Builds and returns the associated report
     * 
     * @return The report
     * @throws DevFailed
     */
    public String getReport() throws DevFailed {
	final StringBuffer buff = new StringBuffer();

	buff.append(getReportHeader());
	buff.append(Tools.CRLF);

	buff.append(getAttributesReport());
	buff.append(Tools.CRLF);

	buff.append(getArchiversReport());
	buff.append(Tools.CRLF);

	buff.append(getDomainsReport());
	buff.append(Tools.CRLF);

	// buff.append ( this.getAttributeSubNameReport () );

	final String ret = buff.toString();

	return ret;
    }

    /**
     * @return
     */
    private String getArchiversReport() {
	final StringBuffer buff = new StringBuffer();

	if (errorArchivers != null && errorArchivers.size() != 0) {
	    buff
		    .append("**************************************ARCHIVERS IN CHARGE OF KO ATTRIBUTES*****************************************");
	    buff.append(Tools.CRLF);

	    final List<Archiver> list = new Vector<Archiver>();
	    list.addAll(errorArchivers.values());
	    Collections.sort(list, archiversComparator);
	    final Iterator<Archiver> it = list.iterator();

	    while (it.hasNext()) {
		/*
		 * String nextArchiverName = (String) it.next (); Archiver
		 * nextArchiver = (Archiver) this.errorArchivers.get (
		 * nextArchiverName );
		 */
		final Archiver nextArchiver = it.next();
		buff.append(nextArchiver.getReport());
		buff.append(Tools.CRLF);

	    }

	    // if (this.errorArchivers != null && this.errorArchivers.size() !=
	    // 0) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("**************************************************************************************************");
	}

	return buff.toString();
    }

    private String getDomainsReport() {
	final StringBuffer buff = new StringBuffer();

	if (errorDomains != null && errorDomains.size() != 0) {
	    buff
		    .append("**************************************DOMAINS WITH AT LEAST 1 KO ATTRIBUTE*****************************************");
	    buff.append(Tools.CRLF);

	    final List<Domain> list = new Vector<Domain>();
	    list.addAll(errorDomains.values());
	    Collections.sort(list, domainsComparator);
	    final Iterator<Domain> it = list.iterator();

	    while (it.hasNext()) {
		final Domain nextDomain = it.next();
		buff.append(nextDomain.getReport());
		buff.append(Tools.CRLF);
	    }

	    // if (this.errorDomains != null && this.errorDomains.size() != 0) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("**************************************************************************************************");
	}

	return buff.toString();
    }

    /**
     * @return
     */
    private String getAttributesReport() {
	final StringBuffer bufferKO = new StringBuffer();
	final StringBuffer bufferUndetermined = new StringBuffer();
	final StringBuffer bufferValueCertainlyNull = new StringBuffer();
	final StringBuffer bufferValuePossiblyNull = new StringBuffer();

	final StringBuffer buff = new StringBuffer();
	boolean hasCasesOfValueCertainlyNull = false;
	boolean hasCasesOfValuePossiblyNull = false;

	final List<ControlResultLine> list = new Vector<ControlResultLine>();
	list.addAll(lines.values());
	Collections.sort(list, linesComparator);
	final Iterator<ControlResultLine> it = list.iterator();

	while (it.hasNext()) {
	    final ControlResultLine nextLine = it.next();

	    if (!nextLine.isArchivingOK()) {
		// System.out.println (
		// "CLA/getAttributesReport/nextCompleteName|"+nextCompleteName+"|nextLine.getAttribute ().isDetermined ()|"+nextLine.getAttribute
		// ().isDetermined () );
		if (nextLine.getAttribute().isDetermined()) {
		    bufferKO.append(nextLine.getReport());
		    bufferKO.append(Tools.CRLF);
		} else {
		    bufferUndetermined.append(nextLine.getReport());
		    bufferUndetermined.append(Tools.CRLF);
		}
	    }
	    if (nextLine.isValueCertainlyNull()) {
		hasCasesOfValueCertainlyNull = true;

		bufferValueCertainlyNull.append(nextLine.getReport());
		bufferValueCertainlyNull.append(Tools.CRLF);
	    }
	    if (nextLine.isValuePossiblyNull()) {
		hasCasesOfValuePossiblyNull = true;

		bufferValuePossiblyNull.append(nextLine.getReport());
		bufferValuePossiblyNull.append(Tools.CRLF);
	    }
	}

	if (getNumberOfUndeterminedAttributes() > 0) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("***************************************** Undetermined Attributes **********************************");
	    buff.append(Tools.CRLF);
	    buff.append(bufferUndetermined);
	}
	if (getNumberOfKoAttributes() > 0) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("***************************************** KO Attributes **********************************");
	    buff.append(Tools.CRLF);
	    buff.append(bufferKO);
	}
	if (hasCasesOfValueCertainlyNull) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("******************* OK Attributes whose last values have been null (archiving works but there might be a problem with the archived device) ***********************");
	    buff.append(Tools.CRLF);
	    buff.append(bufferValueCertainlyNull);
	}
	if (hasCasesOfValuePossiblyNull) {
	    buff.append(Tools.CRLF);
	    buff
		    .append("******************* OK Attributes whose last values might have been null, but it couldn't be determined ***********************");
	    buff.append(Tools.CRLF);
	    buff.append(bufferValuePossiblyNull);
	}
	return buff.toString();
    }

    public String getReportHeader() throws DevFailed {
	final StringBuffer buff = new StringBuffer();

	buff
		.append("************************************** General *****************************************");
	buff.append(Tools.CRLF);

	final String endDate = getEndDate() == null ? "NOT YET" : getEndDate() + "";
	buff.append("Cycle started at: " + getStartDate() + ". Current time: "
		+ new Timestamp(System.currentTimeMillis()) + ". Cycle completed at: " + endDate);
	buff.append(Tools.CRLF);

	buff.append("Completed cycles: " + getCycles() + ". Completed steps: " + getSteps());
	buff.append(Tools.CRLF);

	buff.append("Global result: " + ControlResult.getAssociatedLabel(getCode()));
	buff.append(Tools.CRLF);

	buff.append("Attributes recorded in DB when the current fixed cycle started: "
		+ getNumberOfArchivingAttributes());
	buff.append(Tools.CRLF);

	buff.append("Control attempts in the current rollover cycle: "
		+ getNumberOfControlledAttributes());
	buff.append(Tools.CRLF);

	buff.append("Successful control attempts in the current rollover cycle: "
		+ getNumberOfAttributesControlledWithSuccess());
	buff.append(Tools.CRLF);

	buff.append("Number of KO attributes: " + getNumberOfKoAttributes());
	buff.append(Tools.CRLF);

	buff.append("Number of undetermined attributes: " + getNumberOfUndeterminedAttributes());
	buff.append(Tools.CRLF);

	buff.append("Number of attributes with null values: "
		+ getNumberOfAttributesControlledWithSuccessWithNullValue());

	return buff.toString();
    }

    /**
     * Tests whether a given attribute is correctly archived
     * 
     * @param completeName
     *            The attribute's complete name
     * @return Whether the attribute is correctly archived
     */
    public boolean isAttributeCorrectlyArchived(final String completeName) {
	final ControlResultLine nextLine = lines.get(completeName);
	final int attributeStatus = nextLine.getAttribute().getArchivingStatus();

	boolean ret = attributeStatus == IModeController.CONTROL_OK;
	ret = ret || attributeStatus == IModeController.CONTROL_OK_BUT_VALUE_MIGHT_BE_NULL;
	ret = ret || attributeStatus == IModeController.CONTROL_OK_BUT_VALUE_IS_NULL;
	return ret;
    }

    private static String getAssociatedLabel(final int _code) throws DevFailed {
	switch (_code) {
	case ControlResult.NOT_READY:
	    return "NOT_READY";

	case ControlResult.ALL_OK:
	    return "ALL_OK";

	case ControlResult.ALL_KO:
	    return "ALL_KO";

	case ControlResult.PARTIAL_KO:
	    return "PARTIAL_KO";

	case ControlResult.NO_ATTRIBUTES_TO_CONTROL:
	    return "NO_ATTRIBUTES_TO_CONTROL";

	case ControlResult.ALL_UNDETERMINED:
	    return "ALL_UNDETERMINED";

	default:
	    Tools
		    .throwDevFailed(new IllegalArgumentException("Expected either of " + NOT_READY
			    + "," + ALL_OK + "," + ALL_KO + "," + PARTIAL_KO + ","
			    + NO_ATTRIBUTES_TO_CONTROL + " as a parameter. Received " + _code
			    + " instead."));
	}
	return null;
    }

    /**
     * @return Returns the code.
     */
    public int getCode() {
	return code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(final int code) {
	this.code = code;
    }

    /**
     * Adds a line to the current CR
     * 
     * @param line
     *            The line to add
     */
    public void addLine(final ControlResultLine line) {
	final ArchivingAttribute attr = line.getAttribute();
	final String completeName = attr.getCompleteName();
	// System.out.println (
	// "ControlResult/addLine/completeName/"+completeName );
	lines.put(completeName, line);
    }

    /**
     * Computes the status code and builds the report, from the accumulated
     * individual attributes data.
     * 
     * @param doArchiverDiagnosis
     *            True if one desires informations on archiver that have KO
     *            attributes. Makes the process a bit more resource-intensive.
     * @throws DevFailed
     */
    public void build(final boolean doArchiverDiagnosis, final boolean _isFullCycle)
	    throws DevFailed {
	// Tools.trace ( "ControlResult/build/START" , Warnable.LOG_LEVEL_DEBUG
	// );

	// System.out.println (
	// "CLA/ControlResult/build/isFullCycle/"+_isFullCycle );
	isFullCycle = _isFullCycle;

	final int _numberOfControlledAttributes = lines.size();

	buildLines(lines);

	setNumberOfControlledAttributes(_numberOfControlledAttributes);
	setNumberOfAttributesControlledWithSuccess(_numberOfControlledAttributes
		- getNumberOfUndeterminedAttributes());

	// int _code = this.buildGlobalCode ( _numberOfControlledAttributes ,
	// this.getNumberOfKoAttributes() );
	final int _code = buildGlobalCode(_numberOfControlledAttributes,
		getNumberOfAttributesControlledWithSuccess(), getNumberOfKoAttributes());
	setCode(_code);

	// Tools.trace (
	// "ControlResult/build/_numberOfControlledAttributes/"+_numberOfControlledAttributes+"/_numberOfKoAttributes/"+this.getNumberOfKoAttributes()+"/_code/"+_code+"/"
	// , Warnable.LOG_LEVEL_DEBUG );

	Map<String, Archiver> _errorArchivers;
	try {
	    _errorArchivers = buildArchivers(errorArchivers, doArchiverDiagnosis);
	} catch (final Exception e) {
	    // Tools.throwDevFailed ( e );
	    Tools.trace("ControlResult/failed to build the archivers report!", e,
		    Warnable.LOG_LEVEL_ERROR);
	    _errorArchivers = new Hashtable<String, Archiver>();
	}
	setErrorArchivers(_errorArchivers);

	final Map<String, Domain> _errorDomains = buildDomains(errorDomains);
	setErrorDomains(_errorDomains);

	final Map<String, ArchivingAttributeSubName> _errorSubAttributes = buildSubAttributes(errorSubAttributes);
	setErrorSubAttributes(_errorSubAttributes);

	isBuilt = true;
    }

    private Map<String, ArchivingAttributeSubName> buildSubAttributes(
	    final Map<String, ArchivingAttributeSubName> _errorSubAttributes) {
	// final Enumeration<String> errorSubAttributesKeys =
	// _errorSubAttributes.keys();
	for (final String key : _errorSubAttributes.keySet()) {

	    final ArchivingAttributeSubName attrSubName = new ArchivingAttributeSubName();
	    attrSubName.setName(key);
	    attrSubName.buildKOAttributes(errorAttributes);
	    if (!attrSubName.hasKOAttributes()) {
		_errorSubAttributes.remove(key);
		continue;
	    }

	    _errorSubAttributes.put(key, attrSubName);
	}
	return _errorSubAttributes;
    }

    private Map<String, Domain> buildDomains(final Map<String, Domain> _errorDomains) {

	for (final String key : _errorDomains.keySet()) {
	    final Domain domain = new Domain();
	    domain.setName(key);
	    domain.buildKOAttributes(errorAttributes);
	    if (!domain.hasKOAttributes()) {
		_errorDomains.remove(key);
		continue;
	    }

	    _errorDomains.put(key, domain);
	}
	return _errorDomains;
    }

    private void buildLines(final Map<String, ControlResultLine> _lines) throws DevFailed {
	// final Enumeration<String> _enum = _lines.keys();
	int _numberOfKoAttributes = 0;
	int _numberOfUndeterminedAttributes = 0;
	int _numberOfAttributesOKWithNullValues = 0;
	for (final ControlResultLine value : _lines.values()) {
	    final ControlResultLine nextLine = value;

	    nextLine.build();

	    final ArchivingAttribute attr = nextLine.getAttribute();
	    final boolean isArchivingOK = attr.isArchivingOK();
	    final boolean isDetermined = attr.isDetermined();
	    final ModeData modeData = nextLine.getModeData();

	    if (!isArchivingOK) {
		if (modeData != null && !modeData.isIncomplete()) {
		    errorArchivers.put(modeData.getArchiver(), new Archiver());
		}

		errorDomains.put(attr.getDomain(), new Domain());
		errorSubAttributes.put(attr.getAttributeSubName(), new ArchivingAttributeSubName());
		errorAttributes.put(attr.getCompleteName(), attr);

		if (isDetermined) {
		    _numberOfKoAttributes++;
		} else {
		    _numberOfUndeterminedAttributes++;
		}
	    } else {
		if (attr.getArchivingStatus() == IModeController.CONTROL_OK_BUT_VALUE_IS_NULL) {
		    _numberOfAttributesOKWithNullValues++;
		}

		okAttributes.put(attr.getCompleteName(), attr);
		errorAttributes.remove(attr.getCompleteName());
	    }
	}

	setNumberOfKoAttributes(_numberOfKoAttributes);
	setNumberOfUndeterminedAttributes(_numberOfUndeterminedAttributes);
	setNumberOfAttributesControlledWithSuccessWithNullValue(_numberOfAttributesOKWithNullValues);
    }

    private Map<String, Archiver> buildArchivers(final Map<String, Archiver> _errorArchivers,
	    final boolean doArchiverDiagnosis) // throws
    // DevFailed,
    // ArchivingException
    {
	// Tools.trace ( "ControlResult/buildArchivers/START" ,
	// Warnable.LOG_LEVEL_DEBUG );

	// final Enumeration<String> errorArchiversKeys = _errorArchivers.;
	for (final String nextKey : _errorArchivers.keySet()) {

	    // while (errorArchiversKeys.hasMoreElements()) {
	    // final String nextKey = entry.getKey();
	    // Tools.trace ( "ControlResult/buildArchivers/nextKey|"+nextKey+"|"
	    // , Warnable.LOG_LEVEL_DEBUG );

	    final Archiver archiver = new Archiver();
	    archiver.setName(nextKey);
	    archiver.setDoDiagnosis(doArchiverDiagnosis);
	    archiver.buildKOAttributes(errorAttributes);
	    if (!archiver.hasKOAttributes()) {
		_errorArchivers.remove(nextKey);
		continue;
	    }

	    if (doArchiverDiagnosis) {
		final IDBReader attributesReader = DBReaderFactory.getCurrentImpl();

		boolean isAlive = false;
		try {
		    isAlive = attributesReader.isAlive(nextKey);
		} catch (final DevFailed e) {
		    Tools.trace("ControlResult/buildArchivers/failed calling isAlive for archiver|"
			    + nextKey + "|", e, Warnable.LOG_LEVEL_WARN);
		}
		archiver.setLiving(isAlive);

		if (isAlive) {
		    int scalarLoad = -1;
		    int spectrumLoad = -1;
		    int imageLoad = -1;
		    int totalLoad = -1;
		    boolean ok = true;

		    try {
			scalarLoad = attributesReader.getArchiverLoad(nextKey,
				IDBReader.LOAD_SCALAR);
			ok = false;
		    } catch (final DevFailed e) {
			Tools.trace(
				"ControlResult/buildArchivers/failed calling getArchiverLoad (LOAD_SCALAR) for archiver|"
					+ nextKey + "|", e, Warnable.LOG_LEVEL_WARN);
		    }

		    try {
			spectrumLoad = attributesReader.getArchiverLoad(nextKey,
				IDBReader.LOAD_SPECTRUM);
			ok = false;
		    } catch (final DevFailed e) {
			Tools.trace(
				"ControlResult/buildArchivers/failed calling getArchiverLoad (LOAD_SPECTRUM) for archiver|"
					+ nextKey + "|", e, Warnable.LOG_LEVEL_WARN);
		    }

		    try {
			imageLoad = attributesReader.getArchiverLoad(nextKey, IDBReader.LOAD_IMAGE);
			ok = false;
		    } catch (final DevFailed e) {
			Tools.trace(
				"ControlResult/buildArchivers/failed calling getArchiverLoad (LOAD_IMAGE) for archiver|"
					+ nextKey + "|", e, Warnable.LOG_LEVEL_WARN);
		    }

		    if (ok) {
			totalLoad = scalarLoad + spectrumLoad + imageLoad;
		    }

		    archiver.setScalarLoad(scalarLoad);
		    archiver.setSpectrumLoad(spectrumLoad);
		    archiver.setImageLoad(imageLoad);
		    archiver.setTotalLoad(totalLoad);

		    String status = "";
		    try {
			status = attributesReader.getDeviceStatus(nextKey);
		    } catch (final DevFailed e) {
			Tools.trace(
				"ControlResult/buildArchivers/failed calling getDeviceStatus for archiver|"
					+ nextKey + "|", e, Warnable.LOG_LEVEL_WARN);
		    }

		    archiver.setStatus(status);
		}
	    }

	    _errorArchivers.put(nextKey, archiver);
	}
	return _errorArchivers;
    }

    /**
     * @param ofControlledAttributes
     * @param ofKoAttributes
     */
    private int buildGlobalCode(final int _numberOfControlledAttributes,
	    final int _numberOfControlledAttributesWithSuccess, final int _numberOfKoAttributes) {
	if (_numberOfControlledAttributes == 0) {
	    return ControlResult.NO_ATTRIBUTES_TO_CONTROL;
	} else {
	    if (_numberOfControlledAttributesWithSuccess == 0) {
		return ControlResult.ALL_UNDETERMINED;
	    }
	    if (_numberOfKoAttributes == 0) {
		return ControlResult.ALL_OK;
	    } else if (_numberOfKoAttributes == _numberOfControlledAttributesWithSuccess) {
		return ControlResult.ALL_KO;
	    } else {
		return ControlResult.PARTIAL_KO;
	    }
	}
    }

    /**
     * Tests whether the CR contains KO attributes. The CR must have been built
     * first.
     * 
     * @return Whether the CR contains KO attributes.
     */
    public boolean hasErrors() {
	if (!isBuilt) {
	    throw new IllegalStateException("Build the ControlResult first.");
	}

	switch (getCode()) {
	case ControlResult.ALL_OK:
	    return false;

	case ControlResult.NO_ATTRIBUTES_TO_CONTROL:
	    return false;

	default:
	    return true;
	}
    }

    /**
     * Returns the list of all archivers that have KO attributes
     * 
     * @return A Hashtable which keys are the KO archivers names and which
     *         values are the corresponding Archiver objects.
     */
    public Map<String, Archiver> getErrorArchivers() {
	return errorArchivers;
    }

    /**
     * @param errorArchivers
     *            The errorArchivers to set.
     */
    private void setErrorArchivers(final Map<String, Archiver> errorArchivers) {
	this.errorArchivers = errorArchivers;
    }

    /**
     * Returns the list of all domains that have KO attributes
     * 
     * @return A Hashtable which keys are the KO domains names and which values
     *         are the corresponding Domain objects.
     */
    public Map<String, Domain> getErrorDomains() {
	return errorDomains;
    }

    /**
     * @param errorDomains
     *            The errorDomains to set.
     */
    private void setErrorDomains(final Map<String, Domain> errorDomains) {
	this.errorDomains = errorDomains;
    }

    /**
     * @return Returns the numberOfArchivingAttributes.
     */
    public int getNumberOfArchivingAttributes() {
	return numberOfArchivingAttributes;
    }

    /**
     * @param numberOfArchivingAttributes
     *            The numberOfArchivingAttributes to set.
     */
    public void setNumberOfArchivingAttributes(final int _numberOfArchivingAttributes) {
	numberOfArchivingAttributes = _numberOfArchivingAttributes;
    }

    /**
     * @return Returns the numberOfControlledAttributes.
     */
    public int getNumberOfControlledAttributes() {
	return numberOfControlledAttributes;
    }

    /**
     * @param numberOfControlledAttributes
     *            The numberOfControlledAttributes to set.
     */
    public void setNumberOfControlledAttributes(final int numberOfControlledAttributes) {
	this.numberOfControlledAttributes = numberOfControlledAttributes;
    }

    /**
     * @return Returns the numberOfKoAttributes.
     */
    public int getNumberOfKoAttributes() {
	return numberOfKoAttributes;
    }

    /**
     * @param numberOfKoAttributes
     *            The numberOfKoAttributes to set.
     */
    public void setNumberOfKoAttributes(final int numberOfKoAttributes) {
	this.numberOfKoAttributes = numberOfKoAttributes;
    }

    /**
     * Returns the date when the CR was completed
     * 
     * @return Returns the endDate.
     */
    public Timestamp getEndDate() {
	return endDate;
    }

    /**
     * Returns the date when the CR was started
     * 
     * @return Returns the startDate.
     */
    public Timestamp getStartDate() {
	return startDate;
    }

    /**
     * Adds the lines of another CR to this one
     * 
     * @param step
     * @param controlResult
     *            the other CR
     */
    public void addLines(final ControlResult controlResult2) {
	final Map<String, ControlResultLine> lines2 = controlResult2.lines;
	for (final ControlResultLine value : lines2.values()) {
	    addLine(value);
	}
    }

    /**
     * Clones a ControlResult object
     * 
     * @return A clone
     */
    public ControlResult cloneControlResult() {
	final ControlResult ret = new ControlResult();

	ret.lines = new HashMap<String, ControlResultLine>(lines);
	ret.code = code;

	Timestamp newDate;
	if (startDate == null) {
	    newDate = null;
	} else {
	    newDate = new Timestamp(startDate.getTime());
	}
	ret.startDate = newDate;

	if (endDate == null) {
	    newDate = null;
	} else {
	    newDate = new Timestamp(endDate.getTime());
	}
	ret.endDate = newDate;

	ret.errorArchivers = new HashMap<String, Archiver>(errorArchivers);
	ret.errorAttributes = new HashMap<String, ArchivingAttribute>(errorAttributes);
	ret.errorDomains = new HashMap<String, Domain>(errorDomains);
	ret.errorSubAttributes = new HashMap<String, ArchivingAttributeSubName>(errorSubAttributes);
	ret.okAttributes = new HashMap<String, ArchivingAttribute>(okAttributes);

	ret.isBuilt = isBuilt;

	ret.numberOfArchivingAttributes = numberOfArchivingAttributes;
	ret.numberOfControlledAttributes = numberOfControlledAttributes;
	ret.numberOfKoAttributes = numberOfKoAttributes;
	ret.numberOfAttributesControlledWithSuccess = numberOfAttributesControlledWithSuccess;
	ret.m_numberOfAttributesControlledWithSuccessWithNullValue = m_numberOfAttributesControlledWithSuccessWithNullValue;

	ret.timeApplyingChoosingStrategy = timeApplyingChoosingStrategy;
	ret.timeControlling = timeControlling;
	ret.timeReadingArchivingAttributes = timeReadingArchivingAttributes;
	ret.totalTime = totalTime;

	ret.cycles = cycles;
	ret.steps = steps;

	return ret;
    }

    /**
     * Returns the list of all attribute names that have KO attributes
     * 
     * @return A Hashtable which keys are the attribute names and which values
     *         are empty.
     */
    public Map<String, ArchivingAttributeSubName> getErrorSubAttributes() {
	return errorSubAttributes;
    }

    /**
     * @param errorSubAttributes
     *            The errorSubAttributes to set.
     */
    private void setErrorSubAttributes(
	    final Map<String, ArchivingAttributeSubName> errorSubAttributes) {
	this.errorSubAttributes = errorSubAttributes;
    }

    /**
     * Returns whether this CR has been built yet
     * 
     * @return Returns the isBuilt.
     */
    public boolean isBuilt() {
	return isBuilt;
    }

    /**
     * @return Returns the numberOfUndeterminedAttributes.
     */
    public int getNumberOfUndeterminedAttributes() {
	return numberOfUndeterminedAttributes;
    }

    /**
     * @param numberOfUndeterminedAttributes
     *            The numberOfUndeterminedAttributes to set.
     */
    private void setNumberOfUndeterminedAttributes(final int numberOfUndeterminedAttributes) {
	this.numberOfUndeterminedAttributes = numberOfUndeterminedAttributes;
    }

    /**
     * @return Returns the numberOfAttributesControlledWithSuccess.
     */
    public int getNumberOfAttributesControlledWithSuccess() {
	return numberOfAttributesControlledWithSuccess;
    }

    /**
     * @param numberOfAttributesControlledWithSuccess
     *            The numberOfAttributesControlledWithSuccess to set.
     */
    private void setNumberOfAttributesControlledWithSuccess(
	    final int numberOfAttributesControlledWithSuccess) {
	this.numberOfAttributesControlledWithSuccess = numberOfAttributesControlledWithSuccess;
    }

    /**
     * @return Returns the
     *         m_numberOfAttributesControlledWithSuccessWithNullValue.
     */
    public int getNumberOfAttributesControlledWithSuccessWithNullValue() {
	return m_numberOfAttributesControlledWithSuccessWithNullValue;
    }

    /**
     * @param numberOfAttributesControlledWithSuccessWithNullValue
     *            The numberOfAttributesControlledWithSuccessWithNullValue to
     *            set.
     */
    private void setNumberOfAttributesControlledWithSuccessWithNullValue(
	    final int numberOfAttributesControlledWithSuccessWithNullValue) {
	m_numberOfAttributesControlledWithSuccessWithNullValue = numberOfAttributesControlledWithSuccessWithNullValue;
    }

    /**
     * @return Returns the isFullCycle.
     */
    public boolean isFullCycle() {
	return isFullCycle;
    }

    /**
     * @param startDate
     *            The startDate to set.
     */
    public void setStartDate(final Timestamp startDate) {
	this.startDate = startDate;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(final Timestamp endDate) {
	this.endDate = endDate;
    }

    public void setCompletedCycles(final int _cycles) {
	cycles = _cycles;
    }

    public void setCompletedSteps(final int _steps) {
	steps = _steps;
    }

    /**
     * @return Returns the cycles.
     */
    public int getCycles() {
	return cycles;
    }

    /**
     * @param cycles
     *            The cycles to set.
     */
    public void setCycles(final int _cycles) {
	cycles = _cycles;
    }

    /**
     * @return Returns the steps.
     */
    public int getSteps() {
	return steps;
    }

    /**
     * @param steps
     *            The steps to set.
     */
    public void setSteps(final int _steps) {
	System.out.println("CLA/ControlResult/" + hashCode() + "/setSteps/_steps/" + _steps);
	steps = _steps;
    }

    public ControlResultLine[] sort() {
	ControlResultLine[] ret = new ControlResultLine[lines.size()];
	final Iterator<ControlResultLine> it = lines.values().iterator();
	int i = 0;

	while (it.hasNext()) {
	    final ControlResultLine next = it.next();
	    ret[i] = next;
	    i++;
	}

	final List<ControlResultLine> list = Arrays.asList(ret);
	Collections.sort(list, linesComparator);
	ret = list.toArray(ret);

	return ret;
    }

    public void removeOldLines(final Set<String> namesOfAttributesToControl)// CLA
    // 14/12/06
    {
	// System.out.println (
	// "CLA/removeOldLines/namesOfAttributesToControl|"+namesOfAttributesToControl.size()
	// );
	// final Enumeration<String> it = lines.keys();
	// while (it.hasMoreElements()) {
	for (final String attribute : namesOfAttributesToControl) {
	    // final ControlResultLine next = lines.get(attribute);

	    // if ( next.isOlderThan ( step , cycle ) )
	    if (!namesOfAttributesToControl.contains(attribute)) {
		// System.out.println ( "CLA/removeOldLines/removing|"+nextName
		// );
		lines.remove(attribute);
		errorAttributes.remove(attribute);// CLA 22/01/07
	    }
	}
    }
}
