package dev.obscuria.afterwear.platform.neoforge;

//? neoforge {
/*import dev.obscuria.afterwear.Afterwear;
import dev.obscuria.afterwear.AfterwearClient;
import dev.obscuria.afterwear.config.ConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Afterwear.MOD_ID, dist = Dist.CLIENT)
public class NeoforgeClientEntrypoint {

	public NeoforgeClientEntrypoint(ModContainer container) {
		AfterwearClient.INSTANCE.onInitializeClient();
		container.registerExtensionPoint(
				IConfigScreenFactory.class,
				NeoforgeClientEntrypoint::createConfigScreen);
	}

	private static Screen createConfigScreen(ModContainer container, Screen parent) {
		return ConfigScreen.INSTANCE.create(parent);
	}
}
*///?}
