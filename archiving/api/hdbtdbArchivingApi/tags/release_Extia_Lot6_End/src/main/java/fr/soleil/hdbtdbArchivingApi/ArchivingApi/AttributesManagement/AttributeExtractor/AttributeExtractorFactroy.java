/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.DataGettersFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.InfSupGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.MinMaxAvgGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.DataGettersBetweenDatesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.InfSupGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.MinMaxAvgGettersBetweenDates;

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

	public static AttributeExtractor getAttributeExtractor(int arch_type) {
		AttributeExtractor extractor = new AttributeExtractor(arch_type);
		extractor.dataGetters = DataGettersFactory.getDataGatters(arch_type);
		extractor.dataGettersBetweenDates = DataGettersBetweenDatesFactory
				.getDataGatters(arch_type);
		extractor.infSupGetters = new InfSupGetters(arch_type);
		extractor.infSupGettersBetweenDates = new InfSupGettersBetweenDates(
				arch_type);
		extractor.minMaxAvgGetters = new MinMaxAvgGetters(arch_type);
		extractor.minMaxAvgGettersBetweenDates = new MinMaxAvgGettersBetweenDates(
				arch_type);
		return extractor;
	}
}
