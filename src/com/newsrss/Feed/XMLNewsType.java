package com.newsrss.Feed;

/**
 * Created with IntelliJ IDEA.
 * User: MediumMG
 * Date: 08.04.13
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public enum XMLNewsType {
    AuditNAccounting (0),
    Business (1),
    Governance (2),
    Insolvency (3),
    Practice (4),
    Tax (5);

    private final int index;

    XMLNewsType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static XMLNewsType fromInt(int value) {
        switch (value) {
            case 0: return XMLNewsType.AuditNAccounting;
            case 1: return XMLNewsType.Business;
            case 2: return XMLNewsType.Governance;
            case 3: return XMLNewsType.Insolvency;
            case 4: return XMLNewsType.Practice;
            case 5: return XMLNewsType.Tax;
            default: return null;
        }
    }
}
