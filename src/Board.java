import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;


public class Board extends JPanel implements ActionListener { // the board of the game, which is the main part of it
	Toolkit tk = Toolkit.getDefaultToolkit();

    private final int B_WIDTH = (int) tk.getScreenSize().getWidth(); // width of the screen
    private final int B_HEIGHT = (int) tk.getScreenSize().getHeight(); // height of the screen

    private final int FPS = 60; // frames per second
    private final int DELAY = 1000/FPS; // time between frames

    private Image track; // the image of the track for displaying on screen
    private static HashSet<Point> hitMap; //hash map of where you can and cannot be on screen
    private Car car; // the car
    private Timer timer; // timer for looping main

	public static final double FRICTION_FORWARD = 0.03; // size of the friction applied towards the car's direction
    public static final double FRICTION_SIDE = 0.05; // size of the friction applied perpendicular to the car's direction
    private final double BREAKFRIC = 0.02; // size of the friction applied when breaking
    private static final double STOP = 0.1; // size speed which i round to 0
    private final double TURNINGRATE = 0.1; // turning rate
    private final double ACCEL = 0.3; // acceleration rate

    private Color timerColor = new Color(51, 236, 226); // color of the timer
    private Color carColor = new Color(195, 0, 195); // color of the car
    private int time = 0; // timer for displaying on screen. counts in ticks

    private boolean[] presses = new boolean[5]; // key pressing map
    
    public Board() {
        // initializer of board
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT)); // sets screen size

        BufferedImage hitMapImage = null; // loads hitmap.png to translate to a hashmap
        try {
            File file = new File("hitmap.png");
            if (file.exists())
                hitMapImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        hitMap = new HashSet<>(); // translates the hitmap.png to a hashmap
        if (hitMapImage != null) {
            for (int x = 0; x < hitMapImage.getWidth(); x++) {
                for (int y = 0; y < hitMapImage.getHeight(); y++) {
                    if (hitMapImage.getRGB(x, y) == -16777216) { // if hitmap.png is black in this spot, add it to the hashmap
                        hitMap.add(new Point(x, y));
                    }
                }
            }
        }

        car = new Car(B_WIDTH / 2, B_HEIGHT / 1.15, 25, 75, 0); // creates the car
        track = new ImageIcon("racetrack.png").getImage(); // creates the track
        timer = new Timer(DELAY, this); // creates the loop cycle
        timer.start(); // starts the loop cycle
    }

    @Override
    public void paintComponent(Graphics g) { // the part that loops
        super.paintComponent(g);
        time++; // increases the timer by one tick
        drawStuff(g); // enters the main loop
    }

    private void drawStuff(Graphics g) { // the main loop of this program
    	Graphics2D g2 = (Graphics2D) g; // creates a new graphics for customization
        g2.setStroke(new BasicStroke(2)); // sets stroke size to 2
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // makes the edges look nicer

    	drawTrack(g2); // draws the race track

    	String str = ""; //translates the ticks to an actual timer in format of min:sec.milisec
    	str += (time / FPS / 60) + ":" + (time / FPS % 60) + "." + (time % FPS * 50 / 3);

        g2.setColor(timerColor); // sets color for the timer
        g2.setFont(new Font("Courier", Font.BOLD,75)); // sets font and size of the timer
        g2.drawString(str, 50, 100); // draws the timer

    	checkpresses(); // if any keys were pressed, this does the events those keys activate
    	friction(car); // adds frictions

        car.move(); // calculates the car's new position

        collisionCheck(); // checks for collisions

        g2.setColor(carColor); // sets color for the car
        car.draw(g2); // draws the car
    	}

    private void collisionCheck(){ // checks for collisions made between the car and the road
        int x, y;
        for (Point pnt:car.getEdges() // runs through the edges of the car
             ) {
            x = (int)Math.abs(pnt.getX());
            y = (int)Math.abs(pnt.getY());
            if (hitMap.contains(new Point(x, y))){ // if the edge of the car is in the hashmap kill it
                car.shine(); // kills the car
                break;
            }
        }
    }

    private void drawTrack(Graphics g){ //draws the track
        g.drawImage(track, 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private void checkpresses() { // activates the events according to the key presses
    	if(presses[4]) //Space: activates breaks
			breaking(car, BREAKFRIC);
    	else
    	    if(!presses[0] || !presses[1]) // you cant both fas forward and backwards
    	    {
                if (presses[0]) //W: activates gas, can break and gas at the same time so breaking takes priority
                    car.accel(ACCEL);
                if(presses[1]) //S: activates gas backwards, can break and gas at the same time so breaking takes priority
                    car.accel(-ACCEL / 2);
    	    }
		if(presses[2]) {
    		//D: turns the car right
	    	car.wheelTurn(TURNINGRATE);
		}
		if(presses[3]) {
    		//A: turns the car left
	    	car.wheelTurn(-TURNINGRATE);
		}
    }

    public void key_pressed(String key) { // stores the keys that are currently being pressed in the pressed table by their index
    	if(key.equals("W"))
    		presses[0] = true;
    		
    	if(key.equals("S"))
    		presses[1] = true;
    		
    	if(key.equals("D"))
    		presses[2] = true;
    		
    	if(key.equals("A"))
            presses[3] = true;

        if(key.equals("Space"))
            presses[4] = true;
    }

    public void key_released(String key) { // releases the keys that are being pressed from the pressed table
    	if(key.equals("W"))
    		presses[0] = false;
    	if(key.equals("S"))
    		presses[1] = false;
    	if(key.equals("D"))
    		presses[2] = false;
    	if(key.equals("A"))
    		presses[3] = false;
        if(key.equals("Space"))
            presses[4] = false;
    }


    public static void friction(Car object) { // applies friction
        object.getSpeed().projectionAcceleration(object.getAngle() - Math.PI, -FRICTION_FORWARD, -FRICTION_SIDE);
    }
    
    public static void breaking(Car object, double by) { // applies breaking/friction
        object.getSpeed().paccel(by); // breaks the object by the amount specified

        // if the speed is under STOP, round it to 0
        if (object.getSpeed().getX() < STOP && (object.getSpeed().getX() > -STOP))
            object.getSpeed().setX(0);
        if (object.getSpeed().getY() < STOP && (object.getSpeed().getY() > -STOP))
            object.getSpeed().setY(0);
    }
}