package org.unix4j.unix.sort;

import java.util.ArrayList;
import java.util.Collections;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * Line processor for normal in-memory sort using an {@link ArrayList} to cache
 * and sort the lines.
 */
class SortLineProcessor extends AbstractSortLineProcessor {
	
	private ArrayList<Line> lineBuffer = new ArrayList<Line>();

	public SortLineProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		lineBuffer.add(line);
		return true;//we want all the lines
	}

	@Override
	public void finish() {
		final LineProcessor output = getOutput();
		Collections.sort(lineBuffer, getComparator());
		final int size = lineBuffer.size();
		for (int i = 0; i < size; i++) {
			final Line line = lineBuffer.set(i, null);//clear the line in the buffer
			if (!output.processLine(line)) {
				break;//they want no more lines
			}
		}
		lineBuffer = null;//free for gc
		output.finish();
	}

}
