package com.example.moleculeviewer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bond implements Serializable{
	public int atom1;
	public int atom2;
	int bond_number;
	
	public Bond(int atom1, int atom2, int bond_number){
		this.atom1 = atom1;
		this.atom2 = atom2;
		this.bond_number = bond_number;
	}
}