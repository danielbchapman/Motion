package com.danielbchapman.functional;

public interface BiFunction<A, B, Out>
{
  public Out call(A a, B b);
}
