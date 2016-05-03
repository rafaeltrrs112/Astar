package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.uwsoft.editor.renderer.components.TransformComponent;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.components.spells.Spell;
import squares.utils.Enums;

import java.awt.*;

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
            case WideSwordOccupied:
                if (spellRep.isActive) handleSwordType(spellRep, transformComponent, delta);
                break;
            case BlasterThreeOccupied:
            case BlasterTwoOccupied:
            case BlasterOneOccupied:
                if (spellRep.isActive) handleBlasterType(spellRep, transformComponent, delta);
                break;
            case ShortSwordOccupied:
                if (spellRep.isActive) handleSwordTypeShort(spellRep, transformComponent, delta);
                break;
            case LongSwordOccupied:
                if (spellRep.isActive) handleSwordTypeLong(spellRep, transformComponent, delta);
                break;
            case BombOneOccupied:
            case BombTwoOccupied:
                if (spellRep.isActive) handleBombType(spellRep, transformComponent, delta);
                break;
            case BoomerangOccupied:
                if (spellRep.isActive) handleBoomerangType(spellRep, transformComponent, delta);
                break;
            case GrappleOccupied:
                if (spellRep.isActive) handleGrappleType(spellRep, transformComponent, delta);
                break;
            default:
                System.out.println("Not applicable!");
        }
        TileComponent currentTile = tileM.get(gridField.get((int) transformComponent.y).get((int) transformComponent.x));
        if (currentTile.isOccupied() && currentTile.occupier.key.health < 0) checkDeath(currentTile);
    }

    private void handleBoomerangType(Spell spellRep, TransformComponent transformComponent, float delta) {
        if (spellRep.iterateTravel(delta)) {
            float nextXPosit = transformComponent.x + spellRep.getDirection().xIncrement;
            float nextYPosit = transformComponent.y + spellRep.getDirection().yIncrement;

            TileComponent prevTileComp = tileM.get(gridField.get((int) transformComponent.y).get((int) transformComponent.x));

            if (prevTileComp.getCurrentType() == spellRep.occupyEffect) {
                prevTileComp.setCurrentType(prevTileComp.getDefaultType());
            }

            switch (spellRep.getDirection()) {
                case RIGHT:
                case LEFT: {
                    if (!(nextXPosit > 9 || nextXPosit < 0)) {
                        transformComponent.x = nextXPosit;

                        Entity targetTile = gridField.get((int) transformComponent.y).get((int) transformComponent.x);

                        TileComponent tileComp = tileM.get(targetTile);

                        if (!tileComp.isOccupied()) {
                            tileComp.setCurrentType(spellRep.occupyEffect);
                        } else {
                            tileComp.occupier.key.health -= spellRep.damage;
                            transformComponent.x = 0;
                            transformComponent.y = 0;
                            spellRep.isActive = false;
                        }
                    } else if (spellRep.getDirection().flip() == spellRep.getOriginalDirection()) {
                        transformComponent.x = 0;
                        transformComponent.y = 0;
                        spellRep.isActive = false;
                    }

                    if (spellRep.getDirection() == Enums.TravelDirection.RIGHT && nextXPosit == 9) {
                        spellRep.setDirection(Enums.TravelDirection.UP);
                    } else if (spellRep.getDirection() == Enums.TravelDirection.LEFT && nextXPosit == 0) {
                        spellRep.setDirection(Enums.TravelDirection.UP);
                    }
                    break;
                }
                case UP:
                case DOWN: {
                    if (!(nextYPosit > 4 || nextXPosit < 0)) {
                        transformComponent.x = nextXPosit;
                        transformComponent.y = nextYPosit;

                        Entity targetTile = gridField.get((int) transformComponent.y).get((int) transformComponent.x);

                        TileComponent tileComp = tileM.get(targetTile);

                        if (!tileComp.isOccupied()) {
                            tileComp.setCurrentType(spellRep.occupyEffect);
                        } else {
                            tileComp.occupier.key.health -= spellRep.damage;
                            transformComponent.x = 0;
                            transformComponent.y = 0;
                            spellRep.isActive = false;
                        }
                    }
                    if (spellRep.getDirection() == Enums.TravelDirection.UP && nextYPosit == 4) {
                        spellRep.setDirection(spellRep.getOriginalDirection().flip());
                    }
                    break;
                }
            }
        }
    }

    private void handleSwordTypeLong(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int xIncrement = spellRep.getDirection().xIncrement;

        float playerFrontY = transformComponent.y;
        float pointFront = transformComponent.x + xIncrement;

        float pointFurtherTwo = transformComponent.x + 2 * xIncrement;

        float pointFurtherThree = transformComponent.x + 3 * xIncrement;

        handleStaticEffect(spellRep, delta,
                Array.with(
                        Point(pointFront, playerFrontY),
                        Point(pointFurtherTwo, playerFrontY),
                        Point(pointFurtherThree, playerFrontY)
                ));
    }

    private void handleSwordTypeShort(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int xIncrement = spellRep.getDirection().xIncrement;

        float playerFrontY = transformComponent.y;
        float playerFrontX = transformComponent.x + xIncrement;

        handleStaticEffect(spellRep, delta,
                Array.with(
                        Point(playerFrontX, playerFrontY)
                ));
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        process(transformM.get(entity), spellM.get(entity), delta);
    }

    private void handleBombType(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int xIncrement = spellRep.getDirection().xIncrement;

        int bombPositY = (int) transformComponent.y;
        int bombPositX = (int) transformComponent.x + (xIncrement * 3);

        handleStaticEffect(
                spellRep, delta,
                Array.with(Point(bombPositX, bombPositY))
        );

    }

    private void handleSwordType(Spell spellRep, TransformComponent transformComponent, float delta) {
        spellRep.iterate(delta);

        int xIncrement = spellRep.getDirection().xIncrement;

        float leftSwipeY = transformComponent.y + xIncrement;
        float leftSwipeX = transformComponent.x + xIncrement;

        float rightSwipeY = transformComponent.y - xIncrement;
        float rightSwipeX = transformComponent.x + xIncrement;

        float playerFrontY = transformComponent.y;
        float playerFrontX = transformComponent.x + xIncrement;

        handleStaticEffect(spellRep, delta,
                Array.with(
                        Point(leftSwipeX, leftSwipeY),
                        Point(rightSwipeX, rightSwipeY),
                        Point(playerFrontX, playerFrontY)
                )
        );
    }

    private void handleStaticEffect(Spell spellRep, float delta, Array<Point> damageField) {
        spellRep.iterate(delta);
        Array<Boolean> hitMarkers = new Array<>();
        for (int i = 0; i < damageField.size; i++) {
            hitMarkers.add(false);
        }
        if (spellRep.isActive) {
            for (int i = 0; i < damageField.size; i++) {
                Point currentPoint = damageField.get(i);
                hitMarkers.set(i, fillDamageCheck(spellRep, currentPoint.x, currentPoint.y));
            }
        }
        if (!spellRep.isActive) {
            for (int i = 0; i < damageField.size; i++) {
                Boolean isHit = hitMarkers.get(i);
                Point currentPoint = damageField.get(i);
                if (!isHit) resetDamageField(currentPoint.x, currentPoint.y);
            }
        }
        if (hitMarkers.contains(true, true)) spellRep.damage = 0;
    }

    private void handleBlasterType(Spell spellRep, TransformComponent transformComponent, float delta) {
        if (spellRep.iterateTravel(delta)) {
            float nextXPosit = transformComponent.x + spellRep.getDirection().xIncrement;
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
                    tileComp.occupier.key.health -= spellRep.damage;
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
    }

    private void handleGrappleType(Spell spellRep, TransformComponent transformComponent, float delta) {
        if (spellRep.iterateTravel(delta)) {
            float nextXPosit = transformComponent.x + spellRep.getDirection().xIncrement;

            if (!(nextXPosit > 9 || nextXPosit < 0)) {
                transformComponent.x = nextXPosit;

                Entity targetTile = gridField.get((int) transformComponent.y).get((int) transformComponent.x);

                TileComponent tileComp = tileM.get(targetTile);

                if (!tileComp.isOccupied()) {
                    tileComp.setCurrentType(spellRep.occupyEffect);

                } else {
                    tileComp.occupier.key.health -= spellRep.damage;

                    tileComp.occupier.key.stunPlayer();

                    Array<Entity> laneTiles = gridField.get((int) transformComponent.y);

                    for (Entity t : laneTiles) {
                        TileComponent tileComponent = tileM.get(t);
                        if (tileComponent.getCurrentType() == spellRep.occupyEffect) {
                            tileComponent.setCurrentType(tileComponent.getDefaultType());
                        }
                    }

                    yankPlayer(tileComp, transformComponent);

                    transformComponent.x = 0;
                    transformComponent.y = 0;
                    spellRep.isActive = false;
                }
            } else {
                Array<Entity> laneTiles = gridField.get((int) transformComponent.y);

                for (Entity t : laneTiles) {
                    TileComponent tileComponent = tileM.get(t);
                    if (tileComponent.getCurrentType() == spellRep.occupyEffect) {
                        tileComponent.setCurrentType(tileComponent.getDefaultType());
                    }
                }
                transformComponent.x = 0;
                transformComponent.y = 0;
                spellRep.isActive = false;
            }
        }
    }

    private void yankPlayer(TileComponent currentTile, TransformComponent tilesTransform) {
        IdentityMap.Entry<PlayerComponent, TransformComponent> pair = currentTile.occupier;

        currentTile.setCurrentType(currentTile.getDefaultType());

        pair.value.x = currentTile.occupier.key.occupyType == Enums.TileTypes.GreenPlayerOccupied ? 4 : 5;

        currentTile.occupier = null;

        TileComponent yankTile = gridField.get((int) tilesTransform.y).get((int) pair.value.x).getComponent(TileComponent.class);

        yankTile.setOccupier(pair.key, pair.value);
        yankTile.setCurrentType(pair.key.occupyType);
    }

    private boolean fillDamageCheck(Spell spellRep, float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            TileComponent tileComp = tileM.get(targetTile);
            if (tileComp.isOccupied() && spellRep.isActive) {
                tileComp.occupier.key.health -= spellRep.damage;
                return true;
            } else {
                tileComp.setCurrentType(spellRep.occupyEffect);
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private void resetDamageField(float x, float y) {
        try {
            Entity targetTile = gridField.get((int) y).get((int) x);
            TileComponent tileComp = tileM.get(targetTile);
            if (!tileComp.isOccupied()) tileComp.setCurrentType(tileComp.getDefaultType());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private void checkDeath(TileComponent tileComp) {
        //TODO : Test code.
        tileComp.occupier = null;
        tileComp.setCurrentType(tileComp.getDefaultType());
    }

    private Point Point(float x, float y) {
        return new Point((int) x, (int) y);
    }

    private Point Point(int x, int y) {
        return new Point((int) x, (int) y);
    }


}
