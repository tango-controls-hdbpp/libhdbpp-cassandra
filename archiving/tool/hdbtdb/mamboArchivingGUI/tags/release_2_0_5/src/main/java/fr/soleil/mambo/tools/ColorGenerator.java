package fr.soleil.mambo.tools;

import java.awt.Color;

/**
 * A class to generate colors.
 * 
 * @author GIRARDOT
 */
public class ColorGenerator
{
    /**
     * Generates a table of colors, all different, avoiding a specified color.
     * 
     * @param nbColors
     *            The wished number of colors (size of the table).
     * @param avoidColorRGB
     *            The RGB value of the color to avoid.
     * @return A <code>Color[]</code>, of size <code>nbColors</code>,
     *         containing colors which are all different, arranged as a rainbow.
     *         The table does not contain the specified Color.
     */
    public static Color[] generateColors (int nbColors, int avoidColorRGB)
    {
        Color[] colors = new Color[nbColors];
        PseudoGradient gradient = new PseudoGradient();
        int[] rgb;
        if (avoidColorRGB > 0)
        {
            rgb = gradient.buildColorMap(nbColors + 1);
        }
        else
        {
            rgb = gradient.buildColorMap(nbColors);
        }
        boolean jumped = false;
        for (int i = 0; i < nbColors; i++)
        {
            if (rgb[i] == avoidColorRGB)
            {
                jumped = true;
            }
            if (jumped)
            {
                colors[i] = new Color(rgb[i+1]);
            }
            else
            {
                colors[i] = new Color(rgb[i+1]);
            }
        }
        return colors;
    }

}

/**
 * Based on class <code>Gradient</code> in
 * <code>fr.esrf.tangoatk.widget.util</code> (does not extend this class, but
 * the code is based on the code of this class)
 * 
 * @author GIRARDOT
 * @see fr.esrf.tangoatk.widget.util.Gradient (atk widget package)
 */
class PseudoGradient
{
    protected Color[]  colorVal = null;
    protected double[] colorPos = null;

    /**
     * Construct a default gradient black to white
     */
    public PseudoGradient ()
    {
        colorVal = new Color[7];
        colorPos = new double[7];
        colorVal[0] = new Color( 255, 0, 0 ); // Red
        colorVal[1] = new Color( 255, 255, 0 ); // Yellow
        colorVal[2] = new Color( 0, 255, 0 ); // Green
        colorVal[3] = new Color( 0, 0, 255 ); // Blue
        colorVal[4] = new Color( 255, 0, 255 ); // Purple
        colorVal[5] = Color.BLACK;
        colorVal[6] = Color.WHITE;
        colorPos[0] = 0.0;
        colorPos[1] = 0.20;
        colorPos[2] = 0.40;
        colorPos[3] = 0.60;
        colorPos[4] = 0.80;
        colorPos[5] = 0.90;
        colorPos[6] = 1.0;
    }

    /**
     * Build a color map for this gradient.
     * 
     * @param nb
     *            Number of color for the colormap
     * @return a nb 32Bit [ARGB] array, null if fails
     */
    public int[] buildColorMap (int nb)
    {
        if ( colorVal == null ) return null;
        if ( colorVal.length <= 1 ) return null;
        int colId;
        colId = 0;
        int[] ret = new int[nb];
        for (int i = 0; i < nb; i++)
        {
            double r1, g1, b1;
            double r2, g2, b2;
            double r = (double) i / (double) nb;
            if ( colId < ( colorPos.length - 2 )
                    && r >= colorPos[colId + 1] ) colId++;
            r1 = (double) colorVal[colId].getRed();
            g1 = (double) colorVal[colId].getGreen();
            b1 = (double) colorVal[colId].getBlue();
            r2 = (double) colorVal[colId + 1].getRed();
            g2 = (double) colorVal[colId + 1].getGreen();
            b2 = (double) colorVal[colId + 1].getBlue();
            double rr = ( r - colorPos[colId] )
                    / ( colorPos[colId + 1] - colorPos[colId] );
            if ( rr < 0.0 ) rr = 0.0;
            if ( rr > 1.0 ) rr = 1.0;
            ret[i] = (int) ( r1 + ( r2 - r1 ) * rr ) * 65536
                    + (int) ( g1 + ( g2 - g1 ) * rr ) * 256
                    + (int) ( b1 + ( b2 - b1 ) * rr );
        }
        return ret;
    }
}
