package fr.soleil.commonarchivingapi.ArchivingTools.Tools;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.TimeVal;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.TimedAttrData;

//+======================================================================
// $Source: $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DbData.
//						(chinkumo) - 31 août 2005
//
// $Author: ounsy $
//O
// $Revision: 1.22 $
//
// $Log: DbData.java,v $
// Revision 1.22  2007/05/10 14:59:21  ounsy
// NaN and null distinction
//
// Revision 1.21  2007/02/28 09:46:37  ounsy
// better null values management
//
// Revision 1.20  2006/11/20 09:31:28  ounsy
// minor changes
//
// Revision 1.19  2006/11/09 14:19:57  ounsy
// addded a missing break in setData
//
// Revision 1.18  2006/10/31 16:54:23  ounsy
// milliseconds and null values management
//
// Revision 1.17  2006/08/29 13:56:36  ounsy
// setValue(XXX) : taking care of case where parameter is null
//
// Revision 1.16  2006/07/18 10:42:27  ounsy
// image support
//
// Revision 1.15  2006/05/05 14:07:50  ounsy
// corrected the "No Data" bug on write-only attributes
//
// Revision 1.14  2006/04/05 13:50:50  ounsy
// new types full support
//
// Revision 1.13  2006/03/27 15:20:01  ounsy
// new spectrum types support + better spectrum management + new scalar types full support
//
// Revision 1.12  2006/03/15 16:07:56  ounsy
// states converted to their string value only in GUI
//
// Revision 1.11  2006/03/15 15:11:13  ounsy
// minor changes
//
// Revision 1.10  2006/03/14 12:52:11  ounsy
// corrected the SNAP/spectrums/RW problem
// about the read and write values having the same length
//
// Revision 1.9  2006/03/10 11:31:00  ounsy
// state and string support
//
// Revision 1.8  2006/02/16 10:37:01  ounsy
// code cleaning
//
// Revision 1.7  2006/02/16 09:57:27  ounsy
// No more confusion between table length and dim x
// splitDbData() allows read and write value to be of different sizes.
// splitDbData() [0] allways corresponds to the read value
// splitDbData() [1] allways corresponds to the write value
//
// Revision 1.6  2006/02/15 14:54:49  ounsy
// indexOutOfBounds avoided
//
// Revision 1.5  2006/02/07 11:54:11  ounsy
// removed useless logs
//
// Revision 1.4  2006/02/06 12:42:20  ounsy
// -corrected a bug in the case of successive spectrums with different sizes
// -added RW spectrum support
// -adapted the spitDbData method for spectrum attributes
// -added a debug method "traceShort"
//
// Revision 1.3  2006/01/27 14:13:33  ounsy
// spectrum extraction allowed
//
// Revision 1.2  2005/11/29 17:11:17  chinkumo
// no message
//
// Revision 1.1.2.3  2005/11/15 13:34:38  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/26 08:30:08  chinkumo
// no message
//
// Revision 1.1.2.1  2005/09/09 08:21:24  chinkumo
// First commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

public class DbData implements java.io.Serializable {

	protected String name;
	/**
	 * Max size for spectrums and images
	 */
	int max_x;
	int max_y;

	/**
	 * Data type
	 */
	protected int data_type;
	/**
	 * Data format
	 */
	protected int data_format;
	/**
	 * Writable
	 */
	protected int writable;

	/**
	 * Data found in HDB/TDB
	 */
	protected NullableTimedData[] timedData;

	public DbData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getData_type() {
		return data_type;
	}

	public void setData_type(int data_type) {
		this.data_type = data_type;
	}

	public int getData_format() {
		return data_format;
	}

	public void setData_format(int data_format) {
		this.data_format = data_format;
	}

	public int getWritable() {
		return writable;
	}

	public void setWritable(int writable) {
		this.writable = writable;
	}

	public int getMax_x() {
		return max_x;
	}

	public void setMax_x(int max_x) {
		this.max_x = max_x;
	}

	public int getMax_y() {
		return max_y;
	}

