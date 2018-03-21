package com.vaadin;

import com.vaadin.ui.NativeSelect;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kalistrat on 24.01.2017.
 */
public class tUsefulFuctions {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost/teljournal";
    public static final String USER = "kalistrat";
    public static final String PASS = "045813";

    public static List<String> GetListFromString(String DevidedString,String Devider){
        List<String> StrPieces = new ArrayList<String>();
        int k = 0;
        String iDevidedString = DevidedString;

        if (DevidedString.contains(Devider)) {

            while (!iDevidedString.equals("")) {
                int Pos = iDevidedString.indexOf(Devider);
                StrPieces.add(iDevidedString.substring(0, Pos));
                iDevidedString = iDevidedString.substring(Pos + 1);
                k = k + 1;
                if (k > 100000) {
                    iDevidedString = "";
                }
            }
        }

        return StrPieces;
    }

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }


    public static Double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber.replace(",", "."));
            } catch(Exception e) {
                return null;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return null;
    }

    public static void refreshUserTree(
            String qUserLog
    ){
        try {

            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            CallableStatement treeStmt = Con.prepareCall("{call p_refresh_user_tree(?)}");
            treeStmt.setString(1, qUserLog);
            treeStmt.execute();

            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }


    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static boolean IsLatinAndDigits(String stringCode){
        Pattern p1 = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m1 = p1.matcher(stringCode);
        //System.out.println("m.matches() :" + m.matches());
        //System.out.println("Matcher m :" + m);

        return m1.matches();
    }

    public static boolean IsDigits(String stringCode){
        Pattern p2 = Pattern.compile("^[0-9]+$");
        Matcher m2 = p2.matcher(stringCode);
        //System.out.println("m.matches() :" + m.matches());
        //System.out.println("Matcher m :" + m);

        return m2.matches();
    }

    public static boolean IsEmailName(String stringCode){
        Pattern p3 = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        Matcher m3 = p3.matcher(stringCode);
        //System.out.println("m.matches() :" + m.matches());
        //System.out.println("Matcher m :" + m);

        return m3.matches();
    }

    public static Integer StrToIntValue(String Sval) {

        try {
            //System.out.println(Sval);
            return Integer.parseInt(Sval);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int genRandInt(int mii,int mai){
        Random rnd = new Random(System.currentTimeMillis());
        int number = mii + rnd.nextInt(mai - mii + 1);
        rnd = null;
        System.gc();

        return  number;
    }

    public static String genSign() {
        Random rnds = new Random(System.currentTimeMillis());
        int SignNum = 1 + rnds.nextInt(3);
        rnds = null;
        System.gc();

        switch (SignNum) {
            case 1 : return "+";
            case 2 : return "-";
            case 3 : return "*";
            default: return  "+";
        }

    }

    public static Integer calculateAge(Date birthday)
    {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(birthday);
        // include day of birth
        dob.add(Calendar.DAY_OF_MONTH, -1);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    public static void setTimeZoneList(NativeSelect eListBox){

        try {
            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            String DataSql = "select tz.timezone_value\n" +
                    "from timezones tz";

            PreparedStatement DataStmt = Con.prepareStatement(DataSql);

            ResultSet DataRs = DataStmt.executeQuery();

            while (DataRs.next()) {
                eListBox.addItem(DataRs.getString(1));
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

    public static Integer isExistsContLogIn(String qLogIn){
        Integer isE = 0;
        try {

            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            CallableStatement callStmt = Con.prepareCall("{? = call fIsExistsContLogin(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.setString(2, qLogIn);
            callStmt.execute();

            isE =  callStmt.getInt(1);

            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return isE;
    }

    public static Integer isExistsUserMail(String qMailValue){
        Integer isE = 0;
        try {

            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            CallableStatement callStmt = Con.prepareCall("{? = call fisExistsUserMail(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.setString(2, qMailValue);
            callStmt.execute();

            isE =  callStmt.getInt(1);

            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return isE;
    }

    public static Integer isExistsUserLogin(String qLoginValue){
        Integer isE = 0;
        try {

            Class.forName(tUsefulFuctions.JDBC_DRIVER);
            Connection Con = DriverManager.getConnection(
                    tUsefulFuctions.DB_URL
                    , tUsefulFuctions.USER
                    , tUsefulFuctions.PASS
            );

            CallableStatement callStmt = Con.prepareCall("{? = call fisExistsUserLogin(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.setString(2, qLoginValue);
            callStmt.execute();

            isE =  callStmt.getInt(1);

            Con.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return isE;
    }


}
