package com.example.moleculeviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class DataXmlParser {
	private static final String ns = null;
	
	public ArrayList<Chemical> parse(InputStream in) throws XmlPullParserException, IOException {
		try{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
		}
	}
	
	private ArrayList<Chemical> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
	    ArrayList<Chemical> chemicals = new ArrayList<Chemical>();

	    parser.require(XmlPullParser.START_TAG, ns, "data");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // search for chemical tags
	        if (name.equals("chemical")) {
	            chemicals.add(readChemical(parser));
	        } else {
	            skip(parser);
	        }
	    }  
	    return chemicals;
	}
	
	private Chemical readChemical(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "chemical");
	    String atom_block = null;
	    String formula = null;
	    String molecular_weight = null;
		String bond_string = null;
		String molecule_name = parser.getAttributeValue(null, "name");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("atom_block")) {
	            atom_block = readData(parser);
	        } else if (name.equals("formula")) {
	            formula = readData(parser);
	        } else if (name.equals("molecular_weight")) {
	            molecular_weight = readData(parser);
	        } else if (name.equals("bond_string")) {
				bond_string = readData(parser);
			}else {
	            skip(parser);
	        }
	    }
		String[] data_array = atom_block.split("\\s+");
		//System.out.println(Arrays.toString(data_array));
		int atom_count = Integer.parseInt(data_array[0]);
		int bond_count = Integer.parseInt(data_array[1]);
		ArrayList<Atom> atom_list = new ArrayList<Atom>();
		int i = 0;
		float maxX = 1;
		float maxY = 1;
		float maxZ = 1;
		float minX = 9999;
		float minY = 9999;
		float minZ = 9999;
		for(i=10; i<(10+atom_count*15); i += 15){
			float x = Float.parseFloat(data_array[i]);
			float y = Float.parseFloat(data_array[i+1]);
			float z = Float.parseFloat(data_array[i+2]);
			if(x+1 > maxX)
				maxX = x+1;
			if(x-1 < minX)
				minX= x-1;
			if(y+1 > maxY)
				maxY = y+1;
			if(y-1 < minY)
				minY= y-1;
			if(z+1 > maxZ)
				maxZ = z+1;
			if(z-1 < minZ)
				minZ= z-1;
			String symbol = data_array[i+3];
			atom_list.add(new Atom(x, y, z, symbol));
		}
		//divide all values by max
		for(int j = 0; j<atom_list.size(); j++){
			float temp_x = (atom_list.get(j).x-minX)/(maxX-minX);
			float temp_y = (atom_list.get(j).y-minY)/(maxY-minY);
			float temp_z = (atom_list.get(j).z-minZ)/(maxZ-minZ);
			String temp_symbol = atom_list.get(j).symbol;
			atom_list.set(j, new Atom(temp_x, temp_y, temp_z, temp_symbol));
		}
		ArrayList<Bond> bond_list = new ArrayList<Bond>();
		while(i<(10+atom_count*15+bond_count*4)){
			int atom1 = Integer.parseInt(data_array[i]);
			int atom2 = Integer.parseInt(data_array[i+1]);
			int bonds = Integer.parseInt(data_array[i+2]);
			Bond bond = new Bond(atom1, atom2, bonds);
			bond_list.add(bond);
			i += 4;
		}
		Structure structure = new Structure(atom_list, bond_list);
		
	    return new Chemical(structure, formula, molecular_weight, bond_string, molecule_name);
	}


	private String readData(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, null);
	    String data = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, null);
	    return data;
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText().trim();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
