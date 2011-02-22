package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.NullableTimedData;

public class HdbTdbDbData extends DbData
{
	public HdbTdbDbData(String name)
	{
		super(name);
	}
	
	private static ArchivingException generateException(String cause , int cause_value , String name)
	{
		String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
		String reason = "Failed while executing AttributeSupport.checkAttributeSupport()...";
		String desc = cause + " (" + cause_value + ") not supported !! [" + name + "]";
		return new ArchivingException(message , reason , ErrSeverity.WARN , desc , "");
	}
	   
    /**
     * Method used for spectrum extraction
     * @param values a Vector of SpectrumEvent_RO
     * @throws ArchivingException 26 janv. 2006
     */
    public void setData(Vector values) throws ArchivingException
    {
        if (values == null)
        {
            this.timedData = null;
            return;
        }
        int data_size = values.size();
        //System.out.println("values size :"+data_size);
        NullableTimedData[] data = new NullableTimedData[ data_size ];
        switch ( data_format )
        {
            case AttrDataFormat._SPECTRUM:
                
                switch ( writable )
                {
                    case AttrWriteType._WRITE:
                        for (int i = 0; i < data_size; i++)
                        {
                            SpectrumEvent_RO event = (SpectrumEvent_RO)values.get(i);
                            //System.out.println ( "CLA/DbData/setData/this.getMax_x ()/"+this.getMax_x ()+"/event.getDim_x()/"+event.getDim_x() );
                            //added CLA 30/01/06
                            if ( event.getDim_x() > this.getMax_x () )
                            {
                                this.setMax_x ( event.getDim_x() );    
                            }
                            
                            Long time = new Long(event.getTimeStamp());
                            Boolean[] bval;
                            Double[] dval;
                            Float[] fval;
                            Integer[] ival;
                            Short[] sval;
                            String[] stval;
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_SHORT:
                                case TangoConst.Tango_DEV_USHORT:
                                    sval = (Short[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_LONG:
                                case TangoConst.Tango_DEV_ULONG:
                                    ival = (Integer[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    fval = (Float[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = fval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    bval = (Boolean[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = bval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    dval = (Double[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = dval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    stval = (String[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = stval;
                                    data[i].x = event.getDim_x();
                                    break;
                                default:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                            }
                        }
                        //System.out.println("data length :" + data.length );
                        this.setData_timed(data);
                        break;
                    case AttrWriteType._READ:
                        for (int i = 0; i < data_size; i++)
                        {
                            SpectrumEvent_RO event = (SpectrumEvent_RO)values.get(i);
                            //System.out.println ( "CLA/DbData/setData/this.getMax_x ()/"+this.getMax_x ()+"/event.getDim_x()/"+event.getDim_x() );
                            //added CLA 30/01/06
                            if ( event.getDim_x() > this.getMax_x () )
                            {
                                this.setMax_x ( event.getDim_x() );    
                            }
                            
                            Long time = new Long(event.getTimeStamp());
                            Boolean[] bval;
                            Double[] dval;
                            Float[] fval;
                            Integer[] ival;
                            Short[] sval;
                            String[] stval;
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_SHORT:
                                case TangoConst.Tango_DEV_USHORT:
                                    sval = (Short[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_STATE:
                                case TangoConst.Tango_DEV_LONG:
                                case TangoConst.Tango_DEV_ULONG:
                                    ival = (Integer[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    fval = (Float[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = fval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    bval = (Boolean[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                   data[i].time = time;
                                    data[i].value = bval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    dval = (Double[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = dval;
                                    data[i].x = event.getDim_x();
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    stval = (String[])event.getValue();
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = stval;
                                    data[i].x = event.getDim_x();
                                    break;
                                default:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                            }
                        }
                        //System.out.println("data length :" + data.length );
                        this.setData_timed(data);
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                    case AttrWriteType._READ_WRITE:
                    
                    for (int i = 0; i < data_size; i++)
                    {
                        SpectrumEvent_RW event = (SpectrumEvent_RW)values.get(i);
                        
                        if ( event.getDim_x() > this.getMax_x () )
                        {
                            this.setMax_x ( event.getDim_x() );    
                        }
                        
                        Long time = new Long(event.getTimeStamp());
                        Boolean[] bval;
                        Double[] dval;
                        Float[] fval;
                        Integer[] ival;
                        Short[] sval;
                        String[] stval;
                        switch ( data_type )
                        {
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                                sval = (Short[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                               data[i].time = time;
                                data[i].value = sval;
                                data[i].x = event.getDim_x();
                                break;
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                                ival = (Integer[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                                data[i].time = time;
                                data[i].value = ival;
                                data[i].x = event.getDim_x();
                                break;
                            case TangoConst.Tango_DEV_FLOAT:
                                fval = (Float[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                                data[i].time = time;
                                data[i].value = fval;
                                data[i].x = event.getDim_x();
                                break;
                            case TangoConst.Tango_DEV_BOOLEAN:
                                bval = (Boolean[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                                data[i].time = time;
                                data[i].value = bval;
                                data[i].x = event.getDim_x();
                                break;
                            case TangoConst.Tango_DEV_DOUBLE:
                                dval = (Double[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                                data[i].time = time;
                                data[i].value = dval;
                                data[i].x = event.getDim_x();
                                break;
                            case TangoConst.Tango_DEV_STRING:
                                stval = (String[])event.getValue();
                                data[i] = new NullableTimedData();
                                data[i].data_type = data_type;
                                data[i].time = time;
                                data[i].value = stval;
                                data[i].x = event.getDim_x();
                              break;
                            default:
                                throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                        }
                    }
                    this.setData_timed(data);
                    break;
                    default:
                        throw generateException(GlobalConst.DATA_WRITABLE_EXCEPTION , writable , name);
                }
                break;
            case AttrDataFormat._SCALAR:
                //System.out.println("$$$$$scalar format$$$$$");
                switch ( writable )
                {
                    case AttrWriteType._WRITE:
                    case AttrWriteType._READ:
                        //System.out.println("$$$$$scalar format 1 val$$$$$");
                        for (int i = 0; i < data_size; i++)
                        {
                            ScalarEvent_RO event = (ScalarEvent_RO)values.get(i);
                            Long time = new Long(event.getTimeStamp());
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_STATE:
                                    Integer[] ival = new Integer[1];
                                    if (event.getScalarValue() == null)
                                    {
                                        ival[0] = null;
                                    }
                                    else
                                    {
                                        ival[0] = new Integer( (event.getScalarValue()).intValue() );
                                                              }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    String[] sval = new String[1];
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = 1;
                                    break;
                                default:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                            }
                        }
                        this.setData_timed(data);
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                    case AttrWriteType._READ_WRITE:
                        //System.out.println("$$$$$scalar format 2 vals$$$$$");
                        for (int i = 0; i < data_size; i++)
                        {
                            ScalarEvent_RW event = (ScalarEvent_RW)values.get(i);
                            Long time = new Long(event.getTimeStamp());
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_STATE:
                                    Integer[] ival = new Integer[2];
                                    if (event.getScalarValueRW()[0] == null)
                                    {
                                        ival[0] = null;
                                    }
                                    else
                                    {
                                        ival[0] = new Integer( event.getScalarValueRW()[0].intValue() );
                                    }
                                    if (event.getScalarValueRW()[1] == null)
                                    {
                                        ival[1] = null;
                                    }
                                    else
                                    {
                                        ival[1] = new Integer( event.getScalarValueRW()[1].intValue() );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    String[] sval = new String[2];
                                    sval = event.getScalarValueRWS();
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = 1;
                                    break;
                                default:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                            }
                        }
                        this.setData_timed(data);
                        break;
                    default:
                        throw generateException(GlobalConst.DATA_WRITABLE_EXCEPTION , writable , name);
                }
                break;
            case AttrDataFormat._IMAGE:
                this.setMax_x(0);
                this.setMax_y(0);
                switch ( writable )
                {
                    case AttrWriteType._READ:
                        for (int i = 0; i < data_size; i++)
                        {
                            ImageEvent_RO event = (ImageEvent_RO)values.get(i);
                            if ( event.getDim_x() > this.getMax_x () )
                            {
                                this.setMax_x ( event.getDim_x() );    
                            }
                            if ( event.getDim_y() > this.getMax_y () )
                            {
                                this.setMax_y ( event.getDim_y() );    
                            }

                            Long time = new Long(event.getTimeStamp());
                            Boolean[] bval;
                            Double[] dval;
                            Float[] fval;
                            Integer[] ival;
                            Short[] sval;
                            String[] stval;
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_SHORT:
                                case TangoConst.Tango_DEV_USHORT:
                                    Short[][] tempsval = (Short[][])event.getValue();
                                    if (tempsval == null || tempsval.length == 0 || tempsval[0].length == 0)
                                    {
                                        sval = new Short[0];
                                    }
                                    else
                                    {
                                        sval = new Short[tempsval.length * tempsval[0].length];
                                        int svalindex = 0;
                                        for (int j = 0; j < tempsval.length; j++)
                                        {
                                            for (int k = 0; k < tempsval[0].length; k++)
                                            {
                                                sval[svalindex++] = tempsval[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                case TangoConst.Tango_DEV_STATE:
                                case TangoConst.Tango_DEV_LONG:
                                case TangoConst.Tango_DEV_ULONG:
                                    Integer[][] tempival = (Integer[][])event.getValue();
                                    if (tempival == null || tempival.length == 0 || tempival[0].length == 0)
                                    {
                                        ival = new Integer[0];
                                    }
                                    else
                                    {
                                        ival = new Integer[tempival.length * tempival[0].length];
                                        int ivalindex = 0;
                                        for (int j = 0; j < tempival.length; j++)
                                        {
                                            for (int k = 0; k < tempival[0].length; k++)
                                            {
                                                ival[ivalindex++] = tempival[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    Float[][] tempfval = (Float[][])event.getValue();
                                    if (tempfval == null || tempfval.length == 0 || tempfval[0].length == 0)
                                    {
                                        fval = new Float[0];
                                    }
                                    else
                                    {
                                        fval = new Float[tempfval.length * tempfval[0].length];
                                        int fvalindex = 0;
                                        for (int j = 0; j < tempfval.length; j++)
                                        {
                                            for (int k = 0; k < tempfval[0].length; k++)
                                            {
                                                fval[fvalindex++] = tempfval[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = fval;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    Boolean[][] tempbval = (Boolean[][])event.getValue();
                                    if (tempbval == null || tempbval.length == 0 || tempbval[0].length == 0)
                                    {
                                        bval = new Boolean[0];
                                    }
                                    else
                                    {
                                        bval = new Boolean[tempbval.length * tempbval[0].length];
                                        int bvalindex = 0;
                                        for (int j = 0; j < tempbval.length; j++)
                                        {
                                            for (int k = 0; k < tempbval[0].length; k++)
                                            {
                                                bval[bvalindex++] = tempbval[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = bval;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    Double[][] tempdval = (Double[][])event.getValue();
                                    if (tempdval == null || tempdval.length == 0 || tempdval[0].length == 0)
                                    {
                                        dval = new Double[0];
                                    }
                                    else
                                    {
                                        dval = new Double[tempdval.length * tempdval[0].length];
                                        int dvalindex = 0;
                                        for (int j = 0; j < tempdval.length; j++)
                                        {
                                            for (int k = 0; k < tempdval[0].length; k++)
                                            {
                                                dval[dvalindex++] = tempdval[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = dval;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    String[][] tempstval = (String[][])event.getValue();
                                    if (tempstval == null || tempstval.length == 0 || tempstval[0].length == 0)
                                    {
                                        stval = new String[0];
                                    }
                                    else
                                    {
                                        stval = new String[tempstval.length * tempstval[0].length];
                                        int stvalindex = 0;
                                        for (int j = 0; j < tempstval.length; j++)
                                        {
                                            for (int k = 0; k < tempstval[0].length; k++)
                                            {
                                                stval[stvalindex++] = tempstval[j][k];
                                            }
                                        }
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = stval;
                                    data[i].x = event.getDim_x();
                                    data[i].y = event.getDim_y();
                                    break;
                                default:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                            }
                        }

                        this.setData_timed(data);
                        break;
                    case AttrWriteType._WRITE:
                    case AttrWriteType._READ_WITH_WRITE:
                    case AttrWriteType._READ_WRITE:
                    default:
                        throw generateException(GlobalConst.DATA_WRITABLE_EXCEPTION , writable , name);
                }
                break;
            default:
                throw generateException(GlobalConst.DATA_FORMAT_EXCEPTION , data_format , name);
        }
    }

    
    public void setData(DevVarDoubleStringArray devVarDoubleStringArray) throws ArchivingException
    {
        if (devVarDoubleStringArray == null)
        {
            this.timedData = null;
            return;
        }
        int data_size = 0;
        if (data_type == TangoConst.Tango_DEV_STRING)
        {
            data_size = devVarDoubleStringArray.dvalue.length;
        }
        else
        {
            data_size = devVarDoubleStringArray.svalue.length;
        }
        NullableTimedData[] data = new NullableTimedData[ data_size ];

        for ( int i = 0 ; i < data.length ; i++ )
        {
            Long time = new Long(0);
            if (data_type == TangoConst.Tango_DEV_STRING)
            {
                time = new Long(( long ) devVarDoubleStringArray.dvalue[ i ]);
            }
            else
            {
                time = new Long(DateUtil.stringToMilli((String) devVarDoubleStringArray.svalue[i]));
            }
            Boolean[] bval;
            Double[] dval;
            Float[] fval;
            Integer[] ival;
            Short[] sval;
            String[] stval;
            switch ( data_format )
            {
                case AttrDataFormat._SCALAR:
                    switch ( writable )
                    {
                        case AttrWriteType._WRITE:
                        case AttrWriteType._READ:
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_SHORT:
                                case TangoConst.Tango_DEV_USHORT:
                                    sval = new Short[ 1 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        sval[0] = null;
                                    }
                                    else
                                    {
                                        sval[ 0 ] = new Short(( short ) devVarDoubleStringArray.dvalue[ i ]);
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_STATE:
                                case TangoConst.Tango_DEV_LONG:
                                case TangoConst.Tango_DEV_ULONG:
                                    ival = new Integer[ 1 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        ival[0] = null;
                                    }
                                    else
                                    {
                                        ival[ 0 ] = new Integer(( int ) devVarDoubleStringArray.dvalue[ i ]);
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    dval = new Double[ 1 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        long l = Double.doubleToRawLongBits(
                                                devVarDoubleStringArray.dvalue[i]
                                        );
                                        if (l == Double.doubleToRawLongBits(GlobalConst.NAN_FOR_NULL) ) {
                                            dval[0] = null;
                                        }
                                        else {
                                            dval[ 0 ] = new Double( devVarDoubleStringArray.dvalue[ i ] );
                                        }
                                    }
                                    else
                                    {
                                        dval[ 0 ] = new Double( devVarDoubleStringArray.dvalue[ i ] );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = dval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    fval = new Float[ 1 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        fval[0] = null;
                                    }
                                    else
                                    {
                                        fval[ 0 ] = new Float( ( float ) devVarDoubleStringArray.dvalue[ i ]);
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = fval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    bval = new Boolean[ 1 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        bval[0] = null;
                                    }
                                    else
                                    {
                                        bval[ 0 ] = new Boolean( ( devVarDoubleStringArray.dvalue[ i ] == 0 ) ? false : true );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = bval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_CHAR:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                                case TangoConst.Tango_DEV_UCHAR:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                                case TangoConst.Tango_DEV_STRING:
                                    stval = new String[ 1 ];
                                    if (devVarDoubleStringArray.svalue[ i ] == null || "null".equals(devVarDoubleStringArray.svalue[ i ]))
                                    {
                                        stval[ 0 ] = null;
                                    }
                                    else
                                    {
                                        stval[ 0 ] = devVarDoubleStringArray.svalue[ i ];
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = stval;
                                    data[i].x = 1;
                                    break;
                            }
                            break;
                        case AttrWriteType._READ_WRITE:
                        case AttrWriteType._READ_WITH_WRITE:
                            switch ( data_type )
                            {
                                case TangoConst.Tango_DEV_SHORT:
                                case TangoConst.Tango_DEV_USHORT:
                                    sval = new Short[ 2 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        sval[0] = null;
                                    }
                                    else
                                    {
                                        sval[ 0 ] = new Short( ( short ) devVarDoubleStringArray.dvalue[ i ] );
                                    }
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i + data_size]))
                                    {
                                        sval[1] = null;
                                    }
                                    else
                                    {
                                        sval[ 1 ] = new Short( ( short ) devVarDoubleStringArray.dvalue[ i + data_size ] );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = sval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_LONG:
                                case TangoConst.Tango_DEV_ULONG:
                                    ival = new Integer[ 2 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        ival[0] = null;
                                    }
                                    else
                                    {
                                        ival[ 0 ] = new Integer( ( int ) devVarDoubleStringArray.dvalue[ i ] );
                                    }
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i + data_size]))
                                    {
                                        ival[1] = null;
                                    }
                                    else
                                    {
                                        ival[ 1 ] = new Integer( ( int ) devVarDoubleStringArray.dvalue[ i + data_size ] );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = ival;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    dval = new Double[ 2 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        long l = Double.doubleToRawLongBits(
                                                devVarDoubleStringArray.dvalue[i]
                                        );
                                        if (l == Double.doubleToRawLongBits(GlobalConst.NAN_FOR_NULL) ) {
                                            dval[0] = null;
                                        }
                                        else {
                                            dval[ 0 ] = new Double( devVarDoubleStringArray.dvalue[ i ] );
                                        }
                                    }
                                    else
                                    {
                                        dval[ 0 ] = new Double( devVarDoubleStringArray.dvalue[ i ] );
                                    }
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i + data_size]))
                                    {
                                        long l = Double.doubleToRawLongBits(
                                                devVarDoubleStringArray.dvalue[i+data_size]
                                        );
                                        if (l == Double.doubleToRawLongBits(GlobalConst.NAN_FOR_NULL) ) {
                                            dval[1] = null;
                                        }
                                        else {
                                            dval[ 1 ] = new Double( devVarDoubleStringArray.dvalue[ i + data_size ] );
                                        }
                                    }
                                    else
                                    {
                                        dval[ 1 ] = new Double( devVarDoubleStringArray.dvalue[ i + data_size ] );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = dval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    fval = new Float[ 2 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        fval[0] = null;
                                    }
                                    else
                                    {
                                        fval[ 0 ] = new Float( ( float ) devVarDoubleStringArray.dvalue[ i ] );
                                    }
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i + data_size]))
                                    {
                                        fval[1] = null;
                                    }
                                    else
                                    {
                                        fval[ 1 ] = new Float( ( float ) devVarDoubleStringArray.dvalue[ i + data_size ] );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = fval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    bval = new Boolean[ 2 ];
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i]))
                                    {
                                        bval[0] = null;
                                    }
                                    else
                                    {
                                        bval[ 0 ] = new Boolean( ( devVarDoubleStringArray.dvalue[ i ] == 0 ) ? false : true );
                                    }
                                    if (Double.isNaN(devVarDoubleStringArray.dvalue[i + data_size]))
                                    {
                                        bval[1] = null;
                                    }
                                    else
                                    {
                                        bval[ 1 ] = new Boolean( ( devVarDoubleStringArray.dvalue[ i + data_size ] == 0 ) ? false : true );
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = bval;
                                    data[i].x = 1;
                                    break;
                                case TangoConst.Tango_DEV_CHAR:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                                case TangoConst.Tango_DEV_UCHAR:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                                case TangoConst.Tango_DEV_STATE:
                                    throw generateException(GlobalConst.DATA_TYPE_EXCEPTION , data_type , name);
                                case TangoConst.Tango_DEV_STRING:
                                    stval = new String[ 2 ];
                                    if (devVarDoubleStringArray.svalue[ i ] == null || "null".equals(devVarDoubleStringArray.svalue[ i ]))
                                    {
                                        stval[ 0 ] = null;
                                    }
                                    else
                                    {
                                        stval[ 0 ] = devVarDoubleStringArray.svalue[ i ];
                                    }
                                    if (devVarDoubleStringArray.svalue[ i + data_size ] == null || "null".equals(devVarDoubleStringArray.svalue[ i + data_size ]))
                                    {
                                        stval[ 1 ] = null;
                                    }
                                    else
                                    {
                                        stval[ 1 ] = devVarDoubleStringArray.svalue[ i + data_size ];
                                    }
                                    data[i] = new NullableTimedData();
                                    data[i].data_type = data_type;
                                    data[i].time = time;
                                    data[i].value = stval;
                                    data[i].x = 1;
                                    break;
                            }
                            break;
                        default:
                            throw generateException(GlobalConst.DATA_WRITABLE_EXCEPTION , writable , name);
                    }
                    break;
                case AttrDataFormat._SPECTRUM:
                case AttrDataFormat._IMAGE:
                default:
                    throw generateException(GlobalConst.DATA_FORMAT_EXCEPTION , data_format , name);
            }
        }
        timedData = data;
    }

}
