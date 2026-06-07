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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Toolbox;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HeavyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.MiniBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ProximityBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.ArmorBracing;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.WeaponLacing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryFireBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryRotBullet;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class ToolboxRecipe {
	
	public abstract boolean testIngredients(ArrayList<Item> ingredients);
	
	public abstract int cost(ArrayList<Item> ingredients);
	
	public abstract Item craft(ArrayList<Item> ingredients);
	
	public abstract Item sampleOutput(ArrayList<Item> ingredients);
	
	//*******
	// Static members
	//*******

	private static ToolboxRecipe[] zeroIngredientRecipes = new ToolboxRecipe[]{
			new InventoryBullet.BulletCraft(),
			new Bomb.BombCraft(),
			new WeaponLacing.WeaponLacingCraft(),
			new ArmorBracing.ArmorBracingCraft()
	};
	
	private static ToolboxRecipe[] oneIngredientRecipes = new ToolboxRecipe[]{
			new MiniBomb.MiniBombCraft(),
			new ProximityBomb.ProximityBombCraft()
	};

	private static ToolboxRecipe[] oneIngredientSeedRecipes = new ToolboxRecipe[]{
			new InventoryRotBullet.RotBulletCraft(),
			new InventoryFireBullet.FireBulletCraft()
	};
	
	private static ToolboxRecipe[] twoIngredientRecipes = new ToolboxRecipe[]{
			new HeavyBomb.HeavyBombCraft()
	};
	
	private static ToolboxRecipe[] threeIngredientRecipes = new ToolboxRecipe[]{

	};
	
	public static ArrayList<ToolboxRecipe> findRecipes(ArrayList<Item> ingredients){

		ArrayList<ToolboxRecipe> result = new ArrayList<>();

		if (ingredients.size() == 0) {
			for (ToolboxRecipe recipe : zeroIngredientRecipes) {
				if (recipe.testIngredients(ingredients)) {
					result.add(recipe);
				}
			}
		} else if (ingredients.size() == 1){
			for (ToolboxRecipe recipe : oneIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
			if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.POTION_CRAFTING)) {
				for (ToolboxRecipe recipe : oneIngredientSeedRecipes){
					if (recipe.testIngredients(ingredients)){
						result.add(recipe);
					}
				}
			}
			
		} else if (ingredients.size() == 2){
			for (ToolboxRecipe recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
			
		} else if (ingredients.size() == 3){
			for (ToolboxRecipe recipe : threeIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}
		}
		
		return result;
	}
	
	public static boolean usableInRecipe(Item item){
		if (item instanceof EquipableItem){
			return false;
		} else if (item instanceof Wand) {
			return false;
		} else {
			return item.isIdentified() && (!item.unique || item instanceof Rotberry.Seed);
		}
	}
}


