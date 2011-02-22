package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

public class DataExtractor {
	protected int arch_type;
	protected GenericExtractorMethods methods;

	public DataExtractor(int type) {
		// TODO Auto-generated constructor stub
		this.arch_type = type;
		methods = new GenericExtractorMethods(type);
	}

}
