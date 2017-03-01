package com.danielbchapman.functional;

public interface QuadFunction<A, B, C, D, Out>
{
  public Out call(A a, B b, C c, D d);
}
