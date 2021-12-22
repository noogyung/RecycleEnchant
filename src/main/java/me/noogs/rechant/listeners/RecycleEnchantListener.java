package me.noogs.rechant.listeners;

import me.noogs.rechant.Rechant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class RecycleEnchantListener implements Listener {
    Plugin plugin = Rechant.getPlugin();

    @EventHandler
    public void onAnvilClick(PrepareAnvilEvent event) {
        // 인벤토리 타입이 모루 인지 확인
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            // inventory 를 모루에 들어있는 아이템으로 설정.
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            // inventory 아이템 변수 지정


            // 슬롯 확인
            if (inventory.getItem(2) != null && inventory.getItem(2).getType() != Material.AIR) ;
            else {
                //inventory2 (result) 클릭시 결과, 필요 경험치 표시
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        ItemStack targetItem = inventory.getItem(0);
                        ItemStack matter = inventory.getItem(1);
                        if (targetItem != null && targetItem.getType() != Material.AIR && targetItem.getItemMeta().hasEnchants() != false) {
                            if (matter != null && matter.getType() != Material.AIR && matter.getType() == Material.WRITABLE_BOOK) {
                                if (inventory.getItem(2) == null || inventory.getItem(2).getType() == Material.AIR) {
                                    //가격 설정
                                    inventory.setRepairCost(Rechant.getPlugin().getConfig().getInt("repairCost"));

                                    //targetItem에서 result에 들어갈 인챈트 목록 가져오기
                                    Map<Enchantment, Integer> targetEnchant = targetItem.getEnchantments();

                                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

                                    //인챈트 북에 targetItem의 meta데이터 중 인챈트 항목 붙이기
                                    for (Map.Entry<Enchantment, Integer> mapEntry : targetEnchant.entrySet()) {
                                        Enchantment enchantment = mapEntry.getKey();
                                        Integer level = mapEntry.getValue();
                                        EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
                                        esm.setDisplayName(Rechant.getPlugin().getConfig().getString("EnchantBookName"));
                                        esm.addStoredEnchant(enchantment, level, true);
                                        book.setItemMeta(esm);
                                    }
                                    inventory.setItem(2, book);
                                } else {
                                    return;
                                }
                            }
                        }

                    }
                }, 1);


            }
        }
    }
}
