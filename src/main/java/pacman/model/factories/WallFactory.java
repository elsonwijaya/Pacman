package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Concrete renderable factory for Wall objects
 */
public class WallFactory implements RenderableFactory {

    private static final Map<Character, Image> IMAGES = new HashMap<>();

    static {
        try {
            IMAGES.put(RenderableType.HORIZONTAL_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/horizontal.png"))));
            IMAGES.put(RenderableType.VERTICAL_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/vertical.png"))));
            IMAGES.put(RenderableType.UP_LEFT_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/upLeft.png"))));
            IMAGES.put(RenderableType.UP_RIGHT_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/upRight.png"))));
            IMAGES.put(RenderableType.DOWN_LEFT_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/downLeft.png"))));
            IMAGES.put(RenderableType.DOWN_RIGHT_WALL, new Image(Objects.requireNonNull(WallFactory.class.getResourceAsStream("/maze/walls/downRight.png"))));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load wall images", e);
        }
    }

    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;
    private final Image image;

    public WallFactory(char renderableType) {
        this.image = IMAGES.get(renderableType);
        if (this.image == null) {
            throw new IllegalArgumentException("No image found for wall type: " + renderableType);
        }
    }

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    image.getHeight(),
                    image.getWidth()
            );

            return new StaticEntityImpl(
                    boundingBox,
                    layer,
                    image
            );
        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid static entity configuration | %s", e));
        }
    }
}