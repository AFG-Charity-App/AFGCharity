package com.example.afgcharity;

import java.util.Comparator;

public class CustomComparator implements Comparator<Apparel> {

    @Override
    public int compare(Apparel o1, Apparel o2) {
        return o1.getClothing().compareTo(o2.getClothing());
    }
}
