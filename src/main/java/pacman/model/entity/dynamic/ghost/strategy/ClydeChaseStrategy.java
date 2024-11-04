package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeChaseStrategy implements ChaseStrategy {
    private static final int CHASE_LIMIT = 8 * 16;

    @Override
    public Vector2D getTargetPosition(Ghost ghost) {
        Vector2D playerPosition = ghost.getPlayerPosition();
        Vector2D clydePosition = ghost.getPosition();

        double distance = Vector2D.calculateEuclideanDistance(clydePosition, playerPosition);

        if (distance < CHASE_LIMIT) {
            return playerPosition;
        } else {
            return ghost.getTargetCorner();
        }
    }
}
