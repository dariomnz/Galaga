package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
//import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Librerias.Animacion;
import Librerias.Sound;
import edu.uc3m.game.GameBoardGUI;

/**
 * Se encarga de todo lo relacionado con la interfaz del juego, por eso extiende
 * GameBoardGUI para poder añadir la funcion de pulsacion de teclado y poder
 * hacer el movimiento del personaje fluido entre otras cosas.
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Interfaz extends GameBoardGUI {

	private static final long serialVersionUID = 7972406964063694049L;

	private int numeroId = 0;

	public int getUniqueNumeroId() {
		numeroId++;
		return numeroId;
	}

	private boolean isJugando = true;

	/**
	 * Es el valor necesario para el movimiento del jugador
	 */
	private int RIGHT, LEFT;

	private boolean pause = false;
	private int scene = 0;
	private int level = 1;
	private int patron = 0;
	private long fotograma;
	private String lastAction = "";

	private Sprite pauseSprite;
	public JLabel label1;
	protected JTextField commandBar;
	private JLayeredPane jLayeredPane;
	protected JTextArea commandConsole;
	protected JScrollPane commandConsoleScrollPane;

	private Clip soundDisparo;
	private Clip soundEnemyExplosion;
	private Clip soundPlayerExplosion;
	private Clip soundDrive;
	private Clip soundPowerUp;
	private Clip soundEnemyShoot;
	private Clip soundSpecialShoot;
	private Clip soundHitPlayer;
	private Clip[] soundSong = new Clip[3];

	public void setPauseSprite(Sprite pauseSprite) {
		this.pauseSprite = pauseSprite;
		pauseSprite.move(85, 110);
	}

	Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Constructor de la interfaz
	 * 
	 * @param boardWidth  anchura del tablero
	 * @param boardHeight altura del tablero
	 */
	public Interfaz(int boardWidth, int boardHeight) {
		/*
		 * El super lo que hace es llamar al constructor de GameBoardGUI
		 */
		super(boardWidth, boardHeight);

		setTitle("Galaga by Dario");

		setVisible(true);
		getContentPane().setBackground(Color.black);
		setSize(530, getHeight());
		setResizable(true);
		gb_setGridColor(0, 0, 0);
		for (int i = 0; i < 17; i++)
			for (int j = 0; j < 22; j++)
				gb_setSquareColor(i, j, 0, 0, 0);

		soundDisparo = Sound.getSound("shoot.wav");
		soundEnemyExplosion = Sound.getSound("invaderKilled.wav");
		soundPlayerExplosion = Sound.getSound("explosion.wav");
		soundDrive = Sound.getSound("dive.wav");
		soundEnemyShoot = Sound.getSound("enemyshoot.wav");
		soundSpecialShoot = Sound.getSound("specialshoot.wav");
		soundPowerUp = Sound.getSound("powerup.wav");
		soundHitPlayer = Sound.getSound("hitplayer.wav");
		soundSong[0] = Sound.getSound("song02.wav");
		soundSong[1] = Sound.getSound("song04.wav");
		soundSong[2] = Sound.getSound("song16.wav");

		label1 = new JLabel("FPS: 00");
		add(label1);
		label1.setBounds(700, 0, 100, 30);
		label1.setForeground(Color.white);

		commandBar = new JTextField();
		commandBar.setBounds(5, 615, 495, 30);
		jLayeredPane = getLayeredPane();
		jLayeredPane.add(commandBar, jLayeredPane.highestLayer());
		commandBar.setVisible(false);
		commandBar.setBackground(new Color(70, 70, 70, 100));
		commandBar.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 100)));
		commandBar.setForeground(Color.white);
		commandBar.setFont(new Font("Arial", 1, 25));

		commandConsole = new JTextArea();

		commandConsole.setVisible(true);
		commandConsole.setBackground(new Color(70, 70, 70, 0));
		commandConsole.setForeground(Color.white);
		commandConsole.setFont(new Font("Arial", 1, 20));

		commandConsole.setEditable(false);
		commandConsole.setRows(5);
		commandConsole.setColumns(15);
		commandConsole.setLineWrap(true);
		commandConsoleScrollPane = new JScrollPane(commandConsole);

		jLayeredPane.add(commandConsoleScrollPane, jLayeredPane.highestLayer());
		commandConsoleScrollPane.setBounds(5, 450, 495, 160);
		commandConsoleScrollPane.getViewport().setBackground(new Color(70, 70, 70, 100));
		commandConsoleScrollPane.setViewportBorder(null);
		commandConsoleScrollPane.setWheelScrollingEnabled(true);
		commandConsoleScrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70, 100)));
		commandConsoleScrollPane.setForeground(Color.white);
		commandConsoleScrollPane.setFont(new Font("Arial", 0, 20));
		commandConsoleScrollPane.getVerticalScrollBar().setUI(null);
		commandConsoleScrollPane.setVisible(false);

		/*
		 * El keyboardFocusManager lo uso para generar un foco para recoger el evento
		 * KeyReleased
		 */
		KeyboardFocusManager.setCurrentKeyboardFocusManager(null);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				return keyboardControl(e);
			}
		});

	}

	/**
	 * Se encarga de implementar las pulsaciones de teclas como se requiere depende
	 * e la scene
	 * 
	 * @param e
	 * @return
	 */
	public boolean keyboardControl(KeyEvent e) {

		boolean consumed = false;

		if (e.getID() == KeyEvent.KEY_PRESSED) {

			if (getScene() == 0) {
				setLastAction("anyKey");
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					if (commandBar.getText().length() > 1) {
						if (commandBar.getText().split(" ").length < 2) {
							if (commandBar.getText().length() <= 10) {
								pintarEnConsola("	" + commandBar.getText());
								setLastAction("command " + commandBar.getText());
								commandBar.setVisible(false);
								commandConsoleScrollPane.setVisible(false);
							} else {
								pintarEnConsola("Nombre no valido, es demasiado largo");
							}
						} else {
							pintarEnConsola("Nombre no valido, contiene espacios");
						}
					}
					commandBar.setText("");
					consumed = true;
				}
			} else if (getScene() == 3) {

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setJugando(false);
					consumed = true;
				} else {
					setLastAction("anyKey");
					consumed = true;
				}

			} else {

				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
					setLastAction("up");
					if (!player.isCanHit()) {
						if (!player.getBulletSpecial().isVisible()) {
							Sound.playSound(getSoundSpecialShoot());
							player.getBulletSpecial().move((int) player.getX(), (int) player.getY());
							player.getBulletSpecial().setTamanio(10);
							player.getBulletSpecial().setImage(
									Animacion.bulletEspecial[player.getBulletSpecial().getNumImageBulletSpecial()], 10);
							player.getBulletSpecial().setVisible(true);
							player.addDisparos();
						}
					}
					consumed = true;
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					LEFT = 1;
					setLastAction("left");
					consumed = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					RIGHT = 1;
					setLastAction("right");
					consumed = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					setLastAction("space");
					player.setCanShoot(true);
					consumed = true;

				}

				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					commandBar.setVisible(true);
					commandBar.requestFocusInWindow();
					commandConsoleScrollPane.setVisible(true);
					setPause(true);
					consumed = true;

				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					pintarEnConsola("	" + commandBar.getText());
					setLastAction("command " + commandBar.getText());
					commandBar.setVisible(false);
					commandBar.setText("");
					commandConsoleScrollPane.setVisible(false);
					if (!pauseSprite.isVisible())
						setPause(false);
					consumed = true;

				}

			}
		}
		if (getScene() != 0)
			if (e.getID() == KeyEvent.KEY_RELEASED) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					LEFT = 0;
					consumed = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					RIGHT = 0;
					consumed = true;
				}

				if (!commandBar.isVisible())
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (this.pause == true) {
							this.pause = false;
							pauseSprite.setVisible(false);
						} else {
							this.pause = true;
							pauseSprite.setImage("pause.png", 1000);
							pauseSprite.setVisible(true);
							commandBar.setVisible(false);
						}
						consumed = true;
					}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					player.setCanShoot(false);
					consumed = true;
				}
			}
		return consumed;
	}

	/**
	 * Hace que se escriba en la consola el texto introducido
	 * 
	 * @param text
	 */
	public void pintarEnConsola(String text) {
		if (text != null) {
			if (text.length() > 1)
				if (commandConsole.getText().length() == 0)
					commandConsole.setText(commandConsole.getText() + text);
				else
					commandConsole.setText(commandConsole.getText() + "\n" + text);

			commandConsole.setCaretPosition(commandConsole.getText().length());
		} else
			System.err.println("Error con pintarEnConsola() el texto introducido es null");
	}

	/*
	 * A partir de aqui los getters y setters
	 */

	public int getRIGHT() {
		return RIGHT;
	}

	public void setRIGHT(int RIGHT) {
		this.RIGHT = RIGHT;
	}

	public int getLEFT() {
		return LEFT;
	}

	public void setLEFT(int LEFT) {
		this.LEFT = LEFT;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLastAction() {
		return lastAction;
	}

	public String getLastActionAndRemove() {
		String postLastAction = this.lastAction;
		this.lastAction = "";
		return postLastAction;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public int getPatron() {
		return patron;
	}

	public void setPatron(int patron) {
		this.patron = patron;
	}

	public long getFotograma() {
		return fotograma;
	}

	public void setFotograma(long fotograma) {
		this.fotograma = fotograma;
	}

	public void addOneFotograma() {
		this.fotograma++;
	}

	/**
	 * Scene 0 pantalla de inicio, 1 pantalla de carga, 2 pantalla de juego y 3
	 * pantalla game over
	 * 
	 * @return
	 */
	public int getScene() {
		return scene;
	}

	public void setScene(int scene) {
		this.scene = scene;
	}

	public boolean isJugando() {
		return isJugando;
	}

	public void setJugando(boolean isJugando) {
		this.isJugando = isJugando;
	}

	public Clip getSoundDisparo() {
		return soundDisparo;
	}

	public Clip getSoundEnemyExplosion() {
		return soundEnemyExplosion;
	}

	public Clip getSoundPlayerExplosion() {
		return soundPlayerExplosion;
	}

	public Clip getSoundDrive() {
		return soundDrive;
	}

	public Clip getSoundPowerUp() {
		return soundPowerUp;
	}

	public Clip getSoundEnemyShoot() {
		return soundEnemyShoot;
	}

	public Clip getSoundSpecialShoot() {
		return soundSpecialShoot;
	}

	public Clip getSoundHitPlayer() {
		return soundHitPlayer;
	}

	public Clip[] getSoundSong() {
		return soundSong;
	}

}
