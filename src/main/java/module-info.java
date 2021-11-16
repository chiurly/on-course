module oncourse.oncourse {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.persistence;
    requires mysql.connector.java;
    requires org.hibernate.orm.core;

    requires spring.core;
    requires spring.context;
    requires spring.web;
    requires com.google.gson;


    opens oncourse.oncourse to javafx.fxml;
    exports oncourse.oncourse;

    opens oncourse.oncourse.ds to javafx.fxml, org.hibernate.orm.core, java.persistence;
    exports oncourse.oncourse.ds;
    exports oncourse.oncourse.fxcontroller;
    opens oncourse.oncourse.fxcontroller to javafx.fxml;
}