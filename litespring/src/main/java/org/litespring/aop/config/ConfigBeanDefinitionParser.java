package org.litespring.aop.config;

import org.dom4j.Element;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.aspectj.AspectJExpressionPoincut;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.BeanDefinitionReaderUtils;
import org.litespring.beans.factory.support.BeanDefinitionRegister;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成advice和pointcut的BeanDefinition
 */
public class ConfigBeanDefinitionParser {

    private static final String ASPECT = "aspect";
    private static final String EXPRESSION = "expression";
    private static final String ID = "id";
    private static final String POINTCUT = "pointcut";
    private static final String POINTCUT_REF = "pointcut-ref";
    private static final String REF = "ref";
    private static final String BEFORE = "before";
    private static final String AFTER = "after";
    private static final String AFTER_RETURNING_ELEMENT = "after-returning";
    private static final String AFTER_THROWING_ELEMENT = "after-throwing";
    private static final String AROUND = "around";
    private static final String ASPECT_NAME_PROPERTY = "aspectName";

    public BeanDefinition parse(Element el, BeanDefinitionRegister register){
        List<Element> childEles = el.elements();
        for (Element element : childEles){
            String localName = element.getName();
            if (ASPECT.equals(localName)){
                parseAspect(element,register);
            }
        }
        return null;
    }

    /**
     *
     * @param aspectElement aspect的Element节点
     * @param register 实际时DefaultBeanFactory
     */
    private void parseAspect(Element aspectElement, BeanDefinitionRegister register) {
        String aspectId = aspectElement.attributeValue(ID);
        String aspectName = aspectElement.attributeValue(REF);

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        List<RuntimeBeanReference> beanReferences = new ArrayList<>();

        List<Element> eleList = aspectElement.elements();
        boolean adviceFoundAlready = false;
        for (int i = 0; i < eleList.size(); i++){
            Element ele = eleList.get(i);
            // 只对advice进行处理
            if (isAdviceNode(ele)){
                if (!adviceFoundAlready){
                    adviceFoundAlready = true;
                    if (!StringUtils.hasText(aspectName)){
                        return;
                    }
                    beanReferences.add(new RuntimeBeanReference(aspectName));
                }
                GenericBeanDefinition advisorDefinition =
                        parseAdvice(
                                aspectName,i,aspectElement,ele,register,beanDefinitions,beanReferences
                        );
                beanDefinitions.add(advisorDefinition);
            }
        }
        List<Element> pointcuts = aspectElement.elements(POINTCUT);
        for (Element ele : pointcuts){
            parsePointcut(ele,register);
        }

    }

    private GenericBeanDefinition parsePointcut(Element ele,BeanDefinitionRegister register) {
        String id = ele.attributeValue(ID);
        String expression = ele.attributeValue(EXPRESSION);

        GenericBeanDefinition pointcutDefinition;

        pointcutDefinition = createPointcutDefinition(expression);

        String pointcutBeanName = id;
        if (StringUtils.hasText(pointcutBeanName)){
            pointcutDefinition.setId(pointcutBeanName);
            register.registerBeanDefinition(pointcutDefinition);
        }else{
            BeanDefinitionReaderUtils.registerWithGeneratedName(pointcutDefinition,register);
        }

        return pointcutDefinition;
    }

    /**
     *
     * @param aspectName ref
     * @param order 第几个element
     * @param aspectElement aspect的Element节点
     * @param adviceElement
     * @param register factory
     * @param beanDefinitions 最外层方法new的list
     * @param beanReferences 最外层方法new的list
     * @return
     */
    private GenericBeanDefinition parseAdvice(
            String aspectName, int order, Element aspectElement, Element adviceElement, BeanDefinitionRegister register,
            List<BeanDefinition> beanDefinitions, List<RuntimeBeanReference> beanReferences) {

        // 方法对象的bd
        GenericBeanDefinition methodBeanDefinition = new GenericBeanDefinition(MethodLocatingFactory.class);
        methodBeanDefinition.getPropertyValues().add(new PropertyValue(aspectName,"targetBeanName"));
        methodBeanDefinition.getPropertyValues().add(new PropertyValue(adviceElement.attributeValue("method"),"methodName"));
        methodBeanDefinition.setSynthetic(true);

        // 拦截对象
        GenericBeanDefinition aspectFactoryDef = new GenericBeanDefinition(AspectInstanceFactory.class);
        aspectFactoryDef.getPropertyValues().add(new PropertyValue(aspectName,"aspectName"));
        aspectFactoryDef.setSynthetic(true);

        GenericBeanDefinition adviceDef = createAdviceDefinition(
                adviceElement,register,aspectName,order,methodBeanDefinition,aspectFactoryDef,
                beanDefinitions,beanReferences
        );
        adviceDef.setSynthetic(true);
        BeanDefinitionReaderUtils.registerWithGeneratedName(adviceDef,register);

        return adviceDef;
    }

