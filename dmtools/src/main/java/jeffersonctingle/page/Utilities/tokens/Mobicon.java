package jeffersonctingle.page.Utilities.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import jeffersonctingle.page.App;
import jeffersonctingle.page.Utilities.Data_Management.p_data_store;

public class Mobicon implements CommandExecutor,Listener,TabCompleter {   
    @SuppressWarnings("unused")
    private App app;
    public Mobicon(App app){
        this.app = app;
    }
    //these can be overridden by permissions, well some of them
    //these are temporary assignments to pass information from one method to another
    ArrayList<Player> tokenPlacer = new ArrayList<>();
    ArrayList<Player> deleteMob = new ArrayList<>();
    Map<Player, ArrayList<String>> informationHold = new HashMap<>();

    // ArrayList<Player> dungeonMaster = new ArrayList<>();
    // Map<Player, ArrayList<UUID>> unitSpawner = new HashMap<>();
    // Map<UUID, Player> idOwners = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            //EntityType.values()
            if(args[0].equals("help")){
                p.sendMessage("Proper format is /token <MobType> <Name> <Health> <AC>");
                return true;
            }
            else if (args.length < 4 || args.length > 5){
                p.sendMessage("Invalid Token Format, use /token help for more information");
                return true;
            }
            else if (!tokenPlacer.contains(p)) {
                tokenPlacer.add(p);
                //so it's /mobi <mobtype> <mobname> <health> <AC>
                //need to add safety logic for non integer values
                    p.sendMessage("Creating a Token for " + args[1] + " as a " + args[0]);
                    String mobType = args[0].toUpperCase();
                    String mobName = args[1];
                    String health = args[2];
                    String AC = args[3];
                    ArrayList<String> temphold = new ArrayList<>();
                    temphold.add(mobType);
                    temphold.add(mobName);
                    temphold.add(health);
                    temphold.add(AC);
                    informationHold.put(p, temphold);
                    return true;
                                        }
            
            else if(tokenPlacer.contains(p)){
                p.sendMessage("adjusting" + args[0]);
                String mobType = args[0].toUpperCase();
                String mobName = args[1];
                String health = args[2];
                String AC = args[3];
                ArrayList<String> temphold = new ArrayList<>();
                temphold.add(mobType);
                temphold.add(mobName);
                temphold.add(health);
                temphold.add(AC);
                if (args.length == 5 && args[4].equals("true")){
                    temphold.add("PC");
                    informationHold.replace(p, temphold);
                }
                else{
                    informationHold.replace(p, temphold);
                }
                return true;
            }
            else{
                return true;
            }
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(tokenPlacer.contains(e.getPlayer())) {
                Player p = (Player) e.getPlayer();
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());

                    //deconstructor I guess
                    ArrayList<String> tokenInfo = informationHold.get(p);
                    String mobType = tokenInfo.get(0).toString();
                    boolean gate = false;
                    for (EntityType type : EntityType.values()) {
                        if (type.name().equals(mobType)){
                            gate = true;
                        }
                            
                    }
                    if(gate){
                        String mobName = tokenInfo.get(1).toString();
                        Double health = Double.parseDouble(tokenInfo.get(2).toString());
                        Double AC = Double.parseDouble(tokenInfo.get(3).toString());
                        //end deconstructor

                        Location pSpotCopy = pSpot.clone();
                        pSpotCopy.setY(pSpot.getY() - 2);
                        LivingEntity token = (LivingEntity) p.getLocation().getWorld().spawnEntity(pSpot,EntityType.valueOf(mobType));
                        tokenPlacer.remove(p);
                        token.setSilent(true);
                        token.setAI(false);
                        //check and set max health higher than current
                        if(mobType.equalsIgnoreCase("wandering_trader")){
                            WanderingTrader sl = (WanderingTrader) token;
                            sl.setDespawnDelay(0);
                        }
                        token.setCustomName(mobName);
                        token.setCustomNameVisible(true); 
                        token.setPersistent(true);
                        token.setRemoveWhenFarAway(false);  
                        token.addScoreboardTag("ac:" + AC);
                        p_data_store.storeValueInt(token, "ac", AC.intValue());
                        //persistentdatacontainer
                        token.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                        token.setHealth(health);
                        token.setInvulnerable(true);
                        token.addScoreboardTag("token");
                        informationHold.remove(p);
                        //this is kind of wacky, find an easier way to associate with player names
                        // token.addScoreboardTag("owner:" + p.getName());
                        // idOwners.put(token.getUniqueId(), p);
                        if(tokenInfo.size() > 4){
                            token.addScoreboardTag("PlayerCharacter");
                            //this will be changed if more options emerge in this argument slot
                        }
                    }
                    else{
                        tokenPlacer.remove(p);
                        informationHold.remove(p);
                        p.sendMessage("Not an accepted Token Mob");
                    //add PC tag for player tokens
                    }
                    }
                else{
                    p.sendMessage("Please select solid ground");
                }
            }
            //right click on the specific mob, and gets their info
            //need a check for OP and/or DM player
            //if permission OR unit belongs to player, probably also add some kind of player permission

        }   
    }
    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mobi")) {
            if (args.length == 1) {
                ArrayList<String> entityTypes = new ArrayList<String>();
                if (!args[0].equals("")) {
                    for (EntityType type : EntityType.values()) {
                        if (type.isAlive() && type.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                            entityTypes.add(type.name());
                        }
                    }
                }
                else {
                    for (EntityType type : EntityType.values()) {
                        if (type.isAlive()) {
                            entityTypes.add(type.name());
                        }
                    }
                }
                return entityTypes;
            }
        }
        return null;
    }
}
