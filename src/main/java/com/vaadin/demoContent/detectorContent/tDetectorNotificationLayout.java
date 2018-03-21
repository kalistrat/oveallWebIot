package com.vaadin.demoContent.detectorContent;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.demoContent.notificationContent.tNotificationConditionSelect;
import com.vaadin.demoContent.notificationContent.tNotificationCriteriaField;
import com.vaadin.demoContent.notificationContent.tNotificationListLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.demoContent.tTreeContentLayout;
import com.vaadin.commonFuctions;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalistrat on 01.11.2017.
 */
public class tDetectorNotificationLayout extends VerticalLayout {
    Button AddButton;
    Button DeleteButton;
    Button SaveButton;

    Table iNotificationTable;
    IndexedContainer iNotificationContainer;
    int iUserDeviceId;
    tTreeContentLayout iParentContentLayout;

    class stateIdMap{
        int itemId;
        int stateId;
        public stateIdMap(int itId,int stId){
            itemId = itId;
            stateId = stId;
        }
    }

    List<stateIdMap> statesList;

    public tDetectorNotificationLayout(
            int eUserDeviceId
            ,tTreeContentLayout eParentContentLayout
    ){

        iUserDeviceId = eUserDeviceId;
        iParentContentLayout = eParentContentLayout;
        statesList = new ArrayList<>();

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.CALENDAR_ENVELOPE.getHtml() + "  " + "Перечень оповещений");
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

        iNotificationTable = new Table();
        iNotificationTable.setWidth("100%");

        iNotificationTable.setColumnHeader(1, "№");
        iNotificationTable.setColumnHeader(2, "Условие<br/>оповещения");
        iNotificationTable.setColumnHeader(3, "Критерий<br/>оповещения");
        iNotificationTable.setColumnHeader(4, "Δt, с");
        iNotificationTable.setColumnHeader(5, "Системы<br/>оповещения");

        iNotificationContainer = new IndexedContainer();
        iNotificationContainer.addContainerProperty(1, Integer.class, null);
        iNotificationContainer.addContainerProperty(2, tNotificationConditionSelect.class, null);
        iNotificationContainer.addContainerProperty(3, tNotificationCriteriaField.class, null);
        iNotificationContainer.addContainerProperty(4, TextField.class, null);
        iNotificationContainer.addContainerProperty(5, tNotificationListLayout.class, null);

        setNotificationContainer();

        iNotificationTable.setContainerDataSource(iNotificationContainer);


        iNotificationTable.setPageLength(iNotificationContainer.size());

        iNotificationTable.addStyleName(ValoTheme.TABLE_COMPACT);
        iNotificationTable.addStyleName(ValoTheme.TABLE_SMALL);
        iNotificationTable.addStyleName("TableRow");


        iNotificationTable.setSelectable(true);

        VerticalLayout NotificationTableLayout = new VerticalLayout(
                iNotificationTable
        );
        NotificationTableLayout.setWidth("100%");
        NotificationTableLayout.setHeightUndefined();
        NotificationTableLayout.setComponentAlignment(iNotificationTable,Alignment.MIDDLE_CENTER);

        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,NotificationTableLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);

    }

    public void NotificationContainerRefresh(){
        iNotificationContainer.removeAllItems();
        setNotificationContainer();
        iNotificationTable.setPageLength(iNotificationContainer.size());

    }

    public void setNotificationContainer(){
        try {
            Class.forName(commonFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    commonFuctions.DB_URL
                    , commonFuctions.USER
                    , commonFuctions.PASS
            );
            statesList.clear();

            String DataSql = "select @num1:=@num1+1 notification_condition_num\n" +
                    ",uas.actuator_state_name notification_condition_name\n" +
                    ",case when uas.actuator_message_code='INTERVAL' then\n" +
                    "concat(\n" +
                    "(\n" +
                    "select uasc.right_part_expression\n" +
                    "from user_actuator_state_condition uasc\n" +
                    "where uasc.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    "and uasc.condition_num=1\n" +
                    ")\n" +
                    ",'|',(\n" +
                    "select uasc.right_part_expression\n" +
                    "from user_actuator_state_condition uasc\n" +
                    "where uasc.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    "and uasc.condition_num=2\n" +
                    "),'|'\n" +
                    ")\n" +
                    "else (\n" +
                    "select uasc.right_part_expression\n" +
                    "from user_actuator_state_condition uasc\n" +
                    "where uasc.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    "and uasc.condition_num=1\n" +
                    ") end criteria_value\n" +
                    ",uas.transition_time\n" +
                    ",(\n" +
                    "select concat(group_concat(nt.notification_code separator '|'),'|')\n" +
                    "from user_device_state_notification uno\n" +
                    "join notification_type nt on nt.notification_type_id=uno.notification_type_id\n" +
                    "where uno.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    ") notification_codes\n" +
                    ",uas.user_actuator_state_id\n" +
                    "from user_actuator_state uas\n" +
                    "join (select @num1:=0) t\n" +
                    "where uas.user_device_id = ?";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);
            DataStmt.setInt(1,iUserDeviceId);

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {

                Item newItem = iNotificationContainer.addItem(DataRs.getInt(1));
                newItem.getItemProperty(1).setValue(DataRs.getInt(1));
                com.vaadin.demoContent.notificationContent.tNotificationCriteriaField creField = new tNotificationCriteriaField();
                creField.setFieldsValues(DataRs.getString(3));
                tNotificationConditionSelect condSel = new tNotificationConditionSelect(creField);
                condSel.select(DataRs.getString(2));
                condSel.setEnabled(false);
                newItem.getItemProperty(2).setValue(condSel);
                creField.valueFromField.setEnabled(false);
                creField.valueTillField.setEnabled(false);
                newItem.getItemProperty(3).setValue(creField);
                TextField timeInt = new TextField();
                timeInt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
                timeInt.setValue(String.valueOf(DataRs.getInt(4)));
                timeInt.setEnabled(false);
                timeInt.setWidth("50px");
                newItem.getItemProperty(4).setValue(timeInt);
                tNotificationListLayout noteListLay = new tNotificationListLayout();
                noteListLay.setEnabledFalse();
                for (String iNoteType : commonFuctions.GetListFromString(DataRs.getString(5),"|")) {
                    noteListLay.markNotification(iNoteType);
                }
                newItem.getItemProperty(5).setValue(noteListLay);

                statesList.add(new stateIdMap(DataRs.getInt(1),DataRs.getInt(6)));

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
