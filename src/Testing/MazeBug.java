  /**
   * Classname	MazeBug.java
   * Version 1.0
   * Date	9/9
   * Copyright 08386153
   */

package Testing;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;

import UIcontrol.*;

import DBcontrol.Actor;
import DBcontrol.Bug;
import DBcontrol.Flower;
import DBcontrol.Rock;

/**
 * A <code>MazeBug</code> can find its way in a maze. <br />
 * The implementation of this class is testable on the AP CS A and AB exams.
 */
public class MazeBug extends Bug {
	
	public Location next;
	public Location last;
	public boolean isEnd = false;
	
	/**
	 * The peak element stores the locations which can be there in the next step.
	 */
	public Stack<ArrayList<Location>> crossLocation = new Stack<ArrayList<Location>>();
	
	/**
	 * stores the moving direction the last points to now.
	 */
	public Stack<Integer>  directionlist=new Stack<Integer>();
	
	/**
	 * stores the locations which has been to.
	 */
	public Stack<Location> locationlist= new Stack<Location>();
	
	public Integer stepCount = 0;
	
	/**
	 * Uses for initial.0 means not initialed yet.
	 */
	public int init ;
	
	boolean hasShown = false;//final message has been shown
 
	/**
	 * Constructs a box bug that traces a square of a given side length.
	 */
	public MazeBug() 
	{
		setColor(Color.GREEN);
		init=0;
	}

	
	/**
	 * Moves to the next location of the square.<br>
	 * @see DBcontrol.Bug#act()
	 */
	public void act() 
	{	
		//Initial. Stores the locations which can be reached in the first location.
		//Push it into the stack
		if(init==0)
		{
		  last=this.getLocation();
		  next = last;
		  crossLocation.push(getValid(last));
		  init=1;
		}
		
		if (isEnd == true) 
		{
		//to show step count when reach the goal		
			if (hasShown == false) 
			{
				String msg = stepCount.toString() + " steps";
				JOptionPane.showMessageDialog(null, msg);
				hasShown = true;
			}
		} 
		else  
		{
			if(!crossLocation.isEmpty())
			{	
				//peak's size is greater than 0, needs to move forward from one of the locations.
			    if(crossLocation.peek().size()!=0)
				 {
			    	 
	  				 for(Location loc: crossLocation.peek())
	  				 {
	  					 Actor redrock=getGrid().get(loc);
	  					 //Find the end of the maze.
	    		         if(redrock instanceof Rock && redrock.getColor().getRGB()==Color.RED.getRGB())
	    		         {
	    		        	isEnd=true; 
	    		         	return;
	    		         }  
	  				 }
			     
	  				 //random choose a direction to move.
					 double r = Math.random()*crossLocation.peek().size();
		             next=crossLocation.peek().get((int)Math.floor(r));
		             
		             //After a success move, top of stack directionlist sotres the
		             //direction from the last location to local location.
		          	 directionlist.push(this.getDirection());
		   		     last=getLocation();
		   	         
		   		     //After a success move, top of the stack locationlist stores the
		   		     //last location it just been on.
		   		     locationlist.push(last);

		   	         
		           	 if(canMove())
		           	 {
		       	         move();
				         stepCount++;      		 
		   		         crossLocation.push(getValid(next));
		   		         last=next;
		   		         return;
		             }
                 }
				 
			    //peak's size is 0, need to fall back.
				else
	            {
					
					Location putflower=getLocation();			
		        	this.moveTo(locationlist.pop()); 	   
	           		this.setDirection(directionlist.pop());
	           		Flower flower = new Flower(getColor());
	           		flower.putSelfInGrid(getGrid(), putflower);
	
	           		//a two-time pop and a push means to pop the top 
	           		//and renew the element below the top
		           	crossLocation.pop();
		           	crossLocation.pop();
		           	crossLocation.push(getValid(getLocation()));
		           	    
		            return;
	            }
	           
            
			} 
		}
	}
	

	/**
	 * Find all positions that can be move to.<br>
	 * Add locations contains red stones and null to the valid array.<br>
	 * @param loc the location to detect.
	 * @return List of positions.
	 */
	public ArrayList<Location> getValid(Location loc) 
	{
		Grid<Actor> gr = getGrid();
		if (gr == null) 
			return null;
		
		ArrayList<Location> valid = new ArrayList<Location>();
		
		
        int[] directions=
        { Location.NORTH, Location.SOUTH,Location.WEST,Location.EAST};
        
        for (int dirs : directions)
        {
            Location neighborLoc = loc.getAdjacentLocation(getDirection() + dirs);
            if (gr.isValid(neighborLoc))
            {
            Actor act =  gr.get(neighborLoc);

            //Only red stones and null locations can be added into the stack.
            if( act instanceof Rock && act.getColor().getRGB()==Color.RED.getRGB() ) {
            	
            	    valid.add(neighborLoc);
            }
            else if( act == null)                  	
                  valid.add(neighborLoc);
            }
        }
		
		return valid;
	}

	/**
	 * Can only move to valid positions with null inside.<br>
	 * @return true if this bug can move.
	 */
	public boolean canMove() 
	{
	        Grid<Actor> gr = getGrid();
	        if (gr == null)
	            return false;
	        
	         
	        if (!gr.isValid(next))
	            return false;

	        Actor neighbor = gr.get(next);
	        return (neighbor == null) ;
	    
	}
	/**
	 * Moves the bug forward, putting a flower into the location it previously
	 * occupied.
	 */
	public void move() 
	{
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		if (gr.isValid(next)) {
			setDirection(getLocation().getDirectionToward(next));
			moveTo(next);
		} else
			removeSelfFromGrid();
		Flower flower = new Flower(getColor());
		flower.putSelfInGrid(gr, loc);
	}
}