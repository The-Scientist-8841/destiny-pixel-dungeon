package com.shatteredpixel.shatteredpixeldungeon.items.modifications;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ArmorBracing extends Item {

    private static final String AC_APPLY = "APPLY";
    public int particle = Speck.YELLOW_LIGHT;
    public int uses = 1;

    {
        stackable = true;
        image = ItemSpriteSheet.ARMOR_BRACING;

        defaultAction = AC_APPLY;

        unique = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_APPLY );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_APPLY )) {

            curUser = hero;
            curItem = this;

            GameScene.selectItem(applier);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public String application_info() { return Messages.get(this, "application_info"); }

    private final WndBag.ItemSelector applier = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(ArmorBracing.class, "apply_prompt");
        }

        @Override
        public boolean itemSelectable(Item item) { return item instanceof Armor; }

        public void whenDone(Hero hero) {
            hero.busy();
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
        }

        @Override
        public void onSelect( final Item item ) {
            if (item != null && item.isIdentified()) {
                if (((Armor) item).bracing != null) {
                    GLog.n(Messages.get(ArmorBracing.class, "already_braced"));
                    return;
                }
                ((Armor) item).bracing = (ArmorBracing) curItem;


                curUser.spend(1f);
                curUser.busy();
                curUser.sprite.operate(curUser.pos);
                Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
                updateQuickslot();
                curItem.detach(curUser.belongings.backpack);
            } else if (item != null && !item.isIdentified()) GLog.n(Messages.get(ArmorBracing.class, "not_identified"));
        }
    };

    public static class ArmorBracingCraft extends ToolboxRecipe {
        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.isEmpty();
        }

        @Override
        public int cost(ArrayList<Item> ingredients) { return 3; }

        @Override
        public Item craft(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            return new ArmorBracing();
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            return new ArmorBracing();
        }
    }
}
