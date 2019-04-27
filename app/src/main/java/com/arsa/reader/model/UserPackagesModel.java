package com.arsa.reader.model;

import org.readium.r2.shared.opds.Price;

import java.util.List;

public class UserPackagesModel extends  VW_PackageBooks {
    public int ID ;
    public int CategoryID;
    public String PackageTitle ;
    public int Price  ;
    public byte StatusID ;
    public int Score;
    public List<VW_PackageBooks> Books;

}
