package com.para;

public class JuiceMachine implements SnackMachine {
  private final int id;
  private final SnacksStore mainStore;
  private Thread thread;
  private boolean isStillRunning;

  public JuiceMachine(int id, SnacksStore mainStore) {
    this.id = id;
    this.mainStore = mainStore;
  }

  @Override
  public void generateSnacks() {
    System.out.println("JuiceMachine Started");
    while (this.isStillRunning) {
      System.out.println("JuiceMachine generate");

      try {
        Thread.sleep(40000);
        this.isStillRunning = !this.mainStore.increaseJuiceStore(this.id);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    System.out.println("JuiceMachine Stopped");
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
      Thread.currentThread().interrupt();
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "JuiceMachine [id=" + id + "]";
  }
}
