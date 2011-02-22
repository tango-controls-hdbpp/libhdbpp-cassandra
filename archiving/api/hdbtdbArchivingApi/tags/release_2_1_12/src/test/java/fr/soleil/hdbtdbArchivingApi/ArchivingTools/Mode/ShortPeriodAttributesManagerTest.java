package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class ShortPeriodAttributesManagerTest extends TestCase {

    public ShortPeriodAttributesManagerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws ArchivingException {
        ShortPeriodAttributesManager.getShortPeriodAttributes();
        
        // To test initialization with GetConf, comment this and adapt test cases
        Map<String,Integer> testMap = new HashMap<String,Integer>();
        testMap.put("domain/family/member",3);
        // You can add as many other attributes as you wish
        // testMap.put("domain/family/member1",4);
        ShortPeriodAttributesManager.setShortPeriodAttributes(testMap);
    }

    protected void tearDown() {
        ShortPeriodAttributesManager.setShortPeriodAttributes(null);
    }

    @org.junit.Test
    public void testGetShortPeriodAttributes() throws ArchivingException {
        assertFalse(ShortPeriodAttributesManager.getShortPeriodAttributes().isEmpty());
    }

    @org.junit.Test
    public void testIsShortPeriodAttribute() throws ArchivingException {
        assertTrue(ShortPeriodAttributesManager.isShortPeriodAttribute("domain/family/member"));
        assertFalse(ShortPeriodAttributesManager.isShortPeriodAttribute("domain/family"));
        assertFalse(ShortPeriodAttributesManager.isShortPeriodAttribute("3"));
        assertFalse(ShortPeriodAttributesManager.isShortPeriodAttribute(""));
    }

    @org.junit.Test
    public void testGetPeriodFromShortAttributeName() throws ArchivingException {
        assertTrue(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("") == null);
        assertFalse("3".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("")));
        assertFalse("0".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("")));
        assertFalse(new Integer(3).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("")));
        assertFalse(new Integer(0).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("")));

        assertTrue(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family") == null);
        assertFalse("3".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family")));
        assertFalse("0".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family")));
        assertFalse(new Integer(3).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family")));
        assertFalse(new Integer(0).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family")));

        assertTrue(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("3") == null);
        assertFalse("3".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("3")));
        assertFalse("0".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("3")));
        assertFalse(new Integer(3).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("3")));
        assertFalse(new Integer(0).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("3")));

        assertFalse(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family/member") == null);
        assertFalse("3".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family/member")));
        assertFalse("0".equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family/member")));
        assertTrue(new Integer(3).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family/member")));
        assertFalse(new Integer(0).equals(ShortPeriodAttributesManager.getPeriodFromShortAttributeName("domain/family/member")));
    }

    // XXX: WARNING /!\
    // Check with Jive if there is a property named shortPeriodAttributes in
    // HdbArchiver device class
    public static Test suite() {
        TestSuite suite = new TestSuite(ShortPeriodAttributesManagerTest.class);
        return suite;
    }
}
