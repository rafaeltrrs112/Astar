package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.spells.Spell;

/**
 * Weapons system handler.
 */
public class WeaponSystem extends IteratingSystem {
    private ComponentMapper<Spell> spellM = ComponentMapper.getFor(Spell.class);

    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TileComponent> tileM = ComponentMapper.getFor(TileComponent.class);
    private Array<Array<Entity>> gridField;

    public WeaponSystem(Array<Array<Entity>> gridField) {
        super(Family.all(TransformComponent.class, Spell.class).get());
        this.gridField = gridField;
    }

    private void process(TransformComponent transformComponent, Spell spellRep, float delta) {
        switch (spellRep.occupyEffect) {
            case SwordOccupied:
                if (spellRep.isActive) handleSwordType(spellRep, transformComponent, delta);
                break;
            case BlasterOccupied:
                if (spellRep.isActive) handleBlasterType(spellRep, transformComponent);
                break;
            default:
                System.out.println("Not applicable!");
        }
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        process(transformM.get(entity), spellM.get(entity), delta);
    }

    private void handleSwordType(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int inc = spellRep.direction.increment;

        float leftSwipeY = transformComponent.y + inc;
        float leftSwipeX = transformComponent.x + inc;

        float rightSwipeY = transformComponent.y - inc;
        float rightSwipeX = transformComponent.x + inc;

        float playerFrontY = transformComponent.y;
        float playerFrontX = transformComponent.x + inc;

        if (spellRep.isActive) {
            fillSwordFields(leftSwipeX, leftSwipeY);
            fillSwordFields(rightSwipeX, rightSwipeY);
            fillSwordFields(playerFrontX, playerFrontY);
        } else {
            resetSwordFields(leftSwipeX, leftSwipeY);
            resetSwordFields(rightSwipeX, rightSwipeY);
            resetSwordFields(playerFrontX, playerFrontY);
        }

    }

    private void handleBlasterType(Spell spellRep, TransformComponent transformComponent) {

        float nextXPosit = transformComponent.x + spellRep.direction.increment;
        TileComponent prevTileComp = tileM.get(gridField.get((int) transformComponent.y).get((int) transformComponent.x));

        if (prevTileComp.getCurrentType() == Enums.TileTypes.BlasterOccupied) {
            prevTileComp.setCurrentType(prevTileComp.getDefaultType());
        }

        if (!(nextXPosit > 9 || nextXPosit < 0)) {
            transformComponent.x = nextXPosit;

            Entity targetTile = gridField.get((int) transformComponent.y).get((int) transformComponent.x);

            TileComponent tileComp = tileM.get(targetTile);
            if(!tileComp.isOccupied()){
                tileComp.setCurrentType(Enums.TileTypes.BlasterOccupied);
            } else {
                tileComp.occupier.health -= spellRep.damage;
                System.out.println("Enemy health is now " + tileComp.occupier.health);
                System.out.println("Collision!");
                transformComponent.x = 0;
                transformComponent.y = 0;
                spellRep.isActive = false;
            }
        } else {
            transformComponent.x = 0;
            transformComponent.y = 0;
            spellRep.isActive = false;
        }
    }

    private void fillSwordFields(float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            tileM.get(targetTile).setCurrentType(Enums.TileTypes.SwordOccupied);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds at sword creation.");
        }
    }

    private void resetSwordFields(float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            TileComponent tileComp = tileM.get(targetTile);
            tileComp.setCurrentType(tileComp.getDefaultType());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds at sword creation.");
        }
    }
}
