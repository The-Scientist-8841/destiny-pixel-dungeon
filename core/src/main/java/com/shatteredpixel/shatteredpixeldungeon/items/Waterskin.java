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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.VialOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class Waterskin extends Item {

	private static final int MAX_VOLUME	= 20;

	private static final String AC_DRINK	= "DRINK";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_STATUS	= "%d/%d";

	private static final String AC_BLESS = "BLESS";

	{
		image = ItemSpriteSheet.WATERSKIN;

		defaultAction = AC_DRINK;

		unique = true;
	}

	private int volume = 0;

	private static final String VOLUME	= "volume";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		volume	= bundle.getInt( VOLUME );
	}

	private int getVolDeduction(Hero hero) {
		int volDeduction = 0;

		int[] talentPoints = { 0, 0, 0, 0, 0, 0 };

		if (hero.hasTalent(Talent.MIGHTY_BLESSING)) {
			talentPoints[0] += hero.pointsInTalent(Talent.MIGHTY_BLESSING);
		}
		if (hero.hasTalent(Talent.EMPOWERING_BLESSING)) {
			talentPoints[1] += hero.pointsInTalent(Talent.EMPOWERING_BLESSING);
		}
		if (hero.hasTalent(Talent.EVASIVE_BLESSING)) {
			talentPoints[2] += hero.pointsInTalent(Talent.EVASIVE_BLESSING);
		}
		if (hero.hasTalent(Talent.KEEN_BLESSING)) {
			talentPoints[3] += hero.pointsInTalent(Talent.KEEN_BLESSING);
		}
		if (hero.hasTalent(Talent.FERVENT_BLESSING)) {
			talentPoints[4] += hero.pointsInTalent(Talent.FERVENT_BLESSING);
		}

		boolean canStop = false;
		int numTalents = 0;
		do {
			int highestIndex = 0;
			for (int i = 0; i < 6; i += 1) {
				if (talentPoints[i] > talentPoints[highestIndex]) highestIndex = i;
			}

			if (talentPoints[highestIndex] > 0) {
				numTalents += 1;

				if (numTalents <= 2) volDeduction += talentPoints[highestIndex] - 1;
				else if (numTalents <= 4) volDeduction += Math.max(talentPoints[highestIndex] - 2, 0);
				else volDeduction += Math.max(talentPoints[highestIndex] - 3, 0);

				talentPoints[highestIndex] = 0;
			} else canStop = true;
		} while (!canStop);

		return volDeduction;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (volume > 0) {
			actions.add( AC_DRINK );
		}

		if ((volume >= MAX_VOLUME - getVolDeduction(hero)) && (hero.hasTalent(Talent.MIGHTY_BLESSING))) {
			actions.add(AC_BLESS);
		}

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_DRINK )) {

			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float) hero.HT);

				//each drop is worth 5% of total health
				float dropsNeeded = missingHealthPercent / 0.05f;

				//we are getting extra heal value, scale back drops needed accordingly
				if (dropsNeeded > 1.01f && VialOfBlood.delayBurstHealing()) {
					dropsNeeded /= VialOfBlood.totalHealMultiplier();
				}

				//add extra drops if we can gain shielding
				int curShield = 0;
				if (hero.buff(Barrier.class) != null)
					curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT * 0.2f * hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)) {
					float missingShieldPercent = 1f - (curShield / (float) maxShield);
					missingShieldPercent *= 0.2f * hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0) {
						dropsNeeded += missingShieldPercent / 0.05f;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsToConsume = (int) Math.ceil(dropsNeeded - 0.01f);
				dropsToConsume = (int) GameMath.gate(1, dropsToConsume, volume);

				if (Dewdrop.consumeDew(dropsToConsume, hero, true)) {
					volume -= dropsToConsume;
					Catalog.countUses(Dewdrop.class, dropsToConsume);

					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			} else {
				GLog.w(Messages.get(this, "empty"));
			}

		} else if (action.equals(AC_BLESS)) {
			if (hero.hasTalent(Talent.MIGHTY_BLESSING)) {
				hero.STR += 1;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.STRENGTH);

				GLog.p( Messages.get(this, "mighty_blessing", hero.STR()) );
			}

			if (hero.hasTalent(Talent.EMPOWERING_BLESSING)) {
				hero.wandDmgBonusFactor += 0.05f;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.MAGIC_DMG);

				GLog.p( Messages.get(this, "empowering_blessing", (int)(hero.wandDmgBonusFactor*100)) );
			}

			if (hero.hasTalent(Talent.EVASIVE_BLESSING)) {
				hero.evasionBonusFactor += 0.05f;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.MISS_EVA);

				GLog.p( Messages.get(this, "evasive_blessing", (int)(hero.evasionBonusFactor*100)) );
			}

			if (hero.hasTalent(Talent.KEEN_BLESSING)) {
				hero.accuracyBonusFactor += 0.05f;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.MISS_ACC);

				GLog.p( Messages.get(this, "keen_blessing", (int)(hero.accuracyBonusFactor*100)) );
			}

			if (hero.hasTalent(Talent.FERVENT_BLESSING)) {
				hero.meleeDmgBonusFactor += 0.05f;
				hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, "1", FloatingText.PHYS_DMG);

				GLog.p( Messages.get(this, "fervent_blessing", (int)(hero.accuracyBonusFactor*100)) );
			}

			volume = Math.max(0, volume - MAX_VOLUME + getVolDeduction(hero));
			Sample.INSTANCE.play(Assets.Sounds.DRINK);
			hero.sprite.operate(hero.pos);
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (volume == 0){
			info += "\n\n" + Messages.get(this, "desc_water");
		} else {
			info += "\n\n" + Messages.get(this, "desc_heal");
		}

		if (isFull()){
			info += "\n\n" + Messages.get(this, "desc_full");
		}

		return info;
	}

	public void empty() {
		volume = 0;
		updateQuickslot();
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public boolean isFull() {
		return volume >= MAX_VOLUME;
	}

	public void collectDew( Dewdrop dew ) {

		GLog.i( Messages.get(this, "collected") );
		volume += dew.quantity;
		if (volume >= MAX_VOLUME) {
			volume = MAX_VOLUME;
			GLog.p( Messages.get(this, "full") );
		}

		updateQuickslot();
	}

	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}

	@Override
	public String status() {
		return Messages.format( TXT_STATUS, volume, MAX_VOLUME );
	}

}
