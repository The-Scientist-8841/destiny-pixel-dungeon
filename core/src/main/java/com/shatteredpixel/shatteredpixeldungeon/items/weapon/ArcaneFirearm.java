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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.AmmoBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.WeaponLacing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
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
	public float unloadTime = 1f;
	public ArrayList<Bullet> bulletsToLoad = new ArrayList<>();

	public WeaponLacing lacing = null;
	
	{
		image = ItemSpriteSheet.ARCANE_FIREARM;
		
		defaultAction = AC_SHOOT;
		usesTargeting = true;
		
		unique = true;
		bones = false;
	}

	public int chamber_size() {
		if (Dungeon.hero.hasTalent(Talent.TRUSTY_SIDEARM)) return 8;
		else return 6;
	}

	public float load_time() {
		if (Dungeon.hero.hasTalent(Talent.TRUSTY_SIDEARM) && Dungeon.hero.pointsInTalent(Talent.TRUSTY_SIDEARM) > 1) return 1f;
		else return 2f;
	}

	private static final String CHAMBER = "chamber";
	private static final String NUM_BULLETS = "num_bullets";
	private static final String UNLOAD_TIME = "unloadTime";
	public static final String LACING = "lacing";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(NUM_BULLETS, chamber.size());
		for (int i = 0; i < chamber.size(); i += 1) {
			bundle.put(CHAMBER + Integer.toString(i), chamber.get(i));
		}
		bundle.put(UNLOAD_TIME, unloadTime);
		bundle.put(LACING, lacing);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		int num_bullets = bundle.getInt(NUM_BULLETS);
		chamber.clear();
		for (int i = 0; i < num_bullets; i += 1) {
			Bullet b = (Bullet) bundle.get(CHAMBER + Integer.toString(i));
			b.gun = this;
			chamber.add(b);
		}
		unloadTime = bundle.getFloat(UNLOAD_TIME);
		lacing = (WeaponLacing) bundle.get(LACING);
	}
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero)) {

			if (chamber.size() > 0) { actions.add(AC_SHOOT); actions.add(AC_UNLOAD); }
			if (chamber.size() < chamber_size()) actions.add(AC_LOAD);
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

		if (lacing != null) info += "\n\n" + lacing.application_info();
		
		return info;
	}

	@Override
	public String status() {
		return Integer.toString(chamber.size() + bulletsToLoad.size()) + "/" + Integer.toString(chamber_size());
	}
	
	@Override
	public int STRReq(int lvl) {
		return STRReq(1, lvl); //tier 1
	}
	
	@Override
	public int min(int lvl) { //For Melee
		return lvl + 1;
	}
	
	@Override
	public int max(int lvl) { //For Melee
		return 2*lvl + 3;
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
		int damage;
		if (lacing == null || lacing.getClass() != WeaponLacing.class) damage = super.damageRoll(owner);
		else damage = max();

		if (owner instanceof Hero) {
			int exStr = ((Hero) owner).STR() - STRReq();
			if (exStr > 0) {
				if (lacing == null || lacing.getClass() != WeaponLacing.class) damage += Hero.heroDamageIntRange(0, exStr);
				else damage += exStr;
			}

			if (owner.buff(Talent.pistolWhipTracker.class) != null) {
				damage += 1 + 2*((Hero) owner).pointsInTalent(Talent.PISTOL_WHIP);
				owner.buff(Talent.pistolWhipTracker.class).detach();
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
					hero.spend(load_time());
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
		hero.spend(load_time());
		hero.busy();
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
		return true;
	}

	public boolean load(Bullet bullet) {
		if (chamber.size() >= chamber_size()) return false;
		bullet.gun = this;
		chamber.add(bullet);
		return true;
	}

	public Emitter lacingEmitter() {
		if (lacing != null) {
			Emitter emitter = new Emitter();
			emitter.pos(0, 0, 16, 16);
			emitter.fillTarget = false;
			emitter.pour(Speck.factory(lacing.particle), 0.25f);
			return emitter;
		} else return null;
	}

	public void consumeLacing() {
		if (lacing != null) {
			lacing.uses -= 1;
			if (lacing.uses <= 0) lacing = null;
		}
	}
	
	public static class Bullet extends MissileWeapon {

		ArcaneFirearm gun = null;
		public int baseDmg = 3;
		public float scalingFactorMin = 1f;
		public float scalingFactorMax = 2f;
		public float maxFactor = 2f;
		public Class<? extends InventoryBullet> parentClass = InventoryBullet.class;
		
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
		private static final String PARENT_CLASS = "parentClass";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(BASE_DMG, baseDmg);
			bundle.put(SCALE_MIN, scalingFactorMin);
			bundle.put(SCALE_MAX, scalingFactorMax);
			bundle.put(MAX_FACTOR, maxFactor);
			bundle.put(PARENT_CLASS, parentClass);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			baseDmg = bundle.getInt(BASE_DMG);
			scalingFactorMin = bundle.getFloat(SCALE_MIN);
			scalingFactorMax = bundle.getFloat(SCALE_MAX);
			maxFactor = bundle.getFloat(MAX_FACTOR);
			parentClass = bundle.getClass(PARENT_CLASS);
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
				damage = onHit(attacker, defender, damage);
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

		public int onHit(Char attacker, Char defender, int damage) {
			//Default - do nothing.
			return damage;
		}

		@Override
		public Item split(int amount) {
			return null;
		}

		@Override
		public void throwSound() {
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1.5f, Random.Float(0.2f, 0.3f) );
		}

		@Override
		protected float adjacentAccFactor(Char owner, Char target) {
			float toAdd = 0f;
			if (owner instanceof Hero) {
				if (((Hero) owner).hasTalent(Talent.TRUSTY_SIDEARM) && ((Hero) owner).pointsInTalent(Talent.TRUSTY_SIDEARM) >= 3) toAdd = 0.25f;
			}
			return super.adjacentAccFactor(owner, target) + toAdd;
		}

		@Override
		public String name() {
			return Messages.get(parentClass, "name");
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

				Buff buff = curUser.buff(Talent.resourcefulMealTracker.class);
				if (curUser.hasTalent(Talent.RESOURCEFUL_MEAL) && buff != null) {
					if (Random.Int(2) >= curUser.pointsInTalent(Talent.RESOURCEFUL_MEAL)) chamber.remove(0);
					((Talent.resourcefulMealTracker) buff).addUse();
				} else chamber.remove(0);

				if (curUser.hasTalent(Talent.PISTOL_WHIP)) {
					Buff.prolong(curUser, Talent.pistolWhipTracker.class, 5f);
				}

				if (chamber.isEmpty() && curUser.hasTalent(Talent.PLAN_B)) {
					Buff.affect(curUser, Barrier.class).setShield(2*curUser.pointsInTalent(Talent.PLAN_B) + 1);
				}

				//Alert enemies within a large radius
				int d = 7;
				if (Dungeon.hero.hasTalent(Talent.TRUSTY_SIDEARM) && Dungeon.hero.pointsInTalent(Talent.TRUSTY_SIDEARM) == 4) d = 4;
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob.alignment != Char.Alignment.ALLY) {
						if (Dungeon.level.heroFOV[mob.pos] || Dungeon.level.distance(curUser.pos, mob.pos) <= d) mob.beckon( curUser.pos );
					}
				}
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
				Bullet bullet = ((InventoryBullet) item).get_bullet();
				((ArcaneFirearm) curItem).bulletsToLoad.add(bullet);
				if (item.quantity() > 1) item.quantity(item.quantity() - 1);
				else item.detach(curUser.belongings.backpack);
				updateQuickslot();

				if (((ArcaneFirearm) curItem).bulletsToLoad.size() < chamber_size() - chamber.size()) {
					((ArcaneFirearm) curItem).selectBulletToLoad(curUser);
				} else ((ArcaneFirearm) curItem).load(curUser);
			} else ((ArcaneFirearm) curItem).load(curUser);
		}
	};
}
