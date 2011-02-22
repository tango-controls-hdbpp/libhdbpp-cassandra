/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.DataGettersFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.InfSupGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.MinMaxAvgGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.DataGettersBetweenDatesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.InfSupGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.MinMaxAvgGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;

/**
 * @author AYADI
 *
 */
public class AttributeExtractorFactroy {

	/**
	 * 
	 */
	public AttributeExtractorFactroy() {
		// TODO Auto-generated constructor stub
	}
	
	public static AttributeExtractor getAttributeExtractor(IDBConnection con, IAdtAptAttributes at){
		AttributeExtractor extractor = new AttributeExtractor(con, at);
		switch(DbUtils.getDbType(con.getDbType())){
		case ConfigConst.BD_MYSQL:{
			extractor.dataGetters = DataGettersFactory.getDataGatters(con, at, ConfigConst.BD_MYSQL);
			extractor.dataGettersBetweenDates = DataGettersBetweenDatesFactory.getDataGatters(con, at, ConfigConst.BD_MYSQL);
			break;
		}
		case ConfigConst.BD_ORACLE: {
			extractor.dataGetters = DataGettersFactory.getDataGatters(con,  at, ConfigConst.BD_ORACLE);
			extractor.dataGettersBetweenDates = DataGettersBetweenDatesFactory.getDataGatters(con, at, ConfigConst.BD_ORACLE);
		
			break;
		}
		default: 
			return null;
		}
		extractor.infSupGetters = new InfSupGetters(con, at);
		extractor.infSupGettersBetweenDates = new InfSupGettersBetweenDates(con,  at);
		extractor.minMaxAvgGetters = new MinMaxAvgGetters(con,  at);
		extractor.minMaxAvgGettersBetweenDates = new MinMaxAvgGettersBetweenDates(con,  at);
		return extractor;
	}
}
