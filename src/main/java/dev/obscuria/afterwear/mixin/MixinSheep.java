package dev.obscuria.afterwear.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.BreakageHelperShared;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinSheep {

	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void mobInteract$onHead(
			Player player, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> cir
	) {
		var stack = player.getItemInHand(hand);
		if (!BreakageHelperShared.INSTANCE.shouldPassInteraction(stack)) return;
		cir.setReturnValue(InteractionResult.PASS);
	}
}
