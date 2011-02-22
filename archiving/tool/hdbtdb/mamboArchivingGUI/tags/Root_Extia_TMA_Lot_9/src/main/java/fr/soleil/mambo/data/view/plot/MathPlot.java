package fr.soleil.mambo.data.view.plot;

import fr.soleil.comete.widget.properties.MathProperties;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class MathPlot extends MathProperties {
    public static final String XML_TAG                   = "math";
    public static final String FUNCTION_PROPERTY_XML_TAG = "function";

    public MathPlot() {
        super();
    }

    public MathPlot(int function) {
        super(function);
    }

    public MathPlot(MathProperties math) {
        super();
        if (math != null) {
            try {
                MathProperties clone = (MathProperties) math.clone();
                setFunction(clone.getFunction());
            } catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(FUNCTION_PROPERTY_XML_TAG, String
                .valueOf(getFunction()));

        return openingLine.toString();
    }
}
