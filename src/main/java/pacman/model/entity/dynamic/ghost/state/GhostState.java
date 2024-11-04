package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface GhostState {
    void enterState(Ghost ghost);
    void handlePowerPelletEaten(Ghost ghost);
    void handleFrightenedDone(Ghost ghost);
    void handleStateChange(Ghost ghost);
}
