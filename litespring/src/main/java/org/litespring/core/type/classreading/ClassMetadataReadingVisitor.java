package org.litespring.core.type.classreading;

import org.litespring.core.type.ClassMetadata;
import org.litespring.utils.ClassUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;

/**
 * 保存类的基本信息
 */
public class ClassMetadataReadingVisitor extends ClassVisitor implements ClassMetadata{

    private String className;

    private boolean isInterface;

    private boolean isAbstract;

    private boolean isFinal;

    private String superClassName;

    private String[] interfaces;

    public ClassMetadataReadingVisitor(){
        super(SpringAsmInfo.ASM_VERSION);
    }
    public void visit(int version,int access,String name, String signture,String superName,String[] interfaces){
        this.className = ClassUtils.convertResourcePathToClassName(name);
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
        if (superName != null){
            this.superClassName = ClassUtils.convertResourcePathToClassName(superName);
        }
        this.interfaces = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++){
            this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }
    }

    public String getClassName() {
        return className;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean hasSuperClass() {
        return (superClassName != null);
    }

    public String getSuperClassName() {
        return superClassName;
    }

    @Override
    public String[] getInterfaceNames() {
        return interfaces;
    }
}
