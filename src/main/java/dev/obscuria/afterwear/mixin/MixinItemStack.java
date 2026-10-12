package dev.obscuria.afterwear.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.BreakageHelper;
import dev.obscuria.afterwear.common.helper.DegradationHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinItemStack {

	@Inject(method = "hurtAndBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"), cancellable = true)
	private <T extends LivingEntity> void hurtAndBreak$override(
			int amount, T entity, Consumer<T> onBroken, CallbackInfo info
	) {
		if (!BreakageHelper.INSTANCE.hurtAndBreak((ItemStack) (Object) this, entity, amount)) return;
		info.cancel();
	}

	@Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
	private void onInteract(
			Player player, LivingEntity entity, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> info
	) {
		var self = (ItemStack) (Object) this;
		if (!BreakageHelper.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResult.PASS);
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void onUse(
			Level level, Player player, InteractionHand hand,
			CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info
	) {
		var self = (ItemStack) (Object) this;
		if (!BreakageHelper.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResultHolder.pass(self));
	}

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void onUseOn(
			UseOnContext context, CallbackInfoReturnable<InteractionResult> info
	) {
		final var self = (ItemStack) (Object) this;
		if (!BreakageHelper.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResult.PASS);
	}

	@ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
	private float getDestroySpeed$modifyReturnValue(float original) {
		return DegradationHelper.INSTANCE.degradeDestroySpeed((ItemStack) (Object) this, original);
	}

	@ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
	private Multimap<Attribute, AttributeModifier> getAttributeModifiers$modifyReturnValue(
			Multimap<Attribute, AttributeModifier> original,
			@Local(argsOnly = true) EquipmentSlot slot
	) {
		return DegradationHelper.INSTANCE.degradeAttributes((ItemStack) (Object) this, slot, original);
	}

	@ModifyReturnValue(method = "getTooltipLines", at = @At("RETURN"))
	private List<Component> getTooltipLines$modifyReturnValue(List<Component> original) {
		return BreakageHelper.INSTANCE.modifyTooltip((ItemStack) (Object) this, original);
	}
}
