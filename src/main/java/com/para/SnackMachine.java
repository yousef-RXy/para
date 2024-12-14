package com.para;

import java.util.concurrent.Semaphore;

public class SnackMachine {
  private final int id;
  private final int workingTime;
  private final SnacksStore mainStore;
  private final Semaphore semaphore;
  private final SnacksEnum mode;
  private Thread thread;
  private boolean isStillRunning;

  public SnackMachine(int id, SnacksStore mainStore, Semaphore semaphore, SnacksEnum mode, int workingTime) {
    this.id = id;
    this.workingTime = workingTime;
    this.mainStore = mainStore;
    this.semaphore = semaphore;
    this.mode = mode;
  }

  public void generateSnacks() {
    System.out.println(this.mode.toString() + "Machine Started");
    while (this.isStillRunning) {
      System.out.println(this.mode.toString() + "Machine generate");
      try {
        Thread.sleep(this.workingTime * 1000);
        this.isStillRunning = !this.mainStore.increaseSnackStore(this.id, this.semaphore, this.mode);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    System.out.println(this.mode.toString() + "Machine Stopped");
  }

  public void start() {
    this.isStillRunning = true;
    this.thread = new Thread(this::generateSnacks);
    this.thread.start();
  }

  public void stop() {
    this.isStillRunning = false;
    try {
      if (this.thread.isAlive()) {
        System.out.println("done");
        this.thread.join();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "PopcornMachine [id=" + id + "]";
  }
}
