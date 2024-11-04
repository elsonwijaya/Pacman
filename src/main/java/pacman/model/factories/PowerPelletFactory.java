package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PowerPelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private static final int NUM_POINTS = 50;
    private static final double SIZE_MULTIPLIER = 2.0;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            position = position.add(new Vector2D(-8, -8));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    PELLET_IMAGE.getHeight() * SIZE_MULTIPLIER,
                    PELLET_IMAGE.getWidth() * SIZE_MULTIPLIER
            );

            Pellet pellet = new Pellet(boundingBox, layer, PELLET_IMAGE, 10);

            return new PowerPellet(
                    pellet,
                    boundingBox,
                    layer,
                    PELLET_IMAGE,
                    NUM_POINTS
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid power pellet configuration | %s", e));
        }
    }
}
