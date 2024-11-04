package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.factories.RenderableFactoryRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class MazeCreator {

    public static final int RESIZING_FACTOR = 16;
    private final String mapPath;
    private final RenderableFactoryRegistry renderableFactoryRegistry;

    public MazeCreator(String mapPath,
                       RenderableFactoryRegistry renderableFactoryRegistry) {
        this.mapPath = mapPath;
        this.renderableFactoryRegistry = renderableFactoryRegistry;
    }

    public Maze createMaze() {
        Maze maze = new Maze();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(
                                    getClass().getResourceAsStream(mapPath),
                                    "Map file not found: " + mapPath
                            )
                    )
            );

            int y = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                char[] row = line.toCharArray();

                for (int x = 0; x < row.length; x++) {
                    Vector2D position = new Vector2D(x * RESIZING_FACTOR, y * RESIZING_FACTOR);

                    char renderableType = row[x];
                    Renderable renderable = renderableFactoryRegistry.createRenderable(
                            renderableType, position
                    );

                    maze.addRenderable(renderable, renderableType, x, y);
                }

                y += 1;
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading maze file: " + e.getMessage());
            System.exit(0);
        }

        return maze;
    }
}