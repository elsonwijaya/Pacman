package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;

public abstract class PelletDecorator extends StaticEntityImpl implements Collectable {
    protected final Collectable decoratedPellet;
    protected boolean isCollectable;

    public PelletDecorator(Collectable pellet, BoundingBox boundingBox, Layer layer, Image image) {
        super(boundingBox, layer, image);
        this.decoratedPellet = pellet;
        this.isCollectable = true;
    }

    @Override
    public void collect() {
        decoratedPellet.collect();
        this.isCollectable = false;
        setLayer(Layer.INVISIBLE);
    }

    @Override
    public void reset() {
        decoratedPellet.reset();
        this.isCollectable = true;
        setLayer(Layer.BACKGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable &&decoratedPellet.isCollectable();
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return decoratedPellet.getPoints();
    }

}
