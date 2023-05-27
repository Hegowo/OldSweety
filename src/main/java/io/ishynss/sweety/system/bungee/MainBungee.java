package io.ishynss.sweety.system.bungee;

import io.ishynss.sweety.system.bungee.Commands.CommandADM;
import io.ishynss.sweety.system.bungee.Commands.CommandFriends;
import io.ishynss.sweety.system.bungee.Commands.CommandPTP;
import io.ishynss.sweety.system.bungee.sql.DatabaseManager;
import io.ishynss.sweety.system.bungee.utils.SQL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MainBungee extends Plugin {









	// SET CONFIG ELEMENTS IN SQL.GetInfos !!!!!





	public static Map<ProxiedPlayer, Integer> Play15m = new HashMap<>();


	private static MainBungee INSTANCE;
	public static HashMap<String, String> datababase = new HashMap<>();

	public File theFile = new File(getDataFolder(), "config.yml");
	public Configuration theConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(theFile);

	public MainBungee() throws IOException {
	}

	@Override
	public void onLoad() {

		super.onEnable();
		INSTANCE = this;


		try { // CREATE CONFIG

			File ok = new File(getDataFolder(), "config.yml");
			if (!ok.exists()) {
				ok.getParentFile().mkdir();
				ok.createNewFile();
				Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ok);
				configuration.set("database.ip", "localhost");
				configuration.set("database.port", 3306);
				configuration.set("database.password", "'localhost'");
				configuration.set("database.user", "user");
				configuration.set("database.name", "dbname");

				configuration.set("Staff.1", "Administrateur");
				configuration.set("Staff.2", "Super-Modo");
				configuration.set("Staff.3", "Modérateur");
				configuration.set("Staff.4", "Assistant");
				configuration.set("Staff.5", "Constructeur");
				configuration.set("Staff.6", "Graphiste");
				configuration.set("Staff.7", "STAFF7");
				configuration.set("Staff.8", "STAFF8");
				configuration.set("Staff.9", "STAFF9");


				configuration.set("Messages.SQL.Error", "§dSatsuki §8| §cUne erreur de type SQL s'est produite.");


				configuration.set("Messages.NoPlayer", "§dSatsuki §8| §cCe joueur n'existe pas.");

				configuration.set("Messages.ADM.History.Empty", "§dSatsuki §8| §cCe joueur est clean.");


				configuration.set("Messages.ADM.Ban.Syntaxe", "§dSatsuki §8| §cAide: /adm ban <joueur> <temps> <unit> <raison>");



				configuration.set("Messages.SQL.TimestampCountry", "fr");
				configuration.set("Messages.SQL.TimestampLanguage", "FR");


				configuration.set("Messages.ADM.Friends.EmptyFriends", "§dSatsuki §8| §cCe joueur n'a pas d'amis.");
				configuration.set("Messages.ADM.Friends.Syntaxe", "§dSatsuki §8| §cAide: /adm friends <joueur>.");
				configuration.set("Messages.ADM.Friends.Infos.1", "[Infos]");
				configuration.set("Messages.ADM.Friends.Infos.2", "Voir le profil de %target ?");
				configuration.set("Messages.ADM.Friends.Infos.ChatColor2", "WHITE");
				configuration.set("Messages.ADM.Friends.Infos.ChatColor1", "LLIGHT_PURPLE");
				configuration.set("Messages.ADM.Friends.Infos.Each", "§8- §7%player §d");
				configuration.set("Messages.ADM.Friends.Infos.Title", "§dSatsuki §8| §7Amis de §d%target");

				configuration.set("Messages.PTP.Syntaxe", "§dSatsuki §8| §cAide: /ptp <joueur>");
				configuration.set("Messages.PTP.Teleported", "§dSatsuki §8| §7Téléporation vers §d%target §7à travers le serveur §d%serverdestination §7!");
				configuration.set("Messages.PTP.TP", "§dSatsuki §8| §7Téléporation vers §d%player §7!");


				configuration.set("Messages.Staffs.Line1", "§6--------------------------");
				configuration.set("Messages.Staffs.Line2", "§dSatsuki §8| §7Membres du Staff en ligne");
				configuration.set("Messages.Staffs.Base", "§7- §r%name §f<> §8(§7%server§8)");
				configuration.set("Messages.Staffs.Line3", "§6--------------------------");


				configuration.set("Messages.SQL.YourAccountNotExist", "§dSatsuki §8| §cVotre compte ne semble pas exister.");
				configuration.set("Messages.SQL.PlayerDontExist", "§dSatsuki §8| §cCe joueur n'existe pas.");


				configuration.set("Messages.SQL.Money1", "Yen");
				configuration.set("Messages.SQL.SetMoney1", "§dSatsuki §8| §8(§5Yen§8) §7Valeur édité sur §a%amount §7pour le joueur joueur §a%target§7.");
				configuration.set("Messages.SQL.DefaultValueMoney1", 0.0);

				configuration.set("Messages.SQL.Money2", "Sagesse");
				configuration.set("Messages.SQL.SetMoney2", "§dSatsuki §8| §8(§5Sagesse§8) §7Valeur édité sur §a%amount §7pour le joueur joueur §a%target§7.");
				configuration.set("Messages.SQL.DefaultValueMoney2", 0.0);


				configuration.set("Messages.SQL.Money3", "Cerise");
				configuration.set("Messages.SQL.SetMoney3", "§dSatsuki §8| §8(§5Cerise§8) §7Valeur édité sur §a%amount §7pour le joueur joueur §a%target§7.");
				configuration.set("Messages.SQL.DefaultValueMoney3", 0.0);

				configuration.set("Messages.SQL.IPDontExist", "§dSatsuki §8| §cCette IP n'existe pas.");


				configuration.set("Messages.StaffMSG", "§dSatsuki §8| §r%name §7-> §b@Staff's §7: §r%msg");

				ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, ok);
			}

			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ok);
			datababase.put("ip", configuration.getString("database.ip"));
			datababase.put("name", configuration.getString("database.name"));
			datababase.put("user", configuration.getString("database.user"));
			datababase.put("password", configuration.getString("database.password"));

		} catch (IOException e) { // CREATE AND LOAD CONFIG
			e.printStackTrace();
		}

		DatabaseManager.initAllDatabaseConnections();

		getProxy().registerChannel("Sanctions");
		getProxy().registerChannel("GUI");
		getProxy().registerChannel("TP");

		getProxy().getPluginManager().registerListener(this, new io.ishynss.sweety.system.bungee.Listeners.Event());
		getProxy().getPluginManager().registerCommand(this, new CommandADM(this));
		getProxy().getPluginManager().registerCommand(this, new CommandFriends(this));
		getProxy().getPluginManager().registerCommand(this, new CommandPTP(this));


		SQL.createTable();
		SQL.updateAllOnline(false);

	}

	public static MainBungee getINSTANCE() {
		return INSTANCE;
	}



	public void runPlayTime(ProxiedPlayer p) {

		this.getProxy().getScheduler().schedule(this, new Runnable() {
			@Override
			public void run() {

				Integer bb = Play15m.get(p);
				bb++;

				if(bb == 14) {
					Play15m.replace(p, 0);

					double random;
					random = ThreadLocalRandom.current().nextDouble(2, 4);

					double pp = SQL.getCerise(p.getName(), p);
					double pp2 = pp + random;

					SQL.setCerise(null, p.getName(), pp2);
					DecimalFormat df = new DecimalFormat("###.##");
					p.sendMessage(TextComponent.fromLegacyText("§dSatsuki §8| §aVotre temps de jeu vous rapporte " + df.format(random) + " cerises."));
				} else {
					Play15m.replace(p, bb);
				}


				Integer pt = SQL.getPlayTime(p.getName(), p);
				pt++;
				SQL.setPlayTime(p, pt);

			}
		}, 0L, 1, TimeUnit.MINUTES);

	}

	@Override
	public void onDisable() {

		SQL.updateAllOnline(false);
		DatabaseManager.closeAllDatabaseConnections();

	}

}