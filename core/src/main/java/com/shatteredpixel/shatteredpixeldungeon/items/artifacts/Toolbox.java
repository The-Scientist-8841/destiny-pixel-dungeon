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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ToolboxCraftingScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SentrySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndToolboxAbilities;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
	public Artifact artifact2 = null;

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
		if (!Dungeon.hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) || Dungeon.hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) < 2) {
			desc += "\n\n" + Messages.get(this, "desc_affix", "an", "");
		} else desc += "\n\n" + Messages.get(this, "desc_affix", "two", "s");

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
					(hero.belongings.misc2 != null && artifact.getClass() == hero.belongings.misc2.getClass())
				)
		) {
			GLog.w( Messages.get(Toolbox.class, "affixed_artifact_conflict", artifact.name()) );
			return false;
		}

		if (artifact2 != null &&
				(
						(hero.belongings.artifact != null && artifact2.getClass() == hero.belongings.artifact.getClass()) ||
						(hero.belongings.artifact2 != null && artifact2.getClass() == hero.belongings.artifact2.getClass()) ||
						(hero.belongings.misc != null && artifact2.getClass() == hero.belongings.misc.getClass()) ||
						(hero.belongings.misc2 != null && artifact2.getClass() == hero.belongings.misc2.getClass())
				)
		) {
			GLog.w( Messages.get(Toolbox.class, "affixed_artifact_conflict", artifact2.name()) );
			return false;
		}

		return super.doEquip(hero);
	}

	@Override
	public void activate(Char ch) {
		if (artifact != null) artifact.activate(ch);
		if (artifact2 != null) artifact2.activate(ch);
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

		if (artifact instanceof HornOfPlenty) ((HornOfPlenty) artifact).updateImage(charge);
		if (artifact2 instanceof HornOfPlenty) ((HornOfPlenty) artifact2).updateImage(charge);
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

			if (artifact instanceof HornOfPlenty) ((HornOfPlenty) artifact).updateImage(charge);
			if (artifact2 instanceof HornOfPlenty) ((HornOfPlenty) artifact2).updateImage(charge);
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
		if (artifact2 != null) {
			artifact2.toolbox = null;
			artifact2.upgrade();
			artifact2.toolbox = this;
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

			if (artifact instanceof HornOfPlenty) ((HornOfPlenty) artifact).updateImage(charge);
			if (artifact2 instanceof HornOfPlenty) ((HornOfPlenty) artifact2).updateImage(charge);
			updateQuickslot();
		}
	}

	private static final String ARTIFACT = "artifact";
	private static final String ARTIFACT2 = "artifact2";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(ARTIFACT, artifact);
		bundle.put(ARTIFACT2, artifact2);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		artifact = (Artifact) bundle.get(ARTIFACT);
		if (artifact != null) {
			artifact.toolbox = this;
		}

		artifact2 = (Artifact) bundle.get(ARTIFACT2);
		if (artifact2 != null) {
			artifact2.toolbox = this;
		}


		if (artifact instanceof HornOfPlenty) ((HornOfPlenty) artifact).updateImage(charge);
		if (artifact2 instanceof HornOfPlenty) ((HornOfPlenty) artifact2).updateImage(charge);
	}

	public void placeSentry(int pos) {
		Sentry sentry = new Sentry();
		sentry.pos = pos;
		sentry.tier = Dungeon.hero.hasTalent(Talent.AUTO_TURRET) ? Dungeon.hero.pointsInTalent(Talent.AUTO_TURRET) : 1;
		sentry.upgrade(buffedLvl());
		GameScene.add(sentry, 1f);
		Dungeon.level.occupyCell(sentry);
		sentry.sprite.emitter().burst(SentryParticle.UP, sentry.tier);
		Dungeon.level.pressCell(pos + 1);
		Sample.INSTANCE.play(Assets.Sounds.PUFF);
	}

	public boolean canUseAbility(Hero hero, ToolboxAbilities ability) {
		if (ability == ToolboxAbilities.ARTIFACT2 || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT2 || ability == ToolboxAbilities.AFFIX_ARTIFACT2) {
			if (!hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) || hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) < 2) return false;
		}

		if (ability == ToolboxAbilities.RETURN_ARTIFACT || ability == ToolboxAbilities.RETURN_ARTIFACT2) {
			if (!hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) || hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) < 3) return false;
		}

		boolean canUse = isEquipped(hero) && charge >= ability.chargeCost() && Dungeon.materials >= ability.materialsCost();
		if (ability == ToolboxAbilities.ARTIFACT || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT || ability == ToolboxAbilities.RETURN_ARTIFACT) canUse = canUse && artifact != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT) canUse = canUse && artifact == null;

		if (ability == ToolboxAbilities.ARTIFACT2 || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT2 || ability == ToolboxAbilities.RETURN_ARTIFACT2) canUse = canUse && artifact2 != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT2) canUse = canUse && artifact2 == null;

		if (ability == ToolboxAbilities.PLACE_SENTRY) canUse = canUse && Dungeon.hero.hasTalent(Talent.AUTO_TURRET);
		return canUse;
	}

	public boolean abilityIsApplicable(Hero hero, ToolboxAbilities ability) {
		if (ability == ToolboxAbilities.ARTIFACT2 || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT2 || ability == ToolboxAbilities.AFFIX_ARTIFACT2) {
			if (!hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) || hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) < 2) return false;
		}

		if (ability == ToolboxAbilities.RETURN_ARTIFACT || ability == ToolboxAbilities.RETURN_ARTIFACT2) {
			if (!hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) || hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) < 3) return false;
		}

		if (ability == ToolboxAbilities.ARTIFACT || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT || ability == ToolboxAbilities.RETURN_ARTIFACT) return artifact != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT) return artifact == null;

		if (ability == ToolboxAbilities.ARTIFACT2 || ability == ToolboxAbilities.DECONSTRUCT_ARTIFACT2 || ability == ToolboxAbilities.RETURN_ARTIFACT2) return artifact2 != null;
		if (ability == ToolboxAbilities.AFFIX_ARTIFACT2) return artifact2 == null;

		if (ability == ToolboxAbilities.PLACE_SENTRY) return hero.hasTalent(Talent.AUTO_TURRET);

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

				curUser.spendAndNext(ability.timeToUse);

				break;
			case ARTIFACT2:
				if (artifact2 != null) {
					GameScene.show(new WndUseItem(null, artifact2));
				}
				break;
			case AFFIX_ARTIFACT2:
				GameScene.selectItem(affixer2);
				break;
			case DECONSTRUCT_ARTIFACT2:
				curUser.busy();
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);

				artifact2.onUnequip(curUser, false, true);
				Dungeon.materials += 5;
				artifact2 = null;

				updateQuickslot();

				curUser.spendAndNext(ability.timeToUse);

				break;
			case RETURN_ARTIFACT:
				curUser.busy();
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);

				// Return a new artifact, fresh slate. I think we need to do this because some artifacts do weird things when they level up.
				Artifact toReturn = getArtifact(artifact);
				if (toReturn != null) {
					toReturn.identify();
					toReturn.cursed = false;
					if (!toReturn.collect()) Dungeon.level.drop(toReturn, curUser.pos);
				}

				artifact.onUnequip(curUser, false, true);
				artifact = null;

				Dungeon.materials -= ability.materialsCost();

				updateQuickslot();

				curUser.spendAndNext(ability.timeToUse);
				break;
			case RETURN_ARTIFACT2:
				curUser.busy();
				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.Sounds.EAT, 1f, 0.6f);

				// Return a new artifact, fresh slate. I think we need to do this because some artifacts do weird things when they level up.
				Artifact toReturn2 = getArtifact(artifact2);
				if (toReturn2 != null) {
					toReturn2.identify();
					toReturn2.cursed = false;
					if (!toReturn2.collect()) Dungeon.level.drop(toReturn2, curUser.pos);
				}

				artifact2.onUnequip(curUser, false, true);
				artifact2 = null;

				Dungeon.materials -= ability.materialsCost();

				updateQuickslot();

				curUser.spendAndNext(ability.timeToUse);
				break;
			case PLACE_SENTRY:
				GameScene.selectCell(placer);
				break;
		}
	}

	private Artifact getArtifact(Artifact a) {
		Artifact toReturn;
		if (a.getClass() == AlchemistsToolkit.class) toReturn = new AlchemistsToolkit();
		else if (a.getClass() == CapeOfThorns.class) toReturn = new CapeOfThorns();
		else if (a.getClass() == ChaliceOfBlood.class) toReturn = new ChaliceOfBlood();
		else if (a.getClass() == DriedRose.class) toReturn = new DriedRose();
		else if (a.getClass() == EtherealChains.class) toReturn = new EtherealChains();
		else if (a.getClass() == HornOfPlenty.class) toReturn = new HornOfPlenty();
		else if (a.getClass() == LloydsBeacon.class) toReturn = new LloydsBeacon();
		else if (a.getClass() == MasterThievesArmband.class) toReturn = new MasterThievesArmband();
		else if (a.getClass() == SandalsOfNature.class) toReturn = new SandalsOfNature();
		else if (a.getClass() == SkeletonKey.class) toReturn = new SkeletonKey();
		else if (a.getClass() == TalismanOfForesight.class) toReturn = new TalismanOfForesight();
		else if (a.getClass() == TimekeepersHourglass.class) toReturn = new TimekeepersHourglass();
		else if (a.getClass() == UnstableSpellbook.class) toReturn = new UnstableSpellbook();
		else toReturn = null; //Should never happen
		return toReturn;
	}

	public String getAbilityTitle(ToolboxAbilities ability) {
		if (ability == ToolboxAbilities.ARTIFACT) {
			if (artifact != null) return Messages.titleCase(artifact.title());
		}
		if (ability == ToolboxAbilities.ARTIFACT2) {
			if (artifact2 != null) return Messages.titleCase(artifact2.title());
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
		ARTIFACT(0,0, 0f),
		AFFIX_ARTIFACT(0,0,3f),
		DECONSTRUCT_ARTIFACT(0,0,3f),
		RETURN_ARTIFACT(0,3,5f),
		ARTIFACT2(0,0,0f),
		AFFIX_ARTIFACT2(0,0,3f),
		DECONSTRUCT_ARTIFACT2(0,0,3f),
		RETURN_ARTIFACT2(0,3,5f),
		PLACE_SENTRY(2,5,1f);

		private final float chargeCost;
		private final int materialsCost;
		private final float timeToUse;

		ToolboxAbilities(int charge_cost, int materials_cost, float time_to_use) {
			this.chargeCost = charge_cost;
			this.materialsCost = materials_cost;
			this.timeToUse = time_to_use;
		}

		private String title() {
			return Messages.get(this, name() + ".title");
		}

		public float chargeCost() {
			float cost = chargeCost;
			if (Dungeon.hero.hasTalent(Talent.TOOLS_OF_THE_TRADE) && Dungeon.hero.pointsInTalent(Talent.TOOLS_OF_THE_TRADE) == 4) {
				if (this == ARM || this == DISARM) cost -= 0.5f;
			}
			return cost;
		}
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

	private CellSelector.Listener placer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curUser.curAction = new HeroAction.PlaceSentry(target);
				curUser.next();
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbox.class, "place_prompt");
		}
	};

	private final WndBag.ItemSelector affixer = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Toolbox.class, "affix_prompt");
		}

		@Override
		public boolean itemSelectable(Item item) {
			if (item instanceof Artifact && !(item instanceof Toolbox) && !(item instanceof HolyTome) && !(item instanceof CloakOfShadows)) {
				if (((Toolbox) curItem).artifact2 != null && ((Toolbox) curItem).artifact2.getClass() == item.getClass()) return false;

				if (!item.isEquipped(curUser)) {
					if (curUser.belongings.artifact != null && curUser.belongings.artifact.getClass() == item.getClass()) return false;
					if (curUser.belongings.artifact2 != null && curUser.belongings.artifact2.getClass() == item.getClass()) return false;
					if (curUser.belongings.misc != null && curUser.belongings.misc.getClass() == item.getClass()) return false;
					if (curUser.belongings.misc2 != null && curUser.belongings.misc2.getClass() == item.getClass()) return false;
				}
			}
			return true;
		}

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

	private final WndBag.ItemSelector affixer2 = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Toolbox.class, "affix_prompt");
		}

		@Override
		public boolean itemSelectable(Item item) {
			if (item instanceof Artifact && !(item instanceof Toolbox) && !(item instanceof HolyTome) && !(item instanceof CloakOfShadows)) {
				if (((Toolbox) curItem).artifact != null && ((Toolbox) curItem).artifact.getClass() == item.getClass()) return false;

				if (!item.isEquipped(curUser)) {
					if (curUser.belongings.artifact != null && curUser.belongings.artifact.getClass() == item.getClass()) return false;
					if (curUser.belongings.artifact2 != null && curUser.belongings.artifact2.getClass() == item.getClass()) return false;
					if (curUser.belongings.misc != null && curUser.belongings.misc.getClass() == item.getClass()) return false;
					if (curUser.belongings.misc2 != null && curUser.belongings.misc2.getClass() == item.getClass()) return false;
				}
			}
			return true;
		}

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

				((Toolbox) curItem).artifact2 = (Artifact) item;
				artifact2.toolbox = (Toolbox) curItem;

				curUser.busy();

				curUser.spend(ToolboxAbilities.AFFIX_ARTIFACT2.timeToUse());
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
			if (artifact instanceof HornOfPlenty) ((HornOfPlenty) artifact).updateImage(charge);
			if (artifact2 instanceof HornOfPlenty) ((HornOfPlenty) artifact2).updateImage(charge);

			spend( TICK );

			return true;
		}
	}

	public static class Sentry extends NPC {

		public int tier = 1;
		public int toolboxLevel = 1;

		{
			spriteClass = SentrySprite.class;

			alignment = Alignment.ALLY;

			properties.add(Property.IMMOVABLE);
			properties.add(Property.INORGANIC);

			viewDistance = 4;
			state = WANDERING;
			HT = 5;
			HP = HT;
		}

		@Override
		public int damageRoll() {
			return Hero.heroDamageIntRange(toolboxLevel, 2 * toolboxLevel);
		}

		@Override
		public int attackSkill( Char target ) {
			return INFINITE_ACCURACY; //Always hits
		}

		@Override
		public int drRoll() {
			int bonus;
			switch (tier) {
				case 1: default: bonus = 0; break;
				case 2: bonus = toolboxLevel/3; break;
				case 3: bonus = toolboxLevel/2; break;
				case 4: bonus = toolboxLevel; break;
			}
			return super.drRoll() + bonus;
		}

		@Override
		protected boolean canAttack( Char enemy ) {
			if (super.canAttack(enemy)){
				return true;
			} else {
				return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ).collisionPos == enemy.pos;
			}
		}

		protected boolean doAttack( Char enemy ) {
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}

		protected void zap() {
			spend( 1f / tier );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			if (hit( this, enemy, true )) {

				int effectiveDmg = attackProc( enemy, damageRoll() );
				enemy.damage(effectiveDmg, this);

			} else {
				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}
		}

		public void onZapComplete() {
			zap();
			next();
		}

		@Override
		public String name() {
			return Messages.get(this, "name", tier );
		}

		public void upgrade(int toolboxLevel ){
			if (this.toolboxLevel < toolboxLevel){
				this.toolboxLevel = toolboxLevel;
			}

			switch (tier){
				case 1: default:
					HT = 5 + toolboxLevel;
					viewDistance = 4;
					break;
				case 2:
					HT = 10 + 2*toolboxLevel;
					viewDistance = 5;
					break;
				case 3:
					HT = 20 + 3*toolboxLevel;
					viewDistance = 6;
					break;
				case 4:
					HT = 30 + 4*toolboxLevel;
					viewDistance = 7;
					break;
			}
			HP = HT;
			if (sprite != null) {
				((SentrySprite) sprite).updateTier(tier);
				sprite.place(pos);
			}
			GameScene.updateFog(pos, viewDistance+1);
		}

		@Override
		public int defenseSkill(Char enemy) {
			switch (tier) {
				case 1: default: defenseSkill = 0; break;
				case 2: defenseSkill = toolboxLevel/2; break;
				case 3: defenseSkill = toolboxLevel; break;
				case 4: defenseSkill = Math.round(toolboxLevel*1.5f); break;
			}
			return super.defenseSkill(enemy);
		}

		@Override
		protected boolean getCloser(int target) {
			return false;
		}

		@Override
		protected boolean getFurther(int target) {
			return false;
		}

		@Override
		public CharSprite sprite() {
			SentrySprite sprite = (SentrySprite) super.sprite();
			sprite.linkVisuals(this);
			return sprite;
		}

		@Override
		public void updateSpriteState() {
			super.updateSpriteState();
			((SentrySprite)sprite).updateTier(tier);
			sprite.place(pos);
		}

		@Override
		public void destroy() {
			super.destroy();
			Dungeon.observe();
			GameScene.updateFog(pos, viewDistance+1);
		}

		@Override
		public boolean canInteract(Char c) {
			return true;
		}

		@Override
		public boolean interact( Char c ) {
			if (c != Dungeon.hero){
				return true;
			}
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show(new WndOptions( sprite(),
							Messages.get(Toolbox.Sentry.class, "dismiss_title"),
							Messages.get(Toolbox.Sentry.class, "dismiss_body"),
							Messages.get(Toolbox.Sentry.class, "dismiss_confirm"),
							Messages.get(Toolbox.Sentry.class, "dismiss_cancel") ){
						@Override
						protected void onSelect(int index) {
							if (index == 0){
								Dungeon.materials += 2;
								updateQuickslot();
								Sample.INSTANCE.play(Assets.Sounds.ITEM);
								die(null);
							}
						}
					});
				}
			});
			return true;
		}

		@Override
		public String description() {
			return Messages.get(this, "desc", toolboxLevel, 2*toolboxLevel, tier, viewDistance);
		}

		{
			immunities.add( Sleep.class );
			immunities.add( Terror.class );
			immunities.add( Dread.class );
			immunities.add( Vertigo.class );
			immunities.add( AllyBuff.class );
		}

		private static final String TIER = "tier";
		private static final String TOOLBOX_LEVEL = "toolbox_level";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TIER, tier);
			bundle.put(TOOLBOX_LEVEL, toolboxLevel);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			tier = bundle.getInt(TIER);
			viewDistance = 3 + tier;
			toolboxLevel = bundle.getInt(TOOLBOX_LEVEL);
		}
	}

	public static class SentryParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((SentryParticle)emitter.recycle( SentryParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
		};

		public static final Emitter.Factory UP = new Emitter.Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((SentryParticle)emitter.recycle( SentryParticle.class )).resetUp( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
		};

		public SentryParticle() {
			super();

			lifespan = 0.6f;

			color( 0xffb64f );
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;
			size = 10;
		}

		public void resetUp( float x, float y){
			reset(x, y);

			speed.set( Random.Float( -8, +8 ), Random.Float( -32, -48 ) );
		}

		@Override
		public void update() {
			super.update();

			am = 1 - left / lifespan;
		}
	}
}
