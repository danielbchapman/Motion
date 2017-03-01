package com.danielbchapman.functional;

public interface TriFunction<A, B, C, Out>
{
  public Out call(A a, B b, C c);
}
