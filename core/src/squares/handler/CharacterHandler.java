package squares.handler;

import squares.character.CharacterEntity;

/**
 * A character handler interface
 */
public interface CharacterHandler {
    void handle(float delta);
    CharacterEntity getCharacter();
}
