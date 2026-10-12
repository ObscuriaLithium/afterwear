package dev.obscuria.afterwear.platform.forge;

//? forge {
import dev.obscuria.afterwear.Afterwear;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Afterwear.MOD_ID)
public class ForgeEntrypoint {

	public ForgeEntrypoint() {
		Afterwear.INSTANCE.onInitialize();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgeClientEntrypoint::init);
	}
}
//?}
