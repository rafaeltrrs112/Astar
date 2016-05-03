package squares.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import squares.utils.Enums.TileTypes;

public class PlayerComponent implements Component {
    public Array<TileTypes> allergies = Array.with(
            TileTypes.BlasterOneOccupied, TileTypes.BlasterTwoOccupied, TileTypes.BlasterThreeOccupied,
            TileTypes.LongSwordOccupied, TileTypes.ShortSwordOccupied, TileTypes.WideSwordOccupied,
            TileTypes.BoomerangOccupied, TileTypes.BombOneOccupied, TileTypes.BombTwoOccupied);

    private Queue<Entity> spells = new Queue<Entity>();
    public int health = 250;
    public int spellCap;

    private boolean stun = false;

    private float stunCounter = 0f;

    private final float STUN_TIME = 3f;

    public boolean allergic(TileTypes type) {
        return !allergies.contains(type, true);
    }

    public TileTypes occupyType;

    public boolean hasSpells() {
        return spells.size != 0;
    }

    public Entity popSpell() {
        return spells.removeFirst();
    }

    public Entity peekSpell() {
        return spells.first();
    }

    public void queueSpell(Entity spell) {
        if (spells.size < spellCap) {
            spells.addLast(spell);
        }
    }

    public void clearAllSpells() {
        spells.clear();
    }


    public ImmutableArray<Entity> getSpells() {
        Array<Entity> entityArrray = new Array<>();
        for (Entity e : spells) {
            entityArrray.add(e);
        }
        return new ImmutableArray<>(entityArrray);
    }

    public void shuffleSpells(){
        Array<Entity> temp = new Array<>();
        for(Entity spell: spells){
            temp.add(spell);
        }
        temp.shuffle();
        Queue<Entity> shuffled = new Queue<>();
        for(Entity e: temp){
            shuffled.addLast(e);
        }
        spells = shuffled;
    }

    public void stunPlayer(){
        if(!stun){
            stun = true;
            stunCounter = 0f;
        }
    }

    public void cont(float delta){
        stunCounter += delta;
        if(stunCounter >= STUN_TIME){
            stun = false;
            stunCounter = 0;
        }
    }

    public boolean stunned(){
        return stun;
    }
}
