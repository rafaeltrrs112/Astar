package squares.templates.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.components.spells.Spell;
import squares.utils.Enums;

import static squares.utils.Initializer.spellTempalte;

/**
 */
public class BlasterTwoGenerator implements SpellMaker{
    private Engine engine;

    public BlasterTwoGenerator(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Entity makeSpell(int damage) {
        Entity blaster = spellTempalte.makeEntity();
        Spell spellRep = blaster.getComponent(Spell.class);
        spellRep.occupyEffect = Enums.TileTypes.BlasterTwoOccupied;
        spellRep.damage = damage;
        engine.addEntity(blaster);
        return blaster;
    }
}
