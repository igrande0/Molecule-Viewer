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
	List<Atom> molecule = new ArrayList<Atom>();
	List<Bond> bondList = new ArrayList<Bond>();
	
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
			myPaint.setTextSize(determineMaxTextSize(atom.name,(viewWidth*viewHeight)/relativeFontSizeConstant));
			canvas.drawText(atom.name, (float) (atom.x*viewWidth), (float) (atom.y*viewHeight), myPaint);
		}
		for(Bond bond : bondList){
			float singleXoff1=0;
			float singleXoff2=0;
			float singleYoff1=0;
			float singleYoff2=0;
			float doubleXoff1=3;
			float doubleXoff2=3;
			float doubleXoff3=3;
			float doubleXoff4=3;
			float doubleYoff1=3;
			float doubleYoff2=3;
			float doubleYoff4=3;
			float doubleYoff3=3;
			boolean specialCase=false;
			Atom firstBondAtom=molecule.get(bond.first);
			Atom secondBondAtom=molecule.get(bond.second);
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
				singleXoff1=myPaint.measureText(firstBondAtom.name); singleXoff2=-idealPixelOffset; singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x-myPaint.measureText(firstBondAtom.name), (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x+idealPixelOffset, (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x > 0 && firstBondAtom.y - secondBondAtom.y < 0){
				//singleXoff1=neg; singleXoff2=pos; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=-idealPixelOffset; singleXoff2=myPaint.measureText(secondBondAtom.name); singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x+idealPixelOffset, (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x-myPaint.measureText(secondBondAtom.name), (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x < 0 && firstBondAtom.y - secondBondAtom.y > 0){
				//singleXoff1=pos; singleXoff2=neg; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=myPaint.measureText(firstBondAtom.name); singleXoff2=-idealPixelOffset; singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x-myPaint.measureText(firstBondAtom.name), (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x+idealPixelOffset, (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if( firstBondAtom.x - secondBondAtom.x > 0 && firstBondAtom.y - secondBondAtom.y > 0){
				//singleXoff1=neg; singleXoff2=pos; singleYoff1=neg; singleYoff2=neg;
				singleXoff1=-idealPixelOffset; singleXoff2=myPaint.measureText(secondBondAtom.name); singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2); singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				//canvas.drawText(firstBondAtom.name, (float) firstBondAtom.x+idealPixelOffset, (float) firstBondAtom.y+(myPaint.measureText(firstBondAtom.name)/2), myPaint);
				//canvas.drawText(secondBondAtom.name, (float) secondBondAtom.x-myPaint.measureText(secondBondAtom.name), (float) secondBondAtom.y+(myPaint.measureText(secondBondAtom.name)/2), myPaint);
			}
			else if(firstBondAtom.x - secondBondAtom.x == 0){
				if(firstBondAtom.y < secondBondAtom.y){
					singleXoff1=myPaint.measureText(firstBondAtom.name)/2;
					singleXoff2=myPaint.measureText(secondBondAtom.name)/2;
					singleYoff1=(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))+idealPixelOffset);
				}
				else{
					singleXoff1=myPaint.measureText(firstBondAtom.name)/2;
					singleXoff2=myPaint.measureText(secondBondAtom.name)/2;
					singleYoff1=-(myPaint.measureText(secondBondAtom.name.substring(0, 1)));
					singleYoff2=(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				}
				specialCase=true;
			}
			else if(firstBondAtom.y - secondBondAtom.y == 0){
				if(firstBondAtom.x < secondBondAtom.x){
					singleXoff1=myPaint.measureText(firstBondAtom.name.substring(0, 1));
					singleXoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
					singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				}
				else{
					singleXoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2);
					singleXoff2=(myPaint.measureText(secondBondAtom.name.substring(0, 1)));
					singleYoff1=-(myPaint.measureText(firstBondAtom.name.substring(0, 1))/2);
					singleYoff2=-(myPaint.measureText(secondBondAtom.name.substring(0, 1))/2);
				}
				specialCase=true;
			}

			System.out.println("Xoff1" + singleXoff1 + " Xoff2" + singleXoff2 + " Yoff1"+singleYoff1+" Yoff2"+singleYoff2);
			
			switch(bond.type){
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
				canvas.drawLine( (float) firstBondAtom.x + doubleXoff1, (float) firstBondAtom.y + doubleYoff1, (float) secondBondAtom.x + doubleXoff2, (float) secondBondAtom.y + doubleYoff2, myPaint);
				canvas.drawLine( (float) firstBondAtom.x + doubleXoff3, (float) firstBondAtom.y + doubleYoff3, (float) secondBondAtom.x + doubleXoff4, (float) secondBondAtom.y + doubleYoff4, myPaint);
				break;
			default: //Unknown, single drawn
				canvas.drawLine( (float) firstBondAtom.x + singleXoff1, (float) firstBondAtom.y + singleYoff1, (float) secondBondAtom.x + singleXoff2, (float) secondBondAtom.y + singleYoff2, myPaint);
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