	public void setMax_y(int max_y) {
		this.max_y = max_y;
	}

	public NullableTimedData[] getData_timed() {
		return timedData;
	}

	public void setData_timed(NullableTimedData[] data) {
		this.timedData = data;
	}

	public void traceShort() {
		System.out.println("traceShort------------------------------------");
		if (this.timedData == null) {
			System.out.println("this.timedAttrData == null");
			return;
		}
		if (this.timedData.length == 0) {
			System.out.println("this.timedAttrData.length == 0");
			return;
		}
		NullableTimedData aNullableTimedData = this.timedData[this.timedData.length - 1];
		Short[] sh_ptr = (Short[]) aNullableTimedData.value;
		for (int j = 0; j < sh_ptr.length; j++) {
			System.out.println("j/" + j + "/sh_ptr[j]/" + sh_ptr[j]);
		}
		System.out.println("traceShort------------------------------------");
	}

	public int size() {
		return (timedData != null) ? timedData.length : 0;
	}

	/**
	 * This method is used to cope with today attribute tango java limitations
	 * (read/write attribute are not supported !!).
	 * 
	 * @return A two DbData object array; the first DbData is for read values;
	 *         the second one for write values.
	 */
	public DbData[] splitDbData() throws DevFailed {
		DbData[] argout = new DbData[2];
		DbData dbData_r = new DbData(name);
		dbData_r.setData_type(data_type);
		dbData_r.setData_format(data_format);
		dbData_r.setWritable(writable);
		dbData_r.setMax_x(max_x);
		dbData_r.setMax_y(max_y);

		DbData dbData_w = new DbData(name);
		dbData_w.setData_type(data_type);
		dbData_w.setData_format(data_format);
		dbData_w.setWritable(writable);
		dbData_w.setMax_x(0);
		dbData_w.setMax_y(max_y);

		if (timedData == null) {
			dbData_r.timedData = null;
			dbData_w.timedData = null;
			dbData_r.max_x = 0;
			dbData_r.max_y = 0;
			dbData_w.max_x = 0;
			dbData_w.max_y = 0;
			argout[0] = dbData_r;
			argout[1] = dbData_w;
			return argout;
		}

		NullableTimedData[] timedAttrData_r = new NullableTimedData[timedData.length];
		NullableTimedData[] timedAttrData_w = new NullableTimedData[timedData.length];
		for (int i = 0; i < timedData.length; i++) {
			int dimWrite = 0;
			switch (this.getData_type()) {
			case TangoConst.Tango_DEV_USHORT:
			case TangoConst.Tango_DEV_SHORT:
				Short[] sh_ptr_init = (Short[]) timedData[i].value;
				Short[] sh_ptr_read = null;
				Short[] sh_ptr_write = null;
				if (sh_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						sh_ptr_read = new Short[timedData[i].x * timedData[i].y];
						dimWrite = sh_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						sh_ptr_read = new Short[timedData[i].x];
						dimWrite = sh_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}

					if (dimWrite < 0)
						dimWrite = 0;
					sh_ptr_write = new Short[dimWrite];
					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							sh_ptr_read[j] = sh_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < sh_ptr_init.length; j++) {
							sh_ptr_write[j - timedData[i].x] = sh_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							sh_ptr_read[j] = sh_ptr_init[j];
						}
						for (int j = timedData[i].x; j < sh_ptr_init.length; j++) {
							sh_ptr_write[j - timedData[i].x] = sh_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = sh_ptr_write;
					timedAttrData_w[i].value = sh_ptr_read;
				} else {
					timedAttrData_r[i].value = sh_ptr_read;
					timedAttrData_w[i].value = sh_ptr_write;
				}
				break;

			case TangoConst.Tango_DEV_DOUBLE:
				Double[] db_ptr_init = (Double[]) timedData[i].value;
				Double[] db_ptr_read = null;
				Double[] db_ptr_write = null;
				if (db_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						db_ptr_read = new Double[timedData[i].x
								* timedData[i].y];
						dimWrite = db_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						db_ptr_read = new Double[timedData[i].x];
						dimWrite = db_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}
					if (dimWrite < 0)
						dimWrite = 0;
					db_ptr_write = new Double[dimWrite];

					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							db_ptr_read[j] = db_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < db_ptr_init.length; j++) {
							db_ptr_write[j - timedData[i].x] = db_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							db_ptr_read[j] = db_ptr_init[j];
						}
						for (int j = timedData[i].x; j < db_ptr_init.length; j++) {
							db_ptr_write[j - timedData[i].x] = db_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = db_ptr_write;
					timedAttrData_w[i].value = db_ptr_read;
				} else {
					timedAttrData_r[i].value = db_ptr_read;
					timedAttrData_w[i].value = db_ptr_write;
				}
				break;

			case TangoConst.Tango_DEV_FLOAT:
				Float[] fl_ptr_init = (Float[]) timedData[i].value;
				Float[] fl_ptr_read = null;
				Float[] fl_ptr_write = null;
				if (fl_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						fl_ptr_read = new Float[timedData[i].x * timedData[i].y];
						dimWrite = fl_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						fl_ptr_read = new Float[timedData[i].x];
						dimWrite = fl_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}
					if (dimWrite < 0)
						dimWrite = 0;
					fl_ptr_write = new Float[dimWrite];

					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							fl_ptr_read[j] = fl_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < fl_ptr_init.length; j++) {
							fl_ptr_write[j - timedData[i].x] = fl_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							fl_ptr_read[j] = fl_ptr_init[j];
						}
						for (int j = timedData[i].x; j < fl_ptr_init.length; j++) {
							fl_ptr_write[j - timedData[i].x] = fl_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = fl_ptr_write;
					timedAttrData_w[i].value = fl_ptr_read;
				} else {
					timedAttrData_r[i].value = fl_ptr_read;
					timedAttrData_w[i].value = fl_ptr_write;
				}
				break;

