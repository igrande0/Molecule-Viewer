package com.example.moleculeviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	int idealPixelOffset = 5;
	List<com.example.moleculeviewer.Atom> molecule = new ArrayList<Atom>();
	List<com.example.moleculeviewer.Bond> bondList = new ArrayList<Bond>();
	
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
	
	/*public void initData(){
		molecule.add(new Atom(.35,.5,"H"));
		molecule.add(new Atom(.65,.5,"H"));
		molecule.add(new Atom(.5,.65,"H"));
		molecule.add(new Atom(.5,.35,"H"));
		molecule.add(new Atom(.5,.5,"C"));
		bondList.add(new Bond(0,4,1));
		bondList.add(new Bond(1,4,1));
		bondList.add(new Bond(2,4,1));
		bondList.add(new Bond(3,4,1));
		invalidate();
	}*/
	
	public void drawMolecule(Chemical drawMolecule){
		molecule=drawMolecule.structure.atom_list;
		bondList=drawMolecule.structure.bond_list;
		invalidate();
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
		viewWidth = xNew;
		viewHeight = yNew;
		zeroWidth = xNew/2;
		zeroHeight = yNew/2;
		super.onSizeChanged(xNew, yNew, xOld, yOld);
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
	
	private double calculateAbsSlope(double x1, double y1, double x2, double y2){ //return -1 for vertical line
		double rise;
		double run;
		rise=y1-y2;
		run=x1-x2;
		if(run==0){
			return -1;
		}
		else if(rise/run < 0){
			return -rise/run;
		}
		else return rise/run;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		myPaint.setColor(Color.GRAY);
		canvas.drawPaint(myPaint);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setColor(Color.WHITE);
		for(Atom atom : molecule){
			myPaint.setTextSize(determineMaxTextSize(atom.symbol,(viewWidth*viewHeight)/relativeFontSizeConstant));
			System.out.println(atom.symbol + " " + atom.x + " " + atom.y);
			if(atom.x < 1) atom.x=(atom.x*viewWidth + viewWidth)/2 ;
			if(atom.y < 1) atom.y=(atom.y*viewHeight + viewHeight)/2;
			canvas.drawText(atom.symbol, (float) (atom.x), (float) (atom.y), myPaint);
		}
		for(Bond bond : bondList){
			float singleXoff1=0;
			float singleXoff2=0;
			float singleYoff1=0;
			float singleYoff2=0;
			float doubleXoff1=0;
			float doubleXoff2=0;
			float doubleXoff3=0;
			float doubleXoff4=0;
			float doubleYoff1=0;
			float doubleYoff2=0;
			float doubleYoff4=0;
			float doubleYoff3=0;
			boolean specialCase=false;
			System.out.println(bond.atom1 + " " + bond.atom2 + " " + bond.bond_number);
			Atom firstBondAtom=molecule.get(bond.atom1-1);
			Atom secondBondAtom=molecule.get(bond.atom2-1);
			//Calculate offsets here
			
			
			/*float d = getResources().getDisplayMetrics().density;
			canvas.drawLine(x1*d, y1*d, x2*d, y2*d, paint);
			Need to test this effect, should center line on text?
			*/
			//Offset Logic
			double slope = calculateAbsSlope(firstBondAtom.x,firstBondAtom.y,secondBondAtom.x,secondBondAtom.y);
			System.out.println(slope);
			
			if( firstBondAtom.x - secondBondAtom.x < 0 && firstBondAtom.y - secondBondAtom.y < 0){
				//singleXoff1=pos; singleXoff2=neg; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=myPaint.measureText(firstBondAtom.symbol); singleXoff2=-idealPixelOffset; singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
				doubleXoff1=myPaint.measureText(firstBondAtom.symbol); doubleXoff3=myPaint.measureText(firstBondAtom.symbol);
				doubleXoff2=-idealPixelOffset; doubleXoff4=-idealPixelOffset;
				doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
				doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x-myPaint.measureText(firstBondAtom.name), (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x+idealPixelOffset, (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x > 0 && firstBondAtom.y - secondBondAtom.y < 0){
				//singleXoff1=neg; singleXoff2=pos; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=-idealPixelOffset; singleXoff2=myPaint.measureText(secondBondAtom.symbol); singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
				doubleXoff1=-idealPixelOffset; doubleXoff3=-idealPixelOffset;
				doubleXoff2=myPaint.measureText(secondBondAtom.symbol); doubleXoff4=myPaint.measureText(secondBondAtom.symbol);
				doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
				doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x+idealPixelOffset, (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x-myPaint.measureText(secondBondAtom.name), (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x < 0 && firstBondAtom.y - secondBondAtom.y > 0){
				//singleXoff1=pos; singleXoff2=neg; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=myPaint.measureText(firstBondAtom.symbol); singleXoff2=-idealPixelOffset; singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
				doubleXoff1=myPaint.measureText(firstBondAtom.symbol); doubleXoff3=myPaint.measureText(firstBondAtom.symbol);
				doubleXoff2=-idealPixelOffset; doubleXoff4=-idealPixelOffset;
				doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
				doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x-myPaint.measureText(firstBondAtom.name), (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x+idealPixelOffset, (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x > 0 && firstBondAtom.y - secondBondAtom.y > 0){
				//singleXoff1=neg; singleXoff2=pos; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=-idealPixelOffset; singleXoff2=myPaint.measureText(secondBondAtom.symbol); singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
				doubleXoff1=-idealPixelOffset; doubleXoff3=-idealPixelOffset;
				doubleXoff2=myPaint.measureText(secondBondAtom.symbol); doubleXoff4=myPaint.measureText(secondBondAtom.symbol);
				doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
				doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x+idealPixelOffset, (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x-myPaint.measureText(secondBondAtom.name), (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if(firstBondAtom.x - secondBondAtom.x == 0){
				if(firstBondAtom.y < secondBondAtom.y){
					singleXoff1=myPaint.measureText(firstBondAtom.symbol)/2;
					singleXoff2=myPaint.measureText(secondBondAtom.symbol)/2;
					singleYoff1=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))+idealPixelOffset);
					doubleXoff1=myPaint.measureText(firstBondAtom.symbol)/4; doubleXoff3=3*myPaint.measureText(firstBondAtom.symbol)/4;
					doubleXoff2=myPaint.measureText(secondBondAtom.symbol)/4; doubleXoff4=3*myPaint.measureText(secondBondAtom.symbol)/4;
					doubleYoff1=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2); doubleYoff3=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))+idealPixelOffset); doubleYoff4=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))+idealPixelOffset);
				}
				else{
					singleXoff1=myPaint.measureText(firstBondAtom.symbol)/2;
					singleXoff2=myPaint.measureText(secondBondAtom.symbol)/2;
					singleYoff1=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1)));
					singleYoff2=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleXoff1=myPaint.measureText(firstBondAtom.symbol)/4; doubleXoff3=3*myPaint.measureText(firstBondAtom.symbol)/4;
					doubleXoff2=myPaint.measureText(secondBondAtom.symbol)/4; doubleXoff4=3*myPaint.measureText(secondBondAtom.symbol)/4;
					doubleYoff1=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2); doubleYoff3=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleYoff2=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))); doubleYoff4=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1)));
				}
				specialCase=true;
			}
			else if(firstBondAtom.y - secondBondAtom.y == 0){
				if(firstBondAtom.x < secondBondAtom.x){
					singleXoff1=myPaint.measureText(firstBondAtom.symbol);
					singleXoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleXoff1=myPaint.measureText(firstBondAtom.symbol); doubleXoff3=myPaint.measureText(firstBondAtom.symbol);
					doubleXoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2); doubleXoff4=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
					doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				}
				else{
					singleXoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2);
					singleXoff2=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1)));
					singleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleXoff1=-myPaint.measureText(firstBondAtom.symbol); doubleXoff3=-myPaint.measureText(firstBondAtom.symbol);
					doubleXoff2=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2); doubleXoff4=(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/2);
					doubleYoff1=-(myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4); doubleYoff3=-(3*myPaint.measureText(firstBondAtom.symbol.substring(0, 1))/4);
					doubleYoff2=-(myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4); doubleYoff4=-(3*myPaint.measureText(secondBondAtom.symbol.substring(0, 1))/4);
				}
				specialCase=true;
			}

			System.out.println("Xoff1" + singleXoff1 + " Xoff2" + singleXoff2 + " Yoff1"+singleYoff1+" Yoff2"+singleYoff2);
			
			switch(bond.bond_number){
			case 1: //single
				myPaint.setColor(Color.DKGRAY);
				canvas.drawLine( (float) firstBondAtom.x + singleXoff1, (float) firstBondAtom.y + singleYoff1, (float) secondBondAtom.x + singleXoff2, (float) secondBondAtom.y + singleYoff2, myPaint);
				myPaint.setStyle(Paint.Style.FILL);
				myPaint.setColor(Color.BLACK);
				if(!specialCase){
					canvas.drawCircle((float) firstBondAtom.x + singleXoff1, (float) firstBondAtom.y + singleYoff1, 5, myPaint);
					canvas.drawCircle((float) secondBondAtom.x + singleXoff2, (float) secondBondAtom.y + singleYoff2, 5, myPaint);
				}
				break;
			case 2: //double
				myPaint.setColor(Color.DKGRAY);
				canvas.drawLine( (float) firstBondAtom.x + doubleXoff1, (float) firstBondAtom.y + doubleYoff1, (float) secondBondAtom.x + doubleXoff2, (float) secondBondAtom.y + doubleYoff2, myPaint);
				canvas.drawLine( (float) firstBondAtom.x + doubleXoff3, (float) firstBondAtom.y + doubleYoff3, (float) secondBondAtom.x + doubleXoff4, (float) secondBondAtom.y + doubleYoff4, myPaint);
				break;
			default: //Unknown, single drawn
				myPaint.setColor(Color.DKGRAY);
				canvas.drawLine( (float) firstBondAtom.x + singleXoff1, (float) firstBondAtom.y + singleYoff1, (float) secondBondAtom.x + singleXoff2, (float) secondBondAtom.y + singleYoff2, myPaint);
				break;
			}
		}
	}
}
