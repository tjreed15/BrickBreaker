package creator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import basics.Board;
import basics.FileToBoard;

public class BoardCreator {
	
	private File file;
	private boolean newFile;

	public BoardCreator(int level){
		try {
			file = new File("C:/Users/tjreed/workspace/BrickBreaker/Levels/Level" + level);
			if (file.createNewFile()){
				newFile = true;
			}
			else{
				newFile = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	
	public static void main(String[] args){
		Scanner kb = new Scanner(System.in);
		String answer;
		BoardCreator bc;
		Board board = null;
		boolean retry;
		do{
			System.out.println("What level would you like to edit?");
			int level = kb.nextInt(); kb.nextLine();
			bc = new BoardCreator(level);
			if(!bc.newFile){
				System.out.println("File already exists. Would you like to edit it");
				answer = kb.nextLine();
				if(answer.contains("y")){
					FileToBoard ftb = new FileToBoard();
					board = ftb.getBoard(level);
					retry = false;
				}
				else{
					System.out.println("Would you like to choose a different file to edit");
					answer = kb.nextLine();
					if(answer.contains("y")) retry = true;
					else return;
				}
			}
			else retry = false;
		} while(retry);
		System.out.println("Ended ready to update: " + bc.file.getName());
		GraphicalDesign gd = new GraphicalDesign(board);
		gd.setFile(bc.file);
		gd.start();
	}
	
}
