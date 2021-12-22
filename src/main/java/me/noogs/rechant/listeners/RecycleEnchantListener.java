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
            // 슬롯이 비었는지 확인
            if (inventory.getItem(2) != null && inventory.getItem(2).getType() != Material.AIR) ;
            else {
                //딜레이 : 없을 경우 result아이템 표시 안됨
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        // 인챈트 추출할 아이템
                        ItemStack targetItem = inventory.getItem(0);
                        // 인챈트를 넣을 아이템
                        ItemStack matter = inventory.getItem(1);
                        // 인첸트 추출 아이템에 인챈트가 있는지 확인
                        if (targetItem != null && targetItem.getType() != Material.AIR && targetItem.getItemMeta().hasEnchants() != false) {
                            //매체 아이템 확인
                            if (matter != null && matter.getType() != Material.AIR && matter.getType() == Material.WRITABLE_BOOK) {
                                //가격 설정
                                inventory.setRepairCost(Rechant.getPlugin().getConfig().getInt("repairCost"));
                                //targetItem에서 result에 들어갈 인챈트 전체 가져오기
                                Map<Enchantment, Integer> targetEnchant = targetItem.getEnchantments();
                                //ResultItem 설정
                                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                                //ResultItem에 targetItem의 meta데이터 중 인챈트 항목 붙이기
                                //
                                for (Map.Entry<Enchantment, Integer> mapEntry : targetEnchant.entrySet()) {
                                    //인챈트 이름
                                    Enchantment enchantment = mapEntry.getKey();
                                    //인챈트 레벨
                                    Integer level = mapEntry.getValue();
                                    //재 인챈트 가능한 ResultItem Meta
                                    EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
                                    //ResultItem 이름
                                    esm.setDisplayName(Rechant.getPlugin().getConfig().getString("EnchantBookName"));
                                    //ResultItem Meta + TargetItem Meta, 레벨 제한 - 해제
                                    esm.addStoredEnchant(enchantment, level, true);
                                    //ResultItem Meta를 ResultItem 에 적용
                                    book.setItemMeta(esm);
                                }
                                //ResultItem 을 인벤토리에 적용
                                inventory.setItem(2, book);
                            } else {
                                return;
                            }
                        }
                    }
                }, 1);
            }
        }
    }
}
