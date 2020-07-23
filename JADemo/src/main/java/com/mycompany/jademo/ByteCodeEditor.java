package com.mycompany.jademo;

import java.io.File;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod; 
import javassist.bytecode.InstructionPrinter; 
import java.io.FileInputStream; 
import java.io.PrintStream;

/**
 *
 * @author Nano
 */
public class ByteCodeEditor {
 
    public static String filePath;
    public static ClassPool _classPool = ClassPool.getDefault();

    public static void Main(String FileName) throws Exception {
        filePath = "C:\\Users\\Nano\\Documents\\NetBeansProjects\\JADemo"
                + "\\target\\test-classes\\com\\mycompany\\jademo\\" + FileName + ".class";
         
        PrintStream o = new PrintStream(new File("C:\\Users\\Nano\\Documents\\NetBeansProjects\\JADemo"
                + "\\Temp\\" + FileName + "_ByteCode.txt"));  
        PrintStream console = System.out;  
        System.setOut(o);  

        ByteCodeEditor _byteCodeEditor = new ByteCodeEditor();
        CtClass _ctClass = _classPool.makeClass(new FileInputStream(filePath));
 
        _byteCodeEditor.printMethodCode(_ctClass); 
        
         System.setOut(console);
    }
    
    public static void printMethodCode(CtClass _ctClass) throws Exception {
        PrintStream ps = new PrintStream(System.out);
        InstructionPrinter instructionPrinter = new InstructionPrinter(ps);
        for (CtMethod method : _ctClass.getDeclaredMethods()) {
            System.out.println("MethodName : " + method.getName());
            instructionPrinter.print(method);
        }
        
    }
 

}
