package com.vilt.minium.recorder;

import com.vilt.minium.CoreWebElements;
import com.vilt.minium.impl.actions.DefaultInteraction;
import com.vilt.minium.recorder.impl.WebElementsScreenRecorder;

public abstract class RecorderInteraction extends DefaultInteraction {

	protected WebElementsScreenRecorder recorder;

	public RecorderInteraction(CoreWebElements<?> elems, WebElementsScreenRecorder recorder) {
		super(elems);
		this.recorder = recorder;
	}
}
