package fr.soleil.mambo.data.attributes;

import java.util.HashMap;
import fr.esrf.tangoatk.core.Property;
import fr.esrf.tangoatk.core.attribute.NumberImage;
import fr.esrf.tangoatk.widget.attribute.NumberImageViewer;

public class FakeNumberImage extends NumberImage
{
    protected double[][] value;
    protected String name;
    protected String alias;
    protected String label;
    protected NumberImageViewer viewer;
    protected String format = "";

    public FakeNumberImage ()
    {
        super();
        value = new double[0][0];
        name = null;
        alias = null;
        label = null;
        propertyMap = new HashMap();
        viewer = new NumberImageViewer();
        viewer.setModel(this);
    }

    public void setValue (double[][] value)
    {
        if (value == null)
        {
            value = new double[0][0];
        }
        checkDimensions(value);
        viewer.setData(value);
    }

    public boolean isWritable ()
    {
        return false;
    }

    public String getNameSansDevice ()
    {
        if (name != null)
        {
            int last = name.lastIndexOf("/");
            if (last > 0)
            {
                return  name.substring(last+1, name.length());
            }
        }
        return name;
    }

    public void refresh()
    {
        setValue(value);
    }

    protected void checkDimensions(double o [][]) {
    }

    public Property getProperty(String s)
    {
      Property p = null;
      if (propertyMap != null)
      {
        p = (Property) propertyMap.get(s);
      }
      return p;
    }

    public String getFormat()
    {
      return format;
    }

    public void setFormat(String _format) {
      if (_format == null)
      {
          this.format = "";
      }
      else this.format = _format;
    }

    public NumberImageViewer getViewer()
    {
        return viewer;
    }
}
