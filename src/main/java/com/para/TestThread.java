package com.para;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TestThread extends Thread {
  Semaphore mutexSemaphore;
  Message controller;
  HashSet<String> set;

  public TestThread(Semaphore mutexSemaphore, Message controller, HashSet<String> set) {
    this.mutexSemaphore = mutexSemaphore;
    this.controller = controller;
    this.set = set;
  }

  @Override
  public void run() {
    try {
      mutexSemaphore.acquire();
      controller.setMessage(set.toString());
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.interrupted();
    } finally {
      mutexSemaphore.release();
    }
  }
}
