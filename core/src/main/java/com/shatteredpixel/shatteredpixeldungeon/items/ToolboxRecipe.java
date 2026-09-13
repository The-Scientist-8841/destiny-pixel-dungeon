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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HeavyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.MiniBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ProximityBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.ArmorBracing;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.WeaponLacing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBlindingBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryEarthBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryFadeBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryFireBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryIceBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryMageBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryRotBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventorySorrowBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryStarBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryStormBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventorySunBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventorySwiftBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic.InventoryCleansingBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryExperienceBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryFlameBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryFrostBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryHasteBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryHealingBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryStealthBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryLevitationBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryMindBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryParalyticBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryPurifyingBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryStrongBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryToxicBullet;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;

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
			new InventoryFireBullet.Craft(),
			new InventoryIceBullet.Craft(),
			new InventorySunBullet.Craft(),
			new InventoryMageBullet.Craft(),
			new InventorySwiftBullet.Craft(),
			new InventoryEarthBullet.Craft(),
			new InventoryRotBullet.Craft(),
			new InventorySorrowBullet.Craft(),
			new InventoryStormBullet.Craft(),
			new InventoryFadeBullet.Craft(),
			new InventoryBlindingBullet.Craft()
	};

	private static ToolboxRecipe[] oneIngredientPotionRecipes = new ToolboxRecipe[]{
			new InventoryFlameBullet.Craft(),
			new InventoryFrostBullet.Craft(),
			new InventoryHealingBullet.Craft(),
			new InventoryParalyticBullet.Craft(),
			new InventoryToxicBullet.Craft(),
			new InventoryHasteBullet.Craft(),
			new InventoryExperienceBullet.Craft(),
			new InventoryLevitationBullet.Craft(),
			new InventoryMindBullet.Craft(),
			new InventoryPurifyingBullet.Craft(),
			new InventoryStrongBullet.Craft(),
			new InventoryStealthBullet.Craft(),
	};

	private static ToolboxRecipe[] oneIngredientExoticPotionRecipes = new ToolboxRecipe[]{
		new InventoryCleansingBullet.Craft()
	};
	
	private static ToolboxRecipe[] twoIngredientRecipes = new ToolboxRecipe[]{
			new HeavyBomb.HeavyBombCraft()
	};

	private static ToolboxRecipe[] twoIngredientSeedRecipes = new ToolboxRecipe[] {
			new InventoryStarBullet.Craft()
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

				if (Dungeon.hero.pointsInTalent(Talent.POTION_CRAFTING) >= 2) {
					for (ToolboxRecipe recipe : oneIngredientPotionRecipes){
						if (recipe.testIngredients(ingredients)){
							result.add(recipe);
						}
					}
				}

				if (Dungeon.hero.pointsInTalent(Talent.POTION_CRAFTING) >= 3) {
					for (ToolboxRecipe recipe : oneIngredientExoticPotionRecipes){
						if (recipe.testIngredients(ingredients)){
							result.add(recipe);
						}
					}
				}
			}
			
		} else if (ingredients.size() == 2){
			for (ToolboxRecipe recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					result.add(recipe);
				}
			}

			if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.POTION_CRAFTING)) {
				for (ToolboxRecipe recipe : twoIngredientSeedRecipes){
					if (recipe.testIngredients(ingredients)){
						result.add(recipe);
					}
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
		} else if (item instanceof Potion) {
			return true;
		} else {
			return item.isIdentified() && (!item.unique || item instanceof Rotberry.Seed);
		}
	}
}


