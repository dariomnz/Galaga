package Game;

import Librerias.*;

/**
 * Contiene el main
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Juego {

	private static boolean canPlayMusic = true;

	private static Interfaz interfaz;

	private static Player player;
	private static Enemy enemyControler;
	private static Enemy enemyArea;
	private static Enemy[][] enemy;

	private static Sprite[] stars;

	private static NumberLabel[] highScoreLabel;
	private static NumberLabel highScore;
	private static NumberLabel[] scoreLabel;
	private static NumberLabel Score;
	private static LetraLabel[] labelPressAnyKey;
	private static LetraLabel[] labelCreatedByDario;
	private static Sprite portada;
	private static LetraLabel[] labelNombre;
	private static Sprite transicion;
	private static Sprite stage;
	private static NumberLabel[] stageNumberLabel;
	private static LetraLabel[] labelResults;
	private static LetraLabel[] labelShotsFired;
	private static NumberLabel[] disparosLabel;
	private static LetraLabel[] labelNumberOfHits;
	private static NumberLabel[] disparosAcertadosLabel;
	private static LetraLabel[] labelHitMissRatio;
	private static NumberLabel[] hitMissRatioLabel;
	private static LetraLabel[] labelPressAnyKeyForNewGame;
	private static LetraLabel[] labelsPressEscapeToExit;

	/*
	 * Sirve para controlar el lag
	 */
	private static long pretime = System.currentTimeMillis();

	public static void main(String[] args) throws InterruptedException {

		createObjects();

		while (interfaz.isJugando()) {
			switch (interfaz.getScene()) {
			/*
			 * Esta es la pantalla de inicio en la que se pide el nombre del jugador
			 */
			case 0:
				boolean waiting = true;
				interfaz.addOneFotograma();

				portada.setImage("portada.png", 493, 638);
				portada.setVisible(true);
				LetraLabel.updateImage("press any key", labelPressAnyKey);
				for (int i = 0; i < labelPressAnyKey.length; i++) {
					labelPressAnyKey[i].move(i * 5 + 53, 180);
					labelPressAnyKey[i].setVisible(true);
				}
				/*
				 * Este while le hago para que al iniciar la aplicacion haya un segundo en el
				 * que si pulsas cualquier tecla no haga nada pero si que este parpadeando el
				 * letrasLabel
				 */
				while (waiting) {
					if (interfaz.getFotograma() % 60 == 0)
						waiting = false;
					interfaz.setLastAction("");
					interfaz.addOneFotograma();
					if (interfaz.getFotograma() % 30 == 0)

						for (int i = 0; i < labelPressAnyKey.length; i++) {
							if (labelPressAnyKey[i].isVisible())
								labelPressAnyKey[i].setVisible(false);
							else
								labelPressAnyKey[i].setVisible(true);
						}
					Thread.sleep(10);
				}
				waiting = true;
				/*
				 * Aqui ya esta esperando a que se pulse una tecla para pedir el nombre del
				 * jugador
				 */
				while (waiting) {
					if (interfaz.getFotograma() % 30 == 0)

						for (int i = 0; i < labelPressAnyKey.length; i++) {
							if (labelPressAnyKey[i].isVisible())
								labelPressAnyKey[i].setVisible(false);
							else
								labelPressAnyKey[i].setVisible(true);
						}

					if (interfaz.getLastActionAndRemove().equals("anyKey")) {
						waiting = false;
						for (int i = 0; i < labelPressAnyKey.length; i++)
							labelPressAnyKey[i].setVisible(false);
						interfaz.commandBar.setVisible(true);
						interfaz.commandBar.requestFocusInWindow();
						interfaz.commandConsoleScrollPane.setVisible(true);
						interfaz.pintarEnConsola("Escribe el nombre del jugador y pulsa enter");
					}
					interfaz.addOneFotograma();
					Thread.sleep(1000 / 60);
				}
				waiting = true;
				/*
				 * Aqui espera a que se meta un mombre valido por la barra de comandos
				 */
				while (waiting) {
					if (interfaz.getLastAction().startsWith("command")) {
						player.setName(interfaz.getLastAction().split(" ")[1]);
						LetraLabel.updateImage(player.getName(), labelNombre);
						waiting = false;
					}
					Thread.sleep(10);
				}
				portada.setVisible(false);
				interfaz.commandBar.setVisible(false);
				interfaz.setScene(interfaz.getScene() + 1);

				break;
			case 1:

				startGame();

				break;
			case 2:

				/*
				 * Se encarga de actualizar los letraLabel de puntuacion y del record
				 */
				if (interfaz.getFotograma() % 5 == 0) {
					if (player.getRecord() < player.getPuntos())
						NumberLabel.updateImage(player.getPuntos(), highScoreLabel);
					else
						NumberLabel.updateImage(player.getRecord(), highScoreLabel);

					NumberLabel.updateImage(player.getPuntos(), scoreLabel);
				}

				/*
				 * Se encarga de mover las estrellas
				 */
				for (int i = 0; i < stars.length; i++) {
					if (i < stars.length / 3) {
						stars[i].setY(stars[i].getY() + 0.25f);
						stars[i].updatePosition();
					} else if (i > stars.length / 3 * 2) {
						stars[i].setY(stars[i].getY() + 0.5f);
						stars[i].updatePosition();
					} else {
						stars[i].setY(stars[i].getY() + 0.35f);
						stars[i].updatePosition();
					}
					if (stars[i].getY() > 220) {
						stars[i].setY(0);
						stars[i].setX((int) (Math.random() * 170));
					}
					if (interfaz.getFotograma() % (int) (Math.random() * 30 + 50) == 0)
						if ((int) (Math.random() * 10) < 5)
							stars[i].setVisible(true);
						else
							stars[i].setVisible(false);
				}

				/*
				 * Se encarga de poner musica
				 */
				if (canPlayMusic) {
					if (!Sound.isAnyActive(interfaz.getSoundSong()))
						Sound.playSound(interfaz.getSoundSong()[(int) (Math.random() * 3)]);
				} else {
					for (int i = 0; i < interfaz.getSoundSong().length; i++) {
						interfaz.getSoundSong()[i].stop();
					}
				}

				/*
				 * Aqui se realizan todos los movimientos de los enemigos
				 */
				switch (interfaz.getPatron()) {
				case 0:
					Moves.Patron1(enemyControler, enemy);
					break;
				case 1:
					Moves.Patron2(enemyControler, enemy);
					break;
				case 2:
					Moves.Patron3(enemyControler, enemy);
					break;

				default:
					break;
				}

				enemyControler.updateEnemyControler(player, enemy);

				if (enemyArea.isVisible())
					enemyArea.move(((int) enemyControler.getX() + enemyArea.getXmatrix()),
							(int) enemyControler.getY() + enemyArea.getYmatrix());

				for (int i = 0; i < enemy.length; i++) {
					for (int j = 0; j < enemy[i].length; j++) {
						enemy[i][j].update();
					}
				}

				player.update();

				/*
				 * Este comprueba si se ha metido alguna linea por los comandos y depende de que
				 * cosa sea ejecuta algo diferente
				 */

				if (interfaz.getLastAction().startsWith("command"))

				{

					if (interfaz.getLastAction().endsWith("edit mode")) {
						interfaz.gb_setGridColor(100, 100, 100);
						enemyArea.setVisible(true);
						interfaz.setResizable(true);
						interfaz.pintarEnConsola("Entering editor mode");
					}
					if (interfaz.getLastAction().endsWith("normal mode")) {
						interfaz.gb_setGridColor(0, 0, 0);
						enemyArea.setVisible(false);
						interfaz.setResizable(false);
						interfaz.pintarEnConsola("Entering normal mode");
					}

					if (interfaz.getLastAction().endsWith("close")) {
						if (player.getPuntos() > player.getRecord()) {
							player.setRecord(player.getPuntos());
							TxtFile.guardarRecord(player);
						}
						interfaz.setJugando(false);
					}
					/*
					 * Te sube 10 puntos en y
					 */
					if (interfaz.getLastAction().endsWith("up")) {
						if (player.getY() > 10) {
							player.setY(player.getY() - 10);
							interfaz.pintarEnConsola("Subiendo");
						}
					}
					/*
					 * Te baja 10 puntos en y
					 */
					if (interfaz.getLastAction().endsWith("down")) {
						if (player.getY() < 210) {
							player.setY(player.getY() + 10);
							interfaz.pintarEnConsola("Bajando");
						}
					}
					/*
					 * Te limpia los consola
					 */
					if (interfaz.getLastAction().endsWith("clear")) {
						interfaz.commandConsole.setText("");
					}
					/*
					 * Te carga el siguiente nivel
					 */
					if (interfaz.getLastAction().endsWith("next level")) {

						changeLevel();

						interfaz.pintarEnConsola("Loading next level");
					}

					/*
					 * Te lleva al nivel que pongas
					 */
					if (interfaz.getLastAction().endsWith("level "
							+ interfaz.getLastAction().split(" ")[interfaz.getLastAction().split(" ").length - 1])) {

						try {
							interfaz.setLevel(Integer.parseInt((interfaz.getLastAction()
									.split(" ")[interfaz.getLastAction().split(" ").length - 1])) - 1);
						} catch (NumberFormatException e) {
							interfaz.pintarEnConsola(
									"ERROR\nEl formato del numero: "
											+ interfaz.getLastAction()
													.split(" ")[interfaz.getLastAction().split(" ").length - 1]
											+ " no es valido");
						}

						changeLevel();
						interfaz.pintarEnConsola("Loading level: " + interfaz.getLevel());
					}

					/*
					 * Entra y sale en el modo dios que no te pueden dar los enemigos
					 */
					if (interfaz.getLastAction().endsWith("god")) {
						if (player.isGodActive()) {
							player.setGodActive(false);
							interfaz.pintarEnConsola("God mode desactivated");
						} else {
							player.setGodActive(true);
							interfaz.pintarEnConsola("God mode activated");
						}

					}

					interfaz.setLastAction("");

				}

				/*
				 * Añade un fotograma cada vez que se reproduce el bucle
				 */
				interfaz.addOneFotograma();

				if (!player.isVisible()) {
					if (player.getPuntos() > player.getRecord()) {
						player.setRecord(player.getPuntos());
						TxtFile.guardarRecord(player);
					}
					interfaz.setScene(3);
				}
				do {
					/*
					 * Esto hace que el bucle se pare los milisegundos necesarios para que valla a
					 * 60 fps contando que lo demas tarda algo de tiempo en reproducirse
					 */
					if (1000 / 60 - (System.currentTimeMillis() - pretime) > 0)
						Thread.sleep(1000 / 60 - (System.currentTimeMillis() - pretime));
				} while (interfaz.isPause());
				/*
				 * Esto se encarga de decir los milisegundos que pasan por cada frame que tiene
				 * el juego
				 */

				interfaz.label1.setText("Ms per frame: " + ((System.currentTimeMillis() - pretime)));

				pretime = System.currentTimeMillis();

				break;
			case 3:

				/*
				 * Aqui se colocan todos los letraLabel y numberLabel de la escena final
				 */
				if (!transicion.getImage().equals("transicion.png"))
					transicion.setImage("transicion.png", 1000);

				transicion.setVisible(true);

				for (int i = 0; i < labelResults.length; i++) {

					labelResults[i].move(i * 6 + 60, 60);
					labelResults[i].setVisible(true);
				}

				for (int i = 0; i < labelShotsFired.length; i++) {

					labelShotsFired[i].move(i * 6 + 30, 80);
					labelShotsFired[i].setVisible(true);
				}

				NumberLabel.updateImage(player.getDisparos(), disparosLabel);
				for (int i = 0; i < disparosLabel.length; i++) {
					disparosLabel[i].move(i * 5 + 120, 80);
					disparosLabel[i].setVisible(true);
				}
				for (int i = 0; i < labelNumberOfHits.length; i++) {

					labelNumberOfHits[i].move(i * 6 + 30, 100);
					labelNumberOfHits[i].setVisible(true);
				}

				NumberLabel.updateImage(player.getAciertos(), disparosAcertadosLabel);
				for (int i = 0; i < disparosAcertadosLabel.length; i++) {
					disparosAcertadosLabel[i].move(i * 5 + 120, 100);
					disparosAcertadosLabel[i].setVisible(true);
				}

				for (int i = 0; i < labelHitMissRatio.length; i++) {

					labelHitMissRatio[i].move(i * 6 + 30, 120);
					labelHitMissRatio[i].setVisible(true);
				}
				if (player.getDisparos() != 0) {
					if ((int) ((player.getAciertos() * 1.0) / player.getDisparos()) * 100 < 99) {
						NumberLabel.updateImage((int) (((player.getAciertos() * 1.0) / player.getDisparos()) * 100),
								hitMissRatioLabel);
					} else
						NumberLabel.updateImage(100, hitMissRatioLabel);
				} else
					NumberLabel.updateImage(0, hitMissRatioLabel);

				for (int i = 0; i < hitMissRatioLabel.length; i++) {
					hitMissRatioLabel[i].move(i * 5 + 120, 120);
					hitMissRatioLabel[i].setVisible(true);
				}

				if (interfaz.getFotograma() % 30 == 0) {
					for (int i = 0; i < labelPressAnyKeyForNewGame.length; i++) {
						labelPressAnyKeyForNewGame[i].move(i * 6 + 10, 160);

						if (labelPressAnyKeyForNewGame[i].isVisible())
							labelPressAnyKeyForNewGame[i].setVisible(false);
						else
							labelPressAnyKeyForNewGame[i].setVisible(true);
					}

					for (int i = 0; i < labelsPressEscapeToExit.length; i++) {
						labelsPressEscapeToExit[i].move(i * 6 + 30, 180);

						if (labelsPressEscapeToExit[i].isVisible())
							labelsPressEscapeToExit[i].setVisible(false);
						else
							labelsPressEscapeToExit[i].setVisible(true);
					}
				}

				/*
				 * Si se pulsa cualquier tecla se reinicia el juego y se esconden los letraLabel
				 * y numberLabel de la ultima scena
				 */
				if (interfaz.getLastAction().equals("anyKey")) {
					for (int i = 0; i < labelResults.length; i++) {
						labelResults[i].setVisible(false);
					}
					for (int i = 0; i < labelShotsFired.length; i++) {
						labelShotsFired[i].setVisible(false);
					}
					for (int i = 0; i < disparosLabel.length; i++) {
						disparosLabel[i].setVisible(false);
					}
					for (int i = 0; i < labelNumberOfHits.length; i++) {
						labelNumberOfHits[i].setVisible(false);
					}
					for (int i = 0; i < disparosAcertadosLabel.length; i++) {
						disparosAcertadosLabel[i].setVisible(false);
					}
					for (int i = 0; i < labelHitMissRatio.length; i++) {
						labelHitMissRatio[i].setVisible(false);
					}
					for (int i = 0; i < hitMissRatioLabel.length; i++) {
						hitMissRatioLabel[i].setVisible(false);
					}
					for (int i = 0; i < labelPressAnyKeyForNewGame.length; i++) {
						labelPressAnyKeyForNewGame[i].setVisible(false);
					}
					for (int i = 0; i < labelsPressEscapeToExit.length; i++) {
						labelsPressEscapeToExit[i].setVisible(false);
					}

					startGame();

				}

				interfaz.setLastAction("");
				interfaz.addOneFotograma();
				Thread.sleep(1000 / 60);
				break;
			default:
				break;
			}
		}
		System.exit(0);
	}

	private static void startGame() throws InterruptedException {
		transicion.setVisible(false);

		stage.setImage("stage.png", 80, 80);
		stage.setVisible(true);
		stage.move(130, 5);

		for (int i = 0; i < stageNumberLabel.length; i++) {

			stageNumberLabel[i].move(i * 5 + 150, 5);
			stageNumberLabel[i].setVisible(true);
		}

		/*
		 * Determino los valores de enemyMatriz
		 */

		enemyControler.create(25, 25);
		enemyControler.setVisible(false);

		for (int i = 0; i < enemy.length; i++) {
			for (int j = 0; j < enemy[i].length; j++) {
				enemy[i][j].setVisible(false);
			}
		}
		/*
		 * Crea el jugador
		 */

		player.create(80, 200, 2, 0);
		player.setPuntos(0);
		player.setRecord(Integer.parseInt(TxtFile.leer(player, "record.txt").split("\n")[0]));
		player.setDisparos(0);
		player.setAciertos(0);
		player.setSaludAct(3);

		for (int i = 0; i < player.getBullet().length; i++) {
			player.getBullet()[i].setVisible(false);
		}

		for (int i = 0; i < enemyControler.getBullet().length; i++) {
			enemyControler.getBullet()[i].setVisible(false);
		}

		for (int i = 0; i < stars.length; i++)
			stars[i].create((int) (Math.random() * 170), (int) (Math.random() * 210));

		highScore.setImage("high score.png", 150, 150);
		highScore.move(85, 5);
		highScore.setVisible(true);
		for (int i = 0; i < highScoreLabel.length; i++) {
			highScoreLabel[i].move(i * 5 + 70, 13);
			highScoreLabel[i].setVisible(true);
		}
		NumberLabel.updateImage(player.getRecord(), highScoreLabel);
		Score.setImage("score.png", 80, 80);
		Score.move(30, 5);
		Score.setVisible(true);
		for (int i = 0; i < scoreLabel.length; i++) {

			scoreLabel[i].move(i * 5 + 15, 13);
			scoreLabel[i].setVisible(true);
		}

		NumberLabel.updateImage(player.getPuntos(), scoreLabel);
		for (int i = 0; i < labelNombre.length; i++) {

			labelNombre[i].move(i * 6 + 110, 13);
			labelNombre[i].setVisible(true);
		}

		/*
		 * Sprite del pause
		 */

		interfaz.setPauseSprite(transicion);
		interfaz.setPlayer(player);
		interfaz.setLevel(0);
		changeLevel();
		for (int i = 0; i < player.getBullet().length; i++) {
			player.getBullet()[i].setVisible(false);
		}
		player.getBulletSpecial().setVisible(false);
		for (int i = 0; i < enemyControler.getBullet().length; i++) {
			enemyControler.getBullet()[i].setVisible(false);

		}
		interfaz.setScene(2);
	}

	/**
	 * Se encarga de crear los objetos necesarios para el juego
	 */
	private static void createObjects() {
		interfaz = new Interfaz(17, 22);

		portada = new Sprite(interfaz);

		stars = new Sprite[200];

		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Sprite(interfaz.getUniqueNumeroId(), interfaz);
			stars[i].setImage("Matriz.png", 2);
		}

		labelPressAnyKey = new LetraLabel["press any key".length()];
		for (int i = 0; i < labelPressAnyKey.length; i++) {
			labelPressAnyKey[i] = new LetraLabel(32, 18, 20, interfaz);

		}

		/*
		 * Uso una matriz de enemigos para colocarlos donde toque, depende de la fila
		 * les hago de un tipo o de otro
		 */
		enemyControler = new Enemy(0, null, null, 0, 0, interfaz);

		enemyArea = new Enemy(100, null, enemyControler, 45, 20, interfaz);

		enemy = new Enemy[5][10];
		for (int i = 0; i < enemy.length; i++)
			for (int j = 0; j < enemy[i].length; j++) {
				if (i == 0)
					enemy[i][j] = new Enemy(1, enemyControler.getBullet(), enemyControler, j * 10, i * 10, interfaz);
				if (i == 1 || i == 2)
					enemy[i][j] = new Enemy(2, enemyControler.getBullet(), enemyControler, j * 10, i * 10, interfaz);
				if (i > 2)
					enemy[i][j] = new Enemy(3, enemyControler.getBullet(), enemyControler, j * 10, i * 10, interfaz);
			}

		player = new Player(enemy, interfaz);

		for (int i = 0; i < enemy.length; i++)
			for (int j = 0; j < enemy[i].length; j++) {
				enemy[i][j].setTarget(player);
			}

		highScoreLabel = new NumberLabel[7];
		for (int i = 0; i < highScoreLabel.length; i++)
			highScoreLabel[i] = new NumberLabel(i, 12, 20, interfaz);

		highScore = new NumberLabel(0, 100, 100, interfaz);

		scoreLabel = new NumberLabel[7];
		for (int i = 0; i < scoreLabel.length; i++)
			scoreLabel[i] = new NumberLabel(i, 12, 20, interfaz);
		Score = new NumberLabel(0, 100, 100, interfaz);
		labelNombre = new LetraLabel[10];
		for (int i = 0; i < labelNombre.length; i++) {
			labelNombre[i] = new LetraLabel(32, 18, 20, interfaz);

		}
