/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import fr.esrf.Tango.AttrDataFormat;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class MinMaxAvgGettersBetweenDates extends DataExtractor {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public MinMaxAvgGettersBetweenDates(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	/**
	 * <b>Description : </b>  Returns the lower value recorded for the given attribute
	 * and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The lower scalar data for the specified attribute and for the specified period
	 * @throws ArchivingException
	 */
	public double getAttDataMinBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att =AdtAptAttributesFactory.getInstance(arch_type);
		if(att == null)
			return 0;
		String att_name = argin[ 0 ].trim();
		String time_0 = argin[ 1 ].trim();
		String time_1 = argin[ 2 ].trim();
		double min = 0;

		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				min = getAttScalarDataMinBetweenDates(att_name , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		return min;
	}
	/**
	 * <b>Description : </b>  Returns the upper value recorded for the given attribute during a given period.
	 * and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The upper scalar data for the specified attribute and for the specified period.
	 * @throws ArchivingException
	 */
	public double getAttDataMaxBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att =AdtAptAttributesFactory.getInstance(arch_type);
		if(att == null)
			return 0;
		String att_name = argin[ 0 ].trim();
		String time_0 = argin[ 1 ].trim();
		String time_1 = argin[ 2 ].trim();
		double max = 0;

		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
	{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				max = getAttScalarDataMaxBetweenDates(att_name , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		return max;
	}
	/**
	 * <b>Description : </b>    	Returns the mean value for the given attribute and during a given period.
	 * and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The mean scalar data for the specified attribute and for the specified period.
	 * @throws ArchivingException
	 */
	public double getAttDataAvgBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att =AdtAptAttributesFactory.getInstance(arch_type);
		if(att == null)
			return 0;
 		String att_name = argin[ 0 ].trim();
		String time_0 = argin[ 1 ].trim();
		String time_1 = argin[ 2 ].trim();
		double avg = 0;
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				avg = getAttScalarDataAvgBetweenDates(att_name , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		return avg;
	}
	/*
	 * 
	 */
	private double getAttScalarDataMaxBetweenDates(String att_name , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		return methods.getAttScalarDataMinMaxAvgBetweenDates(att_name, time_0, time_1, writable, "MAX");
	}
	/*
	 * 
	 */
	private double getAttScalarDataMinBetweenDates(String att_name , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		return methods.getAttScalarDataMinMaxAvgBetweenDates(att_name, time_0, time_1, writable, "MIN");
	}
	/*
	 * 
	 */
	private double getAttScalarDataAvgBetweenDates(String att_name , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		return methods.getAttScalarDataMinMaxAvgBetweenDates(att_name, time_0, time_1, writable, "AVG");
	}

}
