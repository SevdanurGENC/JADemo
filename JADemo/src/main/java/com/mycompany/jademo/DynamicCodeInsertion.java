package com.mycompany.jademo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nano
 */
public class DynamicCodeInsertion {

    public static DB db;
    public static DBCollection table;
    public static BasicDBObject document;
    //public static String packageName, className, methodName, filePath, fileName;
    public static ClassPool cp;
    public static CtClass cc;
    public static CtMethod m;
    public static CtMethod[] methods;

    public static String fileName = "DemoClass.java";
    public static String filePath = "C:\\Users\\Nano\\Documents\\NetBeansProjects\\JADemo\\src\\main\\java\\com\\mycompany\\jademo\\";
    public static String packageName = "com.mycompany.jademo";
    public static String className = "DemoClass";
    public static String methodName = "yaz";

    public DynamicCodeInsertion() {     //this method is constructor method for mongo db connection 
        //table = ConnectionDB.connIsExists("kayitlar"); 
        Mongo mongo = new Mongo("localhost", 27017);
        db = mongo.getDB("nanoTez");
        if (!db.collectionExists("kayitlar")) {
            db.createCollection("kayitlar", null);
        }
        table = db.getCollection("kayitlar");
        table.drop();
    }

    public static void main(String[] args) throws Exception {
        fileName = "DemoClass.java";
        filePath = "C:\\Users\\Nano\\Documents\\NetBeansProjects\\JADemo\\src\\main\\java\\com\\mycompany\\jademo\\";
        packageName = "com.mycompany.jademo";
        className = "DemoClass";
        methodName = "yaz";

        // js(packageName, className, methodName);
        File dosya = new File(filePath + "TempClass.java");
        if (!dosya.exists()) {
            //dosya.createNewFile();
            createClass(methodName);
            
        } else {
            System.out.println("Dosya mevcut");
        }

//        createClass(methodName);
//        js2(packageName, className, methodName);
//        final String[] args1 = {};
//        final Class<?> classa = Class.forName(packageName + ".TempClass");
//        final Method main = classa.getDeclaredMethod("main", String[].class);
//        main.invoke(null, new Object[]{args1});
//        deleteClass();
        //(new DemoClass()).helloWorld();
    }

    public static void js(String PackageName, String ClassName, String MethodName) throws NotFoundException, CannotCompileException {
        cp = ClassPool.getDefault();
        cc = cp.get(PackageName + "." + ClassName);

        methods = cc.getDeclaredMethods();
        for (CtMethod meth : methods) {
            System.out.println("Method Name : " + meth.getName() + " executed ");
            //meth.insertBefore(buffer.toString());

        }

    }

    public static void js2(String PackageName, String ClassName, String MethodName) throws NotFoundException, CannotCompileException {
        cp = ClassPool.getDefault();
        cc = cp.get(PackageName + "." + ClassName);
        cp.makePackage(cp.getClassLoader(), PackageName);
        m = cc.getDeclaredMethod(MethodName);

        m.insertBefore("System.out.println(\"-->> Method Name : " + m.getName() + " -->> Baslangic Zamani : \" + new java.util.Date());");
        m.insertAfter("System.out.println(\"-->> Method Name : " + m.getName() + " -->> Bitis Zamani : \" + new java.util.Date());");

        cc.toClass();
    }

    public static void createClass(String methodName) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

        try (FileOutputStream fos = new FileOutputStream(filePath + "TempClass.java")) {
            PrintStream ps = new PrintStream(fos);
            ps.println("package com.mycompany.jademo;");
            ps.println("public class TempClass {");
            ps.println("public static void main(String[] args) {\n");
            ps.println("(new " + className + "())." + methodName + "();");
            ps.println("}}\n");
            ps.close();
        }

        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        String javacOpts[] = {filePath + "TempClass.java"};
        if (javac.run(null, null, null, javacOpts) != 0) {
            throw new RuntimeException("compilation of TempClass.java Failed");
        }

        //  TempClass.main(null); kodu icin kullanilacak 4 satirlik kod; 
    }

    public static void deleteClass() {

        File fileJ = new File(filePath + "TempClass.java");
        File fileC = new File(filePath + "TempClass.class");

        if (fileJ.exists()) { //Dosyalar var mı
            if (fileJ.delete()) {
                System.out.println("FileJ deleted successfully");
            } else {
                System.out.println("Failed to delete the fileJ");
            }
        }

        if (fileC.exists()) { //Dosyalar var mı
            if (fileC.delete()) {
                System.out.println("FileC deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        }

    }

//    public void doOnStart(Object source, Object[] arg, String executionId) {
//
//        document = new BasicDBObject();
//        //System.currentTimeMillis();
//        try {
//            MethodBilgisiAl(fileName);
//        } catch (Exception ex) {
//            Logger.getLogger(DynamicCodeInsertion.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        ClassName = m.getDeclaringClass().getSimpleName();
//        MethodName = m.getName();
//        MethodReturnType = m.getReturnType().toString();
//        MethodParameters = Arrays.toString(m.getParameters());
//
//        document.put("Execution Id", "'" + executionId + "'");
//        document.put("Kaynak", "'" + source + "'");
//        document.put("Baslangic Suresi", "'" + new Date(start) + "'");
//        document.put("Degiskenler", "'" + toString(arg) + "'");
//
//        document.put("ClassName", "'" + ClassName + "'");
//        document.put("MethodName", "'" + MethodName + "'");
//        document.put("MethodReturnType", "'" + MethodReturnType + "'");
//        document.put("MethodParameters", "'" + MethodParameters + "'");
//
//        // objectName = ReadDataFromDB.objectName;
//        // objectClassName = ReadDataFromDB.objectClassName;
//        document.put("ObjectInClass_Field", "'" + ObjectInClass_Field + "'");
//        document.put("ObjectClassName_Method", "'" + ObjectClassName_Method + "'");
//        document.put("Mocking", "'" + Mocking + "'");
//        document.put("ImportMethod", "'" + ImportMethod + "'");
//        document.put("ImportField", "'" + ImportField + "'");
//
//       
//        _arg = arg;
//
//        table.insert(document);
//
//    }
//
//       private File getFile(Object source, String executionId) {
//        String loggingFolderPath = "JVM-" + ManagementFactory.getRuntimeMXBean().getStartTime() + "/" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId() + "/" + executionId + "-";
//        if (source instanceof Method) {
//            Method m = (Method) source;
//            loggingFolderPath += m.getDeclaringClass().getName() + "." + m.getName() + "()";
//        } else if (source instanceof Constructor) {
//            Constructor c = (Constructor) source;
//            String className = c.getDeclaringClass().getName();
//            if (className != null && className.length() > 0) {
//                loggingFolderPath += className + ".init()";
//            } else {
//                loggingFolderPath += "init()";
//            }
//        } else {
//            loggingFolderPath += source;
//        }
//
//        loggingFolderPath += ".log";
//        loggingFolderPath = loggingFolderPath.replaceAll("[<>:]", "-");
//        File ret = new File(rootFile, loggingFolderPath);
//        try {
//            FileUtils.forceMkdir(ret.getParentFile());
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        return ret;
//    }
//    
//    
//    public void MethodBilgisiAl(String fName) throws FileNotFoundException, IOException, Exception {
//
//        ByteCodeEditor.Main(fName);
//        FindObjectsInClasses.Main(fName);
//    }
//
}
