package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class BlinkyChaseStrategy implements ChaseStrategy {
    @Override
    public Vector2D getTargetPosition(Ghost ghost) {
        return ghost.getPlayerPosition();
    }
}
