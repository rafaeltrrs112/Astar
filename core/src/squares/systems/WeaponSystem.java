package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.utils.Enums;
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
            case BlasterThreeOccupied:
            case BlasterTwoOccupied:
            case BlasterOneOccupied:
                if (spellRep.isActive) handleBlasterType(spellRep, transformComponent);
                break;
            case ShortSwordOccupied:
                if (spellRep.isActive) handleSwordTypeShort(spellRep, transformComponent, delta);
                break;
            case LongSwordOccupied:
                if (spellRep.isActive) handleSwordTypeLong(spellRep, transformComponent, delta);
                break;
            default:
                System.out.println("Not applicable!");
        }
    }

    private void handleSwordTypeLong(Spell spellRep, TransformComponent transformComponent, float delta) {
        System.out.println("Long sword");
        spellRep.iterate(delta);

        int inc = spellRep.direction.increment;

        float playerFrontY = transformComponent.y;
        float pointFront = transformComponent.x + inc;

        float pointFurtherTwo = transformComponent.x + 2 * inc;

        float pointFurtherThree = transformComponent.x + 3 * inc;

        boolean forwardHit = false;
        boolean furtherHitTwo = false;
        boolean furthestHitThree = false;

        if (spellRep.isActive) {

            forwardHit = fillDamageCheck(spellRep, pointFront, playerFrontY);
            furtherHitTwo = fillDamageCheck(spellRep, pointFurtherTwo, playerFrontY);
            furthestHitThree = fillDamageCheck(spellRep, pointFurtherThree, playerFrontY);
        }

        if (!spellRep.isActive) {

            if (!forwardHit) resetSwordFields(pointFront, playerFrontY);
            if (!furtherHitTwo) resetSwordFields(pointFurtherTwo, playerFrontY);
            if (!furthestHitThree) resetSwordFields(pointFurtherThree, playerFrontY);

        }

        if (forwardHit || furtherHitTwo || furthestHitThree) {
            spellRep.damage = 0;
        }
    }

    private void handleSwordTypeShort(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int inc = spellRep.direction.increment;

        float playerFrontY = transformComponent.y;
        float playerFrontX = transformComponent.x + inc;

        boolean centerHit = false;

        if (spellRep.isActive) {

            centerHit = fillDamageCheck(spellRep, playerFrontX, playerFrontY);
        }

        if (!spellRep.isActive) {

            if (!centerHit) resetSwordFields(playerFrontX, playerFrontY);
        }

        if (centerHit) {
            spellRep.damage = 0;
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

        boolean leftHit = false;
        boolean centerHit = false;
        boolean rightHit = false;

        if (spellRep.isActive) {
            leftHit = fillDamageCheck(spellRep, leftSwipeX, leftSwipeY);
            rightHit = fillDamageCheck(spellRep, rightSwipeX, rightSwipeY);
            centerHit = fillDamageCheck(spellRep, playerFrontX, playerFrontY);
        }

        if (!spellRep.isActive) {
            if (!leftHit) resetSwordFields(leftSwipeX, leftSwipeY);
            if (!rightHit) resetSwordFields(rightSwipeX, rightSwipeY);
            if (!centerHit) resetSwordFields(playerFrontX, playerFrontY);
        }

        if (leftHit || rightHit || centerHit) {
            spellRep.damage = 0;
        }

    }

    private void handleBlasterType(Spell spellRep, TransformComponent transformComponent) {

        float nextXPosit = transformComponent.x + spellRep.direction.increment;
        TileComponent prevTileComp = tileM.get(gridField.get((int) transformComponent.y).get((int) transformComponent.x));

        if (prevTileComp.getCurrentType() == spellRep.occupyEffect) {
            prevTileComp.setCurrentType(prevTileComp.getDefaultType());
        }

        if (!(nextXPosit > 9 || nextXPosit < 0)) {
            transformComponent.x = nextXPosit;

            Entity targetTile = gridField.get((int) transformComponent.y).get((int) transformComponent.x);

            TileComponent tileComp = tileM.get(targetTile);
            if (!tileComp.isOccupied()) {
                tileComp.setCurrentType(spellRep.occupyEffect);
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

    private boolean fillDamageCheck(Spell spellRep, float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            TileComponent tileComp = tileM.get(targetTile);
            if (tileComp.isOccupied() && spellRep.isActive) {
                tileComp.occupier.health -= spellRep.damage;
                System.out.println("Collision! player health is now " + tileComp.occupier.health);
                return true;
            } else {
                tileComp.setCurrentType(spellRep.occupyEffect);
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds at sword creation.");
            return false;
        }
    }

    private void resetSwordFields(float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            TileComponent tileComp = tileM.get(targetTile);
            if (!tileComp.isOccupied()) tileComp.setCurrentType(tileComp.getDefaultType());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds at sword creation.");
        }
    }
}
