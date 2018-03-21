package com.vaadin.demoContent.actuatorContent;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.tUsefulFuctions;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by kalistrat on 26.05.2017.
 */
public class tActuatorDataFormLayout extends VerticalLayout {

    int iUserDeviceId;

    TextField NameTextField;
    TextField DetectorAddDate;
    TextField InTopicNameField;


    public tActuatorDataFormLayout(int eUserDeviceId) {

        iUserDeviceId = eUserDeviceId;

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.FORM.getHtml() + "  " + "Параметры исполнительного устройства");
        Header.addStyleName(ValoTheme.LABEL_COLORED);
        Header.addStyleName(ValoTheme.LABEL_SMALL);

        HorizontalLayout FormHeaderLayout = new HorizontalLayout(
                Header
                //, FormHeaderButtons
        );
        FormHeaderLayout.setWidth("100%");
        FormHeaderLayout.setHeightUndefined();
        FormHeaderLayout.setComponentAlignment(Header, Alignment.MIDDLE_LEFT);
        //FormHeaderLayout.setComponentAlignment(FormHeaderButtons, Alignment.MIDDLE_RIGHT);

        NameTextField = new TextField("Наименование устройства :");
        NameTextField.setEnabled(false);
        DetectorAddDate = new TextField("Дата добавления устройства :");
        DetectorAddDate.setEnabled(false);
        InTopicNameField = new TextField("mqtt-топик для записи :");
        InTopicNameField.setEnabled(false);

        setActuatorParameters();


        FormLayout ActuatorForm = new FormLayout(
                NameTextField
                , DetectorAddDate
                , InTopicNameField
        );

        ActuatorForm.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        ActuatorForm.addStyleName("FormFont");
        ActuatorForm.setMargin(false);

        VerticalLayout ActuatorFormLayout = new VerticalLayout(
                ActuatorForm
        );
        ActuatorFormLayout.addStyleName(ValoTheme.LAYOUT_CARD);
        ActuatorFormLayout.setWidth("100%");
        ActuatorFormLayout.setHeightUndefined();

        VerticalLayout ContentLayout = new VerticalLayout(
                FormHeaderLayout
                , ActuatorFormLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);
    }

    public void setActuatorParameters(){

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            String DataSql = "select ud.device_user_name\n" +
                    ",ud.user_device_date_from\n" +
                    ",ud.mqtt_topic_write\n" +
                    ",ud.mqtt_topic_read\n" +
                    ",ser.server_ip mqqtt\n" +
                    ",ifnull(ud.device_log,'')\n" +
                    ",ifnull(ud.device_pass,'')\n" +
                    "from user_device ud\n" +
                    "join mqtt_servers ser on ser.server_id=ud.mqqt_server_id\n" +
                    "where ud.user_device_id = ?";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);
            DataStmt.setInt(1,iUserDeviceId);

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {
                NameTextField.setValue(DataRs.getString(1));
                if (DataRs.getTimestamp(2) != null) {
                    DetectorAddDate.setValue(df.format(new Date(DataRs.getTimestamp(2).getTime())));
                } else {
                    DetectorAddDate.setValue("");
                }
                InTopicNameField.setValue(DataRs.getString(3));
            }


            Con.close();

        } catch (SQLException se3) {
            //Handle errors for JDBC
            se3.printStackTrace();
        } catch (Exception e13) {
            //Handle errors for Class.forName
            e13.printStackTrace();
        }
    }

}
