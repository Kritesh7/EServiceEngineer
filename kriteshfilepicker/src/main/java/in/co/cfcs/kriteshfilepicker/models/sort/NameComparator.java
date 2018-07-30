package in.co.cfcs.kriteshfilepicker.models.sort;

import java.util.Comparator;

import in.co.cfcs.kriteshfilepicker.models.Document;


public class NameComparator implements Comparator<Document> {

    protected NameComparator() { }

    @Override
    public int compare(Document o1, Document o2) {
        return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
    }
}
