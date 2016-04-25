package squares.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import squares.character.MainCharacter;
import squares.components.Enums.TileTypes;
import squares.components.Enums.UnitMovement;
import squares.components.PlayerComponent;
import squares.components.TileComponent;
import squares.components.TransformComponent;

import static com.badlogic.gdx.Gdx.input;

/**
 * Movement system definition.
 */
public class PCMovementSystem extends IteratingSystem {
    private MainCharacter mainCharacter;
    private Array<Array<Entity>> gridField;

    private ComponentMapper<TileComponent> tileMapper = ComponentMapper.getFor(TileComponent.class);

    public PCMovementSystem(MainCharacter mainCharacter, Array<Array<Entity>> gridField) {
        super(Family.all(PlayerComponent.class).get());
        this.mainCharacter = mainCharacter;
        this.gridField = gridField;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TransformComponent currentPosition = mainCharacter.getTransformComponent();

        clearTile(currentPosition);



        if (input.isKeyJustPressed(Keys.W)) {
            mainCharacter.Move(UnitMovement.North, gridField);
        } else if (input.isKeyJustPressed(Keys.A)) {
            mainCharacter.Move(UnitMovement.West, gridField);
        } else if (input.isKeyJustPressed(Keys.S)) {
            mainCharacter.Move(UnitMovement.South, gridField);
        } else if (input.isKeyJustPressed(Keys.D)) {
            mainCharacter.Move(UnitMovement.East, gridField);
        }


        Entity targetTileEntity = gridField.get((int) currentPosition.y()).get((int) currentPosition.x());
        TileComponent tile = tileMapper.get(targetTileEntity);

        tile.setCurrentType(TileTypes.Occupied);
    }

    private void clearTile(TransformComponent currentPosition) {
        Entity prevTile = gridField.get((int) currentPosition.y()).get((int) currentPosition.x());
        TileComponent prevTileComp = tileMapper.get(prevTile);
        prevTileComp.setCurrentType(prevTileComp.getDefaultType());
    }

    private void clearGrid() {
        Array<TileComponent> entityList = new Array<TileComponent>();
        for (Array<Entity> row : gridField) {
            for (Entity ent : row) {
                TileComponent tileComponent = tileMapper.get(ent);
                tileComponent.setCurrentType(tileComponent.getDefaultType());
            }
        }
    }
}