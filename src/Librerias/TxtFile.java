package Librerias;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import Game.Player;

/**
 * Se encarga de trabajar con txt
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public abstract class TxtFile {

	/**
	 * Lee un arcivo
	 * 
	 * @param archivo que lee
	 * @return el contanido del archivo
	 */
	private static String leer(String archivo) {
		File file = null;
		FileReader fileReader = null;
		BufferedReader br = null;
		String linea;
		String lectura = "";
		try {
			file = new File("txt\\" + archivo);
			fileReader = new FileReader(file);
			br = new BufferedReader(fileReader);
			while ((linea = br.readLine()) != null) {
				lectura += linea + "\n";

			}
		} catch (Exception e) {
			System.err.println("Fallo la lectura");
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return lectura;
	}

	/**
	 * Lee un archivo txt
	 * 
	 * @param player  jugador del que coje el nombre y el record
	 * @param archivo el nombre del archivo a leer
	 * @return el record asociado al nombre del player
	 */
	public static String leer(Player player, String archivo) {
		File file = null;
		FileReader fileReader = null;
		BufferedReader br = null;
		String key = player.getName();
		String linea;
		String lectura = "";
		boolean existe = false;
		try {
			file = new File("txt\\" + archivo);
			fileReader = new FileReader(file);
			br = new BufferedReader(fileReader);

			while ((linea = br.readLine()) != null) {
				if (linea.startsWith(key + " ")) {
					lectura += linea + "\n";
					existe = true;
				}
			}
			if (existe)
				lectura = lectura.split(" ")[1];
			else {
				guardarRecord(player);
				lectura = "0";
			}
		} catch (Exception e) {
			System.err.println("Fallo la lectura");
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return lectura;
	}

	/**
	 * Reescribe el archivo con el texto introducido
	 * 
	 * @param texto   para escribir
	 * @param archivo en el que escribe
	 */
	private static void escribir(String texto, String archivo) {
		File file = null;
		PrintWriter fileWriter = null;
		try {
			file = new File("txt\\" + archivo);
			fileWriter = new PrintWriter(file);
			fileWriter.write(texto);

		} catch (Exception e) {
			System.err.println("Fallo la escritura");
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * Reescribe el record del jugador
	 * 
	 * @param player del que recoje el record
	 */
	public static void guardarRecord(Player player) {
		String txtRecord = TxtFile.leer("record.txt");

		String[] txtRecordArray = txtRecord.split(" ");

		String guardar = "";

		boolean existe = false;
		int posicion = 0;

		for (int i = 0; i < txtRecordArray.length; i++) {
			if (txtRecordArray[i].equals("\n" + player.getName())) {
				existe = true;
				posicion = i;
			}
		}
		if (!existe) {
			escribir(txtRecord + player.getName() + " " + player.getRecord() + " ", "record.txt");
		} else {
			txtRecordArray[posicion + 1] = Integer.toString(player.getRecord());

			for (int i = 0; i < txtRecordArray.length - 1; i++) {
				guardar += txtRecordArray[i] + " ";

			}

			escribir(guardar, "record.txt");
		}
	}
}
