package spaceapes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.action.Action;
import eea.engine.component.Component;

public class MoveRightOnPlanetAction implements Action {

	private boolean playerInteractionAllowed;
	private Player activePlayer;
	
	public MoveRightOnPlanetAction(boolean playerInteractionAllowed, Player activePlayer) {
		this.playerInteractionAllowed = playerInteractionAllowed;
		this.activePlayer = activePlayer;
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2, Component arg3) {
		if (playerInteractionAllowed) {
			activePlayer.getApe().stepOnPlanet(1); // 1 = rechts rum
		}
	}

}
