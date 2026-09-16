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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneMaterial;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HeavyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.MiniBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ProximityBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.ArmorBracing;
import com.shatteredpixel.shatteredpixeldungeon.items.modifications.WeaponLacing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryPotionBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryPurifyingBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryStrongBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.InventoryToxicBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic.InventoryCorrosiveBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic.InventoryDivineBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic.InventoryDragonBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.potion_bullets.exotic.InventoryEarthenBullet;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ToolboxCraftingScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class QuickToolboxRecipe extends Component {

	private ArrayList<Item> ingredients;

	private ArrayList<ItemSlot> inputs;
	private QuickToolboxRecipe.arrow arrow;
	private ItemSlot output;

	public QuickToolboxRecipe(ToolboxRecipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		if (inputs.size() > 0) {
			for (final Item in : inputs) {
				anonymize(in);
				ItemSlot curr;
				curr = new ItemSlot(in) {
					{
						hotArea.blockLevel = PointerArea.NEVER_BLOCK;
					}

					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(in));
					}
				};

				int quantity = 0;
				if (Dungeon.hero != null) {
					ArrayList<Item> similar = Dungeon.hero.belongings.getAllSimilar(in);
					for (Item sim : similar) {
						//if we are looking for a specific item, it must be IDed
						if (sim.getClass() != in.getClass() || sim.isIdentified())
							quantity += sim.quantity();
					}
					if (quantity < in.quantity()) {
						curr.sprite.alpha(0.3f);
						hasInputs = false;
					}
				} else {
					hasInputs = false;
				}

				curr.showExtraInfo(false);
				add(curr);
				this.inputs.add(curr);
			}
		} else {
			Item in = new ArcaneMaterial();
			in.quantity(cost);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				{
					hotArea.blockLevel = PointerArea.NEVER_BLOCK;
				}

				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(in));
				}
			};

			if (Dungeon.hero != null) {
				if (Dungeon.materials < cost) {
					curr.sprite.alpha(0.3f);
					hasInputs = false;
				}
			} else {
				hasInputs = false;
			}

			curr.showExtraInfo(false);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0 && inputs.size() > 0) {
			arrow = new arrow(Icons.get(Icons.ARROW), cost);
			arrow.hardlightText(0xFFEEEE);
		} else {
			arrow = new arrow(Icons.get(Icons.ARROW));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(ShatteredPixelDungeon.scene() instanceof ToolboxCraftingScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (Dungeon.hero != null && !hasInputs){
			this.output.sprite.alpha(0.3f);
		}
		this.output.showExtraInfo(false);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;

		int padding = inputs.size() == 1 ? 8 : 0;

		for (ItemSlot item : inputs){
			item.setRect(x + width + padding, y, 16, 16);
			width += 16 + padding;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;

		width += padding;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class arrow extends IconButton {
		
		BitmapText text;
		
		public arrow(){
			super();
		}
		
		public arrow( Image icon ){
			super( icon );
		}
		
		public arrow( Image icon, int count ){
			super( icon );
			hotArea.blockLevel = PointerArea.NEVER_BLOCK;

			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onPointerUp() {
			icon.brightness(1f);
		}

		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((ToolboxCraftingScene)ShatteredPixelDungeon.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}

	private static QuickToolboxRecipe basicOneIngredientRecipeHelper(ToolboxRecipe craft, Item result, Item ingredient) {
		ArrayList<Item> ingredients = new ArrayList<Item>();
		ingredients.add(ingredient);
		return new QuickToolboxRecipe(craft, ingredients, result);
	}

	private static QuickToolboxRecipe potionRecipeHelper(ToolboxRecipe craft, InventoryPotionBullet result, Potion ingredient) {
		ArrayList<Item> ingredients = new ArrayList<Item>();
		ingredient.anonymize();
		ingredients.add(ingredient);
		result.anonymize();
		result.quantity(5);
		return new QuickToolboxRecipe(craft, ingredients, result);
	}

	private static QuickToolboxRecipe exoticPotionRecipeHelper(ToolboxRecipe craft, InventoryPotionBullet result, Potion ingredient) {
		ArrayList<Item> ingredients = new ArrayList<Item>();
		ingredient.anonymize();
		ingredients.add(ingredient);
		result.anonymize();
		result.quantity(5);
		return new QuickToolboxRecipe(craft, ingredients, result);
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickToolboxRecipe> getRecipes(int pageIdx ){
		ArrayList<QuickToolboxRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				break;
			case 1:
				result.add(new QuickToolboxRecipe(new InventoryBullet.BulletCraft(), new ArrayList<Item>(), new InventoryBullet().quantity(2)));

				if (Dungeon.hero == null || Dungeon.hero.hasTalent(Talent.POTION_CRAFTING)) {
					result.add(null);

					result.add(basicOneIngredientRecipeHelper(new InventoryFireBullet.Craft(), new InventoryFireBullet().quantity(3), new Firebloom.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryIceBullet.Craft(), new InventoryIceBullet().quantity(3), new Icecap.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventorySunBullet.Craft(), new InventorySunBullet().quantity(5), new Sungrass.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryMageBullet.Craft(), new InventoryMageBullet().quantity(4), new Mageroyal.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventorySwiftBullet.Craft(), new InventorySwiftBullet().quantity(3), new Swiftthistle.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryEarthBullet.Craft(), new InventoryEarthBullet().quantity(3), new Earthroot.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryRotBullet.Craft(), new InventoryRotBullet().quantity(4), new Rotberry.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventorySorrowBullet.Craft(), new InventorySorrowBullet().quantity(3), new Sorrowmoss.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryStormBullet.Craft(), new InventoryStormBullet().quantity(3), new Stormvine.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryStarBullet.Craft(), new InventoryStarBullet(), new Starflower.Seed().quantity(2)));
					result.add(basicOneIngredientRecipeHelper(new InventoryFadeBullet.Craft(), new InventoryFadeBullet().quantity(2), new Fadeleaf.Seed()));
					result.add(basicOneIngredientRecipeHelper(new InventoryBlindingBullet.Craft(), new InventoryBlindingBullet().quantity(3), new Blindweed.Seed()));

					if (Dungeon.hero == null || Dungeon.hero.pointsInTalent(Talent.POTION_CRAFTING) >= 2) {
						result.add(potionRecipeHelper(new InventoryFlameBullet.Craft(), new InventoryFlameBullet(), new PotionOfLiquidFlame()));
						result.add(potionRecipeHelper(new InventoryFrostBullet.Craft(), new InventoryFrostBullet(), new PotionOfFrost()));
						result.add(potionRecipeHelper(new InventoryHealingBullet.Craft(), new InventoryHealingBullet(), new PotionOfHealing()));
						result.add(potionRecipeHelper(new InventoryParalyticBullet.Craft(), new InventoryParalyticBullet(), new PotionOfParalyticGas()));
						result.add(potionRecipeHelper(new InventoryToxicBullet.Craft(), new InventoryToxicBullet(), new PotionOfToxicGas()));
						result.add(potionRecipeHelper(new InventoryHasteBullet.Craft(), new InventoryHasteBullet(), new PotionOfHaste()));
						result.add(potionRecipeHelper(new InventoryExperienceBullet.Craft(), new InventoryExperienceBullet(), new PotionOfExperience()));
						result.add(potionRecipeHelper(new InventoryLevitationBullet.Craft(), new InventoryLevitationBullet(), new PotionOfLevitation()));
						result.add(potionRecipeHelper(new InventoryMindBullet.Craft(), new InventoryMindBullet(), new PotionOfMindVision()));
						result.add(potionRecipeHelper(new InventoryPurifyingBullet.Craft(), new InventoryPurifyingBullet(), new PotionOfPurity()));
						result.add(potionRecipeHelper(new InventoryStrongBullet.Craft(), new InventoryStrongBullet(), new PotionOfStrength()));
						result.add(potionRecipeHelper(new InventoryStealthBullet.Craft(), new InventoryStealthBullet(), new PotionOfInvisibility()));
					}

					if (Dungeon.hero == null || Dungeon.hero.pointsInTalent(Talent.POTION_CRAFTING) >= 3) {
						result.add(exoticPotionRecipeHelper(new InventoryCleansingBullet.Craft(), new InventoryCleansingBullet(), new PotionOfCleansing()));
						result.add(exoticPotionRecipeHelper(new InventoryCorrosiveBullet.Craft(), new InventoryCorrosiveBullet(), new PotionOfCorrosiveGas()));
						result.add(exoticPotionRecipeHelper(new InventoryDivineBullet.Craft(), new InventoryDivineBullet(), new PotionOfDivineInspiration()));
						result.add(exoticPotionRecipeHelper(new InventoryDragonBullet.Craft(), new InventoryDragonBullet(), new PotionOfDragonsBreath()));
						result.add(exoticPotionRecipeHelper(new InventoryEarthenBullet.Craft(), new InventoryEarthenBullet(), new PotionOfEarthenArmor()));
					}
				}
				break;
			case 2:
				result.add(new QuickToolboxRecipe(new Bomb.BombCraft(), new ArrayList<Item>(), new Bomb()));

				result.add(null);
				ArrayList<Item> ingredients = new ArrayList<Item>();
				ingredients.add(new Bomb());
				result.add(new QuickToolboxRecipe(new MiniBomb.MiniBombCraft(), ingredients, new MiniBomb().quantity(5)));

				ingredients.clear();
				ingredients.add(new Bomb().quantity(2));
				result.add(new QuickToolboxRecipe(new HeavyBomb.HeavyBombCraft(), ingredients, new HeavyBomb()));

				ingredients.clear();
				ingredients.add(new Bomb());
				result.add(new QuickToolboxRecipe(new ProximityBomb.ProximityBombCraft(), ingredients, new ProximityBomb()));

				break;
			case 3:
				result.add(new QuickToolboxRecipe(new WeaponLacing.WeaponLacingCraft(), new ArrayList<Item>(), new WeaponLacing()));
				result.add(new QuickToolboxRecipe(new ArmorBracing.ArmorBracingCraft(), new ArrayList<Item>(), new ArmorBracing()));
				break;
		}

		return result;
	}
	
}
