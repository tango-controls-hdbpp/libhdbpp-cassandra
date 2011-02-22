package fr.soleil.bensikin.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class BensikinToStringObjectComparator implements Comparator
{
    private Comparator referenceComparator;

    public BensikinToStringObjectComparator ()
    {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare (Object o1, Object o2)
    {
        if ( o1 != null && o2 != null )
        {
            return referenceComparator.compare(o1.toString(), o2.toString());
        }
        return 0;
    }
}
