package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class ConvexPolygon extends Polygon implements Cloneable {
		
		static final int maxNumPoints=3;
		static Random gen = new Random();
		public static int max_X;
		public static int max_Y;
		NumberFormat nf = new DecimalFormat("##.00");
		
		
		// randomly generates a polygon
		public ConvexPolygon(int numPoints){
			super();
			genRandomConvexPolygon(numPoints);
			int r = gen.nextInt(256);
			int g = gen.nextInt(256);
			int b = gen.nextInt(256); 
			this.setFill(Color.rgb(r, g, b));
			this.setOpacity(gen.nextDouble());
		}
		
		public ConvexPolygon(){
			super();
		}

		@Override
		public ConvexPolygon clone() {
			ConvexPolygon another = new ConvexPolygon();
			Iterator<Double> it = this.getPoints().iterator();
			while (it.hasNext()) {
				another.addPoint(it.next(), it.next());
			}
			another.setFill(this.getFill());
			another.setOpacity(this.getOpacity());
			return another;
		}
		
		public String toString(){
			String res = super.toString();
			res += " " + this.getFill() + " opacity " + this.getOpacity();
			return res;
		}
			
		public void addPoint(double x, double y){
			getPoints().add(x);
			getPoints().add(y);
		}

		public void genRandomConvexPolygon(int n) {
			List<Double> angles = new ArrayList<>();
			List<Double> scalars = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				angles.add(gen.nextDouble() * 2*Math.PI);
				scalars.add(gen.nextDouble() * Math.min(max_Y, max_X));
			}
			Collections.sort(angles);
			Collections.sort(scalars);

			Point center = new Point(gen.nextInt(max_X), gen.nextInt(max_Y));
			double scalar, x, y;
			Iterator<Double> itscalar = scalars.iterator();
			for (double angle : angles) {
				scalar = itscalar.next();
				x = center.x + scalar * Math.cos(angle);
				y = center.y + scalar * Math.sin(angle);

				x = (x < 0) ? 0 : x;
				x = (x >= max_X) ? max_X-1 : x;
				y = (y < 0) ? 0 : y;
				y = (y >= max_Y) ? max_Y-1 : y;
				addPoint(x, y);
			}
		}
		
		// http://cglab.ca/~sander/misc/ConvexGeneration/convex.html
		public void genRandomConvexPolygon2(int n){
			List<Point> points = new LinkedList<Point>();
			List<Integer> abs = new ArrayList<>();
			List<Integer> ord = new ArrayList<>();
			
			for (int i=0;i<n;i++){
				abs.add(gen.nextInt(max_X));
				ord.add(gen.nextInt(max_Y));
			}
			Collections.sort(abs);
			Collections.sort(ord);
			//System.out.println(abs + "\n" + ord);
			int minX = abs.get(0);
			int maxX = abs.get(n-1);
			int minY = ord.get(0);
			int maxY = ord.get(n-1);
			
			List<Integer> xVec = new ArrayList<>();
			List<Integer> yVec = new ArrayList<>();
			
			int top= minX, bot = minX;
			for (int i=1;i<n-1;i++){
				int x = abs.get(i);
				
				if (gen.nextBoolean()){
					xVec.add(x-top);
					top = x;
				} else{
					xVec.add(bot-x);
					bot = x;
				}
			}
			xVec.add(maxX-top);
			xVec.add(bot-maxX);
			
			int left= minY, right = minY;
			for (int i=1;i<n-1;i++){
				int y = ord.get(i);
				
				if (gen.nextBoolean()){
					yVec.add(y-left);
					left = y;
				} else{
					yVec.add(right-y);
					right = y;
				}
			}
			yVec.add(maxY-left);
			yVec.add(right-maxY);
			
			Collections.shuffle(yVec);
			
			List<Point> lpAux = new ArrayList<>();
			for (int i=0;i<n;i++)
				lpAux.add(new Point(xVec.get(i), yVec.get(i)));
		
			
			// sort in order by angle
			Collections.sort(lpAux, (x,y) -> Double.compare(Math.atan2(x.getY(), x.getX()), Math.atan2(y.getY(), y.getX())));
				
			int x=0,y=0;
			int minPolX=0, minPolY=0;
			
			for (int i=0;i<n;i++){
				points.add(new Point(x,y));
				x += lpAux.get(i).getX();
				y += lpAux.get(i).getY(); 
				
				if (x < minPolX)
					minPolX=x;
				if (y<minPolY)
					minPolY=y;
			}
				
			int xshift = gen.nextInt(max_X - (maxX-minX)) ;
			int yshift = gen.nextInt(max_Y - (maxY-minY)) ;
			xshift -= minPolX;
			yshift -= minPolY;
			for (int i=0;i<n;i++){
				Point p = points.get(i);
				p.translate(xshift,yshift);
			}
			for (Point p : points)
				addPoint(p.getX(), p.getY());
			
		}
		
		
	
		
		
		public class Point {

			int x,y;

			// generate a random point
			public Point(){
				x= gen.nextInt(max_X);
				y= gen.nextInt(max_Y);
			}
			
			public Point(int x, int y){
				this.x=x;
				this.y=y;
			}
			
			public int getX(){return x;}
			public int getY(){return y;}
			public void translate(int vx,int vy){
				x += vx;
				y += vy;
			}
			
			public boolean equals(Object o){
				if (o==null)
					return false;
				else if (o == this)
					return true;
				else if (o instanceof Point)
					return ((Point) o).x== this.x && ((Point) o).y== this.y;
				else
					return false;
			}
			
			public String toString(){
				NumberFormat nf = new DecimalFormat("#.00");
				return "(" + x + "," + y+")"; // + nf.format(Math.atan2(y, x))+")";
			}
			
		}
		
		
	
}
