package com.para;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;

public class SnacksStore {
  private final Semaphore popcornMutex = new Semaphore(1);
  private final Semaphore juiceMutex = new Semaphore(1);
  private final HashMap<Integer, SnackMachine> workingPopcornMap = new HashMap<>();
  private final HashMap<Integer, SnackMachine> stoppedPopcornMap = new HashMap<>();
  private final HashMap<Integer, SnackMachine> workingJuiceMap = new HashMap<>();
  private final HashMap<Integer, SnackMachine> stoppedJuiceMap = new HashMap<>();
  private final int popcornMaxStore;
  private final int juiceMaxStore;

  public SnacksStore(int popcornMachineCount, int juiceMachineCount, int popcornWorkingTime, int juiceWorkingTime,
      int popcornMaxStore, int juiceMaxStore) {
    this.popcornMaxStore = popcornMaxStore;
    this.juiceMaxStore = juiceMaxStore;

    for (int i = 1; i <= popcornMachineCount; i++) {
      stoppedPopcornMap.put(i, new SnackMachine(i, this, popcornMutex, SnacksEnum.POPCORN, popcornWorkingTime));
    }
    for (int i = 1; i <= juiceMachineCount; i++) {
      stoppedJuiceMap.put(i, new SnackMachine(i, this, juiceMutex, SnacksEnum.JUICE, juiceWorkingTime));
    }
  }

  public void start() {
    for (SnackMachine popcornMachine : this.stoppedPopcornMap.values()) {
      popcornMachine.start();
      int id = popcornMachine.getId();
      this.workingPopcornMap.put(id, popcornMachine);
    }
    this.stoppedPopcornMap.clear();

    for (SnackMachine juiceMachine : this.stoppedJuiceMap.values()) {
      juiceMachine.start();
      int id = juiceMachine.getId();
      this.workingJuiceMap.put(id, juiceMachine);
    }
    this.stoppedJuiceMap.clear();
  }

  public void stop() {
    for (SnackMachine popcornMachine : this.workingPopcornMap.values()) {
      System.out.println("done");
      popcornMachine.stop();
      int id = popcornMachine.getId();
      this.stoppedPopcornMap.put(id, popcornMachine);
      System.out.println("done");
    }
    this.workingPopcornMap.clear();

    for (SnackMachine juiceMachine : this.workingJuiceMap.values()) {
      juiceMachine.stop();
      int id = juiceMachine.getId();
      this.stoppedJuiceMap.put(id, juiceMachine);
      System.out.println("done");
    }
    this.workingJuiceMap.clear();
  }

  public boolean increaseSnackStore(int id, Semaphore mutex, SnacksEnum mode) throws InterruptedException {
    mutex.acquire();
    int curr = DatabaseConnection.availableSnacks(mode.toString()) + 1;
    int max = mode.equals(SnacksEnum.POPCORN) ? this.popcornMaxStore : this.juiceMaxStore;
    DatabaseConnection.increaseSnacks(mode.toString(), 1);
    mutex.release();
    if (curr >= max) {
      switch (mode) {
        case POPCORN:
          SnackMachine popcornMachine = this.workingPopcornMap.remove(id);
          this.stoppedPopcornMap.put(id, popcornMachine);
          break;
        case JUICE:
          SnackMachine JuiceMachine = this.workingJuiceMap.remove(id);
          this.stoppedJuiceMap.put(id, JuiceMachine);
          break;
      }
    }
    return curr >= max;
  }

  private void decreaseSnackStore(int count, SnacksEnum mode) {
    switch (mode) {
      case POPCORN:
        if (!this.stoppedPopcornMap.isEmpty())
          startMachine(mode);
        break;
      case JUICE:
        if (!this.stoppedJuiceMap.isEmpty())
          startMachine(mode);
        break;
    }
    DatabaseConnection.increaseSnacks(mode.toString(), count);
  }

  private void startMachine(SnacksEnum mode) {
    SnackMachine Machine;
    int id;
    switch (mode) {
      case POPCORN:
        Machine = this.stoppedPopcornMap.values().iterator().next();
        id = Machine.getId();
        Machine.start();
        this.stoppedPopcornMap.remove(id);
        this.workingPopcornMap.put(id, Machine);
        break;
      case JUICE:
        Machine = this.stoppedJuiceMap.values().iterator().next();
        id = Machine.getId();
        Machine.start();
        this.stoppedJuiceMap.remove(id);
        this.workingJuiceMap.put(id, Machine);
        break;
    }
  }

  public boolean isAvailablePopcornStore() {
    try {
      popcornMutex.acquire();
      int availablePopcorn = DatabaseConnection.availableSnacks("popcorn");
      popcornMutex.release();

      if (availablePopcorn == 0 && !this.stoppedPopcornMap.isEmpty()) {
        startMachine(SnacksEnum.POPCORN);
      }

      return availablePopcorn != 0;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }

  public boolean isAvailableJuiceStore() {
    try {
      juiceMutex.acquire();
      int availableJuice = DatabaseConnection.availableSnacks("juice");
      juiceMutex.release();

      if (availableJuice == 0 && !this.stoppedJuiceMap.isEmpty()) {
        startMachine(SnacksEnum.JUICE);
      }

      return availableJuice != 0;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }

  public void bookSnacks(MessageController controller, int popcornCount, int juiceCount) {
    new Thread(() -> {
      try {
        int currJuiceCount = 0;
        int currPopcornCount = 0;

        if (popcornCount != 0) {
          this.popcornMutex.acquire();
          currPopcornCount = DatabaseConnection.availableSnacks("popcorn");
          Thread.sleep(1000);
          int bookingCount = currPopcornCount >= popcornCount ? popcornCount : currPopcornCount;
          decreaseSnackStore(bookingCount * -1, SnacksEnum.POPCORN);
          this.popcornMutex.release();
        }

        if (juiceCount != 0) {
          this.juiceMutex.acquire();
          currJuiceCount = DatabaseConnection.availableSnacks("juice");
          Thread.sleep(1000);
          int bookingCount = currJuiceCount >= juiceCount ? juiceCount : currJuiceCount;
          decreaseSnackStore(bookingCount * -1, SnacksEnum.JUICE);
          this.juiceMutex.release();
        }

        final int availablePopcorn = currPopcornCount;
        final int availableJuice = currJuiceCount;
        if (currPopcornCount < popcornCount && currJuiceCount >= juiceCount) {
          Platform.runLater(
              () -> controller.failedbookSnacks(
                  "Sorry only available " + availablePopcorn + " popcorn,\n but the juice is waiting for you.",
                  availablePopcorn, juiceCount));
        } else if (currJuiceCount < juiceCount && currPopcornCount >= popcornCount) {
          Platform.runLater(
              () -> controller.failedbookSnacks(
                  "Sorry only available " + availableJuice + " juice,\n but the popcorn is waiting for you.",
                  popcornCount, availableJuice));
        } else if (currPopcornCount < popcornCount || currJuiceCount < juiceCount) {
          Platform.runLater(() -> controller.failedbookSnacks(
              "Sorry only available " + availablePopcorn + " popcorn, and " +
                  availableJuice + " juice ",
              availablePopcorn,
              availableJuice));
        } else {
          Platform.runLater(() -> controller.showInvoice(popcornCount, juiceCount));
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }).start();
  }
}
