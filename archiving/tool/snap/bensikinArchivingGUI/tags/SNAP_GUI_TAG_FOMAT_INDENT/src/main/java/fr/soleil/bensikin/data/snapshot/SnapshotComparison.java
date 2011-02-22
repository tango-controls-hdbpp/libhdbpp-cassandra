// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotComparison.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotComparison.
// (Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotComparison.java,v $
// Revision 1.6 2007/07/03 08:41:53 ounsy
// SnapshotCompareTable sorting ok
//
// Revision 1.5 2006/11/29 10:01:10 ounsy
// minor changes
//
// Revision 1.4 2006/04/13 15:21:52 ounsy
// setting "not applicable" when necessary
//
// Revision 1.3 2006/02/15 09:21:58 ounsy
// spectrums rw management
//
// Revision 1.2 2005/12/14 16:37:37 ounsy
// modifications to add asymmetrical snapshots comparisons
//
// Revision 1.1 2005/11/29 18:25:08 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.data.snapshot;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.TangoConst;

/**
 * Used for the comparison between two snapshots, at the attribute level. As
 * such, has references to the same attribute in the first and second snapshots.
 * The resulting "difference attribute" is calculated if required by the
 * comparison parameters.
 * 
 * @author CLAISSE
 */
public class SnapshotComparison {

	private SnapshotAttribute firstSnapshotAttribute;
	private SnapshotAttribute secondSnapshotAttribute;
	private SnapshotAttribute diffSnapshotAttribute;

	private int comparisonType = ATTR_BELONGS_TO_BOTH;

	/**
	 * A fake type of snapshot, used to display the attribute name column
	 */
	public static final int SNAPSHOT_TYPE_ATTR_NAME = 0;

	// defines display order in compare table
	/**
	 * The type for the first attribute
	 */
	public static final int SNAPSHOT_TYPE_1 = 2;

	/**
	 * The type for the second attribute
	 */
	public static final int SNAPSHOT_TYPE_2 = 3;

	/**
	 * The type for the difference attribute
	 */
	public static final int SNAPSHOT_TYPE_DIFF = 1;

	/**
	 * A fake type of attribute value, used to display the attribute name column
	 */
	public static final int COLUMN_ATTR_NAME_TYPE = 0;

	/**
	 * The type for a read column
	 */
	public static final int COLUMN_READ_TYPE = 1;

	/**
	 * The type for a write column
	 */
	public static final int COLUMN_WRITE_TYPE = 2;

	/**
	 * The type for a delta column
	 */
	public static final int COLUMN_DELTA_TYPE = 3;

	public static final int ATTR_BELONGS_TO_NONE = 0;
	public static final int ATTR_BELONGS_TO_SN1_ONLY = 1;
	public static final int ATTR_BELONGS_TO_SN2_ONLY = 2;
	public static final int ATTR_BELONGS_TO_BOTH = 3;

	public static final Color SN_1_COLOR = Color.GREEN;
	public static final Color SN_2_COLOR = Color.YELLOW;
	public static final Color SN_DIFF_COLOR = Color.BLUE;

	public SnapshotComparison(SnapshotAttribute attribute, boolean showDiff,
			int _comparisonType) {
		/*
		 * System.out.println (
		 * "new SnapshotComparison/attribute----------------------------");
		 * System.out.println ( attribute.toString () ); System.out.println (
		 * "-----------------------------------------------------------" );
		 */

		switch (_comparisonType) {
		case ATTR_BELONGS_TO_SN1_ONLY:
			this.firstSnapshotAttribute = attribute;

			this.secondSnapshotAttribute = SnapshotAttribute
					.createDummyAttribute(attribute);
			break;

		case ATTR_BELONGS_TO_SN2_ONLY:
			this.secondSnapshotAttribute = attribute;

			this.firstSnapshotAttribute = SnapshotAttribute
					.createDummyAttribute(attribute);
			break;

		default:
			throw new IllegalStateException(
					"Expected either ATTR_BELONGS_TO_SN1_ONLY (1) or ATTR_BELONGS_TO_SN2_ONLY (2), got "
							+ _comparisonType
							+ " instead. I can only be used to build dummy comparisons.");
		}

		if (showDiff) {
			this.diffSnapshotAttribute = SnapshotAttribute
					.createDummyAttribute(attribute);
		}

		/*
		 * System.out.println (); System.out.println (); System.out.println (
		 * "new SnapshotComparison/this.firstSnapshotAttribute----------------------------"
		 * ); System.out.println ( this.firstSnapshotAttribute.toString () );
		 * System.out.println (
		 * "-----------------------------------------------------------" );
		 * System.out.println (); System.out.println (); System.out.println (
		 * "new SnapshotComparison/this.secondSnapshotAttribute----------------------------"
		 * ); System.out.println ( this.secondSnapshotAttribute.toString () );
		 * System.out.println (
		 * "-----------------------------------------------------------" );
		 */
	}

