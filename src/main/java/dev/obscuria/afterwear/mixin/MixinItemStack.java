package dev.obscuria.afterwear.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.BreakageHelperShared;
import dev.obscuria.afterwear.common.helper.DegradationHelperShared;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinItemStack {

	@Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
	private void onInteract(
			Player player, LivingEntity entity, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> info
	) {
		var self = (ItemStack) (Object) this;
		if (!BreakageHelperShared.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResult.PASS);
	}

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void onUse(
			Level level, Player player, InteractionHand hand,
			CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info
	) {
		var self = (ItemStack) (Object) this;
		if (!BreakageHelperShared.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResultHolder.pass(self));
	}

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void onUseOn(
			UseOnContext context, CallbackInfoReturnable<InteractionResult> info
	) {
		final var self = (ItemStack) (Object) this;
		if (!BreakageHelperShared.INSTANCE.shouldPassInteraction(self)) return;
		info.setReturnValue(InteractionResult.PASS);
	}

	@ModifyReturnValue(method = "getDestroySpeed", at = @At("RETURN"))
	private float getDestroySpeed$modifyReturnValue(float original) {
		return DegradationHelperShared.INSTANCE.degradeDestroySpeed((ItemStack) (Object) this, original);
	}

	@ModifyReturnValue(method = "getTooltipLines", at = @At("RETURN"))
	private List<Component> getTooltipLines$modifyReturnValue(List<Component> original) {
		return BreakageHelperShared.INSTANCE.modifyTooltip((ItemStack) (Object) this, original);
	}
}
