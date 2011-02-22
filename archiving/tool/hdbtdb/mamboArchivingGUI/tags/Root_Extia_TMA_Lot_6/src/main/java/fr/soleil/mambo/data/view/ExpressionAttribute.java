package fr.soleil.mambo.data.view;

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ExpressionAttribute {
    private String                                   name;
    private String                                   expression;
    private double                                   factor;
    private String[]                                 variables;
    private boolean                                  x;
    private ViewConfigurationAttributePlotProperties properties;

    public final static byte                         RO_MODE                      = 0;           // read
    // values
    // only
    public final static byte                         WO_MODE                      = 1;           // write
    // values
    // only
    public final static byte                         AR_MODE                      = 2;           // auto
    // read
    // =
    // read
    // values
    // only,
    // except
    // for write only attributes
    public final static byte                         AW_MODE                      = 3;           // auto
    // write
    // =
    // write
    // values
    // only,
    // except for read only attributes

    public static final String                       XML_TAG                      = "expression";
    public static final String                       NAME_PROPERTY_XML_TAG        = "name";
    public static final String                       FACTOR_PROPERTY_XML_TAG      = "factor";
    public static final String                       VALUE_PROPERTY_XML_TAG       = "value";
    public static final String                       VARIABLES_PROPERTY_XML_TAG   = "variables";
    public static final String                       X_PROPERTY_XML_TAG           = "isX";

    public static final String                       VARIABLES_PROPERTY_DELIMITER = ", ";

    public ExpressionAttribute(String name, String expression,
            String[] variables, boolean x) {
        this.name = name;
        this.expression = expression;
        this.properties = new ViewConfigurationAttributePlotProperties();
        this.variables = variables;
        this.x = x;
    }

    /**
     * Generate an XML string representation of this expression attribute.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
        openingLine.setAttribute(NAME_PROPERTY_XML_TAG, getName());
        openingLine.setAttribute(FACTOR_PROPERTY_XML_TAG, getFactor() + "");
        openingLine.setAttribute(VALUE_PROPERTY_XML_TAG, getExpression());

        StringBuilder var = new StringBuilder();
        String[] variables = getVariables();
        for (int i = 0; i < variables.length; i++) {
            String variable = variables[i];
            var.append(variable);
            if (i < variables.length - 1) {
                var.append(VARIABLES_PROPERTY_DELIMITER);
            }
        }
        openingLine.setAttribute(VARIABLES_PROPERTY_XML_TAG, var.toString());

        openingLine.setAttribute(X_PROPERTY_XML_TAG, isX() + "");
        XMLLine closingLine = new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

        ret.append(openingLine.toString());
        ret.append(GUIUtilities.CRLF);

        if (properties != null) {
            ret.append(properties.toString());
            ret.append(GUIUtilities.CRLF);
        }

        ret.append(closingLine.toString());

        return ret.toString();
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double _factor) {
        factor = _factor;
    }

    /**
     * @return Returns the properties.
     */
    public ViewConfigurationAttributePlotProperties getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            The properties to set.
     */
    public void setProperties(
            ViewConfigurationAttributePlotProperties properties) {
        this.properties = properties;
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public ExpressionAttribute duplicate() {
        String[] variables2 = null;
        if (variables != null) {
            variables2 = new String[variables.length];
            for (int i = 0; i < variables.length; i++) {
                variables2[i] = new String(variables[i]);
            }
        }
        ExpressionAttribute attribute = new ExpressionAttribute(
                new String(name), new String(expression), variables2, x);
        attribute.setFactor(factor);
        ViewConfigurationAttributePlotProperties properties2 = new ViewConfigurationAttributePlotProperties();
        properties2.setAxisChoice(properties.getAxisChoice());
        properties2.setHidden(properties.isHidden());
        properties2.setBar(properties.getBar());
        properties2.setCurve(properties.getCurve());
        properties2.setMarker(properties.getMarker());
        properties2.setTransform(properties.getTransform());
        properties2.setViewType(properties.getViewType());
        attribute.setProperties(properties2);
        return attribute;
    }

    public void prepareView(ChartViewer chart) {
        if (properties != null) {
            Marker marker = properties.getMarker();
            Curve curve = properties.getCurve();
            Bar bar = properties.getBar();
            // if the properties aren't set we don't add the attribute to the
            // plot
            IChartViewer chartComponent = chart.getComponent();
            if (marker != null) {
                chartComponent.setDataViewMarkerCometeColor(getName(),
                        CometeColor.getColor(marker.getColor()));
                chartComponent.setDataViewMarkerSize(getName(), marker
                        .getSize());
                chartComponent.setDataViewMarkerStyle(getName(), marker
                        .getStyle());
            }
            if (curve != null) {
                chartComponent.setDataViewLineStyle(getName(), curve
                        .getLineStyle());
                chartComponent
                        .setDataViewLineWidth(getName(), curve.getWidth());
                chartComponent.setDataViewCometeColor(getName(), CometeColor
                        .getColor(curve.getColor()));
                chartComponent
                        .setDataViewLineWidth(getName(), curve.getWidth());
            }
            if (bar != null) {
                chartComponent.setDataViewCometeFillColor(getName(),
                        CometeColor.getColor(bar.getFillColor()));
                chartComponent.setDataViewFillMethod(getName(), bar
                        .getFillingMethod());
                chartComponent.setDataViewFillStyle(getName(), bar
                        .getFillStyle());
                chartComponent.setDataViewBarWidth(getName(), bar.getWidth());
            }
            int axis = properties.getAxisChoice();
            chartComponent.setDataViewAxis(getName(), axis);
            chartComponent
                    .setDataViewStyle(getName(), properties.getViewType());
        }
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    public boolean isX() {
        return x;
    }

    public void setX(boolean x) {
        this.x = x;
    }

}
