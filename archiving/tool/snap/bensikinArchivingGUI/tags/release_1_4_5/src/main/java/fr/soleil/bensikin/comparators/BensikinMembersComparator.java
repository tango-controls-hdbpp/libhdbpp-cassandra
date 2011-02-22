package fr.soleil.bensikin.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.bensikin.data.attributes.Member;

public class BensikinMembersComparator implements Comparator
{
    private Comparator referenceComparator;

    public BensikinMembersComparator ()
    {
        referenceComparator = Collator.getInstance(Locale.FRENCH);
    }

    public int compare (Object o1, Object o2)
    {
        if (o1 != null && o2 != null)
        {
            if (o1 instanceof Member && o2 instanceof Member)
            {
                return referenceComparator.compare( ((Member)o1).getName(), ((Member)o2).getName() );
            }
        }
        return 0;
    }
}
