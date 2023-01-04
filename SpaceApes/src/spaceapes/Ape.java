package spaceapes;

import org.newdawn.slick.geom.Vector2f;
import eea.engine.entity.Entity;

public class Ape extends Entity {

	private final Player belongsToPlayer; // Zugehoeriger Spieler
	private final Planet homePlanet; // Planet auf dem sich der Affe befindet

	private float angleOnPlanet; // Einheit Grad, Drehsinn im Uhrzeigersinn, 0 enspricht Osten
	private float angleOfView; // Blickwinkel zischen -90 grad (links) und 90 grad (rechts)
	private final float distancePlanetCenter; // Abstand des Planetenmittelpunkts zur Kreisbahn auf der sich der Affe
												// bewegt
	private float movmentSpeed = 0.08f; // Faktor fuer die Schrittweite des Affen
	public final float apeScalingFactor = 0.18f; // Faktor Abhaengig von Groesse und Skalierung des Affenbilds
	public final float apeDistanceFromSurface = 0.25f; // Faktor Abhaengig von Groesse und Skalierung des Affenbilds
	private int health = 100;

	/**
	 * Konstruktor fuegt einem Planet einen Affen hinzu, der zufaellig auf der
	 * Oberflaeche platziert wird. Ebenso wird der Spieler mit seinem Affen
	 * verknuepft
	 * 
	 * @param entityID String
	 * @param planet   Zugehoeriger Planet
	 * @param player   Zugehoeriger Spieler
	 */
	public Ape(String entityID, Planet planet, Player player) {
		super(entityID);
		belongsToPlayer = player;
		player.setApe(this); // Bidirektionaler Zugriff moeglich: Affe kennt seinen Spieler und umgekehrt.
		homePlanet = planet;
		planet.setApe(this); // Affe kennt seinen Planeten und Planet wei� welcher Affe auf ihm sitzt.
		angleOnPlanet = Utils.randomFloat(0, 360);
		angleOfView = 0f;
		distancePlanetCenter = homePlanet.distanceToEntityPosition();

		setPosition(Utils.toPixelCoordinates(this.getCoordinates()));
		setScale(apeScalingFactor);
		setRotation(angleOnPlanet + 90f); // Entity Rotation ist nach oben Zeigend als 0 definiert

	}

	/**
	 * Berechnet die Welt-Koordinaten des Affen, abhaengig von seinem Winkel auf dem
	 * Planeten (angleOnPlanet) und der Position des Planeten
	 * 
	 * @return Vector2f in Welt-Koordinaten
	 */
	public Vector2f getCoordinates() {
		Vector2f relativPos = Utils.toCartesianCoordinates(distancePlanetCenter, angleOnPlanet);
		// Koordinaten des Planeten + relative Koordinaten vom Planeten zum Affen
		return new Vector2f(homePlanet.getCoordinates().x + relativPos.x, homePlanet.getCoordinates().y + relativPos.y);
	}

	/**
	 * Aendert angleOnPlanet des Affens nach links oder rechts
	 * 
	 * @param direction fuer Bewegung nach links -1 und fuer Bewegung nach rechts +1
	 */
	public void stepOnPlanet(int direction) {
		if (!(direction == -1 || direction == 1)) {
			throw new RuntimeException("Ungueltige direction");
		}
		angleOnPlanet += direction * movmentSpeed / distancePlanetCenter; // Update des Winkels

		setPosition(Utils.toPixelCoordinates(this.getCoordinates()));
		setRotation(angleOnPlanet + 90f);
	}

	public Player belongsToPlayer() {
		return belongsToPlayer;
	}

	public Planet getHomePlanet() {
		return homePlanet;
	}

	public float getAngleOnPlanet() {
		return angleOnPlanet;
	}

	public void setAngleOnPlanet(float alpha) {
		angleOnPlanet = alpha;
	}

	public void setAngleOfView(float beta) {
		angleOfView = beta;
	}

	public float getAngleOfView_local() {
		return angleOfView;
	}

	public float getAngleOfView_global() {
		return angleOnPlanet + angleOfView;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
