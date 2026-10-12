package dev.obscuria.afterwear.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.RepairHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {

	@Shadow private int repairItemCountCost;
	@Unique private boolean afterwear$materialRepair;

	public MixinAnvilMenu(
			@Nullable MenuType<?> type, int i, Inventory inventory,
			ContainerLevelAccess containerLevelAccess
	) {
		super(type, i, inventory, containerLevelAccess);
	}

	@Inject(method = "createResult", at = @At("HEAD"))
	private void createResult$onHead(CallbackInfo ci) {
		this.afterwear$materialRepair = false;
	}

	@ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean createResult$detectMaterialRepair(boolean original) {
		this.afterwear$materialRepair = original && RepairHelper.INSTANCE.shouldOverrideRepair();
		return original;
	}

	@WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V"))
	private void createResult$modifyRepairCost(DataSlot instance, int cost, Operation<Void> original) {
		original.call(instance, afterwear$materialRepair && cost > 0
				? RepairHelper.INSTANCE.getRepairCost(inputSlots.getItem(0), repairItemCountCost)
				: cost);
	}

	@WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setRepairCost(I)V"))
	private void createResult$preserveRepairCost(ItemStack instance, int cost, Operation<Void> original) {
		if (afterwear$materialRepair) return;
		original.call(instance, cost);
	}
}
