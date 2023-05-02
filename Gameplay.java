
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Gameplay extends JPanel {
	private final int SIZE;
	private ArrayList<Opponent> opponents;
	Player player;
	
	// Opponent opponent;

	public Gameplay(int size) {
		SIZE = size;
		opponents = new ArrayList<>();
		setPreferredSize(new Dimension(SIZE, SIZE));
		setBackground(new Color(250, 240, 230));
	}

	public void setCharacters() {
		player = new Player(SIZE);
		for (int i = 0; i < SIZE / 50; i++) {
			opponents.add(new Opponent(SIZE));
		}
	}

	public void movePlayer(Character.Direction direction) {
		player.move(direction);
	}

	public void moveOpponents() {
		for (Opponent opponent : opponents) {
			if (!opponent.intersects(player))
				opponent.move(opponent.getDirection());
		}
	}

	// idk how this should work
	public Character.Direction validMove(Character.Direction direction) {
		for (int i = 0; i < opponents.size(); i++) {
			if (player.intersects(opponents.get(i)) && !player.sameColor(opponents.get(i)))
				return Character.Direction.CENTER;
		}
		return direction;
	}

	public void intersection(Character.Direction direction) {
		for (int i = 0; i < opponents.size(); i++) {
			if (player.intersects(opponents.get(i)) && player.sameColor(opponents.get(i))) {
				opponents.remove(i);
				player.diameter *= 2;
			}
		}
	}

	public boolean gameWon() {
		// return true;
		// if no more opponents in player color or if player is sufficiently large, player has won and game ends
		int opponentsLeft = 0;
		for (Opponent opponent : opponents) {
			if(opponent.COLOR.equals(player.COLOR)) {
				opponentsLeft++;
			}

		}
		if (opponentsLeft == 0 || player.diameter > SIZE * .9) {
			return true;
		}
		else
			return false;
	}

	public void paint() {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// draw each opponent, passing in the same graphics object so that they will all
		// display in this panel
		for (Opponent opponent : opponents) {
			opponent.draw(g2d);
		}
		player.draw(g2d);
		// draws grid for testing
		// for (int i = 0; i < SIZE; i += 30) {
		// g2d.drawLine(i, 0, i, SIZE);
		// g2d.drawLine(0, i, SIZE, i);
		// }
	}

	public static void main(String[] args) {

		final int SIZE = 1000;

		Gameplay panel = new Gameplay(SIZE);
		JFrame frame = new JFrame("SWELL");

		panel.setCharacters();

		// set up the event handler
		class keyEvent implements KeyListener {
			@Override
			public void keyPressed(KeyEvent k) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!panel.gameWon()) {
					Character.Direction direction = Character.Direction.CENTER;
					if (e.getKeyChar() == 'w')
						direction = Character.Direction.UP;
					if (e.getKeyChar() == 's')
						direction = Character.Direction.DOWN;
					if (e.getKeyChar() == 'a')
						direction = Character.Direction.LEFT;
					if (e.getKeyChar() == 'd')
						direction = Character.Direction.RIGHT;
						panel.intersection(direction);
					panel.movePlayer(direction);
					panel.moveOpponents();
					panel.paint();
				} else {
					System.out.println("you win!");
					// how do I put an option to make the game replayable?
					// ideally I'd have it show a different screen that asks: play again?
					// and if yes is clicked then game resets
					// but how to implement the actual resetting?
					// gets a bit tricky with variable scope and all
					System.exit(1);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		}



		frame.addKeyListener(new keyEvent());
		// ==== Start the application
		// add both our panels to the top-level frame
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// tell the frame to resize itself to fit the contents
		frame.pack();

		// show the frame
		frame.setVisible(true);
	}
}
