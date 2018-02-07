package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.List;


public class TaskList implements Serializable{
	 
		List<OptimizationTask> optimizationTaskList;

		public List<OptimizationTask> getOptimizationTaskList() {
			return optimizationTaskList;
		}

		public void setOptimizationTaskList(List<OptimizationTask> optimizationTaskList) {
			this.optimizationTaskList = optimizationTaskList;
		}


    }
