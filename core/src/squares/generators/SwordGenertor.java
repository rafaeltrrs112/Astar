package squares.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import squares.components.Enums;
import squares.components.spells.Spell;

import static squares.generators.Initializer.spellTempalte;

/**
 */
public class SwordGenertor implements SpellMaker {

    private final Engine engine;

    public SwordGenertor(Engine engine){
        this.engine = engine;
    }
    @Override
    public Entity makeSpell() {
        Entity sword = spellTempalte.makeEntity();
        sword.getComponent(Spell.class).occupyEffect = Enums.TileTypes.SwordOccupied;
        engine.addEntity(sword);
        return sword;
    }
}
