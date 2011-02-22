package fr.soleil.mambo.data.view.plot;

import fr.soleil.comete.widget.properties.SmoothingProperties;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Smoothing extends SmoothingProperties {

    public static final String XML_TAG                        = "smoothing";
    public static final String METHOD_PROPERTY_XML_TAG        = "method";
    public static final String NEIGHBORS_PROPERTY_XML_TAG     = "neighbors";
    public static final String GAUSS_SIGMA_PROPERTY_XML_TAG   = "gaussSigma";
    public static final String EXTRAPOLATION_PROPERTY_XML_TAG = "extrapolation";

    public Smoothing() {
        super();
    }

    public Smoothing(int method, int neighbors, double gaussSigma,
            int extrapolation) {
        super(method, neighbors, gaussSigma, extrapolation);
    }

    public Smoothing(SmoothingProperties smoothing) {
        super();
        if (smoothing != null) {
            try {
                SmoothingProperties clone = (SmoothingProperties) smoothing
                        .clone();
                setExtrapolation(clone.getExtrapolation());
                setGaussSigma(clone.getGaussSigma());
                setMethod(clone.getMethod());
                setNeighbors(clone.getNeighbors());
            } catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(METHOD_PROPERTY_XML_TAG, String
                .valueOf(getMethod()));
        openingLine.setAttribute(NEIGHBORS_PROPERTY_XML_TAG, String
                .valueOf(getNeighbors()));
        openingLine.setAttribute(GAUSS_SIGMA_PROPERTY_XML_TAG, String
                .valueOf(getGaussSigma()));
        openingLine.setAttribute(EXTRAPOLATION_PROPERTY_XML_TAG, String
                .valueOf(getExtrapolation()));

        return openingLine.toString();
    }
}
