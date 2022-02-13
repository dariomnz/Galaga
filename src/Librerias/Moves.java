package Librerias;

import Game.*;

/**
 * Se encarga de los movimientos
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public abstract class Moves {

	private final static float[][] moves = { { (float) (0.0 / 4), (float) (-4.0 / 4) }, // DIR_N
			{ (float) (1.0 / 4), (float) (-4.0 / 4) }, // DIR_NNE
			{ (float) (3.0 / 4), (float) (-3.0 / 4) }, // DIR_NE
			{ (float) (4.0 / 4), (float) (-1.0 / 4) }, // DIR_ENE
			{ (float) (4.0 / 4), (float) (0.0 / 4) }, // DIR_E
			{ (float) (4.0 / 4), (float) (1.0 / 4) }, // DIR_ESE
			{ (float) (3.0 / 4), (float) (3.0 / 4) }, // DIR_SE
			{ (float) (1.0 / 4), (float) (4.0 / 4) }, // DIR_SSE
			{ (float) (0.0 / 4), (float) (4.0 / 4) }, // DIR_S
			{ (float) (-1.0 / 4), (float) (4.0 / 4) }, // DIR_SSW
			{ (float) (-3.0 / 4), (float) (3.0 / 4) }, // DIR_SW
			{ (float) (-4.0 / 4), (float) (1.0 / 4) }, // DIR_WSW
			{ (float) (-4.0 / 4), (float) (0.0 / 4) }, // DIR_W
			{ (float) (-4.0 / 4), (float) (-1.0 / 4) }, // DIR_WNW
			{ (float) (-3.0 / 4), (float) (-3.0 / 4) }, // DIR_NW
			{ (float) (-1.0 / 4), (float) (-4.0 / 4) }, // DIR_NNW
	};

	/**
	 * Mueve el sprite en la direcion y con una longitud determinada
	 * 
	 * @param direction en la que se mueve
	 * @param velocidad que se mueve
	 */
	public static void moveDirection(Enemy enemigo, int direction, float velocidad) {
		enemigo.setX(enemigo.getX() + moves[direction][0] * velocidad);
		enemigo.setY(enemigo.getY() + moves[direction][1] * velocidad);
		enemigo.updatePosition();
		changeImage(enemigo, direction);
	}

	/**
	 * Hace un circulo a la derecha
	 * 
	 * @param enemigo que mueve
	 * @param radio
	 */
	public static void moveCirculoRight(Enemy enemigo, int radio, float velocidad) {

		moveDirection(enemigo, enemigo.getDireccion(), velocidad);
		enemigo.setMovimientoLadoCirculo(enemigo.getMovimientoLadoCirculo() + velocidad);

		if (enemigo.getMovimientoLadoCirculo() > (2 * radio * Math.sin(Math.PI / 16))) {
			enemigo.setMovimientoLadoCirculo(0);
			enemigo.setCambiarDireccion(true);
		}

		if (enemigo.isCambiarDireccion()) {
			enemigo.addOneDireccion();
			enemigo.setCambiarDireccion(false);
		}

		if (enemigo.getDireccion() == 16)
			enemigo.setDireccion(0);

	}

	/**
	 * Hace un circulo a la izquierda
	 * 
	 * @param enemigo que mueve
	 * @param radio
	 */
	public static void moveCirculoLeft(Enemy enemigo, int radio, float f) {

		moveDirection(enemigo, enemigo.getDireccion(), f);
		enemigo.setMovimientoLadoCirculo(enemigo.getMovimientoLadoCirculo() + f);
		if (enemigo.getMovimientoLadoCirculo() > (2 * radio * Math.sin(Math.PI / 16))) {
			enemigo.setMovimientoLadoCirculo(0);
			enemigo.setCambiarDireccion(true);
		}

		if (enemigo.isCambiarDireccion()) {
			enemigo.setDireccion(enemigo.getDireccion() - 1);
			enemigo.setCambiarDireccion(false);
		}

		if (enemigo.getDireccion() == -1)
			enemigo.setDireccion(15);
	}

	/**
	 * Mueve el enemigo hacia una posicion cambiando su imagen dependiendo de la
	 * direccion que tome
	 * 
	 * @param enemigo   que mueve
	 * @param x         del punto al que quieres llegar
	 * @param y         del punto al que quieres llegar
	 * @param velocidad a la que se mueve por frame
	 */
	public static void moveToPosition(Enemy enemigo, int x, int y, float velocidad) {

		float[] vectorDirector = { x - enemigo.getX(), y - enemigo.getY() };
		float longitudVector = (float) Math
				.sqrt(vectorDirector[0] * vectorDirector[0] + vectorDirector[1] * vectorDirector[1]);
		float[] vectorUnitario = { (vectorDirector[0] / longitudVector) * velocidad,
				(vectorDirector[1] / longitudVector) * velocidad };

		enemigo.setX(enemigo.getX() + vectorUnitario[0]);
		enemigo.setY(enemigo.getY() + vectorUnitario[1]);
		enemigo.updatePosition();

		changeImage(enemigo, vectorUnitario);

		if ((enemigo.getX() > x - 1 && enemigo.getX() < x + 1) && (enemigo.getY() > y - 1 && enemigo.getY() < y + 1)) {
			enemigo.setMovingToPosition(false);
		}
	}

	/**
	 * Cambia la imagen de el enemigo respecto a la direccion
	 * 
	 * @param enemigo   al que le cambia la imagen
	 * @param direccion direccion de la imagen
	 */
	private static void changeImage(Enemy enemigo, int direccion) {
		enemigo.setImage(Animacion.moveEnemy[enemigo.getNumTipo()][direccion]);
	}

	/**
	 * Cambia la imagen del enemigo respecto a un vector director
	 * 
	 * @param enemigo         al que le cambia la imagen
	 * @param vectorDireccion que usa para saber la direccion
	 */
	private static void changeImage(Enemy enemigo, float[] vectorDireccion) {

		enemigo.setDireccion(calculateDirection(calculoAngulo(vectorDireccion[0], vectorDireccion[1])));
		enemigo.setImage(Animacion.moveEnemy[enemigo.getNumTipo()][enemigo.getDireccion()]);

	}

	private static int calculateDirection(double angulo) {
		byte direccion = 0;
		if (angulo > 355 && angulo <= 5)
			direccion = 4;
		else if (angulo > 5 && angulo <= 30)
			direccion = 3;
		else if (angulo > 30 && angulo <= 60)
			direccion = 2;
		else if (angulo > 60 && angulo <= 85)
			direccion = 1;
		else if (angulo > 85 && angulo <= 95)
			direccion = 0;
		else if (angulo > 95 && angulo <= 120)
			direccion = 15;
		else if (angulo > 120 && angulo <= 150)
			direccion = 14;
		else if (angulo > 150 && angulo <= 175)
			direccion = 13;
		else if (angulo > 175 && angulo <= 185)
			direccion = 12;
		else if (angulo > 185 && angulo <= 210)
			direccion = 11;
		else if (angulo > 210 && angulo <= 240)
			direccion = 10;
		else if (angulo > 240 && angulo <= 265)
			direccion = 9;
		else if (angulo > 265 && angulo <= 275)
			direccion = 8;
		else if (angulo > 275 && angulo <= 300)
			direccion = 7;
		else if (angulo > 300 && angulo <= 330)
			direccion = 6;
		else if (angulo > 330 && angulo <= 355)
			direccion = 5;

		return direccion;
	}

	/**
	 * Calcula el angulo de un vector de forma (x,y) respecto un vector (1,0)
	 * 
	 * @param x del vector
	 * @param y del vector
	 * @return angulo en grados
	 */
	private static float calculoAngulo(float x, float y) {
		float angulo = (float) Math.toDegrees(Math.atan2(x, y));
		if (x > 0 && y > 0)
			angulo += 270;
		else if (x > 0 && y < 0)
			angulo -= 90;
		else if (x < 0 && y > 0)
			angulo += 270;
		else if (x < 0 && y < 0)
			angulo += 270;

		return angulo;
	}

	private static void backToPosition(Enemy enemigo, Enemy controler) {
		moveToPosition(enemigo, (int) (enemigo.getXmatrix() + controler.getX()),
				(int) (enemigo.getYmatrix() + controler.getY()), 2);

		if ((enemigo.getY() > (enemigo.getYmatrix() + controler.getY()) - 1
				&& enemigo.getY() < (enemigo.getYmatrix() + controler.getY()) + 1)) {
			enemigo.addOneFaseMovimiento();
			enemigo.setNormalMove(true);
			enemigo.setFinishAnimation(true);
			enemigo.setLauchPatron(0);
			enemigo.setDireccion(0);
			enemigo.setMovimientoLadoCirculo(0);

		}

	}

	/**
	 * Patron de entrada de los enemigos 1
	 * 
	 * @param controler el controlador de la matriz
	 * @param enemigo   matriz de enemigos
	 */
	public static void Patron1(Enemy controler, Enemy[][] enemigo) {
		if (controler.getFaseMovimiento() == 0) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					enemigo[i][j].setFaseMovimiento(0);
					enemigo[i][j].setNormalMove(false);
					enemigo[i][j].setFinishAnimation(false);
					enemigo[i][j].setMovimientoLadoCirculo(0);
					enemigo[i][j].setExplotando(false);
				}
			enemigo[1][4].create(70, -10);
			enemigo[1][5].create(70, -30);
			enemigo[2][4].create(70, -50);
			enemigo[2][5].create(70, -70);

			enemigo[3][4].create(100, -10);
			enemigo[3][5].create(100, -30);
			enemigo[4][4].create(100, -50);
			enemigo[4][5].create(100, -70);
			controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 1) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 20)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveToPosition(enemigo[i][j], 140, 120, 2);
										enemigo[i][j].setDireccion(6);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveToPosition(enemigo[i][j], 30, 120, 2);
										enemigo[i][j].setDireccion(10);
									}
									if (enemigo[i][j].getY() > 119) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
									}

									break;
								case 2:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveCirculoRight(enemigo[i][j], 20, 2);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveCirculoLeft(enemigo[i][j], 20, 2);
									}
									if (enemigo[i][j].getY() < 140 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:
									backToPosition(enemigo[i][j], controler);

									break;

								default:
									break;
								}
				}
			if (enemigo[1][4].isFinishAnimation() && enemigo[1][5].isFinishAnimation()
					&& enemigo[2][4].isFinishAnimation() && enemigo[2][5].isFinishAnimation()
					&& enemigo[3][4].isFinishAnimation() && enemigo[3][5].isFinishAnimation()
					&& enemigo[4][4].isFinishAnimation() && enemigo[4][5].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 2) {
			enemigo[0][3].create(-10, 200);
			enemigo[1][3].create(-30, 200);
			enemigo[0][4].create(-50, 200);
			enemigo[1][6].create(-70, 200);
			enemigo[0][5].create(-90, 200);
			enemigo[2][3].create(-110, 200);
			enemigo[0][6].create(-130, 200);
			enemigo[2][6].create(-150, 200);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 3) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 4, 2);
									if (enemigo[i][j].getX() > 9)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									moveToPosition(enemigo[i][j], 70, 140, 2);
									if (enemigo[i][j].getX() > 69) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
										enemigo[i][j].setDireccion(1);
									}

									break;
								case 2:

									moveCirculoLeft(enemigo[i][j], 25, 2);

									if (enemigo[i][j].getX() > 69 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;

								}
				}
			if (enemigo[1][3].isFinishAnimation() && enemigo[0][3].isFinishAnimation()
					&& enemigo[1][6].isFinishAnimation() && enemigo[0][4].isFinishAnimation()
					&& enemigo[2][3].isFinishAnimation() && enemigo[0][5].isFinishAnimation()
					&& enemigo[2][6].isFinishAnimation() && enemigo[0][6].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 4) {
			enemigo[1][1].create(180, 200);
			enemigo[1][2].create(200, 200);
			enemigo[1][7].create(220, 200);
			enemigo[1][8].create(240, 200);
			enemigo[2][1].create(260, 200);
			enemigo[2][2].create(280, 200);
			enemigo[2][7].create(300, 200);
			enemigo[2][8].create(320, 200);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 5) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 12, 2);
									if (enemigo[i][j].getX() < 161)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									moveToPosition(enemigo[i][j], 100, 140, 2);
									if (enemigo[i][j].getX() < 101) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
										enemigo[i][j].setDireccion(15);
									}

									break;
								case 2:

									moveCirculoRight(enemigo[i][j], 25, 2);

									if (enemigo[i][j].getX() < 101 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}
				}
			if (enemigo[1][1].isFinishAnimation() && enemigo[2][1].isFinishAnimation()
					&& enemigo[1][2].isFinishAnimation() && enemigo[2][2].isFinishAnimation()
					&& enemigo[1][7].isFinishAnimation() && enemigo[2][7].isFinishAnimation()
					&& enemigo[1][8].isFinishAnimation() && enemigo[2][8].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 6) {
			enemigo[3][2].create(80, -10);
			enemigo[3][3].create(80, -30);
			enemigo[3][6].create(80, -50);
			enemigo[3][7].create(80, -70);

			enemigo[4][2].create(80, -90);
			enemigo[4][3].create(80, -110);
			enemigo[4][6].create(80, -130);
			enemigo[4][7].create(80, -150);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 7) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 10)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									moveToPosition(enemigo[i][j], 60, 140, 2);
									if (enemigo[i][j].getX() < 61) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
										enemigo[i][j].setDireccion(9);
									}

									break;
								case 2:

									moveCirculoRight(enemigo[i][j], 20, 2);

									if (enemigo[i][j].getX() > 30 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}
				}
			if (enemigo[3][2].isFinishAnimation() && enemigo[4][2].isFinishAnimation()
					&& enemigo[3][3].isFinishAnimation() && enemigo[4][3].isFinishAnimation()
					&& enemigo[3][6].isFinishAnimation() && enemigo[4][6].isFinishAnimation()
					&& enemigo[3][7].isFinishAnimation() && enemigo[4][7].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 8) {
			enemigo[3][0].create(80, -10);
			enemigo[3][1].create(80, -30);
			enemigo[3][8].create(80, -50);
			enemigo[3][9].create(80, -70);

			enemigo[4][0].create(80, -90);
			enemigo[4][1].create(80, -110);
			enemigo[4][8].create(80, -130);
			enemigo[4][9].create(80, -150);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 9) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 10)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									moveToPosition(enemigo[i][j], 100, 140, 2);
									if (enemigo[i][j].getX() > 99) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
										enemigo[i][j].setDireccion(7);
									}

									break;
								case 2:

									moveCirculoLeft(enemigo[i][j], 20, 2);

									if (enemigo[i][j].getX() < 130 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}
				}
			if (enemigo[3][0].isFinishAnimation() && enemigo[4][0].isFinishAnimation()
					&& enemigo[3][1].isFinishAnimation() && enemigo[4][1].isFinishAnimation()
					&& enemigo[3][8].isFinishAnimation() && enemigo[4][8].isFinishAnimation()
					&& enemigo[3][9].isFinishAnimation() && enemigo[4][9].isFinishAnimation())
				controler.addOneFaseMovimiento();

		}
		lauch(enemigo, controler);

	}

	/**
	 * Patron de entrada de los enemigos 2
	 * 
	 * @param controler el controlador de la matriz
	 * @param enemigo   matriz de enemigos
	 */
	public static void Patron2(Enemy controler, Enemy[][] enemigo) {
		if (controler.getFaseMovimiento() == 0) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					enemigo[i][j].setFaseMovimiento(0);
					enemigo[i][j].setNormalMove(false);
					enemigo[i][j].setFinishAnimation(false);
					enemigo[i][j].setMovimientoLadoCirculo(0);
					enemigo[i][j].setExplotando(false);
				}
			enemigo[1][4].create(70, -10);
			enemigo[1][5].create(70, -30);
			enemigo[2][4].create(70, -50);
			enemigo[2][5].create(70, -70);

			enemigo[3][4].create(100, -10);
			enemigo[3][5].create(100, -30);
			enemigo[4][4].create(100, -50);
			enemigo[4][5].create(100, -70);
			controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 1) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 20)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveToPosition(enemigo[i][j], 140, 120, 2);
										enemigo[i][j].setDireccion(6);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveToPosition(enemigo[i][j], 30, 120, 2);
										enemigo[i][j].setDireccion(10);
									}
									if (enemigo[i][j].getY() > 119) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
									}

									break;
								case 2:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveCirculoRight(enemigo[i][j], 15, 2);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveCirculoLeft(enemigo[i][j], 15, 2);
									}
									if (enemigo[i][j].getY() < 140 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;

								}
				}
			if (enemigo[1][4].isFinishAnimation() && enemigo[1][5].isFinishAnimation()
					&& enemigo[2][4].isFinishAnimation() && enemigo[2][5].isFinishAnimation()
					&& enemigo[3][4].isFinishAnimation() && enemigo[3][5].isFinishAnimation()
					&& enemigo[4][4].isFinishAnimation() && enemigo[4][5].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 2) {
			enemigo[0][3].create(-10, 200);
			enemigo[0][4].create(-30, 200);
			enemigo[0][5].create(-50, 200);
			enemigo[0][6].create(-70, 200);

			enemigo[1][3].create(-10, 190);
			enemigo[1][6].create(-30, 190);
			enemigo[2][3].create(-50, 190);
			enemigo[2][6].create(-70, 190);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 3) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 4, 2);
									if (enemigo[i][j].getX() > 9)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (i == 0) {
										moveToPosition(enemigo[i][j], 70, 140, 2);
										if (enemigo[i][j].getX() > 69) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(1);
										}
									} else {
										moveToPosition(enemigo[i][j], 63, 135, 2);
										if (enemigo[i][j].getX() > 62) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(1);
										}

									}
									break;
								case 2:
									if (i == 0) {
										moveCirculoLeft(enemigo[i][j], 25, 3);
										if (enemigo[i][j].getX() > 69 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoLeft(enemigo[i][j], 20, 2);
										if (enemigo[i][j].getX() > 62 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;

								}
				}
			if (enemigo[1][3].isFinishAnimation() && enemigo[0][3].isFinishAnimation()
					&& enemigo[1][6].isFinishAnimation() && enemigo[0][4].isFinishAnimation()
					&& enemigo[2][3].isFinishAnimation() && enemigo[0][5].isFinishAnimation()
					&& enemigo[2][6].isFinishAnimation() && enemigo[0][6].isFinishAnimation())
				controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 4) {

			enemigo[1][1].create(180, 200);
			enemigo[1][2].create(200, 200);
			enemigo[1][7].create(220, 200);
			enemigo[1][8].create(240, 200);
			enemigo[2][1].create(180, 190);
			enemigo[2][2].create(200, 190);
			enemigo[2][7].create(220, 190);
			enemigo[2][8].create(240, 190);
			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 5) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 12, 2);
									if (enemigo[i][j].getX() < 161)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:

									if (i == 1) {
										moveToPosition(enemigo[i][j], 100, 140, 2);
										if (enemigo[i][j].getX() < 101) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(15);
										}
									} else {
										moveToPosition(enemigo[i][j], 107, 135, 2);
										if (enemigo[i][j].getX() < 108) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(15);
										}

									}
									break;
								case 2:
									if (i == 1) {
										moveCirculoRight(enemigo[i][j], 25, 3);
										if (enemigo[i][j].getX() < 101 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoRight(enemigo[i][j], 20, 2);
										if (enemigo[i][j].getX() < 108 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[1][1].isFinishAnimation() && enemigo[2][1].isFinishAnimation()
					&& enemigo[1][2].isFinishAnimation() && enemigo[2][2].isFinishAnimation()
					&& enemigo[1][7].isFinishAnimation() && enemigo[2][7].isFinishAnimation()
					&& enemigo[1][8].isFinishAnimation() && enemigo[2][8].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 6) {
			enemigo[3][2].create(80, -10);
			enemigo[3][3].create(80, -30);
			enemigo[3][6].create(80, -50);
			enemigo[3][7].create(80, -70);

			enemigo[4][2].create(90, -10);
			enemigo[4][3].create(90, -30);
			enemigo[4][6].create(90, -50);
			enemigo[4][7].create(90, -70);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 7) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 10)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (i == 3) {
										moveToPosition(enemigo[i][j], 60, 140, 2);
										if (enemigo[i][j].getX() < 61) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(9);
										}
									} else {
										moveToPosition(enemigo[i][j], 70, 140, 2);
										if (enemigo[i][j].getX() < 71) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(9);
										}
									}

									break;
								case 2:
									if (i == 3) {
										moveCirculoRight(enemigo[i][j], 15, 2);

										if (enemigo[i][j].getX() < 61 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoRight(enemigo[i][j], 20, 3);

										if (enemigo[i][j].getX() < 71 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[3][2].isFinishAnimation() && enemigo[4][2].isFinishAnimation()
					&& enemigo[3][3].isFinishAnimation() && enemigo[4][3].isFinishAnimation()
					&& enemigo[3][6].isFinishAnimation() && enemigo[4][6].isFinishAnimation()
					&& enemigo[3][7].isFinishAnimation() && enemigo[4][7].isFinishAnimation())
				controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 8) {
			enemigo[3][0].create(80, -10);
			enemigo[3][1].create(80, -30);
			enemigo[3][8].create(80, -50);
			enemigo[3][9].create(80, -70);

			enemigo[4][0].create(90, -10);
			enemigo[4][1].create(90, -30);
			enemigo[4][8].create(90, -50);
			enemigo[4][9].create(90, -70);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 9) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 10)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (i == 3) {
										moveToPosition(enemigo[i][j], 100, 140, 2);
										if (enemigo[i][j].getX() > 99) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(7);
										}
									} else {
										moveToPosition(enemigo[i][j], 110, 140, 2);
										if (enemigo[i][j].getX() > 109) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(7);
										}
									}

									break;
								case 2:
									if (i == 3) {
										moveCirculoLeft(enemigo[i][j], 20, 3);

										if (enemigo[i][j].getX() > 99 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoLeft(enemigo[i][j], 15, 2);

										if (enemigo[i][j].getX() > 109 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[3][0].isFinishAnimation() && enemigo[4][0].isFinishAnimation()
					&& enemigo[3][1].isFinishAnimation() && enemigo[4][1].isFinishAnimation()
					&& enemigo[3][8].isFinishAnimation() && enemigo[4][8].isFinishAnimation()
					&& enemigo[3][9].isFinishAnimation() && enemigo[4][9].isFinishAnimation())
				controler.addOneFaseMovimiento();

		}
		lauch(enemigo, controler);
	}

	/**
	 * Patron de entrada de los enemigos 3
	 * 
	 * @param controler el controlador de la matriz
	 * @param enemigo   matriz de enemigos
	 */
	public static void Patron3(Enemy controler, Enemy[][] enemigo) {
		if (controler.getFaseMovimiento() == 0) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					enemigo[i][j].setFaseMovimiento(0);
					enemigo[i][j].setNormalMove(false);
					enemigo[i][j].setFinishAnimation(false);
					enemigo[i][j].setMovimientoLadoCirculo(0);
					enemigo[i][j].setExplotando(false);
				}
			enemigo[1][4].create(70, -10);
			enemigo[1][5].create(70, -30);
			enemigo[2][4].create(70, -50);
			enemigo[2][5].create(70, -70);

			enemigo[3][4].create(100, -10);
			enemigo[3][5].create(100, -30);
			enemigo[4][4].create(100, -50);
			enemigo[4][5].create(100, -70);
			controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 1) {
			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 20)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveToPosition(enemigo[i][j], 140, 120, 2);
										enemigo[i][j].setDireccion(6);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveToPosition(enemigo[i][j], 30, 120, 2);
										enemigo[i][j].setDireccion(10);
									}
									if (enemigo[i][j].getY() > 119) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
									}

									break;
								case 2:
									if (enemigo[i][j].getNumTipo() == 2) {
										moveCirculoRight(enemigo[i][j], 15, 2);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveCirculoLeft(enemigo[i][j], 15, 2);
									}
									if (enemigo[i][j].getY() < 140 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[1][4].isFinishAnimation() && enemigo[1][5].isFinishAnimation()
					&& enemigo[2][4].isFinishAnimation() && enemigo[2][5].isFinishAnimation()
					&& enemigo[3][4].isFinishAnimation() && enemigo[3][5].isFinishAnimation()
					&& enemigo[4][4].isFinishAnimation() && enemigo[4][5].isFinishAnimation())
				controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 2) {
			enemigo[0][3].create(-10, 200);
			enemigo[0][4].create(-30, 200);
			enemigo[0][5].create(-50, 200);
			enemigo[0][6].create(-70, 200);

			enemigo[1][3].create(180, 200);
			enemigo[1][6].create(200, 200);
			enemigo[2][3].create(220, 200);
			enemigo[2][6].create(240, 200);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 3) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									if (i == 0) {
										moveDirection(enemigo[i][j], 4, 2);
										if (enemigo[i][j].getX() > 9)
											enemigo[i][j].addOneFaseMovimiento();
									} else {
										moveDirection(enemigo[i][j], 12, 2);
										if (enemigo[i][j].getX() < 161)
											enemigo[i][j].addOneFaseMovimiento();
									}
									break;

								case 1:
									if (i == 0) {
										moveToPosition(enemigo[i][j], 70, 140, 2);
										if (enemigo[i][j].getX() > 69) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(1);
										}
									} else {
										moveToPosition(enemigo[i][j], 100, 140, 2);
										if (enemigo[i][j].getX() < 101) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(15);
										}

									}
									break;
								case 2:
									if (i == 0) {
										moveCirculoLeft(enemigo[i][j], 18, 2);
										if (enemigo[i][j].getX() > 69 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoRight(enemigo[i][j], 18, 2);
										if (enemigo[i][j].getX() < 101 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[1][3].isFinishAnimation() && enemigo[0][3].isFinishAnimation()
					&& enemigo[1][6].isFinishAnimation() && enemigo[0][4].isFinishAnimation()
					&& enemigo[2][3].isFinishAnimation() && enemigo[0][5].isFinishAnimation()
					&& enemigo[2][6].isFinishAnimation() && enemigo[0][6].isFinishAnimation())
				controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 4) {

			enemigo[1][1].create(-10, 200);
			enemigo[1][2].create(-30, 200);
			enemigo[1][7].create(-50, 200);
			enemigo[1][8].create(-70, 200);
			enemigo[2][1].create(180, 200);
			enemigo[2][2].create(200, 200);
			enemigo[2][7].create(220, 200);
			enemigo[2][8].create(240, 200);
			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 5) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									if (i == 1) {
										moveDirection(enemigo[i][j], 4, 2);
										if (enemigo[i][j].getX() > 9)
											enemigo[i][j].addOneFaseMovimiento();
									} else {
										moveDirection(enemigo[i][j], 12, 2);
										if (enemigo[i][j].getX() < 161)
											enemigo[i][j].addOneFaseMovimiento();
									}
									break;

								case 1:
									if (i == 1) {
										moveToPosition(enemigo[i][j], 70, 140, 2);
										if (enemigo[i][j].getX() > 69) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(1);
										}
									} else {
										moveToPosition(enemigo[i][j], 100, 140, 2);
										if (enemigo[i][j].getX() < 101) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
											enemigo[i][j].setDireccion(15);
										}

									}
									break;
								case 2:
									if (i == 1) {
										moveCirculoLeft(enemigo[i][j], 18, 2);
										if (enemigo[i][j].getX() > 69 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									} else {
										moveCirculoRight(enemigo[i][j], 18, 2);
										if (enemigo[i][j].getX() < 101 && ((enemigo[i][j].getInterfaz().getFotograma()
												- enemigo[i][j].getPrefotograma()) > 30)) {
											enemigo[i][j].addOneFaseMovimiento();
											enemigo[i][j].setPrefotograma(0);
										}
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[1][1].isFinishAnimation() && enemigo[2][1].isFinishAnimation()
					&& enemigo[1][2].isFinishAnimation() && enemigo[2][2].isFinishAnimation()
					&& enemigo[1][7].isFinishAnimation() && enemigo[2][7].isFinishAnimation()
					&& enemigo[1][8].isFinishAnimation() && enemigo[2][8].isFinishAnimation())
				controler.addOneFaseMovimiento();

		} else if (controler.getFaseMovimiento() == 6) {
			enemigo[3][2].create(70, -10);
			enemigo[3][3].create(70, -30);
			enemigo[3][6].create(70, -50);
			enemigo[3][7].create(70, -70);

			enemigo[4][2].create(100, -10);
			enemigo[4][3].create(100, -30);
			enemigo[4][6].create(100, -50);
			enemigo[4][7].create(100, -70);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 7) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 20)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (i == 3) {
										moveToPosition(enemigo[i][j], 140, 140, 2);
										enemigo[i][j].setDireccion(6);
									} else {
										moveToPosition(enemigo[i][j], 30, 140, 2);
										enemigo[i][j].setDireccion(10);
									}
									if (enemigo[i][j].getY() > 99) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
									}

									break;

								case 2:
									if (i == 3) {
										moveCirculoRight(enemigo[i][j], 15, 2);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveCirculoLeft(enemigo[i][j], 15, 2);
									}
									if (enemigo[i][j].getY() < 120 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;

								}
				}
			if (enemigo[3][2].isFinishAnimation() && enemigo[4][2].isFinishAnimation()
					&& enemigo[3][3].isFinishAnimation() && enemigo[4][3].isFinishAnimation()
					&& enemigo[3][6].isFinishAnimation() && enemigo[4][6].isFinishAnimation()
					&& enemigo[3][7].isFinishAnimation() && enemigo[4][7].isFinishAnimation())
				controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 8) {
			enemigo[3][0].create(70, -10);
			enemigo[3][1].create(70, -30);
			enemigo[3][8].create(70, -50);
			enemigo[3][9].create(70, -70);

			enemigo[4][0].create(100, -10);
			enemigo[4][1].create(100, -30);
			enemigo[4][8].create(100, -50);
			enemigo[4][9].create(100, -70);

			controler.addOneFaseMovimiento();
		} else if (controler.getFaseMovimiento() == 9) {

			for (int i = 0; i < enemigo.length; i++)
				for (int j = 0; j < enemigo[i].length; j++) {
					if (!enemigo[i][j].isNormalMove())
						if (!enemigo[i][j].isExplotando())
							if (enemigo[i][j].isVisible())
								switch (enemigo[i][j].getFaseMovimiento()) {
								case 0:
									moveDirection(enemigo[i][j], 8, 2);
									if (enemigo[i][j].getY() > 20)
										enemigo[i][j].addOneFaseMovimiento();
									break;

								case 1:
									if (i == 3) {
										moveToPosition(enemigo[i][j], 140, 140, 2);
										enemigo[i][j].setDireccion(6);
									} else {
										moveToPosition(enemigo[i][j], 30, 140, 2);
										enemigo[i][j].setDireccion(10);
									}
									if (enemigo[i][j].getY() > 99) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(enemigo[i][j].getInterfaz().getFotograma());
									}

									break;

								case 2:
									if (i == 3) {
										moveCirculoRight(enemigo[i][j], 15, 2);
									} else if (enemigo[i][j].getNumTipo() == 3) {
										moveCirculoLeft(enemigo[i][j], 15, 2);
									}
									if (enemigo[i][j].getY() < 120 && ((enemigo[i][j].getInterfaz().getFotograma()
											- enemigo[i][j].getPrefotograma()) > 30)) {
										enemigo[i][j].addOneFaseMovimiento();
										enemigo[i][j].setPrefotograma(0);
									}

									break;
								case 3:

									backToPosition(enemigo[i][j], controler);
									break;

								default:
									break;
								}

				}
			if (enemigo[3][0].isFinishAnimation() && enemigo[4][0].isFinishAnimation()
					&& enemigo[3][1].isFinishAnimation() && enemigo[4][1].isFinishAnimation()
					&& enemigo[3][8].isFinishAnimation() && enemigo[4][8].isFinishAnimation()
					&& enemigo[3][9].isFinishAnimation() && enemigo[4][9].isFinishAnimation())
				controler.addOneFaseMovimiento();

		}
		lauch(enemigo, controler);
	}

	private static void lauch(Enemy[][] enemigo, Enemy controler) {
		for (int i = 0; i < enemigo.length; i++) {
			for (int j = 0; j < enemigo[i].length; j++) {
				if (enemigo[i][j].isVisible())
					if (!enemigo[i][j].isExplotando()) {
						if (enemigo[i][j].isNormalMove()) {
							if ((int) (Math.random() * 1500) == 1) {
								enemigo[i][j].setFaseMovimiento(10);
								Sound.playSound(controler.getInterfaz().getSoundDrive());
								switch (enemigo[i][j].getNumTipo()) {
								case 1:
								case 4:
									enemigo[i][j].setLauchPatron(1);
									enemigo[i][j].setNormalMove(false);
									switch (enemigo[i][j].getXmatrix()) {
									case 30:
										for (int k = 1; k < 3; k++)
											for (int t = 1; t < 3; t++)
												if (enemigo[k][t].isNormalMove()) {
													enemigo[k][t].setLauchPatron(10);
													enemigo[k][t].setNormalMove(false);
													enemigo[k][t].setFaseMovimiento(10);
													enemigo[k][t].setLauchMaster(enemigo[i][j]);
												}
										break;
									case 40:
										for (int k = 1; k < 3; k++)
											for (int t = 3; t < 5; t++)
												if (enemigo[k][t].isNormalMove()) {
													enemigo[k][t].setLauchPatron(10);
													enemigo[k][t].setNormalMove(false);
													enemigo[k][t].setFaseMovimiento(10);
													enemigo[k][t].setLauchMaster(enemigo[i][j]);
												}
										break;
									case 50:
										for (int k = 1; k < 3; k++)
											for (int t = 5; t < 7; t++)
												if (enemigo[k][t].isNormalMove()) {
													enemigo[k][t].setLauchPatron(10);
													enemigo[k][t].setNormalMove(false);
													enemigo[k][t].setFaseMovimiento(10);
													enemigo[k][t].setLauchMaster(enemigo[i][j]);
												}
										break;
									case 60:
										for (int k = 1; k < 3; k++)
											for (int t = 7; t < 9; t++)
												if (enemigo[k][t].isNormalMove()) {
													enemigo[k][t].setLauchPatron(10);
													enemigo[k][t].setNormalMove(false);
													enemigo[k][t].setFaseMovimiento(10);
													enemigo[k][t].setLauchMaster(enemigo[i][j]);
												}
										break;
									}
									break;
								case 2:
									enemigo[i][j].setLauchPatron(2);
									enemigo[i][j].setNormalMove(false);
									break;
								case 3:
									enemigo[i][j].setLauchPatron((int) (Math.random() * 2 + 3));
									enemigo[i][j].setNormalMove(false);
									break;
								default:
									break;
								}

							}
						} else {
							switch (enemigo[i][j].getLauchPatron()) {
							case 1:
								lauchPatron1_1(enemigo[i][j], controler);
								break;
							case 10:
								lauchPatron1_2(enemigo[i][j], controler);
								break;
							case 2:
								lauchPatron2_1(enemigo[i][j], controler);
								break;
							case 3:
								lauchPatron3_1(enemigo[i][j], controler);
								break;
							case 4:
								lauchPatron3_2(enemigo[i][j], controler);
								break;
							default:
								break;
							}
						}
					}
			}
		}
	}

	/**
	 * Movimiento de los comandantes
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron1_1(Enemy enemigo, Enemy controler) {

		switch (enemigo.getFaseMovimiento()) {
		case 10:
			enemigo.setFaseMovimiento((int) (Math.random() * 2 + 11));
			enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());

			enemigo.setDireccion(8);

			break;
		case 11:

			moveCirculoLeft(enemigo, 30, 1.2f);

			if (enemigo.getDireccion() == 5) {
				enemigo.setFaseMovimiento(12);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());

			}

			if (enemigo.getY() > 230) {
				enemigo.setY(-10);
				enemigo.setFaseMovimiento(13);
				;
			}
			break;
		case 12:

			moveCirculoRight(enemigo, 30, 1.2f);

			if (enemigo.getDireccion() == 11) {
				enemigo.setFaseMovimiento(11);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			if (enemigo.getY() > 230) {
				enemigo.setY(-10);
				enemigo.setFaseMovimiento(13);
				;
			}
			break;
		case 13:
			backToPosition(enemigo, controler);
			break;

		default:
			break;
		}

	}

	/**
	 * Movimiento de la escolta del comandante Se encarga de seguir al comandante
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron1_2(Enemy enemigo, Enemy controler) {
		switch (enemigo.getFaseMovimiento()) {
		case 10:

			enemigo.setNormalMove(false);

			enemigo.addOneFaseMovimiento();
			break;
		case 11:

			moveToPosition(enemigo, enemigo.getLauchX(), enemigo.getLauchY(), 2);
			if (enemigo.getX() > enemigo.getLauchX() - 1 && enemigo.getX() < enemigo.getLauchX() + 1
					&& enemigo.getY() > enemigo.getLauchY() - 1 && enemigo.getY() < enemigo.getLauchY() + 1)
				enemigo.addOneFaseMovimiento();

			break;
		case 12:
			if (enemigo.getLauchMaster().isVisible()) {
				enemigo.move(enemigo.getLauchX(), enemigo.getLauchY());
				changeImage(enemigo, enemigo.getLauchMaster().getDireccion());
				if (enemigo.getLauchMaster().getY() < 0) {
					enemigo.setY(-10);
					enemigo.addOneFaseMovimiento();
				}
			} else
				backToPosition(enemigo, controler);

			break;
		case 13:
			backToPosition(enemigo, controler);

			break;

		default:
			break;
		}
	}

	/**
	 * Lauch del rojo en circulitos
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron2_1(Enemy enemigo, Enemy controler) {
		switch (enemigo.getFaseMovimiento()) {
		case 10:
			enemigo.setFaseMovimiento((int) (Math.random() * 2 + 11));
			enemigo.setDireccion(0);
			enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			break;
		case 11:
			moveCirculoLeft(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());

			}
			break;
		case 12:
			moveCirculoRight(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			break;
		case 13:
			moveToPosition(enemigo, (int) (enemigo.getTarget().getX()), (int) (enemigo.getTarget().getY()), 1.2f);

			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 15) {

				enemigo.setDirectionToTarget(enemigo.getDireccion());
				enemigo.addOneFaseMovimiento();

			}
			break;
		case 14:
			moveCirculoLeft(enemigo, 10, 1.2f);
			if (enemigo.getDirectionToTarget() - 3 == enemigo.getDireccion()) {
				enemigo.setFaseMovimiento(15);
			}
			if (enemigo.getY() > 220) {
				enemigo.setY(-10);

				enemigo.setDireccion(0);
				enemigo.setFaseMovimiento(16);

			}
			break;
		case 15:
			moveCirculoRight(enemigo, 10, 1.2f);
			if (enemigo.getDirectionToTarget() + 3 == enemigo.getDireccion()) {
				enemigo.setFaseMovimiento(14);
			}
			if (enemigo.getY() > 220) {
				enemigo.setY(-10);

				enemigo.setDireccion(0);
				enemigo.setFaseMovimiento(16);
			}
			break;

		case 16:
			backToPosition(enemigo, controler);
			break;

		default:
			break;
		}

	}

	/**
	 * Lauch del azul en circulitos
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron3_1(Enemy enemigo, Enemy controler) {

		switch (enemigo.getFaseMovimiento()) {
		case 10:
			enemigo.setFaseMovimiento((int) (Math.random() * 2 + 11));
			enemigo.setDireccion(0);
			enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			break;
		case 11:
			moveCirculoLeft(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());

			}
			break;
		case 12:
			moveCirculoRight(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			break;
		case 13:
			moveToPosition(enemigo, (int) (enemigo.getTarget().getX()), (int) (enemigo.getTarget().getY()), 1.2f);

			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 15) {

				enemigo.setDirectionToTarget(enemigo.getDireccion());
				enemigo.addOneFaseMovimiento();

			}
			break;
		case 14:
			moveCirculoLeft(enemigo, 10, 1.2f);
			if (enemigo.getDirectionToTarget() - 3 == enemigo.getDireccion()) {
				enemigo.setFaseMovimiento(15);
			}
			if (enemigo.getY() > 220) {

				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}

			}
			break;
		case 15:
			moveCirculoRight(enemigo, 10, 1.2f);
			if (enemigo.getDirectionToTarget() + 3 == enemigo.getDireccion()) {
				enemigo.setFaseMovimiento(14);
			}
			if (enemigo.getY() > 220) {

				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}
			}
			break;

		case 16:
			moveCirculoLeft(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 17:
			moveCirculoRight(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 18:
			backToPosition(enemigo, controler);
			break;

		default:
			break;
		}

	}

	/**
	 * Zigzag
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron3_2(Enemy enemigo, Enemy controler) {
		switch (enemigo.getFaseMovimiento()) {
		case 10:
			enemigo.setFaseMovimiento((int) (Math.random() * 2 + 11));
			enemigo.setDireccion(0);
			enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			break;
		case 11:
			moveCirculoLeft(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());

			}
			break;
		case 12:
			moveCirculoRight(enemigo, 10, 1.2f);
			if ((enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 20)) {
				enemigo.setFaseMovimiento(13);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			break;
		case 13:
			moveToPosition(enemigo, (int) (enemigo.getTarget().getX()), (int) (enemigo.getTarget().getY()), 1.2f);

			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 15) {

				enemigo.setDirectionToTarget(enemigo.getDireccion());
				enemigo.setFaseMovimiento((int) (Math.random() * 2 + 14));

			}
			break;

		case 14:
			moveDirection(enemigo, enemigo.getDirectionToTarget() - 2, 1.2f);
			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 30) {
				enemigo.setFaseMovimiento(15);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			if (enemigo.getY() > 220) {
				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}
			}
			break;
		case 15:
			moveDirection(enemigo, enemigo.getDirectionToTarget() + 2, 1.2f);
			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 30) {
				enemigo.setFaseMovimiento(14);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			if (enemigo.getY() > 220) {
				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}
			}
			break;

		case 16:
			moveCirculoLeft(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 17:
			moveCirculoRight(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 18:
			backToPosition(enemigo, controler);
			break;

		default:
			break;
		}

	}

	/**
	 * Zigzag
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron4(Enemy enemigo, Enemy controler) {
		switch (enemigo.getFaseMovimiento()) {
		case 10:

			enemigo.addOneFaseMovimiento();
			enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			break;
		case 11:
			moveDirection(enemigo, 6, 1.2f);
			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 30) {
				enemigo.setFaseMovimiento(12);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			if (enemigo.getY() > 220) {
				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}
			}
			break;
		case 12:
			moveDirection(enemigo, 10, 1.2f);
			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 30) {
				enemigo.setFaseMovimiento(11);
				enemigo.setPrefotograma(enemigo.getInterfaz().getFotograma());
			}
			if (enemigo.getY() > 220) {
				if (enemigo.getTarget().getX() < enemigo.getX()) {
					enemigo.setDireccion(11);
					enemigo.setFaseMovimiento(17);
				} else {
					enemigo.setDireccion(5);
					enemigo.setFaseMovimiento(16);
				}
			}
			break;
		case 13:
			moveToPosition(enemigo, (int) (enemigo.getTarget().getX()), (int) (enemigo.getTarget().getY()), 1.2f);

			if (enemigo.getInterfaz().getFotograma() - enemigo.getPrefotograma() > 15) {

				enemigo.setDirectionToTarget(enemigo.getDireccion());
				enemigo.addOneFaseMovimiento();

			}
			break;
		case 16:
			moveCirculoLeft(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 17:
			moveCirculoRight(enemigo, 30, 1.2f);
			if (enemigo.getY() < 190) {

				enemigo.setFaseMovimiento(18);
			}
			break;
		case 18:
			backToPosition(enemigo, controler);
			break;

		default:
			break;
		}

	}

	/**
	 * Bajan para abajo
	 * 
	 * @param enemigo
	 * @param controler
	 */
	public static void lauchPatron5(Enemy enemigo, Enemy controler) {

		switch (enemigo.getFaseMovimiento()) {
		case 10:

			enemigo.addOneFaseMovimiento();
			break;
		case 11:
			enemigo.setY(enemigo.getY() + 2);
			enemigo.updatePosition();
			if (enemigo.getY() > 230) {
				enemigo.setY(-10);
				enemigo.addOneFaseMovimiento();
			}
			break;
		case 12:
			moveToPosition(enemigo, (int) (enemigo.getXmatrix() + controler.getX()),
					(int) (enemigo.getYmatrix() + controler.getY()), 2);

			if (enemigo.getY() > (enemigo.getYmatrix() + controler.getY()) - 1
					&& enemigo.getY() < (enemigo.getYmatrix() + controler.getY()) + 1) {

				enemigo.setNormalMove(true);
				enemigo.addOneFaseMovimiento();
				enemigo.setLauchPatron(0);
			}
			break;

		default:
			break;
		}

	}

}
