package com.example.moleculeviewer;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TwoDModelView extends View {

	int viewWidth;
	int viewHeight;
	int zeroWidth;
	int zeroHeight;
	int widthMargin;
	int heightMargin;
	int relativeFontSizeConstant = 20000;
	List<Atom> molecule;
	List<Bond> bondList;
	
	Paint myPaint= new Paint();
	
	public TwoDModelView(Context context) {
		super(context);
		setWillNotDraw(false);
		// TODO Auto-generated constructor stub
	}
	
	public TwoDModelView(Context context, AttributeSet attr){
		super(context,attr);
		setWillNotDraw(false);
		// TODO
	}
	
	public TwoDModelView(Context context, AttributeSet attr, int defStyle){
		super(context,attr,defStyle);
		setWillNotDraw(false);
		// TODO
	}
	
	public void initData(){
		molecule.add(new Atom(.35,.12,"H"));
		molecule.add(new Atom(.35,.88,"H"));
		molecule.add(new Atom(.7,.5,"O"));
		bondList.add(new Bond(0,2,1));
		bondList.add(new Bond(1,2,1));
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		viewWidth = xNew;
		viewHeight = yNew;
		zeroWidth = xNew/2;
		zeroHeight = yNew/2;
	}
	
	private int determineMaxTextSize(String str, float maxWidth)
	{
	    int size = 0;       
	    Paint paint = new Paint();

	    do {
	        paint.setTextSize(++ size);
	    } while(paint.measureText(str) < maxWidth);

	    return size;
	}
	/*
	boolean getLineIntersection(float p0_x, float p0_y, float p1_x, float p1_y, float p2_x, float p2_y, float p3_x, float p3_y, float *i_x, float *i_y){
		float s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;
		
		float s, t;
		s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
		t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
		{
			// Collision detected
			if (i_x != NULL)
				*i_x = p0_x + (t * s1_x);
			if (i_y != NULL)
				*i_y = p0_y + (t * s1_y);
			return true;
		}

		return false; // No collision
	}*/ //Collision detection logic, also returns the point. The trick is to manipulate this into valid Java code
	
	/*
	boolean checkFourIntersections(float p0_x, float p0_y, float p1_x, float p1_y){ //Check the line being drawn with 4 small lines drawn around the text in a rectangle. If there is a collision use that new point as a coordinate
	//To drawn the original line.
		if(getLineInterestion(p0_x, p0_y, p1_x, p1_y...))
		else if(getLineInterestion(p0_x, p0_y, p1_x, p1_y...))
	}
	*/
	
	@Override
	public void onDraw(Canvas canvas){
		myPaint.setColor(Color.GREY);
		canvas.drawPaint(myPaint);
		myPaint.setColor(Color.WHITE);
		for(Atom atom : molecule){
			atom.x=atom.x*viewWidth;
			atom.y=atom.y*viewHeight;
			myPaint.setTextSize(determineMaxTextSize(atom.name,(viewWidth*viewHeight)/relativeFontSizeConstant));
			canvas.drawText(atom.name, (float) atom.x, (float) atom.y, myPaint);
		}
		for(Bond bond : bondList){
			int singleXoff1=0;
			int singleXoff2=0;
			int singleYoff1=0;
			int singleYoff2=0;
			int doubleXoff1=3;
			int doubleXoff2=3;
			int doubleXoff3=3;
			int doubleXoff4=3;
			int doubleYoff1=3;
			int doubleYoff2=3;
			int doubleYoff4=3;
			int doubleYoff3=3;
			switch(bond.type){
			case 1:
				//checkFourIntersection((float) molecule.get(bond.first).x, (float) molecule.get(bond.first).y, (float) molecule.get(bond.second).x, (float) molecule.get(bond.second).y);
				break;
			case 2:

				break;
			}
			//Calculate offsets here
			
			
			/*float d = getResources().getDisplayMetrics().density;
			canvas.drawLine(x1*d, y1*d, x2*d, y2*d, paint);
			Need to test this effect, should center line on text?
			*/
			switch(bond.type){
			case 1: //single
				canvas.drawLine( (float) molecule.get(bond.first).x - singleXoff1, (float) molecule.get(bond.first).y - singleYoff1, (float) molecule.get(bond.second).x - singleXoff2, (float) molecule.get(bond.second).y - singleYoff2, myPaint);
				break;
			case 2: //double
				canvas.drawLine( (float) molecule.get(bond.first).x - doubleXoff1, (float) molecule.get(bond.first).y - doubleYoff1, (float) molecule.get(bond.second).x - doubleXoff2, (float) molecule.get(bond.second).y - doubleYoff2, myPaint);
				canvas.drawLine( (float) molecule.get(bond.first).x - doubleXoff3, (float) molecule.get(bond.first).y - doubleYoff3, (float) molecule.get(bond.second).x - doubleXoff4, (float) molecule.get(bond.second).y - doubleYoff4, myPaint);
				break;
			default: //Unknown, single drawn
				canvas.drawLine( (float) molecule.get(bond.first).x - singleXoff1, (float) molecule.get(bond.first).y - singleYoff1, (float) molecule.get(bond.second).x - singleXoff2, (float) molecule.get(bond.second).y - singleYoff2, myPaint);
				break;
			}
		}
	}
	
	class Bond{
		int first, second, type;
		
		Bond(int first_num, int second_num, int bond_type){
			first=first_num;
			second=second_num;
			type=bond_type;
		}
		
	}
	
	class Atom{
		double x,y;
		String name;
		
		Atom(double xCoor, double yCoor, String molName){
			x=xCoor;
			y=yCoor;
			name=molName;
		}
	}
	
	
}
