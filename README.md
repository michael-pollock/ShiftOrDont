# ShiftOrDont
Personal project for fun. Challenging my knowledge of Java which I started learning a few months ago for college.  

KEY BINDINGS 
SHIFT: clutch. Push in to engage clutch, let out to disengage clutch. Note that every time you push in the clutch, the gear box will reset to the center neutral position. 
ENTER: Car ignition.
W: Gas pedal. 
S: Brake pedal. 
LEFT ARROW: Move shift knob to left. 
RIGHT ARROW: Move shift knob to right. 
UP ARROW: Move shift knob up. 
DOWN ARROW: Move shift knob down. 
I: Information about what is broken on the car. Currently problems are limited to stalled or blown engine. 
R: Reset car.  

HOW TO PLAY  
Hold SHIFT and press ENTER to start car. 
While holding SHIFT, rev with W, push down LEFT and UP arrow keys to put car in first gear. Let out SHIFT when rpms are around 1.5 or so. 
When RPMS get high, shift into second by pushing in SHIFT, then pushing down the LEFT and DOWN arrow keys. Let out shift and continue driving. 
There are currently 6 gears to go through. 
Try not to blow the engine or stall the car. 

MECHANICS  If your rpms go too low, the car will stall, and you will need to press R to restart the car. 
If your rpms go too high, the engine will blow. Press R to restart the car. 
RPMS grow exponentially but this poses a problem for 1st gear which starts at 0mph. I have a workaround that works ok but could be better. 
When shifting into a gear, a method is called to put the rpms at the correct location for that speed in that gear. If you shift into the wrong gear it will be very easy to stall or blow the engine. 

OTHER INFORMATION  I recently learned about switch cases' but I have much to learn about implementing them. I currently have a ton of if statements. 

KNOWN ISSUES THAT I WILL LOOK AT SOON:  
When the car restarts, sometimes the tachometer no longer rests at 0. 
When the car stalls, it cannot simply be turned back on. It has to be reset to work in the gear it stalled out in.
