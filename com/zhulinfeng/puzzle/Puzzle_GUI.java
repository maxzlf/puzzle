package com.zhulinfeng.puzzle;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Puzzle_GUI extends JFrame implements ActionListener{
	/******************the values for the game*********************************/
	private final int style_limit = 10;		//how many picture styles the game have				
	private static int style;				//the current style	
	private static int level = 3;			//the current level				
	private static boolean mode = true;;	//the current game mode (number or picture)		
											//and false for number, true for picture		
	private String path;					//the path to find pictures
	private Grid grids[][];
	
	/************************values to show on window*************************/
	private int steps = 0;
	private Timer time;						//a timer
	private int time_count=0;				//count how many seconds have pass since game begin
	
	
	
	/*********************************the values for window********************/
	public int ROW = 4;
	public int COL = this.ROW;	
	public int PICTURES = this.ROW * this.COL;
	private final int UNIT = 120;
	private int WIDTH = this.COL * this.UNIT;
	private int HEIGHT = this.ROW * this.UNIT;
	private int X_LOCATION = 1800/this.COL;
	private int Y_LOCATION = 120/this.ROW;

	
	/***********************controllers of window******************************/
	private Container c;
	private CardLayout card;
	private JPanel game_panel;				//play game on this panel
	private JPanel pic_panel;				//show picture on this panel
	private JLabel bg;						//show picture on this label	
	private JLabel time_label;				//show time on this label
	private JLabel step_label;				//show steps on this label
	
	/***********************menu bar*******************************************/
	private JMenuItem item11;
	private JMenuItem item12;
	private JMenuItem item13;
	private JMenuItem item14;
	private JMenuItem item15;
	private JMenuItem item16;
	
	private JMenuItem item21;
	private JMenuItem item22;
	private JMenuItem item23;
	
	private JMenuItem item31;	
	

	/**
	 * constructor
	 * @param level
	 */
	public Puzzle_GUI(int level){
		/***********************set the values for window**********************/
		this.ROW = level;
		this.COL = this.ROW;	
		this.WIDTH = this.COL * this.UNIT + 8;
		this.HEIGHT = this.ROW * this.UNIT + 40 + 25;
		this.X_LOCATION = 1800/this.COL;
		this.Y_LOCATION = 120/this.ROW;
		this.PICTURES = this.ROW * this.COL;
		
		/******************set the frame of window*****************************/
		this.setTitle("puzzle");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.WIDTH,this.HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation(this.X_LOCATION, this.Y_LOCATION);
		
		
		/***********************middle container*******************************/
		c = this.getContentPane();
		this.card = new CardLayout(0,0);
		c.setLayout(this.card);
		
		this.game_panel = new JPanel(new BorderLayout());
		JPanel btn_panel = new JPanel(new GridLayout(this.ROW,this.COL));
		this.pic_panel = new JPanel();									
		this.bg = new JLabel();											
		
		
		/*************************grids****************************************/
		this.grids = new Grid[this.ROW][this.COL];
		for(int i=0; i< this.ROW; i++){
			for(int j=0; j< this.COL; j++){
				this.grids[i][j] = new Grid(this);
				btn_panel.add(this.grids[i][j].get_button());
				this.grids[i][j].set_x(i);
				this.grids[i][j].set_y(j);
			}
		}
		
		/*********************************bottom*******************************/
		JPanel bottom = new JPanel();
		bottom.setSize(this.WIDTH, 30);
		bottom.setLayout(new FlowLayout());
		this.time_label = new JLabel();
		this.step_label= new JLabel();
		bottom.add(time_label);
		bottom.add(this.step_label);
		
		
		/********************add panels to middle container********************/
		this.pic_panel.add(bg);
		c.add(this.pic_panel,"pic_panel");
		this.game_panel.add(btn_panel,"Center");
		this.game_panel.add(bottom,"South");
		c.add(this.game_panel,"game_panel");
		
		
		/*******************set menu bar***************************************/
		JMenuBar menu = new JMenuBar();
		this.setJMenuBar(menu);
		
		JMenu menu1 = new JMenu("game");
		JMenu menu2 = new JMenu("mode");
		JMenu menu3 = new JMenu("info");
		
		menu.add(menu1);
		menu.add(menu2);
		menu.add(menu3);
		
		item11 = new JMenuItem("new game");
		item12 = new JMenuItem("3x3");
		item13 = new JMenuItem("4x4");
		item14 = new JMenuItem("5x5");
		item15 = new JMenuItem("6x6");
		item16 = new JMenuItem("exit");
		
		item21 = new JMenuItem("show picture");
		item22 = new JMenuItem("number mode");
		item23 = new JMenuItem("picture mode");
		
		item31 = new JMenuItem("info");
		
		menu1.add(item11);
		menu1.add(item12);
		menu1.add(item13);
		menu1.add(item14);
		menu1.add(item15);
		menu1.add(item16);
		menu2.add(item21);
		menu2.add(item22);
		menu2.add(item23);
		menu3.add(item31);
		
		item11.addActionListener(this);
		item12.addActionListener(this);
		item13.addActionListener(this);
		item14.addActionListener(this);
		item15.addActionListener(this);
		item16.addActionListener(this);
		item21.addActionListener(this);
		item22.addActionListener(this);
		item23.addActionListener(this);
		item31.addActionListener(this);
	
		
		/****************make some init ***************************************/
		this.card.show(c, "game_panel");
//		this.item14.setEnabled(false);
//		this.item15.setEnabled(false);
		this.time = new Timer(1000, this);
	
		this.init();
	}//end of construction
	

	/**
	 * init the game
	 * start the time
	 * choose a style
	 * get path
	 * set the background image
	 * reset the bottom message panel
	 * reset all the grids
	 * arrange all the grids
	 * let the last grid disappear
	 * then at last,if the deadlock occurs,we init again
	 */
	private void init(){
		if(!this.mode){
			this.item21.setEnabled(false);
		}else{
			this.item21.setEnabled(true);
		}
		this.time.start();
		this.set_style();
		this.set_path();
		this.set_bgimg();
		this.reset_steps();
		this.reset_timecount();
		this.set_step_show();
		this.set_time_show();
			
		this.reset_grids();
		this.arrage_grids();
		this.grids[this.ROW-1][this.COL-1].disappear();
		
		if(this.avoid_deadlock() % 2 != 0){
			this.init();
		}
	}
	
	/**
	 * reset all the grids
	 * set their number to 0
	 * set their has_picture false
	 * let all of them appear
	 * clear the pictures or numbers on the button
	 */
	private void reset_grids(){
		for(int i=0; i<this.ROW; i++){
			for(int j=0; j<this.COL; j++){	
				this.grids[i][j].set_number(0);
				this.grids[i][j].set_has_picture(false);
				this.grids[i][j].appear();
				this.grids[i][j].get_button().setIcon(null);
			}
		}
	}
	
	/**
	 * attribute numbers to each grid randomly
	 */
	private void arrage_grids(){
		Random rand = new Random();
		for(int i=1; i <= this.PICTURES -1; i++){
			int grid_index = rand.nextInt(this.PICTURES - 1);
			int row = grid_index / this.COL;
			int col = grid_index % this.ROW;
			while(this.grids[row][col].get_has_picture()){
				grid_index = rand.nextInt(this.PICTURES - 1);
				row = grid_index / this.COL;
				col = grid_index % this.ROW;
			}
			this.grids[row][col].set_has_picture(true);
			this.grids[row][col].set_number(i);	
		}
	}
	

	/**
	 * in puzzle game,there will have a situation that the game can't be succeeded
	 * so, in this method, we will check the situation in order to avoid it
	 * @return
	 */
	private int avoid_deadlock(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		int negative_count = 0;			//count the negative orders in the array	
		
		for(int i=0; i< this.PICTURES-1; i++){
			int row = i / this.COL;
			int col = i % this.COL;
			list.add(this.grids[row][col].get_number());
		}
		
		for(int i=1; i<list.size(); i++){
			for(int j=0; j < i; j++){
				if( list.get(j) > list.get(i)){
					negative_count ++;
				}
			}
		}
		return negative_count;
	}
	
	
	
	/**
	 *check each time after grid moved
	 *first change the count of steps and show it on the window
	 *then check if game is over
	 *if the mode is for picture,the there is just one situation to game over
	 *but if the mode is for number, then there can be two situations to game over
	 *when game over,stop the time,and let the last grid appear	
	 */
	public void check(){
		this.steps ++;
		this.set_step_show();
		
		boolean case1 = true;
		boolean case2 = true;
		
		if(this.grids[this.ROW-1][this.COL-1].get_visible()){
			case1 = false;
		}
		if(this.grids[0][0].get_visible()){
			case2 = false;
		}
			
		for(int grid_index = 0; grid_index < this.PICTURES -1; grid_index++){
			int row = grid_index / this.COL;
			int col = grid_index % this.COL;
			if(this.grids[row][col].get_number() != grid_index+1){
				case1 = false;
				break;
			}
		}
		
		for(int grid_index = 1; grid_index < this.PICTURES; grid_index++){
			int row = grid_index / this.COL;
			int col = grid_index % this.COL;
			if(this.grids[row][col].get_number() != this.PICTURES-grid_index){
				case2 = false;
				break;
			}
		}

		if(case1){
			this.time.stop();
			this.grids[this.ROW-1][this.COL-1].appear();
			this.grids[this.ROW-1][this.COL-1].set_number(this.PICTURES);
		}
		if(case2 && !this.mode){
			this.time.stop();
			this.grids[0][0].appear();
			this.grids[0][0].set_number(this.PICTURES);
		}
	}
	
	/************************gets and sets*************************************/
	private void set_style(){
		Random rand = new Random();
		this.style = rand.nextInt(this.style_limit) + 1;
	}
	private void set_path(){
		this.path = "img/"+this.level + "/" + this.style + "/";
	}
	private void set_bgimg(){
		ImageIcon icon = new ImageIcon(this.path + "0.jpg","picture");
		this.bg.setIcon(icon);	
	}
	private void set_mode(boolean mode){
		this.mode = mode;
	}
	private void reset_timecount(){
		this.time_count = 0;
	}
	private void reset_steps(){
		this.steps = 0;
	}
	private void set_time_show(){
		int minute = this.time_count / 60;
		int second = this.time_count % 60;
		StringBuilder str = new StringBuilder();
		
		str.append("time : ");
		if(minute < 10){
			str.append("0" + minute + " : ");
		}else{
			str.append(minute + " : ");
		}
		if(second < 10){
			str.append("0" + second);
		}else{
			str.append(second);
		}
		this.time_label.setText("" + str);
	}
	private void set_step_show(){
		this.step_label.setText("       steps : " + this.steps);
	}
	
	public boolean get_mode(){
		return this.mode;
	}
	public String get_path(){
		return this.path;
	}
	public Grid get_grid(int row, int col){
		if(row < this.ROW && col < this.COL && row >= 0 && col >= 0){
			return this.grids[row][col];
		}else{
			return null;
		}
	}
	

	@Override
	/**
	 * response for actions
	 * the change of time
	 * click on the button
	 * click the menu
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		/************************time******************************************/
		if(e.getSource() == this.time){
			this.time_count++;
			this.set_time_show();
			if(this.time_count > 3600){
				System.exit(0);
			}
		}	
		
		//***************************buttons************************************
		for(int i=0; i< this.ROW; i++){
			for(int j=0; j< this.COL; j++){
				if(e.getSource() == this.grids[i][j].get_button()){
					this.grids[i][j].click();
				}
			}
		}
		
		/**********************************************************************/
		/******************************menu bar********************************/	
		if(e.getSource() == this.item11){
			this.init();
		}
		
		if(e.getSource() == this.item12){
			this.dispose();
			this.level = 3;
			new Puzzle_GUI(this.level);
		}
		
		if(e.getSource() == this.item13){
			this.dispose();
			this.level = 4;
			new Puzzle_GUI(this.level);
		}
		
		if(e.getSource() == this.item14){
			this.dispose();
			this.level = 5;
			new Puzzle_GUI(this.level);
		}
		
		if(e.getSource() == this.item15){
			this.dispose();
			this.level = 6;
			new Puzzle_GUI(this.level);
		}
		
		if(e.getSource() == this.item16){
			System.exit(0);
		}
		
		if(e.getSource() == this.item21){
			if(this.mode){
				if(this.item21.getText() == "show picture"){
					this.item21.setText("continue game");
					this.card.show(c,"pic_panel");
					
				}else{
					this.item21.setText("show picture");
					this.card.show(c, "game_panel");
				}
				
			}
		}
		
		if(e.getSource() == this.item22){
			if(this.mode){
				this.set_mode(false);
				this.card.show(c, "game_panel");
				this.item21.setText("show picture");
				this.init();
			}
		}
		
		if(e.getSource() == this.item23){
			if(!this.mode){
				this.set_mode(true);
				
				this.init();
			}
		}
		
		if(e.getSource() == this.item31){
			StringBuilder msg = new StringBuilder();
			msg.append("Puzzle.\n\n");
			msg.append("Zhu Linfeng, from Huzzhong University,China\n");
			msg.append("contact with me : 1453065649@qq.com\n");
			msg.append("                                    2013-3-25");
			JOptionPane.showMessageDialog(null, msg);
		}
	}
	
	public static void main(String args[]){
		new Puzzle_GUI(3);
	}

}
