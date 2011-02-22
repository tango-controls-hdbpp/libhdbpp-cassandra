package fr.soleil.mambo.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.mambo.data.attributes.Domain;

public class MamboDomainsComparator implements Comparator
{
    private Comparator referenceComparator;

    public MamboDomainsComparator ()
    {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare (Object o1, Object o2)
    {
        if (o1 != null && o2 != null)
        {
            if (o1 instanceof Domain && o2 instanceof Domain)
            {
                return referenceComparator.compare( ((Domain)o1).getName(), ((Domain)o2).getName() );
            }
        }
        return 0;
    }
}
