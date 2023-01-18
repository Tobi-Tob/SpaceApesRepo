package spaceapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;

public class Map {
	public List<Planet> listOfPlanets;
	public List<Ape> listOfApes;
	public Entity background;

	/**
	 * Erzeugt ein leeres Map Objekt. Mit den init-Methoden koennen Entitys der Map
	 * hinzugefuegt werden.
	 */
	public Map() {
		listOfPlanets = new ArrayList<>();
		listOfApes = new ArrayList<>();
		background = new Entity("background"); // Entitaet fuer Hintergrund erzeugen
	}

	public void initBackground() {
		background.setPosition(Utils.toPixelCoordinates(0, 0)); // Startposition des Hintergrunds (Mitte des Fensters)
		try {
			Image image = new Image("/assets/space1.jpg");
			background.addComponent(new ImageRenderComponent(new Image("/assets/space1.jpg")));
			background.setScale((float) Launch.HEIGHT / image.getHeight()); // Skalieren des Hintergrunds
		} catch (SlickException e) {
			System.err.println("Cannot find image for background");
		}
	}

	/**
	 * WICHTIG: Muss ausgefuehrt werden, bevor Affen initalisiert werden
	 */
	public void spawnPlanets(float blackHoleProbability, float antiPlanetProbability) {
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		// Home planets zufaellig auf den Spielfeld Haelften platzieren
		Planet planet1 = new Planet("Planet1", Utils.randomFloat(-xBorder * 0.6f, -xBorder * 0.3f),
				Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f)); // rechte Haelfte
		Planet planet2 = new Planet("Planet2", Utils.randomFloat(xBorder * 0.3f, xBorder * 0.6f),
				Utils.randomFloat(-yBorder * 0.5f, yBorder * 0.5f)); // linke Haelfte
		listOfPlanets.add(planet1); // Speichern in der Planeten Liste des Map Objekts
		listOfPlanets.add(planet2);
		try {
			addRandomImageToPlanet(planet1, false);
			addRandomImageToPlanet(planet2, false);
		} catch (SlickException e) {
			System.err.println("Cannot find image for planet");
		}

		// Versuche Schwarzes Loch zu platzieren
		if (Utils.randomFloat(0, 1) < blackHoleProbability) {
			Vector2f blackHolePosition = findValidePositionForPlanetSpawning(5, 30);
			if (blackHolePosition != null) {
				Planet blackHole = new Planet("BlackHole", blackHolePosition.x, blackHolePosition.y);
				blackHole.changeToBlackHole();
				listOfPlanets.add(blackHole);
			}
		}

		// Versuche Anti Planet zu platzieren
		if (Utils.randomFloat(0, 1) < antiPlanetProbability) {
			Vector2f antiPlanetPosition = findValidePositionForPlanetSpawning(4, 30);
			if (antiPlanetPosition != null) {
				Planet antiPlanet = new Planet("Anti-Planet", antiPlanetPosition.x, antiPlanetPosition.y);
				antiPlanet.changeToAntiPlanet();
				listOfPlanets.add(antiPlanet);
			}
		}

