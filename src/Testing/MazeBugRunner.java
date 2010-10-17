  /**
   * Classname	MazeBugRunner.java
   * Version 1.0
   * Date	9/9
   * Copyright 08386153
   */

package Testing;

import java.awt.Color;

import UIcontrol.Location;
import UIcontrol.UnboundedGrid;

import DBcontrol.ActorWorld;
import DBcontrol.Rock;

/**
 * This class runs a world that contains maze bugs. <br />
 * This class is not tested on the AP CS A and AB exams.
 */
public class MazeBugRunner
{
    public static void main(String[] args)
    {
        ActorWorld world = new ActorWorld(); 
        world.add(new Location(0,0), new MazeBug());
        world.add(new Location(1,1),new Rock());
        world.show();
    }
}