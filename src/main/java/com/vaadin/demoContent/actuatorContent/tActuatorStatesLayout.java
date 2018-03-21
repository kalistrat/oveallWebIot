package com.vaadin.demoContent.actuatorContent;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.demoContent.notificationContent.tNotificationListLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.demoContent.tTreeContentLayout;
import com.vaadin.commonFuctions;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;


/**
 * Created by kalistrat on 26.05.2017.
 */
public class tActuatorStatesLayout extends VerticalLayout {
    Button AddButton;
    Button DeleteButton;
    Button SaveButton;

    Table StatesTable;
    IndexedContainer StatesContainer;
    int iUserDeviceId;
    tTreeContentLayout iParentContentLayout;
    Integer iCurrentLeafId;


    public tActuatorStatesLayout(int eUserDeviceId
            ,tTreeContentLayout eParentContentLayout
            ,Integer eCurrentLeafId
    ){

        iUserDeviceId = eUserDeviceId;
        iParentContentLayout = eParentContentLayout;
        iCurrentLeafId = eCurrentLeafId;

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.TABLE.getHtml() + "  " + "Перечень возможных состояний устройства");
        Header.addStyleName(ValoTheme.LABEL_COLORED);
        Header.addStyleName(ValoTheme.LABEL_SMALL);

        SaveButton = new Button();
        SaveButton.setIcon(FontAwesome.SAVE);
        SaveButton.addStyleName(ValoTheme.BUTTON_SMALL);
        SaveButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        SaveButton.setEnabled(false);

        AddButton = new Button();
        AddButton.setIcon(VaadinIcons.PLUS);
        AddButton.addStyleName(ValoTheme.BUTTON_SMALL);
        AddButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);


        DeleteButton = new Button();
        DeleteButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
        DeleteButton.addStyleName(ValoTheme.BUTTON_SMALL);
        DeleteButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        DeleteButton.setData(this);


        HorizontalLayout HeaderButtons = new HorizontalLayout(
                DeleteButton
                ,AddButton
                ,SaveButton
        );
        HeaderButtons.setSpacing(true);
        HeaderButtons.setSizeUndefined();

        HorizontalLayout HeaderLayout = new HorizontalLayout(
                Header
                ,HeaderButtons
        );
        HeaderLayout.setWidth("100%");
        HeaderLayout.setHeightUndefined();
        HeaderLayout.setComponentAlignment(Header, Alignment.MIDDLE_LEFT);
        HeaderLayout.setComponentAlignment(HeaderButtons,Alignment.MIDDLE_RIGHT);

        StatesTable = new Table();
        StatesTable.setWidth("100%");

        StatesTable.setColumnHeader(1, "№");
        StatesTable.setColumnHeader(2, "Наименование<br/>состояния");
        StatesTable.setColumnHeader(3, "Код<br/>сообщения");
        StatesTable.setColumnHeader(4, "");
        StatesTable.setColumnHeader(5, "Δt, с");
        StatesTable.setColumnHeader(6, "Системы<br/>оповещения");

        StatesContainer = new IndexedContainer();
        StatesContainer.addContainerProperty(1, Integer.class, null);
        StatesContainer.addContainerProperty(2, TextField.class, null);
        StatesContainer.addContainerProperty(3, TextField.class, null);
        StatesContainer.addContainerProperty(4, Button.class, null);
        StatesContainer.addContainerProperty(5, TextField.class, null);
        StatesContainer.addContainerProperty(6, tNotificationListLayout.class, null);

        setStatesContainer();

        StatesTable.setContainerDataSource(StatesContainer);


        StatesTable.setPageLength(StatesContainer.size());



        StatesTable.addStyleName(ValoTheme.TABLE_COMPACT);
        StatesTable.addStyleName(ValoTheme.TABLE_SMALL);
        StatesTable.addStyleName("TableRow");


        StatesTable.setSelectable(true);



        VerticalLayout StatesTableLayout = new VerticalLayout(
                StatesTable
        );
        StatesTableLayout.setWidth("100%");
        StatesTableLayout.setHeightUndefined();
        StatesTableLayout.setComponentAlignment(StatesTable,Alignment.MIDDLE_CENTER);

        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,StatesTableLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);

    }

    public void setStatesContainer(){

        try {
            Class.forName(commonFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    commonFuctions.DB_URL
                    , commonFuctions.USER
                    , commonFuctions.PASS
            );

            String DataSql = "select @num1:=@num1+1 num\n" +
                    ",uas.actuator_state_name\n" +
                    ",uas.actuator_message_code\n" +
                    ",uas.user_actuator_state_id\n" +
                    ",(\n" +
                    "select concat(group_concat(nt.notification_code separator '|'),'|')\n" +
                    "from user_device_state_notification uno\n" +
                    "join notification_type nt on nt.notification_type_id=uno.notification_type_id\n" +
                    "where uno.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    ") notification_codes\n" +
                    ",uas.transition_time\n" +
                    "from user_actuator_state uas\n" +
                    "join user_device ud on ud.user_device_id=uas.user_device_id\n" +
                    "join mqtt_servers ser on ser.server_id=ud.mqqt_server_id\n" +
                    "join (select @num1:=0) t1\n" +
                    "where uas.user_device_id = ?";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);
            DataStmt.setInt(1,iUserDeviceId);

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {

                Item newItem = StatesContainer.addItem(DataRs.getInt(1));

                TextField NameTF = new TextField();
                NameTF.setValue(DataRs.getString(2));
                NameTF.setEnabled(false);
                NameTF.addStyleName(ValoTheme.TEXTFIELD_TINY);
                NameTF.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
                NameTF.setWidth("150px");

                TextField CodeTF = new TextField();
                CodeTF.setValue(DataRs.getString(3));
                CodeTF.setEnabled(false);
                CodeTF.addStyleName(ValoTheme.TEXTFIELD_TINY);
                CodeTF.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
                CodeTF.setWidth("100px");

                Button mqttCommitButton = new Button();
                mqttCommitButton.addStyleName(ValoTheme.BUTTON_SMALL);
                mqttCommitButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
                mqttCommitButton.setIcon(VaadinIcons.PLAY_CIRCLE);
                mqttCommitButton.setData(DataRs.getInt(4));

                TextField timeInt = new TextField();
                timeInt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
                timeInt.setValue(String.valueOf(DataRs.getInt(6)));
                timeInt.setEnabled(false);
                timeInt.setWidth("50px");

                tNotificationListLayout noteListLay = new tNotificationListLayout();
                noteListLay.setEnabledFalse();

                if (DataRs.getString(5) != null) {
                    for (String iNoteType : commonFuctions.GetListFromString(DataRs.getString(5), "|")) {
                        noteListLay.markNotification(iNoteType);
                    }
                }

                newItem.getItemProperty(1).setValue(DataRs.getInt(1));
                newItem.getItemProperty(2).setValue(NameTF);
                newItem.getItemProperty(3).setValue(CodeTF);
                newItem.getItemProperty(4).setValue(mqttCommitButton);
                newItem.getItemProperty(5).setValue(timeInt);
                newItem.getItemProperty(6).setValue(noteListLay);


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

