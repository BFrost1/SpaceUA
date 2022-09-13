package ua.space.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import ua.space.control.KeyboardControl;
import ua.space.database.EnemyManager;
import ua.space.entity.Bullet;
import ua.space.entity.Enemy;
import ua.space.entity.Player;

public class GamePanel extends JComponent {

	private static final long serialVersionUID = 831791634939148630L;

	private Graphics2D graphics;
	private BufferedImage bufferedImage;
	private Thread thread;

	private final int FPS = 60;
	private final long TARGER_TIME = TimeUnit.SECONDS.toNanos(1) / FPS;
	private boolean start = true;
	private int shotTime;

	private Player player;
	private List<Bullet> bullets;
	private List<Enemy> enemys;
	private KeyboardControl keyboardControl;
	private EnemyManager enemyManager;
	private Font font;
	private Color colorBackground;
	private Color colorText;

	private int limit;
	private int killEnem;
	private String text;

	public GamePanel(Player player, ArrayList<Bullet> bullets, ArrayList<Enemy> enemys, KeyboardControl keyboardControl,
			EnemyManager enemyManager, Font font, Color colorBackground, Color colorText, int limit) {
		this.player = player;
		this.bullets = bullets;
		this.enemys = enemys;
		this.keyboardControl = keyboardControl;
		this.enemyManager = enemyManager;
		this.font = font;
		this.colorBackground = colorBackground;
		this.colorText = colorText;
		this.limit = limit;
	}

	public void start() {
		bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics = bufferedImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (start) {
					try {
						long startTime = System.nanoTime();
						drawBackground();
						drawGame();
						render();
						long time = System.nanoTime() - startTime;
						if (time < TARGER_TIME) {
							TimeUnit.NANOSECONDS.sleep(TARGER_TIME - time);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				drawBackground();
				drawGame();
				render();
				JOptionPane.showConfirmDialog(null, "Game over. You " + text, "Results", JOptionPane.DEFAULT_OPTION);
				System.exit(0);
			}
		});
		createKeyboardControl();
		createBullets();
		addEnemy();
		controlEnemy();
		thread.start();
	}

	private void addEnemy() {
		Random random = new Random();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (killEnem < limit) {
					enemys.add(enemyManager.getEnemy(new Random().nextInt(3 + 1), getWidth(),
							random.nextInt(getHeight() - 100) + 25));

					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				enemys.add(enemyManager.getEnemy(4, getWidth(), 195));
			}
		}).start();
	}

	private void createKeyboardControl() {
		requestFocus();
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					keyboardControl.setLeft(true);
					break;
				case KeyEvent.VK_D:
					keyboardControl.setRigth(true);
					break;
				case KeyEvent.VK_W:
					keyboardControl.setForward(true);
					break;
				case KeyEvent.VK_SPACE:
					keyboardControl.setSpace(true);
					break;
				}
			};

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					keyboardControl.setLeft(false);
					break;
				case KeyEvent.VK_D:
					keyboardControl.setRigth(false);
					break;
				case KeyEvent.VK_W:
					keyboardControl.setForward(false);
					break;
				case KeyEvent.VK_SPACE:
					keyboardControl.setSpace(false);
					shotTime = 0;
					break;

				}
			};
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				float s = 0.5f;
				while (start) {
					float angle = player.getAngle();
					if (keyboardControl.isLeft()) {
						angle -= s;
					}
					if (keyboardControl.isRigth()) {
						angle += s;
					}
					if (keyboardControl.isSpace()) {
						if (shotTime == 0) {
							bullets.add(0, new Bullet(player.getCentralX(), player.getCentralY(), player.getAngle(), 10,
									3f, 50, Color.red));
						}
						shotTime++;
						if (shotTime == 15) {
							shotTime = 0;
						}
					}

					if (keyboardControl.isForward()) {
						player.speedUP();
					} else {
						player.speedDown();
					}
					player.update();
					player.setAngle(angle);

					for (int i = 0; i < enemys.size(); i++) {
						if (enemys.get(i) != null) {
							enemys.get(i).update();
							if (!enemys.get(i).check(getWidth(), getHeight())) {
								if (enemys.get(i).getName().equals("boss")) {
									text = "lose";
									start = false;
								}
								enemys.remove(enemys.get(i));
							}
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void controlEnemy() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (start) {
					for (int i = 0; i < enemys.size(); i++) {
						if (enemys.get(i) != null && enemys.get(i).isFire()) {
							bullets.add(new Bullet(enemys.get(i).getCentralX(), enemys.get(i).getCentralY(),
									enemys.get(i).getAngle(), 7, 3f, 50, Color.yellow));
						}
					}
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	private void createBullets() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (start) {
					for (int i = 0; i < bullets.size(); i++) {
						Bullet bullet = bullets.get(i);
						if (bullet != null) {
							bullet.update();
							if (!checkBulletsEnemys(bullet)) {
								if (!bullet.check(getWidth(), getHeight())) {
									bullets.remove(bullet);
								}
							}
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private boolean checkBulletsEnemys(Bullet bullet) {
		if (bullet.getColor() == Color.red) {
			for (int i = 0; i < enemys.size(); i++) {
				if (enemys.get(i) != null) {
					Area area = bullet.getShape();
					area.intersect(enemys.get(i).getShap());
					if (!area.isEmpty()) {
						if (enemys.get(i).lossHp(bullet.getDamage()) <= 0) {
							if (enemys.get(i).getName().equals("boss")) {
								enemys.remove(enemys.get(i));
								text = "won";
								start = false;
							} else {
								enemys.remove(enemys.get(i));
								if (killEnem != limit) {
									killEnem++;
								}
							}
						}
						bullets.remove(bullet);
						return true;
					}
				}
			}
		} else if (bullet.getColor() == Color.yellow)

		{
			Area area = bullet.getShape();
			area.intersect(player.getShap());
			if (!area.isEmpty()) {
				if (player.lossHp(bullet.getDamage()) <= 0) {
					text = "lose";
					start = false;
				}
				bullets.remove(bullet);
				return true;
			}
		}
		return false;
	}

	private void drawBackground() {
		graphics.setColor(colorBackground);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		graphics.setFont(font);
		graphics.setColor(colorText);
		graphics.drawString("HP:" + player.getHp() + "     Kills: " + killEnem + " | " + limit, 10, 20);
	}

	private void drawGame() {
		player.draw(graphics);
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i) != null) {
				bullets.get(i).draw(graphics);
			}
		}
		for (int i = 0; i < enemys.size(); i++) {
			if (enemys.get(i) != null) {
				enemys.get(i).draw(graphics);
			}
		}
	}

	private void render() {
		Graphics g = getGraphics();
		g.drawImage(bufferedImage, 0, 0, null);
		g.dispose();
	}
}
