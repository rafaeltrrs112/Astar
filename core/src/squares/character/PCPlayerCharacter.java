package squares.character;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.utils.Enums;
import squares.utils.Enums.TravelDirection;
import squares.utils.Enums.UnitMovement;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
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
        this.playerEntity = entity;
        this.transformComponent = entity.getComponent(TransformComponent.class);
        this.playerComponent = entity.getComponent(PlayerComponent.class);
        this.occupyType = occupyType;

        initAllergies();
    }

    @Override
    public boolean Move(UnitMovement direction, Array<Array<Entity>> currentGrid) {
        switch (direction) {
            case North:
                return transformComponent.y < 4 && tryVertical(currentGrid, 1);
            case South:
                return transformComponent.y > 0 && tryVertical(currentGrid, -1);
            case East:
                return transformComponent.x < 9 && tryHorizontal(currentGrid, 1);
            case West:
                return transformComponent.x > 0 && tryHorizontal(currentGrid, -1);
            default:
                throw new IllegalArgumentException(INVALID_MSG_STRING);
        }
    }

    @Override
    public Entity getEntity() {
        return playerEntity;
    }

    @Override
    public Enums.TileTypes occupyType() {
        return occupyType;
    }

    private boolean tryVertical(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.y + increment;
        Entity potentialTile = currentGrid.get((int) nextPosit).get((int) transformComponent.x);
        boolean canMove = canStep(potentialTile);
        if (canMove) {
            transformComponent.y = nextPosit;
            potentialTile.getComponent(TileComponent.class).occupier = playerComponent;
        }
        return canMove;
    }

    private boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.x + increment;

        Entity potentialTile = currentGrid.get((int) transformComponent.y).get((int) nextPosit);
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.x = nextPosit;
        return canMove;
    }

    private boolean canStep(Entity potentialTile) {
        return playerComponent.allergic(potentialTile
                .getComponent(TileComponent.class)
                .getCurrentType());
    }

    @Override
    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }

    @Override
    public Array<Enums.TileTypes> getAllergies() {
        return playerComponent.allergies;
    }

    @Override
    public void castSpell(TravelDirection direction) {
        if (playerComponent.spells.size != 0) {
            Entity spellEnt = playerComponent.spells.removeFirst();
            Spell nextSpell = spellM.get(spellEnt);
            TransformComponent spellTransform = transformMapper.get(spellEnt);
            spellTransform.x = transformComponent.x;
            spellTransform.y = transformComponent.y;
            nextSpell.isActive = true;
            nextSpell.direction = direction;
        }
    }

    private void initAllergies() {
        playerComponent.allergies.addAll(Enums.TileTypes.VioletPlayerOccupied);
        playerComponent.allergies.addAll(Enums.TileTypes.CoralPlayerOccupied);
    }

}
