package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

/**
 * Bean representing an attribute that caused an ArchivingException to be thrown
 * 
 * @author awo
 *
 */
public class FaultingAttribute {

    private String name;
    private String cause;

    // Constructor
    public FaultingAttribute(String name, String cause) {
        this.name = name;
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Attribute name: ");
        buffer.append(this.name);
        buffer.append("\nError reason: ");
        buffer.append(this.cause);
        return buffer.toString();
    }
}
