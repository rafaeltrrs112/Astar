package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.utils.Array;
import squares.components.spells.Blaster;
import squares.components.Enums;
import squares.components.TileComponent;
import squares.components.TransformComponent;

/**
 * Weapons system handler.
 */
public class WeaponSystem extends IntervalIteratingSystem {
    private ComponentMapper<Blaster> blasterM = ComponentMapper.getFor(Blaster.class);
    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TileComponent> tileM = ComponentMapper.getFor(TileComponent.class);
    private Array<Array<Entity>> gridField;

    public WeaponSystem(Array<Array<Entity>> gridField) {
        super(Family.all(TransformComponent.class, Blaster.class).get(), 0.2f);
        this.gridField = gridField;
    }

    private void process(Entity entity, TransformComponent transformComponent, Blaster blaster) {
        System.out.println(blaster.isActive);
        if (blaster.isActive) {

            float nextXPosit = transformComponent.x() + 1;
            TileComponent prevTileComp = tileM.get(gridField.get((int) transformComponent.y()).get((int) transformComponent.x()));
            if(prevTileComp.getCurrentType() == Enums.TileTypes.BlasterOccupied){
                prevTileComp.setCurrentType(prevTileComp.getDefaultType());
            }
            if (!(nextXPosit > 9 || nextXPosit < 0)) {
                transformComponent.setPosition(transformComponent.x() + 1, transformComponent.y());
                System.out.println(transformComponent.getPosition());

                Entity targetTile = gridField.get((int) transformComponent.y()).get((int) transformComponent.x());

                tileM.get(targetTile).setCurrentType(Enums.TileTypes.BlasterOccupied);
            }
        } else {
            transformComponent.setPosition(-1, -1);
            blaster.isActive = false;
        }
    }

    private void clearGrid() {
        for (Array<Entity> row : gridField) {
            for (Entity tile : row) {
                TileComponent tileComp = tileM.get(tile);
                if (tileComp.getCurrentType() == Enums.TileTypes.BlasterOccupied) {
//                    tileComp.setCurrentType(tileComp.getDefaultType());
                    return;
                }
            }
        }
    }

    @Override
    protected void processEntity(Entity entity) {
        process(entity, transformM.get(entity), blasterM.get(entity));
    }
}
