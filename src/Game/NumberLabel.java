package Game;

import Librerias.*;

/**
 * Se encarga de el label de numeros
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public class NumberLabel extends Sprite {

	public NumberLabel(int numTipo, int anchura, int altura, Interfaz interfaz) {
		super(numTipo, interfaz);
		getInterfaz().gb_addSprite(getId(), Animacion.numero[getNumTipo()], true);
		getInterfaz().gb_setSpriteImage(getId(), Animacion.numero[getNumTipo()], anchura, altura);

	}

	/**
	 * Cambia las imagenes de el array de NumberLabel introducido
	 * 
	 * @param texto      que quieres que se ponga en el letraLabel
	 * @param labelPuntos que quieres que cambie
	 */
	public static void updateImage(int numero, NumberLabel[] labelPuntos) {
		if (Integer.toString(numero).length() <= labelPuntos.length) {
			int[] puntosPlayer = new int[labelPuntos.length];
			String textPuntosPlayer = "";
			int numeroCeros = labelPuntos.length - Integer.toString(numero).length();
			for (int i = 0; i < numeroCeros; i++) {
				textPuntosPlayer = "0" + textPuntosPlayer;
			}
			textPuntosPlayer = textPuntosPlayer + Integer.toString(numero);

			for (int i = 0; i < textPuntosPlayer.length(); i++) {
				puntosPlayer[i] = 0;

				try {
					puntosPlayer[i] = Integer.parseInt(Character.toString(textPuntosPlayer.charAt(i)));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			for (int i = 0; i < labelPuntos.length; i++) {
				if (i + 1 <= numeroCeros)
					labelPuntos[i].setImage("empty.png", 12, 20);
				else
					labelPuntos[i].setImage(Animacion.numero[puntosPlayer[i]], 12, 20);
			}
		}
	}

}
