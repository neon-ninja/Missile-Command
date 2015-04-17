/*
	This program implements a game in which the objective is to protect the users 3
	bases and 6 cities by firing counter-missiles at incoming missiles
	Author: Nicholas Young
	UPI: nyou045
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;

public class A3JPanel extends JPanel implements ActionListener,KeyListener,MouseMotionListener {

	private String spaces = "                                          ";
	private Timer t,t1;
	private int level,score,width,height,mouseX,mouseY,ticksTillImpact,tick,numEnemies,numCities;
	private long start,secondsPassed,lastWave;
	private boolean displayIntro,gameOver,planeFly;
	private Base[] bases;
	private City[] cities;
	private Enemy[] enemies;
	private boolean[] cityStatus;
	private Plane plane;
	private AudioClip newLevel,fire,explosion;

	
 	public A3JPanel() {
		setBackground(Color.BLACK);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		width = 800;
		height = 600;
		mouseX = 400;
		mouseY = 300;
		displayIntro = true;
		gameOver = false;
		planeFly = false;
		level = 0;
		score = 0;
		tick = 25;
		t = new Timer(tick,this);
		t1 = new Timer(1000,this);
		ticksTillImpact = 40;
		
		bases = new Base[3];
		bases[0] = new Base(100,height-50,ticksTillImpact);
		bases[1] = new Base(400,height-50,ticksTillImpact/2);
		bases[2] = new Base(width-100,height-50,ticksTillImpact);

		
		cities = new City[6];
		cities[0] = new City(195,height-50);
		cities[1] = new City(257,height-50);
		cities[2] = new City(315,height-50);
		cities[3] = new City(488,height-50);
		cities[4] = new City(554,height-50);
		cities[5] = new City(611,height-50);
		numCities = 6;
		
		plane = new Plane(width);
		
		enemies = new Enemy[100];
		enemies [0] = new Enemy(width,level,ticksTillImpact,bases,cities);
		enemies [1] = new Enemy(width,level,ticksTillImpact,bases,cities);
		numEnemies = 2;
		
		newLevel = loadSound("newLevel.wav");
		fire = loadSound("fire.wav");
		explosion = loadSound("explosion.wav");
		
		addKeyListener(this);
		addMouseMotionListener(this);
	}
	
	
	public void newLevel () {
		level++;
		numEnemies = 0;
		plane.reset();
		start = System.currentTimeMillis();
		lastWave = 0;
		secondsPassed = 0;
		for (int i = 0;i<bases.length;i++) {
			bases[i].revive();
		}
		newLevel.play();
		repaint();

	}
	
	private AudioClip loadSound(String fileName) {
		URL url = getClass().getResource(fileName);
		return Applet.newAudioClip(url);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == t1) {
			secondsPassed = (System.currentTimeMillis() - start)/1000;
			
			int maxEnemies = level * 6;
			if (maxEnemies>30) {
				maxEnemies = 30;
			}
			if (numEnemies < maxEnemies && (secondsPassed > lastWave + 2)) {
				enemies [numEnemies] = new Enemy(width,level,ticksTillImpact,bases,cities);
				numEnemies++;
				enemies [numEnemies] = new Enemy(width,level,ticksTillImpact,bases,cities);
				numEnemies++;
				lastWave = secondsPassed;
			} else if (numEnemies >= maxEnemies) {
				int numAlive = 0;
				for (int i = 0;i<numEnemies;i++) {
					if (enemies[i].getStatus()) {
						numAlive++;
					}
				}
				if (numAlive == 0) {
					score += numEnemies*100 - (6 - numCities) * 20;
					newLevel();
					
				}
			}
			
		} else {
		for (int i = 0;i<bases.length;i++) {
			if (bases[i].update()) {
				explosion.play();
			}
		}
		for (int i = 0;i<numEnemies;i++) {
			if (enemies[i].update(bases, cities)) {
				explosion.play();
			}
		}
		
		if (secondsPassed > 8 && plane.getStatus()) {
			plane.update();
			if (bases[0].collides(plane.getArea()) || bases[1].collides(plane.getArea())|| bases[2].collides(plane.getArea())) {
				plane.kill();
				score += 1000;
			}
		}
		
		for (int i = 0;i<numEnemies;i++) {
			bases[0].collides(enemies[i]);
			bases[1].collides(enemies[i]);
			bases[2].collides(enemies[i]);
		}
		
		numCities = 0;
		for (int i = 0;i<cities.length;i++) {
			if (cities[i].getStatus()) {
				numCities++;
			}
		}
		if (numCities == 0) {
			gameOver = true;
		}
		
		repaint();
		}
	}
	

	
   	public void paintComponent(Graphics g){
 	    super.paintComponent(g);
		
		if (displayIntro) {
			drawTitleScreen(g);		
		} else {
			g.setColor(Color.BLUE);
			for (int i = 0;i<cities.length;i++) {
				cities[i].draw(g);
			}
			
			for (int i = 0;i<numEnemies;i++) {
				enemies[i].draw(g);
			}
			
			
			g.setColor(Color.YELLOW);
			g.fillRect(0,height - 50,width,50);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Courier New",Font.PLAIN,  12));
			g.drawString ("Level: " + level + spaces + "Score: " + score, 5, height-5);
			
		
			for (int i = 0;i<bases.length;i++) {
				bases[i].draw(g);
			}
			
			plane.draw(g);
			
			if (gameOver) {
				g.setColor(Color.RED);
				g.setFont(new Font("Courier New",Font.PLAIN,  82));
				g.drawString("THE END",50,240);
				t.stop();
				t1.stop();
			}		
		}
		
	}
	
	public void drawTitleScreen (Graphics g) {
		g.setFont(new Font("Courier New",Font.BOLD,  82));
		g.setColor(Color.RED);
		g.drawString("MISSILE COMMAND",32,113);
		g.setFont(new Font("Courier New",Font.PLAIN,  20));
		g.setColor(Color.WHITE);
		g.drawString("by nyou045",302,172);
		g.drawString("Instructions:",41,230);
		g.drawString("Mouse: Aim",87,261);
		g.drawString("z,x,c: Fire from 1st, 2nd, 3rd base",91,291);
		g.drawString("Objective",41,321);
		g.drawString("Protect your bases and cities by firing counter",90,355);
		g.drawString("missiles at the incoming missiles",90,385);
		g.drawString("Press any key to begin...",208,509);
	}
	
	public void keyPressed(KeyEvent e){
		if (!gameOver) {
			if (displayIntro) {
				displayIntro = false;
				repaint();
			} else if (!t.isRunning()) {
				t.start();
				t1.start();
				newLevel();
			} else if (e.getKeyChar() == 'z') {
				if (bases[0].fire(mouseX,mouseY)) {
					fire.play();
				}
			} else if (e.getKeyChar() == 'x') {
				if (bases[1].fire(mouseX,mouseY))  {
					fire.play();
				}
			} else if (e.getKeyChar() == 'c') {
				if (bases[2].fire(mouseX,mouseY))  {
					fire.play();
				}
			}
		}
		
	
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	
	}	
	
	public void keyReleased(KeyEvent e) {}		
	public void keyTyped(KeyEvent e){}	
	public void mouseDragged(MouseEvent e) {}	

}
