package Game;

import Librerias.*;

/**
 * Se encarga del jugador
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Player extends Nave {
	private long tiempoDisparo;
	private int record;
	private int puntos = 0;
	private int velShooting;
	private String name = "Dario";
	private boolean canShoot = false;
	private boolean isGodActive = false;
	private boolean canHit = true;
	private Enemy[][] enemigo;
	private Bullet bulletSpecial;
	private Bonus[] bonus;
	private Sprite[] vida;

	/**
	 * Constructor de Player que determina su imagen, su velocidad de disparo y lo
	 * esconde
	 * 
	 * @param id       del sprite
	 * @param interfaz sobre la que se trabaja
	 */
	public Player(Enemy[][] enemigo, Interfaz interfaz) {
		super(0, interfaz);
		setBullet(new Bullet[50]);
		setCollisionRadio(10);
		for (int i = 0; i < getBullet().length; i++)
			getBullet()[i] = new Bullet(1, interfaz);

		setBulletSpecial(new Bullet(3, interfaz));

		setBonus(new Bonus[4]);
		for (int i = 0; i < bonus.length; i++) {
			bonus[i] = new Bonus(i + 1, interfaz);
		}

		setVida(new Sprite[5]);
		for (int i = 0; i < vida.length; i++) {
			vida[i] = new Sprite(interfaz);
			vida[i].setImage("corazon.png");
			vida[i].move(10 * i + 5, 215);
		}

		setEnemigo(enemigo);
		getInterfaz().gb_setSpriteImage(getId(), "player.png");
		setVisible(false);
		setVelShooting(500);
		setSaludMax(5);
		setSaludAct(3);
	}

	/**
	 * Actualiza el splite en cada frame
	 */
	public void update() {
		if (!isExplotando()) {
			move();
			shoot();
		} else {
			animacionExplosion();
		}

		for (int i = 0; i < getBullet().length; i++) {
			getBullet()[i].update(this, getEnemigo());
		}

		getBulletSpecial().updateBulletSpecial(this, getEnemigo());

		for (int i = 0; i < bonus.length; i++) {
			bonus[i].update(this);
		}

		if (getInterfaz().getFotograma() % 5 == 0)
			for (int i = 0; i < vida.length; i++) {
				if (i < getSaludAct())
					vida[i].setVisible(true);
				else
					vida[i].setVisible(false);
			}
	}

	/**
	 * Mueve al jugador dentro de los limites, depende de si se pulsa las teclas o
	 * no
	 */
	public void move() {
		setX(getX() + (getVelX() * (getInterfaz().getRIGHT() - getInterfaz().getLEFT())));
		if (getX() < 10 || getX() > 160)
			setX(getX() - (getVelX() * (getInterfaz().getRIGHT() - getInterfaz().getLEFT())));
		updatePosition();
	}

	/**
	 * Crea una bullet en el jugador cada vez que se llama
	 * 
	 * @param bullet balas que usa
	 */
	public void shoot() {
		if (isCanShoot())
			if ((System.currentTimeMillis() - tiempoDisparo) > getVelShooting()) {
				Sound.playSound(getInterfaz().getSoundDisparo());

				/*
				 * Uso los disparos modulo de 50 para recorrer la matriz de Bullet sin
				 * pasarme
				 */
				getBullet()[(getDisparos()) % 50].create((int) getX(), (int) getY(), 0, -3);
				addDisparos();
				tiempoDisparo = System.currentTimeMillis();
			}
	}

	public void animacionExplosion() {

		if (getInterfaz().getFotograma() % 4 == 0) {

			if (getNumImageExplosion() == Animacion.explosionPlayer.length) {
				setNumImageExplosion(0);
				setExplotando(false);
				setVisible(false);
				setImage("player.png");

			} else {
				if (getNumImageExplosion() == 0)
					Sound.playSound(getInterfaz().getSoundPlayerExplosion());
				setImageDouble(Animacion.explosionPlayer[getNumImageExplosion()]);
				move((int) getX(), (int) getY());
				setNumImageExplosion(getNumImageExplosion() + 1);
			}
		}

	}

	/*
	 * A partir de aqui geters y seters
	 */
	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Enemy[][] getEnemigo() {
		return enemigo;
	}

	public void setEnemigo(Enemy[][] enemigo) {
		this.enemigo = enemigo;
	}

	public Bullet getBulletSpecial() {
		return bulletSpecial;
	}

	public void setBulletSpecial(Bullet bulletSpecial) {
		this.bulletSpecial = bulletSpecial;
	}

	public boolean isCanShoot() {
		return canShoot;
	}

	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}

	public boolean isGodActive() {
		return isGodActive;
	}

	public void setGodActive(boolean isGodActive) {
		this.isGodActive = isGodActive;
		if (isGodActive) {
			setImageDouble("playerConEscudo.png");
			this.canHit = false;
		} else {
			setImage("player.png");
			this.canHit = true;
		}

	}

	public boolean isCanHit() {
		return canHit;
	}

	public void setCanHit(boolean canHit) {
		if (!isGodActive()) {
			this.canHit = canHit;
			if (canHit) {
				setImage("player.png");
			} else {
				setImageDouble("playerConEscudo.png");
			}
		}
	}

	public int getVelShooting() {
		return velShooting;
	}

	public void setVelShooting(int velShooting) {
		this.velShooting = velShooting;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public Bonus[] getBonus() {
		return bonus;
	}

	public void setBonus(Bonus[] bonus) {
		this.bonus = bonus;
	}

	public Sprite[] getVida() {
		return vida;
	}

	public void setVida(Sprite[] vida) {
		this.vida = vida;
	}
}
