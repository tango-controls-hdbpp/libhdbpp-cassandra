// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotAttributeValue.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotAttributeValue.
// (Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.19 $
//
// $Log: SnapshotAttributeValue.java,v $
// Revision 1.19 2007/08/30 14:33:28 ounsy
// small bug correction
//
// Revision 1.18 2007/08/23 12:59:23 ounsy
// toUserFriendlyString() available in SnapshotAttributeValue
//
// Revision 1.17 2007/07/03 08:39:30 ounsy
// * managing null value
//
// Revision 1.16 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.15 2007/06/28 12:51:00 ounsy
// spectrum and image values represented as arrays
//
// Revision 1.14 2007/02/28 11:18:18 ounsy
// better snapshot file management
//
// Revision 1.13 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.12 2006/06/28 12:51:06 ounsy
// image support
//
// Revision 1.11 2006/06/16 08:52:57 ounsy
// ready for images
//
// Revision 1.10 2006/04/13 12:37:33 ounsy
// new spectrum types support
//
// Revision 1.9 2006/03/20 15:51:04 ounsy
// added the case of Snapshot delta value for spectrums which
// read and write parts are the same length.
//
// Revision 1.8 2006/03/16 16:40:52 ounsy
// taking care of String formating
//
// Revision 1.7 2006/03/15 15:08:36 ounsy
// boolean scalar management
//
// Revision 1.6 2006/02/15 09:21:27 ounsy
// minor changes
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:38 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.data.snapshot;

import java.util.Locale;
import java.util.StringTokenizer;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.comete.util.Format;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.StringFormater;

/**
 * All snapshot attributes values, be it read write or delta, extend this class that implements
 * basic cast operations, value tests, and String representation.
 * 
 * @author CLAISSE
 */
public class SnapshotAttributeValue {
    protected int dataFormat;
    protected int dataType;

    /**
     * The XML tag name used in saving/loading
     */
    public static final String NOT_APPLICABLE_XML_TAG = "notApplicable";

    /**
     * The data format used for "not applicable" values, ie. values that are meaningless (such as
     * the delta value of a read-only attribute)
     */
    public static final int NOT_APPLICABLE_DATA_FORMAT = -1;

    /**
     * The data format used for scalar values, ie. 0-dimensionnal values
     */
    public static final int SCALAR_DATA_FORMAT = 0;

    /**
     * The data format used for spectrum values, ie. 1-dimensionnal values
     */
    public static final int SPECTRUM_DATA_FORMAT = 1;

    /**
     * The data format used for image values, ie. 2-dimensionnal values
     */
    public static final int IMAGE_DATA_FORMAT = 2;

    /**
     * The attribute value
     */
    protected Object value;

    /**
     * The display format in case of scalar value
     */
    protected String display_format;

    /**
     * The attribute value, if it's a scalar
     */
    protected Object scalarValue;

    /**
     * The attribute value, if it's a spectrum
     */
    protected Object[] spectrumValue;

    /**
     * The attribute value, if it's an image
     */
    protected Object[][] imageValue;

    /**
     * True if the value is a "not applicable" value, ie. a value that is meaningless (such as the
     * delta value of a read-only attribute)
     */
    protected boolean isNotApplicable = false;

    /**
     * True if the value has been user-modified
     */
    protected boolean isModified = false;

    /**
     * Separator used for spectrum and images values
     */
    public static final String X_SEPARATOR = ",";

    /**
     * Separator used for images values (not used yet)
     */
    public static final String Y_SEPARATOR = "|";

