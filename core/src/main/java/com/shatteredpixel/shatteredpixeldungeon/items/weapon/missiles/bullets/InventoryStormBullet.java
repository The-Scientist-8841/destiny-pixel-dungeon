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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class InventoryStormBullet extends InventoryBullet {

	{
		image = ItemSpriteSheet.BULLET_STORMVINE;
	}

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new StormBullet();
	}

	public static class StormBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 3;
			scalingFactorMin = 1f;
			scalingFactorMax = 2f;
			maxFactor = 2f;
			parentClass = InventoryStormBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventoryStormBullet();
		}

		@Override
		public void onHit(Char attacker, Char defender) {
			if (defender != null) {
				ArrayList<Mob> mobsToDamage = new ArrayList<Mob>();
				for (Mob m : Dungeon.level.mobs) {
					if (m.alignment != attacker.alignment && m != defender && Dungeon.level.distance(defender.pos, m.pos) <= 2) {
						if (defender.sprite != null && m.sprite != null) {
							defender.sprite.parent.add(
								new Lightning(
									defender.sprite.center(),
									m.sprite.destinationCenter(),
									new Callback() {
										@Override
										public void call() { /*Do nothing*/ }
									}
								)
							);
						}
						mobsToDamage.add(m);
					}
				}
				Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
				for (Mob m : mobsToDamage) { m.damage(damageRoll(attacker), this); }
			}
		}
	}

	public static class StormBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Stormvine.Seed.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventoryStormBullet bullets = new InventoryStormBullet();
			bullets.quantity(3);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventoryStormBullet bullets = new InventoryStormBullet();
			bullets.quantity(3);
			return bullets;
		}
	}
}
