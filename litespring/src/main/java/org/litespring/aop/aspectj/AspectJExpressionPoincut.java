package org.litespring.aop.aspectj;

import org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException;
import org.aspectj.weaver.tools.*;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.utils.ClassUtils;
import org.litespring.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 面向切面表达式的类，对方法和表达式进行匹配，主要使用aspectjweaver提供的方法进行匹配
 */
public class AspectJExpressionPoincut implements Pointcut,MethodMatcher{

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    // 定义支持的原生切点函数
    static{
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    private String experssion;

    private PointcutExpression pointcutExpression;

    private ClassLoader pointcutClassLoader;

    public AspectJExpressionPoincut() {
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return experssion;
    }

    public void setExperssion(String experssion) {
        this.experssion = experssion;
    }

    /**
     * 对方法和表达式进行匹配
     * @param method 匹配的方法
     * @return 是否匹配上
     */
    @Override
    public boolean matches(Method method) {
        // 懒加载的模式来加载 pointcutExpression
        checkReadyToMatch();
        // 获取匹配后的实体
        ShadowMatch shadowMatch = getShadowMatch(method);

        if (shadowMatch.alwaysMatches()){
            return true;
        }
        return false;
    }

    private ShadowMatch getShadowMatch(Method method){
        ShadowMatch shadowMatch = null;
        try{
            // 表达式匹配核心
            shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        }catch (ReflectionWorldException ex){
            throw new RuntimeException("not implemented yet");
        }
        return shadowMatch;
    }

    /**
     * 懒加载的模式来加载 pointcutExpression
     */
    private void checkReadyToMatch() {
        if (getExpression() == null){
            throw new IllegalStateException("Must set property 'expression' before attempt to matches!");
        }
        if (this.pointcutExpression == null){
            this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }

    }

    /**
     * 通过支持的表达式 来创建pointcutExpression，
     * @param pointcutClassLoader
     * @return
     */
    private PointcutExpression buildPointcutExpression(ClassLoader pointcutClassLoader) {
        PointcutParser parser =
                PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES,pointcutClassLoader);

        return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()),
                null,new PointcutParameter[0]);
    }

    private String replaceBooleanOperators(String expression) {

        String result = StringUtils.replace(expression, "and"," && ");
        result = StringUtils.replace(expression, "or"," || ");
        result = StringUtils.replace(expression, "not"," ! ");

        return result;
    }
}
