package fr.soleil.mambo.datasources.db.extracting;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.NullableTimedData;

public class DbDataExtractor {

    public static JLDataView[] extractScalarData(DbData recoveredData) {
        if (recoveredData == null) {
            return null;
        } else {
            if (recoveredData.getData_format() == AttrDataFormat._SCALAR) {
                DbData[] splitedData;
                try {
                    splitedData = recoveredData.splitDbData();
                } catch (DevFailed e) {
                    e.printStackTrace();
                    return null;
                }
                JLDataView[] result;
                switch (recoveredData.getWritable()) {
                    case AttrWriteType._READ:
                        result = new JLDataView[2];
                        result[0] = extractScalarDataView(splitedData[0],
                                recoveredData.getData_type());
                        result[1] = null;
                        break;
                    case AttrWriteType._WRITE:
                        result = new JLDataView[2];
                        result[0] = null;
                        result[1] = extractScalarDataView(splitedData[1],
                                recoveredData.getData_type());
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                    case AttrWriteType._READ_WRITE:
                        result = new JLDataView[2];
                        result[0] = extractScalarDataView(splitedData[0],
                                recoveredData.getData_type());
                        result[1] = extractScalarDataView(splitedData[1],
                                recoveredData.getData_type());
                        break;
                    default:
                        result = null;
                        break;
                }
                return result;
            } else {
                // Not a scalar
                return null;
            }
        } // end if (recoveredData == null) ... else
    }

    private static JLDataView extractScalarDataView(DbData data, int dataType) {
        if ((data == null) || (data.getData_timed() == null)) {
            return null;
        } else {
            JLDataView dataView = null;
            NullableTimedData[] timedAttrData = data.getData_timed();
            switch (dataType) {
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_LONG:
                case TangoConst.Tango_DEV_FLOAT:
                case TangoConst.Tango_DEV_DOUBLE:
                    dataView = new JLDataView();
                    for (int i = 0; i < timedAttrData.length; i++) {
                        long sec_l = timedAttrData[i].time.longValue();
                        if (timedAttrData[i].value == null) {
                            dataView.add(sec_l, JLDataView.NAN_FOR_NULL);
                        } else {
                            for (int j = 0; j < timedAttrData[i].value.length; j++) {
                                double value = JLDataView.NAN_FOR_NULL;
                                if (timedAttrData[i].value[j] != null) {
                                    value = ((Number) timedAttrData[i].value[j])
                                            .doubleValue();
                                }
                                dataView.add(sec_l, value);
                            }
                        }
                    }
                    break;
                case TangoConst.Tango_DEV_BOOLEAN:
                    dataView = new JLDataView();
                    for (int i = 0; i < timedAttrData.length; i++) {
                        long sec_l = timedAttrData[i].time.longValue();
                        if (timedAttrData[i].value == null) {
                            dataView.add(sec_l, JLDataView.NAN_FOR_NULL);
                        } else {
                            for (int j = 0; j < timedAttrData[i].value.length; j++) {
                                double value = JLDataView.NAN_FOR_NULL;
                                if (timedAttrData[i].value[j] != null) {
                                    value = ((Boolean) timedAttrData[i].value[j])
                                            .booleanValue() ? 1 : 0;
                                }
                                dataView.add(sec_l, value);
                            }
                        }
                    }
                    break;
            }
            return dataView;
        }
    }

}
