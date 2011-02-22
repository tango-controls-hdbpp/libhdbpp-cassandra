package fr.soleil.mambo.data.view;

import java.util.ArrayList;
import java.util.List;

import chart.temp.chart.JLDataView;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.NullableTimedData;

public class ViewScalarDAO extends AbstractDAO<List<DataArray>> {
    private List<DataArray> arrayList = new ArrayList<DataArray>();
    public static final String READ_DATA_VIEW_KEY = "read";
    public static final String WRITE_DATA_VIEW_KEY = "write";

    public ViewScalarDAO() {

    }

    private DataArray extractScalarDataView(DbData data, int dataType) {
        if ((data == null) || (data.getData_timed() == null)) {
            return null;
        }
        else {
            DataArray result = null;
            NullableTimedData[] timedAttrData = data.getData_timed();
            switch (dataType) {
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_LONG:
                case TangoConst.Tango_DEV_FLOAT:
                case TangoConst.Tango_DEV_DOUBLE:
                    result = new DataArray();
                    for (int i = 0; i < timedAttrData.length; i++) {
                        long sec_l = timedAttrData[i].time.longValue();
                        if (timedAttrData[i].value == null) {
                            result.add(sec_l, JLDataView.NAN_FOR_NULL);
                        }
                        else {
                            for (int j = 0; j < timedAttrData[i].value.length; j++) {
                                double value = JLDataView.NAN_FOR_NULL;
                                if (timedAttrData[i].value[j] != null) {
                                    value = ((Number) timedAttrData[i].value[j]).doubleValue();
                                }
                                result.add(sec_l, value);
                            }
                        }
                    }
                    break;
                case TangoConst.Tango_DEV_BOOLEAN:
                    result = new DataArray();
                    for (int i = 0; i < timedAttrData.length; i++) {
                        long sec_l = timedAttrData[i].time.longValue();
                        if (timedAttrData[i].value == null) {
                            result.add(sec_l, JLDataView.NAN_FOR_NULL);
                        }
                        else {
                            for (int j = 0; j < timedAttrData[i].value.length; j++) {
                                double value = JLDataView.NAN_FOR_NULL;
                                if (timedAttrData[i].value[j] != null) {
                                    value = ((Boolean) timedAttrData[i].value[j]).booleanValue() ? 1
                                            : 0;
                                }
                                result.add(sec_l, value);
                            }
                        }
                    }
                    break;
            }
            return result;
        }
    }

    public void addData(DbData data) {
        if (data != null) {
            if (data.getData_format() == AttrDataFormat._SCALAR) {
                DbData[] splitedData = null;
                try {
                    splitedData = data.splitDbData();
                }
                catch (DevFailed e) {
                    e.printStackTrace();
                }
                if (splitedData != null) {
                    DataArray result = null;
                    switch (data.getWritable()) {
                        case AttrWriteType._READ:
                            result = extractScalarDataView(splitedData[0], data.getData_type());
                            result.setId(data.getName() + "/" + READ_DATA_VIEW_KEY);
                            arrayList.add(result);
                            break;
                        case AttrWriteType._WRITE:
                            result = extractScalarDataView(splitedData[1], data.getData_type());
                            result.setId(data.getName() + "/" + WRITE_DATA_VIEW_KEY);
                            arrayList.add(result);
                            break;
                        case AttrWriteType._READ_WITH_WRITE:
                        case AttrWriteType._READ_WRITE:
                            result = extractScalarDataView(splitedData[0], data.getData_type());
                            result.setId(data.getName() + "/" + READ_DATA_VIEW_KEY);
                            arrayList.add(result);
                            result = extractScalarDataView(splitedData[1], data.getData_type());
                            result.setId(data.getName() + "/" + WRITE_DATA_VIEW_KEY);
                            arrayList.add(result);
                            break;
                        default:
                            break;
                    }
                }
            }
        } // end if (recoveredData == null) ... else
        fireDAOChanged();
    }

    public void addData(DataArray data) {
        if (data != null) {
            arrayList.add(data);
            fireDAOChanged();
        }
    }

    @Override
    public List<DataArray> getData() {
        return arrayList;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public boolean getReadOnly() {
        return false;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void setData(List<DataArray> value) {
        arrayList = value;
    }
}
