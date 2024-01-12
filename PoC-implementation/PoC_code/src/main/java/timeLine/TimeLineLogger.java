package timeLine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import beanTimelineManager.Logger;

public class TimeLineLogger {

	private int currentStep;

	private boolean loggingMetaInfo = true;
	private boolean loggingMethods = true;
	private boolean loggingLiving = true;
	private boolean loggingCreated = true;
	private boolean loggingDestroyed = true;

	private boolean loggingOnFile = true;
	private boolean consoleLogging = true;
	private String logFilePath = System.getProperty("user.dir") + File.separator + "log";
	private String logFileName;
	private File logFile;

	public TimeLineLogger() {
		currentStep = -1;
		logFileName = "timeline-" + LocalDateTime.now() + ".txt";
		logFileName = logFileName.replace(':', '-'); // colon is not a valid character for files/directories
		if (loggingOnFile) {
			File logDir = new File(logFilePath);
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			logFile = new File(logFilePath + File.separator + logFileName);
		}
	}

	// LOGGING EXPECTED FORMAT

	/*
	 * ############################################################
	 * #################### START OF TIMELINE #####################
	 * ############################################################
	 *
	 * ########################## STEP 1 ########################## # Started at:
	 * [XX:XX:XX] # Ended at: [XX:XX:XX] # Session: [.................] # Method
	 * calls: # - [..........] # - [..........] # - [..........] # - [..........] #
	 * - [..........] # - [..........] # Living instances: # - [..........] # -
	 * [..........] # - [..........] # - [..........] # - [..........] # -
	 * [..........] # Created instances: # + [..........] # + [..........] # +
	 * [..........] # Destroyed instances: # x [..........] # x [..........] # x
	 * [..........] ###################### END OF STEP 1 #######################
	 *
	 * ########################## STEP 2 ########################## # [...]
	 */

	public void log(TimeLine timeLine) {

		if (currentStep == -1) {
			initializeStamp();
		}

		List<TimeStep> timeSteps = timeLine.getTimeSteps();
		// do not log the very last step because it has not been decorated with the
		// destroyed instances yet
		List<TimeStep> newSteps = timeSteps.subList(currentStep, timeSteps.size() - 1);
		printNewSteps(newSteps);

		currentStep = timeSteps.size() - 1;
	}

	public void concludeLog(TimeLine timeLine) {
		List<TimeStep> timeSteps = timeLine.getTimeSteps();
		List<TimeStep> newSteps = timeSteps.subList(currentStep, timeSteps.size());
		printNewSteps(newSteps);

		String str = "############################################################" + '\n'
				+ "###################### END OF TIMELINE #####################" + '\n'
				+ "############################################################" + '\n';
		str += '\n';
		str += '\n';

		logStep(str);
	}

	private void initializeStamp() {
		String head = "############################################################" + '\n'
				+ "#################### START OF TIMELINE #####################" + '\n'
				+ "############################################################" + '\n';
		head += '\n';
		head += '\n';
		currentStep = 0;

		logStep(head, false);
	}

	private void printNewSteps(List<TimeStep> newSteps) {

		for (TimeStep step : newSteps) {
			String stepToPrint = stringifyStep(step);

			logStep(stepToPrint);
		}
	}

	private String stringifyStep(TimeStep step) {

		String stepString = "################ STEP " + step.getRequestId() + " ################" + '\n';

		if (loggingMetaInfo) {
			stepString += extractMetaInfo(step);
		}

		if (loggingMethods) {
			stepString += extractMethodCalls(step);
		}

		if (loggingLiving) {
			stepString += extractLivingInstances(step);
		}

		if (loggingCreated) {
			stepString += extractCreatedInstances(step);
		}

		if (loggingDestroyed) {
			stepString += extractDestroyed(step);
		}

		stepString += "###################### END OF STEP " + step.getRequestId() + " #######################" + '\n';
		stepString += '\n';
		stepString += '\n';

		return stepString;
	}

	private String extractMetaInfo(TimeStep step) {
		String metaInfo = "# Started at: " + step.getBeginTime() + '\n' + "# Ended at: " + step.getEndTime() + '\n'
				+ "# Session: " + step.getSessionId() + '\n' + "#" + '\n';
		metaInfo += "# Page ID: " + step.getPageId() + '\n' + "#" + '\n';
		return metaInfo;
	}

	private String extractMethodCalls(TimeStep step) {

		String methodCalls = "# Method calls: (" + step.getMethodCalls().size() + ")" + '\n';
		for (InstanceMethod method : step.getMethodCalls()) {
			methodCalls += "# - " + method.getDeclaringClassName() + "." + method.getMethodName() + "( ";
			for (Object object : method.getParameters()) {
				methodCalls += object.toString() + ", "; // XXX questa parte non è il massimo così...
				// serirebbe un qualcosa di simile a wosar 21 con il json
			}
			methodCalls += ")" + '\n';
		}
		methodCalls += "#" + '\n';
		return methodCalls;
	}

	private String extractLivingInstances(TimeStep step) {

		String livinInstances = "# Living instances: (" + step.getLivingInstances().size() + ")" + '\n';
		for (ContextualInstance instance : step.getLivingInstances()) {
			livinInstances += "# - " + instance.getClassName() + '\n';
		}
		livinInstances += "#" + '\n';
		return livinInstances;
	}

	private String extractCreatedInstances(TimeStep step) {
		String createdInstances = "# Created instances: (" + step.getGeneratedInstances().size() + ")" + '\n';
		for (ContextualInstance instance : step.getGeneratedInstances()) {
			createdInstances += "# + " + instance.getClassName() + '\n';
		}
		createdInstances += "#" + '\n';
		return createdInstances;
	}

	private String extractDestroyed(TimeStep step) {
		String destroyedInstances = "# Destroyed instances: (" + step.getDestroyedInstances().size() + ")" + '\n';
		for (ContextualInstance instance : step.getDestroyedInstances()) {
			destroyedInstances += "# x " + instance.getClassName() + '\n';
		}
		destroyedInstances += "#" + '\n';
		return destroyedInstances;
	}


	private void logStep(String str) {
		if(consoleLogging) {
			System.out.print(str);
		}
		if (loggingOnFile)
			writeStep(str, true);
	}

	private void logStep(String str, boolean append) {
		if(consoleLogging) {
			System.out.print(str);
		}
		if (loggingOnFile)
			writeStep(str, append);
	}

	private void writeStep(String str, boolean append) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile(), append));
			if (append) {
				writer.append(str);
			} else {
				writer.write(str);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
