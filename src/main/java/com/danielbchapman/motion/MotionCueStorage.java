package com.danielbchapman.motion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MotionCueStorage
{

  private Integer id;
  private String cueId;
  private String description;
  private String label;
  private Float time;
  private Float follow;
  private Float delay;
  
  public MotionCueStorage(MotionCue cue)
  {
    setId(cue.getId());
    setCueId(cue.getCueId());
    setDescription(cue.getDescription());
    setTime(cue.getTime());
    setFollow(cue.getFollow());
    setDelay(cue.getDelay());
    setLabel(cue.getLabel());
  }
  
  public MotionCue toMotionCue()
  {
    MotionCue ret = new MotionCue();
    ret.setId(id);
    ret.setCueId(cueId);
    ret.setDescription(description);
    ret.setTime(time);
    ret.setFollow(follow);
    ret.setDelay(delay);
    ret.setLabel(label);
    return ret;
  }

}