    /**
     *
     * @param adviceElement advice的element节点
     * @param register
     * @param aspectName  ref
     * @param order
     * @param methodBeanDefinition 方法对象的bd
     * @param aspectFactoryDef 拦截对象的bd
     * @param beanDefinitions
     * @param beanReferences
     * @return
     */
    private GenericBeanDefinition createAdviceDefinition(
            Element adviceElement, BeanDefinitionRegister register, String aspectName, int order, GenericBeanDefinition methodBeanDefinition,
            GenericBeanDefinition aspectFactoryDef, List<BeanDefinition> beanDefinitions, List<RuntimeBeanReference> beanReferences) {

        GenericBeanDefinition adviceBeanDefinition = new GenericBeanDefinition(getAdviceClass(adviceElement));

        adviceBeanDefinition.getPropertyValues().add(new PropertyValue(aspectName,ASPECT_NAME_PROPERTY));

        // 设置 advice 构造参数
        ConstructorArgument cav = adviceBeanDefinition.getConstructorArgument();
        cav.addArgumentValue(methodBeanDefinition);

        Object poincut = parsePointcutProperty(adviceElement);
        if  (poincut instanceof BeanDefinition){
            cav.addArgumentValue(poincut);
            beanDefinitions.add((BeanDefinition) poincut);
        }else if (poincut instanceof String){
            RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) poincut);
            cav.addArgumentValue(pointcutRef);
            beanReferences.add(pointcutRef);
        }

        cav.addArgumentValue(aspectFactoryDef);
        return adviceBeanDefinition;
    }

    private Object parsePointcutProperty(Element adviceElement) {
        if  ((adviceElement.attribute(POINTCUT) == null) && (adviceElement.attribute(POINTCUT_REF) == null)){
            return null;
        }else if(adviceElement.attribute(POINTCUT) != null){
            String expression = adviceElement.attributeValue(POINTCUT);
            GenericBeanDefinition pointcutDefinition = createPointcutDefinition(expression);
            return pointcutDefinition;
        }else if(adviceElement.attribute(POINTCUT_REF)!= null){
            String pointcutRef = adviceElement.attributeValue(POINTCUT_REF);
            if (!StringUtils.hasText(pointcutRef)){
                return null;
            }
            return pointcutRef;
        }else {
            return null;
        }
    }

    private GenericBeanDefinition createPointcutDefinition(String expression) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition(AspectJExpressionPoincut.class);
        beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        beanDefinition.setSynthetic(true);
        beanDefinition.getPropertyValues().add(new PropertyValue(expression,EXPRESSION));
        return beanDefinition;
    }

    private boolean isAdviceNode(Element ele) {

        String name = ele.getName();
        return (BEFORE.equals(name) || AFTER.equals(name) || AFTER_RETURNING_ELEMENT.equals(name) ||
                AFTER_THROWING_ELEMENT.equals(name) || AROUND.equals(name));

    }

    private Class<?> getAdviceClass(Element adviceElement) {
        String elementName = adviceElement.getName();
        if (BEFORE.equals(elementName)) {
            return AspectJBeforeAdvice.class;
        }
		/*else if (AFTER.equals(elementName)) {
			return AspectJAfterAdvice.class;
		}*/
        else if (AFTER_RETURNING_ELEMENT.equals(elementName)) {
            return AspectJAfterReturningAdvice.class;
        }
        else if (AFTER_THROWING_ELEMENT.equals(elementName)) {
            return AspectJAfterThrowingAdvice.class;
        }
		/*else if (AROUND.equals(elementName)) {
			return AspectJAroundAdvice.class;
		}*/
        else {
            throw new IllegalArgumentException("Unknown advice kind [" + elementName + "].");
        }
    }
}
