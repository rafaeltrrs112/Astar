package squares.templates.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.components.Enums;
import squares.components.spells.Spell;

import static squares.utils.Initializer.spellTempalte;

/**
 */
public class SwordGenerator implements SpellMaker {

    private final Engine engine;

    public SwordGenerator(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Entity makeSpell(int damage) {
        Entity sword = spellTempalte.makeEntity();

        Spell spellRep = sword.getComponent(Spell.class);
        spellRep.occupyEffect = Enums.TileTypes.SwordOccupied;
        spellRep.damage = damage;

        engine.addEntity(sword);
        return sword;
    }
}
