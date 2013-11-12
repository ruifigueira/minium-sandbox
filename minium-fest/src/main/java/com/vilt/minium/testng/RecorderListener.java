package com.vilt.minium.testng;

import static com.vilt.minium.recorder.RecorderInteractions.startRecordingScreen;
import static com.vilt.minium.recorder.RecorderInteractions.stopRecording;
import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;

public class RecorderListener implements IInvokedMethodListener2 {
	
	private static class StreamGobbler extends Thread {
	    InputStream is;
	    String type;

	    private StreamGobbler(InputStream is, String type) {
	        this.is = is;
	        this.type = type;
	    }

	    @Override
	    public void run() {
	        try {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line = null;
	            while ((line = br.readLine()) != null)
	                System.out.println(type + "> " + line);
	        }
	        catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	}
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public RecorderListener() {
	}
	
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		beforeInvocation(method, testResult, null);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		afterInvocation(method, testResult, null);
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		try {
			if (method.isTestMethod()) {
				File file = getFile(method, testResult, context);
				startRecordingScreen(file);
				System.out.println(format("Video is being writen to %s", file.getAbsolutePath()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		if (method.isTestMethod()) {
			try {
				stopRecording();
				
				File aviFile = new File(context.getOutputDirectory(), getFilename(method, testResult));
				File mp4File = convertFile(aviFile);
				
//				Reporter.log(format(
//						"<embed type='application/x-vlc-plugin' pluginspage='http://www.videolan.org' version='VideoLAN.VLCPlugin.2' width='320' height='200' id='vlc' loop='no' autoplay='no' target='../%s/%s' />", 
//						new File(context.getOutputDirectory()).getName(),
//						getFilename(method, testResult)));
				
				Reporter.log(format(
						"<video width='320' height='200' controls><source src='../%s/%s' type='video/mp4'>Your browser does not support the video tag.</video>", 
						mp4File.getParentFile().getName(),
						mp4File.getName()));
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}			
		}
	}
	
	private File convertFile(File aviFile) {
		try {
			File mp4File = new File(aviFile.getAbsolutePath().replaceAll("\\.avi$", ".mp4"));
			
			Process p = new ProcessBuilder(
					"ffmpeg", 
					"-i", 
					aviFile.getAbsolutePath(),
					"-pix_fmt",
					"yuv420p",
					mp4File.getAbsolutePath()).start();
			
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");

			// start gobblers
			outputGobbler.start();
			errorGobbler.start();
			
			p.waitFor();
			
			return mp4File;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	protected File getFile(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		String filename = getFilename(method, testResult);
		File dir = new File(context.getOutputDirectory());
		if (!dir.exists()) dir.mkdirs();
		return new File(context.getOutputDirectory(), filename);
	}

	protected String getFilename(IInvokedMethod method, ITestResult testResult) {
		return format("%s_%s.avi", testResult.getName(), dateFormat.format(new Date(method.getDate())));
	}
}
