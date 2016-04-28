package squares.spells;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import squares.character.PCPlayerCharacter;
import squares.generators.SpellMaker;

/**
 * A button that represents a chip.
 */
public class ChipButton extends TextButton {
    private SpellMaker generator;

    public ChipButton(String text, TextButtonStyle style, final PCPlayerCharacter character, final SpellMaker generator) {
        super(text, style);
        this.generator = generator;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                character.getPlayerComponent().spells.addFirst(generator.makeSpell());
            }
        });
    }
}
