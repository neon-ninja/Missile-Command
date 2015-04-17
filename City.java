/*
	This class represents a city. It stores information about the position and status of the
	city. It also draws the city.
	Author: Nicholas Young
	UPI: nyou045
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class City {

	private Point p;
	private boolean alive;
	private int radius;
	
	public City (int x,int y) {
		p = new Point(x,y);
		alive = true;
		radius = 25;
	}
	
	public void kill() {
		alive = false;
	}
	
	public boolean getStatus() {
		return alive;
	}
	
	public Point getPos() {
		return p;
	}
	
	public void draw(Graphics g) {
		if (alive) {
			g.setColor(Color.BLUE);
			g.fillOval(p.x-radius,p.y-radius,radius*2,radius*2);
		}
	}
	
}