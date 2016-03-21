package com.danielbchapman.motion;

import java.util.HashMap;

import com.danielbchapman.motion.MotionCue.CueType;

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
  private CueType type;
  private String cueId;
  private String description;
  private String label;
  private Float time;
  private Float follow;
  private Float delay;
  
  private HashMap<String, String> dynamic = new HashMap<>();
  
  public MotionCueStorage(MotionCue cue)
  {
    setId(cue.getId());
    setCueId(cue.getCueId());
    setDescription(cue.getDescription());
    setTime(cue.getTime());
    setFollow(cue.getFollow());
    setDelay(cue.getDelay());
    setLabel(cue.getLabel());
    setType(CueType.fromString(cue.getType()));
    if(cue.getDynamicData() != null)
    {
       cue.getDynamicData()
         .forEach((k, v) -> dynamic.put(k, v));
    }
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
    ret.setType(type == null ? type.CONTENT.toString() : type.toString());
    if(dynamic != null)
    {
      dynamic.forEach((k, v) -> ret.setData(k, v));
    }
    return ret;
  }

}
