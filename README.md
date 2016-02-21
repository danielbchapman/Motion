##Current Branch: development-fx
Latest Builds (demo):
https://github.com/danielbchapman/Motion/releases/tag/Dev-Win32

#Motion
***Motion***: a framework for kinetic projections inspired originally by the fantastic work of Adrien M / Claire B http://www.am-cb.net/ and relentless risk-taking with Candace Winters-March.

###Installation Guide
https://github.com/danielbchapman/Motion/wiki/Building-Motion

###Wiki
https://github.com/danielbchapman/Motion/wiki/

What is Motion?
---------------
Motion is a piece of software built over toxiclibs and Processing that provides a framework for kinetic (responsive) projections for use in live performance and installation art. Motion combines a physics engine and rendering engine in an accessible cross platform framework with an accessible open-source license. 

How do I use Motion?
--------------------

Download and compile the source! You’ll need access to a couple other projects, most notably toxiclibs and Processing. Due to the nature of Maven (and my shear laziness) there is a bit of manual configuration required to get it up and running. Just send me an e-mail chapman@danielbchapman.com and I’ll be happy to walk you through it.

Motion is a Java application built over Processing 2.2 and uses toxiclibs for the physics engine. It structures scenes as a series of cues and incorporates primitive tools for recording motion, altering the physics of the world, receiving and sending events, and custom drawing operations. 

Do I need to be a software engineer to use Motion?
--------------------------------------------------
At the moment: ***probably***. Here's an example of a cue stack:

    //...snip
    add(
    	cue("Rain Falls",
    		Actions.dragTo(0.5f),
    		startBleed(),
    		Actions.gravityTo(new Vec3D(.45f, .85f, 0)),
    		Actions.gravityOn,
    		startRain()
    	),
    	cue("Harder Rain", //---Add 5 or so emitters for 1 seconds
    			Actions.follow(500),
    			hardOn(),
    			Actions.gravityTo(new Vec3D(.45f, .85f, 0).scale(2f)),
    			Actions.gravityOn
    		),
    	cue("Freeze Paint",
    		stopBleed(),
    		Actions.follow(1000)
    //...snip



**Damn!** 

Will I always need to be a programmer?

***No.*** 

A major focus in the current plans is to find a way to make programming a show possible using only the user interface. Motion already has a large set of tools for recording the state of brushes, cues, mouse movement, and other data-structures. It is just a matter of shoving these all into a box.

...right after I update the framework to support Processing 3.0
Who uses Motion?
----------------

Me! And the companies who are willing to take a risk with me. The project is very, very young  (developed April 2015) but it has been used in live performances with Guerrilla Opera (http:// http://guerillaopera.com/) and Western Illinois University. 

Does Motion interface with QLab or other programs?
-----------------
Yes! Motion integrates OSC and you can send whatever messages you see fit. It also has naive implementations of JSpout and JSyphon for OpenGL hooks as a camera into other applications. 

Who maintains it?
-----------------

Me! Daniel B. Chapman, I’m the only current contributor but I am excited to accept pull requests and I am actively looking for other developers who might be interested in developing the framework.

How is it licensed?
-------------------

Currently Motion is licensed under the GPLv3 but I am considering moving it to an MIT, Eclipse, or other commercial friendly license. If licensing is in your way feel free to let me know your concerns. I default to the GPL because it forces collaboration with the host project. 

There is no fee for using Motion in your production. I simply ask that you credit the software and give a thank you in your program materials (and let me know so I can update the list of people using it!)

Project History
---------------
**In performance...**

*Mobilology*, Western Illinois University, choreography by Candace Winters-March

*Troubled Water*, Guerilla Opera, directed by Allegra Libonati

*She Kills Monsters*, Western Illinois University, directed by D.C.Wright

**In words...**
Motion takes its name from eMotion (http://www.am-cb.net/emotion/) as the the inspiration. We couldn't find a way to make eMotion fit our needs for custom drawing and physics and Candace took a big leap leap with me off the deep end and decided to build Motion from the ground up. We expected failure at every turn (and we experienced it in rehearsal after rehearsal) as a result Motion exists.

Expect constant development, revisions, redesign, spaghetti code, AND SUPPORT FOR YOUR PROJECTS if you choose to join us on this adventure.

> Written with [StackEdit](https://stackedit.io/).

