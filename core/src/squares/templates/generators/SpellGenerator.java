package squares.templates.generators;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import squares.components.spells.Spell;
import squares.utils.Enums;

import static squares.utils.Initializer.spellTempalte;

/**
 * A Generator for the spells
 */
public class SpellGenerator implements SpellMaker {
    private Engine engine;
    private Enums.TileTypes occupyType;
    private final Sprite sprite;
    private final float travelTime;

    public SpellGenerator(Engine engine, Enums.TileTypes occupyType, Sprite sprite, float travelTime) {
        this.engine = engine;
        this.occupyType = occupyType;
        this.sprite = sprite;
        this.travelTime = travelTime;
        sprite.setSize(65, 65);
    }

    public SpellGenerator(Engine engine, Enums.TileTypes occupyType, Sprite sprite) {
        this(engine, occupyType, sprite, .25f);
    }

    @Override
    public Entity makeSpell(int damage) {
        Entity entity = spellTempalte.makeEntity();

        Spell spellRep = entity.getComponent(Spell.class);

        spellRep.occupyEffect = occupyType;
        spellRep.damage = damage;
        spellRep.sprite = sprite;
        spellRep.coolDown = travelTime;
        engine.addEntity(entity);

        return entity;
    }
}
