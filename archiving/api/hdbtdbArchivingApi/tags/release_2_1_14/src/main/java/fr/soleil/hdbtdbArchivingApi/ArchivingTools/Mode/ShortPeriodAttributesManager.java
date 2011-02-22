package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode;

import java.util.HashMap;
import java.util.Map;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * Handles comparisons between the initialized list of short period attributes,
 * and other attributes
 * 
 * @author awo
 */
public class ShortPeriodAttributesManager {

    private static final String         m_shortAttributesListProperty = "shortPeriodAttributes";
    private static final String         SEPARATOR                     = ",";
    private static final String         DEFAULT_INIT_LIST_REASON      = "Failed while executing ShortPeriodAttributeManager.initShortPeriodAttributes()...";

    /**
     * Map containing short period attributes: key: the full name of the
     * attribute value: the period in seconds
     */
    private static Map<String, Integer> shortPeriodAttributes;

    /**
     * Initializes the shortPeriodAttributes map with the contents of the
     * property 'shortPeriodAttributes' in the HdbArchiver device class. It
     * stops the launch of HdbArchiver device when the short attributes map is
     * not formatted correctly : period that isn't between 0 and 10 excluded, or
     * syntax error
     */
    private static void initShortPeriodAttributes() throws ArchivingException {

        shortPeriodAttributes = new HashMap<String, Integer>();
        String cause, message, desc;

        try {
            String[] shortPeriodAttributesArray = GetConf
                    .readStringArrayInDB(ConfigConst.HDB_CLASS_DEVICE,
                            m_shortAttributesListProperty);
            if (shortPeriodAttributesArray != null) {
                for (String attribute : shortPeriodAttributesArray) {
                    if ((attribute != null) && (!attribute.trim().isEmpty())) {
                        String temp = attribute;
                        int delimiterCount = 0;
                        while (temp.indexOf(",") > -1) {
                            // Doing so, we are sure to test all bad cases, even
                            // the ones not detectable by split
                            delimiterCount++;
                            if (delimiterCount > 1) {
                                temp = null;
                                // property is not well formatted (too many
                                // separators)
                                throw generateDefaultShortPeriodAttributeException();
                            }
                            temp = temp.replaceFirst(",", "");
                        }
                        temp = null;
                        String[] attributeParts = attribute.split(SEPARATOR);
                        if (attributeParts.length > 2) {
                            // property is not well formatted (too many
                            // separators)
                            throw generateDefaultShortPeriodAttributeException();
                        } else {
                            String attributeName = attributeParts[0].trim()
                                    .toLowerCase();
                            if (attributeName.isEmpty()) {
                                cause = "INVALID_NAME";
                                message = GlobalConst.ARCHIVING_ERROR_PREFIX
                                        + cause;
                                desc = "Some attributes in the short period attributes list have an invalid name (should not be empty)";
                                throw new ArchivingException(message,
                                        DEFAULT_INIT_LIST_REASON,
                                        ErrSeverity.ERR, desc, "");
                            } else if (shortPeriodAttributes
                                    .containsKey(attributeName)) {
                                cause = "DUPLICATED_NAME";
                                message = GlobalConst.ARCHIVING_ERROR_PREFIX
                                        + cause;
                                desc = "Some attributes appear at least twice in the short period attributes list";
                                throw new ArchivingException(message,
                                        DEFAULT_INIT_LIST_REASON,
                                        ErrSeverity.ERR, desc, "");
                            } else {
                                Integer attributePeriod = Integer
                                        .parseInt(attributeParts[1]);
                                if (attributePeriod > 0 && attributePeriod < 10) {
                                    shortPeriodAttributes.put(attributeName,
                                            attributePeriod);
                                } else {
                                    cause = "WRONG_PERIOD_VALUE";
                                    message = GlobalConst.ARCHIVING_ERROR_PREFIX
                                            + cause;
                                    desc = "Some attributes in the short period attributes list have a wrong period value (should be between 0 and 10 excluded)";
                                    throw new ArchivingException(message,
                                            DEFAULT_INIT_LIST_REASON,
                                            ErrSeverity.ERR, desc, "");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof ArchivingException) {
                // We do this in order to handle ArchivingExceptions and other
                // Exceptions separately
                throw (ArchivingException) e;
            } else {
                // We convert other Exceptions into ArchivingExceptions
                throw generateDefaultShortPeriodAttributeException();
            }
        } finally {
            cause = null;
            message = null;
            desc = null;
        }
    }

    private static ArchivingException generateDefaultShortPeriodAttributeException() {
        return new ArchivingException(
                GlobalConst.ARCHIVING_ERROR_PREFIX + "WRONG_FORMAT",
                DEFAULT_INIT_LIST_REASON,
                ErrSeverity.ERR,
                "The shortPeriodAttributes property is not correctly formatted",
                "");
    }

    /**
     * Gets the current instance of the map, and initializes it if it's null
     */
    public static Map<String, Integer> getShortPeriodAttributes()
            throws ArchivingException {
        if (shortPeriodAttributes == null)
            initShortPeriodAttributes();
        return shortPeriodAttributes;
    }

    /**
     * For JUnit purpose only
     */
    public static void setShortPeriodAttributes(
            Map<String, Integer> shortPeriodAttributes) {
        ShortPeriodAttributesManager.shortPeriodAttributes = shortPeriodAttributes;
    }

    /**
     * Return true if the input attribute name is in the shortPeriodAttributes
     * map
     */
    public static boolean isShortPeriodAttribute(String attributeName)
            throws ArchivingException {
        if (attributeName == null)
            return false;
        return getShortPeriodAttributes().containsKey(
                attributeName.toLowerCase());
    }

    /**
     * Returns the corresponding period value to the input attribute name
     */
    public static Integer getPeriodFromShortAttributeName(String attributeName)
            throws ArchivingException {
        if (attributeName == null)
            return null;
        return getShortPeriodAttributes().get(attributeName.toLowerCase());
    }
}
