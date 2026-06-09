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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.ArcaneFirearm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class InventorySunBullet extends InventoryBullet {

	{
		image = ItemSpriteSheet.BULLET_SUNGRASS;
	}

	@Override
	public ArcaneFirearm.Bullet get_bullet() {
		return new SunBullet();
	}

	@Override
	public String desc() {
		SunBullet b = new SunBullet();
		ArcaneFirearm gun = Dungeon.hero == null ? null : Dungeon.hero.belongings.getItem(ArcaneFirearm.class);
		int lvl = gun == null ? 1 : gun.buffedLvl();

		return Messages.get(this, "desc", 3*b.min(lvl), 3*b.max(lvl), b.min(lvl), b.max(lvl));
	}

	public static class SunBullet extends ArcaneFirearm.Bullet {
		{
			baseDmg = 3;
			scalingFactorMin = 1f;
			scalingFactorMax = 2f;
			maxFactor = 2f;
			parentClass = InventorySunBullet.class;
		}

		@Override
		public InventoryBullet get_inventory_bullet() {
			return new InventorySunBullet();
		}

		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (attacker.alignment == defender.alignment) {
				int heal = damageRoll(attacker) + damageRoll(attacker) + damageRoll(attacker);
				defender.HP = Math.min(defender.HP + heal, defender.HT);
				if (defender.sprite != null) {
					defender.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
				}
				return 0;
			} else return super.proc(attacker, defender, damage);
		}
	}

	public static class SunBulletCraft extends ToolboxRecipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0).getClass().equals(Sungrass.Seed.class);
        }

		@Override
		public int cost(ArrayList<Item> ingredients) { return 1; }

		@Override
		public Item craft(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item i : ingredients) { i.quantity(i.quantity() - 1); }

			InventorySunBullet bullets = new InventorySunBullet();
			bullets.quantity(5);
			return bullets;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			InventorySunBullet bullets = new InventorySunBullet();
			bullets.quantity(5);
			return bullets;
		}
	}
}
