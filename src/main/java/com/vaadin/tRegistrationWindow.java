package com.vaadin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.stream.Collectors;

/**
 * Created by kalistrat on 12.03.2018.
 */
public class tRegistrationWindow extends Window {

    TextField LoginField;
    PasswordField PassWordField;
    PasswordField ConfirmPassWordField;
    TextField PhoneTextField;
    TextField MailTextField;


    Button BeginRegistraionButton;
    Button CancelButton;
    Button ConfirmButton;
    FormLayout regParamLayout;

    tCaptchaLayout captchaLayout;
    TextField ActivationPostCode;
    HorizontalLayout ButtonsLayout;

    Integer mailCaptchaRes = null;

    public tRegistrationWindow(){


        this.setIcon(FontAwesome.USER_PLUS);
        this.setCaption(" Добавление нового пользователя");

        ActivationPostCode = new TextField("Код активации :");
        ActivationPostCode.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        ActivationPostCode.setValue("");
        ActivationPostCode.setNullRepresentation("");
        ActivationPostCode.setInputPrompt("Результат арифметического выражения из письма");
        //ActivationPostCode.setWidth("600px");


        ConfirmButton = new Button("Подтвердить");

        ConfirmButton.setData(this);
        ConfirmButton.addStyleName(ValoTheme.BUTTON_SMALL);
        ConfirmButton.addStyleName(ValoTheme.BUTTON_LINK);
        ConfirmButton.setIcon(VaadinIcons.USER_CHECK);
        ConfirmButton.setData(this);

        ConfirmButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                String sErrorMessage = "";
                String sInputValue = ActivationPostCode.getValue();
                Integer parseResult = null;


                if (!sInputValue.equals("")){

                    if (tUsefulFuctions.StrToIntValue(sInputValue)!= null) {

                        parseResult = Integer.parseInt(sInputValue);

                        if (parseResult.intValue() != mailCaptchaRes) {
                            sErrorMessage = sErrorMessage + "Введённый код не соответствует высланному выражению\n";
                        }

                    } else {
                        sErrorMessage = sErrorMessage + "Введёно не числовое значение\n";
                    }

                } else {
                    sErrorMessage = sErrorMessage + "Код не задан\n";
                }


                if (!sErrorMessage.equals("")){
                    Notification.show("Ошибка активации профиля:",
                            sErrorMessage,
                            Notification.Type.TRAY_NOTIFICATION);
                } else {


                    String sUser = LoginField.getValue();
                    String sPass = PassWordField.getValue();
                    String sMail = MailTextField.getValue();
                    String sPhone = PhoneTextField.getValue();

                    String userWsUrl = faddNewUser(sUser,sPass,sMail,sPhone);

                    String wsRes = callUserWsToAddNewUser(
                            sUser
                            ,tUsefulFuctions.sha256(sPass)
                            ,sMail
                            ,sPhone
                            ,userWsUrl
                    );

                    if (wsRes != null) {
                        if (wsRes.equals("USER_WAS_ADDED")){
                            Notification.show("Новый пользователь добавлен!",
                                    null,
                                    Notification.Type.TRAY_NOTIFICATION);
                            UI.getCurrent().removeWindow((tRegistrationWindow) clickEvent.getButton().getData());
                        } else {
                            rollBackAddNewUser(sUser);
                            Notification.show("Ошибка сохранения!",
                                    "Проблема на стороне пользовательской базы данных",
                                    Notification.Type.TRAY_NOTIFICATION);
                            UI.getCurrent().removeWindow((tRegistrationWindow) clickEvent.getButton().getData());
                        }
                    } else {
                        rollBackAddNewUser(sUser);
                        Notification.show("Ошибка сохранения!",
                                "Пользовательский веб-сервис недоступен",
                                Notification.Type.TRAY_NOTIFICATION);
                        UI.getCurrent().removeWindow((tRegistrationWindow) clickEvent.getButton().getData());
                    }

                }


            }
        });


        LoginField = new TextField("Логин :");
        LoginField.setIcon(VaadinIcons.USER);
        LoginField.setNullRepresentation("");
        LoginField.setInputPrompt("Мнемоническое имя, содержащее латиницу и цифры от 7 до 50 символов (GlushkovVM1923)");

        PassWordField = new PasswordField("Пароль :");
        PassWordField.setIcon(VaadinIcons.KEY);
        ConfirmPassWordField = new PasswordField("Подтверждение пароля :");
        ConfirmPassWordField.setIcon(VaadinIcons.KEY_O);

        PhoneTextField = new TextField("Номер телефона :");
        PhoneTextField.setIcon(VaadinIcons.PHONE);
        PhoneTextField.setNullRepresentation("");
        PhoneTextField.setInputPrompt("Номер телефона 11 символов (79160000000)");

        MailTextField = new TextField("Адрес электронной почты :");
        MailTextField.setIcon(VaadinIcons.ENVELOPE);
        MailTextField.setNullRepresentation("");
        MailTextField.setInputPrompt("до 150 символов (ivanov@tj.ru)");


        BeginRegistraionButton = new Button("Подтвердить почту");
        BeginRegistraionButton.setData(this);
        BeginRegistraionButton.addStyleName(ValoTheme.BUTTON_SMALL);
        BeginRegistraionButton.addStyleName(ValoTheme.BUTTON_LINK);
        BeginRegistraionButton.setIcon(FontAwesome.SEND_O);

        BeginRegistraionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String sErrorMessage = "";

                String sLog = LoginField.getValue();

                String sPswd = PassWordField.getValue();
                String scPswd = ConfirmPassWordField.getValue();
                String sPhone = PhoneTextField.getValue();
                String sMail = MailTextField.getValue();

                if (sLog.equals("")){
                    sErrorMessage = "Логин не задан\n";
                } else {

                    if (sLog.length() > 50) {
                        sErrorMessage = sErrorMessage + "Длина логина превышает 50 символов\n";
                    }

                    if (sLog.length() < 8) {
                        sErrorMessage = sErrorMessage + "Длина логина менее 8 символов\n";
                        //System.out.println("Здесь должна быть проверка логина на длину");
                    }

                    if (!tUsefulFuctions.IsLatinAndDigits(sLog)) {
                        sErrorMessage = sErrorMessage + "Логин должен состоять из латиницы и цифр\n";
                    }

                    if (tUsefulFuctions.isExistsUserLogin(sLog).intValue() == 1) {
                        sErrorMessage = sErrorMessage + "Указанный логин уже используется\n";
                    }

                }

                if (sPswd.equals("")){
                    sErrorMessage = sErrorMessage + "Пароль не задан\n";
                } else {

                    if (sPswd.length() > 150) {
                        sErrorMessage = sErrorMessage + "Длина пароля превышает 150 символов\n";
                    }

                    if (sPswd.length() < 8) {
                        sErrorMessage = sErrorMessage + "Длина пароля менее 8 символов\n";
                    }

                    if (!tUsefulFuctions.IsLatinAndDigits(sPswd)) {
                        sErrorMessage = sErrorMessage + "Пароль должен состоять из латиницы и цифр\n";
                    }

                    if (!sPswd.equals(scPswd)) {
                        sErrorMessage = sErrorMessage + "Пароль и его подтверждение не совпадают\n";
                    }

                }


                if (sMail.equals("")) {
                    sErrorMessage = sErrorMessage + "Адрес электронной почты не задан\n";
                } else {
                    if (!tUsefulFuctions.IsEmailName(sMail)) {
                        sErrorMessage = sErrorMessage + "Адрес электронной почты не соответствует указанному формату\n";
                    }

                    if (sMail.length() > 150) {
                        sErrorMessage = sErrorMessage + "Длина адреса электронной почты превышает 150 символов\n";
                    }

                    if (tUsefulFuctions.isExistsUserMail(sMail).intValue() == 1) {
                        sErrorMessage = sErrorMessage + "Указанная электронная почта уже используется\n";
                    }
                }

                Integer InptValue = tUsefulFuctions.StrToIntValue(captchaLayout.ResultTextField.getValue());

                if (InptValue == null) {
                    sErrorMessage = sErrorMessage + "Введён неверный результат проверочного выражения\n";
                } else {
                    if (InptValue.intValue()!=captchaLayout.captchaRes) {
                        sErrorMessage = sErrorMessage + "Введён неверный результат проверочного выражения\n";
                    }
                }

                if (!sErrorMessage.equals("")){
                    Notification.show("Ошибка сохранения:",
                            sErrorMessage,
                            Notification.Type.TRAY_NOTIFICATION);
                } else {

                    if (sendEmail(sMail,sLog)) {
                        Notification.show("Письмо для подтверждения отправлено.",
                                "Заполните поле, соответствующее коду активации " + "\n"
                                        +" выражения из письма и нажмите кнопку подтверждения",
                                Notification.Type.TRAY_NOTIFICATION);
                        regParamLayout.addComponent(ActivationPostCode);
                        LoginField.setEnabled(false);
                        PassWordField.setEnabled(false);
                        ConfirmPassWordField.setEnabled(false);
                        PhoneTextField.setEnabled(false);
                        MailTextField.setEnabled(false);
                        ButtonsLayout.removeAllComponents();
                        ButtonsLayout.addComponent(ConfirmButton);
                    }

                }
            }
        });

        CancelButton = new Button("Отменить");
        CancelButton.setData(this);
        CancelButton.addStyleName(ValoTheme.BUTTON_SMALL);
        CancelButton.addStyleName(ValoTheme.BUTTON_LINK);
        CancelButton.setIcon(FontAwesome.HAND_STOP_O);

        CancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().removeWindow((tRegistrationWindow) clickEvent.getButton().getData());
            }
        });

        ButtonsLayout = new HorizontalLayout(
                CancelButton
                ,BeginRegistraionButton
        );

        ButtonsLayout.setSizeUndefined();
        ButtonsLayout.setSpacing(true);

        regParamLayout = new FormLayout(
                LoginField
                ,PassWordField
                ,ConfirmPassWordField
                ,PhoneTextField
                ,MailTextField
        );

        regParamLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regParamLayout.setWidth("600px");
        regParamLayout.addStyleName("FormFont");
        regParamLayout.setMargin(false);

        captchaLayout = new tCaptchaLayout();

        VerticalLayout MessageLayout = new VerticalLayout(
                captchaLayout
                ,regParamLayout
        );
        MessageLayout.setSpacing(true);
        MessageLayout.setWidth("650px");
        MessageLayout.setHeightUndefined();
        MessageLayout.setMargin(new MarginInfo(true,false,true,false));
        MessageLayout.setComponentAlignment(regParamLayout, Alignment.MIDDLE_CENTER);
        MessageLayout.setComponentAlignment(captchaLayout, Alignment.MIDDLE_CENTER);
        MessageLayout.addStyleName(ValoTheme.LAYOUT_CARD);

        VerticalLayout WindowContentLayout = new VerticalLayout(
                MessageLayout
                ,ButtonsLayout
        );
        WindowContentLayout.setSizeUndefined();
        WindowContentLayout.setSpacing(true);
        WindowContentLayout.setMargin(true);
        WindowContentLayout.setComponentAlignment(ButtonsLayout, Alignment.BOTTOM_CENTER);

        this.setContent(WindowContentLayout);
        this.setSizeUndefined();
        this.setModal(true);

    }


    private boolean sendEmail(String to,String userLogin) {
        boolean isSent = false;
        try {

            tCaptchaImage sendedCaptchaImage = new tCaptchaImage();

            InputStream inputStream = sendedCaptchaImage.getStream();
            String from = "snslog@mail.ru";
            String subject = "Регистрация на snslog.ru";
            String text = "Введите результат арифметического выражения в окне активации на snslog.ru";
            String fileName = userLogin +".png";
            String mimeType = "image/png";

            SpringEmailService.send(from, to, subject, text, inputStream, fileName, mimeType);

            inputStream.close();

            mailCaptchaRes = sendedCaptchaImage.captchaRes;

            isSent = true;


        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Ошибка отправки письма, проверьте адрес электронной почты"
                    , Notification.Type.ERROR_MESSAGE
            );
            System.out.println("Ошибка отправки письма на :" + to);
        }
        return isSent;
    }

    private String faddNewUser(String iUserLog,String iUserPass, String iUserMail, String iUserPhone){
        String urlUserWs = null;
        try {

            Class.forName(com.vaadin.tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    com.vaadin.tUsefulFuctions.DB_URL
                    , com.vaadin.tUsefulFuctions.USER
                    , com.vaadin.tUsefulFuctions.PASS
            );

            String userPassSha256 = tUsefulFuctions.sha256(iUserPass);

            CallableStatement Stmt = Con.prepareCall("{? = call faddNewUser(?,?,?,?)}");
            Stmt.registerOutParameter(1, Types.VARCHAR);
            Stmt.setString(2, iUserLog);
            Stmt.setString(3, userPassSha256);
            Stmt.setString(4, iUserMail);
            Stmt.setString(5, iUserPhone);
            Stmt.execute();
            urlUserWs = Stmt.getString(1);
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        }
        return urlUserWs;
    }

    private void rollBackAddNewUser(String iUserLog){

        try {

            Class.forName(com.vaadin.tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    com.vaadin.tUsefulFuctions.DB_URL
                    , com.vaadin.tUsefulFuctions.USER
                    , com.vaadin.tUsefulFuctions.PASS
            );

            CallableStatement Stmt = Con.prepareCall("{call rollBackAddNewUser(?)}");
            Stmt.setString(1, iUserLog);
            Stmt.execute();
            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
       }
    }

    private String callUserWsToAddNewUser(
            String argUser
            ,String argPass
            ,String argMail
            ,String argPhone
            ,String argUserWsUrl
    ){
        String respXml = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(argUserWsUrl);
            post.setHeader("Content-Type", "text/xml");

            String argServLog = "userWeb045813";
            String argServPass = "BHUerg567Sdc";

            String reqBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <com:addNewUser>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg0>"+argUser+"</arg0>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg1>"+argPass+"</arg1>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg2>"+argMail+"</arg2>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg3>"+argPhone+"</arg3>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg4>"+argServLog+"</arg4>\n" +
                    "         <!--Optional:-->\n" +
                    "         <arg5>"+argServPass+"</arg5>\n" +
                    "      </com:addNewUser>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            StringEntity input = new StringEntity(reqBody, Charset.forName("UTF-8"));
            post.setEntity(input);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            Document resXml = tUsefulFuctions.loadXMLFromString(rd.lines().collect(Collectors.joining()));
            respXml = XPathFactory.newInstance().newXPath()
                    .compile("//return").evaluate(resXml);


        } catch (Exception e){
            e.printStackTrace();
        }
        return respXml;
    }
}
