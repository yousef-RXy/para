package com.para;

public enum SnacksEnum {
  POPCORN,
  JUICE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
