//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotAttribute.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotAttribute.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.16 $
//
// $Log: SnapshotAttribute.java,v $
// Revision 1.16  2007/07/03 08:39:54  ounsy
// trace removed
//
// Revision 1.15  2007/06/29 09:18:58  ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.14  2006/10/31 16:54:08  ounsy
// milliseconds and null values management
//
// Revision 1.13  2006/06/28 12:50:52  ounsy
// image support
//
// Revision 1.12  2006/04/13 12:37:33  ounsy
// new spectrum types support
//
// Revision 1.11  2006/03/29 10:22:13  ounsy
// added a protection against null values
//
// Revision 1.10  2006/03/27 15:26:05  ounsy
// avoiding java.lang.NegativeArraySizeException
//
// Revision 1.9  2006/03/20 15:51:04  ounsy
// added the case of Snapshot delta value for spectrums which
// read and write parts are the same length.
//
// Revision 1.8  2006/03/14 13:06:25  ounsy
// corrected the SNAP/spectrums/RW problem
// about the read and write values having the same length
//
// Revision 1.7  2006/02/15 09:20:12  ounsy
// spectrums rw management
//
// Revision 1.6  2005/12/14 16:35:56  ounsy
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

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.TangoApi.AttributeProxy;
import fr.esrf.TangoDs.AttrData;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;

/**
 * Represents an attribute attached to a snapshot. A SnapshotAttribute belongs
 * to a SnapshotAttributes group, and has 3 kinds of value:
 * <UL>
 * <LI>A SnapshotAttributeReadValue
 * <LI>A SnapshotAttributeWriteValue
 * <LI>A SnapshotAttributeDeltaValue
 * </UL>
 * 
 * @author CLAISSE
 */
public class SnapshotAttribute {
	private SnapshotAttributeReadValue readValue;
	private SnapshotAttributeWriteValue writeValue;
	private SnapshotAttributeDeltaValue deltaValue;

	private String attribute_complete_name;
	private int attribute_id = 0;
	private int data_type;
	private int data_format;// [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
	private int permit;
	private String display_format;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "Attribute";

	/**
	 * The XML tag name used for the "id" property
	 */
	public static final String ID_PROPERTY_XML_TAG = "Id";

	/**
	 * The XML tag name used for the "data type" property
	 */
	public static final String DATA_TYPE_PROPERTY_XML_TAG = "DataType";

	/**
	 * The XML tag name used for the "data format" property
	 */
	public static final String DATA_FORMAT_PROPERTY_XML_TAG = "DataFormat";

	/**
	 * The XML tag name used for the "writable" property
	 */
	public static final String WRITABLE_PROPERTY_XML_TAG = "Writable";

	/**
	 * The XML tag name used for the "complete name" property
	 */
	public static final String COMPLETE_NAME_PROPERTY_XML_TAG = "CompleteName";

	private SnapshotAttributes snapshotAttributes;

	/**
	 * Default constructor, does nothing
	 */
	public SnapshotAttribute() {

	}

