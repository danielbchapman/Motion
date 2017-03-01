package com.danielbchapman.functional;

public interface Function<In, Out>
{
  public Out call(In in);
}
