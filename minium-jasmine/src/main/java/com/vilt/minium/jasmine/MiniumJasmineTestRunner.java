package com.vilt.minium.jasmine;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.json.JsonParser;
import org.mozilla.javascript.json.JsonParser.ParseException;
import org.mozilla.javascript.tools.shell.Global;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import be.klak.junit.jasmine.JasmineTestRunner;
import be.klak.rhino.RhinoContext;

import com.vilt.minium.script.MiniumContextLoader;

public class MiniumJasmineTestRunner extends JasmineTestRunner {

    private TestContextManager contextManager;

    public MiniumJasmineTestRunner(Class<?> testClass) throws IOException {
        super(testClass);
    }
    
    @Override
    protected Object createTestClassInstance() {
        try {
            Object testInstance = super.createTestClassInstance();
            contextManager.prepareTestInstance(testInstance);
            
            for (Field f : testClass.getDeclaredFields()) {
                f.setAccessible(true);
                JsVariable jsVariable = f.getAnnotation(JsVariable.class);
                if (jsVariable == null) continue;
                
                String varName = jsVariable.value();
                checkNotNull(varName, "@JsVariable.value() should not be null");
                Object val = getVal(jsVariable, f.get(testInstance));
                put(rhinoContext, varName, val);
            }
            
            return testInstance;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Object getVal(JsVariable jsVariable, Object object) throws ParseException, IOException {
        switch (jsVariable.resourceType()) {
        case NONE:
            return object;
        case JSON:
            if (object instanceof String) {
                return parseJson(rhinoContext, (String) object);
            }
            else if (object instanceof Resource) {
                return parseJson(rhinoContext, (Resource) object);
            }
        case STRING:
            if (object instanceof String) {
                return object;
            }
            else if (object instanceof Resource) {
                InputStream is = ((Resource) object).getInputStream();
                try {
                    return IOUtils.toString(is, Charsets.UTF_8.name());
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    protected void setUpJasmine(RhinoContext rhinoContext) {
        try {
            super.setUpJasmine(rhinoContext);
            rhinoContext.loadFromClasspath("js/lib/jasmine-override.js");
            
            Context context = rhinoContext.getJsContext();
            Scriptable scope = rhinoContext.getJsScope();

            if (scope instanceof Global) {
                List<String> modulesPath = Arrays.asList(suiteAnnotation.sourcesRootDir());
                ((Global) scope).installRequire(context, modulesPath, false);
            }
            
            MiniumContextLoader miniumContextLoader = new MiniumContextLoader(MiniumJasmineTestRunner.class.getClassLoader(), null);
            miniumContextLoader.load(context, scope);
            contextManager = new TestContextManager(testClass);
            contextManager.registerTestExecutionListeners(new DependencyInjectionTestExecutionListener());
        } catch (IOException e) {
            propagate(e);
        }    
    }

    protected void put(RhinoContext rhinoContext, String name, Object value) {
        Scriptable scope = rhinoContext.getJsScope();
        scope.put(name, scope, Context.javaToJS(value, scope));
    }
    
    protected void delete(RhinoContext rhinoContext, String name) {
        Scriptable scope = rhinoContext.getJsScope();
        scope.delete(name);
    }
    
    protected Object parseJson(RhinoContext rhinoContext, Resource resource) throws ParseException, IOException {
        String json = IOUtils.toString(resource.getInputStream());
        return new JsonParser(rhinoContext.getJsContext(), rhinoContext.getJsScope()).parseValue(json);
    }

    protected Object parseJson(RhinoContext rhinoContext, String json) throws ParseException {
        return new JsonParser(rhinoContext.getJsContext(), rhinoContext.getJsScope()).parseValue(json);
    }
}
