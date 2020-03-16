import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel
        implements ActionListener {
	Toolkit tk = Toolkit.getDefaultToolkit();
	public static final double FRICTION_FORWARD = 0.04;
    public static final double FRICTION_SIDE = 0.1;
    private static final double STOP = 0.02;
    private final int B_WIDTH = (int) tk.getScreenSize().getWidth();
    private final int B_HEIGHT = (int) tk.getScreenSize().getHeight();
    private final int FPS = 60;
    private final int DELAY = 1000/FPS;
    private final double TURNINGRATE = 0.1;
    private final double BREAKFRIC = 0.02;
    private final double ACCEL = 0.3;
    private Car car;
    private Timer timer;

    private boolean[] presses = new boolean[5];
    
    public Board() {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        car = new Car(B_WIDTH / 2.0, B_HEIGHT / 2.0, 25, 75, 0);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawStuff(g);
    }

    private void drawStuff(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.drawRoundRect(700, 400, 500, 300, 100, 100);
    	g2.setColor(new Color(100, 100, 100));
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	checkpresses(); // does what ever needs to be done if any keys are pressed.
    	breaking(car); // adds frictions
        car.move(g2); // moves the car to its new position
    	}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    
    private void checkpresses() {
    	if(presses[4]) //S: activates breaks
			breaking(car, BREAKFRIC);
    	else
            if (presses[0]){ //W: activates gas, can break and gas at the same time so breaking takes priority
                car.accel(ACCEL);
            if(presses[1]) //W: activates gas, can break and gas at the same time so breaking takes priority
                car.accel(-ACCEL);
        }
		if(presses[2]) {
    		//D: turns the car right
	    	car.turn(TURNINGRATE);
		}
		if(presses[3]) {
    		//A: turns the car left
	    	car.turn(-TURNINGRATE);
		}
		//System.out.println(car.getSpeed());
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
    
    public static void breaking(Car object) { // for friction
    	//breaking(object, FRICTION_FORWARD);
        object.getSpeed().projection_accelaration(object.getAngle() - Math.PI, -FRICTION_FORWARD, -FRICTION_SIDE);
        //System.out.println(object.getSpeed().size());
    }
    
    public static void breaking(Car object, double by) { // breaks the object, can also be used for friction
        object.getSpeed().paccel(by);
        if (object.getSpeed().getX() < STOP && (object.getSpeed().getX() > -STOP))
            object.getSpeed().setX(0);
        if (object.getSpeed().getY() < STOP && (object.getSpeed().getY() > -STOP))
            object.getSpeed().setY(0);
    }
}