    /**
     * Returns a formated representation of the value
     */
    public String toFormatedString() {
        String ret = "";

        switch (this.dataFormat) {
            case SCALAR_DATA_FORMAT:
                if (scalarValue != null) {
                    String formatedValue = "";
                    if (scalarValue instanceof String) {
                        ret += StringFormater.formatStringToRead(new String((String) scalarValue));
                    }
                    else if (display_format == null || "".equals(display_format)
                            || this.dataType == TangoConst.Tango_DEV_BOOLEAN
                            || !(scalarValue instanceof Number)) {
                        ret += scalarValue;
                    }
                    else {
                        try {
                            formatedValue = Format.format(Locale.UK, display_format,
                                    (Number) scalarValue);
                            if (formatedValue == null) {
                                ret += scalarValue;
                            }
                            else {
                                Double val = null;
                                try {
                                    val = Double.valueOf(formatedValue);
                                }
                                catch (Exception e) {
                                    val = null;
                                }
                                if (val == null) {
                                    ret += scalarValue;
                                }
                                else {
                                    ret += formatedValue;
                                }
                                val = null;
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            ret += scalarValue;
                        }
                    }
                }
                break;

            case SPECTRUM_DATA_FORMAT:
                if (spectrumValue != null) {
                    if (spectrumValue.length > 0 && !"NaN".equals(spectrumValue[0])
                            && !"[NaN]".equals(spectrumValue[0])) {
                        ret += "NaN";
                        break;
                    }
                    int dimX = 0;
                    switch (dataType) {

                        case TangoConst.Tango_DEV_STRING:
                            String[] stval = (String[]) spectrumValue;
                            dimX = stval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += StringFormater.formatStringToRead(new String(stval[i]));
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_DOUBLE:
                            Double[] dval = (Double[]) spectrumValue;
                            dimX = dval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + dval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_FLOAT:
                            Float[] fval = (Float[]) spectrumValue;
                            dimX = fval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + fval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_USHORT:
                        case TangoConst.Tango_DEV_SHORT:
                            Short[] sval = (Short[]) spectrumValue;
                            dimX = sval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + sval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_UCHAR:
                        case TangoConst.Tango_DEV_CHAR:
                            Byte[] cval = (Byte[]) spectrumValue;
                            dimX = cval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + cval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_STATE:
                        case TangoConst.Tango_DEV_ULONG:
                        case TangoConst.Tango_DEV_LONG:
                            Integer[] lval = (Integer[]) spectrumValue;
                            dimX = lval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + lval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        case TangoConst.Tango_DEV_BOOLEAN:
                            Boolean[] bval = (Boolean[]) spectrumValue;
                            dimX = bval.length;
                            for (int i = 0; i < dimX; i++) {
                                ret += ("" + bval[i]);
                                if (i < dimX - 1) {
                                    ret += X_SEPARATOR;
                                }
                            }
                            break;

                        default:
                            scalarValue = value;

                    } // end switch (dataType)
                }
                else {
                    ret += "NaN";
                }

                break;

            case IMAGE_DATA_FORMAT:
                /*
                 * if ( imageValue != null ) { int dimX = imageValue.length; for
                 * ( int i = 0 ; i < dimX ; i++ ) { Object[] y = imageValue[ i
                 * ]; int dimY = y.length; for ( int j = 0 ; j < dimX ; j++ ) {
                 * ret += ( "" + imageValue[ i ][ j ] ); if ( j < dimY - 1 ) {
                 * ret += Y_SEPARATOR; }
                 * 
                 * }
                 * 
                 * if ( i < dimX - 1 ) { ret += X_SEPARATOR; } } }
                 */
                break;
        }

        ret = ret.trim();
        return ret;
    }

    /**
     * Returns a XML representation of the value.
     * 
     * @return a XML representation of the value
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        appendToString(buffer);
        return buffer.toString().trim();
    }

    public void appendToString(StringBuffer buffer) {
        if (buffer != null) {

            switch (this.dataFormat) {
                case SCALAR_DATA_FORMAT:
                    if (scalarValue != null) {
                        buffer.append(scalarValue);
                    }
                    break;

                case SPECTRUM_DATA_FORMAT:
                    if (spectrumValue != null && spectrumValue.length > 0
                            && !"NaN".equals(spectrumValue[0]) && !"[NaN]".equals(spectrumValue[0])) {
                        int dimX = 0;
                        switch (dataType) {

                            case TangoConst.Tango_DEV_STRING:
                                String[] stval = (String[]) spectrumValue;
                                dimX = stval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(StringFormater.formatStringToRead(new String(
                                            stval[i])));
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_DOUBLE:
                                Double[] dval = (Double[]) spectrumValue;
                                dimX = dval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(dval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_FLOAT:
                                Float[] fval = (Float[]) spectrumValue;
                                dimX = fval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(fval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_SHORT:
                                Short[] sval = (Short[]) spectrumValue;
                                dimX = sval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(sval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_CHAR:
                                Byte[] cval = (Byte[]) spectrumValue;
                                dimX = cval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(cval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_STATE:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_LONG:
                                Integer[] lval = (Integer[]) spectrumValue;
                                dimX = lval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(lval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            case TangoConst.Tango_DEV_BOOLEAN:
                                Boolean[] bval = (Boolean[]) spectrumValue;
                                dimX = bval.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(bval[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                            default:
                                dimX = spectrumValue.length;
                                for (int i = 0; i < dimX; i++) {
                                    buffer.append(spectrumValue[i]);
                                    if (i < dimX - 1) {
                                        buffer.append(X_SEPARATOR);
                                    }
                                }
                                break;

                        } // end switch (dataType)
                    }
                    else {
                        buffer.append("NaN");
                    }
                    break;

                case IMAGE_DATA_FORMAT:
                    if (imageValue != null) {
                        for (int i = 0; i < imageValue.length; i++) {
                            for (int j = 0; j < imageValue[i].length; j++) {
                                buffer.append(imageValue[i][j]);
                                if (j < imageValue[i].length - 1) {
                                    buffer.append(Y_SEPARATOR);
                                }

                            }
                            if (i < imageValue.length - 1) {
                                buffer.append(X_SEPARATOR);
                            }
                        }
                    }
                    break;
            }

        }
    }

    /**
     * Sets the value attribute, and depending on the data format, the scalarValue, imageValue, or
     * spectrumValue.
     * 
     * @param _value The raw value
     */
    public void setValue(Object _value) {
        this.value = _value;
        switch (dataFormat) {
            case SCALAR_DATA_FORMAT:
                if (value instanceof String) {
                    try {
                        String stringValue = ((String) value).trim();
                        Double doubleValue = null;

                        try {
                            doubleValue = Double.valueOf(stringValue);
                        }
                        catch (Exception e) {
                            doubleValue = null;
                        }

                        switch (dataType) {

                            case TangoConst.Tango_DEV_STRING:
                                this.scalarValue = stringValue;
                                break;

                            case TangoConst.Tango_DEV_DOUBLE:
                                this.scalarValue = doubleValue;
                                break;

                            case TangoConst.Tango_DEV_FLOAT:
                                this.scalarValue = new Float(doubleValue.floatValue());
                                break;

                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_SHORT:
                                this.scalarValue = new Short(doubleValue.shortValue());
                                break;

                            case TangoConst.Tango_DEV_STATE:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_LONG:
                                this.scalarValue = new Integer(doubleValue.intValue());
                                break;

                            case TangoConst.Tango_DEV_BOOLEAN:
                                this.scalarValue = Boolean.valueOf(stringValue);
                                break;

                            default:
                                scalarValue = value;

                        } // end switch (dataType)
                    }
                    catch (Exception e) {
                        scalarValue = value;
                    }
                } // end if (value instanceof String)
                else {
                    scalarValue = value;
                }
                break;

            case SPECTRUM_DATA_FORMAT:
                if (value instanceof String) {
                    try {
                        String stringValue = ((String) value).trim();
                        if (stringValue == null || "".equals(stringValue)) {
                            spectrumValue = null;
                            break;
                        }
                        StringTokenizer tokenizer = new StringTokenizer(stringValue, ",");
                        int length = tokenizer.countTokens();

                        switch (dataType) {

                            case TangoConst.Tango_DEV_STRING:
                                String[] strArray = new String[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        strArray[i] = null;
                                    }
                                    else {
                                        strArray[i] = token;
                                    }
                                    token = null;
                                }
                                this.spectrumValue = strArray;
                                break;

                            case TangoConst.Tango_DEV_DOUBLE:
                                Double[] dbArray = new Double[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        dbArray[i] = null;
                                    }
                                    else {
                                        dbArray[i] = Double.valueOf(token);
                                    }
                                    token = null;
                                }
                                this.spectrumValue = dbArray;
                                break;

                            case TangoConst.Tango_DEV_FLOAT:
                                Float[] flArray = new Float[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        flArray[i] = null;
                                    }
                                    else {
                                        flArray[i] = Float.valueOf(token);
                                    }
                                    token = null;
                                }
                                this.spectrumValue = flArray;
                                break;

                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_SHORT:
                                Short[] shArray = new Short[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        shArray[i] = null;
                                    }
                                    else {
                                        shArray[i] = new Short(Double.valueOf(token).shortValue());
                                    }
                                    token = null;
                                }
                                this.spectrumValue = shArray;
                                break;

                            case TangoConst.Tango_DEV_STATE:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_LONG:
                                Integer[] intArray = new Integer[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        intArray[i] = null;
                                    }
                                    else {
                                        intArray[i] = new Integer(Double.valueOf(token).intValue());
                                    }
                                    token = null;
                                }
                                this.spectrumValue = intArray;
                                break;

                            case TangoConst.Tango_DEV_BOOLEAN:
                                Boolean[] boolArray = new Boolean[length];
                                for (int i = 0; i < length; i++) {
                                    String token = tokenizer.nextToken();
                                    if ("".equals(token) || "null".equals(token)) {
                                        boolArray[i] = null;
                                    }
                                    else {
                                        boolArray[i] = new Boolean("true".equalsIgnoreCase(token));
                                    }
                                    token = null;
                                }
                                this.spectrumValue = boolArray;
                                break;

                            default:
                                spectrumValue = new Object[] { value };

                        } // end switch (dataType)
                    }
                    catch (Exception e) {
                        spectrumValue = new Object[] { value };
                    }
                } // end if (value instanceof String)
                else {
                    if (value == null) {
                        this.spectrumValue = null;
                    }
                    else if (value instanceof Object[]) {
                        this.spectrumValue = (Object[]) value;
                    }
                    else
                        this.spectrumValue = new Object[] { value };
                }
                break;

            case IMAGE_DATA_FORMAT:
                if (value == null) {
                    this.setImageValue(null);
                }
                else if (value instanceof Object[][]) {
                    this.setImageValue((Object[][]) value);
                }
                else
                    this.setImageValue(new Object[][] { { value } });
                break;

            case NOT_APPLICABLE_DATA_FORMAT:
                this.setNotApplicable(true);
                break;

            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Builds a SnapshotAttributeValue given its format, type, and value.
     * 
     * @param _dataFormat The data format ( scalar, spectrum, or image )
     * @param _dataType The Tango data type
     * @param _value The value content
     * @throws IllegalArgumentException if dataFormat is not in
     *             (SCALAR_DATA_FORMAT,SPECTRUM_DATA_FORMAT
     *             ,IMAGE_DATA_FORMAT,NOT_APPLICABLE_DATA_FORMAT)
     */
    public SnapshotAttributeValue(int _dataFormat, int _dataType, Object _value)
            throws IllegalArgumentException {
        this.dataFormat = _dataFormat;
        this.dataType = _dataType;

        switch (this.dataFormat) {

            case NOT_APPLICABLE_DATA_FORMAT:
                this.setNotApplicable(true);
            case SCALAR_DATA_FORMAT:
            case SPECTRUM_DATA_FORMAT:
            case IMAGE_DATA_FORMAT:
                setValue(_value);
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Tests whether a value is coherent with the data type
     * 
     * @param value The value to test
     * @return True if it is, false otherwise
     */
    public boolean validateValue(Object value) {
        if (value == null) {
            return false;
        }

        try {
            SnapshotAttributeValue.castValue(value, this.getDataType());
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
        catch (Exception e) {
            ILogger logger = LoggerFactory.getCurrentImpl();
            logger.trace(ILogger.LEVEL_CRITIC, e);
            System.out.println("Abnormal case in validateValue");

            return false;
        }

        return true;
    }

    /**
     * @return Returns the isModified.
     */
    public boolean isModified() {
        return isModified;
    }

    /**
     * @param isModified The isModified to set.
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    /**
     * Casts a value content to its Tango type.
     * 
     * @param value The value content
     * @param _data_type The Tango type
     */
    public static Object castValue(Object value, int _data_type) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            return value;
        }
        String s;
        switch (_data_type) {
            case TangoConst.Tango_DEV_STRING:
                return (String) value;
            case TangoConst.Tango_DEV_STATE:
                try {
                    return (Integer) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Integer(Integer.parseInt(s));
                }
            case TangoConst.Tango_DEV_UCHAR:
                try {
                    return (Byte) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Byte(Byte.parseByte(s));
                }
            case TangoConst.Tango_DEV_LONG:
                try {
                    return (Integer) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Integer(Integer.parseInt(s));
                }
            case TangoConst.Tango_DEV_ULONG:
                try {
                    return (Integer) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Integer(Integer.parseInt(s));
                }
            case TangoConst.Tango_DEV_BOOLEAN:
                try {
                    return (Boolean) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return Boolean.valueOf(s);
                }
            case TangoConst.Tango_DEV_SHORT:
                try {
                    return (Short) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Short(Short.parseShort(s));
                }
            case TangoConst.Tango_DEV_FLOAT:
                try {
                    return (Float) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Float(Float.parseFloat(s));
                }
            case TangoConst.Tango_DEV_DOUBLE:
                try {
                    return (Double) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Double(Double.parseDouble(s));
                }
            default:
                try {
                    return (Double) value;
                }
                catch (ClassCastException e) {
                    s = (String) value;
                    return new Double(Double.parseDouble(s));
                }
        }
    }

    /**
     * @return Returns the imageValue.
     */
    public Object[][] getImageValue() {
        return imageValue;
    }

    /**
     * @param imageValue The imageValue to set.
     */
    public void setImageValue(Object[][] imageValue) {
        if (imageValue != null && !"NaN".equals(imageValue[0][0])
                && !"[NaN]".equals(imageValue[0][0])) {
            this.imageValue = imageValue;
        }
        else
            this.imageValue = null;
    }

    /**
     * @return Returns the scalarValue.
     */
    public Object getScalarValue() {
        return scalarValue;
    }

    /**
     * @param scalarValue The scalarValue to set.
     */
    public void setScalarValue(Object scalarValue) {
        this.scalarValue = scalarValue;
    }

    /**
     * @return Returns the spectrumValue.
     */
    public Object[] getSpectrumValue() {
        return spectrumValue;
    }

    /**
     * @param spectrumValue The spectrumValue to set.
     */
    public void setSpectrumValue(Object[] spectrumValue) {
        if (spectrumValue != null && !"NaN".equals(spectrumValue[0])
                && !"[NaN]".equals(spectrumValue[0])) {
            this.spectrumValue = spectrumValue;
        }
        else
            this.spectrumValue = null;
    }

    /**
     * @return Returns the value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return Returns the dataFormat.
     */
    public int getDataFormat() {
        return dataFormat;
    }

    /**
     * @param dataFormat The dataFormat to set.
     */
    public void setDataFormat(int dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     * @return Returns the dataType.
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * @param dataType The dataType to set.
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * @return Returns the isNotApplicable.
     */
    public boolean isNotApplicable() {
        return isNotApplicable;
    }

    /**
     * @param isNotApplicable The isNotApplicable to set.
     */
    public void setNotApplicable(boolean isNotApplicable) {
        this.isNotApplicable = isNotApplicable;
    }

    /**
     * @return the display format in case of scalar value
     */
    public String getDisplayFormat() {
        return this.display_format;
    }

    /**
     * Sets the display format in case of scalar value
     * 
     * @param format the display format
     */
    public void setDisplayFormat(String format) {
        this.display_format = format;
        // updates value formating
        if (this.value != null) {
            this.setValue(this.value);
        }
    }

    /**
     * @return
     */
    public String toUserFriendlyString() {
        String ret = "";

        ret += toString();

        return ret;
    }

}
