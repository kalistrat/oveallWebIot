package com.vaadin.demoContent.detectorContent;

import com.vaadin.*;
import com.vaadin.demoContent.diagramContent.DiagramLayout;
import com.vaadin.demoContent.tDescriptionLayout;
import com.vaadin.demoContent.tLastMeasureLayout;
import com.vaadin.demoContent.tMeasuresJournalLayout;
import com.vaadin.demoContent.tTreeContentLayout;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by kalistrat on 19.01.2017.
 */
public class tDetectorLayout extends VerticalLayout {

    Button tReturnParentFolderButton;
    Integer tCurrentLeafId;
    Label TopLabel;
    tTreeContentLayout tParentContentLayout;
    Button EditSubTreeNameButton;
    Button DeleteSubTreeButton;
    int iUserDeviceId;
    tDetectorFormLayout DeviceDataLayout;
    tDetectorUnitsLayout DeviceUnitsLayout;
    tDescriptionLayout DeviceDescription;
    DiagramLayout DeviceMeasuresLayout;
    tLastMeasureLayout DeviceLastMeasure;
    tMeasuresJournalLayout DeviceMeasureJournal;
    tDetectorNotificationLayout notificationDetectorLayout;

    public tDetectorLayout(int eUserDeviceId, String eLeafName, int eLeafId,tTreeContentLayout eParentContentLayout){

        this.tCurrentLeafId = eLeafId;
        this.tParentContentLayout = eParentContentLayout;
        iUserDeviceId = eUserDeviceId;

        TopLabel = new Label();
        TopLabel.setContentMode(ContentMode.HTML);


        TopLabel.setValue(FontAwesome.TACHOMETER.getHtml() + " " + eLeafName);
        TopLabel.addStyleName(ValoTheme.LABEL_COLORED);
        TopLabel.addStyleName(ValoTheme.LABEL_SMALL);
        TopLabel.addStyleName("TopLabel");

        DeviceDataLayout = new tDetectorFormLayout(iUserDeviceId,tParentContentLayout.iUserLog);
        DeviceUnitsLayout = new tDetectorUnitsLayout(iUserDeviceId);


        tReturnParentFolderButton = new Button("Вверх");
        tReturnParentFolderButton.setIcon(FontAwesome.LEVEL_UP);
        tReturnParentFolderButton.addStyleName(ValoTheme.BUTTON_SMALL);
        tReturnParentFolderButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        tReturnParentFolderButton.addStyleName("TopButton");

        tReturnParentFolderButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Integer iParentLeafId = tParentContentLayout.GetParentLeafById(tCurrentLeafId);
                //System.out.println("tCurrentLeafId: " + tCurrentLeafId);
                //System.out.println("iParentLeafId: " + iParentLeafId);
                if (iParentLeafId != 0){
                    tParentContentLayout.tTreeContentLayoutRefresh(iParentLeafId,0);
                }
            }
        });

        EditSubTreeNameButton = new Button();
        EditSubTreeNameButton.setIcon(VaadinIcons.EDIT);
        EditSubTreeNameButton.addStyleName(ValoTheme.BUTTON_SMALL);
        EditSubTreeNameButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        DeleteSubTreeButton = new Button("Удалить");
        DeleteSubTreeButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
        DeleteSubTreeButton.addStyleName(ValoTheme.BUTTON_SMALL);
        DeleteSubTreeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        DeleteSubTreeButton.addStyleName("TopButton");


        HorizontalLayout DetectorEditLayout = new HorizontalLayout(
                DeleteSubTreeButton
                ,tReturnParentFolderButton
        );
        DetectorEditLayout.setSizeUndefined();

        HorizontalLayout LabelEditLayout = new HorizontalLayout(
                TopLabel
                ,EditSubTreeNameButton
        );
        LabelEditLayout.setSizeUndefined();
        LabelEditLayout.setSpacing(true);

        HorizontalLayout TopLayout = new HorizontalLayout(
                LabelEditLayout
                ,DetectorEditLayout
        );

        TopLayout.setComponentAlignment(LabelEditLayout,Alignment.MIDDLE_LEFT);
        TopLayout.setComponentAlignment(DetectorEditLayout,Alignment.MIDDLE_RIGHT);

        TopLayout.setSizeFull();
        TopLayout.setMargin(new MarginInfo(false, true, false, true));


        DeviceDescription = new tDescriptionLayout(iUserDeviceId);

        DeviceLastMeasure = new tLastMeasureLayout(iUserDeviceId,"DETECTOR");

        DeviceMeasureJournal = new tMeasuresJournalLayout(iUserDeviceId,"DETECTOR");

        notificationDetectorLayout = new tDetectorNotificationLayout(iUserDeviceId,tParentContentLayout);

        getUserDetectorData(
                iUserDeviceId
                ,DeviceDataLayout
                //,DeviceDescription
                ,DeviceUnitsLayout
        );

        DeviceMeasuresLayout = new DiagramLayout(
                iUserDeviceId
                ,"DETECTOR"
                ,(String) DeviceDataLayout.ArrivedDataTypeSelect.getValue()
        );

        VerticalLayout ContentPrefLayout = new VerticalLayout(
                DeviceDataLayout
                ,DeviceUnitsLayout
                ,notificationDetectorLayout
                ,DeviceDescription
        );

        VerticalLayout ContentMeasureLayout = new VerticalLayout(
                DeviceLastMeasure
                ,DeviceMeasuresLayout
                ,DeviceMeasureJournal
        );

        ContentPrefLayout.setMargin(true);
        ContentPrefLayout.setSpacing(true);
        ContentPrefLayout.setWidth("100%");
        ContentPrefLayout.setHeightUndefined();

        ContentMeasureLayout.setMargin(true);
        ContentMeasureLayout.setSpacing(true);
        ContentMeasureLayout.setWidth("100%");
        ContentMeasureLayout.setHeightUndefined();

        TabSheet DetectorTabSheet = new TabSheet();
        DetectorTabSheet.addTab(ContentMeasureLayout, "Показания датчика", VaadinIcons.CHART,0);
        DetectorTabSheet.addTab(ContentPrefLayout, "Настройки датчика", VaadinIcons.COGS,1);
        DetectorTabSheet.addStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
        DetectorTabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        DetectorTabSheet.setSizeFull();
        DetectorTabSheet.addStyleName("TabSheetSmall");

        DetectorTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent selectedTabChangeEvent) {
//                Component c = DetectorTabSheet.getSelectedTab();
//                TabSheet.Tab tb = DetectorTabSheet.getTab(c);
//                String capt = tb.getCaption();
//                //System.out.println("Selected TabCaption :" + capt);
//                if (capt.equals("Показания датчика")) {
//                    DeviceMeasuresLayout.reDrawGraphByPeriod((String) DeviceMeasuresLayout.tPeriodCB.getValue());
//                }

            }
        });

        VerticalLayout ContentLayout = new VerticalLayout(
                DetectorTabSheet
        );

        ContentLayout.setMargin(false);
        ContentLayout.setSpacing(true);
        ContentLayout.setSizeFull();
        //ContentLayout.setHeightUndefined();
        //ContentLayout.addStyleName(ValoTheme.LAYOUT_CARD);

        VerticalSplitPanel SplPanel = new VerticalSplitPanel();
        SplPanel.setFirstComponent(TopLayout);
        SplPanel.setSecondComponent(ContentLayout);
        SplPanel.setSplitPosition(40, Unit.PIXELS);
        SplPanel.setMaxSplitPosition(40, Unit.PIXELS);
        SplPanel.setMinSplitPosition(40,Unit.PIXELS);

        SplPanel.setHeight("1200px");
        //SplPanel.setWidth("1000px");

        this.addComponent(SplPanel);
        this.setSpacing(true);
        this.setHeight("100%");
        this.setWidth("100%");


    }

    private void getUserDetectorData(
            int qUserDeviceId
            ,tDetectorFormLayout qParamsForm
            //,tDescriptionLayout qDescriptionForm
            ,tDetectorUnitsLayout qUnitsForm
    ){

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");


        try {
            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            String DataSql = "select ud.device_user_name\n" +
                    ",ud.user_device_measure_period\n" +
                    ",ud.user_device_date_from\n" +
                    ",ud.device_units\n" +
                    ",ud.mqtt_topic_write\n" +
                    ",ser.server_ip mqqtt\n" +
                    ",ud.description\n" +
                    ",concat(un.unit_name,concat(' : ',un.unit_symbol))\n" +
                    ",uf.factor_value\n" +
                    ",ifnull(ud.device_log,'') device_log\n" +
                    ",ifnull(ud.device_pass,'') device_pass\n" +
                    ",ud.measure_data_type\n" +
                    "from user_device ud\n" +
                    "left join mqtt_servers ser on ser.server_id = ud.mqqt_server_id\n" +
                    "left join unit un on un.unit_id = ud.unit_id\n" +
                    "left join unit_factor uf on uf.factor_id = ud.factor_id\n" +
                    "where ud.user_device_id = ?";

            PreparedStatement DetectorDataStmt = Con.prepareStatement(DataSql);
            DetectorDataStmt.setInt(1,qUserDeviceId);

            ResultSet DetectorDataRs = DetectorDataStmt.executeQuery();

            while (DetectorDataRs.next()) {
                qParamsForm.NameTextField.setValue(DetectorDataRs.getString(1));
                qParamsForm.PeriodMeasureSelect.select(DetectorDataRs.getString(2));
                if (DetectorDataRs.getTimestamp(3) != null) {
                    qParamsForm.DetectorAddDate.setValue(df.format(new Date(DetectorDataRs.getTimestamp(3).getTime())));
                } else {
                    qParamsForm.DetectorAddDate.setValue("");
                }
                qUnitsForm.UnitTextField.setValue(DetectorDataRs.getString(4));
                qParamsForm.InTopicNameField.setValue(DetectorDataRs.getString(5));
                qUnitsForm.UnitSymbolSelect.select(DetectorDataRs.getString(8));
                qUnitsForm.UnitFactorSelect.select(DetectorDataRs.getString(9));
                qParamsForm.ArrivedDataTypeSelect.select(DetectorDataRs.getString(12));

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
