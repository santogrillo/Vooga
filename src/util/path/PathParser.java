package util.path;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bwelton
 *
 */
public class PathParser {

	public List<PathList> parse(Path path){
		List<PathList> paths = new ArrayList<>();
		for(PathPoint start:path.getStartPoints()) {
			recursiveParse(paths, new PathList(start), path, start);
		}
		return paths;
	}
	
	private void recursiveParse(List<PathList> paths, PathList particularPath, Path path, PathPoint point) {
		if(point.getNextLines().isEmpty()) {
			paths.add(particularPath);
			return;
		}

		for(PathPoint next:point.getNextLines().keySet()) {
			PathList pathCopy = particularPath.clone();
			if(!pathCopy.add(next)) {
				paths.add(pathCopy);
				break;
			}
			recursiveParse(paths, pathCopy, path, next);
		}
	}
}
