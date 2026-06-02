package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.mythical;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class ScrollOfArtifactLevelling extends MythicalScroll {
    {
        image = ItemSpriteSheet.SCROLL_ARTIFACT_LEVELLING;
    }

    @Override
    public void doRead() {
        GameScene.selectItem( itemSelector );
    }

    public static void burst( Char user ) {
        if (user.sprite != null) {
            Emitter e = user.sprite.centerEmitter();
            if (e != null) e.burst(PurpleParticle.BURST, 15);
        }
    }

    private WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(ScrollOfArtifactLevelling.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Artifact && item.level() < ((Artifact) item).getLevelCap();
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                if (item.isIdentified()) {
                    ((MythicalScroll) curItem).readAnimation();
                    Sample.INSTANCE.play(Assets.Sounds.READ);
                    burst(curUser);
                    curItem.detach(curUser.belongings.backpack);
                    item.upgrade();
                } else {
                    GLog.n(Messages.get(ScrollOfArtifactLevelling.class, "not_identified"));
                }
            }
        }
    };
}
