package actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;
import entities.Ape;
import map.Map;

public class MoveOnPlanetAction implements Action {

	private float direction; // Bewegung nach links -1 und Bewegung nach rechts +1

	public MoveOnPlanetAction(float direction) {
		this.direction = direction;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Ape ape = Map.getInstance().getActiveApe();
		if (ape.isInteractionAllowed()) {
			ape.stepOnPlanet(direction);
		}
	}

}
