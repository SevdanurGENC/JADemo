package com.mycompany.jademo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Nano
 */
public class FindObjectsInClasses {

    public static DBCollection table;
    public static BasicDBObject document, searchQuery;
    public static String MethodName, objectName1, objectClassName1, objectName2, objectClassName2,
            ObjectInClass_Field, ObjectClassName_Method, Mocking, ifSetVariable, setingMethodName;
    public static int executionId = 0;
 
    public static String filePath;
    public static int count = 0, countBuffer = 0, countLine = 0, startLine, finishLine;
    public static String inputSearch = "MethodName", line = "", line_ = "", line_iinc = "";
    public static BufferedReader br, briinc;
    public static ArrayList<Integer> lineNumber = new ArrayList<Integer>();
    public static HashMap<Integer, String> IfCollection = new HashMap<Integer, String>();
    public static List<bcList> ifMap = new ArrayList<bcList>();

    public static Object[] data;

    public static void Main(String FileName) throws FileNotFoundException, IOException {
        filePath = "C:\\Users\\Nano\\Documents\\NetBeansProjects\\JADemo"
                + "\\Temp\\" + FileName + "_ByteCode.txt";

        table = ConnectionDB.connIsExists("byteCoding"); 

        try {
            br = new BufferedReader(new FileReader(filePath));
            try {
                while ((line = br.readLine()) != null) {
                    countLine++;
                    //System.out.println(line);
                    IfCollection.put(countLine, line);

                    String[] words = line.split(" ");

                    for (String word : words) {
                        if (word.equals(inputSearch)) {
                            count++;
                            countBuffer++;
                        }
                    }

                    if (countBuffer > 0) {
                        countBuffer = 0;
                        lineNumber.add(countLine);
                    }

                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //System.out.println("Times found at : " + count);

        //for (int i = 0; i < lineNumber.size(); i++) {
        //System.out.println("Word found at : " + lineNumber.get(i));
        //}
        Search();

    }

    public static void Search() throws FileNotFoundException, IOException {

        //System.out.println("Total Line : " + countLine);
        for (int i = 0; i < lineNumber.size(); i++) {
            startLine = 0;
            finishLine = 0;
            if (!lineNumber.get(i).equals(lineNumber.get(lineNumber.size() - 1))) {
                startLine = lineNumber.get(i);
                finishLine = (lineNumber.get(i + 1) - 1) - (lineNumber.get(i) - 1);
            } else {
                startLine = lineNumber.get(i);
                finishLine = countLine;
            }
            executionId++;
            ListAndWriteToDB(startLine, finishLine, filePath, String.valueOf(executionId));
        }

    }

    public static void ListAndWriteToDB(int sl, int fl, String fpath, String executionId) {

        try {
            br = new BufferedReader(new FileReader(fpath));
            int linenumber = 1;

            while ((line_ = br.readLine()) != null) {
                document = new BasicDBObject();

                if (linenumber >= sl && linenumber < (sl + fl)) {
                    if (line_.contains("MethodName : ")) {   //Method isimlerini aliyor. 
                        MethodName = line_.substring(13, line_.length());

                        document.put("Execution Id", "'" + executionId + "'");
                        document.put("MethodName", "'" + MethodName + "'");

                        table.insert(document);

                    }

                    if (line_.contains(": invokevirtual #")) {
                        objectName1 = line_.substring(line_.indexOf("Method") + 7, line_.indexOf("("));
                        document.append("$set", new BasicDBObject().append("ObjectClassName_Method", convertToByteCodeMethod(objectName1)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                        document.append("$set", new BasicDBObject().append("Mocking", convertToByteCodeMocking(objectName1)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                    }

                    if (line_.contains(": getfield #")) {
                        objectClassName1 = line_.substring(line_.indexOf("Field") + 6, line_.indexOf("("));
                        document.append("$set", new BasicDBObject().append("ObjectInClass_Field", convertToByteCodeField(objectClassName1)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                    }

                    if (line_.contains(": invokestatic #")) {
                        objectName2 = line_.substring(line_.indexOf("Method") + 7, line_.indexOf("("));
                        document.append("$set", new BasicDBObject().append("ObjectClassName_Method", convertToByteCodeMethod(objectName2)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                        document.append("$set", new BasicDBObject().append("Mocking", convertToByteCodeMocking(objectName2)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                        document.append("$set", new BasicDBObject().append("ImportMethod", StringUtils.substringBeforeLast(objectName2, ".")));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                    }
                    // _" + linenumber + ""
                    if (line_.contains(": getstatic #")) {
                        objectClassName2 = line_.substring(line_.indexOf("Field") + 6, line_.indexOf("("));
                        document.append("$set", new BasicDBObject().append("ObjectInClass_Field", convertToByteCodeField(objectClassName2)));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                        document.append("$set", new BasicDBObject().append("ImportField", StringUtils.substringBeforeLast(objectClassName2, ".")));
                        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
                        table.update(searchQuery, document, true, false);
                    }

                    if (line_.contains(": ifge")) {   //value>=0 
                        ifMap.add(new bcList("ifge", Integer.parseInt(line_.substring(line_.indexOf("ifge") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable("<", linenumber);
                        writeToDBtoIf("ifge" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": ifle")) {   //value<=0 
                        ifMap.add(new bcList("ifle", Integer.parseInt(line_.substring(line_.indexOf("ifle") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable(">", linenumber);
                        writeToDBtoIf("ifle" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": iflt")) {   //value<0 
                        ifMap.add(new bcList("iflt", Integer.parseInt(line_.substring(line_.indexOf("iflt") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable(">=", linenumber);
                        writeToDBtoIf("iflt" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": ifgt")) {   //value>0  
                        ifMap.add(new bcList("ifgt", Integer.parseInt(line_.substring(line_.indexOf("ifgt") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable("<=", linenumber);
                        writeToDBtoIf("ifgt" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": ifeq")) {   //value=0       
                        ifMap.add(new bcList("ifeq", Integer.parseInt(line_.substring(line_.indexOf("ifeq") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable("!=", linenumber);
                        writeToDBtoIf("ifeq" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": ifne")) {   //value!=0 
                        ifMap.add(new bcList("ifne", Integer.parseInt(line_.substring(line_.indexOf("ifne") + 5, line_.length()))));
                        ifSetVariable = ifGetVariable("==", linenumber);
                        writeToDBtoIf("ifne" + linenumber, ifSetVariable);
                    }

                    if (line_.contains(": iinc")) {   //increment 
                        writeToDBtoLoop(MethodName);
                        int getIINCLineNumber = 0, firstLoopNumbet = 0, secondLoopNumber = 0;
                        briinc = new BufferedReader(new FileReader(fpath));
                        for (int i = 1; i <= countLine; i++) {
                            line_iinc = briinc.readLine();
                            if (line_iinc.contains("iinc")) {
                                getIINCLineNumber = i;
                            }
                            if (line_iinc.contains("if_icmp")) {
                                firstLoopNumbet = i;
                            }
                            if (line_iinc.contains("goto")) {
                                secondLoopNumber = i;
                            }
                        }
                        if ((getIINCLineNumber > firstLoopNumbet) && (getIINCLineNumber < secondLoopNumber)) {
                            setingMethodName = MethodName;

                        }
                        if (getIINCLineNumber < firstLoopNumbet) {
                            setingMethodName = MethodName;
                        }
                    }
                }
                linenumber++;

            }

            //if ((sl + fl) > linenumber) {
            //System.out.println("End of file reached.");
            //}
            br.close();

        } catch (Exception e) {
             System.out.println("Something went horribly wrong: " + e.getMessage());
        }
    }

    public static void writeToDBtoIf(String kind, String ifSetVariable) {
        document.append("$set", new BasicDBObject().append(kind + "Line", ifSetVariable));
        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
        table.update(searchQuery, document, true, false);
    }

    public static void writeToDBtoLoop(String mName) {
        document.append("$set", new BasicDBObject().append("Loop", mName));
        searchQuery = new BasicDBObject().append("Execution Id", "'" + executionId + "'");
        table.update(searchQuery, document, true, false);
    }

    public static String ifGetVariable(Object sign, int linenumber) {

        String getVariable = IfCollection.get(linenumber - 2);
        getVariable = getVariable.substring(getVariable.indexOf("int") + 4, getVariable.length());
        if (getVariable.equals("dconst_1")) {
            getVariable = "1.0";
        }
        if (getVariable.equals("dconst_0")) {
            getVariable = "0.0";
        }

        String setVariable = sign + " " + getVariable;
        return setVariable;
    }

    public static String convertToByteCodeField(String getField) {
        int lastDot = getField.lastIndexOf(".");
        ObjectInClass_Field = getField.substring(lastDot + 1, getField.length());
        return ObjectInClass_Field;
    }

    public static String convertToByteCodeMethod(String getMethod) {

        String start = StringUtils.substringBeforeLast(getMethod, ".");
        ObjectClassName_Method = StringUtils.substringAfterLast(start, ".");
        return ObjectClassName_Method;
    }

    public static String convertToByteCodeMocking(String getMocking) {

        int lastDot = getMocking.lastIndexOf(".");
        String SetMocking = getMocking.substring(lastDot + 1, getMocking.length());
        String start = StringUtils.substringBeforeLast(getMocking, ".");
        ObjectClassName_Method = StringUtils.substringAfterLast(start, ".");

        Mocking = ObjectClassName_Method + "." + SetMocking;

        return Mocking;
    }

}
