package ua.space.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

import javax.swing.ImageIcon;

import ua.space.entity.Enemy;

public class EnemyManager {
	private static final String GET_ENEMY = "SELECT name, hp, size, angle, speed, image, fire FROM enemy WHERE id = ?";
	private DBWorker worker;

	public EnemyManager(DBWorker worker) {
		this.worker = worker;
	}

	public Enemy getEnemy(int id, int width, int height) {
		Connection conn = worker.getConnection();
		PreparedStatement pr = null;
		ResultSet rs = null;
		Enemy enemy = null;

		if (Objects.nonNull(conn)) {
			try {
				pr = conn.prepareStatement(GET_ENEMY);
				pr.setInt(1, id);
				rs = pr.executeQuery();
				while (rs.next()) {
					enemy = new Enemy(rs.getString("name"), rs.getInt("hp"), rs.getDouble("size"), width, height,
							rs.getFloat("angle"), rs.getFloat("speed"),
							new ImageIcon(getClass().getClassLoader().getResource(rs.getString("image"))).getImage(),
							rs.getBoolean("fire"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (Objects.nonNull(rs)) {
						rs.close();
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				try {
					if (Objects.nonNull(pr)) {
						pr.close();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					if (Objects.nonNull(conn)) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return enemy;
	}
	
	public static void main(String[] args) {
		System.out.println(new Random().nextInt(3+1));
	}
}
