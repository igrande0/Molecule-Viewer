package com.example.moleculeviewer;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Structure implements Serializable{
	
	public List<Atom> atom_list;
	public List<Bond> bond_list;
	
	public Structure(List<Atom> atom_list, List<Bond> bond_list){
		this.atom_list = atom_list;
		this.bond_list = bond_list;
	}
}