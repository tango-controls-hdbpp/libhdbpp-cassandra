//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationAttributes.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfigurationAttributes.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.20 $
//
// $Log: ViewConfigurationAttributes.java,v $
// Revision 1.20  2007/03/28 13:24:45  ounsy
// boolean and number scalars displayed on the same panel (Mantis bug 4428)
//
// Revision 1.19  2007/03/20 14:15:09  ounsy
// added the possibility to hide a dataview
//
// Revision 1.18  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.17  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.16  2006/11/14 09:35:55  ounsy
// color rotation bug correction
//
// Revision 1.15  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.14  2006/11/07 14:35:17  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.13  2006/10/19 09:21:21  ounsy
// now the color rotation index of vc attributes is saved
//
// Revision 1.12  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.11  2006/09/05 14:03:12  ounsy
// updated for sampling compatibility
//
// Revision 1.10  2006/08/31 08:37:41  ounsy
// minor changes
//
// Revision 1.9  2006/08/29 14:19:27  ounsy
// now the view dialog is filled through a thread. Closing the dialog will kill this thread.
//
// Revision 1.8  2006/08/23 10:03:04  ounsy
// avoided a concurrent modification exception
//
// Revision 1.7  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.6  2006/07/18 10:27:49  ounsy
// Less time consuming by setting tree expanding on demand only + getImageAttributes
//
// Revision 1.5  2006/04/05 13:47:49  ounsy
// get String, state and boolean scalar attributes
//
// Revision 1.4  2006/03/10 12:03:25  ounsy
// state and string support
//
// Revision 1.3  2006/02/01 14:11:19  ounsy
// spectrum management
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.view;

