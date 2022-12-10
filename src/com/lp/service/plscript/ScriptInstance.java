package com.lp.service.plscript;

import com.lp.server.stueckliste.ejbfac.BaseScriptTyped;

public class ScriptInstance {
    private Class c;
    private Object o;

    public ScriptInstance(Class c) throws IllegalAccessException, InstantiationException {
        this(c, c.newInstance());
    }

    public ScriptInstance(Class c, ScriptRuntime rt) throws IllegalAccessException, InstantiationException {
        this(c, c.newInstance());
        setRuntime(rt);
    }

    public ScriptInstance(Class c, Object instance) {
        this.c = c;
        this.o = instance;
    }

    public Class getClazz() {
        return c;
    }

    public BaseScriptTyped<?> getInstance() {
        return (BaseScriptTyped<?>)o;
    }

    public void setRuntime(ScriptRuntime rt) {
        ((BaseScript)getInstance()).setRuntime(rt);
    }
}