	/**
	 * Builds a SnapshotAttribute with the data from the SnapAttributeExtract,
	 * keeping a reference to the SnapshotAttributes group this attribute is
	 * rattached to.
	 * 
	 * @param extract
	 *            The object containing the attribute fields.
	 * @param attrs
	 *            The SnapshotAttributes group this attribute belongs to.
	 */
	public SnapshotAttribute(SnapAttributeExtract extract,
			SnapshotAttributes attrs) {
		this.snapshotAttributes = attrs;

		int _idAttr = extract.getId_att();
		int _format = extract.getData_format();
		int _dataType = extract.getData_type();
		int _writable = extract.getWritable();
		String _completeName = extract.getAttribute_complete_name();

		this.attribute_complete_name = _completeName;
		this.attribute_id = _idAttr;
		this.data_type = _dataType;
		this.data_format = _format;
		this.permit = _writable;
		this.display_format = "";

		SnapshotAttributeReadValue _readValue = null;
		SnapshotAttributeWriteValue _writeValue = null;
		// SnapshotAttributeDeltaValue _deltaValue = null;

		Object val = extract.getValue();

		switch (_format) {
		case AttrDataFormat._SCALAR:
			switch (_writable) {
			case AttrWriteType._READ:
				_readValue = new SnapshotAttributeReadValue(_format, _dataType,
						val);
				break;

			case AttrWriteType._READ_WITH_WRITE:
				_readValue = new SnapshotAttributeReadValue(_format, _dataType,
						getValue(val, 0));
				_writeValue = new SnapshotAttributeWriteValue(_format,
						_dataType, getValue(val, 1));
				break;

			case AttrWriteType._WRITE:
				_writeValue = new SnapshotAttributeWriteValue(_format,
						_dataType, val);
				break;

			case AttrWriteType._READ_WRITE:
				_readValue = new SnapshotAttributeReadValue(_format, _dataType,
						getValue(val, 0));
				_writeValue = new SnapshotAttributeWriteValue(_format,
						_dataType, getValue(val, 1));
				break;
			}
			break;
		case AttrDataFormat._SPECTRUM:
			switch (_writable) {
			case AttrWriteType._READ:
				switch (_dataType) {
				case AttrData.Tango_DEV_STATE:
				case AttrData.Tango_DEV_STRING:
				case AttrData.Tango_DEV_BOOLEAN:
				case AttrData.Tango_DEV_CHAR:
				case AttrData.Tango_DEV_UCHAR:
				case AttrData.Tango_DEV_LONG:
				case AttrData.Tango_DEV_ULONG:
				case AttrData.Tango_DEV_SHORT:
				case AttrData.Tango_DEV_USHORT:
				case AttrData.Tango_DEV_FLOAT:
				case AttrData.Tango_DEV_DOUBLE:
					_readValue = new SnapshotAttributeReadValue(_format,
							_dataType, val);
					break;
				default:
					// nothing to do
				}
				break;

			case AttrWriteType._WRITE:
				switch (_dataType) {
				case AttrData.Tango_DEV_STRING:
				case AttrData.Tango_DEV_BOOLEAN:
				case AttrData.Tango_DEV_CHAR:
				case AttrData.Tango_DEV_UCHAR:
				case AttrData.Tango_DEV_LONG:
				case AttrData.Tango_DEV_ULONG:
				case AttrData.Tango_DEV_SHORT:
				case AttrData.Tango_DEV_USHORT:
				case AttrData.Tango_DEV_FLOAT:
				case AttrData.Tango_DEV_DOUBLE:
					_writeValue = new SnapshotAttributeWriteValue(_format,
							_dataType, val);
					break;
				default:
					// nothing to do
				}
				break;

			case AttrWriteType._READ_WITH_WRITE:
			case AttrWriteType._READ_WRITE:
				Object[] temp = (Object[]) val;
				switch (_dataType) {
				case AttrData.Tango_DEV_STRING:
					try {
						String[] readst = (String[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readst);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new String[0]);
					}
					try {
						String[] writest = (String[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writest);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new String[0]);
					}
					break;
				case AttrData.Tango_DEV_BOOLEAN:
					try {
						Boolean[] readb = (Boolean[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readb);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Boolean[0]);
					}
					try {
						Boolean[] writeb = (Boolean[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writeb);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Boolean[0]);
					}
					break;
				case AttrData.Tango_DEV_CHAR:
				case AttrData.Tango_DEV_UCHAR:
					try {
						Byte[] readc = (Byte[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readc);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Byte[0]);
					}
					try {
						Byte[] writec = (Byte[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writec);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Byte[0]);
					}
					break;
				case AttrData.Tango_DEV_LONG:
				case AttrData.Tango_DEV_ULONG:
					try {
						Integer[] readl = (Integer[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readl);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Integer[0]);
					}
					try {
						Integer[] writel = (Integer[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writel);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Integer[0]);
					}
					break;
				case AttrData.Tango_DEV_SHORT:
				case AttrData.Tango_DEV_USHORT:
					try {
						Short[] reads = (Short[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, reads);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Short[0]);
					}
					try {
						Short[] writes = (Short[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writes);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Short[0]);
					}
					break;
				case AttrData.Tango_DEV_FLOAT:
					try {
						Float[] readf = (Float[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readf);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Float[0]);
					}
					try {
						Float[] writef = (Float[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writef);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Float[0]);
					}
					break;
				case AttrData.Tango_DEV_DOUBLE:
					try {
						Double[] readd = (Double[]) temp[0];
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, readd);
					} catch (ClassCastException cce) {
						_readValue = new SnapshotAttributeReadValue(_format,
								_dataType, new Double[0]);
					}
					try {
						Double[] writed = (Double[]) temp[1];
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, writed);
					} catch (ClassCastException cce) {
						_writeValue = new SnapshotAttributeWriteValue(_format,
								_dataType, new Double[0]);
					}
					break;
				default: // nothing to do
				}
				break;

			default: // nothing to do
			}
			break;

