package in.co.cfcs.kriteshfilepicker.models.sort;

import java.util.Comparator;

import in.co.cfcs.kriteshfilepicker.models.Document;


public enum SortingTypes {
    name(new NameComparator()), none(null);

    final private Comparator<Document> comparator;

    SortingTypes(Comparator<Document> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Document> getComparator() {
        return comparator;
    }
}
