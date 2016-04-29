package squares.templates.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.utils.Enums;
import squares.components.spells.Spell;

import static squares.utils.Initializer.spellTempalte;

/**
 */
public class ShortsSwordGenerator implements SpellMaker {
    private Engine engine;

    public ShortsSwordGenerator(Engine engine){
        this.engine = engine;
    }

    @Override
    public Entity makeSpell(int damage) {
        Entity sword = spellTempalte.makeEntity();

        Spell spellRep = sword.getComponent(Spell.class);
        spellRep.occupyEffect = Enums.TileTypes.ShortSwordOccupied;
        spellRep.damage = damage;

        engine.addEntity(sword);
        return sword;
    }
}
