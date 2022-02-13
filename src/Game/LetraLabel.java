package Game;

import Librerias.*;

/**
 * Se encarga de el label de letras
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class LetraLabel extends Sprite {

	public LetraLabel(int numTipo, int anchura, int altura, Interfaz interfaz) {
		super(numTipo, interfaz);
		getInterfaz().gb_addSprite(getId(), Animacion.letra[getNumTipo()], true);
		getInterfaz().gb_setSpriteImage(getId(), Animacion.letra[getNumTipo()], anchura, altura);
	}

	/**
	 * Cambia las imagenes de el array de letraLabel introducido
	 * 
	 * @param texto      que quieres que se ponga en el letraLabel
	 * @param letralabel que quieres que cambie
	 */
	public static void updateImage(String texto, LetraLabel[] letralabel) {
		if (texto.length() <= letralabel.length) {
			int numLetrasEmpty = letralabel.length - texto.length();
			texto = texto.toUpperCase();
			for (int i = 0; i < numLetrasEmpty; i++) {
				texto = " " + texto;
			}

			for (int i = 0; i < letralabel.length; i++) {

				letralabel[i].setImage(Animacion.letra[(int) texto.charAt(i)], 15, 17);

			}
		} else {
			System.err.println("El texto es mas largo de lo que se puede poner en este letraLabel");
		}
	}

	/**
	 * Cambia las imagenes de el array de letraLabel introducido, el de creditos
	 * 
	 * @param texto      que quieres que se ponga en el letraLabel
	 * @param letralabel que quieres que cambie
	 */
	public static void updateImageCredits(String texto, LetraLabel[] letralabel) {
		if (texto.length() <= letralabel.length) {
			int numLetrasEmpty = letralabel.length - texto.length();
			texto = texto.toUpperCase();
			for (int i = 0; i < numLetrasEmpty; i++) {
				texto = " " + texto;
			}

			for (int i = 0; i < letralabel.length; i++) {

				letralabel[i].setImage(Animacion.letra[(int) texto.charAt(i)], 6, 8);

			}
		} else {
			System.err.println("El texto es mas largo de lo que se puede poner en este letraLabel");
		}
	}

}