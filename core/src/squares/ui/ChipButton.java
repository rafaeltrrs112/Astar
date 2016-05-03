package squares.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import squares.character.CharacterEntity;
import squares.templates.generators.SpellMaker;

/**
 * A button that represents a chip.
 */
public class ChipButton extends TextButton {

    private final ClickListener clickEvent;
    private final int chipDamage;
    private final CharacterEntity characterEntity;
    private final SpellMaker generator;

    public ChipButton(TextButtonStyle style, final CharacterEntity character, final SpellMaker generator, final int chipDamage) {
        super("", style);
        this.chipDamage = chipDamage;
        this.characterEntity = character;
        this.generator = generator;

        clickEvent = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                apply();
            }
        };

        addListener(clickEvent);

        setWidth(100);
        setHeight(100);
    }

    public void apply(){
        if(chipDamage != 0) characterEntity.getPlayerComponent().queueSpell(generator.makeSpell(chipDamage));
    }

}
