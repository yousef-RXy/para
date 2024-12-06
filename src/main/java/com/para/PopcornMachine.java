package com.para;

public class PopcornMachine implements SnackMachine {
  private final int id;
  private final SnacksStore mainStore;
  private Thread thread;
  private boolean isStillRunning;

  public PopcornMachine(int id, SnacksStore mainStore) {
    this.id = id;
    this.mainStore = mainStore;
  }

  @Override
  public void generateSnacks() {
    System.out.println("popcornMachine Started");
    while (this.isStillRunning) {
      System.out.println("popcornMachine generate");
      try {
        Thread.sleep(60000);
        this.isStillRunning = !this.mainStore.increasePopcornStore(this.id);
      } catch (InterruptedException ex) {
        thread.interrupt();
      }
    }
    System.out.println("popcornMachine Stopped");
  }

  @Override
  public void start() {
    this.isStillRunning = true;
    this.thread = new Thread(this::generateSnacks);
    this.thread.start();
  }

  @Override
  public void stop() {
    this.isStillRunning = false;
    try {
      this.thread.join();
    } catch (InterruptedException e) {
      thread.interrupt();
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
