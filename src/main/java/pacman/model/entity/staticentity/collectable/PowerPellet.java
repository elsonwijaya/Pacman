package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;

/**
 * Represents the PowerPellet in Pac-Man game
 */
public class PowerPellet extends PelletDecorator {

    private final int points;

    public PowerPellet(Collectable pellet, BoundingBox boundingBox, Layer layer, Image image, int points) {
        super(pellet, boundingBox, layer, image);
        this.points = points;
    }

    @Override
    public void collect() {
        if (isCollectable()) {
            super.collect();
        }
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public int getPoints() {
        return this.points;
    }

}
