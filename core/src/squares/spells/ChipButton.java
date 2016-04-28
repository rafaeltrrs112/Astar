package squares.spells;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import squares.character.CharacterEntity;
import squares.character.PCPlayerCharacter;
import squares.templates.generators.SpellMaker;

/**
 * A button that represents a chip.
 */
public class ChipButton extends TextButton {

    public ChipButton(TextButtonStyle style, final CharacterEntity character, final SpellMaker generator, final int chipDamage) {
        super("", style);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                character.getPlayerComponent().spells.addFirst(generator.makeSpell(chipDamage));
            }
        });

        setWidth(100);
        setHeight(100);
    }
}
