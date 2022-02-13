package Game;

import Librerias.Animacion;
import Librerias.Sound;

/**
 * Se encarga de los enemigos
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Enemy extends Nave {

	private int Xmatrix;
	private int Ymatrix;
	private int lauchPatron;
	private float movimientoLadoCirculo;
	private int faseMovimiento = 0;
	private long prefotograma = 0;
	private boolean isCambiarDireccion = false;
	private boolean isFinishAnimation = false;
	private boolean isNormalMove = true;
	private Enemy enemyControler;
	private Enemy lauchMaster;
	private Player target;
	private int directionToTarget;
	private int numImageIdle;

	/**
	 * Constructor de enemigo, que determina la imagen, tipo, posicion relativa y
	 * tambien los oculta
	 * 
	 * @param numTipo        tipo de enemigo, 1 comandante galaga, 2 goei y 3 zako
	 * @param bullet         que utiliza para disparar
	 * @param enemyControler controlador de enemigos
	 * @param Xmatrix        posicion x relariva en la matriz de enemigos
	 * @param Ymatrix        posicion y relariva en la matriz de enemigos
	 * @param interfaz       grafica sobre la que se trabaja
	 */
	public Enemy(int numTipo, Bullet[] bullet, Enemy enemyControler, int Xmatrix, int Ymatrix, Interfaz interfaz) {
		super(numTipo, interfaz);

		this.enemyControler = enemyControler;
		this.Xmatrix = Xmatrix;

		this.Ymatrix = Ymatrix;

		switch (getNumTipo()) {
		case 1:
			getInterfaz().gb_setSpriteImage(getId(), "enemy100.png");
			setSaludMax(1);
			setCollisionRadio(5);
			setVisible(false);
			setBullet(bullet);
			break;
		case 2:
			getInterfaz().gb_setSpriteImage(getId(), "enemy200.png");
			setCollisionRadio(5);
			setVisible(false);
			setBullet(bullet);
			break;
		case 3:
			getInterfaz().gb_setSpriteImage(getId(), "enemy300.png");
			setCollisionRadio(5);
			setVisible(false);
			setBullet(bullet);
			break;
		case 100:
			getInterfaz().gb_setSpriteImage(getId(), "Matriz.png", 290, 145);
			break;
		default:
			getInterfaz().gb_setSpriteImage(getId(), "punto.png");
			setVisible(false);
			setVelX(0.2f);
			setBullet(new Bullet[100]);

			for (int i = 0; i < getBullet().length; i++)
				getBullet()[i] = new Bullet(2, interfaz);
			break;
		}

	}

	/**
	 * Actualiza el splite en cada frame
	 */
	public void update() {

		if (isVisible())
			if (!isExplotando()) {
				if (isNormalMove()) {

					setX(((int) getEnemyControler().getX() + getXmatrix()));
					setY(((int) getEnemyControler().getY() + getYmatrix()));
					updatePosition();
					shoot(getEnemyControler(), getBullet());
				}

				if (getInterfaz().getFotograma() % 30 == 0) {
					setNumImageIdle(getEnemyControler().getNumImageIdle());
					setImage(Animacion.idleEnemigo[getNumTipo()][getNumImageIdle()]);
				}

			} else {
				animacionExplosion();
			}
		isColisionEnemyWithPlayer(getTarget());

	}

	public void updateEnemyControler(Player player, Enemy[][] enemigo) throws InterruptedException {
		moveRightLeft(enemigo);

		for (int i = 0; i < getBullet().length; i++) {
			getBullet()[i].update(player, enemigo);
		}

		if (getInterfaz().getFotograma() % 30 == 0) {
			if (getNumImageIdle() == 0) {
				setNumImageIdle(1);
			} else {
				setNumImageIdle(0);
			}
		}

	}

	/**
	 * Mueve al enemyMatrix que es el elemento de referencia de la matriz enemigo
	 * 
	 * @param enemigo la matriz enemigo
	 * @throws InterruptedException
	 */
	public void moveRightLeft(Enemy[][] enemigo) throws InterruptedException {
		if (isNormalMove()) {

			setX(getX() + getVelX());

			if (!isVisibleAnyEnemy(enemigo)) {
				Juego.changeLevel();
			}

			if (getX() < 15 || getX() > 65) {
				setVelX(getVelX() * -1);
			}
		}
	}

	public void animacionExplosion() {

		if (getInterfaz().getFotograma() % 4 == 0) {

			if (getNumImageExplosion() == 5) {
				setNumImageExplosion(0);
				setExplotando(false);
				setVisible(false);
				setImage(Animacion.idleEnemigo[getNumTipo()][getNumImageExplosion()]);

			} else {
				setImage(Animacion.explosionEnemy[getNumImageExplosion()]);
				updatePosition();
				setNumImageExplosion(getNumImageExplosion() + 1);
			}
		}

	}

	private boolean isColisionEnemyWithPlayer(Player player) {

		if (isCollision(player.getCollisionBox(), (int) getX(), (int) getY()))
			if (this.isVisible() && player.isVisible()) {

				setSaludAct(0);
				removeEnemy(player);

				if (player.isCanHit()) {
					player.setExplotando(true);
					return true;
				}
			}
		return false;
	}

	/**
	 * Esconde el sprite si no le quedan vidas
	 */
	public void removeEnemy(Player player) {
		if (getSaludAct() == 0) {

			if (!isExplotando()) {
				Sound.playSound(getInterfaz().getSoundEnemyExplosion());

				switch (getNumTipo()) {
				case 4:
				case 1:
					player.setPuntos(player.getPuntos() + 400);
					break;
				case 2:
					player.setPuntos(player.getPuntos() + 250);
					break;
				case 3:
					player.setPuntos(player.getPuntos() + 100);
					break;

				default:
					break;
				}

				int rand = (int) (Math.random() * 20);
				switch (rand) {
				case 0:
				case 1:
				case 2:
				case 3:
					player.getBonus()[rand].create((int) getX(), (int) getY());
					break;

				default:
					break;

				}

			}
			setExplotando(true);
			setNormalMove(true);
			setDireccion(0);
			setLauchPatron(0);
			setMovimientoLadoCirculo(0);
			setFinishAnimation(true);

			if (getNumTipo() == 4)
				setNumTipo(1);
		} else

		{
			setSaludAct(getSaludAct() - 1);
			setNumTipo(4);
		}

	}

	/**
	 * Comprueba si algun enemigo esta visible
	 * 
	 * @param enemigo es la matriz de enmigos sobre los que comprueba
	 * @return true si hay algun enemigo en visible, false si no hay ninguno
	 */
	public boolean isVisibleAnyEnemy(Enemy[][] enemigo) {
		for (int i = 0; i < enemigo.length; i++)
			for (int j = 0; j < enemigo[i].length; j++)
				if (enemigo[i][j].isVisible())
					return true;
		return false;
	}

	/**
	 * Comprueba si hay algun enemigo en modo normal
	 * 
	 * @param enemigo es la matriz de enmigos sobre los que comprueba
	 * @return true si hay algun enemigo en visible, false si no hay ninguno
	 */
	public boolean isNormalModeAnyEnemy(Enemy[][] enemigo) {
		for (int i = 0; i < enemigo.length; i++)
			for (int j = 0; j < enemigo[i].length; j++)
				if (enemigo[i][j].isNormalMove())
					return true;
		return false;
	}

	/**
	 * Dispara de forma ramdon
	 * 
	 * @param numDisparo numero de disparo total de los enemigos
	 * @param torpedo2   matriz sobre la que actua
	 */
	public void shoot(Enemy enemyMatriz, Bullet[] torpedo2) {

		if ((int) (Math.random() * 1000) == 1) {
			Sound.playSound(getInterfaz().getSoundEnemyShoot());

			torpedo2[enemyMatriz.getDisparos() % 100].create((int) getX(), (int) getY(), 0, 3);
			setDisparos(getDisparos() + 1);
			enemyMatriz.addDisparos();
		}
	}

	/*
	 * Getters y setters
	 */
	public int getXmatrix() {
		return Xmatrix;
	}

	public int getYmatrix() {
		return Ymatrix;
	}

	public int getLauchPatron() {
		return lauchPatron;
	}

	public void setLauchPatron(int lauchPatron) {
		this.lauchPatron = lauchPatron;
	}

	public boolean isCambiarDireccion() {
		return isCambiarDireccion;
	}

	public void setCambiarDireccion(boolean isCambiarDireccion) {
		this.isCambiarDireccion = isCambiarDireccion;
	}

	public float getMovimientoLadoCirculo() {
		return movimientoLadoCirculo;
	}

	public void setMovimientoLadoCirculo(float f) {
		this.movimientoLadoCirculo = f;
	}

	public boolean isFinishAnimation() {
		return isFinishAnimation;
	}

	public void setFinishAnimation(boolean isFinishAnimation) {
		this.isFinishAnimation = isFinishAnimation;
	}

	public Enemy getEnemyControler() {
		return enemyControler;
	}

	public void setEnemyControler(Enemy enemyControler) {
		this.enemyControler = enemyControler;
	}

	public boolean isNormalMove() {
		return isNormalMove;
	}

	public void setNormalMove(boolean isNormalMove) {
		this.isNormalMove = isNormalMove;
	}

	public int getFaseMovimiento() {
		return faseMovimiento;
	}

	public void setFaseMovimiento(int faseMovimiento) {
		this.faseMovimiento = faseMovimiento;
	}

	public void addOneFaseMovimiento() {
		this.faseMovimiento++;
	}

	public long getPrefotograma() {
		return prefotograma;
	}

	public void setPrefotograma(long prefotograma) {
		this.prefotograma = prefotograma;
	}

	public Enemy getLauchMaster() {
		return lauchMaster;
	}

	public void setLauchMaster(Enemy lauchMaster) {
		this.lauchMaster = lauchMaster;
	}

	public int getLauchX() {
		switch (getYmatrix()) {
		case 10:
			switch (getXmatrix()) {
			case 10:
			case 30:
			case 50:
			case 70:
				return (int) (getLauchMaster().getX() - 15);
			case 20:
			case 40:
			case 60:
			case 80:
				return (int) (getLauchMaster().getX() + 5);
			default:
				return 0;
			}
		case 20:
			switch (getXmatrix()) {
			case 10:
			case 30:
			case 50:
			case 70:

				return (int) (getLauchMaster().getX() - 5);
			case 20:
			case 40:
			case 60:
			case 80:
				return (int) (getLauchMaster().getX() + 15);
			default:
				return 0;
			}
		default:
			return 0;
		}
	}

	public int getLauchY() {
		switch (getYmatrix()) {
		case 10:
			switch (getXmatrix()) {
			case 10:
			case 30:
			case 50:
			case 70:
				return (int) (getLauchMaster().getY() + 7);
			case 20:
			case 40:
			case 60:
			case 80:
				return (int) (getLauchMaster().getY() + 12);
			default:
				return 0;
			}
		case 20:
			switch (getXmatrix()) {
			case 10:
			case 30:
			case 50:
			case 70:

				return (int) (getLauchMaster().getY() + 12);

			case 20:
			case 40:
			case 60:
			case 80:
				return (int) (getLauchMaster().getY() + 7);
			default:
				return 0;
			}
		default:
			return 0;
		}
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public int getDirectionToTarget() {
		return directionToTarget;
	}

	public void setDirectionToTarget(int directionToTarget) {
		this.directionToTarget = directionToTarget;
	}

	public int getNumImageIdle() {
		return numImageIdle;
	}

	public void setNumImageIdle(int numImageIdle) {
		this.numImageIdle = numImageIdle;
	}
}
