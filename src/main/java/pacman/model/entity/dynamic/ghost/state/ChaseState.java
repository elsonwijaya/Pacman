package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ChaseState implements GhostState {
    @Override
    public void enterState(Ghost ghost) {
        ghost.setGhostMode(GhostMode.CHASE);
    }

    @Override
    public void handlePowerPelletEaten(Ghost ghost) {
        ghost.setGhostState(new FrightenedState());
    }

    @Override
    public void handleFrightenedDone(Ghost ghost) {
        //None
    }

    @Override
    public void handleStateChange(Ghost ghost) {
        ghost.setGhostState(new ScatterState());
    }
}
