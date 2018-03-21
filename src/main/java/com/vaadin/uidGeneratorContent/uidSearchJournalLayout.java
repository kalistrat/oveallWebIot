package com.vaadin.uidGeneratorContent;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.tUsefulFuctions;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by kalistrat on 13.03.2018.
 */
public class uidSearchJournalLayout extends VerticalLayout {
    Button searchButton;
    TextField searchTextField;
    Table uidJournalTable;
    IndexedContainer uidJournalContainer;
    int tableRowCnt = 10;

    public uidSearchJournalLayout(){

        Label Header = new Label();
        Header.setContentMode(ContentMode.HTML);
        Header.setValue(VaadinIcons.TABLE.getHtml() + "  " + "Перечень сгенерированных UID");
        Header.addStyleName(ValoTheme.LABEL_COLORED);
        Header.addStyleName(ValoTheme.LABEL_SMALL);

        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        searchButton.addStyleName(ValoTheme.BUTTON_SMALL);
        searchButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        searchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UidJournalContainerRefresh(searchTextField.getValue());
            }
        });

        searchTextField = new TextField();
        searchTextField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        searchTextField.setNullRepresentation("");
        searchTextField.setInputPrompt("Введите UID");

        HorizontalLayout HeaderButtons = new HorizontalLayout(
                searchTextField
                ,searchButton
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

        uidJournalTable = new Table();
        uidJournalTable.setWidth("100%");

        uidJournalTable.setColumnHeader(1, "UID");
        uidJournalTable.setColumnHeader(2, "Дата добавления UID");
        uidJournalTable.setColumnHeader(3, "Статус");
        uidJournalTable.setColumnHeader(4, "Пользователь");

        uidJournalContainer = new IndexedContainer();
        uidJournalContainer.addContainerProperty(1, String.class, null);
        uidJournalContainer.addContainerProperty(2, String.class, null);
        uidJournalContainer.addContainerProperty(3, String.class, null);
        uidJournalContainer.addContainerProperty(4, String.class, null);

        setUidJournalContainer(searchTextField.getValue());

        uidJournalTable.setContainerDataSource(uidJournalContainer);

        uidJournalTable.setPageLength(tableRowCnt);
        uidJournalTable.addStyleName(ValoTheme.TABLE_COMPACT);
        uidJournalTable.addStyleName(ValoTheme.TABLE_SMALL);
        uidJournalTable.addStyleName("TableRow");

        VerticalLayout TableLayout = new VerticalLayout(
                uidJournalTable
        );
        TableLayout.setWidth("100%");
        TableLayout.setHeightUndefined();
        TableLayout.setComponentAlignment(uidJournalTable,Alignment.MIDDLE_CENTER);

        VerticalLayout ContentLayout = new VerticalLayout(
                HeaderLayout
                ,TableLayout
        );
        ContentLayout.setSpacing(true);
        ContentLayout.setWidth("100%");
        ContentLayout.setHeightUndefined();

        this.addComponent(ContentLayout);
    }

    private void setUidJournalContainer(String eUID){

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        try {
            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );
            String DataSql;
            PreparedStatement DataStmt;

            if (eUID.equals("")) {
                DataSql = "select @num1:=@num1+1 num\n" +
                        ",sd.UID\n" +
                        ",sd.DATE_FROM\n" +
                        ",st.STATUS_NAME\n" +
                        ",tu.USER_LOGIN\n" +
                        "from sold_devices sd\n" +
                        "join device_status ds on ds.DEVICE_STATUS_ID=sd.DEVICE_STATUS_ID and ds.SOLD_DEVICE_ID=sd.SOLD_DEVICE_ID\n" +
                        "join status st on st.STATUS_ID=ds.STATUS_ID\n" +
                        "join (select @num1:=0) t1\n" +
                        "left join tj_users tu on tu.USER_ID=sd.USER_ID\n" +
                        "order by sd.DATE_FROM desc";
                DataStmt = Con.prepareStatement(DataSql);
            } else {
                DataSql = "select @num1:=@num1+1 num\n" +
                        ",sd.UID\n" +
                        ",sd.DATE_FROM\n" +
                        ",st.STATUS_NAME\n" +
                        ",tu.USER_LOGIN\n" +
                        "from sold_devices sd\n" +
                        "join device_status ds on ds.DEVICE_STATUS_ID=sd.DEVICE_STATUS_ID and ds.SOLD_DEVICE_ID=sd.SOLD_DEVICE_ID\n" +
                        "join status st on st.STATUS_ID=ds.STATUS_ID\n" +
                        "join (select @num1:=0) t1\n" +
                        "left join tj_users tu on tu.USER_ID=sd.USER_ID\n" +
                        "where sd.UID=?\n" +
                        "order by sd.DATE_FROM desc";
                DataStmt = Con.prepareStatement(DataSql);
                DataStmt.setString(1, eUID);
            }

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {
                DataRs.getString(1);

                Item newItem = uidJournalContainer.addItem(DataRs.getInt(1));

                newItem.getItemProperty(1).setValue(DataRs.getString(2));
                newItem.getItemProperty(2).setValue(df.format(new Date(DataRs.getTimestamp(3).getTime())));
                newItem.getItemProperty(3).setValue(DataRs.getString(4));
                newItem.getItemProperty(4).setValue(DataRs.getString(5));

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

    public void UidJournalContainerRefresh(String sUID){
        uidJournalContainer.removeAllItems();
        setUidJournalContainer(sUID);
        uidJournalTable.setPageLength(tableRowCnt);
    }

}
