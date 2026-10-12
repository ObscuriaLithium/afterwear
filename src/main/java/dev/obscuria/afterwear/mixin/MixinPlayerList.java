package dev.obscuria.afterwear.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.config.CommonConfig;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinPlayerList {

	@Inject(method = "reloadResources", at = @At("TAIL"))
	private void reloadResources$onTail(CallbackInfo ci) {
		CommonConfig.Cache.INSTANCE.invalidate();
	}
}
