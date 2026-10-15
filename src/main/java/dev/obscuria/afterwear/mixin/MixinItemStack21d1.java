package dev.obscuria.afterwear.mixin;

//? 1.21.1 {
/*import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.BreakageHelper;
import dev.obscuria.afterwear.common.helper.DegradationHelper;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinItemStack21d1 {

	@Inject(
			//? fabric
			method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
			//? neoforge
			//method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"), cancellable = true)
	private void hurtAndBreak$override(
			int amount,
			ServerLevel level,
			//? fabric
			net.minecraft.server.level.ServerPlayer entity,
			//? neoforge
			//net.minecraft.world.entity.LivingEntity entity,
			Consumer<Item> consumer,
			CallbackInfo ci
	) {
		var self = (ItemStack) (Object) this;
		if (!BreakageHelper.INSTANCE.hurtAndBreak(self, entity, level, amount, consumer)) return;
		ci.cancel();
	}

	@ModifyVariable(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", at = @At("HEAD"), index = 2, argsOnly = true)
	private BiConsumer<Holder<Attribute>, AttributeModifier> forEachModifierBySlot$modifyConsumer(
			BiConsumer<Holder<Attribute>, AttributeModifier> consumer
	) {
		var self = (ItemStack) (Object) this;
		return DegradationHelper.INSTANCE.forEachModifierDegraded(self, consumer);
	}

	@ModifyVariable(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V", at = @At("HEAD"), index = 2, argsOnly = true)
	private BiConsumer<Holder<Attribute>, AttributeModifier> forEachModifierBySlotGroup$modifyConsumer(
			BiConsumer<Holder<Attribute>, AttributeModifier> consumer
	) {
		var self = (ItemStack) (Object) this;
		return DegradationHelper.INSTANCE.forEachModifierDegraded(self, consumer);
	}
}
*///?}
