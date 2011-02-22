/*
 * Synchrotron Soleil File : BufferedAttrDBManagerImpl.java Project : mambo
 * Description : Author : CLAISSE Original : 10 mai 2006 Revision: Author: Date:
 * State: Log: BufferedAttrDBManagerImpl.java,v
 */
/*
 * Created on 10 mai 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.attributes;

import java.util.Hashtable;
import java.util.Map;

public class BufferedFormatsAttributeManager extends BasicAttributeManager {

	private Map<String, Boolean> isScalarBuffer;
	private Map<String, Boolean> isSpectrumBuffer;
	private Map<String, Boolean> isImageBuffer;

	private static final int MAX_BUFFER_SIZE_ATTRIBUTES_TYPES = 10000;

	private static final String SEPARATOR = "|";

	public BufferedFormatsAttributeManager() {
		super();

		this.isScalarBuffer = new Hashtable<String, Boolean>(
				MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
		this.isSpectrumBuffer = new Hashtable<String, Boolean>(
				MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
		this.isImageBuffer = new Hashtable<String, Boolean>(
				MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
	}

	public boolean isScalar(String completeName, boolean _historic) {
		String key = completeName + SEPARATOR + _historic;
		Boolean isScalar = (Boolean) this.isScalarBuffer.get(key);

		if (isScalar == null) {
			boolean newResult = super.isScalar(completeName, _historic);
			isScalarBuffer.put(key, new Boolean(newResult));
			return newResult;
		} else {
			return isScalar.booleanValue();
		}
	}

	public boolean isSpectrum(String completeName, boolean _historic) {
		String key = completeName + SEPARATOR + _historic;
		Boolean isSpectrum = (Boolean) this.isSpectrumBuffer.get(key);

		if (isSpectrum == null) {
			boolean newResult = super.isSpectrum(completeName, _historic);
			isSpectrumBuffer.put(key, new Boolean(newResult));
			return newResult;
		} else {
			return isSpectrum.booleanValue();
		}
	}

	public boolean isImage(String completeName, boolean _historic) {
		String key = completeName + SEPARATOR + _historic;
		Boolean isImage = (Boolean) this.isImageBuffer.get(key);

		if (isImage == null) {
			boolean newResult = super.isImage(completeName, _historic);
			isImageBuffer.put(key, new Boolean(newResult));
			return newResult;
		} else {
			return isImage.booleanValue();
		}
	}
}
