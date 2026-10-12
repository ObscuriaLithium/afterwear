package dev.obscuria.afterwear.platform.neoforge;

//? neoforge {
/*import dev.obscuria.loot_journal.LootJournal;
import dev.obscuria.loot_journal.LootJournalClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = LootJournal.MOD_ID, dist = Dist.CLIENT)
public class NeoforgeClientEntrypoint {

	public NeoforgeClientEntrypoint(ModContainer container) {
		LootJournalClient.INSTANCE.onInitializeClient();
//		container.registerExtensionPoint(
//				IConfigScreenFactory.class,
//				NeoforgeClientEntrypoint::createConfigScreen);
	}

//	private static Screen createConfigScreen(ModContainer container, Screen parent) {
//		return ConfigScreen.INSTANCE.create(parent);
//	}
}
*///?}
