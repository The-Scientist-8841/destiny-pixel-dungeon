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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.GuidingLight;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.AmmoBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ToolboxCraftingScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndClericSpells;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndToolboxAbilities;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

import javax.tools.Tool;

public class Toolbox extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLBOX;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_ABILITIES;

		unique = true;
		bones = false;
	}

	public Artifact artifact = null;

	public static final String AC_CRAFT = "CRAFT";
	public static final String AC_ABILITIES = "ABILITIES";
	public static final String AC_UPGRADE = "UPGRADE";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && !cursed && hero.buff(MagicImmune.class) == null)  {
			actions.add(AC_ABILITIES);
			actions.add(AC_CRAFT);
			if (level() < levelCap) actions.add(AC_UPGRADE);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (action.equals(AC_CRAFT)) {
			Game.switchScene(ToolboxCraftingScene.class);
		} else if (action.equals(AC_ABILITIES)) {
			curItem = this;
			curUser = hero;
			//Show abilities window
			GameScene.show(new WndToolboxAbilities(this));
		} else if (action.equals(AC_UPGRADE)) {
			if (Dungeon.materials >= 10) {
				if (level() < levelCap) {
					upgrade();
					Dungeon.materials -= 10;
					updateQuickslot();
					hero.sprite.operate(hero.pos);
					hero.spendAndNext(3f);
				} else GLog.p(Messages.get(this, "at_level_cap"));
			} else {
				GLog.n(Messages.get(this, "failed_to_upgrade"));
			}
		}
	}

	@Override
	public String desc() {
		//Introductory paragraph
		String desc = Messages.get(this, "desc");
		desc += "\n\n" + Messages.get(this, "desc_affix", "an", "");

		if (cursed) desc += "\n\n" + Messages.get(this, "desc_cursed");

		return desc;
	}

	@Override
	public int value() {
		return -1;
	}

	@Override
	public boolean doEquip(final Hero hero) {
		if (artifact != null &&
				(
					(hero.belongings.artifact != null && artifact.getClass() == hero.belongings.artifact.getClass()) ||
					(hero.belongings.artifact2 != null && artifact.getClass() == hero.belongings.artifact2.getClass()) ||
					(hero.belongings.misc != null && artifact.getClass() == hero.belongings.misc.getClass()) ||
					(hero.belongings.misc2 != null && artifact.getClass() == hero.belongings.misc2.getClass()
				)
			)
		) {
			GLog.w( Messages.get(Toolbox.class, "affixed_artifact_conflict", artifact.name()) );
			return false;
		}

		return super.doEquip(hero);
	}

	@Override
	public void activate(Char ch) {
		if (artifact != null) artifact.activate(ch);
		super.activate(ch);
	}

	public void spendCharge( float chargesSpent ) {
		partialCharge -= chargesSpent;
		while (partialCharge < 0){
			charge--;
			partialCharge++;
		}

		//target hero level is 1 + 2*tome level
		int lvlDiffFromTarget = Dungeon.hero.lvl - (1+level()*2);
		//plus an extra one for each level after 6
		if (level() >= 7){
			lvlDiffFromTarget -= level()-6;
		}

		if (lvlDiffFromTarget >= 0){
			exp += Math.round(chargesSpent * 10f * Math.pow(1.1f, lvlDiffFromTarget));
		} else {
			exp += Math.round(chargesSpent * 10f * Math.pow(0.75f, -lvlDiffFromTarget));
		}

		if (exp >= (level() + 1) * 50 && level() < levelCap) {
			upgrade();
			Catalog.countUse(Toolbox.class);
			exp -= level() * 50;
			GLog.p(Messages.get(this, "levelup"));

		}

		updateQuickslot();
	}

	public void directCharge(float amount){
		if (charge < chargeCap) {
			partialCharge += amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
		updateQuickslot();
	}

	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		if (artifact != null) {
			artifact.toolbox = null;
			artifact.upgrade();
			artifact.toolbox = this;
		}
		return super.upgrade();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Toolbox.ToolboxRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			partialCharge += 0.25f*amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	private static final String ARTIFACT = "artifact";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(ARTIFACT, artifact);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		artifact = (Artifact) bundle.get(ARTIFACT);
		if (artifact != null) {
			artifact.toolbox = this;
		}
	}

	public boolean canUseAbility(Hero hero, ToolboxAbilities ability) {
		boolean canUse = isEquipped(hero) && charge >= ability.chargeCost() && Dungeon.materials >= ability.materialsCost();
		if (ability == ToolboxAbilities.ARTIFACT || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT) canUse = canUse && artifact != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT) canUse = canUse && artifact == null;
		return canUse;
	}

	public boolean abilityIsApplicable(ToolboxAbilities ability) {
		if (ability == ToolboxAbilities.ARTIFACT || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT) return artifact != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT) return artifact == null;

		return true;
	}

	public void useAbility(ToolboxAbilities ability) {
		switch (ability) {
			case DISARM:
				GameScene.selectCell(disarmer);
				break;
			case ARM:
				GameScene.selectCell(armer);
				break;
			case ARTIFACT:
				if (artifact != null) {
					GameScene.show(new WndUseItem(null, artifact));
				}
				break;
			case AFFIX_ARTIFACT:
				GameScene.selectItem(affixer);
				break;
			case DECONSTRUCT_ARTIFACT:
				curUser.busy();
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);

				artifact.onUnequip(curUser, false, true);
				Dungeon.materials += 5;
				artifact = null;

				updateQuickslot();

				break;
		}
	}

	public String getAbilityTitle(ToolboxAbilities ability) {
		if (ability == ToolboxAbilities.ARTIFACT) {
			if (artifact != null) return Messages.titleCase(artifact.title());
		}
		return ability.title();
	}

	public void spendAbilityCosts(ToolboxAbilities ability) {
		spendCharge(ability.chargeCost());
		Dungeon.materials -= ability.materialsCost();
	}

	public enum ToolboxAbilities {
		DISARM(1, 0, 3f),
		ARM(1, 1, 3f),
		ARTIFACT(0,0, 3f),
		AFFIX_ARTIFACT(0,0,3f),
		DECONSTRUCT_ARTIFACT(0,0,3f);

		private final int chargeCost, materialsCost;
		private final float timeToUse;

		ToolboxAbilities(int charge_cost, int materials_cost, float time_to_use) {
			this.chargeCost = charge_cost;
			this.materialsCost = materials_cost;
			this.timeToUse = time_to_use;
		}

		private String title() {
			return Messages.get(this, name() + ".title");
		}

		public int chargeCost() { return chargeCost; }
		public int materialsCost() { return materialsCost; }
		public float timeToUse() { return timeToUse; }
	}

	private CellSelector.Listener disarmer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curUser.curAction = new HeroAction.Disarm(target);
				curUser.next();
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbox.class, "disarm_prompt");
		}
	};

	private CellSelector.Listener armer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curUser.curAction = new HeroAction.Arm(target);
				curUser.next();
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbox.class, "arm_prompt");
		}
	};

	private final WndBag.ItemSelector affixer = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Toolbox.class, "affix_prompt");
		}

		@Override
		public boolean itemSelectable(Item item) { return item instanceof Artifact && !(item instanceof Toolbox) && !(item instanceof HolyTome) && !(item instanceof CloakOfShadows); }

		public void whenDone(Hero hero) {
			hero.busy();
			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
		}

		@Override
		public void onSelect( final Item item ) {
			if (item != null && item.isIdentified() && !item.cursed) {
				if (item.isEquipped(curUser)) ((Artifact) item).doUnequip(curUser, false);
				else item.detach(curUser.belongings.backpack);

				curItem.upgrade(
					Math.max(0, Math.min(item.level() - curItem.level(), ((Artifact) curItem).levelCap))
				);
				item.upgrade(Math.max(0, Math.min(curItem.level() - item.level(), ((Artifact) curItem).levelCap)));

				Talent.onItemEquipped(curUser, item);
				((Artifact) item).activate( curUser );
				((Artifact) item).onEquip(curUser);

				((Toolbox) curItem).artifact = (Artifact) item;
				artifact.toolbox = (Toolbox) curItem;

				curUser.busy();

				curUser.spend(ToolboxAbilities.AFFIX_ARTIFACT.timeToUse());
				curUser.busy();
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);
				updateQuickslot();
			} else if (item != null && !item.isIdentified()) GLog.n(Messages.get(Toolbox.class, "affix_not_identified"));
			else if (item != null && !item.cursed) GLog.n(Messages.get(Toolbox.class, "affix_cursed"));
		}
	};

	public class ToolboxRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}

				}
			} else {
				partialCharge = 0;
			}

			updateQuickslot();

			spend( TICK );

			return true;
		}
	}
}
