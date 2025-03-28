package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PacmanFactory implements RenderableFactory {
    private static final Image playerLeftImage = new Image(Objects.requireNonNull(PacmanFactory.class.getResourceAsStream("/maze/pacman/playerLeft.png")));
    private static final Image playerRightImage = new Image(Objects.requireNonNull(PacmanFactory.class.getResourceAsStream("/maze/pacman/playerRight.png")));
    private static final Image playerUpImage = new Image(Objects.requireNonNull(PacmanFactory.class.getResourceAsStream("/maze/pacman/playerUp.png")));
    private static final Image playerDownImage = new Image(Objects.requireNonNull(PacmanFactory.class.getResourceAsStream("/maze/pacman/playerDown.png")));
    private static final Image playerClosedImage = new Image(Objects.requireNonNull(PacmanFactory.class.getResourceAsStream("/maze/pacman/playerClosed.png")));

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            Map<PacmanVisual, Image> images = new HashMap<>();
            images.put(PacmanVisual.UP, playerUpImage);
            images.put(PacmanVisual.DOWN, playerDownImage);
            images.put(PacmanVisual.LEFT, playerLeftImage);
            images.put(PacmanVisual.RIGHT, playerRightImage);
            images.put(PacmanVisual.CLOSED, playerClosedImage);

            Image currentImage = playerLeftImage;
            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    currentImage.getHeight(),
                    currentImage.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .setDirection(Direction.LEFT)
                    .build();

            return new Pacman(
                    currentImage,
                    images,
                    boundingBox,
                    kinematicState
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid dynamic entity configuration | %s", e));
        }
    }
}