package jeffersonctingle.page.Utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

import jeffersonctingle.page.App;

public class dmNotes implements CommandExecutor,Listener {   
    @SuppressWarnings("unused")
    private App app;
    public dmNotes(App app){
        this.app = app;
    }
    ArrayList<Player> notePlacers = new ArrayList<>();
    //need to privatize this between players
    String noteText = new String(); 
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(args[0].equals("erase")){
                List<org.bukkit.entity.Entity> nearby = p.getNearbyEntities(5,5,5);
                for( org.bukkit.entity.Entity each : nearby){
                    if (each.getType() == EntityType.TEXT_DISPLAY){
                        each.remove();
                    }
                }
            }
            else if (!notePlacers.contains(p)) {
                notePlacers.add(p);
                StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < args.length; i++) {
                        builder.append(args[i] + " ");
                    } 
                    noteText = builder.toString();
                    p.sendMessage("Placing Dnote " + builder.toString());
                // noteText = args[0];
                                        }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(notePlacers.contains(e.getPlayer())) {
                Player p = (Player) e.getPlayer();
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    Location pSpotCopy = pSpot.clone();
                    pSpotCopy.setY(pSpot.getY() + .5);
                    TextDisplay td = (TextDisplay) pSpot.getWorld().spawn(pSpotCopy, TextDisplay.class);
                    td.setText(noteText);
                    td.setBillboard(Display.Billboard.CENTER);
                    notePlacers.remove(p);
                    }
                else{
                }
            }
        }   
    }

}
