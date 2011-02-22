package fr.soleil.mambo.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.mambo.data.attributes.Attribute;

public class MamboAttributesComparator implements Comparator
{
    private Comparator referenceComparator;

    public MamboAttributesComparator ()
    {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare (Object o1, Object o2)
    {
        if (o1 != null && o2 != null)
        {
            if (o1 instanceof Attribute && o2 instanceof Attribute)
            {
                return referenceComparator.compare( ((Attribute)o1).getName(), ((Attribute)o2).getName() );
            }
        }
        return 0;
    }
}
