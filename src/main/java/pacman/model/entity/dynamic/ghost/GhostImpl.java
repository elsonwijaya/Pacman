package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.state.ChaseState;
import pacman.model.entity.dynamic.ghost.state.FrightenedState;
import pacman.model.entity.dynamic.ghost.state.GhostState;
import pacman.model.entity.dynamic.ghost.state.ScatterState;
import pacman.model.entity.dynamic.ghost.strategy.ChaseStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private final Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds = new HashMap<>();
    private int currentDirectionCount = 0;

    //StatePattern
    private GhostState currentState;
    private double defaultSpeed = 0.0;

    //StrategyPattern
    private final GhostType ghostType;
    private Direction playerDirection;
    private static Ghost blinky;
    private final ChaseStrategy chaseStrategy;

    //PowerPellet Decorator
    private static final Image FRIGHTENED_IMAGE = new Image("maze/ghosts/frightened.png");
    private static final int RESPAWN_DELAY = 30; //same with fps
    private int respawnTime = 0;
    private boolean isRespawning = false;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState,
                     GhostMode ghostMode, Vector2D targetCorner, GhostType ghostType, ChaseStrategy chaseStrategy) {
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        //this.targetLocation = getTargetLocation();
        this.currentDirection = null;

        //StatePattern
        this.kinematicState.setSpeed(defaultSpeed);
        switch (this.ghostMode) {
            case CHASE -> this.currentState = new ChaseState();
            case SCATTER -> this.currentState = new ScatterState();
            case FRIGHTENED -> this.currentState = new FrightenedState();
        };

        this.currentState.enterState(this);
        this.targetLocation = getTargetLocation();

        //StrategyPattern
        this.ghostType = ghostType;
        if (ghostType == GhostType.BLINKY) {
            blinky = this;
        }
        this.chaseStrategy = chaseStrategy;
    }

    @Override
    public void setGhostState(GhostState newState) {
        this.currentState = newState;
        this.currentState.enterState(this);
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    public Image getImage() {
        if (ghostMode == GhostMode.FRIGHTENED) {
            return FRIGHTENED_IMAGE;
        } else {
            return image;
        }
    }

    @Override
    public void update() {
        if (isRespawning) { //check if ghost is respawning
            respawnTime++; // increment timer for counting up to 1 second
            if (respawnTime >= RESPAWN_DELAY) { // if it is greater than the 1 second limit defined
                //reset flag so ghost will move again
                isRespawning = false;
                respawnTime = 0;
                return;
            }
            return;
        }
        //allow ghost to move
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = getTargetLocation();
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    private Vector2D getTargetLocation() {
        /*
        return switch (this.ghostMode) {
            case CHASE -> this.playerPosition;
            case SCATTER -> this.targetCorner;
        };*/
        return switch (this.ghostMode) {
            case CHASE -> this.chaseStrategy.getTargetPosition(this);
            case SCATTER -> this.targetCorner;
            case FRIGHTENED -> this.getPosition();
        };
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        if (ghostMode == GhostMode.FRIGHTENED) {
            Set<Direction> newPossibleDirections = new HashSet<>();
            for (Direction direction : possibleDirections) {
                if (currentDirection == null || direction != currentDirection.opposite()) {
                    newPossibleDirections.add(direction);
                }
            }
            if (newPossibleDirections.isEmpty()) {
                return currentDirection.opposite();
            }
            List<Direction> newDirectionList = new ArrayList<>(newPossibleDirections);
            return newDirectionList.get(new Random().nextInt(newDirectionList.size()));
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        if (!speeds.isEmpty() && speeds.containsKey(ghostMode)) {
            this.kinematicState.setSpeed(speeds.get(ghostMode));
        }
        //this.kinematicState.setSpeed(speeds.get(ghostMode));

        // ensure direction is switched
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            if (ghostMode == GhostMode.FRIGHTENED) {
                level.handleGhostEaten();
                reset();
            } else {
                level.handleLoseLife();
            }
        }
    }

    @Override
    public void update(Vector2D playerPosition, Direction playerDirection) {
        this.playerPosition = playerPosition;
        this.playerDirection = playerDirection;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        if (ghostMode == GhostMode.FRIGHTENED) {
            // return ghost to starting position
            this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .build();
            this.boundingBox.setTopLeft(startingPosition);
            this.ghostMode = GhostMode.SCATTER;
            this.currentDirectionCount = minimumDirectionCount;
            this.currentState = new ScatterState();
            this.currentState.enterState(this);
            //Start timer 1 second before moving again
            isRespawning = true;
            respawnTime = 0;
        } else {
            // return ghost to starting position
            this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .build();
            this.boundingBox.setTopLeft(startingPosition);
            this.ghostMode = GhostMode.SCATTER;
            this.currentDirectionCount = minimumDirectionCount;
            this.currentState = new ScatterState();
            this.currentState.enterState(this);
        }
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public Vector2D getTargetCorner() {
        return targetCorner;
    }

    @Override
    public Vector2D getPlayerPosition() {
        return playerPosition;
    }

    @Override
    public Direction getPlayerDirection() {
        return playerDirection;
    }

    @Override
    public Vector2D getBlinkyPosition() {
        if (blinky == null) {
            return blinky.getPosition();
        }
        return new Vector2D(448,0);
    }

    @Override
    public GhostType getGhostType() {
        return ghostType;
    }

    @Override
    public void handlePowerPelletEaten() {
        currentState.handlePowerPelletEaten(this);
    }

    @Override
    public void handleFrightenedDone() {
        currentState.handleFrightenedDone(this);
    }

    @Override
    public void handleStateChange() {
        currentState.handleStateChange(this);
    }
}
