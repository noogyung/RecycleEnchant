package me.noogs.rechant.listeners;

import me.noogs.rechant.Rechant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RecycleEnchantListener implements Listener {
    Plugin plugin = Rechant.getPlugin();
    Configuration config = plugin.getConfig();
    private static final List<UUID> playersMessagedForEventList = new ArrayList<>();

    @EventHandler
    public void onAnvilClick(PrepareAnvilEvent event) {
        // 인벤토리 타입이 모루 인지 확인
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            // inventory 를 모루에 들어있는 아이템으로 설정.
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            // 현재 모루 사용자
            Player player = (Player) event.getView().getPlayer();

            //딜레이 : 없을 경우 result아이템 표시 안됨
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    // 모루 최대 레벨 제한 변경 - 너무 비쌉니다는 출력하는 대신 아이템은 정상적으로 합쳐짐
                    inventory.setMaximumRepairCost(config.getInt("maxRepairCost"));

                    // 인챈트 추출할 아이템
                    ItemStack targetItem = inventory.getItem(0);
                    // 인챈트를 넣을 아이템
                    ItemStack matter = inventory.getItem(1);

                    // 인첸트 추출 아이템에 인챈트가 있는지 확인
                    if (targetItem != null && targetItem.getType() != Material.AIR && targetItem.getItemMeta().hasEnchants() != false) {
                        //매체 아이템 확인
                        ItemStack MatterItemType = getMatterItemfromConfig();
                        if (matter != null && matter.getType() != Material.AIR && matter.getType() == MatterItemType.getType()) {
                            //가격 설정
                            inventory.setRepairCost(config.getInt("repairCost"));
                            //targetItem에서 result에 들어갈 인챈트 전체 가져오기
                            Map<Enchantment, Integer> targetEnchant = targetItem.getEnchantments();
                            //ResultItem 설정
                            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                            //ResultItem에 targetItem의 meta데이터 중 인챈트 항목 붙이기
                            for (Map.Entry<Enchantment, Integer> mapEntry : targetEnchant.entrySet()) {
                                //인챈트 이름
                                Enchantment enchantment = mapEntry.getKey();
                                //인챈트 레벨
                                Integer level = mapEntry.getValue();
                                //재 인챈트 가능한 ResultItem Meta
                                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
                                //ResultItem 이름
                                esm.setDisplayName(config.getString("EnchantBookName"));
                                //ResultItem Meta + TargetItem Meta, 레벨 제한 - 해제
                                esm.addStoredEnchant(enchantment, level, true);
                                //ResultItem Meta를 ResultItem 에 적용
                                book.setItemMeta(esm);
                            }
                            //ResultItem 을 인벤토리에 적용
                            inventory.setItem(2, book);
                        }
                        // 인챈트북 x 인챈트북 인 경우
                        else if (targetItem.getType() == Material.ENCHANTED_BOOK && matter.getType() == Material.ENCHANTED_BOOK){
                            Map<Enchantment, Integer> matterEnchant = matter.getEnchantments();
                            ItemStack book = targetItem;

                            for (Map.Entry<Enchantment, Integer> mapEntry : matterEnchant.entrySet()){
                                Enchantment enchantment = mapEntry.getKey();
                                Integer level = mapEntry.getValue();
                                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
                                esm.addStoredEnchant(enchantment,level, true);
                                book.setItemMeta(esm);
                            }
                            inventory.setItem(2,book);
                        }
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            // 코스트가 40을 넘어갈 경우 실행할 것 - 현재 : 메시지 전송
                            if (inventory.getItem(2) != null && inventory.getRepairCost() >= 40 ){
                                messagePlayerRepairCostTooExpensive(player, inventory.getRepairCost());
                            }
                        }
                    }, 1L);
                }
            }, 1);
        }
    }

    private void messagePlayerRepairCostTooExpensive(Player player, int repairCost){
        if(!playersMessagedForEventList.contains(player.getUniqueId())){
            playersMessagedForEventList.add(player.getUniqueId());
            String message = ">>>>>>>>>>" + ChatColor.RED + "RepairCost" . replace("RepairCost", repairCost + " ") + ChatColor.YELLOW + config.getString("needCost") + ChatColor.WHITE + "<<<<<<<<<<";
            player.sendMessage("" + message);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->{
                playersMessagedForEventList.remove(player.getUniqueId());
            }, 2L);

        }
    }


    public ItemStack getMatterItemfromConfig() {
        String s = config.getString("matterItem");
        Material m = Material.matchMaterial(s);
        ItemStack matterItemType = new ItemStack(m);
        return matterItemType;
    }
}
