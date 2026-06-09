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
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.ToolboxRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HeavyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.MiniBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ProximityBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
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
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryFireBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryIceBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryRotBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventoryStormBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventorySunBullet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.bullets.InventorySwiftBullet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ToolboxCraftingScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickToolboxRecipe> getRecipes(int pageIdx ){
		ArrayList<QuickToolboxRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				break;
			case 1:
				result.add(new QuickToolboxRecipe(new InventoryBullet.BulletCraft(), new ArrayList<Item>(), new InventoryBullet().quantity(2)));

				if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.POTION_CRAFTING)) {
					result.add(null);
					ArrayList<Item> ingredients = new ArrayList<Item>();
					ingredients.add(new Rotberry.Seed());
					result.add(new QuickToolboxRecipe(new InventoryRotBullet.RotBulletCraft(), ingredients, new InventoryRotBullet().quantity(4)));

					ingredients.clear();
					ingredients.add(new Firebloom.Seed());
					result.add(new QuickToolboxRecipe(new InventoryFireBullet.FireBulletCraft(), ingredients, new InventoryFireBullet().quantity(3)));

					ingredients.clear();
					ingredients.add(new Icecap.Seed());
					result.add(new QuickToolboxRecipe(new InventoryIceBullet.IceBulletCraft(), ingredients, new InventoryIceBullet().quantity(3)));

					ingredients.clear();
					ingredients.add(new Swiftthistle.Seed());
					result.add(new QuickToolboxRecipe(new InventorySwiftBullet.SwiftBulletCraft(), ingredients, new InventorySwiftBullet().quantity(3)));

					ingredients.clear();
					ingredients.add(new Sungrass.Seed());
					result.add(new QuickToolboxRecipe(new InventorySunBullet.SunBulletCraft(), ingredients, new InventorySunBullet().quantity(5)));

					ingredients.clear();
					ingredients.add(new Stormvine.Seed());
					result.add(new QuickToolboxRecipe(new InventoryStormBullet.StormBulletCraft(), ingredients, new InventoryStormBullet().quantity(3)));
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
