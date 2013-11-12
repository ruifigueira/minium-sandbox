package com.vilt.minium.recorder;

import java.io.File;

import org.monte.screenrecorder.ScreenRecorder.State;

import com.vilt.minium.CoreWebElements;
import com.vilt.minium.recorder.impl.WebElementsScreenRecorder;

public class StartRecorderInteraction extends RecorderInteraction {

	private File file;

	public StartRecorderInteraction(CoreWebElements<?> elems, WebElementsScreenRecorder recorder, File file) {
		super(elems, recorder);
		this.file = file;
	}
	
	@Override
	protected void doPerform() {
		try {
			if (recorder.getState() == State.RECORDING) throw new IllegalStateException("Recorder is not recording");
			recorder.start(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
