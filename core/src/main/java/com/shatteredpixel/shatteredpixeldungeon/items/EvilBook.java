/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FlashBangBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class EvilBook extends Item {

    public static final String AC_RESET = "RESET";

    {
        image = ItemSpriteSheet.EVIL_BOOK_INACTIVE;
        unique = true;
        bones = false;
        identify();
    }

    public static int[] resets = {0, 0, 0, 0, 0, //Sewers
                            0, 0, 0, 0, 0, //Dungeon
                            0, 0, 0, 0, 0, //Caves
                            0, 0, 0, 0, 0, //City
                            0, 0, 0, 0, 0}; //Halls

    public static boolean can_reset = true;
    public static boolean active = false;
    public static int[] quest_depths = {0, 0, 0, 0, 0};

    private int lvlsToUpgrade = 4;

    public void levelUp() {
        lvlsToUpgrade -= 1;
        if (lvlsToUpgrade <= 0) {
            ScrollOfUpgrade reward = new ScrollOfUpgrade();
            if (!reward.collect()) {
                Dungeon.level.drop(reward, Dungeon.hero.pos);
            }

            Flare flare = new Flare(6, 20).color(0xFF0000, true).show(Dungeon.hero.sprite, 3f);
            Sample.INSTANCE.play( Assets.Sounds.DEATH );
            GLog.n(Messages.get(this, "upgrade_message"));

            lvlsToUpgrade = 5;
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

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        if (can_reset) actions.add(AC_RESET);
        actions.remove(AC_DROP);
        actions.remove(AC_THROW);
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_RESET )) {
            if (Dungeon.depth == quest_depths[0]) Ghost.Quest.reset();
            if (Dungeon.depth == quest_depths[1]) Wandmaker.Quest.reset();
            if (Dungeon.depth == quest_depths[2]) Blacksmith.Quest.reset();
            if (Dungeon.depth == quest_depths[3]) Imp.Quest.reset();

            InterlevelScene.mode = InterlevelScene.Mode.RESET;
            resets[Dungeon.depth - 1] += 1;
            Game.switchScene(InterlevelScene.class);
            Catalog.countUse(getClass());
            Sample.INSTANCE.play( Assets.Sounds.DEATH );
            GLog.n(Messages.get(this, "reset_message", resets[Dungeon.depth - 1]));

            if (hero.hasTalent(Talent.WARRIORS_TRIAL)) {
                hero.HP = Math.min(hero.HP + Math.max(1, (int)((0.05f + 0.025f*hero.pointsInTalent(Talent.WARRIORS_TRIAL)*hero.HT))), hero.HT);
            }

            if (hero.hasTalent(Talent.WARRIORS_FATE)) {
                Buff.affect(hero, Talent.WarriorsDestinyShieldBuff.class).incShield((int)( hero.HT * 0.05 * hero.pointsInTalent(Talent.WARRIORS_FATE) ));
            }

            if (resets[Dungeon.depth - 1] == 5) {
                if (hero.hasTalent(Talent.WARRIORS_PURPOSE)) {
                    float energy = Hunger.HUNGRY / 2f;
                    if (hero.pointsInTalent(Talent.WARRIORS_PURPOSE) == 2) energy = Hunger.HUNGRY;
                    else if (hero.pointsInTalent(Talent.WARRIORS_PURPOSE) == 3) energy = Hunger.STARVING;
                    else if (hero.pointsInTalent(Talent.WARRIORS_PURPOSE) == 4) {
                        energy = Hunger.STARVING * 2;
                        Buff.affect(hero, WellFed.class).reset();
                    }

                    Buff.affect(hero, Hunger.class).affectHunger(energy);
                    Talent.onFoodEaten(hero, energy, null);
                }
            }
        }
    }

    @Override
    public String desc() {
        if (active)
            return Messages.get(this, "desc_active");
        else
            return super.desc();
    }

    public boolean isActive(){
        return active;
    }

    public void activate(){
        active = true;
        image = ItemSpriteSheet.EVIL_BOOK_ACTIVE;
    }

    public void deactivate(){
        active = false;
        image = ItemSpriteSheet.EVIL_BOOK_INACTIVE;
    }

    public static void showFlareForBonusDrop( Visual vis ){
        if (vis == null || vis.parent == null) return;

        new Flare(6, 32).color(0xFF0000, true).show(vis, 4f);
    }

    public static class TriesToDropTracker extends CounterBuff {
        {
            revivePersists = true;
        }
    }

    public static Item genItemDrop(int level) {
        float lowThresh = 0;
        float midThresh = 0;
        float highThresh = 0;
        float veryHighThresh = 0;
        float maxThresh = 0;
        float exaltedThresh = 0;
        switch (level) {
            case 5:
                lowThresh = 0.1f;
                midThresh = 0.25f;
                highThresh = 0.5f;
                veryHighThresh = 0.75f;
                maxThresh = 0.9f;
                exaltedThresh = 1f;
                break;
            case 4:
                lowThresh = 0.15f;
                midThresh = 0.4f;
                highThresh = 0.6f;
                veryHighThresh = 0.8f;
                maxThresh = 0.925f;
                exaltedThresh = 1f;
                break;
            case 3:
                lowThresh = 0.25f;
                midThresh = 0.6f;
                highThresh = 0.75f;
                veryHighThresh = 0.875f;
                maxThresh = 0.95f;
                exaltedThresh = 1f;
                break;
            case 2:
                lowThresh = 0.4f;
                midThresh = 0.75f;
                highThresh = 0.9f;
                veryHighThresh = 0.95f;
                maxThresh = 1f;
                exaltedThresh = 0f;
                break;
            case 1:
                lowThresh = 0.6f;
                midThresh = 0.9f;
                highThresh = 0.95f;
                veryHighThresh = 1f;
                maxThresh = 0f;
                exaltedThresh = 0f;
                break;
        }
        float roll = Math.min(Random.Float() + 0.025f*Math.max(Dungeon.hero.pointsInTalent(Talent.WARRIORS_STRUGGLE) - 1, 0), 1f);
        if (roll <= exaltedThresh && roll > maxThresh) return genExaltedValueItem();
        else if (roll <= maxThresh && roll > veryHighThresh) return genMaxValueItem(true);
        else if (roll <= veryHighThresh && roll > highThresh) return genVeryHighValueItem(true);
        else if (roll <= highThresh && roll > midThresh) return genHighValueItem(true);
        else if (roll <= midThresh && roll > lowThresh) return genMidValueItem(true);
        else return genLowValueItem();
    }

    private static Item genLowValueItem(){
        switch (Random.Int(6)){
            case 0: default:
                Item i = new Gold().random();
                return i.quantity(i.quantity()/2);
            case 1:
                return Generator.randomUsingDefaults(Generator.Category.SEED);
            case 2:
                return new EnergyCrystal(Random.Int(2, 7));
            case 3:
                return new SmallRation();
            case 4:
                return new Bomb();
            case 5:
                return new Dewdrop();
        }
    }

    private static Item genMidValueItem(boolean canBeEquipment){
        int M = 9;
        if (!canBeEquipment) M = 7;
        switch (Random.Int(M)){
            case 0: default:
                Item i = genLowValueItem();
                if (i instanceof Bomb){
                    return new Bomb.DoubleBomb();
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                return Random.Int(2) == 0 ? new Starflower.Seed() : new Sungrass.Seed();
            case 2:
                return new GooBlob();
            case 3:
                return new MysteryMeat();
            case 4:
                return Generator.randomUsingDefaults(Generator.Category.STONE);
            case 5:
                return new Honeypot.ShatteredPot();
            case 6:
                return new Stylus();
            case 7:
                Weapon w = Generator.randomWeapon(Dungeon.depth/5, true);
                if (!w.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      w.enchant();
                else if (w.hasCurseEnchant())                           w.enchant(null);
                w.cursed = false;
                w.cursedKnown = true;
                return w;
            case 8:
                Armor a = Generator.randomArmor(Dungeon.depth/5);
                if (!a.hasGoodGlyph() && Random.Int(5) > resets[Dungeon.depth-1])        a.inscribe();
                else if (a.hasCurseGlyph())                             a.inscribe(null);
                a.cursed = false;
                a.cursedKnown = true;
                return a;
        }
    }

    private static Item genHighValueItem(boolean canBeEquipment){
        int M = 10;
        if (!canBeEquipment) M = 7;
        switch (Random.Int(M)){
            case 0: default:
                Item i = genMidValueItem(false);
                if (i instanceof Bomb.DoubleBomb){
                    return Generator.randomUsingDefaults(Generator.Category.MIDTIERBOMB);
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                return new StoneOfEnchantment();
            case 2:
                return new MetalShard();
            case 3:
                return new Food();
            case 4:
                return Generator.randomUsingDefaults(Generator.Category.POTION);
            case 5:
                return Generator.randomUsingDefaults(Generator.Category.SCROLL);
            case 6:
                return new Honeypot();
            case 7:
                Weapon w = Generator.randomWeapon(Dungeon.depth/5 + 1, true);
                if (!w.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      w.enchant();
                else if (w.hasCurseEnchant())                           w.enchant(null);
                w.cursed = false;
                w.cursedKnown = true;
                return w;
            case 8:
                Armor a = Generator.randomArmor(Dungeon.depth/5 + 1);
                if (!a.hasGoodGlyph() && Random.Int(5) > resets[Dungeon.depth-1])        a.inscribe();
                else if (a.hasCurseGlyph())                             a.inscribe(null);
                a.cursed = false;
                a.cursedKnown = true;
                return a;
            case 9:
                Weapon m = Generator.randomMissile(Dungeon.depth/5, true);
                if (!m.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      m.enchant();
                else if (m.hasCurseEnchant())                           m.enchant(null);
                m.cursed = false;
                m.cursedKnown = true;
                return m;
        }
    }

    private static Item genVeryHighValueItem(boolean canBeEquipment){
        int M = 11;
        if (!canBeEquipment) M = 8;
        switch (Random.Int(M)){
            case 0: default:
                Item i = genHighValueItem(false);
                boolean isBomb = false;
                for (Class<?> c : Generator.Category.MIDTIERBOMB.classes) {
                    if (i.getClass() == c) {
                        isBomb = true;
                        break;
                    }
                }
                if (isBomb){
                    return Generator.randomUsingDefaults(Generator.Category.HIGHTIERBOMB);
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                return new PotionOfExperience();
            case 2:
                return new ScrollOfUpgrade();
            case 3:
                return new LiquidMetal().quantity(Random.Int(3,10));
            case 4:
                return new Pasty();
            case 5:
                Item j = Generator.randomUsingDefaults(Generator.Category.POTION);
                if (!(j instanceof ExoticPotion)) {
                    return Reflection.newInstance(ExoticPotion.regToExo.get(j.getClass()));
                } else {
                    return Reflection.newInstance(j.getClass());
                }
            case 6:
                Item k = Generator.randomUsingDefaults(Generator.Category.SCROLL);
                if (!(k instanceof ExoticScroll)) {
                    return Reflection.newInstance(ExoticScroll.regToExo.get(k.getClass()));
                } else {
                    return Reflection.newInstance(k.getClass());
                }
            case 7:
                return Generator.randomUsingDefaults(Generator.Category.BREW);
            case 8:
                Weapon w = Generator.randomWeapon(Dungeon.depth/5 + 2, true);
                if (!w.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      w.enchant();
                else if (w.hasCurseEnchant())                           w.enchant(null);
                w.cursed = false;
                w.cursedKnown = true;
                return w;
            case 9:
                Armor a = Generator.randomArmor(Dungeon.depth/5 + 2);
                if (!a.hasGoodGlyph() && Random.Int(5) > resets[Dungeon.depth-1])        a.inscribe();
                else if (a.hasCurseGlyph())                             a.inscribe(null);
                a.cursed = false;
                a.cursedKnown = true;
                return a;
            case 10:
                Weapon m = Generator.randomMissile(Dungeon.depth/5 + 1, true);
                if (!m.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      m.enchant();
                else if (m.hasCurseEnchant())                           m.enchant(null);
                m.cursed = false;
                m.cursedKnown = true;
                return m;
        }
    }

    private static Item genMaxValueItem(boolean canBeEquipment){
        int M = 9;
        if (!canBeEquipment) M = 6;
        switch (Random.Int(M)){
            case 0: default:
                Item i = genVeryHighValueItem(false);
                boolean isBomb = false;
                for (Class<?> c : Generator.Category.HIGHTIERBOMB.classes) {
                    if (i.getClass() == c) {
                        isBomb = true;
                        break;
                    }
                }
                if (isBomb){
                    return new HolyBomb();
                } else {
                    return i.quantity(i.quantity()*2);
                }
            case 1:
                return new PotionOfDivineInspiration();
            case 2:
                return new ScrollOfEnchantment();
            case 3:
                return new UnstableSpell();
            case 4:
                return new PhantomMeat();
            case 5:
                return Generator.randomUsingDefaults(Generator.Category.ELIXIR);
            case 6:
                Item r = Generator.randomUsingDefaults(Generator.Category.RING);
                r.cursed = false;
                r.cursedKnown = true;
                return r;
            case 7:
                return new Ankh();
            case 8:
                Weapon m = Generator.randomMissile(Dungeon.depth/5 + 2, true);
                if (!m.hasGoodEnchant() && Random.Int(5) > resets[Dungeon.depth-1])      m.enchant();
                else if (m.hasCurseEnchant())                           m.enchant(null);
                m.cursed = false;
                m.cursedKnown = true;
                return m;
        }
    }

    private static Item genExaltedValueItem(){
        switch (Random.Int(6)){
            case 0: default:
                Item i = genMaxValueItem(false);
                return i.quantity(i.quantity()*2);
            case 1:
                Ring r = new RingOfFuror();
                r.cursed = false;
                r.cursedKnown = true;
                return r;
            case 2:
                return new MeatPie();
            case 3:
                Item artifact = Generator.randomUsingDefaults(Generator.Category.ARTIFACT);
                artifact.cursed = false;
                artifact.cursedKnown = true;
                return artifact;
            case 4:
                Ankh a = new Ankh();
                a.bless();
                return a;
            case 5:
                Item w = Generator.randomUsingDefaults(Generator.Category.WAND);
                w.cursed = false;
                w.cursedKnown = true;
                return w;
        }
    }

    public static ArrayList<Item> tryForBonusDrop(Char target, int tries ) {
        int bonus = 5 - resets[Dungeon.depth - 1];

        if (bonus <= 0) return null;

        CounterBuff triesToDrop = target.buff(TriesToDropTracker.class);
        if (triesToDrop == null){
            triesToDrop = Buff.affect(target, TriesToDropTracker.class);
            triesToDrop.countUp( Random.NormalIntRange(0, 10) );
        }

        //now handle reward logic
        ArrayList<Item> drops = new ArrayList<>();

        triesToDrop.countDown(tries);
        while ( triesToDrop.count() <= 0 ){
            Item i;
            do {
                i = genItemDrop(bonus - 1);
            } while (Challenges.isItemBlocked(i));
            drops.add(i);
            triesToDrop.countUp( Random.NormalIntRange(0, 10) );
        }

        return drops;
    }

    private static final Glowing RED = new Glowing( 0x550000 );

    @Override
    public Glowing glowing() {
        return isActive() ? RED : null;
    }

    private static final String ACTIVE = "active";
    private static final String RESETS = "resets";
    private static final String CAN_RESET = "can_reset";
    private static final String QUEST_DEPTHS = "quest_depths";
    private static final String LEVELS_TO_UPGRADE = "levels_to_upgrade";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( RESETS, resets );
        bundle.put( CAN_RESET, can_reset );
        bundle.put( QUEST_DEPTHS, quest_depths );
        bundle.put(LEVELS_TO_UPGRADE, lvlsToUpgrade);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (bundle.getBoolean( ACTIVE )) activate();
        resets = bundle.getIntArray(RESETS);
        can_reset = bundle.getBoolean(CAN_RESET);
        quest_depths = bundle.getIntArray(QUEST_DEPTHS);
        lvlsToUpgrade = bundle.getInt(LEVELS_TO_UPGRADE);
    }

    @Override
    public int value() {
        return 999;
    }
}
