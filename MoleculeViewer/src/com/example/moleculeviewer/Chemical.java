package com.example.moleculeviewer;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Chemical implements Serializable {
	
    public Structure structure;
	public String formula;
    public String molecular_weight;
	public String bond_string;
	public String name;

    public Chemical(Structure structure, String formula, String molecular_weight, String bond_string, String name) {
        this.structure = structure;
        this.formula = formula;
        this.molecular_weight = molecular_weight;
		this.bond_string = bond_string;
		this.name = name;
    }
    
    public static Comparator<Chemical> NameAscending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return c1.name.compareTo(c2.name);
    	}
    };
   
    public static Comparator<Chemical> NameDescending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return -c1.name.compareTo(c2.name);
    	}
    };
    
    public static Comparator<Chemical> MolecularWeightAscending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return c1.molecular_weight.compareTo(c2.molecular_weight);
    	}
    };
    
    public static Comparator<Chemical> MolecularWeightDescending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return -c1.molecular_weight.compareTo(c2.molecular_weight);
    	}
    };
    
    public static Comparator<Chemical> FormulaAscending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return c1.formula.compareTo(c2.formula);
    	}
    };
    
    public static Comparator<Chemical> FormulaDescending = new Comparator<Chemical>() {
    	public int compare(Chemical c1, Chemical c2){
    		return -c1.formula.compareTo(c2.formula);
    	}
    };
}
