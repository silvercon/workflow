package com.newfiber.workflow.enums;

/**
 * 工作流活动类型
 */
public interface IWorkflowActivityType {

    enum EventActivity implements IWorkflowActivityType {
        /**
         *
         */
        Event("Event", "事件"),
        StartEvent("startEvent", "开始事件"),
        EndEvent("endEvent", "结束事件")
        ;

        private final String typeKey;

        private final String typeName;

        EventActivity(String typeKey, String typeName) {
            this.typeKey = typeKey;
            this.typeName = typeName;
        }

        @Override
        public String getTypeKey() {
            return typeKey;
        }

        @Override
        public String getTypeName() {
            return typeName;
        }
    }

    enum TaskActivity implements IWorkflowActivityType {
        /**
         *
         */
        Task("Task", "任务"),
        UserTask("userTask", "用户任务"),
        ;

        private final String typeKey;

        private final String typeName;

        TaskActivity(String typeKey, String typeName) {
            this.typeKey = typeKey;
            this.typeName = typeName;
        }

        @Override
        public String getTypeKey() {
            return typeKey;
        }

        @Override
        public String getTypeName() {
            return typeName;
        }
    }

    String getTypeKey();

    String getTypeName();

}
