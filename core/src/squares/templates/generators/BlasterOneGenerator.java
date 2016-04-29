package squares.templates.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.components.spells.Spell;
import squares.utils.Enums;

import static squares.utils.Initializer.spellTempalte;

/**
 */
public class BlasterOneGenerator implements SpellMaker {
    private Engine engine;

    public BlasterOneGenerator(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Entity makeSpell(int damage) {
        Entity blaster = spellTempalte.makeEntity();
        Spell spellRep = blaster.getComponent(Spell.class);
        spellRep.occupyEffect = Enums.TileTypes.BlasterOneOccupied;
        spellRep.damage = damage;
        engine.addEntity(blaster);
        return blaster;
    }
}
