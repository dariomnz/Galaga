package Librerias;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Se controla el sonido
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public abstract class Sound {
	/**
	 * Hace que suene un determinado chip
	 * 
	 * @param clip que suena
	 */
	public static void playSound(Clip clip) {
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clip.start();
		} else {
			System.err.println("No ha cargado bien el archivo de audio");
		}
	}

	/**
	 * Determina el archivo de un clip
	 * 
	 * @param fileName posicion del archivo
	 * @return clip del archivo
	 */
	public static Clip getSound(String fileName) {

		try {

			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("sounds\\" + fileName)));
			return clip;

		} catch (Exception e) {
			System.err.println("Fallo la carga de el archivo: " + fileName);
			return null;
		}

	}

	/**
	 * Comprueba si hay algun clip activo del array de clips que se introduce
	 * 
	 * @param clip array de clips para comprobar
	 * @return true si hay alguno activo, false si no
	 */
	public static boolean isAnyActive(Clip[] clip) {
		for (int i = 0; i < clip.length; i++) {
			if (clip[i].isActive())
				return true;
		}
		return false;

	}
}
