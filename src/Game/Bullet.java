package Game;

import Librerias.Animacion;
import Librerias.Sound;

/**
 * Se encarga de las balas
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Bullet extends Sprite {

	private int numImageBulletSpecial = 1;

	/**
	 * Constructor de Bullet que determina su image y su radio de colision, tambien
	 * lo oculta
	 * 
	 * @param id       del Sprite
	 * @param numTipo  tipo de bullet 1 bullet player,2 bullet enemy y 3 bullet
	 *                 special
	 * @param interfaz sobre la que se trabaja
	 */
	public Bullet(int numTipo, Interfaz interfaz) {
		super(numTipo, interfaz);

		switch (getNumTipo()) {
		case 1:
			getInterfaz().gb_setSpriteImage(getId(), "torpedo100.png");
			setCollisionRadio(7);
			break;
		case 2:
			getInterfaz().gb_setSpriteImage(getId(), "torpedo200.png");
			setCollisionRadio(7);
			break;
		case 3:
			getInterfaz().gb_setSpriteImage(getId(), "torpedoEspecial0.png");
			setCollisionRadio(10);
			setVelY(-2);
			break;

		case 8:
			getInterfaz().gb_setSpriteImage(getId(), "Matriz.png", 2, 2);
			break;
		}
		setVisible(false);

	}

	/**
	 * Actualiza el splite en cada frame
	 */
	public void update(Player player, Enemy[][] enemigo) {
		if (isVisible()) {
			move(player, enemigo);
		}

	}

	/**
	 * Actualiza el splite en cada frame
	 */
	public void updateBulletSpecial(Player player, Enemy[][] enemigo) {
		if (isVisible()) {

			/*
			 * Esto es cuando ya ha pasado el tiempo de carga y empieza a moverse
			 */

			if (getTamanio() > 60) {
				move(player, enemigo);

				/*
				 * Aqui comprueba si esta dentro de la pantalla y si esta lo esconde para no
				 * gastar recursos
				 */
				if (getY() < 0) {
					setVisible(false);
				}

			} else {

				/*
				 * Aqui se encarga de colocar la bola delante del jugador mientras se esta
				 * cargando el ataque
				 */
				setX((int) player.getX());
				setY((int) player.getY() - 20);
				updatePosition();

				/*
				 * Aqui cambia su tamaño
				 */

				setTamanio((getTamanio() + 1));

			}

			/*
			 * Esto se encarga de realizar la animacion del torpedoEspecial
			 */

			setImage(Animacion.bulletEspecial[getNumImageBulletSpecial()], getTamanio());
			if (getInterfaz().getFotograma() % 4 == 0)
				setNumImageBulletSpecial(getNumImageBulletSpecial() + 1);

			if (getNumImageBulletSpecial() == 16) {
				setNumImageBulletSpecial(1);
			}
		}
	}

	/**
	 * Mueve la bala en base a su velocidad y comprueba las colisiones con enemigos
	 * y jugadores
	 * 
	 * @param jugador
	 * @param enemigo
	 */
	public void move(Player jugador, Enemy[][] enemigo) {
		setY(getY() + getVelY());
		updatePosition();
		if (getY() < 0 || getY() > 215)
			setVisible(false);
		if (getVelY() < 0)
			isColisionBulletWithEnemy(enemigo, jugador);
		else
			isColisionBulletWithPlayer(jugador);

	}

	/**
	 * Comprueba la colision con enemy
	 * 
	 * @param enemigo matrix de enemigos sobre la que comprueba
	 * @return true si colisiona false si no colisiona
	 */
	public boolean isColisionBulletWithEnemy(Enemy[][] enemigo, Player player) {

		for (int i = 0; i < enemigo.length; i++)
			for (int j = 0; j < enemigo[i].length; j++)
				if (enemigo[i][j].isVisible() && isVisible())
					if (!enemigo[i][j].isExplotando())
						if (isCollision(this.getCollisionBox(), (int) enemigo[i][j].getX(),
								(int) enemigo[i][j].getY())) {

							if (getNumTipo() != 3)
								setVisible(false);

							player.setAciertos(player.getAciertos() + 1);

							enemigo[i][j].removeEnemy(player);
							return true;
						}

		return false;
	}

	/**
	 * Comprueba la colision con player
	 * 
	 * @param jugador sobre el que comprueba la colision
	 * @return true si colisiona false si no colisiona
	 */
	public boolean isColisionBulletWithPlayer(Player jugador) {
		if (isCollision(this.getCollisionBox(), (int) jugador.getX(), (int) jugador.getY())) {
			if (jugador.isVisible() && isVisible()) {
				setVisible(false);
				if (jugador.isCanHit())
					removePlayer(jugador);
			}
			return true;
		}
		return false;
	}

	public void removePlayer(Player player) {
		if (player.getSaludAct() < 1) {
			player.setExplotando(true);
			player.setVelX(2);
			player.setVelShooting(500);
		} else {
			Sound.playSound(getInterfaz().getSoundHitPlayer());
			for (int i = 0; i < 4; i++) {
				if (player.isVisible())
					player.setVisible(false);
				else
					player.setVisible(true);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			player.setSaludAct(player.getSaludAct() - 1);
		}
	}

	public int getNumImageBulletSpecial() {
		return numImageBulletSpecial;
	}

	public void setNumImageBulletSpecial(int numImageBulletSpecial) {
		this.numImageBulletSpecial = numImageBulletSpecial;
	}

}
