package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyChaseStrategy implements ChaseStrategy {
    private static final int GRID_SPACES_AHEAD = 2;
    private static final int GRID_SIZE = 16;

    @Override
    public Vector2D getTargetPosition(Ghost ghost) {
        Vector2D playerPosition = ghost.getPlayerPosition();
        Direction playerDirection = ghost.getPlayerDirection();
        Vector2D blinkyPosition = ghost.getBlinkyPosition();

        double relativeX = playerPosition.getX();
        double relativeY = playerPosition.getY();

        switch (playerDirection) {
            case UP -> relativeY -= GRID_SPACES_AHEAD * GRID_SIZE;
            case DOWN -> relativeY += GRID_SPACES_AHEAD * GRID_SIZE;
            case LEFT -> relativeX -= GRID_SPACES_AHEAD * GRID_SIZE;
            case RIGHT -> relativeX += GRID_SPACES_AHEAD * GRID_SIZE;
        }

        double targetX = (relativeX - blinkyPosition.getX()) * 2;
        double targetY = (relativeY - blinkyPosition.getY()) * 2;

        return new Vector2D(blinkyPosition.getX() + targetX, blinkyPosition.getY() + targetY);
    }
}
