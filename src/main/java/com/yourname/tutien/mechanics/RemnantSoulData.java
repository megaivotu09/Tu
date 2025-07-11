package com.yourname.tutien.mechanics;

import com.yourname.tutien.player.PlayerData;

public class RemnantSoulData {
    private final PlayerData originalData;
    private long timeRemaining;
    private long linhKhiCollected;
    private final long linhKhiNeeded;

    public RemnantSoulData(PlayerData originalData, long timeRemaining, long linhKhiNeeded) {
        this.originalData = originalData;
        this.timeRemaining = timeRemaining;
        this.linhKhiNeeded = linhKhiNeeded;
        this.linhKhiCollected = 0;
    }
    public PlayerData getOriginalData() { return originalData; }
    public long getTimeRemaining() { return timeRemaining; }
    public void addTime(long seconds) { this.timeRemaining += seconds; }
    public long getLinhKhiCollected() { return linhKhiCollected; }
    public void addLinhKhi(long amount) { this.linhKhiCollected += amount; }
    public long getLinhKhiNeeded() { return linhKhiNeeded; }
    public boolean isReadyToRevive() { return this.linhKhiCollected >= this.linhKhiNeeded; }
}
