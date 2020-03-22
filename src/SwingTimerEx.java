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
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int code = e.getKeyCode();
			    String key = KeyEvent.getKeyText(code); 
			    board.key_released(key);

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}
        };
        this.addKeyListener(listener);
    }
    
    private void initUI() {
    	board = new Board();
        add(board);
        
        setResizable(false);
        pack();
        
        setTitle("Cars the movie 1");
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