import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.EventQueue;
import javax.swing.JFrame;


public class SwingTimerEx extends JFrame {
	public Board board;

   public SwingTimerEx() {
        
        initUI();
        KeyListener listener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

			    int code = e.getKeyCode();
			    String key = KeyEvent.getKeyText(code); 
			    board.key_pressed(key);
			    
//			    System.out.println("   Code: " + KeyEvent.getKeyText(code));
//			 
//			    System.out.println("   Char: " + e.getKeyChar());
//			 
//			    int mods = e.getModifiersEx();
//			 
//			    System.out.println("    Mods: "
//			 
//			+ KeyEvent.getModifiersExText(mods));
//			 
//			    System.out.println("    Location: "
//			 
//			+ keyboardLocation(e.getKeyLocation()));
//			 
//			    System.out.println("    Action? " + e.isActionKey());
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int code = e.getKeyCode();
			    String key = KeyEvent.getKeyText(code); 
			    board.key_released(key);
				//printEventInfo("Key Released", e);
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
			//	printEventInfo("Key Typed", e);
				
			}
			private String keyboardLocation(int keybrd) {
				 
			    switch (keybrd) {
			 
			  case KeyEvent.KEY_LOCATION_RIGHT:
			 
			return "Right";
			 
			  case KeyEvent.KEY_LOCATION_LEFT:
			 
			return "Left";
			 
			  case KeyEvent.KEY_LOCATION_NUMPAD:
			 
			return "NumPad";
			 
			  case KeyEvent.KEY_LOCATION_STANDARD:
			 
			return "Standard";
			 
			  case KeyEvent.KEY_LOCATION_UNKNOWN:
			 
			  default:
			 
			return "Unknown";
			 
			    }
			 
			}
        };
        this.addKeyListener(listener);
    }
    
    private void initUI() {
    	board = new Board();
        add(board);
        
        setResizable(false);
        pack();
        
        setTitle("Tomo");
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public static void main(String[] args) {
	    
	    EventQueue.invokeLater(() -> {
	        SwingTimerEx ex = new SwingTimerEx();
	        ex.setVisible(true);
	    });
	}
}