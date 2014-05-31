package com.soomla.unity;

import android.text.TextUtils;

import com.soomla.store.BusProvider;
import com.soomla.store.StoreUtils;
import com.soomla.store.domain.MarketItem;
import com.soomla.store.domain.virtualGoods.UpgradeVG;
import com.soomla.store.events.*;
import com.squareup.otto.Subscribe;
import com.unity3d.player.UnityPlayer;

public class EventHandler {
    private static EventHandler mLocalEventHandler;

    public static void initialize() {
        mLocalEventHandler = new EventHandler();

    }

    public EventHandler() {
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onBillingSupported(BillingSupportedEvent billingSupportedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onBillingSupported", "");
    }

    @Subscribe
    public void onBillingNotSupported(BillingNotSupportedEvent billingNotSupportedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onBillingNotSupported", "");
    }

    @Subscribe
    public void onIabServiceStartedEvent(IabServiceStartedEvent iabServiceStartedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onIabServiceStarted", "");
    }

    @Subscribe
    public void onIabServiceStoppedEvent(IabServiceStoppedEvent iabServiceStoppedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onIabServiceStopped", "");
    }

    @Subscribe
    public void onCurrencyBalanceChanged(CurrencyBalanceChangedEvent currencyBalanceChangedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onCurrencyBalanceChanged",
                currencyBalanceChangedEvent.getCurrency().getItemId() + "#SOOM#" +
                currencyBalanceChangedEvent.getBalance() + "#SOOM#" +
                currencyBalanceChangedEvent.getAmountAdded());
    }

    @Subscribe
    public void onGoodBalanceChanged(GoodBalanceChangedEvent goodBalanceChangedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onGoodBalanceChanged",
                goodBalanceChangedEvent.getGood().getItemId() + "#SOOM#" +
                        goodBalanceChangedEvent.getBalance() + "#SOOM#" +
                        goodBalanceChangedEvent.getAmountAdded());
    }

    @Subscribe
    public void onGoodEquipped(GoodEquippedEvent goodEquippedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onGoodEquipped", goodEquippedEvent.getGood().getItemId());
    }

    @Subscribe
    public void onGoodUnequipped(GoodUnEquippedEvent goodUnEquippedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onGoodUnequipped", goodUnEquippedEvent.getGood().getItemId());
    }

    @Subscribe
    public void onGoodUpgrade(GoodUpgradeEvent goodUpgradeEvent) {
        String goodItemId = goodUpgradeEvent.getGood().getItemId();
        UpgradeVG currentUpgrade = goodUpgradeEvent.getCurrentUpgrade();
        String upgradeItemId = null;
        if (currentUpgrade != null) {
            upgradeItemId = currentUpgrade.getItemId();
        }

        UnityPlayer.UnitySendMessage("StoreEvents", "onGoodUpgrade",
                goodItemId + (TextUtils.isEmpty(upgradeItemId)? "" : ("#SOOM#" + upgradeItemId)));
    }

    @Subscribe
    public void onItemPurchased(ItemPurchasedEvent itemPurchasedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onItemPurchased", itemPurchasedEvent.getPurchasableVirtualItem().getItemId());
    }

    @Subscribe
    public void onItemPurchaseStarted(ItemPurchaseStartedEvent itemPurchaseStartedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onItemPurchaseStarted", itemPurchaseStartedEvent.getPurchasableVirtualItem().getItemId());
    }

    @Subscribe
    public void onMarketPurchaseCancelled(MarketPurchaseCancelledEvent playPurchaseCancelledEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketPurchaseCancelled",
                playPurchaseCancelledEvent.getPurchasableVirtualItem().getItemId());
    }

    @Subscribe
    public void onMarketPurchase(MarketPurchaseEvent playPurchaseEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketPurchase",
                playPurchaseEvent.getPurchasableVirtualItem().getItemId() + "#SOOM#" +
                        playPurchaseEvent.getPayload() + "#SOOM#" +
                        playPurchaseEvent.getToken()
        );
    }

    @Subscribe
    public void onMarketPurchaseStarted(MarketPurchaseStartedEvent playPurchaseStartedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketPurchaseStarted", playPurchaseStartedEvent.getPurchasableVirtualItem().getItemId());
    }

    @Subscribe
    public void onMarketRefund(MarketRefundEvent playRefundEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketRefund", playRefundEvent.getPurchasableVirtualItem().getItemId());
    }

    @Subscribe
    public void onRestoreTransactionsFinished(RestoreTransactionsFinishedEvent restoreTransactionsFinishedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onRestoreTransactionsFinished", (restoreTransactionsFinishedEvent.isSuccess() ? 1 : 0) + "");
    }

    @Subscribe
    public void onRestoreTransactionsStarted(RestoreTransactionsStartedEvent restoreTransactionsStartedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onRestoreTransactionsStarted", "");
    }

    @Subscribe
    public void onMarketItemsRefreshStarted(MarketItemsRefreshStartedEvent marketItemsRefreshStartedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketItemsRefreshStarted", "");
    }

    @Subscribe
    public void onMarketItemsRefreshFinished(MarketItemsRefreshFinishedEvent marketItemsRefreshFinishedEvent) {
        String marketItemsChanges = "";
        for (MarketItem mi : marketItemsRefreshFinishedEvent.getMarketItems()) {
            marketItemsChanges += "{" +
                    "\"productId\":\"" + mi.getProductId() + "\"," +
                    "\"market_price\":\"" + mi.getMarketPrice() + "\"," +
                    "\"market_title\":\"" + mi.getMarketTitle() + "\"," +
                    "\"market_desc\":\"" + mi.getMarketDescription() + "\"" +
                    "}";
            marketItemsChanges += "#SOOM#";
        }
        if (!TextUtils.isEmpty(marketItemsChanges)) {
            int index = marketItemsChanges.lastIndexOf("#SOOM#");
            if (index > 0) {
                marketItemsChanges = marketItemsChanges.substring(0, index);
            }
        }
        UnityPlayer.UnitySendMessage("StoreEvents", "onMarketItemsRefreshFinished", marketItemsChanges);
    }

    @Subscribe
    public void onStoreControllerInitializedEvent(StoreControllerInitializedEvent storeControllerInitializedEvent) {
        UnityPlayer.UnitySendMessage("StoreEvents", "onStoreControllerInitialized", "");
    }

    @Subscribe
    public void onUnexpectedStoreError(UnexpectedStoreErrorEvent unexpectedStoreErrorEvent) {
        String msg = unexpectedStoreErrorEvent.getMessage();
        UnityPlayer.UnitySendMessage("StoreEvents", "onUnexpectedErrorInStore", (msg == null ? "" : msg));
    }

}
