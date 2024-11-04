package pacman.view.display;

import javafx.scene.Node;
import javafx.scene.text.Font;
import pacman.model.engine.observer.GameState;
import pacman.model.engine.observer.GameStateObserver;
import pacman.model.level.observer.LevelStateObserver;
import pacman.view.GameWindow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages the display nodes for Pac-Man
 */
public class DisplayManager implements LevelStateObserver, GameStateObserver {

    private final ScoreDisplay scoreDisplay;
    private final GameStateDisplay gameStatusDisplay;
    private final NumLivesDisplay numLivesDisplay;

    public DisplayManager() {
        Font font;
        try {
            InputStream fontStream = Objects.requireNonNull(
                    DisplayManager.class.getResourceAsStream("/maze/PressStart2P-Regular.ttf"),
                    "Font file not found"
            );
            font = Font.loadFont(fontStream, 16);
            if (font == null) {
                System.out.println("Warning: Failed to load custom font, falling back to default");
                font = new Font(16);
            }
        } catch (Exception e) {
            System.out.println("Warning: Failed to load custom font: " + e.getMessage());
            font = new Font(16);
        }

        this.scoreDisplay = new ScoreDisplay(font);
        this.gameStatusDisplay = new GameStateDisplay(font);
        this.numLivesDisplay = new NumLivesDisplay();
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(scoreDisplay.getNode());
        nodes.add(gameStatusDisplay.getNode());
        nodes.add(numLivesDisplay.getNode());
        return nodes;
    }

    @Override
    public void updateNumLives(int numLives) {
        numLivesDisplay.update(numLives);
    }

    @Override
    public void updateScore(int scoreChange) {
        scoreDisplay.update(scoreChange);
    }

    @Override
    public void updateGameState(GameState gameState) {
        gameStatusDisplay.update(gameState);
    }
}