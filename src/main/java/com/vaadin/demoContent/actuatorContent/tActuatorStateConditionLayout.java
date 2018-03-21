package com.vaadin.demoContent.actuatorContent;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.commonFuctions;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;


/**
 * Created by kalistrat on 26.05.2017.
 */
public class tActuatorStateConditionLayout extends VerticalLayout {

    Button AddButton;
    Button DeleteButton;
    Button SaveButton;

    TreeTable StatesConditionTable;
    HierarchicalContainer StatesConditionContainer;
    int iUserDeviceId;
    tActuatorStatesLayout iActuatorStatesLayout;
    Integer iCurrentLeafId;


    public tActuatorStateConditionLayout(int eUserDeviceId
                ,tActuatorStatesLayout ActuatorStatesLayout
                ,Integer eCurrentLeafId
    ){

        iUserDeviceId = eUserDeviceId;
        iActuatorStatesLayout = ActuatorStatesLayout;
        iCurrentLeafId = eCurrentLeafId;

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.TREE_TABLE.getHtml() + "  " + "Условия, реализующие состояния устройства");
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

        StatesConditionTable = new TreeTable();
        StatesConditionTable.setWidth("100%");

        StatesConditionTable.setColumnHeader(1, "Наименование<br/>состояния");
        StatesConditionTable.setColumnHeader(2, "Компоненты условия");

        StatesConditionContainer = new HierarchicalContainer();

        StatesConditionContainer.addContainerProperty(1, String.class, null);
        StatesConditionContainer.addContainerProperty(2, VerticalLayout.class, null);

        StatesConditionTable.setColumnWidth(1,-1);
        StatesConditionTable.setColumnWidth(2,-1);

        setStatesConditionContainer();

        StatesConditionTable.setContainerDataSource(StatesConditionContainer);

        // Expand the tree
        for (Object itemId: StatesConditionTable.getContainerDataSource()
                .getItemIds()) {
            StatesConditionTable.setCollapsed(itemId, false);
            // Also disallow expanding leaves
            if (! StatesConditionTable.hasChildren(itemId))
                StatesConditionTable.setChildrenAllowed(itemId, false);

        }

        StatesConditionTable.setPageLength(StatesConditionContainer.size());

        StatesConditionTable.addStyleName(ValoTheme.TREETABLE_SMALL);
        StatesConditionTable.addStyleName(ValoTheme.TREETABLE_COMPACT);
        StatesConditionTable.addStyleName("TableRow");


        StatesConditionTable.setSelectable(true);


        VerticalLayout TableLayout = new VerticalLayout(
                StatesConditionTable
        );
        TableLayout.setWidth("100%");
        TableLayout.setHeightUndefined();
        TableLayout.setComponentAlignment(StatesConditionTable,Alignment.MIDDLE_CENTER);

        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,TableLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);

    }

    public void setStatesConditionContainer(){

        try {
            Class.forName(commonFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    commonFuctions.DB_URL
                    , commonFuctions.USER
                    , commonFuctions.PASS
            );

            String DataSql = "select uas.actuator_state_name\n" +
                    ",ifnull(stc.actuator_state_condition_id,0)\n" +
                    ",stc.left_part_expression\n" +
                    ",stc.sign_expression\n" +
                    ",stc.right_part_expression\n" +
                    ",stc.condition_num\n" +
                    ",stc.condition_interval\n" +
                    "from user_actuator_state uas\n" +
                    "left join user_actuator_state_condition stc \n" +
                    "on stc.user_actuator_state_id=uas.user_actuator_state_id\n" +
                    "where uas.user_device_id = ?\n" +
                    "order by uas.user_actuator_state_id,stc.condition_num";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);
            DataStmt.setInt(1,iUserDeviceId);

            ResultSet DataRs = DataStmt.executeQuery();

            int k = 0;

            while (DataRs.next()) {

                if (DataRs.getInt(2)!= 0) {

                    k = k + 6;//Remove TimeInterval

                    Item HeaderItem = StatesConditionContainer.addItem(k - 5);
                    HeaderItem.getItemProperty(1).setValue(DataRs.getString(1));
                    HeaderItem.getItemProperty(2).setValue(null);

                    Item SubHeaderItem = StatesConditionContainer.addItem(k - 4);
                    SubHeaderItem.getItemProperty(1).setValue("Условие № " + DataRs.getString(6));
                    SubHeaderItem.getItemProperty(2).setValue(null);
                    StatesConditionContainer.setParent(k - 4, k - 5);

                    Item LeftSideItem = StatesConditionContainer.addItem(k - 3);
                    tButtonTextFieldLayout LeftSideFieldLayout = new tButtonTextFieldLayout(DataRs.getString(3),false);
                    LeftSideItem.getItemProperty(1).setValue("Левая часть выражения");
                    LeftSideItem.getItemProperty(2).setValue(LeftSideFieldLayout);
                    StatesConditionContainer.setParent(k - 3, k - 4);

                    Item SignItem = StatesConditionContainer.addItem(k - 2);
                    SignItem.getItemProperty(1).setValue("Знак выражения");
                    SignItem.getItemProperty(2).setValue(new tSignConditionLayout(DataRs.getString(4),false));
                    StatesConditionContainer.setParent(k - 2, k - 4);

                    Item RightSideItem = StatesConditionContainer.addItem(k - 1);
                    tButtonTextFieldLayout RightSideFieldLayout = new tButtonTextFieldLayout(DataRs.getString(5),false);
                    RightSideItem.getItemProperty(1).setValue("Правая часть выражения");
                    RightSideItem.getItemProperty(2).setValue(RightSideFieldLayout);
                    StatesConditionContainer.setParent(k - 1, k - 4);

                    Item VarsItem = StatesConditionContainer.addItem(k);
                    VarsItem.getItemProperty(1).setValue("Соответствие переменных");
                    VarsItem.getItemProperty(2).setValue(
                            new tVarConditionLayout(
                                    DataRs.getInt(2)
                                    , LeftSideFieldLayout.textfield
                                    , RightSideFieldLayout.textfield
                                    , iActuatorStatesLayout
                                    , false
                            )
                    );
                    StatesConditionContainer.setParent(k, k - 4);



                } else {
                    k = k + 1;
                    Item HeaderItem = StatesConditionContainer.addItem(k);
                    HeaderItem.getItemProperty(1).setValue(DataRs.getString(1));
                    HeaderItem.getItemProperty(2).setValue(null);

                }

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

    public void StateConditionTableRefresh(){
        StatesConditionContainer.removeAllItems();
        setStatesConditionContainer();
        // Expand the tree
        for (Object itemId: StatesConditionTable.getContainerDataSource()
                .getItemIds()) {
            StatesConditionTable.setCollapsed(itemId, false);

            if (! StatesConditionTable.hasChildren(itemId))
                StatesConditionTable.setChildrenAllowed(itemId, false);
        }

        StatesConditionTable.setPageLength(StatesConditionContainer.size());
    }


}
