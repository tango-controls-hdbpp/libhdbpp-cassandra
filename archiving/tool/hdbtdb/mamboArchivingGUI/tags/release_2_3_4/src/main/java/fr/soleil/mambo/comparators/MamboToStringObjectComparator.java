package fr.soleil.mambo.comparators;

import java.text.Collator;
import java.util.Comparator;

public class MamboToStringObjectComparator implements Comparator<Object> {

	private Comparator<Object> referenceComparator;

	public MamboToStringObjectComparator() {
		referenceComparator = Collator.getInstance();
	}

	public int compare(Object o1, Object o2) {
		if (o1 != null && o2 != null) {
			return referenceComparator.compare(o1.toString(), o2.toString());
		}
		return 0;
	}
}
