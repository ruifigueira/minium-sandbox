/*
 * Copyright (C) 2013 The Minium Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
