package com.vaadin.demoContent.actuatorContent;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
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
 * Created by kalistrat on 24.11.2017.
 */
public class tActuatorTaskLayout extends VerticalLayout {

    tActuatorStatesLayout statesLayout;
    Table taskTable;
    IndexedContainer taskTableContainer;
    int iUserDeviceId;
    tTreeContentLayout iParentContentLayout;

    Button AddButton;
    Button DeleteButton;
    Button SaveButton;

    class taskIdMap{
        int itemId;
        int taskId;
        public taskIdMap(int itId,int taId){
            itemId = itId;
            taskId = taId;
        }
    }
    List<taskIdMap> tasksList;

    public tActuatorTaskLayout(tActuatorStatesLayout iStatesLayout){
        statesLayout = iStatesLayout;
        iUserDeviceId = iStatesLayout.iUserDeviceId;
        tasksList = new ArrayList<>();
        iParentContentLayout = iStatesLayout.iParentContentLayout;

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.CALENDAR_CLOCK.getHtml() + "  " + "Назначенные задания");
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

        taskTable = new Table();
        taskTable.setWidth("100%");

        taskTable.setColumnHeader(1, "№");
        taskTable.setColumnHeader(2, "Наименование<br/>условия");
        taskTable.setColumnHeader(3, "Тип<br/>интервала");
        taskTable.setColumnHeader(4, "Значение<br/>интервала");

        taskTableContainer = new IndexedContainer();
        taskTableContainer.addContainerProperty(1, Integer.class, null);
        taskTableContainer.addContainerProperty(2, NativeSelect.class, null);
        taskTableContainer.addContainerProperty(3, NativeSelect.class, null);
        taskTableContainer.addContainerProperty(4, TextField.class, null);


        setTaskTableContainer();

        taskTable.setContainerDataSource(taskTableContainer);


        taskTable.setPageLength(taskTableContainer.size());



        taskTable.addStyleName(ValoTheme.TABLE_COMPACT);
        taskTable.addStyleName(ValoTheme.TABLE_SMALL);
        taskTable.addStyleName("TableRow");


        taskTable.setSelectable(true);

        VerticalLayout taskTableLayout = new VerticalLayout(
                taskTable
        );
        taskTableLayout.setWidth("100%");
        taskTableLayout.setHeightUndefined();
        taskTableLayout.setComponentAlignment(taskTable,Alignment.MIDDLE_CENTER);

        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,taskTableLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);
    }

    public void setTaskTableContainer(){
        try {
            Class.forName(commonFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    commonFuctions.DB_URL
                    , commonFuctions.USER
                    , commonFuctions.PASS
            );
            tasksList.clear();

            String DataSql = "select @num1:=@num1+1 num\n" +
                    ",uas.actuator_state_name\n" +
                    ",udta.interval_type\n" +
                    ",udta.task_interval\n" +
                    ",udta.user_device_task_id\n" +
                    "from user_device_task udta\n" +
                    "left join user_actuator_state uas on uas.user_actuator_state_id=udta.user_actuator_state_id\n" +
                    "join (select @num1:=0) t\n" +
                    "where udta.user_device_id=?";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);
            DataStmt.setInt(1,iUserDeviceId);

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {

                Item newItem = taskTableContainer.addItem(DataRs.getInt(1));

                newItem.getItemProperty(1).setValue(DataRs.getInt(1));

                NativeSelect conditionNameSelect = new NativeSelect();
                conditionNameSelect.setNullSelectionAllowed(false);
                conditionNameSelect.addItem(DataRs.getString(2));
                conditionNameSelect.select(DataRs.getString(2));

                newItem.getItemProperty(2).setValue(conditionNameSelect);

                NativeSelect timIntSelect = new NativeSelect();
                timIntSelect.addItem("секунда");
                timIntSelect.addItem("минута");
                timIntSelect.addItem("час");
                timIntSelect.addItem("сутки");
                timIntSelect.setNullSelectionAllowed(false);

                //System.out.println("setTaskTableContainer :DataRs.getString(2) : " + DataRs.getString(2));
                //System.out.println("setTaskTableContainer : iUserDeviceId : " + iUserDeviceId);

                if (DataRs.getString(3).equals("MINUTES")) {
                    timIntSelect.select("минута");
                } else if (DataRs.getString(3).equals("SECONDS")){
                    timIntSelect.select("секунда");
                } else if (DataRs.getString(3).equals("HOURS")){
                    timIntSelect.select("час");
                } else {
                    timIntSelect.select("сутки");
                }

                newItem.getItemProperty(3).setValue(timIntSelect);


                TextField timeInt = new TextField();
                timeInt.setWidth("50px");
                timeInt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
                timeInt.setValue(String.valueOf(DataRs.getInt(4)));
                timeInt.setInputPrompt("0");

                newItem.getItemProperty(4).setValue(timeInt);

                tasksList.add(new taskIdMap(DataRs.getInt(1),DataRs.getInt(5)));

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
