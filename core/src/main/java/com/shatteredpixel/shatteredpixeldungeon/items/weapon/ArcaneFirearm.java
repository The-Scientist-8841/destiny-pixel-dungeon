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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.AmmoBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.BulletType;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class ArcaneFirearm extends Weapon {
	
	public static final String AC_SHOOT		= "SHOOT";
	public static final String AC_LOAD		= "LOAD";
	public static final String AC_UNLOAD	= "UNLOAD";
	public ArrayList<Bullet> chamber = new ArrayList<>();
	public int chamber_size = 6;
	public float loadTime = 1f;
	public float unloadTime = 1f;
	public ArrayList<Bullet> bulletsToLoad = new ArrayList<>();
	
	{
		image = ItemSpriteSheet.ARCANE_FIREARM;
		
		defaultAction = AC_SHOOT;
		usesTargeting = true;
		
		unique = true;
		bones = false;
	}

	private static final String CHAMBER = "chamber";
	private static final String NUM_BULLETS = "num_bullets";
	private static final String CHAMBER_SIZE = "chamber_size";
	private static final String LOAD_TIME = "loadTime";
	private static final String UNLOAD_TIME = "unloadTime";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(CHAMBER_SIZE, chamber_size);
		bundle.put(NUM_BULLETS, chamber.size());
		for (int i = 0; i < chamber.size(); i += 1) {
			bundle.put(CHAMBER + Integer.toString(i), chamber.get(i));
		}
		bundle.put(LOAD_TIME, loadTime);
		bundle.put(UNLOAD_TIME, unloadTime);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		chamber_size = bundle.getInt(CHAMBER_SIZE);
		int num_bullets = bundle.getInt(NUM_BULLETS);
		chamber.clear();
		for (int i = 0; i < num_bullets; i += 1) {
			Bullet b = (Bullet) bundle.get(CHAMBER + Integer.toString(i));
			b.gun = this;
			chamber.add(b);
		}
		loadTime = bundle.getFloat(LOAD_TIME);
		unloadTime = bundle.getFloat(UNLOAD_TIME);
	}
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero)) {

			if (chamber.size() > 0) { actions.add(AC_SHOOT); actions.add(AC_UNLOAD); }
			if (chamber.size() < chamber_size) actions.add(AC_LOAD);
		}
		return actions;
	}

	@Override
	public String defaultAction() {
		if (chamber.size() == 0) {
			usesTargeting = false;
			return AC_LOAD;
		}
		else {
			usesTargeting = true;
			return AC_SHOOT;
		}
	}
	
	@Override
	public void execute(Hero hero, String action) {
		
		super.execute(hero, action);

		if (action.equals(AC_SHOOT)) {
			if (chamber.size() > 0) {
				curUser = hero;
				curItem = this;
				GameScene.selectCell(shooter);
			} else {
				failedToFire();
			}
		} else if (action.equals(AC_LOAD)) {
			selectBulletToLoad(hero);
		} else if (action.equals(AC_UNLOAD)) {
			for (int i = 0; i < chamber.size(); i += 1) {
				curUser = hero;
				chamber.get(i).unload(curUser);
			}
			chamber.clear();
			updateQuickslot();

			hero.spend(unloadTime);
			hero.busy();
			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
		}
	}

	public void failedToFire() {
		Sample.INSTANCE.play(Assets.Sounds.TRAP, 0.75f, 0.67f);
	}

	public void selectBulletToLoad(Hero hero) {
		curUser = hero;
		curItem = this;
		GameScene.selectItem(itemSelector);
	}

	@Override
	public String info() {
		String info = super.info();
		
		info += "\n\n" + Messages.get( ArcaneFirearm.class, "stats",
				1,
				min(),
				max(),
				STRReq());

		if (Dungeon.hero != null) {
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (Dungeon.hero.STR() > STRReq()) {
				info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
			}
		}

		if (chamber.size() > 0) {
			Bullet b = chamber.get(0);
			info += "\n\n" + Messages.get(ArcaneFirearm.class, "bullet_stats",
					b.name(),
					Math.round(augment.damageFactor(b.min())),
					Math.round(augment.damageFactor(b.max()))
			);
		}
		
		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}
		
		info += "\n\n" + Messages.get(ArcaneFirearm.class, "distance");
		
		return info;
	}

	@Override
	public String status() {
		return Integer.toString(chamber.size() + bulletsToLoad.size()) + "/" + Integer.toString(chamber_size);
	}
	
	@Override
	public int STRReq(int lvl) {
		return STRReq(1, lvl); //tier 1
	}
	
	@Override
	public int min(int lvl) { //For Melee
		return Math.round(1 + lvl/3f);
	}
	
	@Override
	public int max(int lvl) { //For Melee
		return min(lvl) + 2;
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		if (chamber.size() > 0) {
			return chamber.get(0).targetingPos(user, dst);
		} else {
			failedToFire();
			return -1;
		}
	}
	
	private int targetPos;
	
	@Override
	public int damageRoll(Char owner) { //For Melee, no augment
		int damage = super.damageRoll(owner);

		if (owner instanceof Hero) {
			int exStr = ((Hero) owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Hero.heroDamageIntRange(0, exStr);
			}
		}

		return damage;
	}

	public boolean load(Hero hero) {
		boolean loadedOne = false;

		if (bulletsToLoad.size() <= 0) return false;

		for (int i = 0; i < bulletsToLoad.size(); i += 1) {
			if (!load(bulletsToLoad.get(i))) {
				for (int j = i; j < bulletsToLoad.size(); j += 1) {
					bulletsToLoad.get(j).unload(hero);
				}
				bulletsToLoad.clear();
				if (loadedOne) {
					hero.spend(loadTime);
					hero.busy();
					hero.sprite.operate(hero.pos);
					Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
				}
				return false;
			}
			else loadedOne = true;
		}
		bulletsToLoad.clear();
		updateQuickslot();
		hero.spend(loadTime);
		hero.busy();
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
		return true;
	}

	public boolean load(Bullet bullet) {
		if (chamber.size() >= chamber_size) return false;
		bullet.gun = this;
		chamber.add(bullet);
		return true;
	}
	
	public static class Bullet extends MissileWeapon {

		ArcaneFirearm gun = null;
		public int baseDmg = 3;
		public float scalingFactorMin = 1f;
		public float scalingFactorMax = 2f;
		public float maxFactor = 2f;
		public BulletType bulletType = BulletType.BULLET;
		
		{
			image = ItemSpriteSheet.BULLET_PROJECTILE;

			hitSound = Assets.Sounds.HIT_STAB;

			setID = 0;

			spawnedForEffect = true;
		}

		private static final String BASE_DMG = "baseDmg";
		private static final String SCALE_MIN = "scalingFactorMin";
		private static final String SCALE_MAX = "scalingFactorMax";
		private static final String MAX_FACTOR = "maxFactor";
		private static final String BULLET_TYPE = "bulletType";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(BASE_DMG, baseDmg);
			bundle.put(SCALE_MIN, scalingFactorMin);
			bundle.put(SCALE_MAX, scalingFactorMax);
			bundle.put(MAX_FACTOR, maxFactor);
			bundle.put(BULLET_TYPE, bulletType);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			baseDmg = bundle.getInt(BASE_DMG);
			scalingFactorMin = bundle.getFloat(SCALE_MIN);
			scalingFactorMax = bundle.getFloat(SCALE_MAX);
			maxFactor = bundle.getFloat(MAX_FACTOR);
			bulletType = bundle.getEnum(BULLET_TYPE, BulletType.class);
		}

		@Override
		public ArrayList<String> actions(Hero hero) {
			return new ArrayList<>();
		}

		@Override
		public String defaultAction() {
			return null;
		}

		@Override
		public int defaultQuantity() {
			return 1;
		}

		@Override
		public int min(int lvl) {
			return Math.max(0, Math.round(baseDmg + scalingFactorMin*lvl));
		}

		@Override
		public int min() {
			if (gun != null) {
				if (Dungeon.hero != null) {
					return Math.max(0, min(gun.buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
				} else {
					return Math.max(0, min(gun.buffedLvl()));
				}
			} else return super.min();
		}

		@Override
		public int max(int lvl) {
			return Math.max(0, Math.round(maxFactor*baseDmg + scalingFactorMax*lvl));
		}

		@Override
		public int max() {
			if (gun != null) {
				if (Dungeon.hero != null) {
					return Math.max(0, max(gun.buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
				} else {
					return Math.max(0, max(gun.buffedLvl()));
				}
			} else return super.max();
		}

		@Override
		public int damageRoll(Char owner) {
			if (gun != null) {
				if (owner instanceof Hero){
					return gun.augment.damageFactor(Hero.heroDamageIntRange(min(), max()));
				} else {
					return gun.augment.damageFactor(Random.NormalIntRange(min(), max()));
				}
			} else return super.damageRoll(owner);
		}
		
		@Override
		public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
			if (gun != null) return gun.hasEnchant(type, owner);
			else return super.hasEnchant(type, owner);
		}
		
		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (gun != null && gun.enchantment != null) {
				damage = gun.enchantment.proc(gun, attacker, defender, damage);
			}
			return super.proc(attacker, defender, damage);
		}
		
		@Override
		public float delayFactor(Char user) {
			if (gun != null) return gun.delayFactor(user);
			else return super.delayFactor(user);
		}
		
		@Override
		public int STRReq(int lvl) {
			if (gun != null) return gun.STRReq(lvl);
			else return super.STRReq(lvl);
		}

		/*
		@Override
		protected void onThrow( int cell ) {
			Char enemy = Actor.findChar( cell );
			if (enemy == null || enemy == curUser) {
				parent = null;
				Splash.at( cell, 0xCC99FFFF, 1 );
			} else {
				if (!curUser.shoot( enemy, this )) {
					Splash.at(cell, 0xCC99FFFF, 1);
				}
				if (sniperSpecial && ArcaneFirearm.this.augment != Augment.SPEED) sniperSpecial = false;
			}
		}
		*/

		@Override
		public Item split(int amount) {
			return null;
		}

		@Override
		public void throwSound() {
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1.5f, Random.Float(0.2f, 0.3f) );
		}

		@Override
		public String name() {
			return bulletType.title();
		}

		public void unload(Hero hero) {
			InventoryBullet item = new InventoryBullet();
			if (!item.collect()) Dungeon.level.drop(item, hero.pos);
		}
	}
	
	private CellSelector.Listener shooter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				chamber.get(0).cast(curUser, target);
				chamber.remove(0);
			}
		}
		@Override
		public String prompt() {
			return Messages.get(ArcaneFirearm.class, "shoot_prompt");
		}
	};

	private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(ArcaneFirearm.class, "load_prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){ return AmmoBag.class; }
		
		@Override
		public boolean itemSelectable(Item item) { return item instanceof InventoryBullet; }

		public void whenDone(Hero hero) {
			hero.busy();
			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
		}

		@Override
		public void onSelect( final Item item ) {
			if (item != null) {
				Bullet bullet = new Bullet();
				bullet.bulletType = ((InventoryBullet) item).bulletType;
				((ArcaneFirearm) curItem).bulletsToLoad.add(bullet);
				if (item.quantity() > 1) item.quantity(item.quantity() - 1);
				else item.detach(curUser.belongings.backpack);
				updateQuickslot();

				if (((ArcaneFirearm) curItem).bulletsToLoad.size() < chamber_size - chamber.size()) {
					((ArcaneFirearm) curItem).selectBulletToLoad(curUser);
				} else ((ArcaneFirearm) curItem).load(curUser);
			} else ((ArcaneFirearm) curItem).load(curUser);
		}
	};
}
