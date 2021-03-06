package com.example.moleculeviewer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Atom implements Serializable{
	public float x;
	public float y;
	public float z;
	public String symbol;
	
	public Atom(float x, float y, float z, String symbol){
		this.x = x;
		this.y = y;
		this.z = z;
		this.symbol = symbol;
	}
}