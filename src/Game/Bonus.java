package Game;

import Librerias.Sound;

/**
 * Se encarga de las balas
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Bonus extends Sprite {

	private int XposicionEspera, YposicionEspera;
	private boolean isEnCaida;
	private long preFotograma;
	private boolean isActive;

	/**
	 * Constructor de Bonus que determina su image y su radio de colision, tambien
	 * lo oculta
	 * 
	 * @param id       del Sprite
	 * @param numTipo  tipo 1 rayo, tipo 2 bala, tipo 3 corazon, tipo 4 escudo
	 * @param interfaz sobre la que se trabaja
	 */
	public Bonus(int numTipo, Interfaz interfaz) {
		super(numTipo, interfaz);
		switch (getNumTipo()) {
		case 1:
			getInterfaz().gb_setSpriteImage(getId(), "rayito.png");
			setXposicionEspera(140);
			break;
		case 2:
			getInterfaz().gb_setSpriteImage(getId(), "balabonus.png");
			setXposicionEspera(160);
			break;
		case 3:
			getInterfaz().gb_setSpriteImage(getId(), "corazon.png");
			break;
		case 4:
			getInterfaz().gb_setSpriteImage(getId(), "celulaEnergia.png");
			setXposicionEspera(120);
			break;
		}
		setCollisionRadio(10);
		setYposicionEspera(215);
		setVisible(false);
		setVelY(2);

	}

	/**
	 * Actualiza el splite en cada frame
	 */
	public void update(Player player) {
		if (isVisible()) {
			if (isEnCaida()) {
				move();

				if (isColisionBonusWithPlayer(player)) {
					Sound.playSound(getInterfaz().getSoundPowerUp());
					move(getXposicionEspera(), getYposicionEspera());
					setEnCaida(false);
					setPreFotograma(getInterfaz().getFotograma());
					setActive(true);
					switch (getNumTipo()) {
					case 1:
						player.setVelX(4);
						break;
					case 2:
						player.setVelShooting(300);
						break;
					case 3:
						if (player.getSaludAct() < player.getSaludMax())
							player.setSaludAct(player.getSaludAct() + 1);
						setVisible(false);
						setActive(false);
						break;
					case 4:
						player.setCanHit(false);
						break;

					default:
						break;
					}

				}
			} else {
				if (getInterfaz().getFotograma() - getPreFotograma() > 360) {
					setVisible(false);
					setActive(false);
					switch (getNumTipo()) {
					case 1:
						player.setVelX(2);
						break;
					case 2:
						player.setVelShooting(500);
						break;
					case 4:
						player.setCanHit(true);
						break;

					default:
						break;
					}

				}

			}
		}
		if (isActive())
			if (getInterfaz().getFotograma() - getPreFotograma() > 300) {
				if (getInterfaz().getFotograma() % 5 == 0) {
					if (isVisible())
						setVisible(false);
					else
						setVisible(true);

				}
			}
	}

	public boolean isColisionBonusWithPlayer(Player jugador) {
		if (isCollision(this.getCollisionBox(), (int) jugador.getX(), (int) jugador.getY())) {
			if (jugador.isVisible() && isVisible()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sobreescribo el metodo para que tambien cambie un valor del objeto bonus
	 */

	public void create(int x, int y) {
		if (!isActive() && !isEnCaida()) {
			super.create(x, y);
			setEnCaida(true);

		}
	}

	/**
	 * Mueve el bonus a su velocidad y jugadores
	 * 
	 * @param jugador
	 * @param enemigo
	 */
	public void move() {
		setY(getY() + getVelY());
		updatePosition();
		if (getY() > 220) {
			setVisible(false);
			setEnCaida(false);
		}

	}

	public int getXposicionEspera() {
		return XposicionEspera;
	}

	public void setXposicionEspera(int xposicionEspera) {
		XposicionEspera = xposicionEspera;
	}

	public int getYposicionEspera() {
		return YposicionEspera;
	}

	public void setYposicionEspera(int yposicionEspera) {
		YposicionEspera = yposicionEspera;
	}

	public boolean isEnCaida() {
		return isEnCaida;
	}

	public void setEnCaida(boolean isEnCaida) {
		this.isEnCaida = isEnCaida;
	}

	public long getPreFotograma() {
		return preFotograma;
	}

	public void setPreFotograma(long preFotograma) {
		this.preFotograma = preFotograma;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}