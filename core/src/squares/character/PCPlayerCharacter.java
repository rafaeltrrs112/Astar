package squares.character;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import squares.components.Enums;
import squares.components.Enums.UnitMovement;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;
import squares.components.spells.Blaster;
import squares.components.spells.Spell;

import static com.badlogic.gdx.Gdx.input;

/**
 * Main player character object wrapper.
 */
public class PCPlayerCharacter implements CharacterEntity {
    private final PlayerComponent playerComponent;
    private final Entity playerEntity;
    private final TransformComponent transformComponent;
    private final Enums.TileTypes occupyType;
    private final ComponentMapper<? extends Spell> spellMapper = ComponentMapper.getFor(Spell.class);
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);


    public PCPlayerCharacter(Entity entity, Enums.TileTypes occupyType) {
        playerEntity = entity;
        this.transformComponent = entity.getComponent(TransformComponent.class);
        this.playerComponent = entity.getComponent(PlayerComponent.class);
        this.occupyType = occupyType;
    }

    public boolean Move(UnitMovement direction, Array<Array<Entity>> currentGrid) {
        System.out.println("Main player move");
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
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    public boolean tryVertical(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().y + increment;
        Entity potentialTile = currentGrid.get((int) nextPosit).get((int) transformComponent.x());
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().y = nextPosit;
        return canMove;
    }

    public boolean tryHorizontal(Array<Array<Entity>> currentGrid, int increment) {
        float nextPosit = transformComponent.getPosition().x + increment;
        Entity potentialTile = currentGrid.get((int) transformComponent.y()).get((int) nextPosit);
        boolean canMove = canStep(potentialTile);
        if (canMove) transformComponent.getPosition().x = nextPosit;
        return canMove;
    }

    public boolean canStep(Entity potentialTile) {
        return playerComponent.allergic(potentialTile
                .getComponent(TileComponent.class)
                .getCurrentType());
    }

    @Override
    public boolean actOnGrid(Array<Array<Entity>> gridField) {
        if (input.isKeyJustPressed(Input.Keys.W)) {
            return Move(UnitMovement.North, gridField);
        } else if (input.isKeyJustPressed(Input.Keys.A)) {
            return Move(UnitMovement.West, gridField);
        } else if (input.isKeyJustPressed(Input.Keys.S)) {
            return Move(UnitMovement.South, gridField);
        } else
            return input.isKeyJustPressed(Input.Keys.D) && Move(UnitMovement.East, gridField);
    }

    @Override
    public Entity getEntity() {
        return playerEntity;
    }

    public TransformComponent getTransformComponent() {
        return transformComponent;
    }
    public PlayerComponent getPlayerComponent() {
        return playerComponent;
    }

    @Override
    public Enums.TileTypes occupyType() {
        return occupyType;
    }

    public void castSpell(){
        if(playerComponent.spells.size != 0){
            Entity spellEnt = playerComponent.spells.removeFirst();
            Spell nextSpell = spellEnt.getComponent(Blaster.class);
            TransformComponent spellTransform = transformMapper.get(spellEnt);
            spellTransform.setPosition(transformComponent.x(), transformComponent.y());
            nextSpell.isActive = true;
        }
    }
}
