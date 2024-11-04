package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.GhostType;
import pacman.model.entity.dynamic.ghost.strategy.*;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    private static final Image BLINKY_IMAGE = new Image(Objects.requireNonNull(GhostFactory.class.getResourceAsStream("/maze/ghosts/blinky.png")));
    private static final Image INKY_IMAGE = new Image(Objects.requireNonNull(GhostFactory.class.getResourceAsStream("/maze/ghosts/inky.png")));
    private static final Image CLYDE_IMAGE = new Image(Objects.requireNonNull(GhostFactory.class.getResourceAsStream("/maze/ghosts/clyde.png")));
    private static final Image PINKY_IMAGE = new Image(Objects.requireNonNull(GhostFactory.class.getResourceAsStream("/maze/ghosts/pinky.png")));

    private final char ghostType;

    List<Vector2D> targetCorners = Arrays.asList(
            new Vector2D(0, TOP_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP),
            new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)
    );

    public GhostFactory(char ghostType) {
        this.ghostType = ghostType;
    }

    private Image getGhostImage() {
        return switch (ghostType) {
            case RenderableType.BLINKY -> BLINKY_IMAGE;
            case RenderableType.PINKY -> PINKY_IMAGE;
            case RenderableType.INKY -> INKY_IMAGE;
            case RenderableType.CLYDE -> CLYDE_IMAGE;
            default -> throw new IllegalArgumentException("Invalid ghost type");
        };
    }

    private GhostType getGhostType() {
        return switch (ghostType) {
            case RenderableType.BLINKY -> GhostType.BLINKY;
            case RenderableType.PINKY -> GhostType.PINKY;
            case RenderableType.INKY -> GhostType.INKY;
            case RenderableType.CLYDE -> GhostType.CLYDE;
            default -> throw new IllegalArgumentException("Invalid ghost type");
        };
    }

    private Vector2D getTargetCorner() {
        return switch (ghostType) {
            case RenderableType.PINKY -> targetCorners.get(0); //top left
            case RenderableType.BLINKY -> targetCorners.get(1); //top right
            case RenderableType.CLYDE -> targetCorners.get(2); //bottom left
            case RenderableType.INKY -> targetCorners.get(3); //bottom right
            default -> throw new IllegalArgumentException("Invalid ghost type");
        };
    }

    private ChaseStrategy getChaseStrategy() {
        return switch (ghostType) {
            case RenderableType.BLINKY -> new BlinkyChaseStrategy();
            case RenderableType.PINKY -> new PinkyChaseStrategy();
            case RenderableType.INKY -> new InkyChaseStrategy();
            case RenderableType.CLYDE -> new ClydeChaseStrategy();
            default -> throw new IllegalArgumentException("Invalid ghost type");
        };
    }

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            position = position.add(new Vector2D(4, -4));
            Image ghostImage = getGhostImage();

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    ghostImage.getHeight(),
                    ghostImage.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();

            return new GhostImpl(
                    ghostImage,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    getTargetCorner(),
                    getGhostType(),
                    getChaseStrategy());
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
