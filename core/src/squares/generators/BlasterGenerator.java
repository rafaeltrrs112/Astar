package squares.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.components.Enums;
import squares.components.spells.Spell;

import static squares.generators.Initializer.spellTempalte;

/**
 */
public class BlasterGenerator implements SpellMaker {
    private Engine engine;

    public BlasterGenerator(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Entity makeSpell() {
        Entity blaster = spellTempalte.makeEntity();
        blaster.getComponent(Spell.class).occupyEffect = Enums.TileTypes.BlasterOccupied;
        engine.addEntity(blaster);
        return blaster;
    }
}
