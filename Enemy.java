/*
	This class represents a enemy. Each enemy is passed the array of bases and cities,
	which it uses to determine its target. Enemies then fire, update and draw.
	If an enemy collides with a base or a city, that structure is destroyed.
	Author: Nicholas Young
	UPI: nyou045
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Enemy {
	
	private Point target;
	private double targetX,targetY,missileX,missileY,missileDX,missileDY;
	private boolean atkBase,firing,exploding;
	private int newTarget,ticksTillImpact,explosionR,radius,x,y;
	
	public Enemy (int width,int level,int missileSpeed, Base[] bases, City[] cities) {
		x = (int)(Math.random() * width);
		y = 0;
		ticksTillImpact = missileSpeed * 10;
		firing = false;
		exploding = false;
		radius = 25;
		while (target == null) {
			atkBase = ((int)(Math.random()*2) == 0);
			if (atkBase) {
				newTarget = (int)(Math.random()*bases.length);
				if (bases[newTarget].getStatus()) {
					target = bases[newTarget].getPos();
				}
				
			} else {
				newTarget = (int)(Math.random()*cities.length);
				if (cities[newTarget].getStatus()) {
					target = cities[newTarget].getPos();
				}
			}
		}
		targetX = target.x;
		targetY = target.y;
		fire();
					
	}
	
	public Point getPos() {
		Point p = new Point((int)missileX,(int)missileY);
		return p;
	}
	
	public boolean getStatus() {
		return firing;
	}
	
	public void kill() {
		firing = false;
	}
	
	public void fire() {
		if (!firing && !exploding) {
			missileX = x;
			missileY = y;
			
			missileDX = (targetX-x)/ticksTillImpact;
			missileDY = (targetY-y)/ticksTillImpact;
			firing = true;
		}
		
	}
	
	public boolean update(Base[] bases, City[] cities) {
		
		if (firing && !(Math.abs(missileY - targetY)<10)) {
			missileX += missileDX;
			missileY += missileDY;
		} else if (firing) {
			firing = false;
			exploding = true;
			explosionR = 0;
			return true;
		} if (exploding && explosionR < radius) {
			explosionR++;
		} else if (exploding) {
			exploding = false;
			if (atkBase) {
				bases[newTarget].kill();
			} else {
				cities[newTarget].kill();
			}
		}	
		return false;
	}
	
	public void draw(Graphics g) {
		if (firing) {
			g.setColor(Color.BLUE);
			g.drawLine(x,y,(int)missileX,(int)missileY);
		} else if (exploding) {
			g.setColor(Color.BLUE);
			g.fillOval((int)missileX-explosionR,(int)missileY-explosionR,explosionR*2,explosionR*2);
		}
	}
	
}