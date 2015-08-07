
---------------------------------------------------------------------
Binghamton University - Research Advancement
(http://research.binghamton.edu/ResearchAdvancement/)

This file provides some information about Slider.

Copyright (C) 2015 Zed Yang

Copying and distribution of this file, with or without modification,
are permitted in any medium without royalty, provided the copyright
notice and this notice are preserved.  This file is offered as-is,
without any warranty.
-----------------------------------------------------------------------

Slider is a compact FREEWARE which runs a full-screen slide show
for Windows 7/8.1.

Supported image file formats:
Image: jpg, png and gif.
Video: mp4, flv, m4a.  
It does not support animated-GIF.


Features:
- Changes made to the directory of images (like adding and removing images), reflect in a slide show.
- No need to restart the show to accomodate new images/videos.
    -images/videos are updated at the end of every iteration
- Auto-expand images (smaller images are stretched to fit screen size)
- Left Arrow/right Arrow display previous/next slide. Show continues from that slide.
- Separate config file "config.txt" holds following parameters for slide show
	- Media_dir:         path to the directory of images/video
	- Delay_time:        second interval between two slides



Usage:
- Set the above mentioned parameters in config.txt file.
- While keeping config file in same directory as exe, run slider.exe file.
- Pressing "Esc" key stops the show and exits.

To create changed exe:
- When you make changes in Slider source code, run build.bat file.
- This would compile changed code and create executable slider.jar file in bin directory.
- Then use any software which would create exe from jar (I used JSmooth).
- Embed the jar file in exe so that jar file need not always accompany the binary executable.


Enjoy !! 

--------------------------------------------------------------------
Slider is a FREEWARE and is for non-commercial use!
No warranty of any kind is expressed or implied.
--------------------------------------------------------------------