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

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class BufferedFormatsAttributeManager extends BasicAttributeManager {

    private final Map<String, Boolean> isScalarBuffer;
    private final Map<String, Boolean> isSpectrumBuffer;
    private final Map<String, Boolean> isImageBuffer;

    private static final int MAX_BUFFER_SIZE_ATTRIBUTES_TYPES = 10000;

    private static final String SEPARATOR = "|";

    public BufferedFormatsAttributeManager() {
	super();

	isScalarBuffer = new Hashtable<String, Boolean>(MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
	isSpectrumBuffer = new Hashtable<String, Boolean>(MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
	isImageBuffer = new Hashtable<String, Boolean>(MAX_BUFFER_SIZE_ATTRIBUTES_TYPES);
    }

    @Override
    public boolean isScalar(final String completeName, final boolean _historic) throws ArchivingException {
	final String key = completeName + SEPARATOR + _historic;
	final Boolean isScalar = isScalarBuffer.get(key);

	if (isScalar == null) {
	    final boolean newResult = super.isScalar(completeName, _historic);
	    isScalarBuffer.put(key, new Boolean(newResult));
	    return newResult;
	} else {
	    return isScalar.booleanValue();
	}
    }

    @Override
    public boolean isSpectrum(final String completeName, final boolean _historic) {
	final String key = completeName + SEPARATOR + _historic;
	final Boolean isSpectrum = isSpectrumBuffer.get(key);

	if (isSpectrum == null) {
	    final boolean newResult = super.isSpectrum(completeName, _historic);
	    isSpectrumBuffer.put(key, new Boolean(newResult));
	    return newResult;
	} else {
	    return isSpectrum.booleanValue();
	}
    }

    @Override
    public boolean isImage(final String completeName, final boolean _historic) {
	final String key = completeName + SEPARATOR + _historic;
	final Boolean isImage = isImageBuffer.get(key);

	if (isImage == null) {
	    final boolean newResult = super.isImage(completeName, _historic);
	    isImageBuffer.put(key, new Boolean(newResult));
	    return newResult;
	} else {
	    return isImage.booleanValue();
	}
    }
}
