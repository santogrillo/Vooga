package player;

import java.util.ArrayList;
import java.util.List;
import networking.AbstractClient;
import java.util.Map;
import java.util.TreeMap;

import networking.MultiPlayerClient;
import util.PropertiesGetter;

public class ActivityListBox extends MultiplayerListBox{
	private final String BOT_NAME = "bot";
	private final String STRING_APPENDER = "stringAppender";
	
//	private Map<String, Integer> activityLog;
	private List<String> activityLog;
	
	public ActivityListBox() {
		super();
//		activityLog = new TreeMap<String, Integer>();
		activityLog = new ArrayList<String>();
	}
	
	public void setNames(AbstractClient multi) {
		activityLog.clear();
		/*
		for(String lobby : multi.getGameRooms()) {
			multi.joinGameRoom(lobby, PropertiesGetter.getProperty(BOT_NAME));
//			activityLog.put(lobby, multi.getPlayerNames().size());
			activityLog.add(lobby + PropertiesGetter.getProperty(STRING_APPENDER) + (multi.getPlayerNames().size() - 1));
			System.out.println(lobby + PropertiesGetter.getProperty(STRING_APPENDER) + multi.getPlayerNames().size());
			multi.exitGameRoom();
		}
		setNames(activityLog);
		*/
	}
}
