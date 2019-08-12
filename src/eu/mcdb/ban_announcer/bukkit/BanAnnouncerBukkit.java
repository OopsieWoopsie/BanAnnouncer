package eu.mcdb.ban_announcer.bukkit;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import eu.mcdb.ban_announcer.BanAnnouncer;
import eu.mcdb.ban_announcer.bukkit.listener.AdvancedBanListener;
import eu.mcdb.ban_announcer.config.Config;
import eu.mcdb.ban_announcer.listener.LiteBans;
import eu.mcdb.util.ServerType;

public final class BanAnnouncerBukkit extends JavaPlugin {

    private BanAnnouncer banAnnouncer;
    private static BanAnnouncerBukkit instance;

    @Override
    public void onEnable() {
        instance = this;
        this.banAnnouncer = new BanAnnouncer(getLogger());
        getLogger().info("The pĺugin will start in 5 seconds...");
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> enable(), 5 * 20);
    }

    private void enable() {
        Config config = new Config(ServerType.BUKKIT);
        switch (config.getPunishmentsManager()) {
        case "auto":
            if (usingLiteBans()) {
                getLogger().info("[AutoDetect] Using LiteBans as the punishment manager.");
                new LiteBans(banAnnouncer);
            } else if (usingAdvancedBan()) {
                getLogger().info("[AutoDetect] Using AdvancedBan as the punishment manager.");
                getServer().getPluginManager().registerEvents(new AdvancedBanListener(), this);
            } else {
                getLogger().severe("[AutoDetect] No compatible plugin found. BanAnnouncer will not work!.");
            }
            break;
        case "advancedban":
            if (usingAdvancedBan()) {
                getLogger().info("Using AdvancedBan as the punishment manager.");
                getServer().getPluginManager().registerEvents(new AdvancedBanListener(), this);
            } else {
                getLogger()
                        .severe("You choose AdvancedBan but you don't have it installed, BanAnnouncer will not work!.");
            }
            break;
        case "litebans":
            if (usingLiteBans()) {
                getLogger().info("Using LiteBans as the punishment manager.");
                new LiteBans(banAnnouncer);
            } else {
                getLogger().severe("You choose LiteBans but you don't have it installed, BanAnnouncer will not work!.");
            }
            break;
        default:
            getLogger().severe("The punishment manager '" + config.getPunishmentsManager()
                    + "' is not compatible with BanAnnouncer, you can request the integration"
                    + " with it on https://github.com/OopsieWoopsie/BanAnnouncer/issues");
            break;
        }
    }

    private boolean usingLiteBans() {
        try {
            Class.forName("litebans.api.Events");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean usingAdvancedBan() {
        try {
            Class.forName("me.leoko.advancedban.Universal");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onDisable() {
        banAnnouncer.disable();
        this.banAnnouncer = null;
    }

    public static BanAnnouncerBukkit getInstance() {
        return instance;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }
}
