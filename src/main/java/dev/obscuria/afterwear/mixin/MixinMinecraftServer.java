package dev.obscuria.afterwear.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.config.CommonConfig;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinMinecraftServer {

	@Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;"))
	private void runServer$onBuildServerStatus(CallbackInfo ci) {
		CommonConfig.Cache.INSTANCE.invalidate();
	}
}