		// Restliche Planeten
		Random r = new Random();
		int morePlanetsToAdd = r.nextInt(4); // 0, 1, 2 oder 3 weitere Planeten
		for (int i = 0; i < morePlanetsToAdd; i++) {
			Vector2f validePosition = findValidePositionForPlanetSpawning(4, 10);
			// Falls keine geeignete Position gefunden wurde, fuege keinen neuen Planeten
			// hinzu
			if (validePosition != null) {
				Planet planet_i = new Planet("Planet" + (i + 3), validePosition.x, validePosition.y);
				listOfPlanets.add(planet_i);
				try {
					addRandomImageToPlanet(planet_i, true);
				} catch (SlickException e) {
					System.err.println("Cannot find image for planet");
				}
			}
		}
	}

	/**
	 * Findet mithilfe von Random-Search einen Koordinaten-Vektor, der weit genug
	 * von allen anderen Planeten entfernt ist
	 * 
	 * @param marginToNextPlanetCenter Gibt Abstand an, wie weit der naechste Planet
	 *                                 mindestens entfernt sein muss
	 * @param iterations               Wie oft soll maximal nach einer gueltigen
	 *                                 Position gesucht werden
	 * @return Vector2f oder null, falls die vorgegebene Anzahl an Iterationen
	 *         ueberschritten wurde
	 */
	private Vector2f findValidePositionForPlanetSpawning(float marginToNextPlanetCenter, int iterations) {
		float xBorder = Utils.worldWidth / 2;
		float yBorder = Utils.worldHeight / 2;
		for (int n = 0; n < iterations; n++) { // Suche so lange wie durch iterations vorgegeben
			Vector2f randomPosition = new Vector2f(Utils.randomFloat(-xBorder * 0.8f, xBorder * 0.8f),
					Utils.randomFloat(-yBorder * 0.7f, yBorder * 0.7f));
			boolean positionIsValide = true;
			// Iteriere ueber alle Planeten
			for (int i = 0; i < listOfPlanets.size(); i++) {
				Planet p_i = listOfPlanets.get(i);
				Vector2f vectorToPlanetCenter = new Vector2f(p_i.getCoordinates().x - randomPosition.x,
						p_i.getCoordinates().y - randomPosition.y);
				// Test ob randomPosition zu nahe am Planeten i liegt (durch Kreisgleichung)
				if (Math.pow(vectorToPlanetCenter.x, 2) + Math.pow(vectorToPlanetCenter.y, 2) < Math
						.pow(marginToNextPlanetCenter, 2)) {
					positionIsValide = false; // Ist dies der Fall, ist die Position ungueltig
					break;
				}
			}
			if (positionIsValide) {
				// java.lang.System.out.println("Planet spawning after: n=" + n);
				return randomPosition; // Wenn gueltige Position gefunden, gib diese zurueck
			}
		}
		// Falls Such-Schleife bis zum Ende durch laeuft:
		// java.lang.System.out.println("Planet spawning after: null");
		return null;
	}

	/**
	 * Fuegt Planeten ein zufaelliges Bild hinzu und skaliert dieses individuell
	 * 
	 * @param planet
	 * @param ringsAllowed true, wenn Planetenbilder mit Ringen verwedet werden
	 *                     duerfen
	 * @throws SlickException
	 */
	private void addRandomImageToPlanet(Planet planet, boolean ringsAllowed) throws SlickException {
		Random r = new Random();
		int imageNumber = r.nextInt(5) + 1; // Integer im Intervall [1, 5]
		if (ringsAllowed) {
			imageNumber = r.nextInt(8) + 1; // Integer im Intervall [1, 8]
		}

		switch (imageNumber) {
		default: // Eqivalent zu case 1
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet1.png")));
			float planetRadiusInPixel = 235;
			float planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 2:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet2.png")));
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 3:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet3.png")));
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 4:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet4.png")));
			planetRadiusInPixel = 242;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 5:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/planet5.png")));
			planetRadiusInPixel = 222;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 6:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet1.png")));
			planetRadiusInPixel = 210;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 7:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet2.png")));
			planetRadiusInPixel = 230;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		case 8:
			planet.addComponent(new ImageRenderComponent(new Image("/assets/ring_planet3.png")));
			planetRadiusInPixel = 245;
			planetRadiusInWorldUnits = Utils.pixelLengthToWorldLength(planetRadiusInPixel);
			planet.setScale(planet.getRadius() / planetRadiusInWorldUnits);
			break;
		}
	}

	/**
	 * Sammelt alle fuer die spaetere Berechnung benoetigten Daten in einer Liste
	 * von Arrays
	 * 
	 * @return Liste mit Planetendaten. Jeder Listeneintrag enthaelt ein Array mit
	 *         Struktur [x, y, mass, radius]
	 */
	public List<float[]> generatePlanetData() {
		List<float[]> planetData = new ArrayList<>();
		for (int i = 0; i < listOfPlanets.size(); i++) {

			Vector2f planetPosition = listOfPlanets.get(i).getCoordinates();
			float planetMass = listOfPlanets.get(i).getMass();
			float planetRadius = listOfPlanets.get(i).getRadius();

			planetData.add(new float[] { planetPosition.x, planetPosition.y, planetMass, planetRadius });
		}
		return planetData;
	}

	/**
	 * WICHTIG: Muss ausgefuehrt werden, nachdem Planeten initalisiert wurden
	 */
	public void initApes(List<Player> listOfAllPlayers) {
		if (listOfPlanets.isEmpty() || listOfAllPlayers.isEmpty()) {
			throw new RuntimeException("initApes failed, one of the required lists is empty");
		}
		// Jeder Spieler in der Liste bekommt seinen eigenen Affen
		for (int i = 0; i < listOfAllPlayers.size(); i++) {
			Ape ape;
			if (i >= listOfPlanets.size()) { // Tritt ein falls weniger Planeten existieren als Spieler
				int randomPlanet = (int) Utils.randomFloat(0, listOfPlanets.size());
				ape = new Ape("ape" + (i + 1), listOfPlanets.get(randomPlanet), listOfAllPlayers.get(i));
			} else {
				ape = new Ape("ape" + (i + 1), listOfPlanets.get(i), listOfAllPlayers.get(i));
			}
			listOfApes.add(ape); // Speichern in der Affen Liste des Map Objekts
			try {
				ape.addComponent(new ImageRenderComponent(new Image("/assets/ape" + (i + 1) + ".png")));
			} catch (SlickException | RuntimeException e) {
				try {
					ape.addComponent(new ImageRenderComponent(new Image("/assets/ape1.png")));
				} catch (SlickException e1) {
					System.err.println("Cannot find image for ape");
				}
			}
		}
	}
}
