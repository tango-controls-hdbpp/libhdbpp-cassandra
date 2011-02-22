package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

import java.util.ArrayList;
import java.util.List;

import fr.esrf.Tango.ErrSeverity;

/**
 * Class dedicated to trace which attributes have caused this ArchivingException
 * to be thrown
 * 
 * @author awo
 */
public class AttributesArchivingException extends ArchivingException {

    private static final long       serialVersionUID   = 6036844349350117539L;

    // List of attributes that have caused this exception
    private List<FaultingAttribute> faultingAttributes = new ArrayList<FaultingAttribute>();

    // Default constructor
    public AttributesArchivingException() {
        super();
    }

    // Same constructor as superclass, for backward-compatibility
    public AttributesArchivingException(String message, String reason,
            ErrSeverity archSeverity, String desc, String origin) {
        super(message, reason, archSeverity, desc, origin);
    }

    public List<FaultingAttribute> getFaultingAttributes() {
        return faultingAttributes;
    }

    public void setFaultingAttributes(List<FaultingAttribute> faultingAttributes) {
        this.faultingAttributes = faultingAttributes;
    }
}
