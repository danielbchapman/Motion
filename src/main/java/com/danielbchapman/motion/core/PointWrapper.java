package com.danielbchapman.motion.core;

import lombok.Getter;
import lombok.Setter;

public class PointWrapper<Wrap> extends Point
{
	@Getter
	@Setter
	private Wrap wrap;

	public PointWrapper(float x, float y, float z, float w)
	{
		super(x, y, z, w);
	}

	public PointWrapper(PointWrapper<Wrap> p)
	{
		super(p);
		this.wrap = p.wrap;
	}

	public PointWrapper()
	{
	}
}