//		LetraLabel.updateImage("dario", labelNombre);
		stage = new Sprite(0, interfaz);
		stageNumberLabel = new NumberLabel[4];
		for (int i = 0; i < stageNumberLabel.length; i++) {
			stageNumberLabel[i] = new NumberLabel(0, 12, 20, interfaz);
		}

		transicion = new Sprite(interfaz);
		transicion.setImage("pause.png", 1000);

		disparosLabel = new NumberLabel[7];
		for (int i = 0; i < disparosLabel.length; i++) {
			disparosLabel[i] = new NumberLabel(0, 12, 20, interfaz);
		}

		labelResults = new LetraLabel["-results-".length()];
		for (int i = 0; i < labelResults.length; i++) {
			labelResults[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("-results-", labelResults);

		labelShotsFired = new LetraLabel["shots fired".length()];
		for (int i = 0; i < labelShotsFired.length; i++) {
			labelShotsFired[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("shots fired", labelShotsFired);

		disparosAcertadosLabel = new NumberLabel[7];
		for (int i = 0; i < disparosAcertadosLabel.length; i++) {
			disparosAcertadosLabel[i] = new NumberLabel(0, 12, 20, interfaz);
		}

		labelNumberOfHits = new LetraLabel["number of hits".length()];
		for (int i = 0; i < labelNumberOfHits.length; i++) {
			labelNumberOfHits[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("number of hits", labelNumberOfHits);

		labelHitMissRatio = new LetraLabel["hit-miss ratio    %".length()];
		for (int i = 0; i < labelHitMissRatio.length; i++) {
			labelHitMissRatio[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("hit-miss ratio    %", labelHitMissRatio);

		hitMissRatioLabel = new NumberLabel[3];
		for (int i = 0; i < hitMissRatioLabel.length; i++) {
			hitMissRatioLabel[i] = new NumberLabel(0, 12, 20, interfaz);
		}

		labelPressAnyKeyForNewGame = new LetraLabel["press any key for new game".length()];
		for (int i = 0; i < labelPressAnyKeyForNewGame.length; i++) {
			labelPressAnyKeyForNewGame[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("press any key for new game", labelPressAnyKeyForNewGame);

		labelsPressEscapeToExit = new LetraLabel["press escape to exit".length()];
		for (int i = 0; i < labelsPressEscapeToExit.length; i++) {
			labelsPressEscapeToExit[i] = new LetraLabel(32, 18, 20, interfaz);
		}
		LetraLabel.updateImage("press escape to exit", labelsPressEscapeToExit);

		labelCreatedByDario = new LetraLabel["created by dario".length()];

		for (int i = 0; i < labelCreatedByDario.length; i++) {
			labelCreatedByDario[i] = new LetraLabel(32, 9, 10, interfaz);
			labelCreatedByDario[i].move(i * 3 + 125, 222);
			labelCreatedByDario[i].setVisible(true);
		}

		LetraLabel.updateImageCredits("created by dario", labelCreatedByDario);

	}

	/**
	 * Se encarga de hacer invisibles los sprites en el cambio de nivel
	 */
	private static void setAllInvisible() {
		for (int i = 0; i < player.getBullet().length; i++) {
			player.getBullet()[i].setVisible(false);
		}

		player.getBulletSpecial().setVisible(false);

		for (int i = 0; i < enemyControler.getBullet().length; i++) {
			enemyControler.getBullet()[i].setVisible(false);
		}

		for (int i = 0; i < player.getBonus().length; i++) {
			player.getBonus()[i].setVisible(false);
		}

		for (int i = 0; i < enemy.length; i++) {
			for (int j = 0; j < enemy[i].length; j++) {
				enemy[i][j].setVisible(false);
			}
		}
	}

	/**
	 * Se encarga de cambiar el nivel al siguiente
	 * 
	 * @throws InterruptedException
	 */
	public static void changeLevel() throws InterruptedException {
		enemyControler.setFaseMovimiento(0);
		interfaz.setPatron((int) (Math.random() * 3));
		interfaz.setLevel(interfaz.getLevel() + 1);
		setAllInvisible();
		boolean pausa = true;
		long prefotograma = interfaz.getFotograma();
		NumberLabel.updateImage(interfaz.getLevel(), Juego.stageNumberLabel);
		while (pausa) {
			stage.setVisible(true);
			stage.move(70, 110);
			for (int i = 0; i < stageNumberLabel.length; i++) {
				stageNumberLabel[i].move(i * 5 + 90, 110);
				stageNumberLabel[i].setVisible(true);
			}
			if (interfaz.getFotograma() - prefotograma > 40)
				pausa = false;

			interfaz.addOneFotograma();
			Thread.sleep(1000 / 60);
		}
		stage.move(130, 5);
		for (int i = 0; i < stageNumberLabel.length; i++) {
			stageNumberLabel[i].move(i * 5 + 150, 5);
			stageNumberLabel[i].setVisible(true);
		}

	}

}
