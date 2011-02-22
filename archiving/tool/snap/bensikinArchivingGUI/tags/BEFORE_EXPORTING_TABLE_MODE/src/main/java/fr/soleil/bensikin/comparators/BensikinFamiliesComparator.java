package fr.soleil.bensikin.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.bensikin.data.attributes.Family;

public class BensikinFamiliesComparator implements Comparator
{
    private Comparator referenceComparator;

    public BensikinFamiliesComparator ()
    {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare (Object o1, Object o2)
    {
        if (o1 != null && o2 != null)
        {
            if (o1 instanceof Family && o2 instanceof Family)
            {
                return referenceComparator.compare( ((Family)o1).getName(), ((Family)o2).getName() );
            }
        }
        return 0;
    }
}