	/**
	 * Builds a SnapshotComparison given two snapshots to compare, and whether
	 * one wants to display the difference between them.
	 * 
	 * @param attribute
	 *            The attribute in the first snapshot
	 * @param attribute2
	 *            The attribute in the second snapshot
	 * @param showDiff
	 *            True if a calculation of the difference is needed, false
	 *            otherwise.
	 */
	public SnapshotComparison(SnapshotAttribute attribute,
			SnapshotAttribute attribute2, boolean showDiff) {
		this.firstSnapshotAttribute = attribute;
		this.secondSnapshotAttribute = attribute2;
		// Spectrum comparison is not allowed. Image comparison neither.
		if (this.firstSnapshotAttribute.getData_format() != AttrDataFormat._SCALAR) {
			int dataType = this.firstSnapshotAttribute.getData_type();
			int id = this.firstSnapshotAttribute.getAttribute_id();
			int permit = this.firstSnapshotAttribute.getPermit();
			String displayFormat = this.firstSnapshotAttribute
					.getDisplayFormat();
			String name = this.firstSnapshotAttribute
					.getAttribute_complete_name();
			this.firstSnapshotAttribute = new SnapshotAttribute();
			this.firstSnapshotAttribute
					.setData_format(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT);
			this.firstSnapshotAttribute.setData_type(dataType);
			this.firstSnapshotAttribute.setAttribute_id(id);
			this.firstSnapshotAttribute.setDisplayFormat(displayFormat);
			this.firstSnapshotAttribute.setAttribute_complete_name(name);
			this.firstSnapshotAttribute
					.setDeltaValue(new SnapshotAttributeDeltaValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							null));
			this.firstSnapshotAttribute
					.setReadValue(new SnapshotAttributeReadValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							dataType, null));
			this.firstSnapshotAttribute
					.setWriteValue(new SnapshotAttributeWriteValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							dataType, null));
			this.firstSnapshotAttribute.setSnapshotAttributes(null);
			this.firstSnapshotAttribute.setPermit(permit);
		}
		if (this.secondSnapshotAttribute.getData_format() != AttrDataFormat._SCALAR) {
			int dataType = this.secondSnapshotAttribute.getData_type();
			int id = this.secondSnapshotAttribute.getAttribute_id();
			int permit = this.secondSnapshotAttribute.getPermit();
			String displayFormat = this.secondSnapshotAttribute
					.getDisplayFormat();
			String name = this.secondSnapshotAttribute
					.getAttribute_complete_name();
			this.secondSnapshotAttribute = new SnapshotAttribute();
			this.secondSnapshotAttribute
					.setData_format(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT);
			this.secondSnapshotAttribute.setData_type(dataType);
			this.secondSnapshotAttribute.setAttribute_id(id);
			this.secondSnapshotAttribute.setDisplayFormat(displayFormat);
			this.secondSnapshotAttribute.setAttribute_complete_name(name);
			this.secondSnapshotAttribute
					.setDeltaValue(new SnapshotAttributeDeltaValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							null));
			this.secondSnapshotAttribute
					.setReadValue(new SnapshotAttributeReadValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							dataType, null));
			this.secondSnapshotAttribute
					.setWriteValue(new SnapshotAttributeWriteValue(
							SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT,
							dataType, null));
			this.secondSnapshotAttribute.setSnapshotAttributes(null);
			this.secondSnapshotAttribute.setPermit(permit);
		}

		if (showDiff) {
			this.diffSnapshotAttribute = this.buildDiffAttribute();
		}
	}

	/**
	 * Calculates the difference.
	 * 
	 * @return A virtual attribute, which values are the differences between the
	 *         two snapshots for each type of value: read,write and delta
	 */
	private SnapshotAttribute buildDiffAttribute() {
		SnapshotAttribute ret = new SnapshotAttribute();

		SnapshotAttributeReadValue read1 = this.firstSnapshotAttribute
				.getReadValue();
		SnapshotAttributeReadValue read2 = this.secondSnapshotAttribute
				.getReadValue();
		SnapshotAttributeWriteValue write1 = this.firstSnapshotAttribute
				.getWriteValue();
		SnapshotAttributeWriteValue write2 = this.secondSnapshotAttribute
				.getWriteValue();

		SnapshotAttributeReadValue readDiff = buildDiffAttributeReadValue(
				read1, read2);
		SnapshotAttributeWriteValue writeDiff = buildDiffAttributeWriteValue(
				write1, write2);
		SnapshotAttributeDeltaValue deltaDiff = new SnapshotAttributeDeltaValue(
				writeDiff, readDiff);

		ret.setReadValue(readDiff);
		ret.setWriteValue(writeDiff);
		ret.setDeltaValue(deltaDiff);
		ret.setDisplayFormat(firstSnapshotAttribute.getDisplayFormat());
		ret.setAttribute_complete_name(this.firstSnapshotAttribute
				.getAttribute_complete_name()
				+ "-"
				+ this.secondSnapshotAttribute.getAttribute_complete_name());
		if (this.firstSnapshotAttribute.getData_format() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
			ret.setData_format(this.firstSnapshotAttribute.getData_format());
		} else if (this.secondSnapshotAttribute.getData_format() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
			ret.setData_format(this.secondSnapshotAttribute.getData_format());
		} else {
			ret
					.setData_format(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT);
		}
		if (this.firstSnapshotAttribute.getData_type() > 0) {
			ret.setData_type(this.firstSnapshotAttribute.getData_type());
		} else if (this.secondSnapshotAttribute.getData_type() > 0) {
			ret.setData_type(this.secondSnapshotAttribute.getData_type());
		} else {
			ret.setData_type(TangoConst.Tango_DEV_VOID);
		}

		return ret;
	}

	/**
	 * Calculates the difference of the write values.
	 * 
	 * @param write1
	 *            The write value of the first attribute
	 * @param write2
	 *            The write value of the second attribute
	 */
	private SnapshotAttributeWriteValue buildDiffAttributeWriteValue(
			SnapshotAttributeWriteValue write1,
			SnapshotAttributeWriteValue write2) {
		SnapshotAttributeReadValue pseudoReadValue = new SnapshotAttributeReadValue(
				write1.getDataFormat(), write1.getDataType(), write1.getValue());
		SnapshotAttributeWriteValue pseudoWriteValue = write2;

		SnapshotAttributeDeltaValue writeValueDiff;
		if (write1.getDataFormat() == AttrDataFormat._SCALAR
				&& write1.getDataFormat() == AttrDataFormat._SCALAR) {
			writeValueDiff = new SnapshotAttributeDeltaValue(pseudoWriteValue,
					pseudoReadValue, true);
		} else {
			writeValueDiff = new SnapshotAttributeDeltaValue(
					SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
		}
		SnapshotAttributeWriteValue ret = new SnapshotAttributeWriteValue(
				writeValueDiff.getDataFormat(), writeValueDiff.getDataType(),
				writeValueDiff.getValue());
		if (writeValueDiff.isNotApplicable()) {
			ret.setNotApplicable(true);
		}
		return ret;
	}

	/**
	 * Calculates the difference of the read values.
	 * 
	 * @param read1
	 *            The read value of the first attribute
	 * @param read2
	 *            The read value of the second attribute
	 */
	private SnapshotAttributeReadValue buildDiffAttributeReadValue(
			SnapshotAttributeReadValue read1, SnapshotAttributeReadValue read2) {
		SnapshotAttributeReadValue pseudoReadValue = read1;
		SnapshotAttributeWriteValue pseudoWriteValue = new SnapshotAttributeWriteValue(
				read2.getDataFormat(), read2.getDataType(), read2.getValue());

		SnapshotAttributeDeltaValue readValueDiff;
		if (read1.getDataFormat() == AttrDataFormat._SCALAR
				&& read2.getDataFormat() == AttrDataFormat._SCALAR) {
			readValueDiff = new SnapshotAttributeDeltaValue(pseudoWriteValue,
					pseudoReadValue, true);
		} else {
			readValueDiff = new SnapshotAttributeDeltaValue(
					SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
		}
		SnapshotAttributeReadValue ret = new SnapshotAttributeReadValue(
				readValueDiff.getDataFormat(), readValueDiff.getDataType(),
				readValueDiff.getValue());
		if (readValueDiff.isNotApplicable()) {
			ret.setNotApplicable(true);
		}
		return ret;
	}

	/**
	 * Returns the required snapshot attribute: first, second, or calculated
	 * difference. In the case of snapshotType=SNAPSHOT_TYPE_ATTR_NAME, it
	 * doesn't make any difference to return either, because the attribute has
	 * the same name in both snapshots. We arbitrarily return the first.
	 * 
	 * @param snapshotType
	 *            The required type of data.
	 * @return The required snapshot attribute
	 * @throws IllegalArgumentException
	 *             If snapshotType is not in ( SNAPSHOT_TYPE_ATTR_NAME,
	 *             SNAPSHOT_TYPE_1, SNAPSHOT_TYPE_2, SNAPSHOT_TYPE_DIFF )
	 */
	public SnapshotAttribute getSnapshotAttribute(int snapshotType)
			throws IllegalArgumentException {
		switch (snapshotType) {
		case SNAPSHOT_TYPE_ATTR_NAME:
			return this.firstSnapshotAttribute;
		case SNAPSHOT_TYPE_1:
			return this.firstSnapshotAttribute;
		case SNAPSHOT_TYPE_2:
			return this.secondSnapshotAttribute;
		case SNAPSHOT_TYPE_DIFF:
			return this.diffSnapshotAttribute;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Builds a list of SnapshotComparison, which length is the number of
	 * attributes of snapshot1 and snapshot2. If required by showDiff, each line
	 * of the list has a calculated difference.
	 * 
	 * @param snapshot1
	 *            The first snapshot
	 * @param snapshot2
	 *            The second snapshot
	 * @param showDiff
	 *            True if a calculation of the difference is needed, false
	 *            otherwise
	 */
	public static SnapshotComparison[] build(Snapshot snapshot1,
			Snapshot snapshot2, boolean showDiff) {
		boolean areCoherent = snapshot1.hasSameAttributesAs(snapshot2);
		SnapshotComparison[] ret = null;

		if (areCoherent) {
			SnapshotAttributes snapshotAttributes1 = snapshot1
					.getSnapshotAttributes();
			SnapshotAttributes snapshotAttributes2 = snapshot2
					.getSnapshotAttributes();

			SnapshotAttribute[] snapshotAttributesTab1 = snapshotAttributes1
					.getSnapshotAttributes();
			SnapshotAttribute[] snapshotAttributesTab2 = snapshotAttributes2
					.getSnapshotAttributes();
			// WARNING : MAKE SURE THE 2 TABLES ARE IN THE SAME ORDER! (use
			// hashtable if necessary)

			if (snapshotAttributesTab1 == null) {
				return null;
			}

			int size = snapshotAttributesTab1.length;
			ret = new SnapshotComparison[size];
			for (int i = 0; i < size; i++) {
				SnapshotComparison row = new SnapshotComparison(
						snapshotAttributesTab1[i], snapshotAttributesTab2[i],
						showDiff);
				ret[i] = row;
			}

		} else {
			ret = buildAsymmetricSnapshotComparison(snapshot1, snapshot2,
					showDiff);
		}

		return ret;
	}

	/**
	 * @param snapshot1
	 * @param snapshot2
	 * @param showDiff
	 * @return
	 */
	private static SnapshotComparison[] buildAsymmetricSnapshotComparison(
			Snapshot snapshot1, Snapshot snapshot2, boolean showDiff) {
		SnapshotAttributes snapshotAttributes1 = snapshot1
				.getSnapshotAttributes();
		SnapshotAttributes snapshotAttributes2 = snapshot2
				.getSnapshotAttributes();

		Hashtable intersection = snapshotAttributes1
				.intersectWith(snapshotAttributes2);
		Hashtable sn1Only = (Hashtable) intersection.get(new Integer(
				SnapshotComparison.ATTR_BELONGS_TO_SN1_ONLY));
		Hashtable sn2Only = (Hashtable) intersection.get(new Integer(
				SnapshotComparison.ATTR_BELONGS_TO_SN2_ONLY));
		Hashtable snBoth = (Hashtable) intersection.get(new Integer(
				SnapshotComparison.ATTR_BELONGS_TO_BOTH));

		// System.out.println(
		// "buildAsymmetricSnapshotComparison/sn1Only/"+sn1Only.size()+"/sn2Only/"+sn2Only.size()+"/snBoth/"+snBoth.size()+"/");
		/*
		 * System.out.println(
		 * "buildAsymmetricSnapshotComparison/sn2Only---------------------" );
		 * System.out.println( sn2Only.toString () ); System.out.println(
		 * "buildAsymmetricSnapshotComparison/sn2Only---------------------" );
		 * System.out.println(
		 * "buildAsymmetricSnapshotComparison/snBoth---------------------" );
		 * System.out.println( snBoth.toString () ); System.out.println(
		 * "buildAsymmetricSnapshotComparison/snBoth---------------------" );
		 */

		SnapshotComparison[] realComparison = SnapshotComparison
				.buildRealComparisonPart(snBoth, showDiff);
		SnapshotComparison[] sn1OnlyComparison = SnapshotComparison
				.buildDummyComparisonPart(sn1Only, showDiff,
						SnapshotComparison.ATTR_BELONGS_TO_SN1_ONLY);
		SnapshotComparison[] sn2OnlyComparison = SnapshotComparison
				.buildDummyComparisonPart(sn2Only, showDiff,
						SnapshotComparison.ATTR_BELONGS_TO_SN2_ONLY);

		// System.out.println(
		// "buildAsymmetricSnapshotComparison/realComparison/"+realComparison.length+"/sn1OnlyComparison/"+sn1OnlyComparison.length+"/sn2OnlyComparison/"+sn2OnlyComparison.length+"/"
		// );

		SnapshotComparison[] ret = SnapshotComparison.mixRealAndDummyParts(
				realComparison, sn1OnlyComparison, sn2OnlyComparison);

		return ret;
	}

	/**
	 * @param realComparison
	 * @param sn1OnlyComparison
	 * @param sn2OnlyComparison
	 * @return
	 */
	private static SnapshotComparison[] mixRealAndDummyParts(
			SnapshotComparison[] realComparison,
			SnapshotComparison[] sn1OnlyComparison,
			SnapshotComparison[] sn2OnlyComparison) {
		// return realComparison;
		int size1Only = sn1OnlyComparison.length;
		int size2Only = sn2OnlyComparison.length;
		int sizeBoth = realComparison.length;
		int totalSize = size1Only + size2Only + sizeBoth;

		SnapshotComparison[] ret = new SnapshotComparison[totalSize];

		for (int i = 0; i < sizeBoth; i++) {
			ret[i] = realComparison[i];
		}
		for (int i = 0; i < size1Only; i++) {
			ret[i + sizeBoth] = sn1OnlyComparison[i];
		}
		for (int i = 0; i < size2Only; i++) {
			ret[i + sizeBoth + size1Only] = sn2OnlyComparison[i];
		}

		return ret;
	}

	/**
	 * @param sn1Only
	 * @return
	 */
	private static SnapshotComparison[] buildDummyComparisonPart(
			Hashtable snXOnly, boolean showDiff, int _comparisonType) {
		/*
		 * System.out.println(
		 * "buildDummyComparisonPart/_comparisonType/"+_comparisonType
		 * +"/-----------------------------" ); System.out.println( snXOnly );
		 * System.out.println(
		 * "------------------------------------------------------------------------------------"
		 * );
		 */

		int size = snXOnly.size();
		SnapshotComparison[] dummyComparisons = new SnapshotComparison[size];
		Enumeration enumer = snXOnly.keys();
		int i = 0;

		while (enumer.hasMoreElements()) {
			String nextKey = (String) enumer.nextElement();
			SnapshotAttribute attr = (SnapshotAttribute) snXOnly.get(nextKey);

			SnapshotComparison row = new SnapshotComparison(attr, showDiff,
					_comparisonType);
			row.setComparisonType(_comparisonType);
			dummyComparisons[i] = row;

			i++;
		}

		return dummyComparisons;
	}

	/**
	 * @param snBoth
	 * @return
	 */
	private static SnapshotComparison[] buildRealComparisonPart(
			Hashtable snBoth, boolean showDiff) {
		SnapshotComparison[] realComparisons = new SnapshotComparison[snBoth
				.size()];
		Enumeration enumer = snBoth.keys();
		int i = 0;

		while (enumer.hasMoreElements()) {
			String nextKey = (String) enumer.nextElement();
			SnapshotAttribute[] attrBoth = (SnapshotAttribute[]) snBoth
					.get(nextKey);

			SnapshotAttribute attr1 = attrBoth[0];
			SnapshotAttribute attr2 = attrBoth[1];

			SnapshotComparison row = new SnapshotComparison(attr1, attr2,
					showDiff);
			realComparisons[i] = row;

			i++;
		}

		return realComparisons;
	}

	/**
	 * @return Returns the comparisonType.
	 */
	public int getComparisonType() {
		return comparisonType;
	}

	/**
	 * @param comparisonType
	 *            The comparisonType to set.
	 */
	public void setComparisonType(int comparisonType) {
		this.comparisonType = comparisonType;
	}

	/**
	 * @return
	 */
	public static Color getFirstSnapshotColor() {
		return new Color(85, 193, 255);
	}

	/**
	 * @return
	 */
	public static Color getSecondSnapshotColor() {
		return new Color(255, 85, 85);
	}

	/**
	 * @return
	 */
	public static Color getDiffSnapshotColor() {
		return new Color(255, 145, 255);
	}
}
