package tests.tutoren.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import adapter.AdapterMinimal;
import eea.engine.entity.StateBasedEntityManager;
import entities.Projectile;
import spaceapes.Constants;
import spaceapes.SpaceApes;

/**
 * Tests if a given map is correctly created.
 */
public class MinimalTest {
	
	AdapterMinimal adapter;
	Vector2f coordinatesPlanet1 = new Vector2f(-4.0f, 0.0f);
	Vector2f coordinatesPlanet2 = new Vector2f(4.0f, 0.0f);
	float radiusPlanet1 = 1f;
	float radiusPlanet2 = 1f;
	int massPlanet1 = 65;
	int massPlanet2 = 65;
	float angleOnPlanetApe1 = 0f;
	float angleOnPlanetApe2 = 0f;
	Vector2f velocityVector = new Vector2f(0,0);
	int timeDelta = 0;
	
	@Before
	public void setUp() {
		adapter = new AdapterMinimal();
	}
	
	@After
	public void finish() {
		adapter.stopGame();
	}
	
	@Test
	public void testCreateMap() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.stopGame();
	}

	@Test
	public final void testMapEntities() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Incorrect planet count", 2, adapter.getPlanetCount());
		assertEquals("Incorrect ape count", 2, adapter.getApeCount());
		
		assertTrue("StateBasedEntityManager doesnt contain Planet1", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Planet1"));
		assertTrue("StateBasedEntityManager doesnt contain Planet2", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Planet2"));
		assertTrue("StateBasedEntityManager doesnt contain Ape1", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Ape1"));
		assertTrue("StateBasedEntityManager doesnt contain Ape2", StateBasedEntityManager.getInstance().hasEntity(SpaceApes.GAMEPLAY_STATE, "Ape2"));
		
		adapter.stopGame();
	}
	
	
	
	@Test
	public final void testPlanetPositionAndValues() { // belongs to task: "Initialisieren einer simplen Spielwelt"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Incorrect x-coordinate of Planet1", -4.0f, adapter.getPlanetCoordinates(0).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet1", 0.0f, adapter.getPlanetCoordinates(0).y, 0.001f);
		assertTrue("Radius of Planet1 is not greater than " + Constants.MINIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(0) > Constants.MINIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Radius of Planet1 is not smaller than " + Constants.MAXIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(0) < Constants.MAXIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Mass of Planet1 is not greater than " + 0.5f, adapter.getPlanetMass(0) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet1 is not smaller than " + 1000.0f, adapter.getPlanetMass(0) < 1000.0f); //prevent too big masses
		
		assertEquals("Incorrect x-coordinate of Planet2", 4.0f, adapter.getPlanetCoordinates(1).x, 0.001f);
		assertEquals("Incorrect y-coordinate of Planet2", 0.0f, adapter.getPlanetCoordinates(1).y, 0.001f);
		assertTrue("Radius of Planet2 is not greater than " + Constants.MINIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(1) > Constants.MINIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Radius of Planet2 is not smaller than " + Constants.MAXIMUM_RADIUS_PLAYER_PLANET, adapter.getPlanetRadius(1) < Constants.MAXIMUM_RADIUS_PLAYER_PLANET);
		assertTrue("Mass of Planet2 is not greater than " + 0.5f, adapter.getPlanetMass(1) > 0.5f); //prevent division by to small mass
		assertTrue("Mass of Planet2 is not smaller than " + 1000.0f, adapter.getPlanetMass(1) < 1000.0f); //prevent too big masses
		
		adapter.stopGame();
	}
	
	@Test
	public final void testApePositionAndValues() { // belongs to task: "Platzieren der Affen"
		
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		
		assertEquals("Ape1 is not positioned on the surface of Planet1 correctly", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Incorrect rotation of Ape1", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		
		adapter.stopGame();
	}
	
	@Test
	public void testNewGame() { // belongs to task: "Wechsel zwischen Gamestates"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		assertTrue("Game is not in main menu state after initialization", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.MAINMENU_STATE);
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		adapter.handleKeyPressed(0, Input.KEY_ESCAPE);
		assertTrue("Game is not in main menu or highscore state after pressing 'esc' in gameplay state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.HIGHSCORE_STATE);
		adapter.handleKeyPressed(0, Input.KEY_ESCAPE);
		adapter.stopGame();
	}
	
	@Test
	public void testApe1Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe1 = adapter.getApeCoordinates(0);
		while(adapter.getNumberOfLivingApes() > 1) {
			adapter.handleKeyPressed(10, Input.KEY_SPACE);
			Projectile projectileFlying = adapter.getProjectile();
			assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
			adapter.setProjectileCoordinates(projectileFlying, coordinatesApe1);
			adapter.runGame(10);
			adapter.runGame(0); // Game has to be run two times otherwise one update call is missing due to the change of the State
		}
		assertTrue("Game is not in main menu or highscore state after Ape1 is dead!", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.HIGHSCORE_STATE);
		adapter.stopGame();
	}
	
	@Test
	public void testApe2Dead() { // belongs to task: "Spielende"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		while(adapter.getNumberOfLivingApes() > 1) {
			adapter.handleKeyPressed(10, Input.KEY_SPACE);
			Projectile projectileFlying = adapter.getProjectile();
			assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
			adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
			adapter.runGame(10);
			adapter.runGame(0); // Game has to be run two times otherwise one update call is missing due to the change of the State
		}
		assertTrue("Game is not in main menu or highscore state after Ape2 is dead!", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.MAINMENU_STATE || adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.HIGHSCORE_STATE);
		adapter.stopGame();
	}
	
	@Test
	public void testMoveLeft() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		float initAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_LEFT);
		float targetAngleOnPlanet = initAngleOnPlanet - (Constants.APE_MOVMENT_SPEED * SpaceApes.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + initAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing left arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move left with the set speed when pressing left arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving left", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		adapter.stopGame();
	}
	
	@Test
	public void testMoveRight() { // belongs to task: "Affenbewegung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		float originalAngleOnPlanet = adapter.getApeAngleOnPlanet(0);
		adapter.handleKeyDown(1000, Input.KEY_RIGHT);
		float targetAngleOnPlanet = originalAngleOnPlanet + (Constants.APE_MOVMENT_SPEED * SpaceApes.UPDATE_INTERVAL / adapter.getApeDistanceToPlanetCenter(0));
		//System.out.println("initAngleOnPlanet=" + originalAngleOnPlanet + "targetAngleOnPlanet=" + targetAngleOnPlanet + "adapter.getApeDistanceToPlanetCenter(0)=" + adapter.getApeDistanceToPlanetCenter(0));
		assertEquals("The distance to the planet center should not change when pressing right arrow", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("The active ape should move right with the set speed when pressing right arrow", targetAngleOnPlanet, adapter.getApeAngleOnPlanet(0), 0.01f);
		assertEquals("Ape1 is not positioned on the surface of Planet1 after moving right", adapter.getPlanetRadius(0), adapter.getApeDistanceFeetToPlanetCenter(0), 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving right", adapter.getApeAngleOnPlanet(0) + 90f, adapter.getApeRotation(0), 0.001f);
		adapter.stopGame();
	}
	
	@Test
	public void testShootStraight() { // belongs to task: "Schießen entlang einer Geraden"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		Vector2f originalCoordinatesApe = adapter.getApeCoordinates(0);
		float originalRotationApe = adapter.getApeRotation(0);
		float originalGlobalAngleOnPlanet = adapter.getApeGlobalAngleOfView(0);
		double originalAngleInRad = Math.toRadians(originalGlobalAngleOnPlanet);
		//float throwStrenght = adapter.getThrowStrength(0);
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Vector2f projectileCoordinates = adapter.getProjectileCoordinates();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager!", projectileCoordinates!=null);
		
		// the following point is necessary to calculate m and b of the linear Trajectory //TODO: WRONG! Improve this by calculating 2D linear Function
		Vector2f potentialNewPosition = new Vector2f(originalCoordinatesApe.x + 1, (float) (originalCoordinatesApe.y + Math.tan(originalAngleInRad)));
		float m = (originalCoordinatesApe.y - potentialNewPosition.y) / (originalCoordinatesApe.x - potentialNewPosition.x);
		float b = originalCoordinatesApe.y - m * originalCoordinatesApe.x;
		float yDesired = m * projectileCoordinates.x + b;
		assertEquals("The projectile does not follow a linear trajectory", yDesired, projectileCoordinates.y, 0.001f); //TODO: siehe Tablet
		assertEquals("The ape should not move in x direction when space is pressed", originalCoordinatesApe.x, adapter.getApeCoordinates(0).x, 0.001f);
		assertEquals("The ape should not move in y direction when space is pressed", originalCoordinatesApe.y, adapter.getApeCoordinates(0).y, 0.001f);
		assertEquals("Rotation of Ape1 is incorrect after moving right", originalRotationApe, adapter.getApeRotation(0), 0.001f);
		adapter.stopGame();
	}
	
	@Test
	public void testActivePlayer() { // belongs to task: "Spielzug Logik"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		assertTrue("Ape1 should be able to interact before shooting its projectile at the start of the game!", adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact at the start of the game!", !adapter.isApeInteractionAllowed(1));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
		assertTrue("Ape1 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should not be able to interact after Ape1 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		//System.out.println("coordinatesApe2 = " + coordinatesApe2);
		//System.out.println("coordinates Projectile before move" + adapter.getProjectileCoordinates());
		adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
		//System.out.println("coordinates Projectile after move" + adapter.getProjectileCoordinates());
		adapter.runGame(10);
		projectileFlying = adapter.getProjectile();
		assertTrue("There should not be an Entity with ID '" + Constants.PROJECTILE_ID + "' in the EntityManager!", projectileFlying==null);
		assertTrue("Ape1 should not be able to interact after its projectile collided!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape2 should be able to interact after the projectile of Ape1 collided with it!", adapter.isApeInteractionAllowed(1));
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		assertTrue("Ape2 should not be able to interact after shooting its projectile!", !adapter.isApeInteractionAllowed(0));
		assertTrue("Ape1 should not be able to interact after Ape2 shot its projectile which is still flying!", !adapter.isApeInteractionAllowed(1));
		
		projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after Player two hit Space-Key!", projectileFlying!=null);
		adapter.setProjectileCoordinates(projectileFlying, coordinatesPlanet1);
		adapter.runGame(10);
		projectileFlying = adapter.getProjectile();
		assertTrue("There should not be an Entity with ID '" + Constants.PROJECTILE_ID + "' in the EntityManager!", projectileFlying==null);
		assertTrue("Ape2 should not be able to interact after its projectile collided!", !adapter.isApeInteractionAllowed(1));
		assertTrue("Ape1 should be able to interact after the projectile of Ape2 collided with Planet2!", adapter.isApeInteractionAllowed(0));
		
		//TODO: teste alle Fälle durch, wie es sich verhält bei Treffer von eigenem Ape, Treffer von eigenem Planet, Treffer von ..., Schuss außerhalb von der Welt, 2x Space
		
		adapter.stopGame();
	}
	
	@Test
	public void testCollisionWithPlanet() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		Projectile projectileCollisionPlanet1 = adapter.createProjectile(coordinatesPlanet1, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet1 befindet!", adapter.doLinearMovementStep(projectileCollisionPlanet1, timeDelta));
		Projectile projectileCollisionPlanet2 = adapter.createProjectile(coordinatesPlanet2, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Planet2 befindet!", adapter.doLinearMovementStep(projectileCollisionPlanet2, timeDelta));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0), velocityVector);
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Planeten befindet!", !adapter.doLinearMovementStep(projectileNoCollision, timeDelta));
		
		adapter.stopGame();
	}
	
	@Test
	public void testCollisionWithApe() { // belongs to task: "Kollision mit Planeten"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		Vector2f coordinatesApe1 = adapter.getApeCoordinates(0);
		Projectile projectileCollisionApe1 = adapter.createProjectile(coordinatesApe1, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape1 befindet!", adapter.doLinearMovementStep(projectileCollisionApe1, timeDelta));
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		Projectile projectileCollisionApe2 = adapter.createProjectile(coordinatesApe2, velocityVector);
		assertTrue("Es wurde keine Kollision erkannt, obwohl ein Projektil sich innerhalb von Ape2 befindet!", adapter.doLinearMovementStep(projectileCollisionApe2, timeDelta));
		Projectile projectileNoCollision = adapter.createProjectile(new Vector2f(0,0), velocityVector);
		assertTrue("Es wurde eine Kollision erkannt, obwohl kein Projektil sich innerhalb eines Apes befindet!", !adapter.doLinearMovementStep(projectileNoCollision, timeDelta));
		
		adapter.stopGame();
	}

	
	@Test
	public void testApeDamage() { // belongs to task: "Schadensberechnung"
		adapter.initializeGame();
		adapter.createMap(coordinatesPlanet1, coordinatesPlanet2, radiusPlanet1, radiusPlanet2, massPlanet1, massPlanet2, angleOnPlanetApe1, angleOnPlanetApe2);
		assertTrue("The map was not created correctly", adapter.isMapCorrect());
		adapter.handleKeyPressed(0, Input.KEY_N);
		assertTrue("Game is not in gameplay state after pressing 'n' in main menu state", adapter.getStateBasedGame().getCurrentStateID()==SpaceApes.GAMEPLAY_STATE);
		
		int healthApe1 = adapter.getApeHealth(0);
		assertTrue("The health of Ape1 should be 100 at the start of the game!", healthApe1==100);
		int healthApe2 = adapter.getApeHealth(1);
		assertTrue("The health of Ape2 should be 100 at the start of the game!", healthApe2==100);
		
		adapter.handleKeyPressed(10, Input.KEY_SPACE);
		Projectile projectileFlying = adapter.getProjectile();
		assertTrue("No Projectile with ID '" + Constants.PROJECTILE_ID + "' in EntityManager after hitting Space-Key!", projectileFlying!=null);
		Vector2f coordinatesApe2 = adapter.getApeCoordinates(1);
		adapter.setProjectileCoordinates(projectileFlying, coordinatesApe2);
		adapter.runGame(10);
		assertTrue("The health of Ape2 should be lower than 100 after a projectile hit it!", adapter.getApeHealth(1)<100);
		
		//TODO: evtl. abfrage des maximalen Schadens eines Projektils und dann test, ob health nach direktem Treffer geringer...
		
		adapter.stopGame();
	}
	
}