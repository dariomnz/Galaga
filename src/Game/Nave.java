package Game;

/**
 * Es la super de Player y de Enemy a la vez hereda de Sprite
 * 
 * @author Dario
 * @version 21.12.2018
 */
public class Nave extends Sprite {

	private Bullet[] bullet;
	private boolean isExplotando = false;
	private int disparos;
	private int aciertos;
	private int numImageExplosion = 0;

	public Nave(int numTipo, Interfaz interfaz) {
		super(numTipo, interfaz);
	}

	public Bullet[] getBullet() {
		return bullet;
	}

	public void setBullet(Bullet[] bullet) {
		this.bullet = bullet;
	}

	public boolean isExplotando() {
		return isExplotando;
	}

	public void setExplotando(boolean isExplotando) {
		this.isExplotando = isExplotando;
	}

	public int getDisparos() {
		return disparos;
	}

	public void setDisparos(int disparos) {
		this.disparos = disparos;
	}

	/**
	 * Suma uno a la variable disparos
	 */
	public void addDisparos() {
		this.disparos++;
	}

	public int getAciertos() {
		return aciertos;
	}

	public void setAciertos(int aciertos) {
		this.aciertos = aciertos;
	}

	public int getNumImageExplosion() {
		return numImageExplosion;
	}

	public void setNumImageExplosion(int numImageExplosion) {
		this.numImageExplosion = numImageExplosion;
	}
}