			case TangoConst.Tango_DEV_STATE:
			case TangoConst.Tango_DEV_ULONG:
			case TangoConst.Tango_DEV_LONG:
				Integer[] lg_ptr_init = (Integer[]) timedData[i].value;
				Integer[] lg_ptr_read = null;
				Integer[] lg_ptr_write = null;
				if (lg_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						lg_ptr_read = new Integer[timedData[i].x
								* timedData[i].y];
						dimWrite = lg_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						lg_ptr_read = new Integer[timedData[i].x];
						dimWrite = lg_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}
					if (dimWrite < 0)
						dimWrite = 0;
					lg_ptr_write = new Integer[dimWrite];

					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							lg_ptr_read[j] = lg_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < lg_ptr_init.length; j++) {
							lg_ptr_write[j - timedData[i].x] = lg_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							lg_ptr_read[j] = lg_ptr_init[j];
						}
						for (int j = timedData[i].x; j < lg_ptr_init.length; j++) {
							lg_ptr_write[j - timedData[i].x] = lg_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = lg_ptr_write;
					timedAttrData_w[i].value = lg_ptr_read;
				} else {
					timedAttrData_r[i].value = lg_ptr_read;
					timedAttrData_w[i].value = lg_ptr_write;
				}
				break;

			case TangoConst.Tango_DEV_BOOLEAN:
				Boolean[] bool_ptr_init = (Boolean[]) timedData[i].value;
				Boolean[] bool_ptr_read = null;
				Boolean[] bool_ptr_write = null;
				if (bool_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						bool_ptr_read = new Boolean[timedData[i].x
								* timedData[i].y];
						dimWrite = bool_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						bool_ptr_read = new Boolean[timedData[i].x];
						dimWrite = bool_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}
					if (dimWrite < 0)
						dimWrite = 0;
					bool_ptr_write = new Boolean[dimWrite];

					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							bool_ptr_read[j] = bool_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < bool_ptr_init.length; j++) {
							bool_ptr_write[j - timedData[i].x] = bool_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							bool_ptr_read[j] = bool_ptr_init[j];
						}
						for (int j = timedData[i].x; j < bool_ptr_init.length; j++) {
							bool_ptr_write[j - timedData[i].x] = bool_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = bool_ptr_write;
					timedAttrData_w[i].value = bool_ptr_read;
				} else {
					timedAttrData_r[i].value = bool_ptr_read;
					timedAttrData_w[i].value = bool_ptr_write;
				}
				break;

			case TangoConst.Tango_DEV_STRING:
				String[] str_ptr_init = (String[]) timedData[i].value;
				String[] str_ptr_read = null;
				String[] str_ptr_write = null;
				if (str_ptr_init != null) {
					if (data_format == AttrDataFormat._IMAGE) {
						str_ptr_read = new String[timedData[i].x
								* timedData[i].y];
						dimWrite = str_ptr_init.length
								- (timedData[i].x * timedData[i].y);
					} else {
						str_ptr_read = new String[timedData[i].x];
						dimWrite = str_ptr_init.length - timedData[i].x;
						if (dimWrite > dbData_w.getMax_x()) {
							dbData_w.setMax_x(dimWrite);
						}
					}
					if (dimWrite < 0)
						dimWrite = 0;
					str_ptr_write = new String[dimWrite];

					if (data_format == AttrDataFormat._IMAGE) {
						for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
							str_ptr_read[j] = str_ptr_init[j];
						}
						for (int j = timedData[i].x * timedData[i].y; j < str_ptr_init.length; j++) {
							str_ptr_write[j - timedData[i].x] = str_ptr_init[j];
						}
					} else {
						for (int j = 0; j < timedData[i].x; j++) {
							// System.out.println("&&&&&&str_ptr_init["+j+"]="+str_ptr_init[
							// j ]);
							str_ptr_read[j] = str_ptr_init[j];
						}
						for (int j = timedData[i].x; j < str_ptr_init.length; j++) {
							str_ptr_write[j - timedData[i].x] = str_ptr_init[j];
						}
					}
				}

				timedAttrData_r[i] = new NullableTimedData();
				timedAttrData_r[i].data_type = data_type;
				timedAttrData_r[i].x = timedData[i].x;
				timedAttrData_r[i].y = timedData[i].y;
				timedAttrData_r[i].time = timedData[i].time;
				timedAttrData_w[i] = new NullableTimedData();
				timedAttrData_w[i].data_type = data_type;
				timedAttrData_w[i].x = timedData[i].x;
				timedAttrData_w[i].y = timedData[i].y;
				timedAttrData_w[i].time = timedData[i].time;
				if (writable == AttrWriteType._WRITE) {
					timedAttrData_r[i].value = str_ptr_write;
					timedAttrData_w[i].value = str_ptr_read;
				} else {
					timedAttrData_r[i].value = str_ptr_read;
					timedAttrData_w[i].value = str_ptr_write;
				}
				break;
			}
		}
		dbData_r.setData_timed(timedAttrData_r);
		dbData_w.setData_timed(timedAttrData_w);
		switch (writable) {
		case AttrWriteType._READ:
			argout[0] = dbData_r;
			argout[1] = null;
			break;
		case AttrWriteType._WRITE:
			argout[0] = null;
			argout[1] = dbData_w;
			break;
		default:
			argout[0] = dbData_r;
			argout[1] = dbData_w;
		}
		return argout;
	}

	public TimedAttrData[] getDataAsTimedAttrData() {
		TimedAttrData[] attrData = null;
		if (timedData == null) {
			return null;
		} else {
			attrData = new TimedAttrData[timedData.length];
		}
		for (int i = 0; i < timedData.length; i++) {
			if (timedData[i] == null) {
				attrData[i] = null;
			} else {
				int sec = 0;
				if (timedData[i].time != null) {
					sec = (int) (timedData[i].time.longValue() / 1000);
				}
				TimeVal timeVal = new TimeVal(sec, 0, 0);
				switch (this.data_type) {
				case TangoConst.Tango_DEV_BOOLEAN:
					boolean[] boolval;
					if (timedData[i].value == null) {
						boolval = null;
					} else {
						boolval = new boolean[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							boolean value = false;
							if (timedData[i].value[j] != null) {
								value = ((Boolean) timedData[i].value[j])
										.booleanValue();
							}
							boolval[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(boolval, timeVal);
					boolval = null;
					break;
				case TangoConst.Tango_DEV_SHORT:
				case TangoConst.Tango_DEV_USHORT:
					// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!!"
					// );
					short[] sval;
					if (timedData[i].value == null) {
						// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 0"
						// );
						sval = null;
					} else {
						sval = new short[timedData[i].value.length];
						// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 1/sval.l/"+sval.length
						// );
						for (int j = 0; j < timedData[i].value.length; j++) {
							// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 2"
							// );
							short value = 0;
							if (timedData[i].value[j] != null) {
								// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 3"
								// );
								value = ((Short) timedData[i].value[j])
										.shortValue();
							}
							// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 4"
							// );
							sval[j] = value;
							// System.out.println("CLA/DbData/getDataAsTimedAttrData/Tango_DEV_SHORT!!!!! 5/j/"+j+"/sval[j]/"+sval[j]
							// );
						}
					}
					attrData[i] = new TimedAttrData(sval, timeVal);
					// System.out.println("CLA/DbData/getDataAsTimedAttrData/timeVal.tv_sec/"+timeVal.tv_sec+"/timeVal.tv_nsec/"+timeVal.tv_nsec+"/timeVal.tv_usec/"+timeVal.tv_usec
					// );
					sval = null;
					break;
				case TangoConst.Tango_DEV_LONG:
				case TangoConst.Tango_DEV_ULONG:
					int[] ival;
					if (timedData[i].value == null) {
						ival = null;
					} else {
						ival = new int[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							int value = 0;
							if (timedData[i].value[j] != null) {
								value = ((Integer) timedData[i].value[j])
										.intValue();
							}
							ival[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(ival, timeVal);
					ival = null;
					break;
				case TangoConst.Tango_DEV_FLOAT:
					float[] fval;
					if (timedData[i].value == null) {
						fval = null;
					} else {
						fval = new float[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							float value = 0;
							if (timedData[i].value[j] != null) {
								value = ((Float) timedData[i].value[j])
										.floatValue();
							}
							fval[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(fval, timeVal);
					fval = null;
					break;
				case TangoConst.Tango_DEV_DOUBLE:
					double[] dval;
					if (timedData[i].value == null) {
						dval = null;
					} else {
						dval = new double[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							double value = Double.NaN;
							if (timedData[i].value[j] != null) {
								value = ((Double) timedData[i].value[j])
										.doubleValue();
							}
							dval[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(dval, timeVal);
					dval = null;
					break;

				case TangoConst.Tango_DEV_STRING:
					String[] strval;
					if (timedData[i].value == null) {
						strval = null;
					} else {
						strval = new String[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							String value = null;
							if (timedData[i].value[j] != null) {
								value = new String(
										(String) timedData[i].value[j]);
							}
							strval[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(strval, timeVal);
					strval = null;
					break;

				case TangoConst.Tango_DEV_STATE:
					String[] strstate;
					if (timedData[i].value == null) {
						strstate = null;
					} else {
						strstate = new String[timedData[i].value.length];
						for (int j = 0; j < timedData[i].value.length; j++) {
							String value = null;
							if (timedData[i].value[j] != null) {
								value = new String(
										TangoStateTranslation
												.getStrStateValue((Integer) (timedData[i].value[j])));
							}
							strstate[j] = value;
						}
					}
					attrData[i] = new TimedAttrData(strstate, timeVal);
					strstate = null;
					break;
				default:
					attrData[i] = new TimedAttrData(new DevError[0], timeVal);
				}
				attrData[i].qual = timedData[i].qual;
				attrData[i].x = timedData[i].x;
				attrData[i].y = timedData[i].y;
				attrData[i].data_type = this.data_type;
				timeVal = null;
			}
		}
		return attrData;
	}
}
