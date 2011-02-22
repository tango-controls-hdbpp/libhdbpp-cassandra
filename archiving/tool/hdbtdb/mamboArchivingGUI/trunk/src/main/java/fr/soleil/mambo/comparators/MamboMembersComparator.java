package fr.soleil.mambo.comparators;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import fr.soleil.mambo.data.attributes.Member;

public class MamboMembersComparator implements Comparator
{
    private Comparator referenceComparator;

    public MamboMembersComparator ()
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
