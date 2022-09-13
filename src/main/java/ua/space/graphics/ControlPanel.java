package ua.space.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ua.space.control.KeyboardControl;
import ua.space.database.DBWorker;
import ua.space.database.EnemyManager;
import ua.space.entity.Bullet;
import ua.space.entity.Enemy;
import ua.space.entity.Player;

public class ControlPanel extends JFrame implements Serializable {

	private static final long serialVersionUID = -3194033653300922913L;

	public ControlPanel() {
		initComponents();
	}

	private void initComponents() {
		setTitle("SpaceUA");
		setSize(1366, 768);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		GamePanel gamePanel = new GamePanel(
				new Player(100, 100, 330, 0, 1,
						new ImageIcon(getClass().getClassLoader().getResource("images/spaceFighterUA.png")).getImage(),
						new ImageIcon(getClass().getClassLoader().getResource("images/spaceFighterSpeedUA.png")).getImage()),
				new ArrayList<Bullet>(), new ArrayList<Enemy>(), new KeyboardControl(),
				new EnemyManager(new DBWorker()), new Font("Times New Roman", Font.BOLD, 20), new Color(30, 30, 30),
				new Color(252, 255, 193), 20);

		add(gamePanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				gamePanel.start();
			}
		});
	}
}
