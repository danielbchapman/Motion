package com.danielbchapman.ui.fx;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * A silly wrapper class that behaves like a pointer.
 *
 */

@AllArgsConstructor
public class Pointer<T>
{
  @Getter
  @Setter
  T value;
}
