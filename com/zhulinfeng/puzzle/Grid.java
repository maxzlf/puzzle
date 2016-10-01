package com.zhulinfeng.puzzle;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Grid {
	/***********************private values*************************************/
	private Puzzle_GUI puzzle_gui;			//the gui
	private JButton button;
	private int number;						//the id of picture which will show on this grid
	private int x;							//the x position of the grid
	private int y;							//the y position of the grid
	private boolean visible;				//whether the grid can be seen
	private boolean has_picture;			//whether the grid has got picture
	
	
	/**
	 * the constructor
	 * @param puzzle_gui
	 */
	public Grid(Puzzle_GUI puzzle_gui){
		this.puzzle_gui = puzzle_gui;
		this.button = new JButton();
		this.button.setBackground(Color.LIGHT_GRAY);
//		this.button.setBorderPainted(false);
		this.button.addActionListener(puzzle_gui);
		this.number = 0;
		this.x = -1;
		this.y = -1;
		this.visible = true;
		this.has_picture = false;
	}
	
	
	/**
	 * if a button is clicked,then we come to this method
	 * first we check in which direction the grid can move
	 * if the direction is 0,the grid can't move
	 * 	 1:can move up
	 *   2:can move left
	 *   3:can move down
	 *   4:can move right
	 * if the grid can move,then we let this grid disappear
	 * and let the destination grid get the number of this grid
	 * and make it appear
	 * At last,we should check whether the game is over
	 */
	public void click(){
		int direction = this.can_move();
		if(direction != 0){
			int row=-1,col=-1;
			switch(direction){
			case 1: {row = this.x - 1; col = this.y; break;}
			case 2: {row = this.x; col = this.y-1; break;}
			case 3: {row = this.x + 1; col = this.y; break;}
			case 4: {row = this.x; col = this.y+1; break;}
			default:{System.out.println("Error in Picture.click"); System.exit(direction);};
			}
			this.disappear();
			this.puzzle_gui.get_grid(row, col).set_number(this.number);
			this.puzzle_gui.get_grid(row, col).appear();
			
			this.puzzle_gui.check();
		}
	}
	
	public void appear(){
		this.visible = true;
		this.button.setVisible(true);
	}
	public void disappear(){
		this.visible = false;
		this.button.setVisible(false);
	}
	
	/*********************gets and sets****************************************/
	
	public void set_number(int number){
		this.number = number;
		if(number > 0){
			if(this.puzzle_gui.get_mode()){
				ImageIcon img = new ImageIcon(this.puzzle_gui.get_path() + number + ".gif","picture");
				this.button.setIcon(img);
			}else{
				this.button.setText("<html><font size=10>"+number+"</font></html>");
			}
		}
	}
	public void set_x(int x){
		this.x = x;
	}
	public void set_y(int y){
		this.y = y;
	}
	public void set_has_picture(boolean has_picture){
		this.has_picture = has_picture;
	}
	
	public JButton get_button(){
		return this.button;
	}
	public int get_number(){
		return this.number;
	}
	public int get_x(){
		return this.x;
	}
	public int get_y(){
		return y;
	}
	public boolean get_visible(){
		return this.visible;
	}
	public boolean get_has_picture(){
		return this.has_picture;
	}
	
	
	
	/**
	 * to check in which direction the grid can move
	 * 0: can't move
	 * 1: move up
	 * 2: move left
	 * 3: move down
	 * 4: move right
	 * At last,return the direction
	 * @return
	 */
	private int can_move(){		
		int direction = 0;
		
		if( (this.x - 1) >= 0){					//up
			if(!this.puzzle_gui.get_grid(this.x-1, this.y).get_visible()){	
				direction = 1;
			}
		}
		if( (this.y-1) >= 0 ){			//left
			if(!this.puzzle_gui.get_grid(this.x, this.y-1).get_visible()){	
				direction = 2;
			}
		}
		if( (this.x + 1) < this.puzzle_gui.ROW ){	//down
			
			if(!this.puzzle_gui.get_grid(this.x+1, this.y).get_visible()){	
				direction = 3;
			}
		}
		if( (this.y+1) < this.puzzle_gui.COL ){		//right
			if(!this.puzzle_gui.get_grid(this.x, this.y+1).get_visible()){	
				direction = 4;
			}
		}
		return direction;
	}
	
}
