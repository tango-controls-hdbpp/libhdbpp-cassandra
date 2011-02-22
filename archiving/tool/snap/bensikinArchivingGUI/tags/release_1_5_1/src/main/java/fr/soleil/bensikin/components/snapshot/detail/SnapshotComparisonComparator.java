package fr.soleil.bensikin.components.snapshot.detail;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.BensikinComparator;
import fr.soleil.bensikin.data.snapshot.SnapshotAttribute;
import fr.soleil.bensikin.data.snapshot.SnapshotComparison;

/**
 * Implements a Comparator on SnapshotAttribute objects, useful to sort the
 * snapshot details list. The comparison can be on any one of the
 * SnapshotAttribute fields, depending on the type specified on constructing the
 * comparator. Empty or null values are considered lowest.
 * 
 * @author CLAISSE
 */
public class SnapshotComparisonComparator extends
        BensikinComparator<SnapshotComparison> {

    /**
     * The comparison will be on the first SnapshotAttribute's name field
     */
    public static final int COMPARE_FIRST_NAME         = 0;
    /**
     * The comparison will be on the second SnapshotAttribute's name field
     */
    public static final int COMPARE_SECOND_NAME        = 1;
    /**
     * The comparison will be on the difference SnapshotAttribute's name field
     */
    public static final int COMPARE_DIFF_NAME          = 2;

    /**
     * The comparison will be on the first SnapshotAttribute's write value field
     */
    public static final int COMPARE_FIRST_WRITE_VALUE  = 3;
    /**
     * The comparison will be on the second SnapshotAttribute's write value
     * field
     */
    public static final int COMPARE_SECOND_WRITE_VALUE = 4;
    /**
     * The comparison will be on the difference SnapshotAttribute's write value
     * field
     */
    public static final int COMPARE_DIFF_WRITE_VALUE   = 5;

    /**
     * The comparison will be on the first SnapshotAttribute's read value field
     */
    public static final int COMPARE_FIRST_READ_VALUE   = 6;
    /**
     * The comparison will be on the second SnapshotAttribute's read value field
     */
    public static final int COMPARE_SECOND_READ_VALUE  = 7;
    /**
     * The comparison will be on the difference SnapshotAttribute's read value
     * field
     */
    public static final int COMPARE_DIFF_READ_VALUE    = 8;

    /**
     * The comparison will be on the first SnapshotAttribute's delta value field
     */
    public static final int COMPARE_FIRST_DELTA_VALUE  = 9;
    /**
     * The comparison will be on the second SnapshotAttribute's delta value
     * field
     */
    public static final int COMPARE_SECOND_DELTA_VALUE = 10;
    /**
     * The comparison will be on the difference SnapshotAttribute's delta value
     * field
     */
    public static final int COMPARE_DIFF_DELTA_VALUE   = 11;

    /**
     * Builds a comparator on the desired SnapshotData field
     * 
     * @param _fieldToCompare
     *            The field on which the comparison will be done
     * @throws IllegalArgumentException
     *             If _fieldToCompare isn't in (COMPARE_ID, COMPARE_TIME,
     *             COMPARE_COMMENT)
     */
    public SnapshotComparisonComparator(int _fieldToCompare)
            throws IllegalArgumentException {
        super(_fieldToCompare);
        if (_fieldToCompare != COMPARE_FIRST_NAME
                && _fieldToCompare != COMPARE_SECOND_NAME
                && _fieldToCompare != COMPARE_DIFF_NAME
                && _fieldToCompare != COMPARE_FIRST_WRITE_VALUE
                && _fieldToCompare != COMPARE_SECOND_WRITE_VALUE
                && _fieldToCompare != COMPARE_DIFF_WRITE_VALUE
                && _fieldToCompare != COMPARE_FIRST_READ_VALUE
                && _fieldToCompare != COMPARE_SECOND_READ_VALUE
                && _fieldToCompare != COMPARE_DIFF_READ_VALUE
                && _fieldToCompare != COMPARE_FIRST_DELTA_VALUE
                && _fieldToCompare != COMPARE_SECOND_DELTA_VALUE
                && _fieldToCompare != COMPARE_DIFF_DELTA_VALUE) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int compare(SnapshotComparison data1, SnapshotComparison data2) {
        if (data1 == null || data2 == null) return 0;
        int ret = 0;
        SnapshotAttribute attr1 = null;
        switch (fieldToCompare) {

            case COMPARE_FIRST_NAME:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_1);
                ret = compareNames(data1, data2,
                        SnapshotComparison.SNAPSHOT_TYPE_1);
                break;
            case COMPARE_SECOND_NAME:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_2);
                ret = compareNames(data1, data2,
                        SnapshotComparison.SNAPSHOT_TYPE_2);
                break;
            case COMPARE_DIFF_NAME:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_DIFF);
                ret = compareNames(data1, data2,
                        SnapshotComparison.SNAPSHOT_TYPE_DIFF);
                break;

            case COMPARE_FIRST_WRITE_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_1);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                }
                break;
            case COMPARE_SECOND_WRITE_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_2);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                }
                break;
            case COMPARE_DIFF_WRITE_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_DIFF);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_WRITE_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_WRITE_TYPE);
                        break;
                }
                break;

            case COMPARE_FIRST_READ_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_1);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                }
                break;
            case COMPARE_SECOND_READ_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_2);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                }
                break;
            case COMPARE_DIFF_READ_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_DIFF);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_READ_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_READ_TYPE);
                        break;
                }
                break;

            case COMPARE_FIRST_DELTA_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_1);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_1,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_1,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                }
                break;
            case COMPARE_SECOND_DELTA_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_2);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_2,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_2,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                }
                break;
            case COMPARE_DIFF_DELTA_VALUE:
                attr1 = data1
                        .getSnapshotAttribute(SnapshotComparison.SNAPSHOT_TYPE_DIFF);
                switch (attr1.getData_format()) {
                    case AttrDataFormat._SCALAR:
                        switch (attr1.getData_type()) {
                            case TangoConst.Tango_DEV_BOOLEAN:
                                ret = compareBooleanScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                ret = compareStringScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                ret = compareNumberScalars(data1, data2,
                                        SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                        SnapshotComparison.COLUMN_DELTA_TYPE);
                                break;
                        }
                        break;
                    case AttrDataFormat._SPECTRUM:
                        ret = compareSpectrums(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                    case AttrDataFormat._IMAGE:
                        ret = compareImages(data1, data2,
                                SnapshotComparison.SNAPSHOT_TYPE_DIFF,
                                SnapshotComparison.COLUMN_DELTA_TYPE);
                        break;
                }
                break;

        }
        return ret;
    }

    protected int compareFormats(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute) {
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        int format1 = attr1.getData_format();
        int format2 = attr2.getData_format();
        return format1 - format2;
    }

    protected int compareNumberScalars(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        Object[] extracted = extractScalarValues(comp1, comp2,
                comparedAttribute, valueType);
        if (extracted[0] instanceof Number) {
            if (extracted[1] instanceof Number) {
                return Double.compare(((Number) extracted[0]).doubleValue(),
                        ((Number) extracted[1]).doubleValue());
            }
            else {
                return Integer.MAX_VALUE;
            }
        }
        else if (extracted[1] instanceof Number) {
            return Integer.MIN_VALUE;
        }
        else if (extracted[0] != null) {
            if (extracted[1] == null) {
                return Integer.MAX_VALUE;
            }
            else {
                return compareStrings("" + extracted[0], "" + extracted[1]);
            }
        }
        else if (extracted[1] != null) {
            return Integer.MIN_VALUE;
        }
        else {
            return 0;
        }
    }

    protected int compareBooleanScalars(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        Object[] extracted = extractScalarValues(comp1, comp2,
                comparedAttribute, valueType);
        if (extracted[0] instanceof Boolean) {
            if (extracted[1] instanceof Boolean) {
                return ((Boolean) extracted[0])
                        .compareTo((Boolean) extracted[1]);
            }
            else {
                return Integer.MAX_VALUE;
            }
        }
        else if (extracted[1] instanceof Boolean) {
            return Integer.MIN_VALUE;
        }
        else if (extracted[0] != null) {
            if (extracted[1] == null) {
                return Integer.MAX_VALUE;
            }
            else {
                return compareStrings("" + extracted[0], "" + extracted[1]);
            }
        }
        else if (extracted[1] != null) {
            return Integer.MIN_VALUE;
        }
        else {
            return 0;
        }
    }

    protected int compareStringScalars(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        String value1 = null;
        String value2 = null;
        switch (valueType) {
            case SnapshotComparison.COLUMN_READ_TYPE:
                value1 = (String) attr1.getReadValue().getScalarValue();
                value2 = (String) attr2.getReadValue().getScalarValue();
                break;
            case SnapshotComparison.COLUMN_WRITE_TYPE:
                value1 = (String) attr1.getWriteValue().getScalarValue();
                value2 = (String) attr2.getWriteValue().getScalarValue();
                break;
            case SnapshotComparison.COLUMN_DELTA_TYPE:
                value1 = (String) attr1.getDeltaValue().getScalarValue();
                value2 = (String) attr2.getDeltaValue().getScalarValue();
                break;
        }
        return compareStrings(value1, value2);
    }

    protected int compareNames(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute) {
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        String value1 = attr1.getAttribute_complete_name();
        String value2 = attr2.getAttribute_complete_name();
        return compareStrings(value1, value2);
    }

    protected int compareSpectrums(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        Object[] value1 = null;
        Object[] value2 = null;
        switch (valueType) {
            case SnapshotComparison.COLUMN_READ_TYPE:
                value1 = attr1.getReadValue().getSpectrumValue();
                value2 = attr2.getReadValue().getSpectrumValue();
                break;
            case SnapshotComparison.COLUMN_WRITE_TYPE:
                value1 = attr1.getWriteValue().getSpectrumValue();
                value2 = attr2.getWriteValue().getSpectrumValue();
                break;
            case SnapshotComparison.COLUMN_DELTA_TYPE:
                value1 = attr1.getDeltaValue().getSpectrumValue();
                value2 = attr2.getDeltaValue().getSpectrumValue();
                break;
        }
        return value1.length - value2.length;
    }

    protected int compareImages(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        Object[][] value1 = null;
        Object[][] value2 = null;
        switch (valueType) {
            case SnapshotComparison.COLUMN_READ_TYPE:
                value1 = attr1.getReadValue().getImageValue();
                value2 = attr2.getReadValue().getImageValue();
                break;
            case SnapshotComparison.COLUMN_WRITE_TYPE:
                value1 = attr1.getWriteValue().getImageValue();
                value2 = attr2.getWriteValue().getImageValue();
                break;
            case SnapshotComparison.COLUMN_DELTA_TYPE:
                value1 = attr1.getDeltaValue().getImageValue();
                value2 = attr2.getDeltaValue().getImageValue();
                break;
        }
        int length1 = value1.length;
        if (length1 > 0) {
            length1 += value1[0].length;
        }
        int length2 = value2.length;
        if (length2 > 0) {
            length2 += value2[0].length;
        }
        return length1 - length2;
    }

    private Object[] extractScalarValues(SnapshotComparison comp1,
            SnapshotComparison comp2, int comparedAttribute, int valueType) {
        Object[] result = new Object[2];
        SnapshotAttribute attr1 = comp1.getSnapshotAttribute(comparedAttribute);
        SnapshotAttribute attr2 = comp2.getSnapshotAttribute(comparedAttribute);
        switch (valueType) {
            case SnapshotComparison.COLUMN_READ_TYPE:
                result[0] = attr1.getReadValue().getScalarValue();
                result[1] = attr2.getReadValue().getScalarValue();
                break;
            case SnapshotComparison.COLUMN_WRITE_TYPE:
                result[0] = attr1.getWriteValue().getScalarValue();
                result[1] = attr2.getWriteValue().getScalarValue();
                break;
            case SnapshotComparison.COLUMN_DELTA_TYPE:
                result[0] = attr1.getDeltaValue().getScalarValue();
                result[1] = attr2.getDeltaValue().getScalarValue();
                break;
        }
        return result;
    }

}
