package Game;

import Librerias.*;

/**
 * Es la super de cualquier sprite
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class Sprite {

	private int id;

	private int saludMax;
	private int saludAct;
	private int tamanio;

	private int numTipo;
	private int collisionRadio;
	private int direccion = 0;
	private float velX;
	private float velY;
	private float x;
	private float y;
	private boolean isVisible;
	private Interfaz interfaz;
	private int[] collisionBox;

	private boolean isMovingToPosition = false;

	private String image = "";


	/**
	 * Contructor de Sprite
	 * 
	 * @param id       del sprite
	 * @param numTipo
	 * @param interfaz sobre la que se trabaja
	 */
	public Sprite(int numTipo, Interfaz interfaz) {
		this.id = interfaz.getUniqueNumeroId();
		this.numTipo = numTipo;
		this.interfaz = interfaz;
		this.collisionBox = new int[4];

		interfaz.gb_addSprite(getId(), "empty.png", true);

	}

	public Sprite(Interfaz interfaz) {
		this.id = interfaz.getUniqueNumeroId();
		this.interfaz = interfaz;
		this.collisionBox = new int[4];

		interfaz.gb_addSprite(getId(), "empty.png", true);

	}

	/**
	 * Mueve el sprite a la posicion x,y
	 * 
	 * @param x posicion en x
	 * @param y posicion en y
	 */
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		updatePosition();
	}

	/**
	 * Actualiza la posicion para que se muestre en pantalla en esa posicion
	 */
	public void updatePosition() {
		this.interfaz.gb_moveSpriteCoord(this.id, (int) this.x, (int) this.y);
	}

	public void setImage(String image) {
		this.image = image;
		getInterfaz().gb_setSpriteImage(getId(), image);

	}

	public void setImageDouble(String image) {
		this.image = image;
		getInterfaz().gb_setSpriteImageDouble(getId(), image);

	}

	public void setImage(String image, int tamanio) {
		this.image = image;
		getInterfaz().gb_setSpriteImage(this.id, image, tamanio, tamanio);

	}

	public void setImage(String image, int anchura, int altura) {
		this.image = image;
		getInterfaz().gb_setSpriteImage(this.id, image, anchura, altura);

	}

	public String getImage() {
		return this.image;
	}

	/**
	 * Crea un sprite en pantalla en la posicion x,y
	 * 
	 * @param x posicion en x
	 * @param y posicion en y
	 */
	public void create(int x, int y) {
		this.x = x;
		this.y = y;
		setSaludAct(getSaludMax());
		updatePosition();
		setVisible(true);
	}

	/**
	 * Crea un sprite en pantalla en la posicion x,y y le determina su velicidad
	 * tipo float en x y en y
	 * 
	 * @param x    posicion en x
	 * @param y    posicion en y
	 * @param velX velocidad en x
	 * @param velY velocidad en y
	 */
	public void create(int x, int y, float velX, float velY) {
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		setSaludAct(getSaludMax());
		updatePosition();
		setVisible(true);
	}

	/**
	 * Comprueba la colision de un rectangulo con un punto
	 * 
	 * @param box rectangulo
	 * @param x   del punto
	 * @param y   del punto
	 * @return true si el punto esta dentro del cuadrado false si no esta
	 */
	public boolean isCollision(int[] box, int x, int y) {
//		/*
//		 * Los puntos estan determinados de la siguiente forma (x,y)
//		 * 	             
//		 *             
//		 *      (box[0],box[2])______________(box[1],box[2])
//		 *                     |            | 
//		 *                     |            |
//		 *                     |   *(x,y)   | 
//		 *                     |            | 
//		 *      (box[0],box[3])|____________|(box[1],box[3])
//		 * 
//		 * 
//		 *Si un punto de una caja esta dentro de otra caja, estas estan en colision              
//		 */
		/*
		 * Si el punto de la forma (x,y) esta dentro de box, devuelve true
		 */
		if (box[0] < x && box[1] > x && box[2] < y && box[3] > y)
			return true;
		return false;
	}

	/*
	 * A partir de aqui van los getters y setters
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Si es true muestra el sprite o si es false lo esconde
	 * 
	 * @param isVisible detetermina si se ve o no
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.interfaz.gb_setSpriteVisible(getId(), this.isVisible);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getSaludMax() {
		return saludMax;
	}

	public void setSaludMax(int saludMax) {
		this.saludMax = saludMax;
	}

	public int getSaludAct() {
		return saludAct;
	}

	public void setSaludAct(int saludAct) {
		this.saludAct = saludAct;
	}

	public int getTamanio() {
		return tamanio;
	}

	public void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}

	/**
	 * Devuelve una caja de la forma [left][right][up][down]
	 * 
	 * @return caja de colision del sprite [left][right][up][down]
	 */
	public int[] getCollisionBox() {
		this.collisionBox[0] = (int) (this.x - this.collisionRadio);
		this.collisionBox[1] = (int) (this.x + this.collisionRadio);
		this.collisionBox[2] = (int) (this.y - this.collisionRadio);
		this.collisionBox[3] = (int) (this.y + this.collisionRadio);
		return this.collisionBox;
	}

	public void setCollisionBox(int[] collisionBox) {
		this.collisionBox = collisionBox;
	}

	public Interfaz getInterfaz() {
		return this.interfaz;
	}

	public int getNumTipo() {
		return numTipo;
	}

	public void setNumTipo(int numTipo) {
		this.numTipo = numTipo;
		setImage(Animacion.idleEnemigo[getNumTipo()][0]);
	}

	public int getCollisionRadio() {
		return collisionRadio;
	}

	public void setCollisionRadio(int collisionRadio) {
		this.collisionRadio = collisionRadio;
	}

	public boolean isMovingToPosition() {
		return isMovingToPosition;
	}

	public void setMovingToPosition(boolean isMovingToPosition) {
		this.isMovingToPosition = isMovingToPosition;
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public void addOneDireccion() {
		this.direccion++;
	}

}
