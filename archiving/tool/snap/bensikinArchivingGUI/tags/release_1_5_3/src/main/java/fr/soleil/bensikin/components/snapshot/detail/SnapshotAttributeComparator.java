package fr.soleil.bensikin.components.snapshot.detail;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.BensikinComparator;
import fr.soleil.bensikin.data.snapshot.SnapshotAttribute;

/**
 * Implements a Comparator on SnapshotAttribute objects, useful to sort the
 * snapshot details list. The comparison can be on any one of the
 * SnapshotAttribute fields, depending on the type specified on constructing the
 * comparator. Empty or null values are considered lowest.
 * 
 * @author CLAISSE
 */
public class SnapshotAttributeComparator extends
		BensikinComparator<SnapshotAttribute> {

	/**
	 * The comparison will be on the SnapshotAttribute's name field
	 */
	public static final int COMPARE_NAME = 0;

	/**
	 * The comparison will be on the SnapshotAttribute's write value field
	 */
	public static final int COMPARE_WRITE_VALUE = 1;

	/**
	 * The comparison will be on the SnapshotAttribute's read value field
	 */
	public static final int COMPARE_READ_VALUE = 2;

	/**
	 * The comparison will be on the SnapshotAttribute's delta value field
	 */
	public static final int COMPARE_DELTA_VALUE = 3;

	/**
	 * Builds a comparator on the desired SnapshotData field
	 * 
	 * @param _fieldToCompare
	 *            The field on which the comparison will be done
	 * @throws IllegalArgumentException
	 *             If _fieldToCompare isn't in (COMPARE_ID, COMPARE_TIME,
	 *             COMPARE_COMMENT)
	 */
	public SnapshotAttributeComparator(int _fieldToCompare)
			throws IllegalArgumentException {
		super(_fieldToCompare);
		if (_fieldToCompare != COMPARE_NAME
				&& _fieldToCompare != COMPARE_WRITE_VALUE
				&& _fieldToCompare != COMPARE_READ_VALUE
				&& _fieldToCompare != COMPARE_DELTA_VALUE) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int compare(SnapshotAttribute data1, SnapshotAttribute data2) {
		int ret = 0;
		if (fieldToCompare == COMPARE_NAME) {
			ret = compareNames(data1, data2);
		} else {
			switch (data1.getData_format()) {
			case AttrDataFormat._SCALAR:
				switch (data1.getData_type()) {
				case TangoConst.Tango_DEV_BOOLEAN:
					ret = compareBooleanScalars(data1, data2, fieldToCompare);
					break;
				case TangoConst.Tango_DEV_STRING:
					ret = compareStringScalars(data1, data2, fieldToCompare);
					break;
				case TangoConst.Tango_DEV_CHAR:
				case TangoConst.Tango_DEV_UCHAR:
				case TangoConst.Tango_DEV_SHORT:
				case TangoConst.Tango_DEV_USHORT:
				case TangoConst.Tango_DEV_LONG:
				case TangoConst.Tango_DEV_ULONG:
				case TangoConst.Tango_DEV_FLOAT:
				case TangoConst.Tango_DEV_DOUBLE:
					ret = compareNumberScalars(data1, data2, fieldToCompare);
					break;
				}
				break;
			case AttrDataFormat._SPECTRUM:
				ret = compareSpectrums(data1, data2, fieldToCompare);
				break;
			case AttrDataFormat._IMAGE:
				ret = compareImages(data1, data2, fieldToCompare);
				break;
			}
		}
		return ret;
	}

	protected int compareFormats(SnapshotAttribute attr1,
			SnapshotAttribute attr2) {
		int format1 = attr1.getData_format();
		int format2 = attr2.getData_format();
		return format1 - format2;
	}

	protected int compareNumberScalars(SnapshotAttribute attr1,
			SnapshotAttribute attr2, int valueType) {
		Number value1 = null;
		Number value2 = null;
		switch (valueType) {
		case COMPARE_READ_VALUE:
			value1 = (Number) attr1.getReadValue().getScalarValue();
			value2 = (Number) attr2.getReadValue().getScalarValue();
			break;
		case COMPARE_WRITE_VALUE:
			value1 = (Number) attr1.getWriteValue().getScalarValue();
			value2 = (Number) attr2.getWriteValue().getScalarValue();
			break;
		case COMPARE_DELTA_VALUE:
			value1 = (Number) attr1.getDeltaValue().getScalarValue();
			value2 = (Number) attr2.getDeltaValue().getScalarValue();
			break;
		}
		double val1 = value1.doubleValue();
		double val2 = value2.doubleValue();
		return Double.compare(val1, val2);
	}

	protected int compareStringScalars(SnapshotAttribute attr1,
			SnapshotAttribute attr2, int valueType) {
		String value1 = null;
		String value2 = null;
		switch (valueType) {
		case COMPARE_READ_VALUE:
			value1 = (String) attr1.getReadValue().getScalarValue();
			value2 = (String) attr2.getReadValue().getScalarValue();
			break;
		case COMPARE_WRITE_VALUE:
			value1 = (String) attr1.getWriteValue().getScalarValue();
			value2 = (String) attr2.getWriteValue().getScalarValue();
			break;
		case COMPARE_DELTA_VALUE:
			value1 = (String) attr1.getDeltaValue().getScalarValue();
			value2 = (String) attr2.getDeltaValue().getScalarValue();
			break;
		}
		return value1.compareTo(value2);
	}

	protected int compareBooleanScalars(SnapshotAttribute attr1,
			SnapshotAttribute attr2, int valueType) {
		Boolean value1 = null;
		Boolean value2 = null;
		switch (valueType) {
		case COMPARE_READ_VALUE:
			value1 = (Boolean) attr1.getReadValue().getScalarValue();
			value2 = (Boolean) attr2.getReadValue().getScalarValue();
			break;
		case COMPARE_WRITE_VALUE:
			value1 = (Boolean) attr1.getWriteValue().getScalarValue();
			value2 = (Boolean) attr2.getWriteValue().getScalarValue();
			break;
		case COMPARE_DELTA_VALUE:
			value1 = (Boolean) attr1.getDeltaValue().getScalarValue();
			value2 = (Boolean) attr2.getDeltaValue().getScalarValue();
			break;
		}
		return value1.compareTo(value2);
	}

	protected int compareNames(SnapshotAttribute attr1, SnapshotAttribute attr2) {
		String name1 = attr1.getAttribute_complete_name();
		String name2 = attr2.getAttribute_complete_name();
		return super.compareStrings(name1, name2);
	}

	protected int compareSpectrums(SnapshotAttribute attr1,
			SnapshotAttribute attr2, int valueType) {
		Object[] value1 = null;
		Object[] value2 = null;
		switch (valueType) {
		case COMPARE_READ_VALUE:
			value1 = attr1.getReadValue().getSpectrumValue();
			value2 = attr2.getReadValue().getSpectrumValue();
			break;
		case COMPARE_WRITE_VALUE:
			value1 = attr1.getWriteValue().getSpectrumValue();
			value2 = attr2.getWriteValue().getSpectrumValue();
			break;
		case COMPARE_DELTA_VALUE:
			value1 = attr1.getDeltaValue().getSpectrumValue();
			value2 = attr2.getDeltaValue().getSpectrumValue();
			break;
		}
		return value1.length - value2.length;
	}

	protected int compareImages(SnapshotAttribute attr1,
			SnapshotAttribute attr2, int valueType) {
		Object[][] value1 = null;
		Object[][] value2 = null;
		switch (valueType) {
		case COMPARE_READ_VALUE:
			value1 = attr1.getReadValue().getImageValue();
			value2 = attr2.getReadValue().getImageValue();
			break;
		case COMPARE_WRITE_VALUE:
			value1 = attr1.getWriteValue().getImageValue();
			value2 = attr2.getWriteValue().getImageValue();
			break;
		case COMPARE_DELTA_VALUE:
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
}