import java.text.Collator;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.Family;
import fr.soleil.mambo.data.attributes.Member;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationAttributes
{
    private TreeMap attributes;
    private int colorIndex = 0;
    public final static String COLOR_INDEX_XML_TAG = "MamboVCAttributesColorIndex";
    public final static String COLOR_INDEX_XML_ATTRIBUTE = "VALUE";

    public String toString ()
    {
        String ret = "";

        XMLLine colorIndexEmptyLine = new XMLLine( COLOR_INDEX_XML_TAG , XMLLine.EMPTY_TAG_CATEGORY );
        colorIndexEmptyLine.setAttribute( COLOR_INDEX_XML_ATTRIBUTE, Integer.toString(colorIndex) );
        ret += colorIndexEmptyLine.toString();

        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            int i = 0;
            while ( keyIterator.hasNext() )
            {
                String nextKey = ( String ) keyIterator.next();
                ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attributes.get( nextKey );

                ret += nextValue.toString();
                if ( i < attributes.size() - 1 )
                {
                    ret += GUIUtilities.CRLF;
                }

                i++;
            }
        }

        return ret;
    }

    public boolean equals ( ViewConfigurationAttributes this2 )
    {
        return this.attributes.size() == this2.attributes.size();
    }

    public ViewConfigurationAttributes ()
    {
        this.attributes = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }

    public void addAttribute ( ViewConfigurationAttribute _attribute )
    {
        String completeName = _attribute.getCompleteName();

        attributes.put( completeName , _attribute );
    }

    public ViewConfigurationAttribute getAttribute ( String completeName )
    {
        return ( ViewConfigurationAttribute ) attributes.get( completeName );
    }

    /**
     * @return Returns the attributes.
     */
    public TreeMap getAttributes ()
    {
        return attributes;
    }

    /**
     * Returns the spectrum attributes
     * @param historic to know in which database you want to check if the attributes are spectrum or not.
     * @return The spectrum attributes
     */
    public TreeMap getSpectrumAttributes (boolean historic)
    {
        TreeMap spectrums = new TreeMap(Collator.getInstance(Locale.FRENCH));
        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            while ( keyIterator.hasNext() )
            {
                String nextKey = ( String ) keyIterator.next();
                ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attributes.get( nextKey );
                if ( nextValue.isSpectrum( historic ) )
                {
                    spectrums.put(nextKey,nextValue);
                }
            }
        }
        return spectrums;
    }

    /**
     * Returns the string and state scalar attributes
     * @param historic to know in which database you want to check the attributes
     * @return The string scalar attributes
     */
    public TreeMap getStringStateScalarAttributes (boolean historic)
    {
        TreeMap strings = new TreeMap(Collator.getInstance(Locale.FRENCH));
        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            while ( keyIterator.hasNext() )
            {
                String nextKey = ( String ) keyIterator.next();
                ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attributes.get( nextKey );
                if ( nextValue.isScalar( historic ) )
                {
                    if ( nextValue.getDataType( historic ) == TangoConst.Tango_DEV_STRING
                            || nextValue.getDataType( historic ) == TangoConst.Tango_DEV_STATE )
                    {
                        strings.put(nextKey,nextValue);
                    }
                }
            }
        }
        return strings;
    }

    /**
     * Returns the image attributes
     * @param historic to know in which database you want to check the attributes
     * @return The image attributes
     */
    public TreeMap getImageAttributes (boolean historic)
    {
        TreeMap images = new TreeMap(Collator.getInstance(Locale.FRENCH));
        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            while ( keyIterator.hasNext() )
            {
                String nextKey = ( String ) keyIterator.next();
                ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attributes.get( nextKey );
                if ( nextValue.isImage( historic ) )
                {
                    images.put(nextKey,nextValue);
                }
            }
        }
        return images;
    }

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes ( TreeMap attributes )
    {
        this.attributes = attributes;
    }

    public void push ()
    {
        TreeMap attributesHash = this.getAttributes();
        if ( attributesHash == null )
        {
            return;
        }

        Set keySet = attributesHash.keySet();
        Iterator keyIterator = keySet.iterator();
        Vector _domains = new Vector();
        while ( keyIterator.hasNext() )
        {
            String completeName = ( String ) keyIterator.next();
            ViewConfigurationAttribute next = ( ViewConfigurationAttribute ) attributesHash.get( completeName );
            String domain_s = next.getDomainName();
            String family_s = next.getFamilyName();
            String member_s = next.getMemberName();
            String attr_s = next.getName();
            /*String cmpltNam = next.getCompleteName();
            System.out.println ( "ArchivingConfigurationAttribute/push/cmpltNam/"+cmpltNam+"/" );
            System.out.println ( "ArchivingConfigurationAttribute/push/domain_s/"+domain_s+"/family_s/"+family_s+"/member_s/"+member_s+"/attr_s/"+attr_s+"/" );*/

            ViewConfigurationAttribute attr = new ViewConfigurationAttribute();
            attr.setName( attr_s );
            attr.setCompleteName( completeName );
            attr.setDevice( member_s );
            attr.setDomain( domain_s );
            attr.setFamily( family_s );

            Domain domain = new Domain( domain_s );
            Family family = new Family( family_s );
            Member member = new Member( member_s );

            Domain dom = Domain.hasDomain( _domains , domain_s );
            if ( dom != null )
            {
                Family fam = dom.getFamily( family_s );
                if ( fam != null )
                {
                    Member mem = fam.getMember( member_s );
                    if ( mem != null )
                    {
                        mem.addAttribute( attr );
                        fam.addMember( mem );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                    else
                    {
                        member.addAttribute( attr );
                        fam.addMember( member );
                        dom.addFamily( fam );
                        //_domains.add ( dom );
                        _domains = Domain.addDomain( _domains , dom );
                    }
                }
                else
                {
                    member.addAttribute( attr );
                    family.addMember( member );
                    dom.addFamily( family );
                    //_domains.add ( dom );
                    _domains = Domain.addDomain( _domains , dom );
                }
            }
            else
            {
                member.addAttribute( attr );
                family.addMember( member );
                domain.addFamily( family );
                //_domains.add ( domain );
                _domains = Domain.addDomain( _domains , domain );
            }
        }
        VCAttributesTreeModel model = VCAttributesTreeModel.getInstance();
        model.removeAll();

        model.build( _domains );
        model.reload();


        //----------
        /*VCAttributesRecapTree recapTree = VCAttributesRecapTree.getInstance();
        if ( recapTree != null )
        {
            recapTree.expandAll( true );
        }
        VCAttributesPropertiesTree propTree = VCAttributesPropertiesTree.getInstance();
        if ( propTree != null )
        {
            propTree.expandAll( true );
        }*/
        //----------
    }


    /**
     * @param chart
     * @param historic
     * @param samplingType 
     * @param string2
     * @param
     * @return 29 août 2005
     * @throws ParseException
     * @throws DevFailed
     */
    public StaticChartMathExpression setChartAttributes ( StaticChartMathExpression chart , String startDate , String endDate , boolean historic, SamplingType samplingType ) throws DevFailed , ParseException
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        if (extractingManager.isCanceled()) return chart;
        if ( attributes != null )
        {
            Set keySet = attributes.keySet();
            Iterator keyIterator = keySet.iterator();
            while ( keyIterator.hasNext() )
            {
                if (extractingManager.isCanceled()) return chart;
                String nextKey = ( String ) keyIterator.next();
                ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attributes.get( nextKey );
                if ( nextValue.isScalar( historic ) )
                {
                    switch (nextValue.getDataType(historic))
                    {
                        case TangoConst.Tango_DEV_BOOLEAN:
                        case TangoConst.Tango_DEV_CHAR:
                        case TangoConst.Tango_DEV_UCHAR:
                        case TangoConst.Tango_DEV_LONG:
                        case TangoConst.Tango_DEV_ULONG:
                        case TangoConst.Tango_DEV_SHORT:
                        case TangoConst.Tango_DEV_USHORT:
                        case TangoConst.Tango_DEV_FLOAT:
                        case TangoConst.Tango_DEV_DOUBLE:
                            nextValue.addToChart( chart , startDate , endDate , historic , samplingType );
                            break;
                    }
                }
                chart.repaint();
            }
            TreeMap expressions = ViewConfiguration.getSelectedViewConfiguration().getExpressions();
            keySet = expressions.keySet();
            keyIterator = keySet.iterator();
            while ( keyIterator.hasNext() )
            {
                String key = (String) keyIterator.next();
                ExpressionAttribute attr = (ExpressionAttribute)expressions.get(key);
                if (attr.getVariables() != null)
                {
                    int axis = -1;
                    switch(attr.getProperties().getAxisChoice())
                    {
                        case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_Y1:
                            axis = StaticChartMathExpression.Y1_AXIS;
                            break;
                        case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_Y2:
                            axis = StaticChartMathExpression.Y2_AXIS;
                           break;
                        case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_X:
                            axis = StaticChartMathExpression.X_AXIS;
                            break;
                    }
                    chart.applyExpressionToChart( attr.getExpression(), attr.prepareView(), axis, attr.getVariables(), attr.isX() );
                }
            }
        }
        return chart;
    }


    /**
     * @param attrs 1 sept. 2005
     */
    public void removeAttributesNotInList ( TreeMap attrs )
    {
        Set keySet = this.attributes.keySet();
        Set toRemoveSet = new HashSet();
        Iterator keyIterator = keySet.iterator();
        while ( keyIterator.hasNext() )
        {
            String next = ( String ) keyIterator.next();
            if ( !attrs.containsKey( next ) )
            {
                toRemoveSet.add(next);
            }
        }
        Iterator toRemoveIterator = toRemoveSet.iterator();
        while (toRemoveIterator.hasNext())
        {
            this.attributes.remove( toRemoveIterator.next() );
        }
    }

    /**
     * @param attrs 1 sept. 2005
     */
    public void addAttributes ( TreeMap attrs )
    {
        Set keySet = attrs.keySet();
        Iterator keyIterator = keySet.iterator();
        while ( keyIterator.hasNext() )
        {
            String nextKey = ( String ) keyIterator.next();
            ViewConfigurationAttribute nextValue = ( ViewConfigurationAttribute ) attrs.get( nextKey );
            //System.out.println ( "removeAttributesNotInList/next|"+next+"|" );

            boolean needsColor = false;
            if ( !this.attributes.containsKey( nextKey ) )
            {
                //System.out.println ( "addAttributes/adding|"+nextKey+"|" );
                this.attributes.put( nextKey , nextValue );

                needsColor = true;
                ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
                nextValue.setProperties( currentProperties );

            }
            else
            {
                ViewConfigurationAttributePlotProperties plotProperties = nextValue.getProperties().getPlotProperties();
                if ( plotProperties.getCurve() == null )
                {
                    needsColor = true;
                }

            }
            if ( needsColor )
            {
                AttributesPlotPropertiesPanel panel = new AttributesPlotPropertiesPanel(false);
                panel.reset();
                panel.setColorIndex(colorIndex);
                colorIndex = panel.rotateCurveColor();
                int viewType = panel.getViewType();
                int axisChoice = panel.getAxisChoice();
                Bar bar = panel.getBar();
                Curve curve = panel.getCurve();
                Marker marker = panel.getMarker();
                Polynomial2OrderTransform transform = panel.getTransform();
                boolean hidden = panel.isHidden();
                ViewConfigurationAttributeProperties currentProperties = nextValue.getProperties();
                ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties( viewType , axisChoice , bar , curve , marker , transform , hidden );
                currentProperties.setPlotProperties( currentPlotProperties );
                nextValue.setProperties( currentProperties );
                panel = null;
                bar = null;
                curve = null;
                marker = null;
                transform = null;
                currentProperties = null;
                currentPlotProperties = null;
            }
        }
    }

    /**
     * @return
     */
    public ViewConfigurationAttributes cloneAttrs() 
    {
        ViewConfigurationAttributes ret = new ViewConfigurationAttributes ();

        ret.setAttributes ( (TreeMap) this.getAttributes().clone() );
        ret.setColorIndex(colorIndex);

        return ret;
    }

    public int getColorIndex ()
    {
        return colorIndex;
    }

    public void setColorIndex (int colorIndex)
    {
        this.colorIndex = colorIndex;
    }
}