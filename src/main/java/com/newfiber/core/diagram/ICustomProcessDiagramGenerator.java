package com.newfiber.core.diagram;

import java.awt.Color;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.image.ProcessDiagramGenerator;

/**
 * @author : X.K
 * @since : 2022/2/24 下午1:39
 */
public interface ICustomProcessDiagramGenerator extends ProcessDiagramGenerator {
	InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities,
		List<String> highLightedFlows, String activityFontName, String labelFontName, String annotationFontName,
		ClassLoader customClassLoader, double scaleFactor, Color[] colors, Set<String> currIds);
}
