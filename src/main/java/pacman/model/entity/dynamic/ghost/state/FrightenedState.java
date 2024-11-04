package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Vector2D;

public class FrightenedState implements GhostState {

    @Override
    public void enterState(Ghost ghost) {
        ghost.setGhostMode(GhostMode.FRIGHTENED);
    }

    @Override
    public void handlePowerPelletEaten(Ghost ghost) {
        ghost.setGhostState(new FrightenedState());
    }

    @Override
    public void handleFrightenedDone(Ghost ghost) {
        ghost.setGhostState(new ScatterState());
    }

    @Override
    public void handleStateChange(Ghost ghost) {
        //No mode changes while frightened
    }
}
