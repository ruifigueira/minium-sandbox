/*
 * Copyright (C) 2013 The Minium Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vilt.minium.jasmine;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Throwables.propagate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.json.JsonParser;
import org.mozilla.javascript.json.JsonParser.ParseException;
import org.mozilla.javascript.tools.shell.Global;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import be.klak.junit.jasmine.JasmineTestRunner;
import be.klak.rhino.RhinoContext;

import com.vilt.minium.script.MiniumContextLoader;

public class MiniumJasmineTestRunner extends JasmineTestRunner {

    private TestContextManager contextManager;
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public MiniumJasmineTestRunner(Class<?> testClass) throws IOException {
        super(testClass);
    }

    @Override
    protected Object createTestClassInstance() {
        try {
            final Object testInstance = super.createTestClassInstance();
            contextManager.prepareTestInstance(testInstance);

            ReflectionUtils.doWithFields(testClass, new FieldCallback() {

                @Override
                public void doWith(Field f) throws IllegalArgumentException, IllegalAccessException {
                    f.setAccessible(true);
                    JsVariable jsVariable = f.getAnnotation(JsVariable.class);
                    if (jsVariable == null) return;

                    String varName = jsVariable.value();
                    checkNotNull(varName, "@JsVariable.value() should not be null");
                    Object fieldVal = f.get(testInstance);
                    Object val = getVal(jsVariable, f.getType(), fieldVal);
                    put(rhinoContext, varName, val);

                    if (fieldVal == null && val != null) {
                        f.set(testInstance, val);
                    }
                }
            });

            return testInstance;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getVal(JsVariable jsVariable, Class<?> clazz, Object object) {
        try {
            if (StringUtils.isNotEmpty(jsVariable.resource())) {
                Resource resource = resourceLoader.getResource(jsVariable.resource());
                checkState(resource.exists() && resource.isReadable());

                if (clazz == String.class) {
                    InputStream is = resource.getInputStream();
                    try {
                        return IOUtils.toString(is, Charsets.UTF_8.name());
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                }
                else {
                    Object val = parseJson(rhinoContext, resource);
                    checkState(clazz.isAssignableFrom(val.getClass()));
                    return val;
                }
            }
            else {
                return object;
            }
        } catch (IOException e) {
            throw propagate(e);
        } catch (ParseException e) {
            throw propagate(e);
        }
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
