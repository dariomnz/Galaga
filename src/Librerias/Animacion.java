package Librerias;

/**
 * Se encarga de guardar las animaciones
 * 
 * @author Dario muñoz
 * @version 21.12.2018
 */
public abstract class Animacion {

	public static final String[] explosionEnemy = { "explosion20.png", "explosion21.png", "explosion22.png",
			"explosion23.png", "explosion24.png" };

	public static final String[] explosionPlayer = { "explosion11.png", "explosion12.png", "explosion13.png",
			"explosion24.png" };

	public static final String[] moveZako = { "enemy300.png", "enemy301.png", "enemy302.png", "enemy303.png",
			"enemy304.png", "enemy305.png", "enemy306.png", "enemy307.png", "enemy308.png", "enemy309.png",
			"enemy310.png", "enemy311.png", "enemy312.png", "enemy313.png", "enemy314.png", "enemy315.png" };

	public static final String[] idleZako = { "enemy3G0.png", "enemy3G1.png" };

	public static final String[] moveGoei = { "enemy200.png", "enemy201.png", "enemy202.png", "enemy203.png",
			"enemy204.png", "enemy205.png", "enemy206.png", "enemy207.png", "enemy208.png", "enemy209.png",
			"enemy210.png", "enemy211.png", "enemy212.png", "enemy213.png", "enemy214.png", "enemy215.png" };

	public static final String[] idleGoei = { "enemy2G0.png", "enemy2G1.png" };

	/*
	 * Comandante normal
	 */
	public static final String[] moveComandante1 = { "enemy100.png", "enemy101.png", "enemy102.png", "enemy103.png",
			"enemy104.png", "enemy105.png", "enemy106.png", "enemy107.png", "enemy108.png", "enemy109.png",
			"enemy110.png", "enemy111.png", "enemy112.png", "enemy113.png", "enemy114.png", "enemy115.png" };

	public static final String[] idleComandante1 = { "enemy1G0.png", "enemy1G1.png" };

	/**
	 * Comandante herido
	 */
	public static final String[] moveComandante2 = { "enemy900.png", "enemy901.png", "enemy902.png", "enemy903.png",
			"enemy904.png", "enemy905.png", "enemy906.png", "enemy907.png", "enemy908.png", "enemy909.png",
			"enemy910.png", "enemy911.png", "enemy912.png", "enemy913.png", "enemy914.png", "enemy915.png" };

	public static final String[] idleComandante2 = { "enemy9G0.png", "enemy9G1.png" };
	/**
	 * El 0 no es nada, el 1 es el comandante normal , el 2 Goei (rojo), el 3 Zako
	 * (azul) y el 4 comandante herido
	 */
	public static final String[][] moveEnemy = { { "" }, moveComandante1, moveGoei, moveZako, moveComandante2 };

	/**
	 * El 0 no es nada, el 1 es Zako (azul), el 2 Goei (rojo), el 3 comandante y el
	 * 4 comandante herido
	 */
	public static final String[][] idleEnemigo = { { "" }, idleComandante1, idleGoei, idleZako, idleComandante2 };

	public static final String[] bulletEspecial = { "torpedoEspecial0.png", "torpedoEspecial1.png",
			"torpedoEspecial2.png", "torpedoEspecial3.png", "torpedoEspecial4.png", "torpedoEspecial5.png",
			"torpedoEspecial6.png", "torpedoEspecial7.png", "torpedoEspecial8.png", "torpedoEspecial9.png",
			"torpedoEspecial10.png", "torpedoEspecial11.png", "torpedoEspecial12.png", "torpedoEspecial13.png",
			"torpedoEspecial14.png", "torpedoEspecial15.png" };

	public static final String[] numero = { "0.png", "1.png", "2.png", "3.png", "4.png", "5.png", "6.png", "7.png",
			"8.png", "9.png" };

	public static final String[] letra = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "empty.png", "", "", "", "", "%.png", "", "", "", "",
			"", "", "", "-.png", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "a.png",
			"b.png", "c.png", "d.png", "e.png", "f.png", "g.png", "h.png", "i.png", "j.png", "k.png", "l.png", "m.png",
			"n.png", "o.png", "p.png", "q.png", "r.png", "s.png", "t.png", "u.png", "v.png", "w.png", "x.png", "y.png",
			"z.png" };

}
