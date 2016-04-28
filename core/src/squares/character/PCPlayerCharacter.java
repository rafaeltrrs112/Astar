package squares.character;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;
import squares.components.Enums.UnitMovement;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.components.spells.Spell;

/**
 * Main player character object wrapper.
 */
public class PCPlayerCharacter implements CharacterEntity {
    private final PlayerComponent playerComponent;
    private final TransformComponent transformComponent;

    private final Entity playerEntity;
    private final Enums.TileTypes occupyType;

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<Spell> spellM = ComponentMapper.getFor(Spell.class);

    private static final String INVALID_MSG_STRING = "Invalid direction";

    public PCPlayerCharacter(Entity entity, Enums.TileTypes occupyType) {
        playerEntity = entity;
        this.transformComponent = entity.getComponent(TransformComponent.class);
        this.playerComponent = entity.getComponent(PlayerComponent.class);
        this.occupyType = occupyType;
    }

    public boolean Move(UnitMovement direction, Array<Array<Entity>> currentGrid) {
        switch (direction) {
            case North:
                return transformComponent.getPosition().y < 4 && tryVertical(currentGrid, 1);
            case South:
                return transformComponent.getPosition().y > 0 && tryVertical(currentGrid, -1);
            case East:
                return transformComponent.getPosition().x < 9 && tryHorizontal(currentGrid, 1);
            case West:
                return transformComponent.getPosition().x > 0 && tryHorizontal(currentGrid, -1);
            default:
                throw new IllegalArgumentException(INVALID_MSG_STRING);
        }
    }

    private boolean tryVertical(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().y + increment;
        Entity potentialTile = currentGrid.get((int) nextPosit).get((int) transformComponent.x());
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().y = nextPosit;
        return canMove;
    }

    private boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().x + increment;
        Entity potentialTile = currentGrid.get((int) transformComponent.y()).get((int) nextPosit);
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().x = nextPosit;
        return canMove;
    }

    private boolean canStep(Entity potentialTile) {
        return playerComponent.allergic(potentialTile
                .getComponent(TileComponent.class)
                .getCurrentType());
    }

    public Entity getEntity() {
        return playerEntity;
    }

    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }

    public Enums.TileTypes occupyType() {
        return occupyType;
    }

    public void castSpell() {
        if (playerComponent.spells.size != 0) {
            Entity spellEnt = playerComponent.spells.removeFirst();
            Spell nextSpell = extractSpellComponent(spellEnt);
            TransformComponent spellTransform = transformMapper.get(spellEnt);
            spellTransform.setPosition(new Vector2(transformComponent.x(), transformComponent.y()));
            nextSpell.isActive = true;
        }
    }

    private Spell extractSpellComponent(Entity entity) {
        return spellM.get(entity);
    }

}
