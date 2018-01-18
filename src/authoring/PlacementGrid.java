package authoring;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import display.sprites.BackgroundObject;
import display.sprites.InteractiveObject;
import util.path.Path;
import javafx.geometry.Point2D;

/**
 * 
 * @author bwelton
 *
 */
public class PlacementGrid extends GridPane {
	private static final int HUNDRED_SIZE_CONSTANT = 100;
	private GameArea area;
	private Path path;
	private Cell[][] cells;
	private int width;
	private int height;
	private int rowPercentage;
	private int colPercentage;
	private int cellSize;
	
	public PlacementGrid(GameArea area, int width, int height, int rowPercent, int colPercent, Path path) {
		this.width = width;
		this.height = height;
		this.path = path;
		this.area = area;
		this.rowPercentage = rowPercent;
		this.colPercentage = colPercent;
		this.cellSize = width/(100/rowPercentage);
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->activatePath(e));
		initializeLayout();
		initializeCells();
	}

	private void initializeLayout() {
		this.setPrefSize(width, height);
		for(int i = 0; i<100; i+=rowPercentage) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(HUNDRED_SIZE_CONSTANT/rowPercentage);
			this.getRowConstraints().add(row);
		}
		
		for(int i = 0; i<100; i+=colPercentage) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(HUNDRED_SIZE_CONSTANT/colPercentage);
			this.getColumnConstraints().add(col);
		}
	}
	
	private void initializeCells() {
		cells = new Cell[(HUNDRED_SIZE_CONSTANT/colPercentage)][(HUNDRED_SIZE_CONSTANT/rowPercentage)];
		for(int i = 0; i<(HUNDRED_SIZE_CONSTANT/rowPercentage); i++) {
			for(int j = 0; j<(HUNDRED_SIZE_CONSTANT/colPercentage);j++) {
				Cell cell = new Cell();
				this.add(cell, j, i); //column then row for this javafx command
				cells[i][j] = cell;
			}
		}
	}

	private void activatePath(MouseEvent e) {
		e.consume();
		int row = (int) e.getY()/(height/(HUNDRED_SIZE_CONSTANT/rowPercentage));
		int col = (int) e.getX()/(width/(HUNDRED_SIZE_CONSTANT/colPercentage));
		Cell cell = cells[row][col];
		
		double x = col*(width/(HUNDRED_SIZE_CONSTANT/colPercentage)) + cell.getWidth()/2;
		double y = row*(height/(HUNDRED_SIZE_CONSTANT/rowPercentage)) + cell.getHeight()/2;
		
		if(cell.pathActive()) {
			cell.deactivate();
		}else {
			area.gameAreaClicked(e, x, y);
			cell.activate();
		}
	}
	
	//Currently unused, might eventually change state when objects are placed in cells
	protected void updateCells(double x, double y) {
		Cell cell = calculateCell(x,y);
		if(cell.pathActive()) {
			return;
		}else {
			cell.activate();
		}
	}
	
	/**
	 * Need to update it to account for different sized objects 
	 * Change the assignToCell method and do different checking for odd/set
	 */
	public Point2D place(InteractiveObject interactive) {
		double minDistance = Double.MAX_VALUE;
		Point2D finalLocation = null;
		int finalRow = 0;
		int finalColumn = 0;
		for (int r = 0; r < cells.length - interactive.getSize()/cellSize + 1; r++) {
			for (int c = 0; c < cells[r].length - interactive.getSize()/cellSize + 1; c++) {
				Cell currCell = cells[r][c];
				Point2D cellLocation = new Point2D(currCell.getLayoutX(), currCell.getLayoutY());
				double totalDistance = Math.abs(cellLocation.distance(interactive.center()));
				if ((totalDistance <= minDistance) && (currCell.isEmpty() &&
						(!neighborsFull(r, c, interactive.getSize()/cellSize))) | 
						(interactive instanceof BackgroundObject)) {
					minDistance = totalDistance;
					finalLocation = cellLocation;
					finalRow = r;
					finalColumn = c;
				}
				
			}
		}
		assignToCells(finalRow, finalColumn, interactive);
		return finalLocation;
	}
	
	private boolean neighborsFull(int row, int col, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!cells[i+row][j+col].isEmpty()) return true;
			}
		}
		return false;
	}
	
	private void assignToCells(int finalRow, int finalCol, InteractiveObject currObject) {
		for (int i = 0; i < currObject.getSize()/cellSize; i++) {
			for (int j = 0; j < currObject.getSize()/cellSize; j++) {
				cells[i+finalRow][j+finalCol].assignToCell(currObject);
			}
		}
	}
	
	private void removeAssignments(int finalRow, int finalCol, InteractiveObject interactive) {
		for (int i = 0; i < interactive.getSize()/cellSize; i++) {
			for (int j = 0; j < interactive.getSize()/cellSize; j++) {
				cells[i + finalRow][j + finalCol].removeAssignment(interactive);
			}
		}
	}

	public void removeFromGrid(InteractiveObject interactive) {
		int col = (int) ((interactive.getX()) / cellSize);
		if (col >= 0) {
			int row = (int) ((interactive.getY()) / cellSize);
			removeAssignments(row, col, interactive);
		}

	}

	private Cell calculateCell(double x, double y) {
		int row = (int) y/(height/(HUNDRED_SIZE_CONSTANT/rowPercentage));
		int col = (int) x/(width/(HUNDRED_SIZE_CONSTANT/colPercentage));
		return cells[row][col];
	}

	protected void resizeGrid(int width, int height) {
		this.width = width;
		this.height = height;
		this.setPrefWidth(width);
		this.setPrefHeight(height);
	}
	
	protected void setActivePath(Path path) {
		this.path = path;
	}
}
