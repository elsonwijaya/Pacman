package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyChaseStrategy implements ChaseStrategy {
    private static final int GRID_SPACES_AHEAD = 4;
    private static final int GRID_SIZE = 16;

    @Override
    public Vector2D getTargetPosition(Ghost ghost) {
        Vector2D playerPosition = ghost.getPlayerPosition();
        Direction playerDirection = ghost.getPlayerDirection();

        double targetX = playerPosition.getX();
        double targetY = playerPosition.getY();

        switch (playerDirection) {
            case UP -> targetY -= GRID_SPACES_AHEAD * GRID_SIZE;
            case DOWN -> targetY += GRID_SPACES_AHEAD * GRID_SIZE;
            case LEFT -> targetX -= GRID_SPACES_AHEAD * GRID_SIZE;
            case RIGHT -> targetX += GRID_SPACES_AHEAD * GRID_SIZE;
        }

        return new Vector2D(targetX, targetY);
    }
}