		case AttrDataFormat._IMAGE:// to do
			switch (_writable) {
			case AttrWriteType._READ:
				switch (_dataType) {
				case AttrData.Tango_DEV_CHAR:
				case AttrData.Tango_DEV_UCHAR:
				case AttrData.Tango_DEV_LONG:
				case AttrData.Tango_DEV_ULONG:
				case AttrData.Tango_DEV_SHORT:
				case AttrData.Tango_DEV_USHORT:
				case AttrData.Tango_DEV_FLOAT:
				case AttrData.Tango_DEV_DOUBLE:
					_readValue = new SnapshotAttributeReadValue(_format,
							_dataType, val);
					break;
				case AttrData.Tango_DEV_STATE:
				case AttrData.Tango_DEV_STRING:
				case AttrData.Tango_DEV_BOOLEAN:
				default:
					// nothing to do
				}
				break;
			default: // nothing to do
			}
			break;

		default:
		}

		if (_readValue != null && _writeValue != null) {
			if (_format == AttrDataFormat._SCALAR
					|| _format == AttrDataFormat._SPECTRUM) {
				this.deltaValue = SnapshotAttributeDeltaValue.getInstance(
						_writeValue, _readValue);
			} else {
				this.deltaValue = new SnapshotAttributeDeltaValue(
						SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
			}
		} else {
			this.deltaValue = new SnapshotAttributeDeltaValue(
					SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
		}

		if (_readValue != null) {
			this.readValue = _readValue;
		} else {
			this.readValue = new SnapshotAttributeReadValue(
					SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, 0, null);
		}

		if (_writeValue != null) {
			this.writeValue = _writeValue;
		} else {
			this.writeValue = new SnapshotAttributeWriteValue(
					SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, 0, null);
		}

		updateDisplayFormat();
	}

	/**
	 * Only initializes the reference to the SnapshotAttributes group this
	 * attribute belongs to.
	 * 
	 * @param attrs
	 */
	public SnapshotAttribute(SnapshotAttributes attrs) {
		this.snapshotAttributes = attrs;
	}

	/**
	 * Initializes the reference to the SnapshotAttributes group this attribute
	 * belongs to, sets the name of the attribute and calculates its display
	 * format.
	 * 
	 * @param name
	 *            the name of the attribute
	 * @param attrs
	 *            the reference to the SnapshotAttributes group
	 */
	public SnapshotAttribute(String name, SnapshotAttributes attrs) {
		this.setAttribute_complete_name(name);
		this.snapshotAttributes = attrs;
		display_format = "";
		updateDisplayFormat();
	}

	/**
	 * Sets the display format, reading it from Tango. Uses a Thread to avoid
	 * freezing
	 */
	public void updateDisplayFormat() {
		(new Thread() {
			public void run() {
				try {
					AttributeProxy proxy;
					proxy = new AttributeProxy(attribute_complete_name);
					display_format = proxy.get_info().format;
				} catch (Exception e) {
					display_format = "";
				} finally {
					if (readValue != null) {
						readValue.setDisplayFormat(display_format);
					}
					if (writeValue != null) {
						writeValue.setDisplayFormat(display_format);
					}
					if (deltaValue != null) {
						deltaValue.setDisplayFormat(display_format);
					}
				}
			}
		}).start();

	}

	/**
	 * Converts to a SnapAttributeExtract object
	 * 
	 * @return The result conversion
	 */
	SnapAttributeExtract toSnapAttributeExtrac() {
		SnapAttributeLight snapAttributeLight = new SnapAttributeLight();

		snapAttributeLight
				.setAttribute_complete_name(this.attribute_complete_name);
		snapAttributeLight.setAttribute_id(this.attribute_id);
		snapAttributeLight.setData_type(this.data_type);
		snapAttributeLight.setData_format(this.data_format);
		snapAttributeLight.setWritable(this.permit);

		SnapAttributeExtract ret = new SnapAttributeExtract(snapAttributeLight);
		Snapshot snapshot = this.getSnapshotAttributes().getSnapshot();
		SnapshotData snapshotData = snapshot.getSnapshotData();

		ret.setId_snap(snapshotData.getId());
		ret.setSnap_date(snapshotData.getTime());

		Object[] value = null;
		Object valueAfter = null;
		switch (this.data_format) {
		case AttrDataFormat._SCALAR:
			switch (this.permit) {
			case AttrWriteType._READ:
				value = new Object[1];
				value[0] = this.readValue.getScalarValue();
				break;

			case AttrWriteType._READ_WITH_WRITE:
				value = new Object[2];
				value[0] = this.readValue.getScalarValue();
				value[1] = this.writeValue.getScalarValue();
				break;

			case AttrWriteType._WRITE:
				value = new Object[1];
				value[0] = this.writeValue.getScalarValue();
				break;

			case AttrWriteType._READ_WRITE:
				value = new Object[2];
				value[0] = this.readValue.getScalarValue();
				value[1] = this.writeValue.getScalarValue();
				break;
			}
			break;
		case AttrDataFormat._SPECTRUM:
			switch (this.permit) {
			case AttrWriteType._READ:
				value = new Object[1];
				value[0] = this.readValue.getSpectrumValue();
				break;

			case AttrWriteType._READ_WITH_WRITE:
				value = new Object[2];
				value[0] = this.readValue.getSpectrumValue();
				value[1] = this.writeValue.getSpectrumValue();
				break;

			case AttrWriteType._WRITE:
				value = new Object[1];
				value[0] = this.writeValue.getSpectrumValue();
				break;

			case AttrWriteType._READ_WRITE:
				value = new Object[2];
				value[0] = this.readValue.getSpectrumValue();
				value[1] = this.writeValue.getSpectrumValue();
				break;
			}
			break;
		default: // nothing to do
		}

		value = getValueTable(value);

		if (value != null) {
			if (value.length == 1) {
				valueAfter = value[0];
			} else {
				valueAfter = value;
			}
		}

		ret.setValue(valueAfter);
		return ret;
	}

	/**
	 * Returns a XML representation of the attribute.
	 * 
	 * @return a XML representation of the attribute
	 */
	public String toString() {
		String ret = "";

		XMLLine openingLine = new XMLLine(SnapshotAttribute.XML_TAG,
				XMLLine.OPENING_TAG_CATEGORY);
		XMLLine closingLine = new XMLLine(SnapshotAttribute.XML_TAG,
				XMLLine.CLOSING_TAG_CATEGORY);

		openingLine.setAttribute(SnapshotAttribute.ID_PROPERTY_XML_TAG, String
				.valueOf(this.attribute_id));
		openingLine.setAttribute(SnapshotAttribute.DATA_TYPE_PROPERTY_XML_TAG,
				String.valueOf(this.data_type));
		openingLine.setAttribute(
				SnapshotAttribute.DATA_FORMAT_PROPERTY_XML_TAG, String
						.valueOf(this.data_format));
		openingLine.setAttribute(SnapshotAttribute.WRITABLE_PROPERTY_XML_TAG,
				String.valueOf(this.permit));
		openingLine.setAttribute(
				SnapshotAttribute.COMPLETE_NAME_PROPERTY_XML_TAG, String
						.valueOf(this.attribute_complete_name));

		ret += openingLine.toString();
		ret += GUIUtilities.CRLF;

		if (this.readValue != null) {
			ret += readValue.toXMLString();
			ret += GUIUtilities.CRLF;
		}
		if (this.writeValue != null) {
			ret += writeValue.toXMLString();
			ret += GUIUtilities.CRLF;
		}

		ret += closingLine.toString();

		return ret;
	}

	/**
	 * Extracts the read or write value from the raw <code>value</code> Object.
	 * First, value is cast to an Object []; the index is used to tell the
	 * method whether one wants the read value, or the write value.
	 * 
	 * @param value
	 *            The Object containing the read/write values
	 * @param pos
	 *            The index to take the value at, once <code>value</code> has
	 *            been cast to an Object []
	 * @return The read/write value
	 */
	private Object getValue(Object value, int pos) {
		try {
			switch (this.data_type) {
			case TangoConst.Tango_DEV_STRING:
				return (String) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_STATE:
				return (Integer) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_UCHAR:
				return (Byte) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_LONG:
				return (Integer) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_ULONG:
				return (Integer) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_BOOLEAN:
				return (Boolean) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_SHORT:
				return (Short) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_FLOAT:
				return (Float) ((Object[]) value)[pos];
			case TangoConst.Tango_DEV_DOUBLE:
				return (Double) ((Object[]) value)[pos];
			default:
				return (Double) ((Object[]) value)[pos];
			}
		} catch (Exception e) {
			return "ERROR";
		}
	}

	/**
	 * Casts all elements of <code>valueIn</code> to a type depending of the
	 * attribute <code>data_type</code>, and returns the result.
	 * 
	 * @param valueIn
	 *            The raw Object[] value
	 * @return The cast value
	 */
	private Object[] getValueTable(Object[] valueIn) {
		if (valueIn == null) {
			return null;
		}

		int l = valueIn.length;
		Object[] valueOut = new Object[l];
		for (int i = 0; i < l; i++) {
			valueOut[i] = SnapshotAttributeValue.castValue(valueIn[i],
					this.data_type);
		}

		return valueOut;
	}

	/**
	 * Sets the modified state of an attribute.
	 * 
	 * @param b
	 *            The new modified state
	 */
	public void setModified(boolean b) {
		if (this.writeValue == null) {
			return;
		}

		this.writeValue.setModified(b);
	}

	/**
	 * @return Returns the attribute_complete_name.
	 */
	public String getAttribute_complete_name() {
		return attribute_complete_name;
	}

	/**
	 * @param attribute_complete_name
	 *            The attribute_complete_name to set.
	 */
	public void setAttribute_complete_name(String attribute_complete_name) {
		this.attribute_complete_name = attribute_complete_name;
	}

	/**
	 * @return Returns the attribute_id.
	 */
	public int getAttribute_id() {
		return attribute_id;
	}

	/**
	 * @param attribute_id
	 *            The attribute_id to set.
	 */
	public void setAttribute_id(int attribute_id) {
		this.attribute_id = attribute_id;
	}

	/**
	 * @return Returns the data_format.
	 */
	public int getData_format() {
		return data_format;
	}

	/**
	 * @param data_format
	 *            The data_format to set.
	 */
	public void setData_format(int data_format) {
		this.data_format = data_format;
	}

	/**
	 * @return Returns the data_type.
	 */
	public int getData_type() {
		return data_type;
	}

	/**
	 * @param data_type
	 *            The data_type to set.
	 */
	public void setData_type(int data_type) {
		this.data_type = data_type;
	}

	/**
	 * @return Returns the deltaValue.
	 */
	public SnapshotAttributeDeltaValue getDeltaValue() {
		return deltaValue;
	}

	/**
	 * @param deltaValue
	 *            The deltaValue to set.
	 */
	public void setDeltaValue(SnapshotAttributeDeltaValue deltaValue) {
		this.deltaValue = deltaValue;
		if (this.deltaValue != null) {
			this.deltaValue.setDisplayFormat(display_format);
		}
	}

	/**
	 * @return Returns the permit.
	 */
	public int getPermit() {
		return permit;
	}

	/**
	 * @param permit
	 *            The permit to set.
	 */
	public void setPermit(int permit) {
		this.permit = permit;
	}

	/**
	 * @return Returns the readValue.
	 */
	public SnapshotAttributeReadValue getReadValue() {
		return readValue;
	}

	/**
	 * @param readValue
	 *            The readValue to set.
	 */
	public void setReadValue(SnapshotAttributeReadValue readValue) {
		this.readValue = readValue;
		if (this.readValue != null) {
			this.readValue.setDisplayFormat(display_format);
		}
	}

	/**
	 * @return Returns the snapshotAttributes.
	 */
	public SnapshotAttributes getSnapshotAttributes() {
		return snapshotAttributes;
	}

	/**
	 * @param snapshotAttributes
	 *            The snapshotAttributes to set.
	 */
	public void setSnapshotAttributes(SnapshotAttributes snapshotAttributes) {
		this.snapshotAttributes = snapshotAttributes;
	}

	/**
	 * @return Returns the writeValue.
	 */
	public SnapshotAttributeWriteValue getWriteValue() {
		return writeValue;
	}

	/**
	 * @param writeValue
	 *            The writeValue to set.
	 */
	public void setWriteValue(SnapshotAttributeWriteValue writeValue) {
		this.writeValue = writeValue;
		if (this.writeValue != null) {
			this.writeValue.setDisplayFormat(display_format);
		}
	}

	/**
	 * @return the display format of this attribute. Exemple : "%6.3f"
	 */
	public String getDisplayFormat() {
		return display_format;
	}

	/**
	 * sets the display format
	 */
	public void setDisplayFormat(String format) {
		this.display_format = format;
		if (this.readValue != null) {
			this.readValue.setDisplayFormat(display_format);
		}
		if (this.writeValue != null) {
			this.writeValue.setDisplayFormat(display_format);
		}
		if (this.deltaValue != null) {
			this.deltaValue.setDisplayFormat(display_format);
		}
	}

	/**
	 * @return
	 */
	public String toUserFriendlyString() {
		String ret = "";
		Options options = Options.getInstance();
		SnapshotOptions snapshotOptions = options.getSnapshotOptions();
		String separator = snapshotOptions.getCSVSeparator();

		ret += String.valueOf(this.attribute_complete_name);
		ret += separator;

		if (this.readValue != null) {
			ret += readValue.toUserFriendlyString();
		}
		ret += separator;

		if (this.writeValue != null) {
			ret += writeValue.toUserFriendlyString();
		}
		ret += separator;

		if (this.deltaValue != null) {
			ret += deltaValue.toUserFriendlyString();
		}

		return ret;
	}

	/**
	 * @param attribute
	 * @return
	 */
	public static SnapshotAttribute createDummyAttribute(
			SnapshotAttribute attribute) {
		SnapshotAttribute ret = new SnapshotAttribute();

		ret.setAttribute_complete_name(attribute.getAttribute_complete_name());

		SnapshotAttributeReadValue readValue = new SnapshotAttributeReadValue(
				0, 0, null);
		readValue.setNotApplicable(true);
		ret.setReadValue(readValue);

		SnapshotAttributeWriteValue writeValue = new SnapshotAttributeWriteValue(
				0, 0, null);
		writeValue.setNotApplicable(true);
		ret.setWriteValue(writeValue);

		SnapshotAttributeDeltaValue deltaValue = new SnapshotAttributeDeltaValue(
				writeValue, readValue);
		deltaValue.setNotApplicable(true);
		ret.setDeltaValue(deltaValue);

		ret.setData_type(attribute.getData_type());
		ret.setData_format(attribute.getData_format());

		return ret;
	}
}
