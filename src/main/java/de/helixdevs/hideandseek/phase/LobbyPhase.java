package de.helixdevs.hideandseek.phase;

import de.helixdevs.hideandseek.HideAndSeek;
import de.helixdevs.hideandseek.api.Countdown;
import de.helixdevs.hideandseek.api.Game;
import de.helixdevs.hideandseek.api.GamePhase;
import de.helixdevs.hideandseek.api.event.GameJoinEvent;
import de.helixdevs.hideandseek.api.event.GamePreJoinEvent;
import de.helixdevs.hideandseek.api.event.GameQuitEvent;
import de.helixdevs.hideandseek.service.BlockSelectionService;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;

public class LobbyPhase extends GamePhase {

    private Countdown countdown;

    public LobbyPhase(JavaPlugin plugin, Game game) {
        super(plugin, game);
    }

    @Override
    public void onStart() {
        this.countdown = new Countdown(getPlugin(), 30) {
            @Override
            public void notify(int counter) {
                if (counter % 30 == 0 || counter == 15 || counter == 10 || counter <= 5) {
                    getGame().getAudience().sendMessage(Component.text()
                            .append(HideAndSeek.prefix)
                            .append(Component.text("Das Spiel startet in ").color(NamedTextColor.GRAY))
                            .append(Component.text(counter).color(NamedTextColor.RED))
                            .append(Component.text(counter == 1 ? " Sekunde!" : " Sekunden!").color(NamedTextColor.GRAY)));
                }
                for (Player player : getGame().getPlayers()) {
                    player.setLevel(counter);
                    player.setExp((float) counter / (float) getStart());
                }
            }

            @Override
            public void finish() {
                for (Player player : getGame().getPlayers()) {
                    player.setLevel(0);
                    player.setExp(0);
                }

                getGame().getAudience().sendMessage(Component.text("End!"));
                try {
                    nextPhase();
                } catch (IOException e) {
                    e.printStackTrace();
                    getGame().getAudience().sendMessage(Component.text("Failed to jump to next phase."));
                }
            }
        };

        if (getGame().getPlayerCount() >= HideAndSeek.minPlayers) {
            this.countdown.start();
        }
    }

    @EventHandler
    public void preJoinGame(GamePreJoinEvent event) {
        if (event.getGame().getPlayers().size() >= getGame().getMaxPlayers())
            event.setCancelled(true);
    }

    @EventHandler
    public void joinGame(GameJoinEvent event) {
        Player player = event.getPlayer();

        event.getGame().getAudience().sendMessage(Component.text()
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("+").color(NamedTextColor.GREEN))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(player.displayName().colorIfAbsent(NamedTextColor.GRAY))
                .build());

        if (getGame().getPlayerCount() >= HideAndSeek.minPlayers) {
            this.countdown.start();
        }
    }

    @EventHandler
    public void quitGame(GameQuitEvent event) {
        Player player = event.getPlayer();

        event.getGame().getAudience().sendMessage(Component.text()
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("-").color(NamedTextColor.RED))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(player.displayName().colorIfAbsent(NamedTextColor.GRAY))
                .build());


        if (getGame().getPlayers().size() < HideAndSeek.minPlayers) {
            this.countdown.reset();
            // TODO: Send message
        }
    }

    @EventHandler
    public void onChangeFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.VOID)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        MetadataStore data = npc.data();
        Boolean value = data.get("selector");
        if (value == null)
            return;
        if (!value)
            return;
        Player clicker = event.getClicker();

        BlockSelectionService service = getService(BlockSelectionService.class);

        Inventory test = Bukkit.createInventory(null, 9, "test");
        BlockData blockData;

    }

    @Override
    public void onEnd() {
        this.countdown.reset();
    }
}
