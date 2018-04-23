package com.vneuron.test;

import org.alfresco.repo.workflow.WorkflowModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StartWorkflowGet extends DeclarativeWebScript {

	private static final String PARAM_WORKFLOW_ID = "wfId";
	private static final String PARAM_NBR_INSTANCE = "nbrInstance";

	private WorkflowService workflowService;

	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		Map<QName, Serializable> parameters = new HashMap<QName, Serializable>();
		String wfId = getWorkflowId(req);
		Integer nbrInstance = getNbrInstance(req);
		for (int i = 0; i < nbrInstance; i++) {
			WorkflowDefinition wfDefinition = workflowService.getDefinitionByName(wfId);
			NodeRef workflowBpmPackage = workflowService.createPackage(null);
			parameters.put(WorkflowModel.ASSOC_PACKAGE, workflowBpmPackage);
			parameters.put(WorkflowModel.PROP_CONTEXT, workflowBpmPackage);
			workflowService.startWorkflow(wfDefinition.getId(), parameters);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("state", "done");
		return model;
	}

	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	private String getWorkflowId(WebScriptRequest req) {
		String wfId = req.getParameter(PARAM_WORKFLOW_ID);
		return wfId;
	}

	private Integer getNbrInstance(WebScriptRequest req) {
		String nbrInstanceParam = req.getParameter(PARAM_NBR_INSTANCE);
		Integer nbrInstance = 100;
		if (nbrInstanceParam != null && nbrInstanceParam.length() != 0) {
			nbrInstance = Integer.parseInt(nbrInstanceParam);
		}
		return nbrInstance;
	}
}
