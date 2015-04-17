/*
	This class represents a base. Bases fire missiles, update their positions, check for collisions
	with enemies, and draw themselves. If a base missile collides with an enemy, the enemy is destroyed.
	Author: Nicholas Young
	UPI: nyou045
*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Base {
	
	private int x,y,radius,explosionR,missiles,ticksTillImpact;
	private boolean firing,exploding,alive;
	private double targetX,targetY,missileX,missileY,missileDX,missileDY;
	
	public Base (int newX,int newY,int missileSpeed){
		x = newX;
		y = newY;
		radius = 25;
		missiles = 10;
		ticksTillImpact = missileSpeed;
		alive = true;
		
		firing = false;
		exploding = false;
	}
	
	public void revive() {
		alive = true;
		missiles = 10;
	}
	
	public Point getPos() {
		Point p = new Point(x,y);
		return p;
	}
	
	public boolean getStatus() {
		return alive;
	}

	
	public void kill() {
		alive = false;
	}
	
	public boolean fire(int mouseX,int mouseY) {
		if (!firing && !exploding && mouseY<500 && missiles > 0 && alive) {
			missiles--;
			targetX = mouseX;
			targetY = mouseY;
			missileX = x;
			missileY = y;
			missileDX = (targetX-x)/ticksTillImpact;
			missileDY = (targetY-y)/ticksTillImpact;
			firing = true;
			return true;
		} else {
			return false;
		}
		
	}
			
	public boolean update() {
		
		if (firing && !((Math.abs(missileX - targetX)<5) && (Math.abs(missileY - targetY)<5))) {
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
		}
		return false;
	}
	
	public void collides(Enemy e) {
		if (exploding) {
			Rectangle explosion = new Rectangle((int)missileX - explosionR,(int)missileY-explosionR,explosionR*2,explosionR*2);
			if (explosion.contains(e.getPos())) {
				e.kill();
			}
		}
	}
	
	public boolean collides(Rectangle plane) {
		if (exploding) {
			Rectangle explosion = new Rectangle((int)missileX - explosionR,(int)missileY-explosionR,explosionR*2,explosionR*2);
			if (explosion.intersects(plane)) {
				return true;
			}
		}
		return false;
	}
		
		
	public void draw(Graphics g) {
		if (alive) {
			g.setColor(Color.YELLOW);
			g.fillOval(x-radius*2,y-radius,radius*2,radius*2);
			g.fillOval(x,y-radius,radius*2,radius*2);
			g.setColor(Color.BLACK);
			g.drawString("" + missiles,x-5,y+10);
		}
		if (firing) {
			g.setColor(Color.BLUE);
			g.drawLine(x,y,(int)missileX,(int)missileY);
		} else if (exploding) {
			g.setColor(Color.BLUE);
			g.fillOval((int)missileX-explosionR,(int)missileY-explosionR,explosionR*2,explosionR*2);
		}
	}
	
}