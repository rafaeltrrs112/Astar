package squares.handler.enemy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import squares.character.CharacterEntity;
import squares.components.TileComponent;
import squares.components.spells.Spell;
import squares.handler.KeyMap;
import squares.handler.UserInputHandler;
import squares.utils.Enums;

/**
 */
abstract public class EnemyHandler extends UserInputHandler {
    private float timePassed = 0f;
    private Enums.UnitMovement movementDirection = Enums.UnitMovement.North;
    protected final Array<Enums.TileTypes> blasterTypes = Array.with(Enums.TileTypes.BlasterOneOccupied, Enums.TileTypes.BlasterTwoOccupied, Enums.TileTypes.BlasterThreeOccupied);
    private boolean hasAttacked = false;

    public EnemyHandler(CharacterEntity playerCharacter, Array<Array<Entity>> gridField) {
        super(playerCharacter, gridField, KeyMap.BLUE_DEFAULT());
    }

    @Override
    public final void handle(float delta) {
        timePassed += delta;

        if (timePassed >= 1) {
            timePassed = 0;

            movementDirection = movementPattern();

            moveCharacter(movementDirection);

            setTile();
            hasAttacked = false;
        }

        if (timePassed <= .80f && hasSpells() && !hasAttacked) {
            shuffleSpells();
            hasAttacked = attackPattern();
        } else if (timePassed > .80f) {
            hasAttacked = true;
        }
    }

    abstract Enums.UnitMovement movementPattern();

    abstract boolean attackPattern();

    protected final boolean comradeInLane() {
        Array<Entity> lane = gridField.get((int) transformComponent.y);
        for (Entity e : lane) {
            TileComponent tileComponent = e.getComponent(TileComponent.class);
            if (tileComponent.isOccupied() && tileComponent.occupier.key.occupyType != Enums.TileTypes.GreenPlayerOccupied && tileComponent.occupier.key.occupyType != getCharacter().occupyType()) {
                System.out.println(tileComponent.occupier.key.occupyType);
                if (lane.indexOf(e, true) < transformComponent.x) return true;
            } else if (tileComponent.isOccupied()) {
                System.out.println(tileComponent.occupier.key.occupyType);

            }
        }
        return false;
    }


    public final Enums.UnitMovement currentDirection() {
        return movementDirection;
    }

    public final boolean playerInLane() {
        Array<Entity> lane = gridField.get((int) transformComponent.y);
        for (Entity tile : lane) {
            TileComponent tileComp = tile.getComponent(TileComponent.class);
            if (tileComp.isOccupied() && tileComp.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied) {
                return true;
            }
        }
        return false;
    }

    public final boolean playerInTopLane() {
        Array<Entity> lane = gridField.get(4);
        for (Entity tile : lane) {
            TileComponent tileComp = tile.getComponent(TileComponent.class);
            if (tileComp.isOccupied() && tileComp.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied) {
                return true;
            }
        }
        return false;
    }

    public final boolean playerInBetweenLanes() {
        Array<Entity> rangeTiles = new Array<>();

        for (int i = (int) transformComponent.y; i < gridField.size; i++) {
            rangeTiles.add(gridField.get(i).get(0));
        }

        for (Entity tile : rangeTiles) {
            TileComponent tileComp = tile.getComponent(TileComponent.class);
            if (tileComp.isOccupied() && tileComp.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied) {
                return true;
            }
        }
        return false;
    }

    public final boolean playerInSSwordRange() {
        int forwardColumn = (int) transformComponent.x - 1;
        for (Array<Entity> entityArray : gridField) {
            if (tileMapper.get(entityArray.get(forwardColumn)).isOccupied() && tileMapper.get(entityArray.get(forwardColumn)).occupier.key.occupyType== Enums.TileTypes.GreenPlayerOccupied)
                return true;
        }
        return false;
    }

    public final Spell nextSpell() {
        return getCharacter().getPlayerComponent().peekSpell().getComponent(Spell.class);
    }

    public final void cast() {
        getCharacter().castSpell(Enums.TravelDirection.LEFT);
    }

    public final boolean playerStunned(){
        for(Array<Entity> lane: gridField){
            for(Entity e: lane){
                TileComponent tileComponent = tileMapper.get(e);
                if(tileComponent.isOccupied() && tileComponent.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied &&
                        tileComponent.occupier.key.stunned()) return true;
            }
        }
        return false;
    }
}
