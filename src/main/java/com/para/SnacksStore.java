package com.para;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;

public class SnacksStore {
  private final Semaphore popcornMutex = new Semaphore(1);
  private final Semaphore juiceMutex = new Semaphore(1);
  private final HashMap<Integer, PopcornMachine> workingPopcornMap = new HashMap<>();
  private final HashMap<Integer, PopcornMachine> stoppedPopcornMap = new HashMap<>();
  private final HashMap<Integer, JuiceMachine> workingJuiceMap = new HashMap<>();
  private final HashMap<Integer, JuiceMachine> stoppedJuiceMap = new HashMap<>();
  private final int popcornMaxStore;
  private final int juiceMaxStore;

  public SnacksStore(int popcornMachineCount, int juiceMachineCount, int popcornMaxStore, int juiceMaxStore) {
    this.popcornMaxStore = popcornMaxStore;
    this.juiceMaxStore = juiceMaxStore;

    for (int i = 1; i <= popcornMachineCount; i++) {
      stoppedPopcornMap.put(i, new PopcornMachine(i, this));
    }
    for (int i = 1; i <= juiceMachineCount; i++) {
      stoppedJuiceMap.put(i, new JuiceMachine(i, this));
    }
  }

  public void start() {
    for (PopcornMachine popcornMachine : this.stoppedPopcornMap.values()) {
      popcornMachine.start();
      int id = popcornMachine.getId();
      this.workingPopcornMap.put(id, popcornMachine);
    }
    this.stoppedPopcornMap.clear();

    for (JuiceMachine juiceMachine : this.stoppedJuiceMap.values()) {
      juiceMachine.start();
      int id = juiceMachine.getId();
      this.workingJuiceMap.put(id, juiceMachine);
    }
    this.stoppedJuiceMap.clear();
  }

  public void stop() {
    for (PopcornMachine popcornMachine : this.workingPopcornMap.values()) {
      popcornMachine.stop();
      int id = popcornMachine.getId();
      this.stoppedPopcornMap.put(id, popcornMachine);
    }
    this.workingPopcornMap.clear();

    for (JuiceMachine juiceMachine : this.workingJuiceMap.values()) {
      juiceMachine.stop();
      int id = juiceMachine.getId();
      this.stoppedJuiceMap.put(id, juiceMachine);
    }
    this.workingJuiceMap.clear();
  }

  public boolean increasePopcornStore(int id) throws InterruptedException {
    popcornMutex.acquire();
    int curr = DatabaseConnection.availableSnacks("popcorn") + 1;
    boolean isFull = curr >= this.popcornMaxStore;
    DatabaseConnection.increaseSnacks("popcorn", 1);
    popcornMutex.release();
    if (isFull) {
      PopcornMachine popcornMachine = this.workingPopcornMap.remove(id);
      this.stoppedPopcornMap.put(id, popcornMachine);
    }
    return isFull;
  }

  private void startPopCornMachine() {
    PopcornMachine popcornMachine = this.stoppedPopcornMap.values().iterator().next();
    int id = popcornMachine.getId();
    popcornMachine.start();
    this.stoppedPopcornMap.remove(id);
    this.workingPopcornMap.put(id, popcornMachine);
  }

  private void decreasePopcornStore(int count) {
    if (!this.stoppedPopcornMap.isEmpty())
      startPopCornMachine();
    DatabaseConnection.increaseSnacks("popcorn", count);
  }

  public boolean isAvailablePopcornStore() {
    try {
      popcornMutex.acquire();
      int availablePopcorn = DatabaseConnection.availableSnacks("popcorn");
      popcornMutex.release();

      if (availablePopcorn == 0 && !this.stoppedPopcornMap.isEmpty()) {
        startPopCornMachine();
      }

      return availablePopcorn != 0;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }

  public boolean increaseJuiceStore(int id) throws InterruptedException {
    juiceMutex.acquire();
    int curr = DatabaseConnection.availableSnacks("juice") + 1;
    boolean isFull = curr >= this.juiceMaxStore;
    if (isFull) {
      JuiceMachine juiceMachine = this.workingJuiceMap.remove(id);
      this.stoppedJuiceMap.put(id, juiceMachine);
    }
    DatabaseConnection.increaseSnacks("juice", 1);
    juiceMutex.release();
    return isFull;
  }

  private void startJuiceMachine() {
    JuiceMachine juiceMachine = this.stoppedJuiceMap.values().iterator().next();
    int id = juiceMachine.getId();
    juiceMachine.start();
    this.stoppedJuiceMap.remove(id);
    this.workingJuiceMap.put(id, juiceMachine);
  }

  private void decreaseJuiceStore(int count) {
    if (!this.stoppedJuiceMap.isEmpty())
      startJuiceMachine();
    DatabaseConnection.increaseSnacks("juice", count);
  }

  public boolean isAvailableJuiceStore() {
    try {
      juiceMutex.acquire();
      int availableJuice = DatabaseConnection.availableSnacks("juice");
      juiceMutex.release();

      if (availableJuice == 0 && !this.stoppedJuiceMap.isEmpty()) {
        startJuiceMachine();
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
          if (currPopcornCount >= popcornCount) {
            Thread.sleep(1000);
            decreasePopcornStore(popcornCount * -1);
          } else {
            Thread.sleep(1000);
            decreasePopcornStore(currPopcornCount * -1);
          }
          this.popcornMutex.release();
        }

        if (juiceCount != 0) {
          this.juiceMutex.acquire();
          currJuiceCount = DatabaseConnection.availableSnacks("juice");
          if (currJuiceCount >= juiceCount) {
            Thread.sleep(1000);
            decreaseJuiceStore(juiceCount * -1);
          } else {
            Thread.sleep(1000);
            decreaseJuiceStore(currJuiceCount * -1);
          }
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
                  "Sorry only available " + availableJuice + "  juice,\n but the popcorn is waiting for you.",
                  popcornCount, availableJuice));
        } else if (currPopcornCount < popcornCount || currJuiceCount < juiceCount) {
          Platform.runLater(() -> controller.failedbookSnacks(
              "Sorry only available " + availablePopcorn + " popcorn, and " + availableJuice + " juice ",
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
