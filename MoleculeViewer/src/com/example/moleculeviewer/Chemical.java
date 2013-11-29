package com.example.moleculeviewer;

public class Chemical {
	
    public final Structure structure;
	public final String formula;
    public final String molecular_weight;
	public final String bond_string;
	public final String name;

    public Chemical(Structure structure, String formula, String molecular_weight, String bond_string, String name) {
        this.structure = structure;
        this.formula = formula;
        this.molecular_weight = molecular_weight;
		this.bond_string = bond_string;
		this.name = name;
    }
}
