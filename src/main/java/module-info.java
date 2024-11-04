module pacman.game {
    requires java.base;
    requires json.simple;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;

    exports pacman;
    exports pacman.model.engine;
    exports pacman.model.engine.observer;
    exports pacman.model.entity;
    exports pacman.model.entity.dynamic;
    exports pacman.model.entity.dynamic.ghost;
    exports pacman.model.entity.dynamic.ghost.state;
    exports pacman.model.entity.dynamic.ghost.strategy;
    exports pacman.model.entity.dynamic.physics;
    exports pacman.model.entity.dynamic.player;
    exports pacman.model.entity.dynamic.player.observer;
    exports pacman.model.entity.staticentity;
    exports pacman.model.entity.staticentity.collectable;
    exports pacman.model.factories;
    exports pacman.model.level;
    exports pacman.model.level.observer;
    exports pacman.model.maze;
    exports pacman.view;
    exports pacman.view.keyboard;
    exports pacman.view.keyboard.command;
    exports pacman.view.entity;
    exports pacman.view.display;
    exports pacman.view.background;

    opens pacman.model.engine to json.simple;
    opens pacman.model.level to json.simple;
}