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

import java.awt.*;

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

    private static final String INVALID_MSG_STRING = "Invalid DIRECTION";

    public PCPlayerCharacter(Entity entity, Enums.TileTypes occupyType) {
        this.playerEntity = entity;
        this.transformComponent = entity.getComponent(TransformComponent.class);
        this.playerComponent = entity.getComponent(PlayerComponent.class);
        this.occupyType = occupyType;
        playerComponent.spellCap = 5;

        initAllergies();
    }

    @Override
    public boolean Move(UnitMovement direction, Array<Array<Entity>> currentGrid) {
        boolean willMove = false;
        Point previous = new Point((int) transformComponent.x, (int) transformComponent.y);

        switch (direction) {
            case North:
                willMove = transformComponent.y < 4 && tryVertical(currentGrid, 1);
                break;
            case South:
                willMove = transformComponent.y > 0 && tryVertical(currentGrid, -1);
                break;
            case East:
                willMove = transformComponent.x < 9 && tryHorizontal(currentGrid, 1);
                break;
            case West:
                willMove = transformComponent.x > 0 && tryHorizontal(currentGrid, -1);
                break;
            case Stay:
                break;
            default:
                throw new IllegalArgumentException(INVALID_MSG_STRING);
        }

        if (willMove) {
            TileComponent t = currentGrid.get(previous.y).get(previous.x).getComponent(TileComponent.class);
            t.occupier = null;
        }

        return willMove;
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
        float nextYPosit = transformComponent.y + increment;

        Entity potentialTile = currentGrid.get((int) nextYPosit).get((int) transformComponent.x);
        boolean canMove = canStep(potentialTile);
        if (canMove) {
            transformComponent.y = nextYPosit;
            if (!willCollide(transformComponent.x, nextYPosit, currentGrid))
                potentialTile.getComponent(TileComponent.class).setOccupier(playerComponent, transformComponent);
        }
        return canMove && !willCollide(transformComponent.x, nextYPosit, currentGrid);
    }

    private boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment) {
        float nextXPosit = transformComponent.x + increment;
        Entity potentialTile = currentGrid.get((int) transformComponent.y).get((int) nextXPosit);
        boolean canMove = canStep(potentialTile);
        if (canMove) {
            transformComponent.x = nextXPosit;
            if (!willCollide(nextXPosit, transformComponent.y, currentGrid))
                potentialTile.getComponent(TileComponent.class).setOccupier(playerComponent, transformComponent);
        }
        return canMove && !willCollide(nextXPosit, transformComponent.y, currentGrid);
    }

    private boolean willCollide(float x, float y, Array<Array<Entity>> gridField) {
        Entity targetTile = gridField.get((int) y).get((int) x);
        TileComponent targetTileComponent = targetTile.getComponent(TileComponent.class);
        return targetTileComponent.isOccupied();
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
        if (playerComponent.hasSpells()) {
            Entity spellEnt = playerComponent.popSpell();
            Spell nextSpell = spellM.get(spellEnt);
            TransformComponent spellTransform = transformMapper.get(spellEnt);
            spellTransform.x = transformComponent.x;
            spellTransform.y = transformComponent.y;
            nextSpell.isActive = true;
            nextSpell.setOriginalDirection(direction);
            nextSpell.setDirection(direction);

        }
    }

    private void initAllergies() {
        playerComponent.allergies.addAll(Enums.TileTypes.VioletPlayerOccupied);
        playerComponent.allergies.addAll(Enums.TileTypes.CoralPlayerOccupied);
    }

}
