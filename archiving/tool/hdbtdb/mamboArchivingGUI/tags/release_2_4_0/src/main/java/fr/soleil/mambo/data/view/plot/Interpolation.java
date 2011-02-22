package fr.soleil.mambo.data.view.plot;

import fr.soleil.comete.widget.properties.InterpolationProperties;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Interpolation extends InterpolationProperties {
    public static final String XML_TAG                               = "interpolation";
    public static final String INTERPOLATION_METHOD_PROPERTY_XML_TAG = "interpolationMethod";
    public static final String INTERPOLATION_STEP_PROPERTY_XML_TAG   = "interpolationStep";
    public static final String HERMITE_BIAS_PROPERTY_XML_TAG         = "hermiteBias";
    public static final String HERMITE_TENSION_PROPERTY_XML_TAG      = "hermiteTension";

    public Interpolation() {
        super();
    }

    public Interpolation(int interpolationMethod, int interpolationStep,
            double hermiteBias, double hermiteTension) {
        super(interpolationMethod, interpolationStep, hermiteBias,
                hermiteTension);
    }

    public Interpolation(InterpolationProperties interpolation) {
        super();
        if (interpolation != null) {
            try {
                InterpolationProperties clone = (InterpolationProperties) interpolation
                        .clone();
                setHermiteBias(clone.getHermiteBias());
                setHermiteTension(clone.getHermiteTension());
                setInterpolationMethod(clone.getInterpolationMethod());
                setInterpolationStep(clone.getInterpolationStep());
            } catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(INTERPOLATION_METHOD_PROPERTY_XML_TAG, String
                .valueOf(getInterpolationMethod()));
        openingLine.setAttribute(INTERPOLATION_STEP_PROPERTY_XML_TAG, String
                .valueOf(getInterpolationStep()));
        openingLine.setAttribute(HERMITE_BIAS_PROPERTY_XML_TAG, String
                .valueOf(getHermiteBias()));
        openingLine.setAttribute(HERMITE_TENSION_PROPERTY_XML_TAG, String
                .valueOf(getHermiteTension()));

        return openingLine.toString();
    }
}
